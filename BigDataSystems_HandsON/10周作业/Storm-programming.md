# 1. 编写Storm程序

+ #### 新建Maven项目并添加pom依赖

    + [新建maven项目](../Create Maven Project/create maven.md)


~~~xml
+ 修改pom.xml 

pom.xml文件内容为

​```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>test</groupId>
    <artifactId>test</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.storm</groupId>
            <artifactId>storm-core</artifactId>
            <version>1.2.2</version>
            <!--<scope>provided</scope>-->
        </dependency>
    </dependencies>

    <build>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
​```

到目前为止编写Storm程序的基本环境已搭建好，下面介绍如何编写Storm的WordCount程序
~~~
+ #### IDE环境编写代码

一个基本的Strom程序由Spout、bolt和Topology三部分组成，Spout是是用于数据生成的组件，Bolt是Storm的计算基本单位可以称其为算子，Topology就是Spout和Boltd的摆放组合加上一些配置信息，本文将按照Spout,Bolt和Topology
这个顺序介绍Storm程序的编写

现在创建一个能够无限产生String数据的数据源Spout，在src/mian/java/目录下新建Java class取名RandomSentenceSpout
​     
 填写如下代码
 + 不启用ACK机制
   
 ```java
package example.storm.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/*
 定义一个Spout，用于产生数据。该类继承自BaseRichSpout
*/
public class RandomSentenceSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;
    private static final long serialVersionUID = 5028304756439810609L;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    @Override
    public void nextTuple() {

        // 睡眠一段时间后再产生一个数据
        Utils.sleep(1000);

        // 句子数组
        String[] sentences = new String[]{"the cow jumped over the moon", "an apple a day keeps the doctor away",
                "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"};

        // 随机选择一个句子
        String sentence = sentences[_rand.nextInt(sentences.length)];

        _collector.emit(new Values(sentence));
    }

    // 不使用ACK无需做任何操作
    @Override
    public void ack(Object id) { //非抽象方法可以删除

    }

    // 不使用ACK无需做任何操作
    @Override
    public void fail(Object id) { //非抽象方法可以删除

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 定义一个字段sentence
        declarer.declare(new Fields("sentence"));
    }
}
 ```
+ 启用ACK机制

```java
package example.storm.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/*
 定义一个Spout，用于产生数据。该类继承自BaseRichSpout
 */
public class RandomSentenceSpoutWithAck extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;
    private static final long serialVersionUID = 5028304756439810609L;
    // 其中key表示Spout-Tuple-id
    private HashMap<String, String> waitAck = new HashMap<String, String>();

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    @Override
    public void nextTuple() {

        // 睡眠一段时间后再产生一个数据
        Utils.sleep(1000);

        // 句子数组
        String[] sentences = new String[]{"the cow jumped over the moon", "an apple a day keeps the doctor away",
                "four score and seven years ago", "snow white and the seven dwarfs", "i am at two with nature"};

        // 随机选择一个句子
        String sentence = sentences[_rand.nextInt(sentences.length)];
        String spout_tuple_id = UUID.randomUUID().toString().replaceAll("-", "");
        // 发射该句子给Bolt

        waitAck.put(spout_tuple_id, sentence);
        //指定messageId，开启ackfail机制
        _collector.emit(new Values(sentence), spout_tuple_id);
    }

    // 确认函数
    @Override
    public void ack(Object id) {
        System.out.println("消息处理成功:" + id);
        System.out.println("删除缓存中的数据...");
        waitAck.remove(id);
    }

    // 处理失败的时候调用
    @Override
    public void fail(Object id) {
        System.out.println("消息处理失败:" + id);
        System.out.println("重新发送失败的信息...");
        //重发如果不开启ackfail机制，那么spout的map对象中的该数据不会被删除的。
        _collector.emit(new Values(waitAck.get(id)), id);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 定义一个字段sentence
        declarer.declare(new Fields("sentence"));
    }
}

```

数据源建立完毕，完成wordcount功能还需要Split和Count功能，因此需要创建src/main/java/SplitSentenceBolt.java

填写如下代码
```   
package example.storm.wordcount;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.StringTokenizer;

/*
 定义个Bolt，用于将句子切分为单词
 */
public class SplitSentenceBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        // 接收到一个句子
        String sentence = tuple.getString(0);
        // 把句子切割为单词
        StringTokenizer iter = new StringTokenizer(sentence);
        // 发送每一个单词//
        while (iter.hasMoreElements()) {
            collector.emit(new Values(iter.nextToken()));
        }
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 定义一个字段
        declarer.declare(new Fields("word"));
    }
}
```
创建main/java/CountBolt.java

