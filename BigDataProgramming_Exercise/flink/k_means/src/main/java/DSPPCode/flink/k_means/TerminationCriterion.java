package DSPPCode.flink.k_means;

import DSPPCode.flink.k_means.util.Centroid;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.operators.FilterOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import java.io.Serializable;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
abstract public class TerminationCriterion implements Serializable {
    /** 允许误差 */
    static final double EPSILON = 0.000001;
    /**
     * TODO://使迭代 “应该” 终止时 此方法返回一个空值
     * @param newCentroids 新的中心点
     * @param oldCentroids 旧的中心点
     * @return Tuple2<经过比较之后的数据集> 注意数据格式 newc.join(oldc)
     * */
    abstract public FilterOperator<Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>> getTerminatedDataSet(DataSet<Centroid> newCentroids, DataSet<Centroid> oldCentroids);
}
