package DSPPCode.hadoop.select;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class SelectReducerImpl extends SelectReducer {
    @Override
    public void reduce(Text key, Iterable<NullWritable> values, Context context)
        throws IOException, InterruptedException {
        //key: Text
        context.write(key, NullWritable.get());
    }

}
