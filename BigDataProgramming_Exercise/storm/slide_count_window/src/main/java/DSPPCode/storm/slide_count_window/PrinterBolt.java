package DSPPCode.storm.slide_count_window;

import DSPPCode.storm.slide_count_window.util.FileProcess;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;


/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-04
 */
public class PrinterBolt extends BaseBasicBolt {
    private String outputFile;
    public PrinterBolt(String outputFile) {
        this.outputFile = outputFile;
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {

        String result = tuple.getString(0);
        BufferedWriter bw = FileProcess.getWriter(outputFile);
        FileProcess.write(result, bw);
        FileProcess.close(bw);

        stop(result);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
    public void stop(String result) {
        // for online judge
        if (result.equals("b_WINDOW_3<--->[b 456]\n")) {
            BufferedWriter bw2 = FileProcess.getWriter(outputFile + "stop");
            FileProcess.write("stop", bw2);
            FileProcess.close(bw2);
        }
    }
}
