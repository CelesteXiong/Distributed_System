package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class TwitterMain {
    public int run(String[] args) throws Exception {
        // 自动获取执行模式 <本地 or 集群>
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为1
        env.setParallelism(1);
        // 设置数据源
        DataStream<String> streamSource = env.addSource(new Source(args[0]));
        // 解析JSON
        DataStream<Tuple2<String, Integer>> frequentWord = streamSource
                .flatMap(new ParseJsonAndSplitImpl())
                .keyBy(0)
                .sum(1)
                .filter(new FilterImpl(args[1]));
        // 将结果写入文件
        frequentWord.writeAsText(args[2]);
        // 执行作业，设置作业名
        env.execute("Twitter json filter");

        return 0;
    }
}
