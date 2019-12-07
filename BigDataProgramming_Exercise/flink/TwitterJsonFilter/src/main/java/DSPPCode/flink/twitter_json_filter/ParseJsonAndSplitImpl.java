package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

public class ParseJsonAndSplitImpl extends ParseJsonAndSplit{
    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
//        String parse = (jsonParser);
        jsonParser = new ObjectMapper();
        JsonNode node = jsonParser.readTree(value);
        String lang = node.get("user").get("lang").asText();
        if (!lang.equals("en"))
        {
            return;
        }
        String text = node.get("text").asText();
        String[] words = text.split(" ");
        HashMap<String, Integer> hashmap = null;
        Integer count = 0;
        for(String word:words)
        {
            Tuple2<String, Integer> tuple = new Tuple2<>(word.toLowerCase(),1);
            collector.collect(tuple);
        }
        return;
    }
}
