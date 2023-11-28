package audio.ytils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * x.z
 * Create in 2023/8/29
 */
public class PinYinObj {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-rule_20231101-111051.txt"));
        lines.sort(Comparator.comparing(String::length));
        for (String l : lines) {
            String[] split = l.split("=");
            if (split[0].length() != split[1].length()){

            }
        }
    }
}
