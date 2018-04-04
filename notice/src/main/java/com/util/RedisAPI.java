package com.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

public class RedisAPI {

	private Jedis redis;

	public RedisAPI(Jedis redis){
		this.redis = redis;
	}


	public void KeyApi() {
		System.out.println("==============key=========================");
		// 清空数据
		System.out.println("清空库中所有数据：" + redis.flushDB());
		;
		print("判断key111是否存在" + redis.exists("key111"));
		print("增加key111:" + redis.set("key111", "hello world"));
		print("输出key111:" + redis.get("key111"));
		print("增加新的key112：" + redis.set("key112", "key112value"));
		print("增加新的key113：" + redis.set("key113", "key113value"));
		// 输出所有key的的值 无序的
		for (String key : redis.keys("*")) {
			print(key + ":" + redis.get(key));
		}
		print("删除key111：" + redis.del("key111"));
		print("key111是否存在：" + redis.exists("key111"));
		// 设置key的过期时间
		print("设置key112的过期时间3秒" + redis.expire("key112", 3));
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("key112的剩余生存时间：" + redis.ttl("key112"));
		print("key112的取消过期时间：" + redis.persist("key112"));
		print("查看key112是否还存在：" + redis.exists("key112"));
		print("查看key112的value类型：" + redis.type("key112"));
		print("给key113改成key114：" + redis.rename("key113", "key114"));
		print("查看key114value：" + redis.get("key114"));
	}

	public void StringApi() {
		print("=====================string========================");
		print("清空数据：" + redis.flushDB());
		print("============单个增删改=============");
		print("增加key001:" + redis.set("key001", "key001value"));
		print("修改key001：" + redis.set("key001", "key-update") + " "
				+ redis.get("key001"));
		print("最后追加字符串：" + redis.append("key001", "-append") + " "
				+ redis.get("key001"));
		print("删除key001：" + redis.del("key001"));

		print("==================多个一起添加,删除==================");
		print("增减key021，key022，key023："
				+ redis.mset("key021", "key021value", "key022", "key022value",
						"key023", "key023value"));
		print("一次获取多个key值：" + redis.mget("key021", "key022", "key023"));
		print("一次删除多个key：" + redis.del(new String[] { "key021", "key022" }));
		print("获取key021：" + redis.get("key021"));

		print("===========other=======================");
		print("不在在key时再添加值：" + redis.setnx("key031", "key031value"));
		print("不在在key时再添加值：" + redis.setnx("key032", "key032value"));
		print("存在在key时再添加值：" + redis.setnx("key032", "key032value_tow"));
		print("key031:" + redis.get("key031"));
		print("key032值没变:" + redis.get("key032"));

		print("添加key033并设置过期时间：" + redis.setex("key033", 2, "key033value"));
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("获取key033值：" + redis.get("key033"));
		print("获取值得子串key032:" + redis.getrange("key032", 1, 5));
	}

	public void ListApi() {
		// list存储结构是栈类型的 最后插入的索引是0
		print("====================list======================");
		print("清空数据：" + redis.flushDB());
		print("===========增加=================");
		print("添加了字符串list");
		print("listString:" + redis.lpush("listString", "vector"));
		print("listString:" + redis.lpush("listString", "vector"));
		print("listString:" + redis.lpush("listString", "arraylist"));
		print("listString:" + redis.lpush("listString", "hashmap"));
		print("listNumber:" + redis.lpush("listNumber", "1"));
		print("listNumber:" + redis.lpush("listNumber", "4"));
		print("listNumber:" + redis.lpush("listNumber", "3"));
		print("listNumber:" + redis.lpush("listNumber", "2"));
		print("listString全部元素：" + redis.lrange("listString", 0, -1));
		print("listNumber全部元素：" + redis.lrange("listNumber", 0, -1));
		print("");
		print("============修改=================");
		print("对指定下标进行修改：" + redis.lset("listString", -1, "vector-update"));
		print("修改后的0下标值：" + redis.lindex("listString", 0));
		print("删除后的元素：" + redis.lrange("listString", 0, -1));
		print("");
		print("==============删除================");
		print("删除指定的元素，重复的删除后添加的：" + redis.lrem("listString", 1, "vector"));
		print("删除后的元素：" + redis.lrange("listString", 0, -1));
		print("");
		/*
		 * list中存字符串时必须指定参数为alpha，如果不使用SortingParams，而是直接使用sort("list")，
		 * 会出现"ERR One or more scores can't be converted into double"
		 */
		SortingParams sortingParameters = new SortingParams();
		sortingParameters.alpha();// 目测是根据字母顺序排序
		print("排序元素：" + redis.sort("listString", sortingParameters));
		print("排序元素：" + redis.sort("listNumber"));

	}

