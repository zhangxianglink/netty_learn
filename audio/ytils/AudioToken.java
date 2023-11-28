package audio.ytils;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * x.z
 * Create in 2023/3/22
 */
public class AudioToken {
    public static void main(String[] args) {
        String jwt = extracted();
        System.out.println(jwt);
        Jws<Claims> secretKey = Jwts.parser().setSigningKey("asr2023").parseClaimsJws(jwt);
        System.out.println(secretKey);
        System.out.println(secretKey.getBody().getExpiration().getTime());

        for (int i = 0; i < 10; i++) {
            
        }
    }

    private static String extracted() {
        //设置密钥
        String secretKey = "asr2023";
        //设置主题
        String subject = "asr";
        //获取当前时间
        Date now = new Date();
        //设置过期时间
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.of(2024, 8, 1);
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());

        System.out.println(date);
        //生成JWT
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setIssuer("HangDong")
                .setAudience("user")
                .setIssuedAt(now)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwt;
    }

//    eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWFuZ2RvbmciLCJpYXQiOjE2Nzk2MTk2MDUsImV4cCI6MTY3OTcwNjAwNX0.4qdSEXetG8fHGx9wB29oKgbJyzoZgImYTzWtALwPg1Y
}
