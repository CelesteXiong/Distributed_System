package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.api.java.tuple.Tuple2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterImpl extends Filter {
    /**
     * 给路径赋值，调用读取停词文件数据方法
     * 在实现类Impl中需要实现此构造方法
     *
     * @param stopWordPath 文件路径
     */
    FilterImpl(String stopWordPath) throws IOException {
        super(stopWordPath);
    }

    @Override
    public void readStopWords(String stopWordPath) throws IOException {
        FileReader fr = new FileReader(stopWordPath);
        BufferedReader br = new BufferedReader(fr);
        String stopword;
        while ((stopword = br.readLine()) != null)
        {
            stopWords.add(stopword);
        }
    }

    @Override
    public boolean filter(Tuple2<String, Integer> wordcount) throws Exception {
//        HashMap<String, Integer> hashmap;
        System.out.println(wordcount.f0 +"\t"+ wordcount.f1);

        if (stopWords.contains(wordcount.f0) || wordcount.f1<4)
        {

            return false;
        }

        else
        {
//            System.out.println(wordcount.f0 +"\t"+ wordcount.f1);
            return true;

        }
    }
}
