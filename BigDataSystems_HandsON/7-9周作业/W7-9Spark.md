# 华东师范大学数据科学与工程学院实验报告

| **课程名称**：分布式模型与编程         | **年级**：2017       | **上机实践成绩**：                                         |
| -------------------------------------- | -------------------- | ---------------------------------------------------------- |
| **指导教师**：**徐辰**                 | **姓名**：**熊双宇** | **学号**：**10174102103**                                  |
| **上机实践名称**：**Spark部署与编程 ** |                      | **上机实践日期**：**2019.10.20-2019.11.17【第7-8、11周】** |
| **上机实践编号**：**实验二**           | **组号**：**11**     | **上机实践时间**：**18:00-19:30**                          |

## 一. 实验目的

- 学习Spark的部署，简单使用Spark-shell
- 查看Spark的运行日志，体会与MapReduce运行过程中日志的区别
- 通过系统部署理解体系架构，体会Spark与MapReduce之间的区别
- 学习常用Spark API编程：RDD、DataSet、DataFrame、SQL
- 通过基于Yarn部署Spark，深入理解Yarn的作用
- 通过学习Spark Streaming编程理解微批处理模型、体会与Hadoop Streaming的区别

## 二. 实验任务

- [Spark部署]()【第7周】：单机集中式、单机伪分布式（在个人用户下独立完成）、分布式（多位同学新建一个相同的用户，例如ecnu，协作完成）
- [Spark RDD编程]()【第7周】
- [Spark on Yarn部署]()【第8周】：单机伪分布式（在个人用户下独立完成）、分布式（多位同学新建一个相同的用户，例如ecnu，协作完成）
- [Spark Streaming编程]()【第11周】

## 三. 使用环境

1. Ubuntu18.04
2. hadoop-2.9.2
3. spark-2.4.4

## 四. 实验过程

### (一)Spark 部署

#### 1. 单机集中式部署

**1.1 运行 Spark 应用程序**

**1.1.1 通过 Spark-Shell 运行应用程序l**

- 进入 Spark-Shell ![s1.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s1.1.png)

- 在 scala> 后输入 Scala 代码, 此处执行的是统计 /home/syx/spark-2.4.4/RELEASE 文件中的单词数量

  ![s1.12](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s1.12.png)

**1.1.2 通过提交 Jar 包运行应用程序**

