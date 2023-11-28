package example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.io.IOException;

/**
 * x.z
 * Create in 2023/7/31
 */
public class Example4 {
    public static void main(String[] args) throws IOException {
        //编译
        Expression exp = AviatorEvaluator.getInstance().compileScript("example/fn.av");
        //执行
        exp.execute();
    }
}
