import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class WriteHDFSFile {
    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                //args: hdfs://localhost:9000 ./write-test Hello,hadoop
                System.err.println("Usage: WriteHDFSFile <default file system> <path> <content>");
                System.exit(2);
            }

            Configuration conf = new Configuration();

            /* fs.defaultFS - NameNode URI - hdfs://localhost:port */
            // conf.set("fs.defaultFS", "hdfs://localhost:9000");
            conf.set("fs.defaultFS", args[0]);
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            FileSystem fs = FileSystem.get(conf);

            byte[] buff = args[2].getBytes(); //content to be written in
            String filename = args[1];

            FSDataOutputStream os = fs.create(new Path(filename));
            os.write(buff, 0, buff.length);
            System.out.println("Create:" + filename);
            os.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
