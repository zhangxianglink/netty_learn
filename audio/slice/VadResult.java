package audio.slice;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * x.z
 * Create in 2023/5/15
 */
public class VadResult {
    public static void main(String[] args) throws IOException, InterruptedException {

        Pattern pattern = Pattern.compile("微信(公众号|吗)");
        Matcher matcher = pattern.matcher("微信关助理通助理的微信公众号您就可以领取两款会员来用了先生");
        boolean matches = matcher.find();
        System.out.println(matches);

    }



}
