package DSPPCode.giraph.page_rank;

import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;

public class PageRankImpl extends PageRank {
    @Override
    public void compute(Vertex<LongWritable, DoubleWritable, FloatWritable> vertex, Iterable<DoubleWritable> iterable) throws IOException {

        double edge_num = (double)vertex.getNumEdges();
        if (getSuperstep()==0)
        {
            sendMessageToAllEdges(vertex,new DoubleWritable(vertex.getValue().get()/edge_num));
        }
        else if(getSuperstep() < MAX_SUPERSTEP)
        {
            double pg_rank = (1-D) / getTotalNumVertices();
            for (DoubleWritable msg: iterable)
            {
                double pg_ratio = msg.get();
                pg_rank += D * pg_ratio;
            }
            vertex.setValue(new DoubleWritable(pg_rank));
            DoubleWritable output = new DoubleWritable(pg_rank/edge_num);
            sendMessageToAllEdges(vertex,output);
        }
        else vertex.voteToHalt();
    }
}
