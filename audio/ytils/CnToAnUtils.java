package audio.ytils;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * x.z
 * Create in 2023/8/30
 */
public class CnToAnUtils {


    public static void main(String[] args) {
        System.out.println(convert("您好，我这边是$地方撒中国联通dfg给您来电，请问d您的手机号码幺三零=+七八八三四八二零，正在使用的对吧？"));
    }
    public static String convert(String original){
        String text = original;
        if (StringUtils.isBlank(original)){
            return original;
        }
        Matcher matcher = pattern2.matcher(original);
        while (matcher.find()){
            try {
                original = chinese_to_num(original,matcher.group());
            }catch (Exception e){
               e.printStackTrace();
                break;
            }
        }
        return original;
    }

    private static String chinese_to_num(String original ,String match){
            String result;
        String replaceAll = match.replaceAll("^[个只分万亿秒]+", "").replaceAll("[个只分万亿秒]+$", "");

        if (pure_num.matcher(replaceAll).matches()){
                String convert = convert_pure_num(match,false);
                result = original.replace(match, convert);
            }else if (value_num.matcher(replaceAll).matches()){
                String convert = convert_value_num(match);
                result = original.replace(match, convert);
            }else if(percent_value.matcher(match).matches() ){
                String convert = convert_percent_value(match);
                result = original.replace(match, convert);
            }else if(pattern_dot_value.matcher(match).matches() ){
                String convert = convert_percent_value(match);
                result = original.replace(match, convert);
            } else if (fraction_value.matcher(match).matches()){
                String convert = convert_fraction_value(match);
                result = original.replace(match, convert);
            } else if(ratio_value.matcher(match).matches()){
                String convert = convert_ratio_value(match);
                result = original.replace(match, convert);
            } else if (time_value.matcher(match).matches()){
                String convert = convert_time_value(match);
                result = original.replace(match, convert);
            }else if (date_value.matcher(match).matches()){
                String convert = convert_date_value(match);
                result = original.replace(match, convert);
            }else {
                return original;
            }
            return result;
    }


    // 纯数字序号
    static Pattern pure_num = Pattern.compile("[" + "零幺一二三四五六七八九" + "]+(点[" + "零幺一二三四五六七八九" + "]+)* *[a-zA-Z个只分万亿秒]?");

    // 数值
    static Pattern value_num = Pattern.compile("十?(零?[一二两三四五六七八九十][十百千万]{1,2})*零?[一二三四五六七八九]?(点[" + "零一二三四五六七八九" + "]+)? *[a-zA-Z个只分万亿秒]?");

    // 百分值
    static Pattern percent_value = Pattern.compile("(?<![一二三四五六七八九])(百分之)[" + "零一二三四五六七八九十百千万" + "]+(点)?");
    static Pattern pattern_dot_value = Pattern.compile("(百分之)[零一二三四五六七八九]+(点)[零一二三四五六七八九]+");

    // 分数
    static Pattern fraction_value = Pattern.compile("([零一二三四五六七八九十百千万]+(点)?([零一二三四五六七八九]?))分之([零一二三四五六七八九十百千万]+(点)?([零一二三四五六七八九]?))");
    // 比值
    static Pattern ratio_value = Pattern.compile("([零一二三四五六七八九十百千万]+(点)?([零一二三四五六七八九]?))比([零一二三四五六七八九十百千万]+(点)?([零一二三四五六七八九]?))");
    // 时间
    static Pattern time_value = Pattern.compile("[零一二三四五六七八九十]+点([零一二三四五六七八九十]+分)([零一二三四五六七八九十]+秒)?");

    // 日期
    static Pattern date_value = Pattern.compile("([零一二三四五六七八九]+年)?([一二三四五六七八九十]+月)([一二三四五六七八九十]+[日号])");


    private static final Pattern pattern2 = Pattern.compile("((([一二两三四五六七八九十])[a-zA-Z年月日号个只分万亿秒]{2,20}|([一二两三四五六七八九十]\\s)[a-zA-Z])|([零幺一二两三四五六七八九十百千万亿点比]{2,20}|(分之)))+");


    private static final HashMap<String,String> num_mapper = new HashMap<String,String>(){{
        put("零","0");
        put("一","1");
        put("幺","1");
        put("二","2");
        put("两","2");
        put("三","3");
        put("四","4");
        put("五","5");
        put("六","6");
        put("七","7");
        put("八","8");
        put("九","9");
        put("点",".");
    }};
    private static final HashMap<String,String> value_mapper = new HashMap<String,String>(){{
        put("零","0");
        put("一","1");
        put("二","2");
        put("两","2");
        put("三","3");
        put("四","4");
        put("五","5");
        put("六","6");
        put("七","7");
        put("八","8");
        put("九","9");
        put("十","10");
        put("百","100");
        put("千","1000");
        put("万","10000");
    }};

