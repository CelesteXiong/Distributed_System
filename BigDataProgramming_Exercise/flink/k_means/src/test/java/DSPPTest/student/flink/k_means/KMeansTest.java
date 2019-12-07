package DSPPTest.student.flink.k_means;

import DSPPCode.flink.k_means.KMeansMain;
import DSPPTest.student.TestTemplate;
import org.junit.Test;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-04
 */
public class KMeansTest extends TestTemplate {
    @Test
    public void test() throws Exception {
        String[] args = new String[3];

        args[0] = root + "/flink/k_means/points";
        args[1] = root + "/flink/k_means/centers";
        args[2] = outputRoot + "/flink/k_means/output";

        String outputFile = args[2];
        String answerFile = root + "/flink/k_means/answer";
        System.out.println(outputFile);
        // 删除旧输出
        deleteFolder(outputFile);

        new KMeansMain().runJob(args);

        // 检验结果
        verifyKV(readFile2String(outputFile), readFile2String(answerFile), 0.01);

        System.out.println("恭喜通过~");
    }
}
