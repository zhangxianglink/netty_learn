package buider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * x.z
 * Create in 2023/8/28
 */
public class CnToAnTest {

    private static Pattern pattern = Pattern.compile(
            "(?ix)([a-z]\\s*)?(([零幺一二两三四五六七八九十百千万点比]|[零一二三四五六七八九十]|(?<=[一二两三四五六七八九十])[年月日号分]|(分之))+((?<=[一二两三四五六七八九十])[a-zA-Z年月日号个只分万亿秒]|(?<=[一二两三四五六七八九十]\\s)[a-zA-Z]))"
    );

    private static Pattern pattern2 = Pattern.compile("(?ix)([a-z]\\s*)?(((?<=[一二两三四五六七八九十])[a-zA-Z年月日号个只分万亿秒]|(?<=[一二两三四五六七八九十]\\s)[a-zA-Z])?([零幺一二两三四五六七八九十百千万亿点比]|(分之)))+");

    public static void main(String[] args) {
        String commonUnits = "个只分万亿秒";
        String input = "三千一百万零五十四";

        String regex = "(.*?)([" + commonUnits + "a-zA-Z]*)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String stripped = matcher.group(1);
            String unit = matcher.group(2);

            System.out.println("剥离后的字符串：" + stripped);
            System.out.println("单位：" + unit);
        } else {
            System.out.println("未匹配到结果");
        }
    }


}
