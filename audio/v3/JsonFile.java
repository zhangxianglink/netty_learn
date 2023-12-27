package audio.v3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
public class JsonFile {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\linuxupload\\result.log");
        List<String> lines = Files.readAllLines(path);
        for (String line:
             lines) {
            streamVadFile(line);
        }
    }


    private static void streamVadFile(String line) throws IOException {
        String[] split = line.split("最后结果");
        String s = split[0];
        String[] split1 = s.split(".mp3");
        String name = split1[0].substring(split1[0].length() - 24);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(split[1].trim());
        String prettyJsonString = gson.toJson(je);

        String file= "D:\\data\\dudu\\" + name + ".txt";
        Files.write(Paths.get(file), Arrays.asList(prettyJsonString));
    }
}
