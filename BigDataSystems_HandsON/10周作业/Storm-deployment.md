# 1. 单机集中式

* #### 准备工作
	
	+ 安装配置JDK
	
	  下载软件包：访问[Oracle官网](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) ，下载jdk-8u202-linux-x64.tar.gz 
  
	  解压：` tar -zxvf jdk-8u192-linux-x64.tar.gz` ，保存在/home/dase/jdk1.8.0_192目录下，其中假设本机用户名为dase
  
	  配置环境变量:
  
    ```shell
    vi ~/.bashrc
    在该配置文件中添加以下内容
    export JAVA_HOME=/home/dase/jdk1.8.0_192
    export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
    export PATH=$JAVA_HOME/bin:$PATH
    保存并退出(Esc,:wq)
    source ~/.bashrc
    ```
	  
	  ​
	
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

# 2. 单机伪分布式
由于Storm系统利用zookeeper做任务调度和元数据存储，故欲部署Storm须先部署Zookeeper，JDK默认已配好不再赘述

## 2.1 部署zookeeper

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
		dataDir=/home/dase/zookeeper-3.4.13/data  #存放数据的目录
		clientPort=2181   #客户端默认的端口号
		dataLogDir=/home/dase/zookeeper-3.4.13/data/log  #存放log的目录
		autopurge.snapRetainCount=20  #保留的快照数目
		autopurge.purgeInterval=48   #定期清理快照，时间单位：时
		server.1=127.0.0.1:2888:3888  #主机名, 心跳端口，数据端口
		```
    
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

    ```
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

## 2.2 Storm安装配置

