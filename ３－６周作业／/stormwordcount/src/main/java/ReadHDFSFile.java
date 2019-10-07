import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataInputStream;

public class ReadHDFSFile {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();

            if (args.length != 2) {
                // args: hdfs:localhost:9000 ./input/core-site.xml
                System.err.println("Usage: ReadHDFSFile <default file system> <path>");
                System.exit(2);
            }

            conf.set("fs.defaultFS", args[0]);
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            FileSystem fs = FileSystem.get(conf);

            String uri = args[1];

            Path file = new Path(uri);
            FSDataInputStream getIt = fs.open(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(getIt));
            String content = d.readLine(); // read one line
            while (content != null) {
                System.out.println(content);
                content = d.readLine();
            }

            d.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
