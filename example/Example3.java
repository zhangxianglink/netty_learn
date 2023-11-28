package example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.io.IOException;

/**
 * x.z
 * Create in 2023/7/31
 */
public class Example3 {
    public static void main(String[] args) throws IOException {
        //编译
        Expression exp = AviatorEvaluator.getInstance().compileScript("example/fw.av");
        //执行
        exp.execute();
    }
}
