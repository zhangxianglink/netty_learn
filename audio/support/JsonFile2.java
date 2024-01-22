package audio.support;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(s);
        String prettyJsonString = gson.toJson(je);

        String file= "C:\\Users\\admin\\Desktop\\debug\\22\\" + task_id + ".txt";
        Files.write(Paths.get(file), Arrays.asList(prettyJsonString));
    }
}
