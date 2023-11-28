package audio.v2;

import java.io.IOException;
import java.util.ArrayList;

/**
 * x.z
 * Create in 2023/7/14
 */
public class audioInfo {
    public static void main(String[] args) throws IOException {
        ArrayList<Double> timestamps = new ArrayList<Double>() {{
            add(473319.0);
            add(477999.0);
            add(478159.0);
            add(478239.0);
            add(478399.0);
            add(478479.0);
            add(478599.0);
            add(478679.0);
            add(478919.0);
            add(478999.0);
            add(479279.0);
            add(479319.0);
            add(479559.0);
            add(479639.0);
            add(479999.0);
            add(480119.0);
            add(480239.0);
            add(480399.0);
        }};
        ArrayList<String> tokens = new ArrayList<String>() {{
            add("<unk>");
            add("上");
            add("装");
            add("到");
            add("这");
            add("个");
            add("外");
            add("面");
            add("没");
            add("有");
            add("没");
            add("有");
            add("东");
            add("西");
            add("打");
            add("那");
            add("种");
            add("的");
        }};
        int i = tokens.indexOf("<unk>");
        System.out.println(i);
        System.out.println(tokens.remove(i));
        System.out.println(timestamps.remove(i));
    }
}
