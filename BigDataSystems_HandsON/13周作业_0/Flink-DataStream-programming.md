# 1. 编写Flink程序
+ #### 新建Maven项目并添加pom依赖

    + [新建maven项目](../Create Maven Project/create maven.md)
    
    + 修改pom.xml
    
      ```xml
      <dependencies>
      <!-- https://mvnrepository.com/artifact/org.apache.flink/flink-streaming-java -->
       <dependency>
           <groupId>org.apache.flink</groupId>
           <artifactId>flink-streaming-java_2.11</artifactId>
           <version>1.7.2</version>
           <scope>compile</scope>
       </dependency>
      
      <!-- https://mvnrepository.com/artifact/org.apache.flink/flink-core -->
       <dependency>
           <groupId>org.apache.flink</groupId>
           <artifactId>flink-core</artifactId>
           <version>1.7.2</version>
       </dependency>
      </dependencies>
      ```

+ #### IDE环境编写代码
    + 编写java代码，在src->main->java目录下新建名为DataStreamWordCount的Java类，并编写如下代码                                                                                                                                      
    
      ```java
      import org.apache.flink.api.common.functions.FlatMapFunction;
      import org.apache.flink.api.common.functions.ReduceFunction;
      import org.apache.flink.api.java.utils.ParameterTool;
      import org.apache.flink.streaming.api.datastream.DataStream;
      import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
      import org.apache.flink.streaming.api.windowing.time.Time;
      import org.apache.flink.util.Collector;
      
      public class DataStreamWordCount {
          public static void main(String[] args) throws Exception {
              //获取hostname和port
              final String hostname;
              final int port;
              try {
                  final ParameterTool params = ParameterTool.fromArgs(args);
                  hostname = params.has("hostname") ? params.get("hostname") : "localhost";
                  port = params.getInt("port");
              } catch (Exception e) {
                  //提示错误信息
                  System.err.println("No port specified. Please run 'SocketWindowWordCount " +
                          "--hostname <hostname> --port <port>', where hostname (localhost by default) " +
                          "and port is the address of the text server");
                  System.err.println("To start a simple text server, run 'netcat -l <port>' and " +
                          "type the input text into the command line");
                  return;
              }
      
              //获取执行环境
              final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
      
              //从连接的套接字获取数据
              DataStream<String> text = env.socketTextStream(hostname, port, "\n");
      
              // 分析数据对其分组并指定数据的窗口大小进行聚合计数
              DataStream<WordWithCount> windowCounts = text
      
                      .flatMap(new FlatMapFunction<String, WordWithCount>() {
                          @Override
                          public void flatMap(String value, Collector<WordWithCount> out) {
                              for (String word : value.split("\\s")) {
                                  out.collect(new WordWithCount(word, 1L));
                              }
                          }
                      })
      
                      .keyBy("word")
                      .timeWindow(Time.seconds(5))
      
                      .reduce(new ReduceFunction<WordWithCount>() {
                          @Override
                          public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                              return new WordWithCount(a.word, a.count + b.count);
                          }
                      });
      
              //设置并行度为1，即单线程打印
              windowCounts.print().setParallelism(1);
      
              env.execute("Socket Window WordCount");
          }
      
          // ------------------------------------------------------------------------
      
          //记录单词及次数
          public static class WordWithCount {
      
              public String word;
              public long count;
      
              public WordWithCount() {}
      
              public WordWithCount(String word, long count) {
                  this.word = word;
                  this.count = count;
              }
      
              @Override
              public String toString() {
                  return word + " : " + count;
              }
          }
  }
      ```
    
      
# 2. 调试Flink程序
+ #### IDE中直接运行
    + 配置运行环境，并进行本地调试。在IntelliJ菜单栏中选择Run->Edit Configuration，在弹出对话框中新建Application配置，配置Main Class为DataStreamWordCount，Program arguments为hostname port，分别为主机名和端口号，默认主机名为localhost。如下图所示：

      

      ![](./images/EditConfiguration-DataStream.png)
    
      
    
    + 配置完成后，右键->Run'DataStreamWordCount'
    
    + 运行结果如下：
    
    输入：
    
    ![](./images/flink_datastream_input.png)

    输出：
    
    ![](./images/flink_datastream_output.png)
    
+ #### 调试经验
    +  IDE中设置断点

# 3. 运行Flink程序


+ #### 利用IDE打包jar文件

    [IDEA打jar包教程](../IDEA/IDEA jar.md)

+ #### 伪分布模式下提交Flink程序
    + 在终端输入命令：`flink run --class 主类名 jar包路径 -- hostname xxx --port xxx`，向jobmanager提交作业
    
    + 如下所示：
    
      ```shell
      ./flink run --class DataStreamWordCount /home/user/FlinkProgram.jar --hostname localhost --port 9000
      ```
    
      另起终端输入如下命令查看运行结果
    
      ```shell
      tail -f flink-xxx-taskexecutor-x-xxx.out
      ```
    
      
    
+ #### 分布式模式下提交flink程序
    + 首先是上传jar包，执行scp命令将jar包上传至集群
    
      ```shell
      scp flinkprogram.jar usrname@ip:/home/username/flink
      ```
    + 在client中输入命令：`flink run --class 主类名 jar包路径 -- hostname xxx --port xxx`，向集群中的jobmanager提交作业
    
    + 如下所示： 
    
      ```shell
      ./bin/flink run DataStreamWordCount /home/user/FlinkProgram.jar --hostname 20s211  --port 9000
      ```
    
      20s211另起终端，在flink log目录下输入如下命令，查看结果
    
      ```shell
      tail -f flink-xxx-taskexecutor-x-xxx.out
      ```

