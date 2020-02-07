package DSPPTest.student.giraph.count_vertex;


import DSPPCode.giraph.count_vertex.GiraphCountVertexRunner;
import DSPPTest.student.TestTemplate;
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
public class CountVertexTest extends TestTemplate {
    @Test
    public void test() throws Exception {
        String[] args = new String[3];
        args[0] = root + "/giraph/count_vertex/input/input.txt";
        args[1] = outputRoot + "/giraph/count_vertex/output";
        args[2] = root + "/giraph/count_vertex/answer";

        String outputFile = args[1] + "/part-m-00000";
        String answerFile = args[2];
        deleteFolder(args[1]);

        GiraphCountVertexRunner.run(args);

        verifyKV(readFile2String(outputFile), readFile2String(answerFile));
        System.out.println("恭喜通过~");
    }
}
