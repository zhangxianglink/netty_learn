package audio.v3;


import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import audio.v3.PyResult;

import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class JavaClient  extends WebSocketClient {

    public ConcurrentLinkedQueue<PyResult> getResults() {
        return results;
    }

    private ConcurrentLinkedQueue <PyResult> results = new ConcurrentLinkedQueue <>();
    private Gson gson = new Gson();
    public JavaClient(URI serverURI) {
        super(serverURI);
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


