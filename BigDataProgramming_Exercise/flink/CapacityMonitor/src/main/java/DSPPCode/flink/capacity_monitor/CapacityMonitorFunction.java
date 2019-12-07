package DSPPCode.flink.capacity_monitor;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

abstract public class CapacityMonitorFunction
        extends RichFlatMapFunction<Tuple2<String, Integer>, Tuple2<String, Boolean>> {

    // 单个区域可容纳的人数上限
    static int THRESHOLD;

    // 当前区域内人数
    transient ValueState<Integer> count;

    public static void setThreshold(int threshold) {
        THRESHOLD = threshold;
    }

    /**
     * TODO 请完成该函数
     *
     * 当区域内人数从正常变为大于 THRESHOLD 时发出警报, 即返回 (区域名, true)
     * 当区域内人数回归正常时解除警报, 即返回 (区域名, false)
     * 其余时刻不返回信息
     *
     * @param tuple     属性 1 为区域名称; 属性 2 为进出人数 (1 表示进入 1 人, -1 表示离开 1 人)
     * @param collector 用于存放返回数据
     */
    @Override
    abstract public void flatMap(Tuple2<String, Integer> tuple, Collector<Tuple2<String, Boolean>> collector)
            throws Exception;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<Integer> descriptor = new ValueStateDescriptor<>("count", Integer.class);
        count = getRuntimeContext().getState(descriptor);

    }

}
