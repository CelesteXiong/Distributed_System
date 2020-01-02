## 背景

1. Collector c

   ```java
   c.collect()
   ```

2. ValueState\<T\> count 

   - access the state value `count.value()`
   - update the state value `count.update(newCount)`
   - clear the state `count.clear()`, after clear, it becomes `null` or the `default value` in the class `Configuration`

## 重载

1. map

   ```java
   .map(new MapFucntion<TYPEOUT, TYPEIN>() {
   @Override
   public TYPEOUT map(TYPEIN Parameter){
    Transformation;
   }
   })
   ```

## 工具

1. join

   ```
   .join().where().equalto()
   ```

2. jsonParser: 

   ```java
   // 声明
   jsonparser = new ObjectMapper();
   // 解析json
   JsonNode node = jsonParser.readTree(value);
   .readValue()
   // 判断是否有关键字"field"
   .has("field");
   // 获取值
   .get("field"); //获取有关键字"field"的jsonnode
   String lang = node.get("user").get("lang").asText();//遇到叶子节点时用.asText()获取字符
   ```

3. String.toLowerCase()