package DSPPCode.flink.stream_warm_up;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class WordCountRunner {

    public static void run(SourceFunction<String> source, String outputFile) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        new WordCountImpl().wordCount(env.addSource(source)).writeAsCsv(outputFile);
        env.execute();
    }

}
