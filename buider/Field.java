package buider;

import lombok.Data;

import java.util.List;

/**
 * x.z
 * Create in 2023/5/30
 */
@Data
public class Field extends Table{
    private String name;
    // 接口为不同函数提供实现
    private SqlFunction function;
    private List<String> params;
}
