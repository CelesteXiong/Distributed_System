package DSPPTest.student.flink.twitter_json_filter;

import DSPPCode.flink.twitter_json_filter.FilterImpl;
import DSPPCode.flink.twitter_json_filter.ParseJsonAndSplitImpl;
import DSPPCode.flink.twitter_json_filter.TwitterMain;
import DSPPTest.student.TestTemplate;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;
import static DSPPTest.util.Verifier.verifyList;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-30
 */
public class TwitterJsonFilterTest extends TestTemplate {
    @Test
    public void test() throws Exception {

        String[] args = new String[3];
        // 设置路径

        args[0] = root + "/flink/twitter_json_filter/input";
        args[1] = root + "/flink/twitter_json_filter/stopWord";
        args[2] = outputRoot + "/flink/twitter_json_filter";

        String answerFile = root + "/flink/twitter_json_filter/answer";

        // 删除旧输出
        deleteFolder(args[2]);

        new TwitterMain().run(args);

        verifyList(readFile2String(answerFile), readFile2String(args[2]));

        System.out.println("恭喜通过~");

    }
}
