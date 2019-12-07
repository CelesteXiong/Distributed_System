package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Source implements SourceFunction<String> {
    private String inputPath;

    private boolean running = true;

    public Source(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));
        String twitter = null;
        while ((twitter = bufferedReader.readLine()) != null && running) {
            sourceContext.collect(twitter);
            Thread.sleep(100);
        }

    }

    @Override
    public void cancel() {
        running = false;
    }
}
