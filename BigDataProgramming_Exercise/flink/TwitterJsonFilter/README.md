# TwitterJsonFilter

## 待完成:

* 请在DSPPCode.flink.twitter_json_filter中创建ParseJsonAndSplitImpl, 继承ParseJsonAndSplit, 实现抽象方法
* 请在DSPPCode.flink.twitter_json_filter中创建FilterImpl, 继承Filter, 实现抽象方法
## 题目描述:

* 对源源不断到达的twitter数据进行解析，找到使用英文（"en"）的推特文本（"text"）中的高频词汇，大写字符(例如'A')要转化为小写字符（例如'a'）。
当某一个单词出现超过4次(包含4次)时保存这个单词，这个单词不应该是无意义的e.g."the"，即停词表（src/test/resources/student/flink/twitter_json_filter/stopWord）中的词汇不应该出现在保存的输出（output）文件中。

    每一条数据均以JSON的形式给出： "text" 表示Twitter的文本, "user"->"lang" 表示用户所使用的语言 
  
  ```json
  {
  	"created_at": "Mon Jan 1 00:00:00 +0000 1901",
  	"id": 0,
  	"id_str": "000000000000000000",
  	"text": "How to use flink? i love it",
  	"source": null,
  	"truncated": false,
  	"in_reply_to_status_id": null,
  	"in_reply_to_status_id_str": null,
  	"in_reply_to_user_id": null,
  	"in_reply_to_user_id_str": null,
  	"in_reply_to_screen_name": null,
  	"user": {
  		"id": 0,
  		"id_str": "0000000000",
  		"name": "test1",
  		"screen_name": "iphone",
  		"location": "Shanghai",
  		"protected": false,
  		"verified": false,
  		"followers_count": 999999,
  		"friends_count": 99999,
  		"listed_count": 999,
  		"favourites_count": 9999,
  		"statuses_count": 999,
  		"created_at": "Mon Jan 1 00:00:00 +0000 1901",
  		"utc_offset": 7200,
  		"time_zone": "Amsterdam",
  		"geo_enabled": false,
  		"lang": "en",
  		"entities": {
  			"hashtags": [{
  				"text": "example1",
  				"indices": [0, 0]
  			}, {
  				"text": "tweet1",
  				"indices": [0, 0]
  			}]
  		},
  		"contributors_enabled": false,
  		"is_translator": false,
  		"profile_background_color": "C6E2EE",
  		"profile_background_tile": false,
  		"profile_link_color": "1F98C7",
  		"profile_sidebar_border_color": "FFFFFF",
  		"profile_sidebar_fill_color": "252429",
  		"profile_text_color": "666666",
  		"profile_use_background_image": true,
  		"default_profile": false,
  		"default_profile_image": false,
  		"following": null,
  		"follow_request_sent": null,
  		"notifications": null
  	},
  	"geo": null,
  	"coordinates": null,
  	"place": null,
  	"contributors": null
  }
  ```
  
  