	public void SetApi() {
		print("=====================set=====================");
		print("清空数据：" + redis.flushDB());
		print("============增加==============");
		print("名为sets的集合中添加元素value1" + redis.sadd("sets", "value1"));
		print("名为sets的集合中添加元素value2" + redis.sadd("sets", "value2"));
		print("名为sets的集合中添加元素value3" + redis.sadd("sets", "value3"));
		print("sets所有值：" + redis.smembers("sets"));
		print("===============修改=============");
		print("无法修改指定的值但是,不能存入相同的值：" + redis.sadd("sets", "value2"));
		print("sets:" + redis.smembers("sets"));
		print("============查询==================");
		for (String value : redis.smembers("sets")) {
			print(value);
		}
		print("判断元素是否在集合中：" + redis.sismember("sets", "value2"));
		print("==============删除=============");
		print("删除指定的值：" + redis.srem("sets", "value2"));
		print("元素出栈：" + redis.spop("sets"));
		print("sets:" + redis.smembers("sets"));
		print("");
		print("==============集合运算=================");
		print("名为sets1的集合中添加元素value1" + redis.sadd("sets1", "value1"));
		print("名为sets1的集合中添加元素value2" + redis.sadd("sets1", "value2"));
		print("名为sets1的集合中添加元素value3" + redis.sadd("sets1", "value3"));
		print("名为sets2的集合中添加元素value3" + redis.sadd("sets2", "value3"));
		print("名为sets2的集合中添加元素value4" + redis.sadd("sets2", "value4"));
		print("名为sets2的集合中添加元素value5" + redis.sadd("sets2", "value5"));
		print("sets1所有值：" + redis.smembers("sets1"));
		print("sets2所有值：" + redis.smembers("sets2"));
		print("sets1和sets2的交集：" + redis.sinter("sets1", "sets2"));
		print("sets1和sets2的并集：" + redis.sunion("sets1", "sets2"));
		print("sets1和sets2的差集：" + redis.sdiff("sets1", "sets2"));

	}

	public void HashApi() {
		print("================hash=================");
		print("清空数据：" + redis.flushDB());
		print("==================增加================");
		print("hashs添加key001=>value001"
				+ redis.hset("hashs", "key001", "value001"));
		print("hashs添加key002=>value002"
				+ redis.hset("hashs", "key002", "value002"));
		print("hashs添加key003=>value003"
				+ redis.hset("hashs", "key003", "value003"));
		print("hashs添加key004=>4L" + redis.hincrBy("hashs", "key004", 4L));
		print("hashs:" + redis.hgetAll("hashs"));
		print("===================查询================");
		print("判断是否存在key001：" + redis.hexists("hashs", "key001"));
		print("查找key001的值：" + redis.hget("hashs", "key001"));
		print("获得所有集合的key：" + redis.hkeys("hashs"));
		print("获得所有集合的value：" + redis.hvals("hashs"));

		print("=============修改====================");
		print("修改key001值为value001_update:"
				+ redis.hset("hashs", "key001", "value001_update"));
		print("修改key004值增加11:" + redis.hincrBy("hashs", "key004", 11L));
		print("hashs:" + redis.hvals("hashs"));

		print("=============删除=======================");
		print("删除key002：" + redis.hdel("hashs", "key002"));
		print("hashs:" + redis.hgetAll("hashs"));

	}

	public void SortedSetApi() {
		// 有序集合类似
		print("=================SortedSet================");
		print("清空数据：" + redis.flushDB());
		print("==============增加===============");
		print("名为zsets添加数据value1：" + redis.zadd("zsets", 1, "value1"));
		print("名为zsets添加数据value2：" + redis.zadd("zsets", 2, "value2"));
		print("名为zsets添加数据value3：" + redis.zadd("zsets", 3, "value3"));
		print("名为zsets添加数据value4：" + redis.zadd("zsets", 4, "value4"));
		print("名为zsets添加数据value4：" + redis.zadd("zsets", 4, "value4"));
		print("zsets：" + redis.zrange("zsets", 0, -1));

		print("===============查询===============");
		print("查找元素的个数：" + redis.zcard("zsets"));
		print("查找权重范围内元素的个数：" + redis.zcount("zsets", 1, 3));
		print("查找元素value4的权重：" + redis.zscore("zsets", "value4"));

		print("=============修改===================");

		print("=============删除===================");
		print("删除value4:" + redis.zrem("zsets", "value4"));
		print("redis:" + redis.zrange("zsets", 0, -1));

	}

	public void print(Object obj) {
		System.out.println(obj);
	}

}