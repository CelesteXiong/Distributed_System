package DSPPCode.hadoop.select;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import javax.naming.Context;
import java.io.IOException;

public class SelectMapperImpl extends SelectMapper {
    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String compared_string = "shanghai";
        if (value.toString().contains("shanghai")) {
            context.write(value, NullWritable.get());
        }
    }
}