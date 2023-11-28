package audio.ytils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * x.z
 * Create in 2023/8/10
 */
public class AddPrefixToFiles {
    public static void main(String[] args) throws IOException, InterruptedException {
        String folderPath = "C:\\Users\\admin\\Desktop\\转译有误差录音";
        addPrefixToFiles(folderPath);
    }

    public static void addPrefixToFiles(String folderPath) throws IOException, InterruptedException {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String originalName = file.getName();
                        String l = folderPath + File.separator + originalName.substring(0,4) + "_left.wav";
                        String r = folderPath + File.separator + originalName.substring(0,4) + "_right.wav";
                        String f = folderPath + File.separator + originalName;
                        sliceWav2ByFfmpeg(f,l,r);
//                        String newName = originalName.substring(originalName.length() - 8);
//                        File newFile = new File(folderPath + File.separator + newName);
//                        if (file.renameTo(newFile)) {
//                            System.out.println("文件名修改成功：" + originalName + " -> " + newName);
//                        } else {
//                            System.out.println("文件名修改失败：" + originalName);
//                        }
                    } else if (file.isDirectory()) {
                        addPrefixToFiles(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void sliceWav2ByFfmpeg(String filePath, String leftPath, String rightPath) throws IOException, InterruptedException {
        List<String> list = new ArrayList<>();
        list.add("ffmpeg");
        list.add("-i");
        list.add(filePath);
        list.add("-map_channel");
        list.add("0.0.0");
        list.add("-ar");
        list.add("8000");
        list.add(leftPath);
        list.add("-map_channel");
        list.add("0.0.1");
        list.add("-ar");
        list.add("8000");
        list.add(rightPath);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(list);
        Process start = pb.start();
        start.waitFor();
        start.destroy();
    }
}
