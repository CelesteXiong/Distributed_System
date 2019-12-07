package DSPPCode.flink.k_means.util;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.Collection;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class SelectNearestCenter extends RichMapFunction<Point, Tuple2<Integer, Point>> {
    private Collection<Centroid> centroids;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.centroids = getRuntimeContext().getBroadcastVariable("centroids");
    }

    @Override
    public Tuple2<Integer, Point> map(Point p) throws Exception {

        double minDistance = Double.MAX_VALUE;
        int closestCentroidId = -1;

        for (Centroid centroid : centroids) {
            double distance = p.euclideanDistance(centroid);

            if (distance < minDistance) {
                minDistance = distance;
                closestCentroidId = centroid.id;
            }
        }

        return Tuple2.of(closestCentroidId, p);
    }
}
