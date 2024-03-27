package audio.slice;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/5/6
 * 左右声道分离成功
 */
public class vad3 {
    private static final byte[] SPLIT_BYTE = {0x01};
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        Gson gson = new Gson();
        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\admin\\Desktop\\备份sql\\1.sql"));
        List<String> collect = lines.stream().limit(1).collect(Collectors.toList());
        for (String line : collect) {
            StringBuilder str = new StringBuilder();
            String[] split = line.split(", ");
            long id = Long.parseLong(split[0]);
            HashMap hashMap = gson.fromJson(split[2], HashMap.class);
            LinkedTreeMap data = (LinkedTreeMap) hashMap.get("data");
            String chunk_message = (String) data.get("chunk_message");
            System.out.println(chunk_message);
            String[] strings = chunk_message.split("n");
            for (int i = 0; i < strings.length - 1; i++) {
                if (strings[i].length() > 0) {
                    String s = strings[i] + "\\n";
                    String json = "data: {\"model\": \"gpt4-641\",\"data\": {\"chunk_message\": \"" + s + "\",\"finish_reason\": null}}";
                    str.append(json).append(new String(SPLIT_BYTE));
                }
            }
            String last = strings[strings.length - 1];
            data.put("chunk_message",last);
            str.append("data: "+gson.toJson(hashMap)).append(new String(SPLIT_BYTE));

            System.out.println(id);
            System.out.println(str);
        }

    }

    /**
     * 将字符串按照指定长度分割成字符串数组
     *
     * @param text   需要拆取的字符串
     * @param length 截取的长度
     * @return
     */
    public static String[] stringToStringArray(String text, int length) {
        //检查参数是否合法
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        if (length <= 0) {
            return null;
        }
        //获取整个字符串可以被切割成字符子串的个数
        int n = (text.length() + length - 1) / length;
        String[] splitArr = new String[n];
        for (int i = 0; i < n; i++) {
            if (i < (n - 1)) {
                splitArr[i] = text.substring(i * length, (i + 1) * length);
            } else {
                splitArr[i] = text.substring(i * length);
            }
        }
        return splitArr;
    }


    public static void sliceWav(String fileName, String leftPath, String rightPath) throws UnsupportedAudioFileException, IOException {
        File file = new File(fileName);
        FileInputStream fis=new FileInputStream(file);
        BufferedInputStream bis=new BufferedInputStream(fis);
        AudioInputStream ais=AudioSystem.getAudioInputStream(bis);

        AudioFormat format = ais.getFormat();
        System.out.println(format.toString());
        System.out.println(format.getChannels());

        if (format.getChannels() != 2 )
        {
            throw new RuntimeException("需要双声道的wav音频文件。");
        }

        int numFrames = (int) ais.getFrameLength();
        int audioLength = numFrames * format.getFrameSize();
        byte[] audioData = new byte[audioLength];

        ais.read(audioData);
        byte[] leftChannel = new byte[numFrames * 2];
        byte[] rightChannel = new byte[numFrames * 2];

        for (int i = 0; i < audioData.length; i += 4) {
            leftChannel[i/2] = audioData[i];
            leftChannel[i/2 + 1] = audioData[i + 1];
            rightChannel[i/2] = audioData[i + 2];
            rightChannel[i/2 + 1] = audioData[i + 3];
        }

        FileOutputStream fosLeft = new FileOutputStream(leftPath);
        FileOutputStream fosRight = new FileOutputStream(rightPath);

        byte[] headerBytes = getHeaderBytes(leftChannel);

        outChannel(leftChannel, fosLeft, headerBytes);
        outChannel(rightChannel, fosRight, headerBytes);

        ais.close();
        bis.close();
        fis.close();
    }

    private static byte[] getHeaderBytes(byte[] leftChannel) throws IOException {
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = leftChannel.length + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;//比特率
        header.Channels = 1;//双通道
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;//采样率
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = leftChannel.length;
        byte[] headerBytes = header.getHeader();
        assert headerBytes.length == 44; //WAV标准，头部应该是44字节
        return headerBytes;
    }

    private static void outChannel(byte[] pcmData, FileOutputStream outputStream, byte[] headerBytes) throws IOException {
        byte[] byteResult = new byte[headerBytes.length + pcmData.length];
        System.arraycopy(headerBytes, 0, byteResult, 0, headerBytes.length);
        System.arraycopy(pcmData, 0, byteResult, headerBytes.length, pcmData.length);
        outputStream.write(byteResult, 0, byteResult.length);
        outputStream.flush();
        outputStream.close();
    }
}
