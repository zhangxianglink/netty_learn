package audio.v3;


import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JavaClient  extends WebSocketClient {


    private final ConcurrentLinkedQueue <PyResult> results = new ConcurrentLinkedQueue <>();
    private final Gson gson = new Gson();
    public JavaClient(URI serverURI) {
        super(serverURI);
    }
    public JavaClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("握手成功");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        PyResult pyResult = gson.fromJson(message, PyResult.class);
        results.add(pyResult);
        System.out.println(message);


    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("关闭连接");
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}


