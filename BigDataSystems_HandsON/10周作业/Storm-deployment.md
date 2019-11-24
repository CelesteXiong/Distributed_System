# 华东师范大学数据科学与工程学院实验报告

| **课程名称：分布式模型与编程**         | **年级：2017**       | **上机实践成绩**：                                    |
| -------------------------------------- | -------------------- | ----------------------------------------------------- |
| **指导教师**：**徐辰**                 | **姓名**：**熊双宇** | **学号**：**10174102103**                             |
| **上机实践名称**：**Spark部署与编程 ** |                      | **上机实践日期**：**2019.11.17-2019.11.24【第10周】** |
| **上机实践编号：实验二**               | **组号**：**11**     | **上机实践时间**：**18:00-19:30**                     |

## 一. 实验目的

- 学习Storm的部署，体会ZooKeeper在其中的作用
- 练习Storm的简单编程
- 了解系统日志的查看
- 体会流计算系统与批处理系统之间输入方式的异同

## 二. 实验任务

- [Storm部署]()【第10周】：单机集中式、单机伪分布式（在个人用户下独立完成）、分布式（多位同学新建一个相同的用户，例如ecnu，协作完成）
- [Storm编程]()【第10周】

## 三. 使用环境

1. Ubuntu18.04
2. zookeeper-3.4.13
3. apache-storm-1.2.3

## 四. 实验过程

### Storm-deployment

#### 1. 单机集中式

