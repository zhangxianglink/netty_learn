package audio.slice;

import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.K;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * x.z
 * Create in 2023/5/15
 */
public class Vad {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\linuxupload\\hot-rule.txt");
        List<String> list = Files.readAllLines(path);
        HashMap<String, String> hotRuleMap = new HashMap<>();
        list.forEach(e -> {
            String[] split = e.split("=");
            hotRuleMap.put(split[0], split[1]);
        });


        Collection<String> values = hotRuleMap.values();
        HashSet<String> strings = new HashSet<>(values);

        ArrayList<String> strList = new ArrayList<>();
        for (String s : strings) {
            if (hotRuleMap.keySet().contains(s)){
               strList.add(s);
            }
        }

        // key value 有重复
        HashMap<String, String> hashMap = new HashMap<>();

        HashSet<Object> objects = new HashSet<>();
        hotRuleMap.forEach((k, v) -> {
            if (strList.contains(v)){
                hashMap.put(k,v);
            }
            if (k.length() == 1){
                objects.add(k);
            }
        });
        System.out.println(new Gson().toJson(hashMap));
        System.out.println(objects);



    }
}
