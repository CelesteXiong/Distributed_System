package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-30
 */
abstract public class Filter implements FilterFunction<Tuple2<String, Integer>> {

    /**
     * 存储停词的数据结构
     * */
    List<String> stopWords = new ArrayList<>();

    /**
     * 给路径赋值，调用读取停词文件数据方法
     * 在实现类Impl中需要实现此构造方法
     * @param stopWordPath 文件路径
     * */
    Filter(String stopWordPath) throws IOException {
        readStopWords(stopWordPath);
    }


    /**
     * 调用逻辑已经写在构造方法中
     * 实现读取文件赋值给List<String> stopWords 功能
     * @throws  IOException 读取文件时的IO异常
     * @param stopWordPath 文件路径
     * */
    abstract public void readStopWords(String stopWordPath) throws IOException;

    /**
     *
     * 出现次数大于等于4的有意义的词汇需要保存
     * @return 是否需要保存 true or false
     * @param wordcount  上游传递的统计过后的数据
     * @throws Exception 调用方法时抛出异常
     * */
    @Override
    abstract public boolean filter(Tuple2<String, Integer> wordcount) throws Exception;
}
