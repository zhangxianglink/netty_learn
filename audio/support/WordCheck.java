package audio.support;

import com.google.gson.Gson;
import org.opencv.core.Mat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2024/1/31
 */
public class WordCheck {
    static Gson gson = new Gson();


    public static void main(String[] args) throws IOException {
        String path = "D:\\linuxupload\\热词31.txt";
        HashSet<String> sets = getSets(path);
        String path2 = "D:\\linuxupload\\热词30.txt";
        HashSet<String> sets2 = getSets(path2);
        sets.addAll(sets2);

        ArrayList<Message> list = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-rule.txt"));
        for (String l : lines){
            String[] split = l.split("=");
            if (!sets.contains(split[0])){
                list.add(new Message(split[0],split[1],l.length()+""));
            }
        }

       list.sort(Comparator.comparing(x -> Integer.valueOf(x.getType())));

        list.forEach(tuple2 -> System.out.println(tuple2.getTimeStr()+"="+tuple2.getNum()));
    }

    private static HashSet<String> getSets(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        HashSet<String> sets = new HashSet<>();
        String s = new String(bytes);
        Map map = gson.fromJson(s, Map.class);
        List<Map<String,String>> results = (List<Map<String, String>>) map.get("result");
        for (int i = 0; i < results.size(); i++) {
            Map<String, String> tmp = results.get(i);
            sets.add(tmp.get("left"));
        }
        return sets;
    }
}
