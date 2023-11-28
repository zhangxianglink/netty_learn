package audio.slice;

import audio.ytils.HttpUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * x.z
 * Create in 2023/5/15
 */
public class Vad {



    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("D:\\linuxupload\\hot-rule_20231101-111051.txt"));
        lines.forEach(line -> {
            String[] split = line.split("=");
            String url = split[0];
            String name = split[1];
           if (name.contains("清楚")){
               System.out.println(line);
           }
        });
    }
}
