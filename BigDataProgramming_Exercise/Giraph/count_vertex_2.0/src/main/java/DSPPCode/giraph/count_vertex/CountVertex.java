package DSPPCode.giraph.count_vertex;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.*;

import java.io.IOException;

/**
 * 统计图的顶点数目
 */
public abstract class CountVertex extends BasicComputation<IntWritable, IntWritable,
        NullWritable, IntWritable> {
    @Override
    public abstract void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex, Iterable<IntWritable> iterable) throws IOException;
}
