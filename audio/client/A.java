package audio.client;

import java.util.ArrayList;
import java.util.List;

/**
 * x.z
 * Create in 2023/6/2
 */
public class A {
    public List<Integer> workList = initList();
    public String excute(CallbackInterface callbackInterface){
        String result =  callbackInterface.process(workList);
        return result;
    }

    public List<Integer> initList(){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list;
    }
}
