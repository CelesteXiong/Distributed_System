package DSPPTest.student.storm.top_n;

import DSPPCode.storm.top_n.PrinterBolt;
import DSPPCode.storm.top_n.TopicCountBolt;
import DSPPCode.storm.top_n.TopicCountBoltImpl;
import DSPPCode.storm.top_n.TweetSpout;
import DSPPTest.student.TestTemplate;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.daemon.supervisor.ReadClusterState;
import org.apache.storm.topology.TopologyBuilder;
import org.junit.Test;

import java.io.File;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

public class TopNTest extends TestTemplate {

    @Test
    public void test() throws Exception {
        //设置路径
        String inputFile = root + "/storm/top_n/input";
        String outputFolder = outputRoot + "/storm/top_n";
        String outputFile = outputFolder + "/top_n";
        String answerFile = root + "/storm/top_n/answer";

        //删除旧输出
        deleteFolder(outputFolder);

        //执行
        String topologyName = "topn";
        int topN = 5;
        String spoutid = "tweet_spout";
        String countid = "counter";
        String printerid = "printer";

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(spoutid, new TweetSpout(inputFile));
        TopicCountBolt topicCountBolt = new TopicCountBoltImpl();
        topicCountBolt.setTopN(topN);
        builder.setBolt(countid, topicCountBolt).shuffleGrouping(spoutid);
        builder.setBolt(printerid, new PrinterBolt(outputFile, 1)).globalGrouping(countid);

        Logger.getLogger(ReadClusterState.class).setLevel(Level.FATAL);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topologyName, new Config(), builder.createTopology());
        long t = System.currentTimeMillis();
        while (System.currentTimeMillis() - t < 60000) {
            File file = new File(outputFile);
            if (file.exists()) {
                break;
            }
            Thread.sleep(1000);
        }
        cluster.killTopology(topologyName);
        cluster.shutdown();

        //检验结果
        verifyKV(readFile2String(outputFile), readFile2String(answerFile));

        System.out.println("恭喜通过~");
    }

}
