package buider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * x.z
 * Create in 2023/10/24
 */
public class CreateTimeFile {

    public static void main(String[] args) {
        LocalDateTime of = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(of);
        System.out.println(format);
        System.out.println(of);
    }
}
