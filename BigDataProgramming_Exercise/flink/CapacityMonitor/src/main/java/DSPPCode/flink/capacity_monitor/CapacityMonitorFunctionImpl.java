package DSPPCode.flink.capacity_monitor;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class CapacityMonitorFunctionImpl extends CapacityMonitorFunction {
    @Override
    public void flatMap(Tuple2<String, Integer> tuple, Collector<Tuple2<String, Boolean>> collector) throws Exception {
        System.out.println("--------------");
        System.out.println(tuple);
        // access the state value and update the newCount
        System.out.println(count.value());
        if (count.value() == null)
        {
            count.update(0);
        }
        Integer oldCount = count.value();
        Integer newCount = oldCount + tuple.f1;
        // update the count
        count.update(newCount);

        // emit the newCount
        if (oldCount <= THRESHOLD && newCount > THRESHOLD)
        {
            collector.collect(new Tuple2(tuple.f0, true));
        }
        if(oldCount > THRESHOLD && newCount <= THRESHOLD)
        {
            collector.collect(new Tuple2(tuple.f0, false));
        }
    }
}