    static Pattern strip_regex = Pattern.compile("(.*?)([" + "个只分万亿秒" + "a-zA-Z]*)$");

    private static String[] stripUnit(String original) {
        Matcher matcher = strip_regex.matcher(original);
        if (matcher.find()) {
            String stripped = matcher.group(1).trim();
            String unit = matcher.group(2);
            if (StringUtils.isBlank(unit)){
                return new String[]{original,""};
            }
            return new String[]{stripped,unit};
        } else {
            return new String[]{original,""};
        }
    }

    private static String convert_pure_num(String original, boolean strict){
        String[] stripUnit = stripUnit(original);
        String stripped = stripUnit[0];
        if (StringUtils.isBlank(stripped)){
            return original;
        }
        String unit = stripUnit[1];

        StringBuilder sb = new StringBuilder();
        String[] split = stripped.split("");
        for (String s: split) {
            String num = num_mapper.get(s);
            if (Objects.isNull(num)){
                return original;
            }
            sb.append(num);
        }
        sb.append(unit);
        return sb.toString();

    }

    private static String convert_value_num(String original){
        String[] stripUnit = stripUnit(original);
        String stripped = stripUnit[0];
        if (StringUtils.isBlank(stripped)){
            return original;
        }
        String unit = stripUnit[1];
        if (!stripped.contains("点")){
            stripped += "点";
        }
        String[] parts = stripped.split("点");
        if (StringUtils.isBlank(parts[0])){
            return original;
        }
        String[] intArr = parts[0].split("");
        int value = 0, temp = 0, base = 1;
        for (String c:intArr) {
            if (c.equals("十")){
                base = 1;
                if (temp == 0) {
                    temp = 10;
                } else {
                    temp = Integer.parseInt(value_mapper.get(c)) * temp;
                }
            }else if(c.equals("零")){
                base = 1;
            }else if("一二两三四五六七八九".contains(c)){
                temp += Integer.parseInt(value_mapper.get(c));
            }else if("万".contains(c)){
                value += temp;
                value *= Integer.parseInt(value_mapper.get(c));
                base = Integer.parseInt(value_mapper.get(c))  / 10;
                temp = 0;
            }else if("百千".contains(c)){
                value += temp * Integer.parseInt(value_mapper.get(c));
                base = Integer.parseInt(value_mapper.get(c))  / 10;
                temp = 0;
            }
        }
        value += temp * base;
        if (parts.length > 1) {
            String decimal_part = parts[1];
            String decimal_str = convert_pure_num(decimal_part, true);
            return value + "." + decimal_str + unit;
        }else {
            return value + unit;
        }

    }

    private static String convert_time_value(String original){
        List<String> res = new ArrayList<>();
        String[] split = Pattern.compile("[点分秒]").split(original);
        for (String x : split) {
            if (!x.isEmpty()) {
                res.add(x);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(convert_value_num(res.get(0))).append(":");
        sb.append(convert_value_num(res.get(1)));
        if (res.size() > 2){
            sb.append(":").append(convert_value_num(res.get(2)));
        }
        if (res.size() > 3){
            sb.append(".").append(convert_value_num(res.get(3)));
        }
        return sb.toString();
    }

    private static String convert_date_value(String original){
        StringBuilder sb = new StringBuilder();
        if (original.contains("年")){
            String[] split = original.split("年");
            sb.append(convert_pure_num(split[0],false)).append("年");
            original = split.length > 1 ? split[1] : "";
        }
        if (original.contains("月")){
            String[] split = original.split("月");
            sb.append(convert_value_num(split[0])).append("月");
            original = split.length > 1 ? split[1] : "";
        }
        if (original.contains("日")){
            String[] split = original.split("日");
            sb.append(convert_value_num(split[0])).append("日");
            original = split.length > 1 ? split[1] : "";
        }
        if (original.contains("号")){
            String[] split = original.split("号");
            sb.append(convert_value_num(split[0])).append("号");
        }
        return sb.toString();
    }


    private static String convert_percent_value(String original){
        return convert_value_num(original.substring(3)) + "%";
    }

    private static String convert_fraction_value(String original){
        String[] split = original.split("分之");
        return convert_value_num(split[0]) + "/" + convert_value_num(split[1]);
    }

    private static String convert_ratio_value(String original) {
        String[] split = original.split("比");
        return convert_value_num(split[0]) + ":" + convert_value_num(split[1]);
    }

}

