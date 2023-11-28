package audio.v3;




import org.java_websocket.WebSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * x.z
 * Create in 2023/3/13
 */
public class JavaStart2 {

    public static byte[] resampleTo16kHz(AudioInputStream originalAudioInputStream) {
        AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false);

        try {
            // 使用AudioSystem获得转换后的音频输入流
            AudioInputStream resampledAudioInputStream = AudioSystem.getAudioInputStream(targetFormat, originalAudioInputStream);

            // 将音频输入流写入字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = resampledAudioInputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] convert8kTo16k1(byte[] audioData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        AudioFormat sourceFormat = new AudioFormat(8000, 16, 1, true, false);
        AudioInputStream sourceAudioInputStream = new AudioInputStream(byteArrayInputStream, sourceFormat, audioData.length / sourceFormat.getFrameSize());

        AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream targetAudioInputStream = AudioSystem.getAudioInputStream(targetFormat, sourceAudioInputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = targetAudioInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }



    /**
     * 取两个采样点中间值添加到音频数据，增加转换后效果
     * 8k16bit->16k16bit
     * @param orig
     * @return
     */
    public static byte[] convert8000To16000(byte[] orig){
        byte[] dest = new byte[]{};
        for (int j = 0; j < orig.length; j = j + 2) {
            byte[] byte1 = new byte[2];
            byte1[1] = orig[j + 1];
            byte1[0] = orig[j];

            dest = append(dest, byte1);
            if (j+2>=orig.length){
                dest = append(dest,byte1);
            }else {
                short sample = toShort(byte1);
                byte[] byte2 = new byte[2];
                byte2[0] = orig[j+2];
                byte2[1] = orig[j+3];
                short sample1 = toShort(byte2);
                short sample2 = (short) ((sample+sample1)/2);
                byte[] byte3= toByte(sample2);
                dest = append(dest, byte3);
            }
        }
        return dest;
    }

    /**
     * short->byte
     * @param s
     * @return
     */
    public static byte[] toByte(short s){
        byte[] byte2 = new byte[2];
        byte2[1] = (byte) ((s << 16) >> 24);
        byte2[0] = (byte) ((s << 24) >> 24);
        return byte2;
    }

    /**
     * byte->short
     * @param b
     * @return
     */
    public static short toShort(byte[] b) {
        return(short)((b[1]<<8)+(b[0]<<0));
    }


    public static byte[] append(byte[] orig, byte[] dest) {

        byte[] newByte = new byte[orig.length + dest.length];

        System.arraycopy(orig, 0, newByte, 0, orig.length);
        System.arraycopy(dest, 0, newByte, orig.length, dest.length);

        return newByte;

    }


    public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedAudioFileException, IOException {
        // 获取音频字节流
        String path = "D:\\data\\8k\\output.wav";
        Path path1 = Paths.get(path);
        byte[] fileContent = Files.readAllBytes(path1);
        // 建立语音识别功能的websocket连接
        JavaClient client2  = new JavaClient(new URI("ws://192.168.6.102:6206"));

        client2.connect();
        Thread.sleep(200);
        WebSocket.READYSTATE readyState = client2.getReadyState();
        System.out.println("当前连接状态：" + readyState);

        int sampleRate = 16000; // sample rate
        int numSamples = fileContent.length / 2; // assuming 16-bit samples
        int numBytes = numSamples * 4; // number of bytes in float32 representation

        ByteBuffer buffer = ByteBuffer.allocate(8 + numBytes).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(sampleRate);
        buffer.putInt(numBytes);
        for (int i = 0; i < numSamples; i++) {
            short sampleInt16 = (short)((fileContent[2 * i] & 0xff) | (fileContent[2 * i + 1] << 8));
            float sampleFloat32 = sampleInt16 / 32768.0f;
            buffer.putFloat(sampleFloat32);
        }
        byte[] array = buffer.array();
        client2.send(array);
    }

}
