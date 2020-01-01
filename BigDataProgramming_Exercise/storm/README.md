## 文件IO

1. BufferedWriter:

   ```java
   String outputfile;
   StringBuilder outputs;
   
   BufferedWriter bw = FileProcess.getWriter(outputfile);
   FileProcess.write(outputs.toString(),bw);
   FileProcess.close(bw);
   ```



## 工具

1. String.trim: 去除首尾空格
2. map: 

# Notes

## 0.运行流程

  - spout
  - bolt
  - 通信: collector.emit()

## 1.warm_up

  - 重要的类:
    - Map.Entry<String, Integer> entry
    - HashMap<>()
      - .put(key, value): 若插入重复的key, 则value会被覆盖
      - .remove(key)
  - 易错: 给单词初始化个数时为1, 不是0
  - 注意: CounterBolt表示流计算结束的词"stop", 遇到之后需要将结果emit给PrinterBolt.

## 2.window_join

  - 重要的类:

    - tuple

      - .getValues()

    - BufferedWriter bw = FileProcess.getWriter(outputFile);
      FileProcess.write(result, bw);

    - StormJoinBolt

      ```java
      JoinBolt jbolt = new JoinBolt("spoult1ID","join_on_key1")
      .join("spout2ID","join_on_key2","spout1ID)
      .select("selected_id1, selected_id2, ...")
      .withThumblingWindow(new BaseWindowBolt.Duration(2, TimeUnit.SECONDS));
      ```

      

## 3.slide_count_window

  - key: letter
  - value_list: ArrayList\<String\> 
    - 0: 输入的value
    - 1: count
    - 2: #window_num
  - 不同的key有一个窗口序列, 比如:a有窗口[1,2,3], b有[1,2,3,4,5]
  - 某个key被emit给printerbolt后:
    - 在hashmap中对应的(key,value_list)中的value_list的value要被截断剩下最后一个字符[因为是处理两个数据之后就能输出最近三次的数据,3-2=1]
    - 该key对应的窗口数量要增加1, 对应的count值要为0(等到下一个key相同的pair输入,count将加一)
- 补充另一种思路[yhm]: 
  - 不截断地往value list中增加value, 每当list.size()%2==0时, 触发一次emit();
  - 通过循环为emit()的value赋值, 索引从(value_list.length-3)开始, 如果长度小于3, 即索引为负, continue即可. 这一步代替了value_list中的count;

## 4. top_n

  - 重要的类: 

    - hashtags: 返回所有的topic

    - ValueComparator : 构造比较的规则

      ```java
      // 按照Entry中第二个元素, 从大到小的顺序排列Entry
      private class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
      @Override
      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
          return (o2.getValue() - o1.getValue());
      }
      }
      ```

    - Collection.sort(list, vc); //vc: ValueComparator
    - Map.Entry<type1, type2>;
    - Map.entrySet()
    - List<Map.Entry<t1, t2>> list = new ArrayList<>(Map.entrySet());
