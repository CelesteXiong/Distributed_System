package DSPPCode.flink.k_means;

import DSPPCode.flink.k_means.util.Centroid;
import DSPPCode.flink.k_means.util.Point;
import org.apache.flink.api.java.DataSet;

import java.io.Serializable;

/**
 *
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
abstract public class IterationStep implements Serializable {

    /**
     * TODO://利用已有工具类（k_means->util）实现kmeans运算迭代步
     * @return 返回迭代一次后的中心点坐标
     * @param points 数据点 <x,y> e.g. (32.05 -32.08)
     * @param centroid  中心点 <id, x, y> e.g. (1 30.01 -30.02)
     * */
    abstract public DataSet<Centroid> runStep(DataSet<Point> points, DataSet<Centroid> centroid);
}
