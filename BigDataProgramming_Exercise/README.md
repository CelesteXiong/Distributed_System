# 按照题型

## WordCount

1. 演化:
   - wordcount [all warmup]
   - topN [Storm]
   - TwitterJsonFilter [Flink]
   - slidecountWindow [storm]
2. 思路: 
   - 处理文件后(解析如Json),split
   - hashmap计数: 从1开始: `HashMap<String, Integer> hashmap;`
   - sort排序: 
     - ValueComparator
   - 流计算时注意判停词
3. 结合数据模型:
   - storm: spout/bolt, collector.emit()
   - spark: rdd.flatMap().mapToPair().ReduceByKey()
   - flink: datastram.flatMap().KeyBy(0).timeWindow().sum(1)
4. 补充:
   - flink: stateful wordcount https://github.com/dasebigdata-ecnu/BigDataSystems_Example/blob/master/flink/src/main/java/wordcount/MainWithDefinedState.java

## 迭代

1. 演化: 
   - K_means [Mapreduce/Spark/Flink]
   - PageRank [Giraph/Spark]
   - SSP [Mapreduce]
2. 思路: 
   - K_means: 在flink中结合map/reduce/groupby/filter等接口, 将function实现在这些接口中:
     - 初始化中心点;
     - 为每个点找到最近的中心点;
     - 判停;
     - 计算新的中心点;
   - PageRank: 
     - 根据公式和入边的rank值, 计算出边的rank值, 传递给出边所在节点;
   - SSP:
     - 首轮将A的distance设置为"0", 其他节点到A的distance设置为"inf";
     - 非首轮, 若本节点到A的距离非"inf", 则更新本节点的邻接点到A的距离为: 本节点到A的距离+邻接点到A的距离;
     - 判断是否需要继续迭代: isChange(node, 本次reduce得出的mindistance, context);

3. 补充: 
   - flink: pagerank: https://github.com/apache/flink/blob/master/flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java/graph/PageRank.java

## Join

1. 演化
   - Multi_join/multi_input_join[MR]
   - BroadcastJoin/ShuffleJoin [Spark]
   - WindowJoin [Storm]
2. 思路:
   - 注意join的悬浮数组的处理, 和数组的访问越界问题;
   - spark中BroadcastJoin: 将person表作为broadcast变量, ShuffleJoin: 两张表作为两个输入
   - storm中的WindowJoin: 作为流计算, 设置一个窗口, 在窗口内进行统计

# 按照处理引擎

## 流处理

1. 水位线
   - flink

## 图处理

## 批处理

# 按计算系统
1. hadoop: 
   - context.write(KEYOUT key, VALUEOUT value): 生成一组键值(Mapper-->Reducer)
   - context.getConfiguration().getInt("")
2. spark: rdd.map
3. storm: collector.emit()
4. flink: collector.collect()
5. giraph: vertex, aggregator, sendmessage etc

# 面向考试
总体思路: map--shuffle(groupby)--reduce--filter
##  文件
1. IO:读取文件(停用词)
   ```java
   // 读取
    FileReader fr = new FileReader(stopWordPath);
    BufferedReader br = new BufferedReader(fr);
    String stopword;
    while ((stopword = br.readLine()) != null)
    {
        stopWords.add(stopword);
    }
    // 写入
    String outputfile;
    StringBuilder outputs;
    BufferedWriter bw = FileProcess.getWriter(outputfile);
    FileProcess.write(outputs.toString(),bw);
    FileProcess.close(bw);
   ```
2. 解析: JsonParser
   ```java
   // 声明
   jsonparser = new ObjectMapper();
   // 解析json
   JsonNode node = jsonParser.readTree(value);
   .readValue()
   // 判断是否有关键字"field"
   .has("field");
   // 获取值
   .get("field"); //获取有关键字"field"的jsonnode
   String lang = node.get("user").get("lang").asText();//遇到叶子节点时用.asText()获取字符
   ```
## balabla
1. 广播变量:
   ```java
   .withBroadcastSet(dataset,String(dataset name));
   ```
## 编程
### MapReduce
```
(input) <k1, v1> -> 
map -> <k2, v2> -> 
combine -> <k2, v2> -> 
reduce -> <k3, v3> (output)

MapReduce 至今用到的类和函数

Class Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
四个参数分别为传入键的类型、传入值的类型、传出键的类型、传出值的类型

map(KEY key, VALUE value, Context context)
第一个参数是传入的键
第二个参数是传入的值
第三个参数是在Mapper中生成键值对传给Reducer用的

Class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
四个参数分别为传入键的类型、传入值的类型、传出键的类型、传出值的类型
reduce(KEYIN key, Iterable<VALUEIN> values, Context context)
第一个和第二参数同理， 第三个参数是生成键值对传出

Context<KEYIN,VALUEIN,KEYOUT,VALUEOUT> // A context object that allows input and output from the task. It is only supplied to the Mapper or Reducer.
    .write(KEYOUT key, VALUEOUT value) // 生成一组键值





IntWritable() // 一个存储Integer的可比较类 
    .(int value) // 初始化设置Inwritable的值 

NullWritable() // 无数据的Writable，充当占位符用，
    .get() // 返回一个新的Nullwritable，不能new NullWritable生成

LongWritable // // 一个存储Long的可比较类 
    .get() // 返回存储的类型为Long的值

Text() // 存储文本类 
    .set(String string) // 设置包含字符串的内容 
    .toString() // 传出其包含的字符串内容

    
```
### Spark
1. .map(->) // 使用匿名函数
### Strom
1. joinBolt: https://storm.apache.org/releases/1.2.3/Joins.html
   ```java
   // 设置join条件, 设置筛选内容, 设置时间窗口
   JoinBolt jbolt = new JoinBolt("genderSpout","id")
                .join("ageSpout","id","genderSpout")
                .select("id, gender, age")
                .withTumblingWindow(new BaseWindowedBolt.Duration(2,TimeUnit.SECONDS));
   ```
### Flink
接口和类: https://ci.apache.org/projects/flink/flink-docs-master/api/java/org/apache/flink/api/common/functions/package-summary.html
1. map
    
    ```java
    new MappFunction <TYPEIN, TYPEOUT>(){
      @Override
      public TYPEOUT map (TYPEIN parameter){}
    }
    ```

2. groupby()
3. reduce: TYPEIN和TYPEOUT保持一致;
    ```java
    new ReduceFunction<TYPEOUTr>(){
      @Override
      public TYPEOUT reduce(TYPEOUT parameter){}

    }
    ```

4. filter
    ```java
    new FilterFunction <TYPEIN>() {
      @Override
      public boolean filter(TYPEIN parameter)()
    }
    ```
    
5. join
    ```java
    .join.where("field").equalTo("field")
    ```
6. ValueStatus
  ValueState\<T\> count;
   ```java
    // access the state value 
    count.value();
    // update the state value
    count.update(newCount);
    // clear the state, after clear, it becomes `null` or the `default value` in the class `Configuration`
    count.clear();
  ```

### Giraph
1. balabala
   ```java
    aggregate(COUNT_VERTEX, new IntWritable(1));
    if(getSuperstep()==2)
    {
        IntWritable v1 = getAggregatedValue(COUNT_VERTEX);
        vertex.setValue(v1);
        vertex.voteToHalt();
    }
   ```
2. voteHalt(): 通过判断超步步数等;
3. 顶点之间通信: sendMessageToAllEdges(vertex, value);