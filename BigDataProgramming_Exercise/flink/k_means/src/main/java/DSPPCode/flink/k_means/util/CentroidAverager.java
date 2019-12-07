package DSPPCode.flink.k_means.util;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 求平均值
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class CentroidAverager implements MapFunction<Tuple3<Integer, Point, Long>, Centroid> {

    @Override
    public Centroid map(Tuple3<Integer, Point, Long> value) throws Exception {
        return new Centroid(value.f0, value.f1.div(value.f2));
    }
}