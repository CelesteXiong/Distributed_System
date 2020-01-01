package DSPPCode.flink.stream_warm_up;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Java答案示例
 */
public class WordCountImpl extends WordCount {

    @Override
    public DataStream<Tuple2<String, Integer>> wordCount(DataStream<String> text) {
        return text
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
                        for (String token : value.split("\\W+")) {
                            if (token.length() > 0) {
                                out.collect(new Tuple2<>(token, 1));
                            }
                        }
                    }
                })
                .keyBy(0)
                .timeWindow(Time.seconds(1), Time.seconds(1))
                .sum(1);
    }

}
