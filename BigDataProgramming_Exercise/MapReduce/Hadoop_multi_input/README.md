# Mistake
0. Remember to check out the "value.toString.isEmpty()"
1. Remember to CHECK OUT whether the "key" or "value" is null!!! 
2. if value: "companyname alibaba"
3. input:
companyname	addressid
xiaomi	1
baidu	1
xiecheng	2
alibaba	3	

addressid	addressname
1	beijing
2	shanghai
3	hangzhou

if this is changed to:
companyname	addressid
xiaomi	1
baidu	1
xiecheng	2
alibaba	3	

addressid	addressname
1	beijing
2	shanghai
	hangzhou

then the output will be:
companyname	addressname
baidu	beijing
xiaomi	beijing
xiecheng	shanghai
alibaba	

and the code for testing will get "java.lang.ArrayIndexOutOfBoundsException: 1"!!
4. what a pity!!!  :(
