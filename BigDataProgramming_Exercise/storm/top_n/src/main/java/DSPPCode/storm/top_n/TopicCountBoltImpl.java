package DSPPCode.storm.top_n;

import DSPPCode.storm.top_n.util.HashTags;
import org.apache.flink.api.java.typeutils.runtime.ValueComparator;

import java.util.*;
import java.util.regex.Pattern;

public class TopicCountBoltImpl extends TopicCountBolt{
    @Override
    void processTweet(String tweet) {
//        System.out.println(tweet);
//        ArrayList<String> topic = new ArrayList<>();
        if (tweet.equals("stop!"))
        {
            reportTopNToPrinter();
            return;
        }
//        String [] sub_tweets = tweet.split("#");
//        Pattern pattern = Pattern.compile("^[A-Za-z]+");
//        if (sub_tweets.length==0) return;
        HashTags hashtags = new HashTags();
        List<String> topics = hashtags.getHashTags(tweet);
        for (String topic: topics)
        {
            if (counter.containsKey(topic))
            {
                counter.put(topic, counter.get(topic)+1);
            }
            else
            {
                counter.put(topic,1);
            }
        }
    }

    @Override
    void reportTopNToPrinter() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.addAll(counter.entrySet());
        ValueComparator vc = new ValueComparator();
        Collections.sort(list,vc);
        Iterator<Map.Entry<String, Integer>> iter = list.iterator();
        String result = "";
        int i = 0;
        while (iter.hasNext() && i<topN)
        {
            Map.Entry<String,Integer> entry = iter.next();
            result += entry.getKey()+","+entry.getValue()+"\n";
            i += 1;
        }
        result.trim();
        collector.emit(Collections.singletonList(result));

    }
    private class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return (o2.getValue() - o1.getValue());
        }
    }

}
