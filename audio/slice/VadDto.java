package audio.slice;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * x.z
 * Create in 2023/5/15
 */
public class VadDto {
    public static void main(String[] args) throws IOException, InterruptedException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("ffmpeg -i D:\\linuxupload\\20068.wav -ss ")
//                .append(formatTime(23)).append(" -to ")
//                .append(formatTime(29))
//                .append(" -c copy D:\\linuxupload\\29.wav ");
//        Runtime.getRuntime().exec(sb.toString());




        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i",
                "D:\\linuxupload\\20068.wav",
                "-ss",
                formatTime(23),
                "-to",
                formatTime(30),
                "-c",
                "copy",
                "D:\\linuxupload\\30.wav"
        );

        Process start = pb.start();
        boolean completed = start.waitFor(5, TimeUnit.SECONDS);
        if (completed) {
            if (start.exitValue() != 0) {
                System.out.println("a");
            }else {
                System.out.println("b");
            }
        } else {
            System.out.println("子进程未在规定时间内结束");
        }
        start.destroy();
    }

    private static String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600; // 小时数
        long minutes = (totalSeconds % 3600) / 60; // 分钟数
        long seconds = totalSeconds % 60; // 秒数
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
