package net.nls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * x.z
 * Create in 2023/3/15
 */
public class ConversionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConversionUtils.class);

    /**
     * 采样率8k -> 16k
     */
    public static byte[] convert8kTo16k(byte[] audioData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        AudioFormat sourceFormat = new AudioFormat(8000, 16, 1, true, false);
        AudioInputStream sourceAudioInputStream = new AudioInputStream(byteArrayInputStream, sourceFormat, audioData.length / sourceFormat.getFrameSize());

        AudioFormat targetFormat = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream targetAudioInputStream = AudioSystem.getAudioInputStream(targetFormat, sourceAudioInputStream);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = targetAudioInputStream.read(buffer, 0, buffer.length)) != -1) {

                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.error("采样率转换异常：",e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 字节转换
     */
    public static byte[] byteConversion(byte[] buffer){
        // byte array to short array
        short[] shortArr = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArr);

        // short arr 转 float arr
        int sampleIndex = 0;
        float[] floats = new float[buffer.length / 2];
        for (short value : shortArr) {
            float temp = value / 32767.0f;
            floats[sampleIndex++] = temp;
        }

        // float arr 转 byte arr
        ByteBuffer buffer2 = ByteBuffer.allocate(floats.length * Float.BYTES);
        // 设置字节顺序为小端
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (float f : floats) {
            buffer2.putFloat(f);
        }
        return buffer2.array();
    }
}
