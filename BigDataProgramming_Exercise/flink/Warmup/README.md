# Capacity Monitor

## 待完成

  - 请在 DSPPCode.flink.watermark 中创建 TimestampWithWatermarkAssignerImpl, 
    继承 TimestampWithWatermarkAssigner, 实现虚函数

## 题目描述

  - 背景: 使用滑动窗口统计输入数据的总和

  - 需求: 自定义水位线, 要求水位线始终比当前接收的最晚时间戳早一个给定的时间跨度 t

    如: 当前最晚时间戳为 1443000, 则水位线为 1443000 - t
