package audio.ytils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * x.z
 * Create in 2023/8/29
 */
public class PinYinObj {

    public static final ConcurrentHashMap<String,Integer> retryMap = new ConcurrentHashMap<>();
    public static void main(String[] args) throws IOException {
       retryMap.put("a",1);
       retryMap.put("b",2);
       retryMap.clear();
       retryMap.put("c",3);
        System.out.println(retryMap);
    }
}
