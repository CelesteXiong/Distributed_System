package DSPPCode.flink.k_means;

import DSPPCode.flink.k_means.util.*;
import org.apache.flink.api.java.DataSet;

public class IterationStepImpl extends IterationStep {
    @Override
    public DataSet<Centroid> runStep(DataSet<Point> points, DataSet<Centroid> centroid) {
        DataSet<Centroid> newCentroids = points.map(new SelectNearestCenter()).withBroadcastSet(centroid,"centroids")
                .map(new CountAppender())
                .groupBy(0)
                .reduce(new CentroidAccumulator())
                .map(new CentroidAverager());
        return newCentroids;
    }
}
