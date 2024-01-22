package audio.support;

import audio.ytils.HttpUtils;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/7/14
 */
public class audioInfo {

    // 根据请求路径，批量下载，或者批量发出请求
    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
       String[] arr = new String[] {"http://192.168.6.55:10000/data/823/953659319902900225_15323227224.mp3 "};
       // 批量下载文件
       List<String> lines = Arrays.asList(arr).stream().map(String::trim).collect(Collectors.toList());
/*         for(String httpUrl : lines) {
            // 创建一个信任所有证书的TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            // 创建一个SSLContext并设置信任所有证书
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // 设置默认的SSLContext
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL url = new URL(httpUrl);
            String name = FilenameUtils.getName(url.getPath());
            System.out.println("下载文件：" + name);
            FileUtils.copyURLToFile(url,new File("D:\\linuxupload\\"+name),100,10000);
        }*/


        // 批量发送url
        Gson gson = new Gson();
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
        }

    }

}
