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
   // 获取值
   String lang = node.get("user").get("lang").asText();
   ```

3. String.toLowerCase()