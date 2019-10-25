# W7-9

## 四. 实验过程

### Spark 部署

#### 1. 单机集中式部署

**1.1 运行 Spark 应用程序**

**1.1.1 通过 Spark-Shell 运行应用程序l**

- 进入 Spark-Shell ![s1.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s1.1.png)

- 在 scala> 后输入 Scala 代码, 此处执行的是统计 /home/syx/spark-2.4.4/RELEASE 文件中的单词数量

  ![s1.12](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s1.12.png)

**1.1.2 通过提交 Jar 包运行应用程序**

-  `~/spark-2.4.4/bin/spark-submit \`
    `--master local \`
      ` --class org.apache.spark.examples.SparkPi \`
    `  ~/spark-2.4.4/examples/jars/spark-examples_2.11-2.4.4.jar`

-  运行结果如下图所示:![s1.13](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s1.13.png)

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

- 在末尾添加![s2.22](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.2.2.png)

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

- 查看 Spark 服务日志![s2.23](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.2.3.png)

- 访问 Spark Web 界面, 可看到 Master 和 Worker: http://localhost:8080

  ![s2.2.4](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.2.4.png)

**2.5 运行 Spark 应用程序**

**2.5.1 通过 Spark-Shell 运行应用程序**

- 进入 Spark-Shell ![s2.5.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.1.png)(注: 如使用 localhost 无法正常启动, 可尝试将 localhost 改为 127.0.1.1)

- 在 scala> 后输入 Scala 代码. 此处执行的是统计 RELEASE 文件中的单词数量

  ` sc.textFile("hdfs://localhost:9000/user/syx/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect`

  执行后应打印出如下结果

  ![s2.5.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.2.png)

**2.5.2 通过提交 Jar 包运行应用程序**

- (注: 如使用 localhost 无法正常启动, 可尝试将 localhost 改为 127.0.1.1)

- Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 可以在客户端看到应用程序运行过程中的信息

  ![s2.5.4](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.4.png)

  运行结果如下图所示:

  ![s2.5.3](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.3.png)

  在运行过程中另起一个终端执行 jps 查看进程.此时会存在一个 CoarseGrainedExecutorBackend 进程, 负责创建及维护 Executor 对象

  ![s2.5.5](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.5.png)

- Cluster 提交模式, 此模式下 Master 会随机选取一个 Worker 节点启动 Driver, 故在客户端看不到应用程序运行过程中的信息

  运行结果如下图所示:

  ![s2.5.6](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.6.png)

  在运行过程中另起一个终端执行 jps 查看进程.在 Cluster 提交模式下, 还可以看到一个 DriverWrapper 进程

  ![s2.5.7](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.5.7.png)

**2.6 查看 Spark 程序运行信息**

**2.6.1 实时查看应用运行情况**

- 在应用运行过程中 (如进入 Spark-Shell 之后), 访问 http://localhost:4040

  ![s2.6.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.6.1.png)

**2.6.2 查看 Spark 应用程序日志**

- 在提交一个应用程序后，在 ~/spark-2.4.4/work 下会出现应用程序运行日志

  ![s2.6.3](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.6.3.png)

**2.6.4 查看应用历史记录**

- 在应用运行结束后, 访问 http://localhost:18080

  ![s2.6.4](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.6.4.png)

**2.7 停止服务**

- 停止命令

  ![s2.6.5](/home/syx/文档/Dase/DistributedSytem/lab2/pic/s2.6.5.png)

### Spark on Yarn 模式部署

#### 1.预备知识: Yarn Client 和 Yarn Cluster 区别

- 在 Yarn Client 模式中, Driver 运行在 Client 上, 通过 ApplicationMaster 向 ResourceManager 获取资源, 并负责与所有的 Executor Container 进行交互, 将最后的结果汇总. 结束掉终端, 相当于停止这个 Spark 应用;
- 在 Yarn Cluster 模式中, 当用户向 Yarn 提交一个应用程序后, Spark 的 Driver 作为一个 ApplicationMaster 首先被启动. 然后由 ApplicationMaster 创建应用程序, 向 ResourceManager 申请资源, 并启动 Executor 来执行程序. 同时 ApplicationMaster 监控整个运行过程, 直到运行完成

#### 2.单机伪分布式部署

**2.1  准备工作**

**2.1.1 完成 Spark 单机伪分布式部署**

- `~/spark-2.4.4/bin/spark-shell --master spark://localhost:7077`或者`~/spark-2.4.4/bin/spark-shell --master spark://127.0.1.1:7077`

  ![syarn2.1.1(addition)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.1.1(addition).png)

  ![syarn2.1.2(a)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.1.2(a).png)

**2.2.2完成 MapReduce v2 单机伪分布式部署**

**2.2 修改配置**

- 修改 spark-env.sh 文件, 使 Spark 能够读取 Yarn 配置
  `vi ~/spark-2.4.4/conf/spark-env.sh`

- 在末尾添加

  `export HADOOP_CONF_DIR=/home/you/hadoop-2.9.2/etc/hadoop    # Hadoop 配置目录`

  ![syarn2.2.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.2.2.png)

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

  ![syarn2.4](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.4.png)

**2.5 运行 Spark 应用程序**

**2.5.1通过 Spark-Shell 运行应用程序 **

- 准备输入文件: 将本地的 ~/spark-2.4.4/RELEASE 上传至 HDFS 的 /user/you/spark_input 下

  ![syarn2.5.1(a)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.1(a).png)

- 进入 Spark-Shell

  ` ~/spark-2.4.4/bin/spark-shell --master yarn`

  ![syarn2.5.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.1.png)

  若出现 Your application is being killed for virtual memory usage 报错, 请在 Hadoop 配置文件 `yarn-site.xml` 中添加

  ```shell
  <property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
  </property>
  ```

- 在 `scala>` 后输入 Scala 代码. 此处执行的是统计 `RELEASE` 文件中的单词数量

  `sc.textFile("hdfs://localhost:9000/user/ecnu/spark_input/RELEASE").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).collect`

  执行后应打印出如下结果: ``res2: Array[(String, Int)] = Array((-Psparkr,1), (-B,1), (Spark,1), (-Pkubernetes,1), (-Pyarn,1), (2.4.4,1), (Build,1), (built,1), (-Pflume,1), (-DzincPort=3036,1), (flags:,1), (-Phive-thriftserver,1), (-Pmesos,1), (for,1), (-Phive,1), (-Pkafka-0-8,1), (2.7.3,1), (-Phadoop-2.7,1), (Hadoop,1))`

  ![syarn2.5.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.2.png)

- 通过提交 Jar 包运行应用程序

  - Client 提交模式 (默认), 此模式下 Driver 运行在客户端, 

    ![syarn2.5.3](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.3.png)

    可以在客户端看到应用程序运行过程中的信息

    ![syarn2.5.2(a)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.2(a).png)

    在运行过程中另起一个终端执行 jps 查看进程.此时会存在一个ApplicationMaster 进程, 以及若干个 CoarseGrainedExecutorBackend 进程

    ![syarn2.5.7.2(client)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.7.2(client).png)

  - Cluster 提交模式, 此模式下 ResourceManager 会随机选取一个NodeManager 所在节点在其上启动 ApplicationMaster,
    Driver 运行在 ApplicationMaster 中, 故在客户端看不到应用程序运行过程中的信息

    ![syarn2.5.3(a)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.3(a).png)

    在运行过程中另起一个终端执行 `jps` 查看进程.此时会存在一个 ApplicationMaster 进程, 以及若干个 CoarseGrainedExecutorBackend 进程![syarn2.5.4](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.5.4.png)

**2.6 查看 Spark 程序运行信息**

**2.6.1 查看 Spark 程序运行信息**

- 实时查看应用运行情况, 在应用运行过程中 (如进入 Spark-Shell 之后), 访问 <http://localhost:4040>

  ![syarn2.6.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.6.1.png)

- 查看 Spark 应用程序日志
  在提交一个应用程序后，在 ~/hadoop-2.9.2/logs/userlogs 下会出现应用程序运行日志

  ![syarn2.6.1(a)](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.6.1(a).png)

- 查看应用历史记录
  在应用运行结束后, 访问 http://localhost:18080

  ![syarn2.6.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.6.2.png)

**2.7 停止服务**

**2.7.1 停止命令**

```shell
~/spark-2.4.4/sbin/stop-history-server.sh

~/hadoop-2.9.2/sbin/stop-yarn.sh

~/hadoop-2.9.2/sbin/mr-jobhistory-daemon.sh stop historyserver

~/hadoop-2.9.2/sbin/stop-dfs.sh
```

![syarn2.7.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.7.1.png)

![syarn2.7.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/syarn2.7.2.png)

###Spark RDD编程

#### 1. 编写, 调试Spark程序

**1.1.1 Edit Configuration**

- java

  ![srddconfig.java](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srddconfig.java.png)

  ![srdd2.java](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd2.2.png)

- scala

  ![srdd3config.scala](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd3config.scala.png)

  ![srdd.scala](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd.scala.png)

- check output:

  ![srdd2.java](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd2.java.png)

####  2. 运行Spark程序

**2.1利用IDE打包jar文件 **

**2.1.1 ** RDDWordCount

![srdd.artifacts](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd.artifacts.png)

**2.2 伪分布模式下提交Spark程序**

- 在Spark的Master节点输入命令：spark-submit --master 指定伪分布式master --class 主类名 jar包路径 输入路径 输出路径

  如下所示(其中20s211为主机名，也可用ip地址)：

  ![srdd2.jar.3](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd2.jar.3.png)

  ![srdd2.jar.2](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd2.jar.2.png)

  ![srdd2.jar](/home/syx/文档/Dase/DistributedSytem/lab2/pic/srdd2.jar.png)

## mistake

1. 启动Spark-Shell，出现 Your application is being killed for virtual memory usage 报错, 请在 Hadoop 配置文件 yarn-site.xml 中添加

   ![myarn2.5.3](/home/syx/文档/Dase/DistributedSytem/lab2/pic/myarn2.5.3.png)

2. `java_home` is not set

   ![SW7M](/home/syx/文档/Git/SW7M.png)

   change `~/spark-2.4.4/conf/spark-env.sh`, add one line:

   `export JAVA_HOME=/usr/local/jdk1.8`

   ![sw7m2](/home/syx/文档/Git/sw7m2.png)

   ​

3. 单机集中式部署: 

   ` ~/spark-2.4.4/bin/spark-shell --master local`

   **close the vpn** to start the spark-shell

4. 运行sparkRDD时，忘记添加Maven镜像源

5. 运行sparkRDD前，新建了输出路径spark_output,报错Error:	output dirctory exists

   ![mrdd.output](/home/syx/文档/Dase/DistributedSytem/lab2/pic/mrdd.output.png)删除output文件夹后可以运行

6. 在Spark-Shell中运行scala代码时，忘记修改文件路径，导致报错

   ![myarn2.5.1](/home/syx/文档/Dase/DistributedSytem/lab2/pic/myarn2.5.1.png)