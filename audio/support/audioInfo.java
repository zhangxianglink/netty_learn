package audio.support;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * x.z
 * Create in 2023/7/14
 */
public class audioInfo {



    // 根据请求路径，批量下载，或者批量发出请求
    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException, UnsupportedAudioFileException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("D:\\data\\8kt\\20088.wav"));
        AudioFormat format = ais.getFormat();
        byte[] result = new byte[(int) (ais.getFrameLength() * format.getFrameSize())];
        ais.read(result);
        ais.close();
        for (int i = 0; i < 1000; i++) {
            System.out.println(result[i]);
        }


        // 批量发送url
/*        Gson gson = new Gson();
        HashMap<String, Object> params = new HashMap<>();
        params.put("appkey","default");
        params.put("token","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWFuZ2RvbmciLCJpYXQiOjE2ODE5NzU3MzQsImV4cCI6MjAzMDgwMzIwMH0.wn1FMgemnqj5_jaBZ6nPrKpKGsva-UBUnXbO2-MDgCQ");
        params.put("enable_semantic_sentence_detection",true);
        params.put("enable_inverse_text_normalization",false);
        params.put("enable_callback",true);
        params.put("callback_url","http://192.168.6.102:10101/sdk/vad");
        params.put("open_ws",true);
        params.put("max_single_segment_time",500);
        params.put("enable_kafka_rule",false);
        params.put("enable_speed_rule",false);
        params.put("enable_db_rule",false);
        for (String httpUrl : lines) {
            params.put("file_link",httpUrl);
            String call_number = FilenameUtils.getName(httpUrl).replaceAll(".mp3", "").split("_")[1];
            params.put("task_id",call_number);
            String json = gson.toJson(params);
            System.out.println("入参：" + json);
            String postRequest = HttpUtils.postRequestByJson("http://192.168.6.102:9997/vad/asr", json);
            System.out.println("结果：" + postRequest);
        }*/

    }

}
