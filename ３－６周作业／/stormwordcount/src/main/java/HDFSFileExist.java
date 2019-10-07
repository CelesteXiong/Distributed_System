import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSFileExist {
    public static void main(String[] args){
        try{
            Configuration conf = new Configuration();

            if (args.length != 2) {
                // 参数设置： hdfs://localhost:9000 ./input/core-site.xml
                // 如果是分布式请填写主节点ip地址或者域名
                System.err.println("Usage: HDFSFileifExist <default file system> <file path>");
                System.exit(2);
            }

            /* set directory be tested whether a file exists*/
            // String fileName = "./input/file0.txt";
            String fileName = args[1];

            /* fs.defaultFS - NameNode URI - hdfs://host:port */
            // conf.set("fs.defaultFS", "hdfs://localhost:9000");
            conf.set("fs.defaultFS", args[0]);

            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            FileSystem fs = FileSystem.get(conf);
            if (fs.exists(new Path(fileName))){
                System.out.println("File/Directory exists");
            }
            else {
                System.out.println("File/Directory not exists!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
