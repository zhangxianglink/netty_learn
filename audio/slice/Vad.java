package audio.slice;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

        Path path1 = Paths.get("D:\\linuxupload\\word_count.txt");
        byte[] bytes = Files.readAllBytes(path1);
        String str = new String(bytes, "UTF-8");
        HashMap hashMap = new Gson().fromJson(str, HashMap.class);


        List<Map.Entry<String, String>> listMap = new ArrayList<Map.Entry<String, String>>(hashMap.entrySet());
        //list.sort()
        listMap.sort(new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
               return Integer.valueOf(o2.getValue()).compareTo(Integer.valueOf(o1.getValue()));
            }
        });

//         被选中的热词排序
//        listMap.forEach(e -> {
//            System.out.println(e.getKey() + " " + e.getValue() + " " + hotRuleMap.get(e.getKey()));
//        });

        ArrayList<HotRule> hotRuleList = new ArrayList<>();
        hotRuleMap.forEach((k, v) -> {
            if (hashMap.get(k) == null) {
                hotRuleList.add(new HotRule(k, v));
            }
        });

        Map<String, List<HotRule>> collect = hotRuleList.stream().collect(Collectors.groupingBy(HotRule::getValue));
        collect.forEach((k, v) -> {
            if (v.size() == 1) {
                System.out.println(k + " " + v.size() + " " + v.stream().map(HotRule::getName).collect(Collectors.joining(",")) );
            }
        });

    }
}

class HotRule {
    private String name;
    private String value;

    public HotRule(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HotRule{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
