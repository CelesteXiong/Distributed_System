package DSPPCode.spark.shuffle_join;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

public class ShuffleJoinImpl extends ShuffleJoin {
    @Override
    public JavaRDD<String> join(JavaPairRDD<Long, String> personRdd, JavaPairRDD<Long, String> orderRdd) {
        JavaPairRDD<Long, Tuple2<String, String>> joinRdd = personRdd.join(orderRdd);
        System.out.println(joinRdd.values().collect());
        JavaRDD<String> output = joinRdd.values().map(
                (Tuple2<String, String> value) -> (value._1 + "," + value._2)
        );
        return output;
    }
}
