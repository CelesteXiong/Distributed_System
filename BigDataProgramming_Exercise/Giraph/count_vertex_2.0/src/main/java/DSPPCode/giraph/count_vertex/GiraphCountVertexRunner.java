package DSPPCode.giraph.count_vertex;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.io.formats.*;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-16
 */
public class GiraphCountVertexRunner {

    public static int run(String[] args) throws Exception {
        String inputPath = args[0];
        String outputPath = args[1];

        GiraphConfiguration giraphConf = new GiraphConfiguration(new Configuration());

        //配置具体用户自定义应用计算类

        giraphConf.setComputationClass(CountVertexImpl.class);
        giraphConf.setMasterComputeClass(CountVertexMasterCompute.class);
        giraphConf.setVertexInputFormatClass(IntIntNullTextVertexInputFormat.class);

        GiraphFileInputFormat.addVertexInputPath(giraphConf, new Path(inputPath));

        giraphConf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
        giraphConf.set(IdWithValueTextOutputFormat.LINE_TOKENIZE_VALUE, ",");
        System.out.println("计算需要30秒钟，请稍等\n可尝试修改日志等级，对比输出内容的不同");

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println("预计" + df.format(new Date(System.currentTimeMillis() + 30000)) + "完成计算");
        giraphConf.set("giraph.logLevel", "error");
        giraphConf.setLocalTestMode(true);
        giraphConf.setWorkerConfiguration(1, 1, 100);
        GiraphConstants.SPLIT_MASTER_WORKER.set(giraphConf, false);
        InMemoryVertexOutputFormat.initializeOutputGraph(giraphConf);
        GiraphJob giraphJob = new GiraphJob(giraphConf, "GiraphCountVertex");
        FileOutputFormat.setOutputPath(giraphJob.getInternalJob(), new Path(outputPath));
        giraphJob.run(true);

        System.out.println(df.format(new Date(System.currentTimeMillis())) + "完成计算");
        return 0;
    }


}
