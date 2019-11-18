package DSPPCode.storm.window_join;

import DSPPCode.storm.window_join.util.FileProcess;
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
abstract public class PrinterBolt extends BaseBasicBolt {
    String outputFile;

    public PrinterBolt(String outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        saveResult(outputFile, parseTuple(tuple));
        stop(parseTuple(tuple));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
    /**
     * 用于停止程序
     * */
    public void stop(String result) {
        if (result.equals("[12, female, 32]\n")) {
            BufferedWriter bw2 = FileProcess.getWriter(outputFile + "stop");
            FileProcess.write("stop", bw2);
            FileProcess.close(bw2);
        }
    }

    abstract public String parseTuple(Tuple tuple);

    abstract public void saveResult(String outputFile, String result);

}
