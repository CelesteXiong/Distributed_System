# PageRank
## 待完成
- 请在 DSPPCode.giraph.page_rank 中创建 PageRankImpl, 继承 PageRank, 实现抽象方法。

## 题目描述
输入中给出图的相关数据，要求完成 PageRank 的计算过程，PageRank 的计算公式为


<a href="https://www.codecogs.com/eqnedit.php?latex=PR(A)&space;=&space;(PR(B)/L(B)&space;&plus;&space;PR(C)/L(C)&space;&plus;&space;PR(D)/L(D)&space;&plus;&space;...)*q&space;&plus;&space;(1&space;-&space;q)/n" target="_blank"><img src="https://latex.codecogs.com/gif.latex?PR(A)&space;=&space;(PR(B)/L(B)&space;&plus;&space;PR(C)/L(C)&space;&plus;&space;PR(D)/L(D)&space;&plus;&space;...)*q&space;&plus;&space;(1&space;-&space;q)/n" title="PR(A) = (PR(B)/L(B) + PR(C)/L(C) + PR(D)/L(D) + ...)*q + (1 - q)/n" /></a>

N为网页总数 其中 q 为阻尼系数，**已在抽象类中给定**，PR 值为顶点Rank值，L(A) 为顶点A的出边个数

## 样例

下面将依次给出图的输入格式以及结果输出格式：

- 输入格式

    ```
    [0 ,1 ,[[1 ,0] ,[2 ,0]]]
    [1 ,1 ,[[2 ,0]]]
    [2 ,1 ,[[0 ,0]]]
    ```
    
    第一列表示顶点 ID，第二列表示顶点初始的 PR 值，[[1,0],[2,0]] 表示顶点 0 与 顶点 1 和 顶点 2 存在边，边的值为 0
    
- 输出格式

    ```
    0,0.3590723673502604
    1,0.25645923614501953
    2,0.38471253712972
    
    ```
    
    其中第一列输出顶点 ID，第二列输出迭代一定次数之后的 rank 值，**迭代次数已在抽象类中给定**

