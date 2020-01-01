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
      - JoinBolt jbolt = new JoinBolt("spoult1ID","join_on_key1")
        .join("spout2ID","join_on_key2","spout1ID)
        .select("selected_id1, selected_id2, ...")
        .withThumblingWindow(new BaseWindowBolt.Duration(2, TimeUnit.SECONDS));
## 3.slide_count_window
  - key: letter
  - value: ArrayList<String> 
    - 0: 输入的value
    - 1: count
    - 2: #window_num
  - 不同的key有一个窗口序列, 比如:a有窗口[1,2,3], b有[1,2,3,4,5]
  - 某个key被emit给printerbolt后:
    - 在hashmap中对应的(key,value)中的value要被截断剩下最后一个字符[因为是处理两个数据之后就能输出最近三次的数据,3-2=1]
    - 该key对应的窗口数量要增加1, 对应的count值要为0(等到下一个key相同的pair输入,count将加一)
## 4. topn
  - 重要的类: 
    - hashtags: 返回所有的topic
    - ValueComparator : 构造比较的规则
    - private class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return (o2.getValue() - o1.getValue());
        }
    }
