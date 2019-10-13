package DSPPCode.hadoop.multi_input_join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import scala.xml.Null;

import java.io.IOException;
/**
 * 处理 Order 表的数据，根据结果集保留对应的数据，传递到 Reducer 端
 */
public class OrderMapperImpl extends OrderMapper{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] table = value.toString().split(DELIMTER);
    String OrderNo = table[1];
    String Id_P = table[2];
    context.write(new Text(Id_P), new TextPair(new Text(OrderNo), new Text("order")));

    }
}
