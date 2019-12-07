package DSPPTest.student.flink.capacity_monitor;

import DSPPCode.flink.capacity_monitor.CapacityMonitor;
import DSPPTest.student.TestTemplate;
import org.junit.Test;

import static DSPPTest.util.FileOperator.deleteFolder;
import static DSPPTest.util.FileOperator.readFile2String;
import static org.junit.Assert.assertEquals;

public class CapacityMonitorTest extends TestTemplate {

    @Test
    public void test() throws Exception {
        // 设置路径
        String inputFile = root + "/flink/capacity_monitor/input";
        String outputFile = outputRoot + "/flink/capacity_monitor";
        String answerFile = root + "/flink/capacity_monitor/answer";

        // 删除旧输出
        deleteFolder(outputFile);

        // 执行
        CapacityMonitor.run(inputFile, outputFile);

        // 检验结果
        assertEquals(readFile2String(outputFile), readFile2String(answerFile));

        System.out.println("恭喜通过~");
    }

}
