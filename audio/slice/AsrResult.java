package audio.slice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/5/8
 */
public class AsrResult {
    public static void main(String[] args) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get("C:\\Users\\admin\\Desktop\\6026\\28w.txt"));
        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\admin\\Desktop\\6026\\27w.txt"));

        Map<String, Integer> collect = allLines.stream().collect(Collectors.toMap(s -> s, s -> 0));

        for (String line : lines) {
            collect.put(line, 1);
        }

        collect.forEach((k, v) -> {
            if (v == 0) {
                System.out.println(k);
            }
        });
    }
}
