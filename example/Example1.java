package example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * x.z
 * Create in 2023/7/31
 */
public class Example1 {
    public static void main(String[] args) {
        String str = "a - (b - c) > 100";
        Expression expression = AviatorEvaluator.compile(str);
        double a=100.3,b=45,c= -199.100;
        HashMap<String, Object> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        Boolean execute = (Boolean) expression.execute(map);
        System.out.println("执行结果：" + execute);

        // 高精度计算
        String str2 = "a + b";
        BigDecimal b1 = BigDecimal.valueOf(1.23534);
        BigDecimal b2 = BigDecimal.valueOf(3.12121);
        Expression expression2 = AviatorEvaluator.compile(str2);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("a", b1);
        map2.put("b", b2);
        Object execute1 = expression2.execute(map2);
        System.out.println(execute1);
    }
}
