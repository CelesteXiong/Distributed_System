# 按照题型

## WordCount

1. 演化:
   - wordcount [all warmup]
   - topN [Storm]
   - TwitterJsonFilter [Flink]
   - slidecountWindow [storm]
2. 思路: 
   - split
   - hashmap计数: 从1开始
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
     - 详见Hadoop的README.md
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
1. hadoop: context.write()
2. spark: rdd
3. storm: collector.emit()
4. flink: collector.collect()
5. giraph: vertex, aggregator, sendmessage etc