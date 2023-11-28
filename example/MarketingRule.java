package example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.HashMap;

/**
 * x.z
 * Create in 2023/7/31
 * 营销规则编写
 */
public class MarketingRule {
    public static void main(String[] args) {
        String formDb = "if (amount>=100){\n" +
                "    return 200;\n" +
                "}elsif(amount>=500){\n" +
                "    return 100;\n" +
                "}else{\n" +
                "    return 0;\n" +
                "}";
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        Expression compile = instance.compile(formDb);
        HashMap<String,Object> map = new HashMap<String,Object>() {{
            put("amount", 300);
        }};
        Object execute = compile.execute(map);
        System.out.println(execute);
    }
}
