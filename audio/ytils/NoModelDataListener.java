package audio.ytils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x.z
 * Create in 2024/2/23
 */
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    List<List<Object>> list;
    public NoModelDataListener(List<List<Object>> list) {
        this.list = list;
    }

    @Override
     public void invoke(Map<Integer, String> data, AnalysisContext context) {
        List<Object> collect = data.values().stream().collect(Collectors.toList());
        list.add(collect);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("解析完了");
    }


}
