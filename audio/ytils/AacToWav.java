package audio.ytils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/5/16
 */
public class AacToWav {


    public static void main(String[] args) throws IOException {

        String wavPath = "D:\\data\\情绪声音";
        Path localDirPath = Paths.get(wavPath);
        Files.walkFileTree(localDirPath,
                new SimpleFileVisitor<Path>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file,
                                                     BasicFileAttributes attrs) throws IOException {
                        File file1 = file.toFile();
                        File newFile = new File("D:\\data\\情绪声音\\情绪识别"+ file.getFileName());
                        boolean success = file1.renameTo(newFile);
                        System.out.println(file.getFileName());
                        return FileVisitResult.CONTINUE;
                    }
                    // 再去遍历删除目录
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir,
                                                              IOException exc) throws IOException {
                        System.out.printf("文件夹: %s%n", dir);
                        return FileVisitResult.CONTINUE;
                    }

                }
        );
    }
}
