package DSPPCode.flink.capacity_monitor;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CapacityMonitor {

    public static void run(String inputFile, String outputFile) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        CapacityMonitorFunction.setThreshold(12);

        env.readTextFile(inputFile)
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String line) {
                        String[] tuple = line.split(",");
                        return new Tuple2<>(tuple[0], Integer.parseInt(tuple[1]));
                    }
                })
                .keyBy(0)
                .flatMap(new CapacityMonitorFunctionImpl())
                .map((Tuple2<String, Boolean> tuple) -> tuple.f0 + (tuple.f1 ? " is overwhelmed" : " is recovered"))
                .writeAsText(outputFile);

        env.execute();
    }

}