-  `~/spark-2.4.4/bin/spark-submit \`
    `--master local \`
      ` --class org.apache.spark.examples.SparkPi \`
    `  ~/spark-2.4.4/examples/jars/spark-examples_2.11-2.4.4.jar`

-  运行结果如下图所示:![s1.13](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s1.13.png)

  在运行过程中另起一个终端执行 jps 查看进程.
  此时只会出现 SparkSubmit 进程, 应用程序运行结束后该进程消失![sw7.1.2](/home/syx/文档/Git/sw7.1.2.png)

#### 2.单机伪分布式部署

**2.2 修改配置**

**2.2.1 修改 spark-env.sh 文件**

- 在末尾添加 

  ![sw7m2](/home/syx/文档/Git/sw7m2.png)

**2.2.2 修改 slaves 文件**

- `mv ~/spark-2.4.4/conf/slaves.template ~/spark-2.4.4/conf/slaves`

**2.2.3修改 spark-defaults.conf 文件 **

- `mv ~/spark-2.4.4/conf/spark-defaults.conf.template ~/spark-2.4.4/conf/spark-defaults.conf`

- `vi ~/spark-2.4.4/conf/spark-defaults.conf`

- 在末尾添加![s2.22](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.2.2.png)

- 并在 HDFS 中建立目录 /tmp/spark_history

  `~/hadoop-2.9.2/bin/hdfs dfs -mkdir -p /tmp/spark_history`

**2.3 启动服务**

**2.3.1 启动 Spark**

![sw7s1](/home/syx/文档/Git/sw7s1.png)

**2.3.2 启动应用日志服务器 **

![sw7s2](/home/syx/文档/Git/sw7s2.png)

**2.4 查看服务信息**

- `jps`

  ![img](file:///home/syx/%E6%96%87%E6%A1%A3/Git/se7s3.png?lastModify=1571309057)

  在单机伪分布式部署模式下, 该节点既充当 Master, 又充当 Worker, 故该节点上会有两个进程: Master 和 Worker

- 查看 Spark 服务日志![s2.23](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.2.3.png)

- 访问 Spark Web 界面, 可看到 Master 和 Worker: http://localhost:8080

  ![s2.2.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.2.4.png)

**2.5 运行 Spark 应用程序**

**2.5.1 通过 Spark-Shell 运行应用程序**

- 进入 Spark-Shell ![s2.5.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.1.png)(注: 如使用 localhost 无法正常启动, 可尝试将 localhost 改为 127.0.1.1)

- 在 scala> 后输入 Scala 代码. 此处执行的是统计 RELEASE 文件中的单词数量

  ` sc.textFile("hdfs://localhost:9000/user/syx/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect`

  执行后应打印出如下结果

  ![s2.5.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.2.png)

**2.5.2 通过提交 Jar 包运行应用程序**

- (注: 如使用 localhost 无法正常启动, 可尝试将 localhost 改为 127.0.1.1)

- Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 可以在客户端看到应用程序运行过程中的信息

  ![s2.5.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.4.png)

  运行结果如下图所示:

  ![s2.5.3](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.3.png)

  在运行过程中另起一个终端执行 jps 查看进程.此时会存在一个 CoarseGrainedExecutorBackend 进程, 负责创建及维护 Executor 对象

  ![s2.5.5](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.5.png)

- Cluster 提交模式, 此模式下 Master 会随机选取一个 Worker 节点启动 Driver, 故在客户端看不到应用程序运行过程中的信息

  运行结果如下图所示:

  ![s2.5.6](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.6.png)

  在运行过程中另起一个终端执行 jps 查看进程.在 Cluster 提交模式下, 还可以看到一个 DriverWrapper 进程

  ![s2.5.7](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.5.7.png)

**2.6 查看 Spark 程序运行信息**

**2.6.1 实时查看应用运行情况**

- 在应用运行过程中 (如进入 Spark-Shell 之后), 访问 http://localhost:4040

  ![s2.6.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.6.1.png)

**2.6.2 查看 Spark 应用程序日志**

- 在提交一个应用程序后，在 ~/spark-2.4.4/work 下会出现应用程序运行日志

  ![s2.6.3](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.6.3.png)

**2.6.4 查看应用历史记录**

- 在应用运行结束后, 访问 http://localhost:18080

  ![s2.6.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.6.4.png)

**2.7 停止服务**

- 停止命令

  ![s2.6.5](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/s2.6.5.png)

#### 3.分布式部署

**3.1 准备工作**
单机伪分布式是部署在同学们现有的用户名 you 下, 大家名字是不相同的.但是分布式部署需要每个节点都用同一个名字. 以下使用用户名ecnu
请确认是否已完成以下内容:

- 有至少两台的服务器, 每台服务器上都有用于分布式部置的用户 ecnu
- 每台机器上的 ecnu 用户已完成 Prepare.md
- 服务器之间实现免密登录
- 在其中一台机器上完成单机集中式部署
- 已完成 HDFS v2 分布式部署并启动

**3.2 修改配置**

- 修改 `spark-env.sh` 文件

  ```shell
  mv ~/spark-2.4.4/conf/spark-env.sh.template ~/spark-2.4.4/conf/spark-env.sh
  vi ~/spark-2.4.4/conf/spark-env.sh
  ```

  在末尾添加

  ```vim
  export SPARK_MASTER_IP=219.228.135.71 # 主机 IP
  export SPARK_MASTER_PORT=7078       # 端口号
  ```

- 修改 `slaves` 文件

  ```vim
  mv ~/spark-2.4.4/conf/slaves.template ~/spark-2.4.4/conf/slaves
  vi ~/spark-2.4.4/conf/slaves
  ```

  添加节点 IP

  ```
  219.228.135.71
  219.228.135.124
  ```

- 修改 `spark-defaults.conf` 文件

  ```
  mv ~/spark-2.4.4/conf/spark-defaults.conf.template ~/spark-2.4.4/conf/spark-defaults.conf
  vi ~/spark-2.4.4/conf/spark-defaults.conf
  ```

  在末尾添加

  ```vim
  spark.eventLog.enabled=true
  spark.eventLog.dir=hdfs://219.228.135.71:9000/tmp/spark_history
  spark.history.fs.logDirectory=hdfs://219.228.135.71:9000/tmp/spark_history
  ```

  并在 HDFS 中建立目录 `/tmp/spark_history`

  ```
  ~/hadoop-2.9.2/bin/hdfs dfs -mkdir -p /tmp/spark_history
  ```

  将配置好的 Spark 拷贝到其它节点

  ```shell
  scp -r ~/spark-2.4.4 ecnu@219.228.135.124:~/
  ```

**3.3 启动服务**

- 启动命令 (在主节点上执行)

  ```shell
  ~/spark-2.4.4/sbin/start-all.sh             # 启动 Spark
  ~/spark-2.4.4/sbin/start-history-server.sh  # 启动应用日志服务器
  ```

**3.4 查看服务信息**

- 查看进程, 验证是否成功启动服务

  - 主节点上执行 `jps`

    ![jps-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jps-master.png)

  - 从节点上执行 `jps`

    ![jps-slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jps-slaves.png)

    在分布式部署模式下, 主节点充当 Master 角色, 从节点充当 Worker,故在主节点上存在 Master 进程, 在从节点节点上存在 Worker 进程

- 查看 Spark 服务日志

  主节点的日志中会出现 Master 进程启动的记录信息, 从节点的日志中会出现 Worker 进程启动的记录信息.可分别在主节点和从节点上的 `~/spark-2.4.4/logs` 中查看日志

  - 主节点日志

    ![log-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/log-master.png)

  - 从节点日志

    ![log-slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/log-slaves.png)

- 访问 Spark Web 界面, 可看到 Master 和 Worker: <http://localhost:8080>

  ![webui](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/webui.png)

**3.5 运行 Spark 应用程序**

- 通过 Spark-Shell 运行应用程序

  - 准备输入文件

    将本地的 `~/spark-2.4.4/RELEASE` 上传至 HDFS 的 `/user/ecnu/spark_input` 下

  - 进入 Spark-Shell

    ![shell-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/shell-master.png)

  - 在 `scala>` 后输入 Scala 代码.此处执行的是统计 `RELEASE` 文件中的单词数量

    ```SCALA
    scala> sc.textFile("hdfs://219.228.135.71:9000/user/ecnu/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect
    ```

    执行后应打印出如下结果

    ![scala](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/scala.png)

- 通过提交 Jar 包运行应用程序

  - Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 可以在客户端看到应用程序运行过程中的信息

    ![jar-client_2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-client_2.png)

    ![jar-client](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-client.png)

    在运行过程中另起一个终端执行 `jps` 查看进程.此时各子节点上会存在一个 CoarseGrainedExecutorBackend 进程, 负责创建及维护 Executor 对象

    - 主节点上执行 `jps`

      ![jar-client-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-client-master.png)

    - 从节点上执行 `jps`

      ![jar-client_slaes](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-client_slaes.png)

  - Cluster 提交模式, 此模式下 Master 会随机选取一个 Worker 节点启动 Driver, 故在客户端看不到应用程序运行过程中的信息

    在运行过程中另起一个终端执行 `jps` 查看进程.在 Cluster 提交模式下, 还可以在主节点看到一个 DriverWrapper 进程

    - 主节点上执行 `jps`

      ![jar-cluster-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-cluster-master.png)

    - 从节点上执行 `jps`

      ![jar-cluster-slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/jar-cluster-slaves.png)

**3.6 查看 Spark 程序运行信息**

- 实时查看应用运行情况

  在应用运行过程中 (如进入 Spark-Shell 之后), 访问 <http://219.228.135.71:4040>

  ![4040](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/4040.png)

- 查看 Spark 应用程序日志, 在 `~/spark-2.4.4/work` 下

  在提交一个应用程序后，在各子节点的 `~/spark-2.4.4/work` 下会出现应用程序运行日志

  ![work-slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/work-slaves.png)

- 查看应用历史记录

  在应用运行结束后, 访问 <http://219.228.135.71:18080>

  ![history-server-18080](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/history-server-18080.png)

**3.7 停止服务**

- 停止命令 (在主节点上执行)

  ```shell
  ~/spark-2.4.4/sbin/stop-all.sh              # 停止 Spark
  ~/spark-2.4.4/sbin/stop-history-server.sh   # 停止日志服务器
  ```

### (二)Spark on Yarn 模式部署

#### 1.预备知识: Yarn Client 和 Yarn Cluster 区别

- 在 Yarn Client 模式中, Driver 运行在 Client 上, 通过 ApplicationMaster 向 ResourceManager 获取资源, 并负责与所有的 Executor Container 进行交互, 将最后的结果汇总. 结束掉终端, 相当于停止这个 Spark 应用;
- 在 Yarn Cluster 模式中, 当用户向 Yarn 提交一个应用程序后, Spark 的 Driver 作为一个 ApplicationMaster 首先被启动. 然后由 ApplicationMaster 创建应用程序, 向 ResourceManager 申请资源, 并启动 Executor 来执行程序. 同时 ApplicationMaster 监控整个运行过程, 直到运行完成

#### 2.单机伪分布式部署

**2.1  准备工作**

**2.1.1 完成 Spark 单机伪分布式部署**

- `~/spark-2.4.4/bin/spark-shell --master spark://localhost:7077`或者`~/spark-2.4.4/bin/spark-shell --master spark://127.0.1.1:7077`

  ![syarn2.1.1(addition)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.1.1(addition).png)

  ![syarn2.1.2(a)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.1.2(a).png)

**2.2.2完成 MapReduce v2 单机伪分布式部署**

**2.2 修改配置**

- 修改 spark-env.sh 文件, 使 Spark 能够读取 Yarn 配置
  `vi ~/spark-2.4.4/conf/spark-env.sh`

- 在末尾添加

  `export HADOOP_CONF_DIR=/home/you/hadoop-2.9.2/etc/hadoop    # Hadoop 配置目录`

  ![syarn2.2.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.2.2.png)

**2.3 启动服务**

- 启动命令

  ```shell
  ~/hadoop-2.9.2/sbin/start-yarn.sh                               # 启动 Yarn
  ~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh start historyserver # 启动 Yarn 历史服务器
  ~/hadoop-2.9.2/sbin/start-dfs.sh                                # 启动 HDFS
  ~/spark-2.4.4/sbin/start-history-server.sh                      # 启动 Spark 应用日志服务器

  ```

**2.4 查看服务信息**

- jps查看进程, 验证是否成功启动服务

  ![syarn2.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.4.png)

**2.5 运行 Spark 应用程序**

**2.5.1通过 Spark-Shell 运行应用程序 **

- 准备输入文件: 将本地的 ~/spark-2.4.4/RELEASE 上传至 HDFS 的 /user/you/spark_input 下

  ![syarn2.5.1(a)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.1(a).png)

- 进入 Spark-Shell

  ` ~/spark-2.4.4/bin/spark-shell --master yarn`

  ![syarn2.5.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.1.png)

  若出现 Your application is being killed for virtual memory usage 报错, 请在 Hadoop 配置文件 `yarn-site.xml` 中添加

  ```vim
  <property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
  </property>
  ```

- 在 `scala>` 后输入 Scala 代码. 此处执行的是统计 `RELEASE` 文件中的单词数量

  `sc.textFile("hdfs://localhost:9000/user/ecnu/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect`

  执行后打印出如下结果: 

  ![syarn2.5.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.2.png)

- 通过提交 Jar 包运行应用程序

  - Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 

    ![syarn2.5.3](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.3.png)

    可以在客户端看到应用程序运行过程中的信息

    ![syarn2.5.2(a)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.2(a).png)

    在运行过程中另起一个终端执行 jps 查看进程.此时会存在一个ApplicationMaster 进程, 以及若干个 CoarseGrainedExecutorBackend 进程

    ![syarn2.5.7.2(client)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.7.2(client).png)

  - Cluster 提交模式, 此模式下 ResourceManager 会随机选取一个NodeManager 所在节点在其上启动 ApplicationMaster,
    Driver 运行在 ApplicationMaster 中, 故在客户端看不到应用程序运行过程中的信息

    ![syarn2.5.3(a)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.3(a).png)

    在运行过程中另起一个终端执行 `jps` 查看进程.此时会存在一个 ApplicationMaster 进程, 以及若干个 CoarseGrainedExecutorBackend 进程![syarn2.5.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.5.4.png)

**2.6 查看 Spark 程序运行信息**

**2.6.1 查看 Spark 程序运行信息**

- 实时查看应用运行情况, 在应用运行过程中 (如进入 Spark-Shell 之后), 访问 <http://localhost:4040>

  ![syarn2.6.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.6.1.png)

- 查看 Spark 应用程序日志
  在提交一个应用程序后，在 ~/hadoop-2.9.2/logs/userlogs 下会出现应用程序运行日志

  ![syarn2.6.1(a)](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.6.1(a).png)

- 查看应用历史记录
  在应用运行结束后, 访问 http://localhost:18080

  ![syarn2.6.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.6.2.png)

**2.7 停止服务**

**2.7.1 停止命令**

```shell
~/spark-2.4.4/sbin/stop-history-server.sh

~/hadoop-2.9.2/sbin/stop-yarn.sh

~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh stop historyserver

~/hadoop-2.9.2/sbin/stop-dfs.sh
```

![syarn2.7.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.7.1.png)

![syarn2.7.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/syarn2.7.2.png)

#### 3. 分布式部署

**3.1 准备工作**

单机伪分布式是部署在同学们现有的用户名 `you` 下, 大家名字是不相同的.但是分布式部署需要每个节点都用同一个名字. 以下使用用户名 `ecnu`

请确认是否已完成以下内容:

- 有至少两台的服务器, 每台服务器上都有用于分布式部置的用户 `ecnu`
- 在所有机器上完成[单机伪分布式部署](#2-%E5%8D%95%E6%9C%BA%E4%BC%AA%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2)
- 已完成 [MapReduce v2 分布式部署]()

**3.2 启动服务**

- 启动命令 (在 Yarn 主节点上执行)

  ```shell
  ~/hadoop-2.9.2/sbin/start-yarn.sh                               # 启动 Yarn
  ~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh start historyserver # 启动 Yarn 历史服务器
  ~/hadoop-2.9.2/sbin/start-dfs.sh                                # 启动 HDFS
  ~/spark-2.4.4/sbin/start-history-server.sh                      # 启动 Spark 应用日志服务器
  ```

**3.3 查看服务信息**

查看进程, 验证是否成功启动服务

- 主节点上执行 `jps`

  ![jps-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/jps-master.png)

- 从节点上执行 `jps`

  ![jps-slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/jps-slaves.png)

**3.4 运行 Spark 应用程序**

- 通过 Spark-Shell 运行应用程序

  - 准备输入文件

    将本地的 `~/spark-2.4.4/RELEASE` 上传至 HDFS 的 `/user/ecnu/spark_input` 下

  - 进入 Spark-Shell

    ```shell
    ~/spark-2.4.4/bin/spark-shell --master yarn
    ```

    ![shell](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/shell.png)

  - 在 `scala>` 后输入 Scala 代码, 此处执行的是统计 `RELEASE` 文件中的单词数量

    ```scala
    sc.textFile("hdfs://219.228.135.71:9000/user/ecnu/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect
    ```

    执行后应打印出如下结果

    ![scala](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/scala.png)

  - 通过提交 Jar 包运行应用程序

    - Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 可以在客户端看到应用程序运行过程中的信息

      ![jar-client-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/jar-client-master.png)

      在运行过程中另起一个终端执行 `jps` 查看进程.
      此时会存在一个 ExecutorLauncher 进程, 以及若干个 CoarseGrainedExecutorBackend 进程

      主节点上执行 `jps`

      ![jar-client-jps-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/jar-client-jps-master.png)

    - Cluster 提交模式, 此模式下 ResourceManager 会随机选取一个 NodeManager 所在节点在其上启动 ApplicationMaster,
      Driver 运行在 ApplicationMaster 中, 故在客户端看不到应用程序运行过程中的信息

      ​

      在运行过程中另起一个终端执行 `jps` 查看进程.
      此时会存在一个 ApplicationMaster 进程, 以及若干个 CoarseGrainedExecutorBackend 进程

      主节点上执行 `jps`

      ![jar-cluster-jps-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/jar-cluster-jps-master.png)

**3.5 查看 Spark 程序运行信息**

- 实时查看应用运行情况

  在应用运行过程中 (如进入 Spark-Shell 之后), 访问 <http://219.228.135.71:4040>

  ![4040](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/4040.png)

- 查看应用历史记录

  在应用运行结束后, 访问 <http://192.168.1.11:18080>

  ![18080](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/yarn/18080.png)

**3.6 停止服务**

停止命令 (在 Yarn 主节点上执行)

```shell
~/spark-2.4.4/sbin/stop-history-server.sh
~/hadoop-2.9.2/sbin/stop-yarn.sh
~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh stop historyserver
~/hadoop-2.9.2/sbin/stop-dfs.sh
```

###(三)Spark RDD编程

#### 1. 编写, 调试Spark程序

**1.1.1 Edit Configuration**

- java

  ![srddconfig.java](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srddconfig.java.png)

  ![srdd2.java](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd2.2.png)

- scala

  ![srdd3config.scala](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd3config.scala.png)

  ![srdd.scala](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd.scala.png)

- check output:

  ![srdd2.java](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd2.java.png)



####  2. 运行Spark程序

**2.1利用IDE打包jar文件 **

**2.1.1 ** RDDWordCount

![srdd.artifacts](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd.artifacts.png)

**2.2 伪分布模式下提交Spark程序**

- 在Spark的Master节点输入命令：spark-submit --master 指定伪分布式master --class 主类名 jar包路径 输入路径 输出路径

  如下所示(其中20s211为主机名，也可用ip地址)：

  ![srdd2.jar.3](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd2.jar.3.png)

  ![srdd2.jar.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd2.jar.2.png)

  ![srdd2.jar](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/srdd2.jar.png)

**2.3分布式模式下提交程序**

- ```shell
  ~/spark-2.4.4/bin/spark-submit   --master spark://219.228.135.71:7078   --class RDDWordCount RDDWordCount.jar /home/ecnu/spark_input /home/ecnu/spark_output
  ```

- ![rdd_submit](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/rdd/rdd_submit.png)

- ![hdfs_output_path](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/rdd/hdfs_output_path.png)

- check output:

  ![hdfs_output](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/rdd/hdfs_output.png)

### (四)Spark Streaming 编程

#### 1. 编写程序

**1.1 新建 Maven 项目并添加依赖**

**1.2 编写代码**

- Java 版: 新建 Java 类 StreamingWordCount

  ![streaming.java.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/streaming.java.2.png)

  ![streaming.java](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/streaming.java.png)

- Scala 版: [新建 Scala 类]() StreamingWordCountScala (**注意 Scala 版本为 2.11.12**)

  ![streaming.scala2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/streaming.scala2.png)

#### 2. 提交 Spark 程序

**2.1将程序打成 jar 包** 

**2.2伪分布模式下提交程序**

```shell
~/spark-2.4.4/bin/spark-submit   
--master spark://localhost:7077   
--class StreamingWordCount /home/syx/StreamingWordCount/out/artifacts/StormWordCount/StormWordCount.jar localhost 8888 /home/syx/spark_tmp
```

![streaming.jar.weifenbushi](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/streaming.jar.weifenbushi.png)

**2.3分布式模式下提交程序**

- MasterIP: 219.228.135.71
- NetcatIP: 8887
- HdfsIP: 219.228.135.71

```shell
nc -lk 8887
```

```shell
~/spark-2.4.4/bin/spark-submit\
--master spark://219.228.135.71:7078\
--class   StreamingWordCount \
SparkStreamingJava.jar 219.228.135.71 8887 hdfs://219.228.135.71:9000/user/ecnu/spark_output
```

![submit_master](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/streaming/submit_master.png)

- Input:

![input_nc](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/streaming/input_nc.png)

- Output:

![output_nc_1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/streaming/output_nc_1.png)

![output_nc_2](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/streaming/output_nc_2.png)

## 五. 总结

1. 启动Spark-Shell，出现 Your application is being killed for virtual memory usage 报错, 请在 Hadoop 配置文件 yarn-site.xml 中添加

   ![myarn2.5.3](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/myarn2.5.3.png)

2. `java_home` is not set

   ![SW7M](/home/syx/文档/Git/SW7M.png)

   solution: 

   change `~/spark-2.4.4/conf/spark-env.sh`, add one line:

   `export JAVA_HOME=/usr/local/jdk1.8`

   ![sw7m2](/home/syx/文档/Git/sw7m2.png)

3. 单机集中式部署: 

   ` ~/spark-2.4.4/bin/spark-shell --master local`

   **close the vpn** to start the spark-shell

4. 运行sparkRDD时，忘记添加Maven镜像源

	. 运行sparkRDD前，新建了输出路径spark_output,报错Error:	output dirctory exists

  ![mrdd.output](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/mrdd.output.png)删除output文件夹后可以运行

5. 在Spark-Shell中运行scala代码时，忘记修改文件路径，导致报错

   ![myarn2.5.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/pic/myarn2.5.1.png)

6. 分布式部署：

7. - 问题一:	主节点能够成功启动spark，启动后通过jps查看主从节点的进程都是正常的，主节点只能实现伪分布式

8. 1. -    解决: 修改主从节点的hdfs配置文件core-site.xml,	将其中的localhost修改为主节点的IP：219.228.135.71, 如图

           ![hdfs_core_site](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/hdfs_core_site.png)

9. -	问题二:	但是从webUI上看，只能现实主节点的worker

10. 1. - 解决: 修改主从节点的spark配置文件spark-envs.sh为如图s

        ![spark-envs](/home/syx/文档/Dase/DistributedSytem/Hands_on/7-9周作业/dis/spark/spark-envs.png)