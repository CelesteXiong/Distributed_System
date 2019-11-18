package DSPPCode.storm.warm_up;

import DSPPCode.storm.warm_up.util.FileProcess;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;

public class PrinterBolt extends BaseBasicBolt {

    private String outputFile;

    private StringBuilder outputs;

    private int size;

    public PrinterBolt(String outputFile, int size) {
        this.outputFile = outputFile;
        outputs = new StringBuilder();
        this.size = size;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // do nothing
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String result = tuple.getString(0);
        outputs.append(result).append("\n");
        size--;

        if (size == 0) {
            BufferedWriter bw = FileProcess.getWriter(outputFile);
            FileProcess.write(outputs.toString(), bw);
            FileProcess.close(bw);
        }
    }

}
