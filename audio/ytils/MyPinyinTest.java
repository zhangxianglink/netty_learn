package audio.ytils;

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.mayabot.nlp.module.pinyin.PinyinResult;
import com.mayabot.nlp.module.pinyin.Pinyins;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/8/29
 */
public class MyPinyinTest {
    public static void main(String[] args) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(convertHotWords("好的那您先不挂机给您讲完一下哈套餐呢还需保留员套餐正常用好登记办理好十元五G组合绑架邮包三个工作日生效谢谢东西真效后立即扣费正款不抵扣月十万的费用如果您有副卡的话可以共享这个流量和通话的哈流量用不完的话也可以结转到下个月的语音不结转那您再有五十终端午就芒果会在线细节节享受五G芒果五十信号了哈您都清楚了对吧"));
        System.out.println(Duration.between(now, LocalDateTime.now()).toMillis());


    }

    public static void convertWords(String ex,ArrayList<Set<String>> list, ArrayList<String> arr,int n){
        if (n < list.size()){
            Set<String> set = list.get(n);
            for (String s: set) {
                if (list.size() == (n+1) ){
                    arr.add(ex + "-" + s);
                }
                if (n == 0){
                    convertWords(s ,list,arr,n+1);
                }else {
                    convertWords(ex + "-" + s ,list,arr,n+1);
                }
            }
        }
    }

    public static HashMap<String, Set<List<String>>>  reloadPinYinDict() throws IOException {
        HashMap<String, Set<List<String>>> map = new HashMap<>();
        List<String> allLines = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-zh.txt"));
        List<String> filterLines = allLines.stream().filter(e -> !e.contains("#")).collect(Collectors.toList());

        for (String hotWord: filterLines) {
            hotWord = hotWord.trim();
            char[] chars = hotWord.toCharArray();
            ArrayList<Set<String>> arrayList = new ArrayList<>();
            PinyinResult result = Pinyins.convert(hotWord);
            List<String> fuzzyList = result.fuzzy(true).asList();

            for (int i=0; i < chars.length; i++) {
                Set<String> set = new HashSet<>(PinyinHelper.toPinyinList(chars[i], PinyinStyleEnum.NORMAL));
                set.add(fuzzyList.get(i));
                arrayList.add(set);
            }
            ArrayList<String> arr = new ArrayList<>();
            convertWords("",arrayList,arr,0);
            Set<List<String>> collect = arr.stream().map(e -> Arrays.asList(e.split("-"))).collect(Collectors.toSet());
            map.put(hotWord,collect);
        }
        return map;
    }

    public static   HashMap<String, List<List<String>>> match(HashMap<String, Set<List<String>>> map, String words){
        String pinyinWords = PinyinHelper.toPinyin(words, PinyinStyleEnum.NORMAL).replaceAll(" ", "");
        HashMap<String, List<List<String>>> result = new HashMap<>();
        map.forEach((k,v) -> {
            ArrayList<List<String>> list = new ArrayList<>();
            v.stream().forEach(e -> {
                String join = e.stream().collect(Collectors.joining());
                if (pinyinWords.contains(join)){
                    list.add(e);
                }
            });
            if (list.size() > 0) {
                result.put(k, list);
            }
        });
        return result;
    }

    public static String convertHotWords(String words) throws IOException {
        words = words.replaceAll("\\s", "");
        HashMap<String, Set<List<String>>> dict = reloadPinYinDict();
        HashMap<String, List<List<String>>> match = match(dict, words);
        String allPinyin = PinyinHelper.toPinyin(words, PinyinStyleEnum.NORMAL, "-");
        List<String> matchKeys = match.keySet().stream().sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList());
        for (String key : matchKeys) {
            List<List<String>> values = match.get(key);
            for (List<String> value: values) {
                String collect = value.stream().collect(Collectors.joining("-"));
                int count = 0;
                Pattern pattern = Pattern.compile("\\b" + collect + "\\b");
                Matcher matcher = pattern.matcher(allPinyin);
                while (matcher.find()) {
                    count ++;
                }
                int beginIndex;
                for (int y = 1; y <= count; y++) {
                    String[] keys = key.split("");
                    String[] split = allPinyin.split(collect);
                    int sum = Arrays.stream(split).limit(y).mapToInt(e ->
                            (int) Arrays.stream(e.split("-")).filter(StringUtils::isNotBlank).count()
                    ).sum();

                    beginIndex = sum +  (value.size() * (y -1)) ;
                    String[] wordArr = words.split("");
                    // 判断是否多个英文
                    ArrayList<String> mergeList = new ArrayList<>();
                    String tmp = "";
                    for (int i = 0; i < wordArr.length; i++) {
                        if(!wordArr[i].matches("[\\d\\w]+")){
                            if (tmp.length() > 0){
                                mergeList.add(tmp);
                                tmp = "";
                            }
                            mergeList.add(wordArr[i]);
                        }else {
                            tmp = tmp + wordArr[i];
                        }
                    }
                    if (tmp.length() > 0){
                        mergeList.add(tmp);
                    }
                    for (int i = 0; i < value.size(); i++) {
                        mergeList.set(beginIndex + i, keys[i]);
                    }
                    words = mergeList.stream().collect(Collectors.joining());
                }
            }
        }
        return words;
    }

}
