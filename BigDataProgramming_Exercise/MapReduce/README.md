
## 背景

1. MapReduce的key和value必须实现Writable接口
2. NullWritable.get()

## 通用类

1. String

   - 声明: `String value = null`

   - 函数:

     ```java
     // 字串:substr
     value.contains(substr);
     // 
     ```

     

2. Text

   - 声明: `Text word = new Text();`

   - 函数: 

     ```java
     // set
     word.set(str);
     ```

3. Context: 

   - 声明: `Context contextt = new Context` 

   - 函数: 

     ```java
     // write
     context.write(word, 1);
     //
     context.getConfiguration().getInt("")
     ```

4. Iterable: 

   - 声明: `Iterable<IntWritable> values = new Iterable();` 

   - 函数:

     ```java
     // get
     values.get();
     // hasNext()
     Iterator<String> iter = list.iterator();
     while (iter.hasNext()) {
         System.out.println(iter.next() + " ");
     }
     ```

5. IntWritable 

   - 声明: `IntWritable one = new IntWritable(value:1);`

6. 工具:

   - Pattern: `Pattern.compile("\\w+").matcher(str).replaceAll(replacement: "")`

   - Math: 

     `Math.pow((data.get(j) - center.get(j)), 2);`

## warm_up

1. 思路: 

   - MapperImpl: 分词, 并给collect发送(word, 1)

     ```java
     // one: intWritable(1)
     context.write(word, one);
     ```

   - ReducerImpl: 计数

   - Context: 通讯和结果输出

2. MapperImpl

   - class: 

     - StringTokenizer

       - ```java
         // 声明
         StringTokenizer itr = new StringTokenizer(value.toString());
         // 成员变量
         // 成员函数
         itr.hasMoreTokens();
         itr.newtToken();
         ```

       - 函数定义

         ```java
         public StringTokenizer(String str) {
                 this(str, " \t\n\r\f", false);
             }
         ```

3. Reducer: 

   - class: 
     - Iterable

## Multiinput-[

3. ArrayList<>():

   - 声明: `ArrayList l = new ArrayList<String> ()`

   - 函数: 

     ```java
     // add(value)
     l.add(value);
     // get(index): return [value]
     l.get(0);
     ```

     

## Multi_input_join

1. 调试: 

   - 分割字符串注意分割后的长度, 涉及到手动join的操作需要抛弃掉join后的悬浮数组(即, 长度小于2的一般直接return)

   - 注意深浅拷贝: 给Text赋值时, 使用:

     ```java
     Text name = new Text();
     name.set(new Text(value));
     // 直接使用=赋值会拷贝地址
     // 建议少用=给Text赋值, 尽量用list, 在循环中new Text()产生新地址
     ```

   - 

2. 思路: 

   - person表和order表的元组可能是1:N的关系, 在reduce时, 需要用lis来装载order_id;

3. 实现:

   - class: 

     - TextPair

       ```java
       // get
       .setData()
       // set
       .getData()
       ```

## K_means

1. 思路: 
   - 运行一次
   - 判断是否停止(新旧中心点的差距)

## SSP

1. 思路: 不断更新节点到A的最短距离

   - 首轮将A的distance设置为"0", 其他节点到A的distance设置为"inf";
   - 非首轮, 若本节点到A的距离非"inf", 则更新本节点的邻接点到A的距离为: 本节点到A的距离+邻接点到A的距离;
   - 判断是否需要继续迭代: isChange(node, 本次reduce得出的mindistance, context);

2. class:

   - context

     ```java
     context.getConfiguration.getInt(name, defaultvalue);
     ```

   - node

     ```java
     node.toString();
     node.getNodenum();
     node.getNodekey(index);
     node.getNodeValue(index);
     // 获取到A的距离
     node.getDistance();
     ```

     
     
## MMM
# MapReduce 编程
```
(input) <k1, v1> -> 
map -> <k2, v2> -> 
combine -> <k2, v2> -> 
reduce -> <k3, v3> (output)

MapReduce 至今用到的类和函数

Class Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
四个参数分别为传入键的类型、传入值的类型、传出键的类型、传出值的类型

map(KEY key, VALUE value, Context context)
第一个参数是传入的键
第二个参数是传入的值
第三个参数是在Mapper中生成键值对传给Reducer用的

Class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
四个参数分别为传入键的类型、传入值的类型、传出键的类型、传出值的类型
reduce(KEYIN key, Iterable<VALUEIN> values, Context context)
第一个和第二参数同理， 第三个参数是生成键值对传出

Context<KEYIN,VALUEIN,KEYOUT,VALUEOUT> // A context object that allows input and output from the task. It is only supplied to the Mapper or Reducer.
    .write(KEYOUT key, VALUEOUT value) // 生成一组键值





IntWritable() // 一个存储Integer的可比较类 
    .(int value) // 初始化设置Inwritable的值 

NullWritable() // 无数据的Writable，充当占位符用，
    .get() // 返回一个新的Nullwritable，不能new NullWritable生成

LongWritable // // 一个存储Long的可比较类 
    .get() // 返回存储的类型为Long的值

Text() // 存储文本类 
    .set(String string) // 设置包含字符串的内容 
    .toString() // 传出其包含的字符串内容

    
```
