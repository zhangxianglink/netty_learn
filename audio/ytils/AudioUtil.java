package audio.ytils;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.wav.util.WavInfoReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * x.z
 * Create in 2023/4/27
 */
public class AudioUtil {

    public static void sliceBigFile(String slicePath, String sourceFileName, String name, float second) {

        StringBuilder sb = new StringBuilder();
        String str;
        int count = (int) (second / 120);
        // 无法整除，还有剩余音频部分
        if(BigDecimal.valueOf(second).compareTo(BigDecimal.valueOf(120)) != 0) {
            count += 1;
        }
        int j;
        for (int i = 0; i < count; i++) {
            j = 2 * i;
            if (j >= 60) {
                str = "01:" + (j - 60) + ":00";
            } else {
                str = "00:" + j + ":00";

            }
            // 分割路径
            String sliceFileName = slicePath + "_" + i + "_"+ name ;
            sb.append("ffmpeg").append(" -ss ").append(str).append(" -i ").append(sourceFileName)
                    .append(" -c copy -t 120 ").append(sliceFileName);
            System.out.println("ffmpeg命令，"+ sb);
        }

    }

    public static void main(String[] args) {
        sliceBigFile("/data/software/sound_stream/slice/", "/data/software/sound_stream/test.wav", "test", 309);
    }

    public static Double getDb(byte[] audioBytes){
        // 字节数组中的音频数据转换为short类型的数据
        short[] audioData = new short[audioBytes.length / 2];
        for (int i = 0; i < audioData.length; i++) {
            audioData[i] = (short) (((audioBytes[i * 2 + 1] & 0xff) << 8) | (audioBytes[i * 2] & 0xff));
        }

        // 计算音频的均方根
        double rms = calculateRMS(audioData);

        if (rms > 0) {
            // 计算分贝大小
            double v = 20 * Math.log10(rms);
            return BigDecimal.valueOf(v).setScale(3, RoundingMode.DOWN).doubleValue();
        }
        return 0.0;
    }

    private static double calculateRMS(short[] audioData) {
        double sum = 0;
        for (short sample : audioData) {
            sum += sample * sample;
        }
        double mean = sum / audioData.length;
        return Math.sqrt(mean);
    }
    /**
     * 获取语音文件播放时长(秒) 支持wav 格式
     * @param filePath
     * @return
     */
    public static Float getDuration(String filePath){
        try{

            File destFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(destFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = destFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            return durationInSeconds;

        }catch (Exception e){
            e.printStackTrace();
            return 0f;
        }

    }

    public static float getSecond(String path){
        File file = new File(path);
        WavInfoReader wavInfoReader = new WavInfoReader();
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            // wav音频时长
            return (wavInfoReader.read(raf).getPreciseLength() );
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 获取mp3语音文件播放时长(秒) mp3
     * @param filePath
     * @return
     */
    public static Float getMp3Duration(String filePath){

        try {
            File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength()+"");
        } catch(Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 获取mp3语音文件播放时长(秒)
     * @param mp3File
     * @return
     */
    public static Float getMp3Duration(File mp3File){

        try {
            //File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength()+"");
        } catch(Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }


    /**
     * 得到pcm文件的毫秒数
     *
     * pcm文件音频时长计算
     * 同图像bmp文件一样，pcm文件保存的是未压缩的音频信息。 16bits 编码是指，每次采样的音频信息用2个字节保存。可以对比下bmp文件用分别用2个字节保存RGB颜色的信息。 16000采样率 是指 1秒钟采样 16000次。常见的音频是44100HZ，即一秒采样44100次。 单声道： 只有一个声道。
     *
     * 根据这些信息，我们可以计算： 1秒的16000采样率音频文件大小是 2*16000 = 32000字节 ，约为32K 1秒的8000采样率音频文件大小是 2*8000 = 16000字节 ，约为 16K
     *
     * 如果已知录音时长，可以根据文件的大小计算采样率是否正常。
     * @param filePath
     * @return
     */
    public static long getPCMDurationMilliSecond(String filePath) {
        File file = new File(filePath);

        //得到多少秒
        long second = file.length() / 16000 ;

        long milliSecond = Math.round((file.length() % 16000)   / 16000.0  * 1000 ) ;

        return second * 1000 + milliSecond;
    }



}
