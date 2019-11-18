package DSPPCode.storm.top_n;

import DSPPCode.storm.top_n.util.FileProcess;
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

public class TweetSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private String datasource;
    private List<String> tweets;
    private int pointer;
    private boolean isStop = false;

    public TweetSpout(String inputFile) {
        this.datasource = inputFile;
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        tweets = new ArrayList<>();
        BufferedReader reader = FileProcess.getReader(datasource);
        String line = FileProcess.read(reader);
        this.pointer = 0;
        while (line != null && !line.equals("")) {
            tweets.add(line);
            line = FileProcess.read(reader);
        }
    }

    public void nextTuple() {
        if (this.pointer < tweets.size()) {
            String tweet = tweets.get(this.pointer);
            this.pointer++;
            collector.emit(new Values(tweet));
        } else {
            if (isStop == false) {
                collector.emit(new Values("stop!"));
                isStop = true;
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("tweet"));
    }

}

