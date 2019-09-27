## Run successfully

#### 启动MapReduce服务

查看进程，验证是否成功启动服务

![mapred_hdfs](/home/syx/桌面/mapred_hdfs.png)



#### 提交MapReduce应用程序

提交jar命令并查看运行结果
运行grep示例：

![3](/home/syx/桌面/3.png)



#### 查看运行过程中的进程

运行 wordcount 示例，并且查看系统执行该任务过程中启动的进程：

![8](/home/syx/桌面/8.png)

![7](/home/syx/桌面/7.png)

jps:

![6](/home/syx/桌面/6.png)

####  启动MapReduce服务

- 启动YARN命令

- 开启历史服务器

- 启动HDFS服务

  ![18](/home/syx/桌面/18.png)运行MapReduce应用程序

  - 提交jar命令并查看运行结果
    运行grep示例, 结果如下：

    ![19](/home/syx/桌面/19.png)

    ![20](/home/syx/桌面/20.png)

    ![21](/home/syx/桌面/21.png)运行 wordcount 示例

    ![22](/home/syx/桌面/22.png)

    ![23](/home/syx/桌面/23.png)

    ​

## Mistakes

1. FS![m1](/home/syx/桌面/m1.png)

   solu:

   ![2019-09-26 19-15-17屏幕截图](/home/syx/桌面/2019-09-26 19-15-17屏幕截图.png)

   hadoop's FS is different from linux's FS.  I have tried 

   ```linux
   sudo rm -rf /user/syx/input
   ```

   but this "input" is not the one in the hadoop FS.

2. ​