package DSPPCode.storm.warm_up;

import DSPPCode.storm.warm_up.util.FileProcess;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordSpout extends BaseRichSpout {

    private String datasource;

    private SpoutOutputCollector collector;

    private List<String> words;

    private int index;

    private boolean isStop;

    public WordSpout(String datasource) {
        this.datasource = datasource;
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        index = 0;
        words = new ArrayList<>();
        isStop = false;
        BufferedReader br = FileProcess.getReader(datasource);
        String line = FileProcess.read(br);
        while (line != null && !line.equals("")) {
            String[] words2 = line.split(" ");
            for (String word : words2) {
                words.add(word);
            }
            line = FileProcess.read(br);
        }
        FileProcess.close(br);
    }

    public void nextTuple() {
        if (index < words.size()) {
            String word = words.get(index);
            index++;
            collector.emit(new Values(word));
        } else {
            if (isStop == false) {
                collector.emit(new Values("stop!"));
                isStop = true;
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }

}
