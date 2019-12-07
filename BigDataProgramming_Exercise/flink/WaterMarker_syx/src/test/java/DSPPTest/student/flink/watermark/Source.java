package DSPPTest.student.flink.watermark;

import DSPPCode.flink.watermark.Watermark;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;

import static DSPPTest.util.FileOperator.getBufferedReader;

class Source implements SourceFunction<Tuple2<Long, Integer>> {

    private final String inputPath;

    Source(String inputPath) {
        this.inputPath = inputPath;
    }

    public void run(SourceContext<Tuple2<Long, Integer>> sc) throws Exception {
        BufferedReader br = getBufferedReader(inputPath);
        String line;

        while ((line = br.readLine()) != null) {
            String[] tuple = line.split(",");
            sc.collect(new Tuple2<>(Watermark.FORMAT.parse(tuple[0]).getTime(), Integer.parseInt(tuple[1])));
            Thread.sleep(200);
        }

        br.close();
    }

    public void cancel() {
    }

}
