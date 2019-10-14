package DSPPCode.hadoop.multi_join;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MultiJoinMapperImpl extends MultiJoinMapper {
    private String column = "";

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] table = value.toString().split("\t");
        if (table.length != 2 )
        {
            System.out.println("lack" + value.toString());
            return;
        }
        if (value.toString().equals("\n") || value.toString().equals("\r") || value.toString().equals("\r\n") || value.toString().isEmpty() || value.toString().equals(""))
        {
            System.out.println("kong hang");
            return;
        }

        if (table[0].equals("companyname")) {
            column = "companyname";
            return;
        } else if (table[1].equals("addressname")) {
            column = "addressname";
            return;
        }
        if (column.equals("companyname")) {
            context.write(new Text(table[1]), new Text("companyname\t" + table[0]));
//            System.out.println(table[1] +  new Text("companyname\t" + table[0]));
        } else if (column.equals("addressname")) {
            context.write(new Text(table[0]), new Text("addressname\t" + table[1]));
//            System.out.println(table[0] +  new Text("addressname\t" + table[1]));

        }
    }
}