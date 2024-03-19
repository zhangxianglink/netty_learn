package audio.v3;

import org.java_websocket.WebSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * x.z
 * Create in 2023/3/13
 */
public class JavaStart3 {

    public static byte[] resampleTo16kHz(AudioInputStream originalAudioInputStream) throws IOException {
        byte[] read = new byte[1024];
        originalAudioInputStream.read(read);
        System.out.println(Arrays.toString(read));

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
        byte[] buffer = new byte[4096];
        int bytesRead;
        try {
            while ((bytesRead = targetAudioInputStream.read(buffer, 0, buffer.length)) != -1) {

                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }



    public static void main(String[] args) throws InterruptedException, URISyntaxException, UnsupportedAudioFileException, IOException {
        // 获取音频字节流
        String path = "D:\\data\\8kt\\20088.wav";


        byte[] buffer = Files.readAllBytes(Paths.get(path));
        System.out.println("buffer: " + buffer.length);

        File file = new File(path);
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        AudioFormat format = ais.getFormat();
        System.out.println(format.toString());
        int channels = format.getChannels();




        // 建立语音识别功能的websocket连接
        JavaClient client2 = new JavaClient(new URI("ws://192.168.166.14:8080"));


        client2.connect();
        Thread.sleep(100);
        WebSocket.READYSTATE readyState = client2.getReadyState();
        System.out.println("当前连接状态：" + readyState);


        // byte array to short array
        short[] shortArr = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArr);

        // short arr 转 float arr
        int sampleIndex = 0;
        float[] floats = new float[buffer.length / 2];
        for (int i = 0; i < shortArr.length; i ++) {
            float temp = shortArr[i] / 32767.0f;
            floats[sampleIndex++] = temp;
        }

        // float arr 转 byte arr
        ByteBuffer buffer2 = ByteBuffer.allocate(floats.length * Float.BYTES);
        // 设置字节顺序为小端
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (float f : floats) {
            buffer2.putFloat(f);
        }
        byte[] bytesToSend = buffer2.array();

        System.out.println("bytesToSend: " + bytesToSend.length);


     /*   // 创建预加重对象
        PreEmphasis preEmphasis = new PreEmphasis(0.97); // 0.97是一个常用的预加重系数

        // byte array to short array
        short[] shortArr = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArr);


        double[] doubleArr = new double[shortArr.length];
        for (int i = 0; i < shortArr.length; i++) {
            doubleArr[i] = shortArr[i] / 32767.0;
        }

        double[] emphasized = preEmphasis.filter(doubleArr);

        float[] floats = new float[buffer.length / 2];
        for (int i = 0; i < shortArr.length; i++) {
            floats[i] = (float) emphasized[i];
        }

        // float arr 转 byte arr
        ByteBuffer buffer2 = ByteBuffer.allocate(floats.length * Float.BYTES);
        // 设置字节顺序为小端
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (float f : floats) {
            buffer2.putFloat(f);
        }
        byte[] bytesToSend =  buffer2.array();*/

        // 分批发送
        byte[] tmp = new byte[16384];
        int bytesIndex = 0;
        for (int i = 0; i < bytesToSend.length; i++) {
            tmp[bytesIndex++] = bytesToSend[i];
            if (bytesIndex == 16384) {
                client2.send(tmp);
                Thread.sleep(10);
                bytesIndex = 0;
            }
        }
        if (bytesIndex > 0) { // if there is any leftover data
            byte[] leftover = Arrays.copyOf(tmp, bytesIndex);
            client2.send(leftover);
        }


//        client2.send(bytesToSend);

    }

}
