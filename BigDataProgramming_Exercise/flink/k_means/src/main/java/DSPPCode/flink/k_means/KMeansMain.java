package DSPPCode.flink.k_means;

import DSPPCode.flink.k_means.util.Centroid;
import DSPPCode.flink.k_means.util.Point;
import DSPPCode.flink.k_means.util.SelectNearestCenter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;

import java.io.Serializable;
import java.text.DecimalFormat;


/**
 *
 * Flink KMeans运算主函数
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class KMeansMain implements Serializable {
    private static final DecimalFormat FORMAT = new DecimalFormat("#0.00");

    public int runJob(String[] args) throws Exception {

        // 自动获取执行模式 <本地 or 集群>
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 设置默认并行度为1
        env.setParallelism(1);
        // 读取数据点
        DataSet<Point> points = env.readCsvFile(args[0])
                .fieldDelimiter(" ")
                .pojoType(Point.class, "x", "y");
        // 读取中心点
        DataSet<Centroid> centroids = env.readCsvFile(args[1])
                .fieldDelimiter(" ")
                .pojoType(Centroid.class, "id", "x", "y");
        // 设置迭代初始DataSet 这里为"loop"， 设置默认最大迭代次数为10000
        IterativeDataSet<Centroid> loop = centroids.iterate(10000);
        // 经过一次迭代，生成新的中心点  注意：需要同学们实现 IterationStepImpl类和它的runStep方法
        DataSet<Centroid> newCentroids = new IterationStepImpl().runStep(points, loop);
        // 满足“自定义迭代终止条件”后，最终生成的中心点  注意：需要同学们实现 自定义的迭代终止条件即 TerminationCriterionImpl类和其getTerminatedDataSet方法
        // 当getTerminatedDataSet返回值为空DataSet（也可以是DataSet的子类）时迭代终止
        DataSet<Centroid> finalCentroids = loop.closeWith(newCentroids, new TerminationCriterionImpl().getTerminatedDataSet(newCentroids, loop));
        // 将结果转换格式保留两位小数写入文件
        finalCentroids.map(new MapFunction<Centroid, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> map(Centroid centroid) throws Exception {
                return Tuple2.of(FORMAT.format(centroid.x), FORMAT.format(centroid.y));
            }
        }).writeAsCsv(args[2],"\n", ",");
        // 执行作业 设置作业名为K_means
        env.execute("K_means");

        return 0;
    }

}
