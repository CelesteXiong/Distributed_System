package DSPPTest.student.flink.watermark;

import DSPPCode.flink.watermark.Watermark;
import DSPPTest.student.TestTemplate;
import org.junit.Test;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static DSPPTest.util.Verifier.verifyKV;

public class WatermarkTest extends TestTemplate {

    @Test
    public void test() throws Exception {
        // 设置路径
        String inputFile = root + "/flink/watermark/input";
        String outputFile = outputRoot + "/flink/watermark";
        String answerFile = root + "/flink/watermark/answer";

        // 删除旧输出
        deleteFolder(outputFile);

        // 执行
        Watermark.run(new Source(inputFile), outputFile);

        // 检验结果
        verifyKV(readFile2String(outputFile), readFile2String(answerFile));

        System.out.println("恭喜通过~");
    }

}
