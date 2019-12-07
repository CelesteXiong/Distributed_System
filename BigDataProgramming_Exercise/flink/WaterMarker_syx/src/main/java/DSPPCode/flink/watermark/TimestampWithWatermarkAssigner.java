package DSPPCode.flink.watermark;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

abstract public class TimestampWithWatermarkAssigner implements AssignerWithPeriodicWatermarks<Tuple2<Long, Integer>> {

    // 给定的可容忍的时间跨度 t
    static long MAX_OUT_OF_ORDER;
    public long cuttentMaxTimeStamp;

    public static void setMaxOutOfOrder(long maxOutOfOrder) {
        MAX_OUT_OF_ORDER = maxOutOfOrder;
    }

    /**
     * TODO 请完成该函数
     *
     * @return 当前的水位线
     */
    @Nullable
    @Override
    abstract public Watermark getCurrentWatermark();

    /**
     * TODO 请完成该函数
     *
     * @param tuple 输入数据, 属性 1 为时间戳
     * @return tuple 的时间戳
     */
    @Override
    abstract public long extractTimestamp(Tuple2<Long, Integer> tuple, long unused);

}
