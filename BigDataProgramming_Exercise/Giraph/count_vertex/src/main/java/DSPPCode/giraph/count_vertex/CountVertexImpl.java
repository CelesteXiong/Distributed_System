package DSPPCode.giraph.count_vertex;

import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import java.io.IOException;

import static DSPPCode.giraph.count_vertex.CountVertexMasterCompute.COUNT_VERTEX;

public class CountVertexImpl extends CountVertex{
    @Override
    public void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex, Iterable<IntWritable> iterable) throws IOException {

        aggregate(COUNT_VERTEX, new IntWritable(1));
        if(getSuperstep()==2)
        {
            IntWritable v1 = getAggregatedValue(COUNT_VERTEX);
            vertex.setValue(v1);
            vertex.voteToHalt();

        }
    }
}
