package audio.ytils;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * x.z
 * Create in 2023/5/8
 */
public class HttpUtils {



    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build();

    public static String postRequest(String url){
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request build = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response;
        try {
            response = client.newCall(build).execute();
            String result;
            if(response.body() != null){
                result = response.body().string();
            }else {
                result = "";
            }
            System.out.println("请求{} 返回结果：{}" + url + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String postRequestByJson(String url,String json){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request build = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response;
        try {
            response = client.newCall(build).execute();
            String result;
            if(response.body() != null){
                result = response.body().string();
            }else {
                result = "";
            }
            System.out.println("请求{} 返回结果：{}" + url + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
