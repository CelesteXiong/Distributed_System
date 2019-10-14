package DSPPCode.hadoop.multi_join;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

public class MultiJoinReducerImpl extends MultiJoinReducer {
    private int FLAG = 1;

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            if (FLAG == 1) {
                context.write(new Text("companyname"), new Text("addressname"));
                FLAG = 0;
            }
//            String addressid = key.toString();
            String addressname = "";
            ArrayList<String> companynames = new ArrayList<String>();
            for (Text value : values) {
                System.out.println(key.toString()+"\t"+ value.toString());
                String[] table = value.toString().split("\t");
//                System.out.println(table.length);
                if (table.length != 2 ) {return;}
                if (table[0].equals("addressname")) {
                    addressname = table[1];
                } else if (table[0].equals("companyname")) {
                    companynames.add(table[1]);
                }
            }
            if (addressname.equals("")) {return;}
            else if (companynames.isEmpty()) {return;}
            for (String companyname : companynames) {
                context.write(new Text(companyname), new Text(addressname));
                System.out.println(companyname + "\t"+ (addressname));
            }
        }

}
