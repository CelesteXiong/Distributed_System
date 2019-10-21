package DSPPCode.hadoop.multi_input_join;
import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import scala.xml.Null;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据 OrderMapper 和 PersonMapper 传递过来的数据进行 Join 操作
 */
public class ReduceJoinReducerImpl extends ReduceJoinReducer{

    private static final Object DELIMTER = '\t';

    @Override
    protected void reduce(Text key, Iterable<TextPair> values, Context context) throws IOException, InterruptedException {
    // tp: textpair
        String person= "";
        ArrayList<String> orders = new ArrayList<String>();
        boolean match_person = false, mathch_order = false;
        for (TextPair tp:values) {
            Text flag = tp.getFlag();
            Text data = tp.getData(0);
            if (flag.toString().equals("person")) {
                person = data.toString();
                match_person = true;
            }
            else if (flag.toString().equals("order")) {
                orders.add(data.toString());
                mathch_order = true;
            }
        }

        for (String order: orders) {
            if  (person.equals("")) break;
            context.write(new Text(person+DELIMTER+order+DELIMTER), NullWritable.get());
            System.out.println(person+DELIMTER+order);
        }

    }
}
