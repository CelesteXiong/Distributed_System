package DSPPCode.flink.watermark;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;

public class Watermark {

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static void run(SourceFunction<Tuple2<Long, Integer>> source, String outputFile) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        TimestampWithWatermarkAssigner.setMaxOutOfOrder(1000 * 60 * 2);// 2 min

        env.addSource(source)
                .assignTimestampsAndWatermarks(new TimestampWithWatermarkAssignerImpl())
                .timeWindowAll(Time.minutes(3), Time.minutes(1))
                .apply(new AllWindowFunction<Tuple2<Long, Integer>, Tuple2<String, Integer>, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow window, Iterable<Tuple2<Long, Integer>> tuples,
                                      Collector<Tuple2<String, Integer>> collector) {
                        int sum = 0;
                        for (Tuple2<Long, Integer> tuple : tuples) {
                            sum += tuple.f1;
                        }
                        collector.collect(new Tuple2<>(
                                FORMAT.format(window.getStart()) + "-" + FORMAT.format(window.getEnd()), sum));
                    }
                })
                .writeAsText(outputFile);

        env.execute();
    }

}
