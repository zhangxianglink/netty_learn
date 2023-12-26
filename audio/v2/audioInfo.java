package audio.v2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * x.z
 * Create in 2023/7/14
 */
public class audioInfo {
    public static void main(String[] args) throws IOException, URISyntaxException {



      String link = "https://192.168.1.90:7500/fs125/2023-09-24/912740190498889728_13247317518.mp3";
        URI uri = new URI(link);
        String path = uri.getPath();
        path = path.replace(".mp3","");
        String[] split = path.split("/");
        String[] idAndPhone = split[3].split("_");

        System.out.println(Arrays.asList(split));
        System.out.println(Arrays.asList(idAndPhone));
    }
}
