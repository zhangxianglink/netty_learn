package buider;

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * x.z
 * Create in 2023/5/30
 */
public class main {


    public static void main(String[] args) throws IOException {
        List<String> list = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-rule.txt"));
        list = list.stream().filter(e -> !e.contains("#")).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>();
        for (String line: list) {
            String[] split = line.split("=");
            if (split.length > 1){
                map.put(split[0].trim(),split[1].trim());
            }
        }
        Set<String> keySet = map.keySet();
        ArrayList<pyMap> list1 = new ArrayList<>();
        for (String key:
             keySet) {
            list1.add(new pyMap(key,PinyinHelper.toPinyin(key, PinyinStyleEnum.NORMAL)));
        }
        AtomicInteger count = new AtomicInteger();
        Map<String, List<pyMap>> collect = list1.stream().collect(Collectors.groupingBy(pyMap::getPy));
        collect.forEach((k,v) -> {
            String[] s = k.split(" ");
            if (v.size() > 1 && s.length >3){
                count.getAndIncrement();
                System.out.println(k);
                System.out.println(v);
                System.out.println(" ----------------- ");

            }
        });
        System.out.println("可处理热词数量："+ count.get());
    }
}

class pyMap {
    private String cn;
    private String py;

    public pyMap(String cn, String py) {
        this.cn = cn;
        this.py = py;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String toString() {
        return "pyMap{" +
                "cn='" + cn + '\'' +
                ", py='" + py + '\'' +
                '}';
    }
}
