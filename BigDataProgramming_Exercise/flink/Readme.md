# twitter

## class

1. jsonnode
2. ObjectMapper

# k_means

问题: 

1. broadcast作用:
2. 中间结果没有输出

参考资料

1. iteration: https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/batch/

   - closeWith()

     To specify the end of an iteration call the closeWith(DataSet) method on the IterativeDataSet to specify which transformation should be fed back to the next iteration. You can optionally specify a termination criterion with **closeWith(DataSet, DataSet), which evaluates the second DataSet and terminates the iteration, if this DataSet is empty.** If no termination criterion is specified, the iteration terminates after the given maximum number iterations.

2. transformation

   - join: 

     https://ci.apache.org/projects/flink/flink-docs-stable/dev/batch/dataset_transformations.html

     The Join transformation joins two DataSets into one DataSet. The elements of both DataSets are joined on one or more keys which can be specified using

     - a key expression
     - a key-selector function
     - one or more field position keys (Tuple DataSet only).
     - Case Class Fields

3. **broadcast:** 
   - https://ci.apache.org/projects/flink/flink-docs-stable/dev/batch/#broadcast-variables
   - Broadcast variables allow you to make a data set available to <u>all parallel instances of an operation</u>, in addition to the regular input of the operation. This is useful for auxiliary data sets, or data-dependent parameterization. <u>The data set will then be accessible at the operator as a Collection.</u>
     - **Broadcast**: broadcast sets are registered by name via `withBroadcastSet(DataSet, String)`, and
     - **Access**: accessible via `getRuntimeContext().getBroadcastVariable(String)` at the target operator.
   - Make sure that the names (`broadcastSetName` in the previous example) match when registering and accessing broadcast data sets. For a complete example program, have a look at [K-Means Algorithm](https://github.com/apache/flink/blob/master//flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java/clustering/KMeans.java).

## Utils

## class

1. Point: 

   - Point(double x, double y)
   - euclideanDistance(Point other)

   - clear()
   - toString(): x+" " y

2. Centroid

   - Centroid(id, x, y)
   - Centroid(id, Point)
   - toString(): x+" " y

## main

1. 迭代DataSet: 
   - 设置初始迭代: IterativeDataSet \<Centroid\> loop
   - 经过一次迭代: DataSet\<Centroid\> newCentroids =  new IterationStepImpl.runStep(points, loop)
   - 设置迭代终止条件: DataSet\<Centroid\>  finalCentroids = loop.closeWith(newCentroids, new TerminationCriterionImpl().getTerminatedDataSet(newCentroids, loop));
   - 所以至少会经过一次迭代吗

## IterationStep.java

1. runStep
   - parameters: 
     - DataSet\<Point\>  points
     - DataSet\<Centroids\>  centroids
   - return: 一次迭代后的中心点
     - DataSet\<Centroid\> centroid
   - solu:
     - 遍历所有中心点, 计算欧式距离, 寻找最小距离对应的中心点

## TerminationCriterionImpl.java

1. FilterOperator
   - parameters:
     - DataSet\<Centroid\>  newCentroids, DataSet\<Centroid\> oldCentroids
   - return:
     - Tuple2<Tuple3<Integer, Double, Double>, Tuple3<Integer, Double, Double>>>

## solution

## function:

1. new Mapfunction
2. new FilterFunction

## test

1. args:

   - 0: points: 空格分隔

     ```shell
     -14.22 -48.01
     -22.78 37.10
     56.18 -42.99
     35.04 50.29
     -9.53 -46.26
     -34.35 48.25
     ```

   - 1: centers: 空格分隔

     ```shell
     1 40.14 -47.15
     2 25.54 -9.68
     3 39.05 0.74
     4 49.96 -19.60
     ```

   - 2: output



# Watermarker

问题:

1. 水位线是由人为定义的吗? : 事件时间-最大容忍时间

参考资料:

1. watermarker https://ci.apache.org/projects/flink/flink-docs-stable/dev/event_timestamp_extractors.html
   - More specifically, one can assign timestamps and watermarks by implementing one of the `AssignerWithPeriodicWatermarks` and `AssignerWithPunctuatedWatermarks`

## solution:

https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/event_timestamps_watermarks.html

1. **With Periodic Watermarks**
   - AssignerWithPeriodicWatermarks assigns timestamps and generates watermarks periodically (possibly depending on the stream elements, or purely based on processing time).
   - The interval (every n milliseconds) in which the watermark will be generated is defined via ExecutionConfig.setAutoWatermarkInterval(...). The assigner’s getCurrentWatermark() method will be called each time, and a new watermark will be emitted if the returned watermark is non-null and larger than the previous watermark.

1. 获取当前时间, 记录当前最晚时间戳(保存为类变量);

   ```java
   // 给定的可容忍的时间跨度 t
   static long MAX_OUT_OF_ORDER;
   //记录最晚时间戳
   public long cuttentMaxTimeStamp;
   
   @Override
       public long extractTimestamp(Tuple2<Long, Integer> tuple, long unused) {
           cuttentMaxTimeStamp = Math.max(cuttentMaxTimeStamp,tuple.f0);
           return tuple.f0;
       }
   ```

2. 获取水位线: (当前最晚时间戳-最大容忍时间);

   ```java
   @Nullable
   @Override
   public Watermark getCurrentWatermark() {
           return new Watermark(cuttentMaxTimeStamp - MAX_OUT_OF_ORDER);
       }
   ```



# CapacityMonitor

参考资料:

1. https://ci.apache.org/projects/flink/flink-docs-stable/dev/stream/state/state.html

## solu:

1. ValueState\<T\> count 
   - access the state value `count.value()`
   - update the state value `count.update(newCount)`
   - clear the state `count.clear()`, after clear, it becomes `null` or the `default value` in the class `Configuration`

