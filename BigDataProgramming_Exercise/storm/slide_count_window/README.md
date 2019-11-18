# SlideCountWindow

## 待完成:

* 请在DSPPCode.storm.slide_count_window中创建SlideCountWindowBoltImpl, 继承SlideCountWindowBolt, 实现抽象方法.

## 题目描述:

* 实现一个简单的SlideCountWindow，每接收两个数据就对最近三条数据进行append计算
  
  例如： 
  
  接收数据
   
  - x 1 
  - x 2 
  - x 3
  - x 4
  - y 1
  - y 2
  - y 3
  
  对应输出为
  
  - x 12
  - x 234   
  - y 12
  
 注意：不同的key值要分开处理