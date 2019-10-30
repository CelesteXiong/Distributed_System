package DSPPCode.spark.pagerank;

import org.spark_project.guava.collect.Iterables;
import scala.Tuple2;

import java.util.Iterator;
import java.util.*;

public class FlatMapToPairImpl extends FlatMapToPair{
    @Override
    public Iterator<Tuple2<String, Double>> call(Tuple2<Iterable<String>, Double> outsideWeight) throws Exception {
        List<Tuple2<String, Double>> output = new ArrayList<>();
        int num = 0;
//        for (String Id : outsideWeight._1)
//        {
//            num+=1;
//        }
        num = Iterables.size(outsideWeight._1);
        for (String Id : outsideWeight._1)
        {
            output.add(new Tuple2(Id,outsideWeight._2/num));
        }
        return output.iterator();
    }
}
