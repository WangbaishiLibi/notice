package com;

import com.model.MsgNotice;
import com.util.RedisAPI;
import com.util.RedisClient;
import com.util.SerializeUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lb on 2018/2/27.
 */
public class MainTest {


    @Test
    public void test1(){
        List<MsgNotice> tmp1 = new ArrayList<MsgNotice>();
        MsgNotice notice1 = new MsgNotice();
        MsgNotice notice2 = new MsgNotice();
        notice1.setUserId(123);
        notice2.setUserId(345);
        tmp1.add(notice1);
        tmp1.add(notice2);

        JSONObject json = new JSONObject();
        json.element("key1", "123");
        json.element("key2", tmp1);
        System.out.println(json.toString());
    }

}
