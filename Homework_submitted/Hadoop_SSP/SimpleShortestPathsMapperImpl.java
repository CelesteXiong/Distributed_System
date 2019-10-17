package DSPPCode.hadoop.ssp;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleShortestPathsMapperImpl extends SimpleShortestPathsMapper{
    @Override
    public void map(Text key, Text value, Mapper.Context context) throws IOException, InterruptedException {
//        System.out.println(StringUtils.split(value.toString(), "\t")[0].indexOf("("));

        System.out.println(("Map:\t" + key.toString() + "\t" + value.toString()));
        Node node = new Node();
        String distance = null;
        int counter = context.getConfiguration().getInt("run.counter", 1);

        /*judge whether 1st iteration or other iteration*/
        // first iteration, value: (nei_node, nei_dis)
        if (StringUtils.split(value.toString(), "\t")[0].indexOf("(") == 0 || StringUtils.split(value.toString(), "\t")[0].indexOf("（") ==0) {
//          if (counter ==0 ){
            if (key.toString().equals("A")) {
                distance = "0";
                context.write(key, new Text(distance + "\t" + value.toString()));
                node.FormatNode(distance + "\t" + value.toString());
                for (Integer num = 0; num<node.getNodeNum(); num++)
                {
                    String nei_node = node.getNodeKey(num);
                    String nei_distance = node.getNodeValue(num);
                    context.write(new Text(nei_node), new Text(nei_distance));
                }
            }
            else if (!key.toString().equals("A")) {
                distance = "inf";
                context.write(key, new Text(distance +"\t"+ value.toString()));
            }
        }
        //other iteration, value: distance\t(nei_node, nei_dis)
        else
        {
            context.write(key, value);
            node.FormatNode(value.toString());
            if (node.getDistance().equals("inf")) {
                return;
            }
            else if (!node.getDistance().equals("inf")) {
                for (Integer num = 0; num < node.getNodeNum(); num++) {
                    String nei_node = node.getNodeKey(num);
                    String nei_distance = node.getNodeValue(num);
                    Integer distance_to_A = Integer.parseInt(nei_distance) + Integer.parseInt(node.getDistance());
                    System.out.println(nei_node+"\t"+distance_to_A);
                    context.write(new Text(nei_node), new Text(String.valueOf(distance_to_A)));
                }
            }

        }
    }
}
