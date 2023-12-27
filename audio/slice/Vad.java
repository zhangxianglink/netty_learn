package audio.slice;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/5/15
 */
public class Vad {



    public static void main(String[] args) throws IOException {
        HashMap<Integer, ArrayList<String>> listHashMap = new HashMap<>();
        listHashMap.put(1, Lists.newArrayList("1.mp3"));
        listHashMap.put(2, Lists.newArrayList("2.mp3","x"));
        listHashMap.put(3, Lists.newArrayList("3.mp3","y"));
        System.out.println(listHashMap.values().stream().flatMap(List::stream).collect(Collectors.toList()));
    }
}
