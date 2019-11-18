package DSPPCode.storm.top_n.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashTags {

    private static final Pattern hashtagPattern = Pattern.compile("\\#\\w+");

    public static List<String> getHashTags(String tweet) {
        List<String> hashTags = new ArrayList<>();
        Matcher matcher = hashtagPattern.matcher(tweet);
        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchend = matcher.end();
            String tmpHashTag = tweet.substring(matchStart + 1, matchend);
            hashTags.add(tmpHashTag.trim());
            tweet = tweet.substring(matchend);
            matcher = hashtagPattern.matcher(tweet);
        }
        return hashTags;
    }

    public static void main(String[] args) {
        String tweet = "Pausa pro café antes de embarcar no próximo vôo. #trippolisontheroad #danipolisviaja Pause " +
                "for… https://t.co/PhcJ4oYktP";
        List<String> hashtags = HashTags.getHashTags(tweet);
        for (String tag : hashtags) {
            System.out.println(tag);
        }
    }

}
