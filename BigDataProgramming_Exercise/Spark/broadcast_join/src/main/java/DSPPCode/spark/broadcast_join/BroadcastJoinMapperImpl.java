package DSPPCode.spark.broadcast_join;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BroadcastJoinMapperImpl extends BroadcastJoinMapper{
    @Override
    public Iterator<String> call(String order) {
        List<String> result = new ArrayList<>();
        Map<Long, String> map = persons.getValue();
        String[] o = order.split(",");
        if(map.containsKey(Long.valueOf(o[2])))
        {
            result.add(map.get(Long.valueOf(o[2]))+","+o[1]);
        }
        return result.iterator();
    }
}
