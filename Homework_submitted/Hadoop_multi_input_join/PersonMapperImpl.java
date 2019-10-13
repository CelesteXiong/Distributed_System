package DSPPCode.hadoop.multi_input_join;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 处理 Person 表中的数据，根据结果集保留对应的数据，传递到 Reducer 端
 */
public class PersonMapperImpl extends PersonMapper{
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] table = value.toString().split(DELIMTER);
    String Id_P = table[0];
    String LastName = table[1];
    String FirstName = table[2];
    context.write(new Text(Id_P), new TextPair( new Text(LastName + DELIMTER + new Text(FirstName)), new Text("person")));
    }
}
