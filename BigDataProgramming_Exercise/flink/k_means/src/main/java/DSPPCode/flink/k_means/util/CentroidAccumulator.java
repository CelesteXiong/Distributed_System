package DSPPCode.flink.k_means.util;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 求和
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class CentroidAccumulator implements ReduceFunction<Tuple3<Integer, Point, Long>> {
    @Override
    public Tuple3<Integer, Point, Long> reduce(Tuple3<Integer, Point, Long> input1, Tuple3<Integer, Point, Long> input2) throws Exception {
        return Tuple3.of(input1.f0, input1.f1.add(input2.f1), input1.f2 + input2.f2);
    }
}
