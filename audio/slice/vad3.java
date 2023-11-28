package audio.slice;

import audio.v3.format;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

/**
 * x.z
 * Create in 2023/5/6
 * 左右声道分离成功
 */
public class vad3 {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        sliceWav("D:\\data\\out\\222.wav","D:\\data\\out\\left.wav","D:\\data\\out\\right.wav");

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
