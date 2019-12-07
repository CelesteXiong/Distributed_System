# K-means

## 待完成:

* 请在DSPPCode.flink.k_means中创建IterationStepImpl, 继承IterationStep, 实现抽象方法.
* 请在DSPPCode.flink.k_means中创建TerminationCriterionImpl, 继承TerminationCriterion, 实现抽象方法.

## 题目描述:

* 利用Flink批处理迭代机制求解K-Means
* 输入数据：数据点Point <double x, double y> 中心点Centroid <int id, Point p>
对应的数据POJO类已经在package util中给出，此外还给出了k-means一次迭代处理逻辑即迭代体所需的全部工具类。
同学们可以利用工具类来实现迭代步的处理逻辑。
* 在KMeansMain类中runJob 中已经给出flink环境下处理kmeans问题的完整DAG, 但是
    * 一次迭代的处理逻辑（IterationStepImpl -> runStep）
    * 迭代终止条件(TerminationCriterionImpl -> getTerminatedDataSet)
    
    需要同学们自己来实现，更详细的要求已在代码注释中给出。