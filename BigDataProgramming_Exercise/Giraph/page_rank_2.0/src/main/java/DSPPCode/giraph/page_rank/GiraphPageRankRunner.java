package DSPPCode.giraph.page_rank;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.io.formats.GiraphFileInputFormat;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.io.formats.InMemoryVertexOutputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-12-16
 */
public class GiraphPageRankRunner {

    public static int run(String[] args) throws Exception {

        String inputPath = args[0];
        String outputPath = args[1];

        GiraphConfiguration giraphConf = new GiraphConfiguration(new Configuration());

        //配置具体用户自定义应用计算类

        giraphConf.setComputationClass(PageRankImpl.class);

        giraphConf.setVertexInputFormatClass(JsonLongDoubleFloatDoubleVertexInputFormat.class);
        GiraphFileInputFormat.addVertexInputPath(giraphConf, new Path(inputPath));

        giraphConf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
        giraphConf.set(IdWithValueTextOutputFormat.LINE_TOKENIZE_VALUE, ",");
        System.out.println("计算需要1分钟，请稍等\n可尝试修改日志等级，对比输出内容的不同");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("预计" + sdf.format(new Date(System.currentTimeMillis() + 51000)) + "完成计算");
        giraphConf.set("giraph.logLevel", "error");
        giraphConf.setLocalTestMode(true);
        giraphConf.setWorkerConfiguration(1, 1, 100);
        GiraphConstants.SPLIT_MASTER_WORKER.set(giraphConf, false);
        InMemoryVertexOutputFormat.initializeOutputGraph(giraphConf);
        GiraphJob giraphJob = new GiraphJob(giraphConf, "CountVertex");
        FileOutputFormat.setOutputPath(giraphJob.getInternalJob(), new Path(outputPath));
        giraphJob.run(true);

        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + "完成计算");
        return 0;
    }
}
