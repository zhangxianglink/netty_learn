package audio.support;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * x.z
 * Create in 2023/7/25
 */
public class JsonFile2 {


    // 根据返回的asr信息生成文件
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\linuxupload\\demo.log");
        List<String> lines = Files.readAllLines(path);
        for (String line: lines) {
            streamVadFile(line);
        }
    }


    private static void streamVadFile(String line) throws IOException {
        String[] split = line.split("获取回调的结果：");
        String s = split[1].trim();
        JsonObject returnData = new JsonParser().parse(s).getAsJsonObject();
        JsonObject jsonObject = returnData.getAsJsonObject("header");
        String task_id = jsonObject.get("task_id").getAsString();

        JsonObject payload = returnData.getAsJsonObject("payload");
        JsonArray sentences = payload.getAsJsonArray("sentences");
        ArrayList<String> arrayList = new ArrayList<>();
        sentences.forEach(sentence -> {
            JsonObject asJsonObject = sentence.getAsJsonObject();
            String channel_id = asJsonObject.get("channel_id").getAsString().equals("1") ? "坐席" : "客户";
            String text = asJsonObject.get("text").getAsString();
            arrayList.add(channel_id + "：" + text);
        });
        String file= "D:\\linuxupload\\Desktop\\" + task_id  + ".txt";
        Files.write(Paths.get(file), arrayList);
    }
}