* #### 准备工作
  
	下载软件包：访问[Storm官网](http://storm.apache.org/downloads.html) ，下载storm-1.2.3.tar.gz
	
	解压: `tar -zxvf apache-storm-1.2.3.tar.gz` ，保存在/home/dase/storm-1.2.3
  
	下载示例代码jar包：[wordcount.jar](./storm/code/wordcount.jar)，保存在/home/dase/storm-1.2.3
  
* #### 修改配置文件

	Storm配置文件为storm.yaml，修改配置文件命令: `vi  apache-storm-1.2.3/conf/storm.yaml`
	添加如下设置，注意空格
	
	```yaml
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
	
  保存并退出
	
* #### 启动Storm服务

    + 启动命令

		```shell
		bin/storm nimbus >/dev/null 2>&1 &
		bin/storm ui >/dev/null 2>&1 &
		bin/storm logviewer > /dev/null 2>&1 &
		bin/storm supervisor >/dev/null 2>&1 &
		```
    
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
    
   + jps查看进程


   ![](./storm/storm/单机伪分布进程启动.png)

    由图可知，同一台机器上运行不同进程，此时的Storm中的nimbus，supervisor，core和logviewer进程在同一台机器上均已启动，其中core为ui进程，logviewer为日志进程

* #### 查看Storm服务信息

    + 查看Storm服务日志
	
		日志信息在 /apache-storm-1.2.3/logs 目录
		
    + 访问Storm Web界面
	
		![](./storm/storm/ui2.png)
		
		Cluster Summary是集群的总体信息，可以看到本文使用的Storm版本为1.2.3共有一个Supervisor进程，共有4个slot资源且目前均未使用
		
		Nimbus Summary是集群的Nimbus进程信息，可以看到只有在本地启动的一个Nimbus进程
		
		Topolopy Summary是用户提交的拓扑信息
		
		Supervisor 是集群中Supervisor进程信息
	
* #### 运行Storm应用程序

    + 运行Storm应用程序命令: `bin/storm jar wordcount.jar StormWordCount cluster`

    + jps查看进程

       ![](./storm/storm/提交任务后.png)
		
		可以看到运行应用程序时相比不运行时多出来两个worker进程和其对应的两个日志进程LogWriter

    + 访问Storm Web界面
	
        ![](./storm/单机伪分布ui.png)
		
		 可以看到应用程序已经在运行了，它的执行状态是ACTIVE，共有两个worker进程，27个Task
		
    + 查看拓扑图 
	
        ![](./storm/单机伪分布tp.png)
        
		Spout作为数据源，一个分词Bolt,一个计数Bolt构成了WordCount应用程序
	
* #### 终止Storm应用程序

    方法一：终端命令 `./bin/storm kill word-count`

    方法二：ui界面kill
    
    ![](./storm/kill任务.png)
	
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
		nimbusNum为nimbus进程号，supervisorNum为supervisor进程号，logviewerNum为logviewer进程号，coreNum为core进程号，进程号可以通过jps命令查看
    	
    + jps查看进程
	
		![](./storm/storm/nothing.png)
	
		可以看到Storm所有进程都已关闭
		
# 3. 分布式部署
由于Storm系统利用zookeeper做任务调度和元数据存储，故欲部署Storm须先部署Zookeeper，JDK默认已配好不再赘述

## 3.1 部署zookeeper

* #### 准备工作

    + 下载软件包：[zookeeper官网](http://zookeeper.apache.org/releases.html#download) ,下载zookeeper-3.4.13.tar.gz
	
    + 解压：`tar -zxvf zookeeper-3.4.13.tar.gz` ，保存在/home/dase/zookeeper-3.4.13
	
* #### 修改配置文件

	+ 进入目录：`cd home/dase/zookeeper-3.4.13/`

    + 复制模板：`cp zoo_sample.cfg zoo.cfg`
    
	+ 配置修改：`vi zoo.cfg`
	
    ```
    tickTime=2000   #心跳间隔
    initLimit=10   #初始容忍的心跳数
    syncLimit=5   #等待最大容忍的心跳数
    dataDir=/home/xxxx/zookeeper-3.4.13/data  #存放数据的目录
    clientPort=2181   #客户端默认的端口号
    dataLogDir=/home/xxxx/zookeeper-3.4.13/data/log  #存放log的目录
    autopurge.snapRetainCount=20  #保留的快照数目
    autopurge.purgeInterval=48   #定期清理快照，时间单位：时
    server.1=xxx.xxx.xxx.xxx:2888:3888  #主机名, 心跳端口，数据端口
    server.2=xxx.xxx.xxx.xxx:2888:3888
    ```
    + 创建data目录，然后再下面创建一个myid，写入机器对应配置里的数字
    ```
    mkdir data
    cd data
    mkdir log
    echo 1 > myid
    ```
    + 复制zookeeper文件夹到别的机器上去，并且修改myid
    ```
    scp -r zookeeper-3.4.13 ip:/home/xxx/
    echo 2 > zookeeper-3.4.13/data/myid
    ```
* #### 启动各节点zookeeper服务，注意关闭防火墙
  
    + 启动命令: `zkServer.sh start`

	+ jps查看进程
    
    ![](./storm/storm/分布式zookeeper启动.png)
	
	可以看到各个节点上都有QuorumPeerMain即zookeeper的服务进程

	+ 查看zookeeper启动日志，输入命令：`cat ./zookeeper-3.4.13/zookeeper.out`

## 3.2 Storm配置

* #### 准备工作

	下载软件包：访问[Storm官网](http://storm.apache.org/downloads.html) ，下载storm-1.2.3.tar.gz
	
	解压: `tar -zxvf apache-storm-1.2.3.tar.gz` ，保存在/home/dase/storm-1.2.3
  
	下载示例代码jar包：[wordcount.jar](./storm/code/wordcount.jar)，保存在/home/dase/storm-1.2.3
	
* #### 修改配置文件
  
	Storm配置文件为storm.yaml，修改配置文件命令: `vi  apache-storm-1.2.3/conf/storm.yaml`	
	
	添加如下设置，注意空格
	```yaml
	storm.zookeeper.servers: #zookeeper节点 
		- "xxx.xxx.xxx.xxx"
		- "xxx.xxx.xxx.xxx"
	nimbus.seeds: ["xxx.xxx.xxx.xxx","xxx.xxx.xxx.xxx"] #nimbus进程服务器ip列表
	storm.local.dir: "/home/xxx/apache-storm-1.2.1/data"  #需要自己创建
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
    
    		![](./storm/storm/分布式storm启动.png)
    
    		有图可以看到，主节点上的nimbus进程，core进程和logviewer进程均已启动，从节点上supervisor和	logviewer进程也以启动
* #### 查看Storm服务信息

    + 查看Storm服务日志
	
		日志信息在 /apache-storm-1.2.3/logs 目录
    
    + 访问Storm Web界面
    
    
    ![](./storm/storm/分布式启动ui.png) 
    
	Cluster Summary是集群的总体信息，可以看到本文使用的Storm版本为1.2.3共有两个个Supervisor进程，共有8个slot资源且目前均未使用
	​	
	Nimbus Summary是集群的Nimbus进程信息，可以看到只有在本地启动的一个Nimbus进程
	​	
	Topolopy Summary是用户提交的拓扑信息
	​	
      Supervisor 是集群中Supervisor进程信息

* #### 运行Storm应用程序

    + 运行Storm应用程序命令  `bin/storm jar wordcount.jar StormWordCount cluster`

    + jps查看进程

       ![](./storm/storm/分布式提交jps.png)
       
       可以看到运行应用程序时每一个工作节点相比不运行时各多出来一个worker进程和其对应的一个日志进程LogWriter
       
    + 访问Storm Web界面
       ![](./storm/storm/分布式提交ui.png)
    
        可以看到应用程序已经在运行了，它的执行状态是ACTIVE，共有两个worker进程，27个Task
    
   * 查看拓扑图 
       ​        ![](./storm/单机伪分布tp.png)
       
         
       
       Spout作为数据源，一个分词Bolt,一个计数Bolt构成了WordCount应用程序
   
* #### 终止应用程序

    方法一：终端命令 `./bin/storm kill word-count`

    方法二：ui界面kill

    ![](./storm/kill任务.png)

    kill之后可以在ui界面看到应用程序的状态变为KILLED

* #### 停止服务

    + 先用jps查看storm进程号，然后用：kill -9 x，杀死对应的进程，x为对应进程的进程号
    + 顺序为 先kill掉nimbus，再kill supervisor ，logviewer,core 最后关闭zookeeper服务，执行如下脚本
    ```shell
    #nimbus
    nimbusServers='ip0 ip1'
    
    #supervisor
    supervisorServers='ip0 ip1 ip2'
    
    #stop all nimbus and core(ui)
    for nim in $nimbusServers
    do 
    	ssh $nim "kill -9 `ssh $nim ps -ef | grep nimbus | awk '{print $2}' | head -n 2`" >/dev/null 2>&1
    	ssh $nim "kill -9 `ssh $nim ps -ef | grep core | awk '{print $2}' | head -n 2`" >/dev/null 2>&1
        ssh $nim "kill -9 `ssh $nim ps -ef | grep logviewer | awk '{print $2}' | head -n 2`" >/dev/null 2>&1
    done
    
    for visor in $supervisorServers
    do
    ssh $visor "kill -9 `ssh $visor ps -ef | grep supervisor | awk '{print $2}' | head -n 2`" >/dev/null 2>&1
    ssh $visor "kill -9 `ssh $nim ps -ef | grep logviewer | awk '{print $2}' | head -n 2`" >/dev/null 2>&1
    done
    ```
    + jps查看进程
    

![](./storm/storm/终止服务.png)
​    
可以看到所有节点的storm服务进程均已停止





# mistakes

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

   ​

