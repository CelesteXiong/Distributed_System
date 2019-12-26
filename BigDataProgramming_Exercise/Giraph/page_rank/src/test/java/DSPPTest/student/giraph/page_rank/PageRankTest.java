package DSPPTest.student.giraph.page_rank;

import DSPPCode.giraph.page_rank.GiraphPageRankRunner;
import DSPPTest.student.TestTemplate;
import DSPPTest.util.Parser.LineParser;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-16
 */
public class PageRankTest extends TestTemplate {
    @Test
    public void test() throws Exception {
        String[] args = new String[3];
        args[0] = root + "/giraph/page_rank/input/input.txt";
        args[1] = outputRoot + "/giraph/page_rank/output";
        args[2] = root + "/giraph/page_rank/answer";

        String outputFile = args[1] + "/part-m-00000";
        String answerFile = args[2];
        deleteFolder(args[1]);

        GiraphPageRankRunner.run(args);

        verifyKV(readFile2String(outputFile), readFile2String(answerFile), 0.001);
        System.out.println("恭喜通过~");
    }
}
