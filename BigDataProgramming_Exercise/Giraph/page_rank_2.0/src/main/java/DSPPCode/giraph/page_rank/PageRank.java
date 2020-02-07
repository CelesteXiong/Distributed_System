package DSPPCode.giraph.page_rank;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;

/**
 * 完成 PageRank 计算过程
 */
public abstract class PageRank extends BasicComputation<LongWritable, DoubleWritable,
        FloatWritable, DoubleWritable> {
    /**
     * PageRank 公式中的阻尼系数
     */
    static final double D = 0.5;
    static final int MAX_SUPERSTEP = 12;

    @Override
    public abstract void compute(Vertex<LongWritable, DoubleWritable, FloatWritable> vertex, Iterable<DoubleWritable> iterable) throws IOException;
}
