package DSPPCode.flink.k_means;

import DSPPCode.flink.k_means.util.Centroid;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.FilterOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

public class TerminationCriterionImpl extends  TerminationCriterion {
    @Override
    public FilterOperator<Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>> getTerminatedDataSet(DataSet<Centroid> newCentroids, DataSet<Centroid> oldCentroids) {
        FilterOperator<Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>> result = newCentroids.join(oldCentroids)
                .where("id").equalTo("id")
                .map(new MapFunction<Tuple2<Centroid, Centroid>, Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>>() {
                    @Override
                    public Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>> map(Tuple2<Centroid, Centroid> CentroidTuple2) throws Exception {

                        Tuple3<Integer, Double, Double> c1 = new Tuple3(CentroidTuple2.f0.id, CentroidTuple2.f0.x, CentroidTuple2.f0.y);
                        Tuple3<Integer, Double, Double> c2 = new Tuple3(CentroidTuple2.f1.id, CentroidTuple2.f1.x, CentroidTuple2.f1.y);
                        return new Tuple2(c1,c2);
                    }
                })
                .filter(new FilterFunction<Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>>() {
                    @Override
                    public boolean filter(Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>> Tuples) throws Exception {
                        Tuple3<Integer, Double, Double> c_1 = Tuples.f0;
                        Tuple3<Integer, Double, Double> c_2 = Tuples.f1;
                        double delta = Math.sqrt((c_1.f1 - c_2.f1) * (c_1.f1 - c_2.f1) + (c_1.f2 - c_2.f2) * (c_1.f2 - c_2.f2));
                        if (delta < EPSILON)
                            return false;
                        else
                            return true;
                    }
                });
        return result;
    }
}
