package DSPPCode.giraph.count_vertex;

import org.apache.giraph.aggregators.IntSumAggregator;
import org.apache.giraph.master.DefaultMasterCompute;

/**
 * Master 节点中处理来自各个 Worker 的 Aggregate Sum的值
 */
public class CountVertexMasterCompute extends DefaultMasterCompute {

    public static final String COUNT_VERTEX = "countVertex";

    @Override
    public void initialize() throws InstantiationException, IllegalAccessException {
        registerAggregator(COUNT_VERTEX, IntSumAggregator.class);
    }
}
