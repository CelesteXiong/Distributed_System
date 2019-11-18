package DSPPCode.storm.warm_up;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

abstract public class CountBolt extends BaseRichBolt {

    OutputCollector collector;

    Map<String, Integer> counter;

    //TODO 请完成该函数
    abstract public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector);

    //TODO 请完成该函数
    abstract public void execute(Tuple tuple);

    //TODO 请完成该函数
    abstract public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer);

}
