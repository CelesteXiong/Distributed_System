package DSPPCode.spark.pagerank;

import scala.Tuple2;

public class CalculateRankImpl extends CalculateRank {
    @Override
    public Tuple2<String, Double> call(Tuple2<String, Iterable<Double>> weight) throws Exception {
        Double PR = 0.0;
        for (Double pr : weight._2)
        {
            PR += pr;
        }
        return new Tuple2(weight._1, PR*FACTOR + (1 - FACTOR));
    }
}
