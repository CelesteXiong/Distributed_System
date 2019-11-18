package DSPPCode.storm.warm_up;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Java答案示例
 */
public class CountBoltImpl extends CountBolt {

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        counter = new HashMap<>();
    }

    public void execute(Tuple tuple) {
        String word = tuple.getString(0);
        if (word.equals("stop!")) {
            String result = "";
            Iterator<Map.Entry<String, Integer>> iter = counter.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = iter.next();
                result += entry.getKey() + "," + entry.getValue() + "\n";
            }
            result.trim(); //Returns a string whose value is this string, with any leading and trailing whitespace removed.
            collector.emit(new Values(result));
        } else {
            if (counter.containsKey(word)) {
                counter.put(word, counter.get(word) + 1);
            } else {
                counter.put(word, 1);
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("result"));
    }

}
