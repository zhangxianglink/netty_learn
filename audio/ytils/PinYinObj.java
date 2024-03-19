package audio.ytils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * x.z
 * Create in 2023/8/29
 */
public class PinYinObj {


    public static void main(String[] args) throws IOException {
       String httpUrl = "http://192.168.1.53:21601/cc/atm/tprecord/msa/record/kfDownRecordfile?fileName=2605027565886939715.wav&filePath=acdrecord2/51/20240204/10/2605027565886939715.wav&proId=51" ;
       String fileName = FilenameUtils.getName(httpUrl);
       String[] split = fileName.split("\\.");
       String wavName;
        String type = split[1];
        if (!type.equals("wav")){
            wavName = split[0] + ".wav";
        }else {
            wavName = fileName;
        }
        System.out.println(wavName);
        System.out.println(type);
    }
}
