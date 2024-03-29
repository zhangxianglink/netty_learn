package net.nls;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    ConnectionListener listener;
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    public WebSocketClientHandler() {
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    // 在一个Handler被添加到ChannelPipeline后,它自己的handlerAdded()方法作为第一个被调用。
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("handler added channelid:{}" + ctx.channel().id());
        this.handshakeFuture = ctx.newPromise();
    }

    // handshaker 已经提供： 表示通道已激活,此时可以调用handshake()方法开始握手。
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channel active,id:{},{}"+ ctx.channel().id() + Thread.currentThread().getId());
    }

    //TODO 下游断开，同时关闭上游
    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx.channel() != null) {
            System.out.println("channelInactive:" + ctx.channel().id());
        } else {
            System.out.println("channelInactive");
        }
        // 判断WebSocket连接握手是否已经完成
        if (!this.handshaker.isHandshakeComplete()) {
            String errorMsg;
            if (ctx.channel() != null) {
                errorMsg = "channel inactive during handshake,connectionId:" + ctx.channel().id();
            } else {
                errorMsg = "channel inactive during handshake";
            }

            System.out.println(errorMsg);
            this.handshakeFuture.setFailure(new Exception(errorMsg));
        }

        if (this.listener != null) {
            this.listener.onClose(-1, "channelInactive");
        }

    }
    //TODO 处理asr返回的信息
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        FullHttpResponse response;
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                response = (FullHttpResponse)msg;
                // 处理握手响应完成握手,如果成功调用success()方法
                this.handshaker.finishHandshake(ch, response);
                this.handshakeFuture.setSuccess();
                System.out.println("WebSocket Client connected! response headers: " + response.headers());
            } catch (WebSocketHandshakeException var7) {
                FullHttpResponse res = (FullHttpResponse)msg;
                String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status(), res.content().toString(CharsetUtil.UTF_8));
                System.out.println(errorMsg);
                this.handshakeFuture.setFailure(new Exception(errorMsg));
            }

        } else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse)msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        } else {
            WebSocketFrame frame = (WebSocketFrame)msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
                this.listener.onMessage(textFrame.text());
            } else if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame)frame;
                this.listener.onMessage(binFrame.content().nioBuffer());
            } else if (frame instanceof PongWebSocketFrame) {
                System.out.println("WebSocket Client received pong");
            } else if (frame instanceof CloseWebSocketFrame) {
                System.out.println("receive close frame");
                this.listener.onClose(((CloseWebSocketFrame)frame).statusCode(), ((CloseWebSocketFrame)frame).reasonText());
                ch.close();
            }

        }
    }

//    捕获异常,打印错误日志
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!this.handshakeFuture.isDone()) {
            this.handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
