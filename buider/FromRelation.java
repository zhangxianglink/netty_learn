package buider;

import java.util.List;

/**
 * x.z
 * Create in 2023/5/30
 */
public class FromRelation implements Relation{
    @Override
    public void setParam(List<Object> params) {

    }

    private Table table;

    public void setTable(Table table){
        this.table = table;
    }

    @Override
    public String getRelationStr() {
        return " FROM " + table.getTableName();
    }
}
