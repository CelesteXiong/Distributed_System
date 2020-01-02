package DSPPTest.student.flink.stream_warm_up;

import DSPPCode.flink.stream_warm_up.WordCountRunner;
import DSPPTest.student.TestTemplate;
import org.junit.Test;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

public class WordCountTest extends TestTemplate {

    @Test
    public void test() throws Exception {
        // 设置路径
        String inputFile = root + "/flink/stream_warm_up/input";
        String outputFile = outputRoot + "/flink/stream_warm_up";
        String answerFile = root + "/flink/stream_warm_up/answer";

        // 删除旧输出
        deleteFolder(outputFile);

        // 执行
        WordCountRunner.run(new Source(inputFile, 1), outputFile);

        // 检验结果
        verifyKV(readFile2String(outputFile), readFile2String(answerFile));

        System.out.println("恭喜通过~");
    }

}
