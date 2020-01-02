package DSPPCode.flink.stream_warm_up;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;

abstract public class WordCount {

    // TODO 请完成该函数
    abstract public DataStream<Tuple2<String, Integer>> wordCount(DataStream<String> text);

}
