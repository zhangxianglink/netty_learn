package buider;

import java.util.List;
import java.util.Objects;

/**
 * x.z
 * Create in 2023/5/30
 * 抽象成为接口，为各种关系提供不同的实现
 */
public interface Relation {
    void setParam(List<Object> params);
    String getRelationStr();
}
