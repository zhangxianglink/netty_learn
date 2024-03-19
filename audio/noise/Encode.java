package audio.noise;

import audio.ytils.NoModelDataListener;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.List;

/**
 * x.z
 * Create in 2024/2/22
 */
public class Encode {

    public static void main(String[] args) throws IOException {
        String fileName =  "C:\\Users\\admin\\Desktop\\录音分析单2月.xlsx";
        List<List<Object>> list = ListUtils.newArrayList();
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(fileName, new NoModelDataListener(list)).sheet().doRead();
        System.out.println(list.size());

        String targetFile =  "C:\\Users\\admin\\Desktop\\录音分析单2月info.xlsx";

        Path path = Paths.get("C:\\Users\\admin\\Desktop\\info.txt");
        List<String> stringList = Files.readAllLines(path);
        for (int i = 0; i < list.size(); i++) {

            String s = stringList.get(i);
            if (s.length() > 32767){
                list.get(i).add(s.substring(35, 30000));
                list.get(i).add(s.substring( 30000));
            }else {
                list.get(i).add(s.substring(35));
            }
        }
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(targetFile).sheet("模板").doWrite(list);
    }
}
