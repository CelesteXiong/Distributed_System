package DSPPTest.student.storm.slide_count_window;

import DSPPCode.storm.slide_count_window.SlideCountWindowBoltImpl;
import DSPPCode.storm.slide_count_window.FileReadSpout;
import DSPPCode.storm.slide_count_window.PrinterBolt;
import DSPPTest.student.TestTemplate;
import DSPPTest.util.Parser.KVParser;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.junit.Test;

import java.io.File;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

public class SlideCountWindowTest extends TestTemplate {
    @Test
    public void test() throws Exception {
        String inputFile = root + "/storm/slide_count_window/input";
        String outputFolder = outputRoot + "/storm/slide_count_window";
        String outputFile = outputFolder + File.separator + "storm/slide_count_window";
        String answerFile = root + "/storm/slide_count_window/answer";

        deleteFolder(outputFolder);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("Spout", new FileReadSpout(inputFile));
        builder.setBolt("count", new SlideCountWindowBoltImpl()).fieldsGrouping("Spout",new Fields("key"));
        builder.setBolt("print", new PrinterBolt(outputFile), 1).globalGrouping("count");
        Config conf = new Config();
        conf.setDebug(false);
        LocalCluster cluster = new LocalCluster();
        // 提交名为word-count的拓扑
        cluster.submitTopology("slide_count_window", conf, builder.createTopology());
        Thread.sleep(5000);
        cluster.killTopology("slide_count_window");
        cluster.shutdown();
        verifyKV(readFile2String(outputFile), readFile2String(answerFile),new KVParser(" "));
        System.out.println("恭喜通过～");
    }
}
