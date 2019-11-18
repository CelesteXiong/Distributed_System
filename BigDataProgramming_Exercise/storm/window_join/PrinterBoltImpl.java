package DSPPCode.storm.window_join;

import DSPPCode.storm.window_join.util.FileProcess;

import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;

public class PrinterBoltImpl extends PrinterBolt{
    public PrinterBoltImpl(String outputFile) {
        super(outputFile);
    }

    @Override
    public String parseTuple(Tuple tuple) {

//        String result = tuple.getValue(0)+" "+tuple.getValue(1)+" "+tuple.getValue(2)+"\n";
//        String result = tuple.getInteger(0)+"\t"+tuple.getString(1)+"\t"+tuple.getString(2)+"\n";
        String result = tuple.getValues().toString()+"\n";
        return result;
    }

    @Override
    public void saveResult(String outputFile, String result) {
        BufferedWriter bw = FileProcess.getWriter(outputFile);
        FileProcess.write(result, bw);
    }
}
