package DSPPCode.flink.twitter_json_filter;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

/**
 * @author chenqh
 * @version 1.0.0
 * @date 2019-11-30
 */
abstract public class ParseJsonAndSplit implements FlatMapFunction<String, Tuple2<String, Integer>> {

    /**
     * flink用于解析JSON数据的工具类 使用方法与com.fasterxml.jackson.databind.JsonNode基本一致
     * */
    transient ObjectMapper jsonParser;

    /**
     * 对传入的JSON数据进行解析，找到用户语言为英文（"lang":"en"）的文本数据("text":"hello world")并对此文本数据进行分词
     * Hello World -> {(hello, 1), (world, 1)} 大写字母 要变为小写字母
     * 输入 String -> 输出 Collector<Tuple2<String, Integer>>
     * @param value 传入的一条数据
     * @param collector 数据收集器用于向下游发送数据
     * @throws Exception 异常
     * */
    @Override
    abstract public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception;

}
