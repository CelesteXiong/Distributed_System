package DSPPCode.storm.slide_count_window;

import DSPPCode.storm.slide_count_window.util.FileProcess;
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

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-04
 */
public class FileReadSpout extends BaseRichSpout {

    SpoutOutputCollector collector;
    String inputFile;
    private List<String> words;
    private int pointer;
    BufferedReader reader;
    public FileReadSpout(String dataSource) {
        inputFile = dataSource;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        words = new ArrayList<>();
        BufferedReader reader = FileProcess.getReader(inputFile);
        String line = FileProcess.read(reader);
        this.pointer = 0;
        while (line != null && !line.equals("")) {
            words.add(line);
            line = FileProcess.read(reader);
        }
    }
    @Override
    public void nextTuple() {
        if (this.pointer < words.size()) {
            String word = words.get(this.pointer);
            String[] keyValue = word.split(" ");
            this.pointer++;
            collector.emit(new Values(keyValue[0],keyValue[1]));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("key","value"));
    }
}
