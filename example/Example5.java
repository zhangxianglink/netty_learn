package example;

import audio.client.A;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.io.IOException;
import java.util.Map;

/**
 * x.z
 * Create in 2023/7/31
 */
public class Example5 {
    public static void main(String[] args) throws IOException {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        // 加入自定义函数
        instance.addFunction(new AddFunc());
        Double excute = (Double) instance.execute("add(1,2)");
        System.out.println(excute);
    }
}

class AddFunc extends AbstractFunction{

    @Override
    public String getName() {
        return "add";
    }

    /**
     * @param env 当前执行上下文
     * @param arg1 第一个参数
     * @param arg2 第二个参数
     * @return  函数返回值
     */
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number a = FunctionUtils.getNumberValue(arg1, env);
        Number b = FunctionUtils.getNumberValue(arg2, env);
        return new AviatorDouble(a.doubleValue() + b.doubleValue());
    }
}
