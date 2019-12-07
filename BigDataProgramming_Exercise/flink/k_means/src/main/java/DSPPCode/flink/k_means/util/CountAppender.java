package DSPPCode.flink.k_means.util;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 计数
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class CountAppender implements MapFunction<Tuple2<Integer, Point>, Tuple3<Integer, Point, Long>> {
    @Override
    public Tuple3<Integer, Point, Long> map(Tuple2<Integer, Point> input) throws Exception {
        return Tuple3.of(input.f0, input.f1, 1L);
    }
}
