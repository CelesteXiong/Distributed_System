package DSPPCode.hadoop.ssp;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class SimpleShortestPathsReducerImpl extends SimpleShortestPathsReducer{
    @Override
    public void reduce(Text nodeKey, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String distance_to_A = "inf";
        String min_distance ="inf";
        String old_distance_to_A = null;
        Node node = new Node();
        String[] values_string;
        //values: {10 (C,1) (D,2)}, {8}, {12}}
        for (Text value: values)
        {
            values_string = StringUtils.split(value.toString(),"\t");
            //value: distance\t(nei_node, nei_distance)
            if (values_string.length > 1)
            {
                node.FormatNode(value.toString());
                distance_to_A =values_string[0];
                old_distance_to_A =values_string[0];
                if (!distance_to_A.equals("inf"))
                {
                    if (min_distance.equals("inf") )
                    {
                        min_distance = distance_to_A;
                    }
                    else if (Integer.parseInt(min_distance)>Integer.parseInt(distance_to_A))
                    {
                        min_distance = distance_to_A;
                    }
                }
            }
            //value: distance
            else if (values_string.length==1)
            {
//                min_distance = values_string[0];
                if (distance_to_A.equals("inf"))
                {
                    distance_to_A = values_string[0];
                }
                if(Integer.parseInt(values_string[0]) > Integer.parseInt(distance_to_A))
                {
                    min_distance = distance_to_A;
                    distance_to_A = min_distance;
                }
                else
                {
                    min_distance = values_string[0];
                    distance_to_A = min_distance;
                }
            }

        }
        node.setDistance(min_distance);
        System.out.println(nodeKey.toString()+"\t" + node.toString());
        context.write(nodeKey, new Text(node.toString()));
        node.setDistance(old_distance_to_A);
        isChange(node,min_distance,context);
    }
}