填写如下代码
```    
package example.storm.wordcount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/*
 定义一个Bolt，用于单词计数
 */
public class CountBolt extends BaseBasicBolt {
    Map<String, Integer> counts = new HashMap<String, Integer>();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        // 接收一个单词
        String word = tuple.getString(0);
        // 获取该单词对应的计数
        Integer count = counts.get(word);
        if (count == null) {
            count = 0;
        }
        // 计数增加
        count++;
        // 将单词和对应的计数加入map中
        counts.put(word, count);
        System.out.println("(" + word + "," + count + ")");
        // 发送单词和计数（分别对应字段word和count）
        collector.emit(new Values(word, count));

    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 定义两个字段word和count
        declarer.declare(new Fields("word", "count"));
    }
}
```

现在把数据源Spout和计算逻辑组合起来形成一个拓扑图，创建src/main/java/WordCount.java
​    
填写如下代码
```
package example.storm.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.*;
import org.apache.storm.tuple.Fields;


/**
 ** 实现词频统计，不使用ACK机制
 */
public class WordCount {

    public static void main(String[] args) throws Exception {

        // 创建一个拓扑
        TopologyBuilder builder = new TopologyBuilder();
        // 设置Spout，这个Spout的名字叫做"Spout"，设置并行度为5
        builder.setSpout("Spout", new RandomSentenceSpout(), 5);
        // 设置Bolt——“split”，并行度为8，它的数据来源是spout的
        builder.setBolt("split", new SplitSentenceBolt(), 8).setNumTasks(9).shuffleGrouping("Spout");
        // 设置Bolt——“count”,并行度为12，它的数据来源是split的word字段
        builder.setBolt("count", new CountBolt(), 12).fieldsGrouping("split", new Fields("word"));
        // 参数设置
        Config conf = new Config();
        conf.setDebug(false);
        conf.setMaxTaskParallelism(3);

        if (args == null || args.length == 0) {
            System.err.println("缺少参数");
            return;
        } else {
            if (args[0].equals("cluster")) { //用于集群提交作业
                conf.setNumWorkers(2);
                conf.setNumAckers(2);
                StormSubmitter.submitTopology("word-count", conf, builder.createTopology());
            } else if (args[0].equals("local")) { //用于本地调试
                LocalCluster cluster = new LocalCluster();
                // 提交名为word-count的拓扑
                cluster.submitTopology("word-count", conf, builder.createTopology());
                Thread.sleep(10000);
                cluster.shutdown();
            } else {
                System.err.println("参数错误");
            }
        }
    }
}

```

# 2. 调试Storm程序
+ #### IDE中直接运行

Run->Edit-configuration->Main class  填写内容 `WordCount` 表示入口类  Program arguments 填写内容 `local` 表示本地运行 点击 `OK` 按钮

Run->Run ``WordCount``


结果如图

![](./picture/output.png)

+ #### 调试经验
    +  IDE中设置断点
    

# 3. 运行Storm程序


+ #### 利用IDE打包jar文件

    [IDEA打jar包教程](../IDEA/IDEA jar.md)

+ #### 伪分布模式下提交Storm程序

    经过上述步骤，我们已经得到打包好的Storm代码myjar.jar,接下来在Storm服务进程中运行Storm代码
    
    进入Storm目录：`cd ~/apache-storm-1.2.2/`
    
    执行命令： `bin/storm jar myjar.jar WordCountTopology local` (单机模式)
    
	`bin/storm jar myjar.jar WordCountTopology cluster` (分布式模式)
	​		   
	
+ #### 分布式模式下提交Storm程序

    在真实的生产环境中，一个Storm应用程序通常是运行在集群上以满足计算性能的需求。
    下面介绍如何在集群环境中运行Storm代码
    其实很简单，首先是上传jar包，执行scp命令将jar包上传至集群
    
    `scp myjar.jar usrname@ip:/home/usrname/apache-storm-1.2.2`
    
    再进入集群执行提交应用程序命令，进入Storm目录：`cd ~/apache-storm-1.2.2/`
    
    执行命令： `bin/storm jar myjar.jar WordCountTopology cluster` 
    
    
    这样一个Storm应用程序就在集群中跑起来了 
    
    **上述程序在cluster方式运行看不到输出，试修改以上程序使得cluster方式下将结果输出到文件中。**


![s3](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s3.png)