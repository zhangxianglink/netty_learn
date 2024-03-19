package audio.v3;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.sound.sampled.*;

public class AudioDBCalculator {

    public static void calculateDB(String audioFilePath) throws UnsupportedAudioFileException, IOException {
        // 打开音频文件
        File audioFile = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        // 获取音频格式
        AudioFormat format = audioStream.getFormat();

        // 读取音频数据
        byte[] audioBytes = new byte[(int) (audioStream.getFrameLength() * format.getFrameSize())];
        audioStream.read(audioBytes);


        byte[] tmp = new byte[3200];
        int bytesIndex = 0;
        for (int i = 0; i < audioBytes.length; i++) {
            tmp[bytesIndex++] = audioBytes[i];
            if (bytesIndex == 3200) {
                System.out.println(getDb(tmp));
                bytesIndex = 0;
            }
        }
        if (bytesIndex > 0) { // if there is any leftover data
            byte[] leftover = Arrays.copyOf(tmp, bytesIndex);
            System.out.println(getDb(leftover));
        }



    }

    private static Double getDb(byte[] audioBytes){
        // 字节数组中的音频数据转换为short类型的数据
        short[] audioData = new short[audioBytes.length / 2];
        for (int i = 0; i < audioData.length; i++) {
            audioData[i] = (short) (((audioBytes[i * 2 + 1] & 0xff) << 8) | (audioBytes[i * 2] & 0xff));
        }

        // 计算音频的均方根
        double rms = calculateRMS(audioData);
        return  Math.abs(20 * Math.log10(rms));
    }



    private static double calculateRMS(short[] audioData) {
        double sum = 0;
        for (short sample : audioData) {
            sum += sample * sample;
        }
        double mean = sum / audioData.length;
        return Math.sqrt(mean);
    }

    // 使用示例
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        String audioFilePath = "D:\\linuxupload\\16k.wav";
        calculateDB(audioFilePath);
    }
}
