package DSPPCode.spark.k_means;

import org.apache.spark.mllib.linalg.Vector;

import java.util.ArrayList;
import java.util.List;

public class KMeansImpl extends KMeans {
    @Override
    int closestPoint(Vector point, List<Vector> centers) {
        Double minDis = Double.MAX_VALUE;
        int idx = 0;
        int min_idx = 0;
        for (Vector center: centers)
        {
            double squaredDis = squaredDistance(point, center);
            if (minDis > squaredDis)
            {
                minDis = squaredDis;
                min_idx = idx;
            }
            idx += 1;
        }
        return min_idx;
    }

    @Override
    double squaredDistance(Vector point, Vector index) {
        double x_1 = point.toArray()[0];
        double y_1 = point.toArray()[1];
        double x_2 = index.toArray()[0];
        double y_2 = index.toArray()[1];
//        return Math.sqrt((x_1-x_2)*(x_1-x_2) + (y_1-y_2)*(y_1-y_2));
        return ((x_1-x_2)*(x_1-x_2) + (y_1-y_2)*(y_1-y_2));

    }
}
