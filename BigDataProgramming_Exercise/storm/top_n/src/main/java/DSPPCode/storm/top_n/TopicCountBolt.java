package DSPPCode.storm.top_n;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.*;

/**
 * 输入:
 * 该Bolt接收的每个tuple即为一条tweet, tweet的形式如下:
 * Good Morning #monday #Saturday, Hello!
 * 其中紧跟着"#"的单词为话题, 不包含逗号, 即这条话题包含monday和Saturday两条话题,
 * 当且仅当接收的tuple内容为字符串stop!时, 输出Top N结果, 即执行reportTopNToPrinter,
 * 否则处理对接收到的tweet进行话题抽取与统计, 即执行processTweet.
 * -
 * 输出:
 * Top N个话题以及该话题出现的次数(区分大小写), 譬如:
 * Saturday,1
 * monday,1
 * 每个记录占据一行输出的结果.
 */

abstract public class TopicCountBolt extends BaseRichBolt {

    OutputCollector collector;

    Map<String, Integer> counter;

    int topN;

    public void setTopN(int topN) {
        this.topN = topN;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        counter = new HashMap<>();
    }

    @Override
    public void execute(Tuple tuple) {
        String tweet = tuple.getString(0);
        if (tweet.equals("stop!")) {
            reportTopNToPrinter();
        } else {
            processTweet(tweet);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("result"));
    }

    //TODO 请完成该函数
    abstract void processTweet(String tweet);

    //TODO 请完成该函数
    abstract void reportTopNToPrinter();

}
