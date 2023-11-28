package audio.client;

import audio.v3.PyResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2023/6/2
 */
public class test {
    public static void main(String[] args) {
        A a = new A();
        String excute = a.excute(new CallbackInterface() {
            @Override
            public String process(List<Integer> params) {
                return params.toString();
            }
        });
        System.out.println(excute);
    }
}
