# Distributed_System
- This is the compulsory course homework for ECNUers in Dase
- All codes are achieved on Ubuntu:
  - Ubuntu version:
    ```shell
        No LSB modules are available.
        Distributor ID:	Ubuntu
        Description:	Ubuntu 18.04.3 LTS
        Release:	18.04
        Codename:	bionic
    ```
  - kernel version:
    ```shell
        5.3.0-28-generic
    ```
## Exercise
**This is the programming exercise**
1. Environment: 
   - jdk-8u221-linux-x64
        ```shell
            java version "1.8.0_221"
            Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
            Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode)
        ```
   - IDEA: ideaIC-2019.2.2-no-jbr
2. File tree introduction
   ```shell
    ├─BigDataProgramming_Exercise
    │  ├─flink 
    │  │  ├─k_means # A assignment project
    │  │  │  ├─out
    │  │  │  │  └─artifacts # A packaged jar
    │  │  │  │      └─k_means_syx
    │  │  │  ├─src
    │  │  │  │  ├─main
    │  │  │  │  │  └─java
    │  │  │  │  │      └─DSPPCode
    │  │  │  │  │          └─flink
    │  │  │  │  │              └─k_means # programming code written here
    │  │  │  │  │                  └─util # existing utility classes  
    │  │  │  │  └─test
    │  │  │  │      ├─java
    │  │  │  │      │  └─DSPPTest
    │  │  │  │      │      ├─student
    │  │  │  │      │      │  └─flink
    │  │  │  │      │      │      └─k_means # test code
    │  │  │  │      │      └─util
    │  │  │  │      │          └─Parser
    │  │  │  │      └─resources
    │  │  │  │          └─student
    │  │  │  │              └─flink
    │  │  │  │                  └─k_means
    │  │  │  └─target
    │  │  │      ├─classes
    │  │  │      │  ├─DSPPCode
    │  │  │      │  │  └─flink
    │  │  │      │  │      └─k_means
    │  │  │      │  │          └─util
    │  │  │      │  └─META-INF
    │  │  │      └─test-classes
    │  │  │          ├─DSPPTest
    │  │  │          │  ├─student
    │  │  │          │  │  └─flink
    │  │  │          │  │      └─k_means
    │  │  │          │  └─util
    │  │  │          │      └─Parser
    │  │  │          ├─META-INF
    │  │  │          └─student
    │  │  │              └─flink
    │  │  │                  └─k_means
   ```
3. Curriculumn web homepage
    ### 大师编程 (DaSE Programming) 训练平台

    ![](./logo.jpg)

    #### 1. 登录内网 VPN

    - 参见大夏学堂实验课程部分

    - 访问 http://10.11.1.208:8080

    #### 2. 注册 DIMA Evaluation Tool, 在 register 中填写信息

    - First Name/Last Name: 请使用汉语拼音 

    - University: 选 other, 填 ECNU

    - Semester: 5

    - Matriculation Number: 序号, 不足六位请在前面补 0 (例如, 序号为8的同学, 此处填写 000008;
    序号为 18 的同学, 此处填写 000018; 依此类推)

    #### 3. 试题

    - 提交入口 DIMA Evaluation Tool → Distributed System Paradigm and Programming (Exercise) → Assignments (密码: dspp2019)

    - 代码入口: 使用 SSH clone (如未设置 SSH key, 请先完成 [Gitlab SSH key 设置](./SSH.md)) 或使用 HTTPS clone. 命令为 `git clone URL` (其中 `URL` 见仓库右上角 clone)

    - 进入代码目录 `cd bigdataprogramming_exercise`

    - 切换代码分支 `git checkout XXX`, 其中 `XXX` 代表分支名称 (如 hadoop_k_means 等) (**切换之前请参考最后一条保存当前分支进度**)

    - 获取新题: 题目会陆续上线, 使用 `sh pull_new_assignment.sh` 获取新题分支 (**获取之前请参考最后一条保存当前分支进度**)

    - 在做题过程中若要切换分支, 使用 `git add . ; git commit -m "v"` 保存当前分支进度

    #### 4. 编码环境设置

    - 在 Intellij IDEA 中导入 Maven 工程

    #### 5. 编码及调试

    - 根据说明补充相应代码

    - 运行 junit test 中的测试代码

    以 hadoop_k_means 为例. 在 Intellij IDEA 中打开 `src/test/java/DSPPTest/student/hadoop/k_means/KMeansTest.java`

    按 Ctrl + Shift + F10 运行, 或右键选择 Run 'KMeansTest.java'

    - 查看测试是否通过, 并根据需要进行并调试

    #### 6. 调试并提交

    - 根据说明补充相应代码

    - 本地测试通过后通过 Intellij IDEA 导出 JAR 包

    - 选择 File → Project Structure → Artifacts → + JAR → Empty, 点击 Output Layout 下的加号, 选择 Module Output 和 Module Sources

    - 选择 Build → Build Artifacts, 在弹框中选择新建的 artifacts 进行 rebuild, JAR将包生成在 out 目录下或 classes 目录下

    - 在 DIMA Evaluation Tool 中的 Submit test solution 处提交 JAR 包
## Hands_on
**This is the practice of curriculumn theory**
1. Environment: concrete for different assignment
2. Note:
   - `3-6周作业`和`7-9周作业`中的`README.md`文件内, 有一部分图片缺失, 但是`.pdf`文件内容是完整的
