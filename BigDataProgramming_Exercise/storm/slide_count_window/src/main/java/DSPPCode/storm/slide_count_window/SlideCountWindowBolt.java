package DSPPCode.storm.slide_count_window;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 实现简单滑动窗口，即
 * 接收到两次相同的Key就最对此Key最近三条数据进行append计算
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-04
 */
abstract public class SlideCountWindowBolt extends BaseBasicBolt {

    final String TITLE = "_WINDOW_";
    HashMap<String, ArrayList<String>> counter = new HashMap<>();

    /**
     * TODO: 实现此方法每次接收一个Tuple e.g. (a 1)将此tuple放入相应得窗口
     *       同一个key的Tuple每出现两次，对此key最近出现的三个元素进行一次计算 这里为append计算即
     *       (a 1) + (a 2) + (a 3) = (a 123)
     *     注意:emit操作使用outputFormat简化操作
     **/
    @Override
    abstract public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector);

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("result"));
    }

    /**
     * 输出格式化,emit时请使用此方法
     * @param key 此window处理的key值
     * @param value 输出结果
     * @param windowNum 窗口号
     * */
    public String outputFormat(String key, String value , String windowNum) {
        return key + TITLE + windowNum + "<--->[" + key + " " + value + "]" + "\n";
    }
}