* #### 准备工作

  + 安装配置JDK

    下载软件包：访问[Oracle官网](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) ，下载jdk-8u202-linux-x64.tar.gz 

    解压：` tar -zxvf jdk-8u192-linux-x64.tar.gz` ，保存在/home/dase/jdk1.8.0_192目录下，其中假设本机用户名为dase

    配置环境变量:

    ```shell
    vi ~/.bashrc
    ```

    在该配置文件中添加以下内容
    ​

    ```shell
    export JAVA_HOME=/home/dase/jdk1.8.0_192

    export CLASSPATH=.:JAVA_HOME/lib/tools.jar:JAVA_HOME/lib/dt.jar

    export PATH=JAVA_HOME/bin:PATH

    ```

    ```shell
    source ~/.bashrc
    ```

  ​       验证是否安装配置成功: `java -version` ，终端出现版本信息则安装成功

  + 安装Storm

      下载软件包：访问[Storm官网](http://storm.apache.org/downloads.html) ，下载storm-1.2.3.tar.gz

      解压: `tar -zxvf apache-storm-1.2.3.tar.gz` ，保存在/home/dase/storm-1.2.3
      下载示例代码jar包：[wordcount.jar](./storm/code/wordcount.jar)，保存在/home/dase/storm-1.2.3

* #### 运行Storm应用程序

  + 运行应用程序命令

    `cd /home/dase/apache-storm-1.2.3`

    ```shell
    bin/storm jar wordcount.jar StormWordCount local
    ```

    示例代码的输出结果如图所示

    ![s1.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s1.1.png)

  + 查看运行过程中的进程

    另启一个终端，jps查看进程

    ![s1.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s1.2.png)		

    由图可知，只有单一进程StormWordCount，此时的linux系统中并没有Storm的nimbus、supervisor等进程

* #### 结束应用程序: 

  由于流式应用程序是长期运行的，需要人工终止。当需要关闭应用程序时，在应用程序提交终端(输出终端)按下ctrl+c即可。也可以在代码中指定程序运行的终止条件，比如运行10s后终止，详细内容将在Storm编程模块介绍

#### 2. 单机伪分布式

由于Storm系统利用zookeeper做任务调度和元数据存储，故欲部署Storm须先部署Zookeeper，JDK默认已配好不再赘述

##### 2.1 部署zookeeper

* #### 准备工作

    + 下载软件包：[zookeeper官网](http://zookeeper.apache.org/releases.html#download) ,下载zookeeper-3.4.13.tar.gz
	
    + 解压：`tar -zxvf zookeeper-3.4.13.tar.gz` ，保存在/home/dase/zookeeper-3.4.13
	
* #### 修改配置文件

  + 进入目录：`cd home/dase/zookeeper-3.4.13/`

    + 复制模板：`cp zoo_sample.cfg zoo.cfg`
    
  + 配置修改：`vi zoo.cfg`

    将内容修改为如下所示：

    ```yaml
    tickTime=2000   #心跳间隔
    initLimit=10   #初始容忍的心跳数
    syncLimit=5   #等待最大容忍的心跳数
    dataDir=/home/syx/zookeeper-3.4.13/data  #存放数据的目录
    clientPort=2181   #客户端默认的端口号
    dataLogDir=/home/syx/zookeeper-3.4.13/data/log  #存放log的目录
    autopurge.snapRetainCount=20  #保留的快照数目
    autopurge.purgeInterval=48   #定期清理快照，时间单位：时
    server.1=127.0.0.1:2888:3888  #主机名, 心跳端口，数据端口
    ```
    ![zoo.cfg](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/zoo.cfg.png)

  + 创建data目录，然后再在data目录下面创建一个myid，写入机器对应配置里的数字

    ```shell
    mkdir data
    cd data
    mkdir log
    echo 1 > myid
    ```

* #### 启动zookeeper服务，注意关闭防火墙

  + 启动命令: `zkServer.sh start`

  + jps查看进程
    
  	![s2.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.1.png)

  	QuorumPeerMain即为zookeeper的服务进程
  	
  + 查看zookeeper启动日志：输入命令 `cat ./zookeeper-3.4.13/zookeeper.out`

    ```shell
    2019-11-06 20:38:10,272 [myid:] - INFO  [main:QuorumPeerConfig@136] - Reading configuration from: /home/syx/zookeeper-3.4.13/bin/../conf/zoo.cfg
    2019-11-06 20:38:10,279 [myid:] - INFO  [main:QuorumPeer$QuorumServer@184] - Resolved hostname: 127.0.0.1 to address: /127.0.0.1
    2019-11-06 20:38:10,279 [myid:] - ERROR [main:QuorumPeerConfig@347] - Invalid configuration, only one server specified (ignoring)
    2019-11-06 20:38:10,280 [myid:] - INFO  [main:DatadirCleanupManager@78] - autopurge.snapRetainCount set to 20
    2019-11-06 20:38:10,280 [myid:] - INFO  [main:DatadirCleanupManager@79] - autopurge.purgeInterval set to 48
    2019-11-06 20:38:10,280 [myid:] - WARN  [main:QuorumPeerMain@116] - Either no config or no quorum defined in config, running  in standalone mode
    2019-11-06 20:38:10,280 [myid:] - INFO  [PurgeTask:DatadirCleanupManager$PurgeTask@138] - Purge task started.
    2019-11-06 20:38:10,286 [myid:] - INFO  [PurgeTask:DatadirCleanupManager$PurgeTask@144] - Purge task completed.
    2019-11-06 20:38:10,287 [myid:] - INFO  [main:QuorumPeerConfig@136] - Reading configuration from: /home/syx/zookeeper-3.4.13/bin/../conf/zoo.cfg
    2019-11-06 20:38:10,287 [myid:] - INFO  [main:QuorumPeer$QuorumServer@184] - Resolved hostname: 127.0.0.1 to address: /127.0.0.1
    2019-11-06 20:38:10,288 [myid:] - ERROR [main:QuorumPeerConfig@347] - Invalid configuration, only one server specified (ignoring)
    2019-11-06 20:38:10,288 [myid:] - INFO  [main:ZooKeeperServerMain@98] - Starting server
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:zookeeper.version=3.4.13-2d71af4dbe22557fda74f9a9b4309b15a7487f03, built on 06/29/2018 04:05 GMT
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:host.name=syx-OptiPlex-7050
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.version=1.8.0_221
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.vendor=Oracle Corporation
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.home=/usr/local/jdk1.8/jre
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.class.path=/home/syx/zookeeper-3.4.13/bin/../build/classes:/home/syx/zookeeper-3.4.13/bin/../build/lib/*.jar:/home/syx/zookeeper-3.4.13/bin/../lib/slf4j-log4j12-1.7.25.jar:/home/syx/zookeeper-3.4.13/bin/../lib/slf4j-api-1.7.25.jar:/home/syx/zookeeper-3.4.13/bin/../lib/netty-3.10.6.Final.jar:/home/syx/zookeeper-3.4.13/bin/../lib/log4j-1.2.17.jar:/home/syx/zookeeper-3.4.13/bin/../lib/jline-0.9.94.jar:/home/syx/zookeeper-3.4.13/bin/../lib/audience-annotations-0.5.0.jar:/home/syx/zookeeper-3.4.13/bin/../zookeeper-3.4.13.jar:/home/syx/zookeeper-3.4.13/bin/../src/java/lib/*.jar:/home/syx/zookeeper-3.4.13/bin/../conf:.:/usr/local/jdk1.8/lib:/usr/local/jdk1.8/jre/lib
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
    2019-11-06 20:38:10,290 [myid:] - INFO  [main:Environment@100] - Server environment:java.io.tmpdir=/tmp
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:java.compiler=<NA>
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:os.name=Linux
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:os.arch=amd64
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:os.version=5.0.0-32-generic
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:user.name=syx
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:user.home=/home/syx
    2019-11-06 20:38:10,291 [myid:] - INFO  [main:Environment@100] - Server environment:user.dir=/home/syx/zookeeper-3.4.13/bin
    2019-11-06 20:38:10,292 [myid:] - INFO  [main:ZooKeeperServer@836] - tickTime set to 2000
    2019-11-06 20:38:10,292 [myid:] - INFO  [main:ZooKeeperServer@845] - minSessionTimeout set to -1
    2019-11-06 20:38:10,292 [myid:] - INFO  [main:ZooKeeperServer@854] - maxSessionTimeout set to -1
    2019-11-06 20:38:10,295 [myid:] - INFO  [main:ServerCnxnFactory@117] - Using org.apache.zookeeper.server.NIOServerCnxnFactory as server connection factory
    2019-11-06 20:38:10,297 [myid:] - INFO  [main:NIOServerCnxnFactory@89] - binding to port 0.0.0.0/0.0.0.0:2181
    ```

    ​

##### 2.2 Storm安装配置

* #### 准备工作
  
	下载软件包：访问[Storm官网](http://storm.apache.org/downloads.html) ，下载storm-1.2.3.tar.gz
	
	解压: `tar -zxvf apache-storm-1.2.3.tar.gz` ，保存在/home/dase/storm-1.2.3
  
	下载示例代码jar包：[wordcount.jar](./storm/code/wordcount.jar)，保存在/home/dase/storm-1.2.3
  
* #### 修改配置文件

  Storm配置文件为storm.yaml，修改配置文件命令: `vi  apache-storm-1.2.3/conf/storm.yaml`
  添加如下设置，注意空格

  ```yaml
  # right
  storm.zookeeper.servers: #zookeeper节点 
   - "127.0.0.1" 
  nimbus.seeds: ["127.0.0.1"] #nimbus进程服务器ip
  storm.local.dir: "/home/xxx/apache-storm-1.2.2/data"  #需要自己创建
  ui.port: 18081 #ui端口
  supervisor.slots.ports: #给supervisor四个端口，即supervisor可以创建四个worker进程
   - 6700
   - 6701
   - 6702
   - 6703
  ```

  ![storm.yaml](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/storm.yaml.png)

  保存并退出

* #### 启动Storm服务

    + 启动命令

      ```shell
      bin/storm nimbus >/dev/null 2>&1 &
      bin/storm ui >/dev/null 2>&1 &
      bin/storm logviewer > /dev/null 2>&1 &
      bin/storm supervisor >/dev/null 2>&1 &
      ```
      jps查看进程

      ![s2.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.png)

      由图可知，同一台机器上运行不同进程，此时的Storm中的nimbus，supervisor，core和logviewer进程在同一台机器上均已启动，其中core为ui进程，logviewer为日志进程

    + 参数说明

      ```shell
      >/dev/null ：代表空设备文件`
      >  ：代表重定向到哪里，例如：echo "123" > /home/123.txt
      1  ：表示stdout标准输出，系统默认值是1，所以">/dev/null"等同于"1>/dev/null"
      2  ：表示stderr标准错误
      &  ：表示等同于的意思，2>&1，表示2的输出重定向等同于1
      1 > /dev/null 2>&1 语句含义：
      1 > /dev/null ： 首先表示标准输出重定向到空设备文件，也就是不输出任何信息到终端，说白了就是不显示任何信息。
      2>&1 ：接着，标准错误输出重定向（等同于）标准输出，因为之前标准输出已经重定向到了空设备文件，所以标准错误输出也重定向到空设备文件。
      最后的&表示脱离终端控制
      ```



* #### 查看Storm服务信息

    + 查看Storm服务日志

      日志信息在 /apache-storm-1.2.3/logs 目录

      ![logs](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/logs.png)

    + 访问Storm Web界面

      ![s2.2.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.1.png)

      - Cluster Summary是集群的总体信息，可以看到本文使用的Storm版本为1.2.3共有一个Supervisor进程，共有4个slot资源, 且目前使用0个
      - Nimbus Summary是集群的Nimbus进程信息，可以看到只有在本地启动的一个Nimbus进程
      - Topolopy Summary是用户提交的拓扑信息
      - Supervisor 是集群中Supervisor进程信息

* #### 运行Storm应用程序

    + 运行Storm应用程序命令: `bin/storm jar wordcount.jar StormWordCount cluster`

    + jps查看进程

       ![s2.2.4](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.4.png)
    	
    	可以看到运行应用程序时相比不运行时多出来两个worker进程和其对应的两个日志进程LogWriter

    + 访问Storm Web界面

        ![s2.2.5](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.5.png)

         可以看到应用程序已经在运行了，它的执行状态是ACTIVE，共有两个worker进程，27个Task

        ![s2.2.7](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.7.png)

    + 查看拓扑图 

        ![s2.2.6](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.6.png)
        
    	Spout作为数据源，一个分词Bolt一个计数Bolt构成了WordCount应用程序

* #### 终止Storm应用程序

    方法一：终端命令 `./bin/storm kill word-count`

    方法二：ui界面kill

    kill之后可以在ui界面看到应用程序的状态变为KILLED

* #### 停止Storm服务

    + 停止服务命令 

      ```shell
      kill -9 nimbusNum
      kill -9 supervisorNum
      kill -9 logviewerNum
      kill -9 coreNum
      zkServer.sh stop
      ```
      ![s2.2.8](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.8.png)

      nimbusNum为nimbus进程号，supervisorNum为supervisor进程号，logviewerNum为logviewer进程号，coreNum为core进程号，进程号可以通过jps命令查看

    + jps查看进程

    	![s2.2.9](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s2.2.9.png)

    	可以看到Storm所有进程都已关闭
    	
#### 3. 分布式部署

由于Storm系统利用zookeeper做任务调度和元数据存储，故欲部署Storm须先部署Zookeeper，JDK默认已配好不再赘述

##### 3.1 部署zookeeper

* #### 准备工作

    + 下载软件包：[zookeeper官网](http://zookeeper.apache.org/releases.html#download) ,下载zookeeper-3.4.13.tar.gz
	
    + 解压：`tar -zxvf zookeeper-3.4.13.tar.gz` ，保存在/home/dase/zookeeper-3.4.13
	
* #### 修改配置文件

  + 进入目录：`cd home/dase/zookeeper-3.4.13/`

    + 复制模板：`cp zoo_sample.cfg zoo.cfg`
    
  + 配置修改：`vi zoo.cfg`

    ![zoo_cfg](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/zoo_cfg.png)

    + 创建data目录，然后再下面创建一个myid，写入机器对应配置里的数字
    ```
    mkdir data
    cd data
    mkdir log
    echo 1 > myid
    ```
    + 复制zookeeper文件夹到别的机器上去，并且修改myid
    ```shell
    scp -r~/zookeeper-3.4.13 219.228.135.124:/home/ecnu/
    echo 2 > zookeeper-3.4.13/data/myid
    ```
    ![zkp_scp](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/zkp_scp.png)

* #### 启动各节点zookeeper服务，注意关闭防火墙

    + 启动命令: `zkServer.sh start`

    + jps查看进程

      ![zkper_start](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/zkper_start.png)

      ![zk_jps_slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/zk_jps_slaves.png)

      可以看到各个节点上都有QuorumPeerMain即zookeeper的服务进程		

      ![status-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/status-slaves.png)

      ![status-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/status-master.png)

    + 查看zookeeper启动日志，输入命令：`cat ./zookeeper-3.4.13/zookeeper.out`

      ![log_master](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/log_master.png)

##### 3.2 Storm配置

* #### 准备工作

	下载软件包：访问[Storm官网](http://storm.apache.org/downloads.html) ，下载storm-1.2.3.tar.gz
	
	解压: `tar -zxvf apache-storm-1.2.3.tar.gz` ，保存在/home/dase/storm-1.2.3
  
	下载示例代码jar包：[wordcount.jar](./storm/code/wordcount.jar)，保存在/home/dase/storm-1.2.3
	
* #### 修改配置文件

  Storm配置文件为storm.yaml，修改配置文件命令: `vi  apache-storm-1.2.3/conf/storm.yaml`	

  添加如下设置，注意空格
  ```yaml
  storm.zookeeper.servers: #zookeeper节点 
  	- "219.228.135.71"
  	- "219.228.135.124"
  nimbus.seeds: ["219.228.135.71","219.228.135.124"] #nimbus进程服务器ip列表
  storm.local.dir: "/home/ecnu/apache-storm-1.2.3/data"  #需要自己创建
  ui.port: 18080 #ui端口
  supervisor.slots.ports: #给supervisor四个端口，即supervisor可以创建四个worker进程
  	- 6700
  	- 6701
  	- 6702
    	- 6703
  ```

* #### 启动Storm服务

    + 启动命令

      主节点上 
      
      ```shell
      bin/storm nimbus >/dev/null 2>&1 &
      bin/storm ui >/dev/null 2>&1 &
      bin/storm logviewer > /dev/null 2>&1 &
      ```
      
      从节点上
      
      ```shell
      bin/storm supervisor >/dev/null 2>&1 &
      bin/storm logviewer > /dev/null 2>&1 &
      ```
      
    + jps查看进程

       ![storm_master_jps](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm_master_jps.png)

       ![storm_jps_slave](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm_jps_slave.png)

       	有图可以看到，主节点上的nimbus进程，core进程和logviewer进程均已启动，从节点上supervisor和	logviewer进程也以启动

* #### 查看Storm服务信息

    + 查看Storm服务日志

      日志信息在 /apache-storm-1.2.3/logs 目录

      ![log_master](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/log_master.png)

      ![logs_slaves](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/logs_slaves.png)

    + 访问Storm Web界面

      ![image-20191124200209693-1574598700213](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm/Storm分布式/image-20191124200209693-1574598700213.png)

      Cluster Summary是集群的总体信息，可以看到本文使用的Storm版本为1.2.3共有两个个Supervisor进程，共有8个slot资源且目前均未使用

      Nimbus Summary是集群的Nimbus进程信息，可以看到只有在本地启动的一个Nimbus进程

      Topolopy Summary是用户提交的拓扑信息

      Supervisor 是集群中Supervisor进程信息

* #### 运行Storm应用程序

    - 运行Storm应用程序命令  `bin/storm jar wordcount.jar StormWordCount cluster`

      ![image-20191124202208171](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm/Storm分布式/image-20191124202208171.png)

    - jps查看进程,可以明显地看到worker节点多出来一个worker进程和其对应的一个日志进程LogWriter：

      ![image-20191124200802460-1574598700213](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm/Storm分布式/image-20191124200802460-1574598700213.png)

    - 访问Storm Web界面

      ![image-20191124200209693](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm/Storm分布式/image-20191124200209693.png)

      可以看到应用程序已经在运行了，它的执行状态是ACTIVE，共有两个worker进程，27个Task

    - 查看拓扑图
      ![image-20191124202720400](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/storm/Storm分布式/image-20191124202720400.png)

      Spout作为数据源，一个分词Bolt,一个计数Bolt构成了WordCount应用程序

* #### 终止应用程序

    方法一：终端命令 `./bin/storm kill word-count`

    方法二：ui界面kill

### Storm-programming

#### 1. 编写Storm程序

- #### 新建Maven项目并添加pom依赖


- #### IDE环境编写代码

  一个基本的Strom程序由Spout、bolt和Topology三部分组成，Spout是是用于数据生成的组件，Bolt是Storm的计算基本单位可以称其为算子，Topology就是Spout和Boltd的摆放组合加上一些配置信息，本文将按照Spout,Bolt和Topology
  这个顺序介绍Storm程序的编写

  现在创建一个能够无限产生String数据的数据源Spout，在src/mian/java/目录下新建Java class取名RandomSentenceSpout
   填写如下代码

- 不启用ACK机制


- 启用ACK机制

#### 2. 调试Storm程序

- #### IDE中直接运行

  Run->Edit-configuration->Main class  填写内容 `WordCount` 表示入口类  Program arguments 填写内容 `local` 表示本地运行 点击 `OK` 按钮

  Run->Run ``WordCount``

  结果如图

  ![s编程1](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s编程1.png)

- #### 调试经验

  - IDE中设置断点

#### 3. 运行Storm程序

- #### 利用IDE打包jar文件

  [IDEA打jar包教程](../IDEA/IDEA jar.md)

- #### 伪分布模式下提交Storm程序

  经过上述步骤，我们已经得到打包好的Storm代码myjar.jar,接下来在Storm服务进程中运行Storm代码

  进入Storm目录：`cd ~/apache-storm-1.2.2/`

  执行命令： `bin/storm jar myjar.jar WordCountTopology local` (单机模式)

  ![streaming_jar_local](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/streaming_jar_local.png)

  ![streaming_local_output](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/streaming_local_output.png)

  `storm jar /home/ecnu/test/out/artifacts/storm_wordcount/storm_wordcount.jar WordCount cluster` (分布式模式)

  ![s编程3](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s编程3.png)

- 分布式模式下提交Storm程序

  在真实的生产环境中，一个Storm应用程序通常是运行在集群上以满足计算性能的需求。下面介绍如何在集群环境中运行Storm代码其实很简单，首先是上传jar包，执行scp命令将jar包上传至集群

  `scp myjar.jar usrname@ip:/home/usrname/apache-storm-1.2.3`

  再进入集群执行提交应用程序命令，进入Storm目录：`cd ~/apache-storm-1.2.3/`

  执行命令： 

  ![jar_cluster](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/dist/jar_cluster.png)

  这样一个Storm应用程序就在集群中跑起来了

  **上述程序在cluster方式运行看不到输出，试修改以上程序使得cluster方式下将结果输出到文件中。**

  change the file CountBolt.java as follows: 

  ```java
  package example.storm.wordcount;

  import org.apache.storm.task.TopologyContext;
  import org.apache.storm.topology.BasicOutputCollector;
  import org.apache.storm.topology.OutputFieldsDeclarer;
  import org.apache.storm.topology.base.BaseBasicBolt;
  import org.apache.storm.tuple.Fields;
  import org.apache.storm.tuple.Tuple;
  import org.apache.storm.tuple.Values;

  import java.io.*;
  import java.nio.channels.FileChannel;
  import java.nio.channels.FileLock;
  import java.util.Calendar;
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
          String data = "(" + word + "," + count + ")";
          Calendar calstart= Calendar.getInstance();
          try {
              File file =new File("/home/syx/apache-storm-1.2.3/result.txt");
              FileOutputStream fos = null;
              if(!file.exists()){
                  file.createNewFile();//如果文件不存在，就创建该文件
                  fos = new FileOutputStream(file);//首次写入获取
              }
              else if (file.ecists)
              {
                  //参数true,表示在文件末尾追加写入
                  fos = new FileOutputStream(file,true);
              }
              //对文件加锁
              RandomAccessFile out = new RandomAccessFile(file, "rw");
              FileChannel fcout=out.getChannel();
              FileLock flock=null;
              while(true){
                  try {
                      flock = ((FileChannel) fcout).tryLock();
                      break;
                  } catch (Exception e) {
                      Thread.sleep(100);
                  }
              }
  			//指定以UTF-8格式写入文件
              OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
              osw.write(data);
              
              osw.close();
              flock.release();
              fcout.close();
              out.close();
              out=null;
          }
          catch (Exception e) {
              e.printStackTrace();
          }
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

  Output:

  ![s3](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/s3.png)

# Mistakes

1. annotation shouldn't follow the code in the same line

   ![m1.1](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/m1.1.png)

   ![m1.2](/home/syx/文档/Dase/DistributedSytem/Hands_on/lab3/pic/m1.2.png)

   ```shell
   2019-11-06 20:36:33,736 [myid:] - INFO  [main:QuorumPeerConfig@136] - Reading configuration from: /home/syx/zookeeper-3.4.13/bin/../conf/zoo.cfg
   2019-11-06 20:36:33,738 [myid:] - ERROR [main:QuorumPeerMain@88] - Invalid config, exiting abnormally
   org.apache.zookeeper.server.quorum.QuorumPeerConfig$ConfigException: Error processing /home/syx/zookeeper-3.4.13/bin/../conf/zoo.cfg
   	at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parse(QuorumPeerConfig.java:156)
   	at org.apache.zookeeper.server.quorum.QuorumPeerMain.initializeAndRun(QuorumPeerMain.java:104)
   	at org.apache.zookeeper.server.quorum.QuorumPeerMain.main(QuorumPeerMain.java:81)
   Caused by: java.lang.NumberFormatException: For input string: "48 #hi"
   	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
   	at java.lang.Integer.parseInt(Integer.java:580)
   	at java.lang.Integer.parseInt(Integer.java:615)
   	at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parseProperties(QuorumPeerConfig.java:211)
   	at org.apache.zookeeper.server.quorum.QuorumPeerConfig.parse(QuorumPeerConfig.java:152)
   	... 2 more
   Invalid config, exiting abnormally
   ```

3. configure: change '\t' to ' '

   ```yaml
   # wrong
   storm.zookeeper.servers: #zookeeper节点 
   	- "127.0.0.1" 
   nimbus.seeds: ["127.0.0.1"] #nimbus进程服务器ip
   storm.local.dir: "/home/xxx/apache-storm-1.2.2/data"  #需要自己创建
   ui.port: 18081 #ui端口
   supervisor.slots.ports: #给supervisor四个端口，即supervisor可以创建四个worker进程
   	- 6700
   	- 6701
   	- 6702
   	- 6703
   ```

   ```yaml
   # right
   storm.zookeeper.servers: #zookeeper节点 
    - "127.0.0.1" 
   nimbus.seeds: ["127.0.0.1"] #nimbus进程服务器ip
   storm.local.dir: "/home/xxx/apache-storm-1.2.2/data"  #需要自己创建
   ui.port: 18081 #ui端口
   supervisor.slots.ports: #给supervisor四个端口，即supervisor可以创建四个worker进程
    - 6700
    - 6701
    - 6702
    - 6703
   ```

   ​

