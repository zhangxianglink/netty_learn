package audio.slice;

import java.io.File;
import java.io.IOException;

/**
 * x.z
 * Create in 2023/5/15
 */
public class VadResult {
    public static void main(String[] args) throws IOException, InterruptedException {

        File file = new File("D:\\linuxupload\\968463034302627840_13229480559.wav");
        if (file.length() < 1000) {
            System.out.println(file.length());
        }

    }



}
