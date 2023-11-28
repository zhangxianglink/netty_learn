package audio.noise;


import java.io.IOException;

/**
 * x.z
 * Create in 2023/11/14
 */
public class FileNoise {

    // 离线降噪
    public static void main(String[] args) throws IOException, InterruptedException {
        noiseFilter("C:\\Users\\admin\\Desktop\\debug\\预加重\\left.wav","C:\\Users\\admin\\Desktop\\debug\\预加重\\left8.wav");
    }

    public static void noiseFilter(String filePath, String noisePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", filePath, "-af", "asendcmd=0.0 afftdn sn start,asendcmd=0.4 afftdn sn stop,afftdn=nr=20:nf=-30", noisePath);
        Process start = pb.start();
        start.waitFor();
        if (start.exitValue() != 0) {
            System.out.println("ffmpeg降噪执行失败{}  " + pb.command());
        }else {
            System.out.println("ffmpeg降噪执行完成{}  " + pb.command());
        }
        start.destroy();
    }
}
