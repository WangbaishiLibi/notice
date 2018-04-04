package com.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import redis.clients.jedis.*;

@Component
public class RedisClient {
	
	private JedisPool jedisPool;				//非切片连接池
    private ShardedJedisPool shardedJedisPool;	//切片连接池
	
	private final String redisServer = "192.168.10.88";
    private final int redisPort = 6380;

    private static RedisClient redisClient = new RedisClient();

    public RedisClient() {
		// TODO Auto-generated constructor stub
    	init();
	}

    public static RedisClient instance(){
        return redisClient;
    }

    public void init(){
    	initialPool();
    	initialShardedPool();
    }
    
    
    /**
     * 初始化非切片池
     */
    private void initialPool() { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(20); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000l); 
        config.setTestOnBorrow(false); 

        jedisPool = new JedisPool(config, redisServer, redisPort);
    }

    /** 
     * 初始化切片池 
     */ 
    private void initialShardedPool() { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(20); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo(redisServer, redisPort, "master")); 
        // 构造池 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    public void Close() {     
        jedisPool.close();
        shardedJedisPool.close();
    } 
    
    public Jedis getJedis() {
        return jedisPool.getResource(); 
    }

    public ShardedJedis getShardedJedis() {
        return  shardedJedisPool.getResource();
    }
}
