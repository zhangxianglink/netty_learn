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

   static String str = ".*(^(?!.*((问|打听|了解|咨询|还有).*(下|个|点|别|其他|其它).*(事|东西|情况|问题)|再见|拜拜|喂喂|停停|(我|你).*(给|让|和|跟).*(我|你).*(说|讲)|等.*下|等.*等|停.*下|别急|我.*没说完|我.*还有.*问|别挂|(你|您)好啊|是(吗$|吧$|不是$)|(不|别|不会|不能|不可以)(需要|要|好|号|用|同意|行|可以|办|开|改|申请|登记|升|整|弄|是|给|的|想|愿意|看|知道|清楚|明白|晓得|了解|理解|搞|(扣|要)费|钱|话费)|自己.*(办|开|弄|申请|改|升级)|回.*(电|话)|我.*想想|(我|你).*是(说|讲|问)|(去|到|上).*(营业厅|店|柜台|家)|(自己|一人|单独|独自).*(去|到|上))).*)(ok|没问题|你开|你办|(^|，)(好|可以|行|想|进|想想|我想|像|像像|同意|忠|中|兑|换|兑换)($|，)|(可以|行|好|同意|兑|换|兑换)(的|滴|啊|呢|呀|哒|大|吧|了|嘞|勒|累|啦|那)|(^|，)(可以|行|好|同意|兑|换|兑换).*(的|滴|啊|呢|呀|哒|大|吧|了|嘞|勒|累|啦|那)|(可以|行|好|同意|兑|换|兑换).*(的|滴|啊|呢|呀|哒|大|吧|了|嘞|勒|累|啦|那)($|，)|(申请|办|弄|整|开|试|用|换|改|处理|登记|加|升级|升|体验).*(吧|个月|看|一个|一下)($|，)|(嗯|哦|啊|哎|噢|谢谢|好|行|想|像|进|可以|同意|忠|中|那)(好|行|想|像|进|可以|同意|忠|中|办|升级|升|体验)|(好|行|想|像|进|可以|同意|统一|忠|中|升级|升|体验)(嗯|哦|啊|吧|好|行|想|像|进|可以|同意|忠|中|办|谢谢)|(开通|办理).*吧|(那|就|先).*(申请|办|弄|整|开|试|用|换|改|处理|登记|加|升级|升).*(吧|个月|看|一个|一下))|没问题";

    public static void main(String[] args) {
        Pattern compile = Pattern.compile(str);
        Matcher matcher = compile.matcher("可以");
        if (matcher.find()) {
            System.out.println(matcher.group());
        }else {
            System.out.println("没有匹配");
        }
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

