package audio.support;

import audio.ytils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * x.z
 * Create in 2024/1/10
 */
public class runLast {
    // 线上机器离线asr队列文件，再执行
    private static  final  Gson gson = new Gson();
    public static void main(String[] args) throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\admin\\Desktop\\项目相关\\asr常用笔记\\55.txt")));
        Map map = gson.fromJson(fileContent, Map.class);
        List<Map<String, Object>> executeStreamQueue = (List<Map<String, Object>>) map.get("waitStreamQueue");
        System.out.println(executeStreamQueue.size());
        pushToAsrService(executeStreamQueue);
    }

    private static void pushToAsrService(List<Map<String, Object>> mapList) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appkey","default");
        params.put("token","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWFuZ2RvbmciLCJpYXQiOjE2ODE5NzU3MzQsImV4cCI6MjAzMDgwMzIwMH0.wn1FMgemnqj5_jaBZ6nPrKpKGsva-UBUnXbO2-MDgCQ");
        params.put("enable_semantic_sentence_detection",true);
        params.put("enable_inverse_text_normalization",true);
        params.put("enable_callback",true);
        params.put("callback_url","http://192.168.1.71:9100/callback/hd");
        params.put("open_ws",true);
        params.put("max_single_segment_time",500);
        params.put("enable_kafka_rule",true);
        params.put("enable_speed_rule",false);
        params.put("enable_db_rule",false);
        for (Map<String, Object> obj: mapList) {
            // 拼接参数， 发送到 http://192.168.2.77:9998/vad/asr
            params.put("file_link",obj.get("file_link"));
            params.put("task_id",obj.get("task_id"));
            String json = gson.toJson(params);
            System.out.println("入参：" + json);
            String postRequest = HttpUtils.postRequestByJson("http://192.168.2.77:9998/vad/asr", json);
            System.out.println("结果：" + postRequest);
        }
    }
}
