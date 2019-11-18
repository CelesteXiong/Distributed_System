package DSPPCode.storm.slide_count_window;

import com.google.inject.internal.util.$ToStringBuilder;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.Collections;

public class SlideCountWindowBoltImpl extends SlideCountWindowBolt{
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String key = tuple.getString(0);
        String value = tuple.getString(1);
        if (counter.containsKey(key))
        {
            String values = counter.get(key).get(0) + value;
            Integer count = Integer.parseInt(counter.get(key).get(1)) + 1;
            String window_num = counter.get(key).get(2);
            String New_window_num = window_num;
            String new_value = values; //


            if (count == 2) {
                String emit = outputFormat(key, values, window_num);
                basicOutputCollector.emit(Collections.singletonList(emit));

                new_value = values.substring(values.length()-1, values.length());
                count = 0;
                Integer temp =(Integer.parseInt(window_num)+1);
                New_window_num = temp.toString();
            }
            ArrayList<String> value_put = new ArrayList<>();
            value_put.add(new_value);
            value_put.add(count.toString());
            value_put.add(New_window_num);
            counter.put(key,value_put);
        }
        else
        {
            ArrayList<String> value_put = new ArrayList<>();
            value_put.add(value); //value
            value_put.add("1");  //count
            value_put.add("1"); //window_num
            counter.put(key, value_put);
        }
    }
}
