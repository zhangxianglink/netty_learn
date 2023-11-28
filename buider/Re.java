package buider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/8/23
 */
public class Re {
    public static void main(String[] args) throws IOException {
        List<String> list = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-rule.txt"));
        list = list.stream().filter(e -> !e.contains("#")).collect(Collectors.toList());
        Map<String,String> map = new HashMap<>();
        for (String line: list) {
            String[] split = line.split("=");
            if (split.length > 1){
                map.put(split[0].trim(),split[1].trim());
            }
        }
        String text = "哦5G接听啊您是我们联通稳定优质用户稍后可以关注广东联通微信公众号就可以享受每月多加十五元的VIP会员领取来用有腾讯视频爱奇艺QQ音乐等二十多款同时呢还会给您赠送二十元的话费分四个月每个月到账五元微信关注广东联通公众号就可以为您办理好名留意使用了好吧APP";

        // 统计能匹配上的key
        ArrayList<String> keys = new ArrayList<>();
        for (String key : map.keySet()){
            if (StringUtils.containsIgnoreCase(text,key)){
                keys.add(key);
            }
        }
        keys.sort(Comparator.comparing(String::length).reversed());
        // 从大到小检测是否能够进行转换
        for (String key: keys) {
            String value = map.get(key);
            if (key.length() == 1){
                if (text.length() == 1 && text.equals(key) ) {
                    text = text.replace(key,value);
                }
            }else {
                text = text.replace(key,value);
            }
        }
        System.out.println(text);

        System.out.println("--------------");

        String[] tokens = {"哦","五"," G","接","听","啊","您","是","我","们","联","通","稳","定","优","质","用","户","稍","后","可","以","关","注","广","东","联","通","微","信","公","众","号","就","可","以","享","受","每","月","多","加","十","五","元","的"," VI","P","会","员","领","取","来","用","有","腾","讯","视","频","爱","奇","艺"," ","Q","Q","音","乐","等","二","十","多","款","同","时","呢","还","会","给","您","赠","送","二","十","元","的","话","费","分","四","个","月","每","个","月","到","账","五","元","微","信","关","注","广","东","联","通","公","众","号","就","可","以","为","您","办","理","好","名","留","意","使","用","了","好","吧"," APP"};

        ArrayList<String> ens = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("\\s")){
                System.out.println("index: " + i );
                // 根据index  ,timestamps 中的数据
            }
            if (tokens[i].matches("[\\s\\w]+")){
                String replaceAll = tokens[i].replaceAll("\\s", "");
                if (replaceAll.length() > 1){
                    // 重要的
                    System.out.println("index:" + i + " token:" + tokens[i] + " length:" + replaceAll.length());
                    ens.add(replaceAll);
                }
            }
        }

        System.out.println(String.join("",tokens));
        for (String en: ens) {
            text = text.replaceAll(en,"("+en+")");
        }
        System.out.println(text);

        String[] split = text.split("");
        StringBuilder builder = new StringBuilder();

        boolean bool = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("(")){
                bool = false;
            }else if(split[i].equals(")")){
                bool = true;
                String replace = sb.toString().replace("(", "").replace(")","");
                builder.append(replace).append("-");
                sb = new StringBuilder();
            }

            if (bool && !split[i].equals(")")) {
                builder.append(split[i]).append("-");
            }else {
                sb.append(split[i]);
            }
        }

        String s = builder.toString();
        s = s.substring(0,s.length()-1);
        System.out.println(s);
        String[] newToken = s.split("-");

        System.out.println(tokens.length);
        System.out.println(newToken.length);

    }
}
