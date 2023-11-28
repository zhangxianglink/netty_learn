package example;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * x.z
 * Create in 2023/7/31
 * 客户端版本控制，（有些功能低版本不支持，高版本废弃）
 */
public class ClientRule {
    public static void main(String[] args) {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        // 默认开启缓存
        instance.setCachedExpressionByDefault(true);
        // 使用LRU缓存，最大值为100个。
        instance.useLRUExpressionCache(100);
        // 注册内置函数，版本比较函数。
        instance.addFunction(new VersionFunc());

        String rule = "if(device==bil){\n" +
                "    return false;\n" +
                "}\n" +
                "\n" +
                "## 控制android的版本\n" +
                "if (device==\"Android\" && compareVersion(version,\"1.38.1\")<0){\n" +
                "    return false;\n" +
                "}\n" +
                "\n" +
                "return true;";

        // 执行参数
        Map<String, Object> env = new HashMap<>();
        env.put("device", "Android");
        env.put("version","1.38.1");
        //编译脚本

        Expression expression = instance.compile("1", rule, true);
        //执行脚本
        boolean isMatch = (boolean) expression.execute(env);
        System.out.println(isMatch);

    }
}
class VersionFunc extends AbstractFunction {

    @Override
    public String getName() {
        return "compareVersion";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        // 获取版本
        String version1 = FunctionUtils.getStringValue(arg1, env);
        String version2 = FunctionUtils.getStringValue(arg2, env);
        int n = version1.length();
        int m = version2.length();
        int i = 0, j = 0;
        // 下面代码就是获取每一个段的信息，然后进行比较
//        x 1
//        x 38
//        y 1
        while (i < n || j < m) {
            int x = 0;
            for (; i < n && version1.charAt(i) != '.'; ++i) {
                x = x * 10 + version1.charAt(i) - '0';
            }
            ++i; // 跳过点号
            int y = 0;
            for (; j < m && version2.charAt(j) != '.'; ++j) {
                y = y * 10 + version2.charAt(j) - '0';
            }
            ++j; // 跳过点号
            if (x != y) {
                return x > y ? new AviatorBigInt(1) : new AviatorBigInt(-1);
            }else {
                System.out.println("x "+ x);
                System.out.println("y "+ y);
            }
        }
        return new AviatorBigInt(0);
    }
}
