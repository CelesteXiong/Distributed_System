import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class ListHDFSFile {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();

        if (args.length != 2){
            //args: hdfs://localhost:9000 ./input
            System.err.println("Usage: listHdfsFile <default file system><path>");
            System.exit(2);
        }

        /* fs.defaultFS - NameNode URI -hdfs://localhost:9000 */
        //conf.set("fs.defaultFS", "hdfs://localhost:9000")
        conf.set("fs.defaultFS", args[0]);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        FileSystem fs = FileSystem.get(conf);

        /* set the directory for test*/
        //String uri = "./input"
        String uri = args[1];

        Path path = new Path(uri);
        FileStatus[] status = fs.listStatus(path);
        for (FileStatus fileStatus : status) {
            System.out.println(fileStatus.getPath().toString());
        }
        // 是一个Collection 子对象（即是容器）之后，就会允许使用增强的for 循环
        //形式，并自动取到c 的迭代器，自动遍历c 中的每个元素。
        fs.close();
    }
}
