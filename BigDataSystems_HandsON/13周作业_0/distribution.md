### 3.5 运行Flink DataStream程序

* 通过提交 jar 包运行DataStream程序

  * 默认模式提交，可以在客户端看到应用程序运行过程中的信息

    ![jar_default](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar_default.png)

    使用jps命令，默认模式提交出现CliFrontend进程，用于提交作业并接受返回信息，应用程序运行结束后该进程消失。

    ![cliFrontend-master](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/cliFrontend-master.png)

    在程序运行位置的flink log目录下输入命令：tail -f flink-xxx-taskexecutor-x-xxx.out，查看运行结果：  

    ![jar_default_1](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar_default_1.png)

    访问flink web界面查看程序运行位置

    ![jar-web](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar-web.png)

    

    DataStream程序终止的两种方法

    - 在[WebUI](localhost:8081)上`cancel`
    - 命令行终止`flink cancel JobID`，`JobID`通过`flink list`查询

  **注：杀掉客户端进程是无法停止程序的**

  * detached模式提交，在客户端看不到应用程序运行过程中的信息

  * 在程序运行位置的flink log目录下输入命令：tail -f flink-xxx-taskexecutor-x-xxx.out，查看运行结果：

    ![jar-detached_master](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar-detached_master.png)

    `程序提交完毕后退出客户端`

    detached模式下无CliFrontend进程

    ![jar_detached-jps](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar_detached-jps.png)

    访问flink web界面查看程序运行位置

    ![jar_detached_web](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/jar_detached_web.png)

      

    

* 停止flink正在运行中的程序

  + 在flink web界面中，进入running jobs，选取正在运行的程序，点击右上角cancel按钮

  + 使用命令行停止。先使用命令./bin/flink list获取正在运行的程序及其ID，再使用命令./bin/flink cancel ID根据程序ID停止程序

    ![flink_cancel](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/flink_cancel.png)

### 3.6 停止Flink服务

* 停止命令 

  ```
  >>> ~/flink-1.7.2/bin/stop-cluster.sh
  ```

* 查看进程，验证是否成功停止服务

  *   若成功停止，JobManager进程和TaskManager进程应消失，如下图所示：
  *   ![stop-cluster](/home/syx/文档/Dase/DistributedSytem/Hands_on/13周作业/dist/stop-cluster.png)

