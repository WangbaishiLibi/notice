package com.util;

import com.model.MsgNotice;
import com.ws.WSServer;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lb on 2018/2/27.
 */
public class MsgThread implements Runnable{

    Jedis redis = RedisClient.instance().getJedis();
    WSServer wsServer = WSServer.instance();

    public static final String RKey = "notice";

    //redis 通知
    private List<MsgNotice> sendNotice(){
        List<MsgNotice> notices = new ArrayList<MsgNotice>();
        Long len = redis.llen(RKey);
        MsgNotice notice;

        for(String id : wsServer.getClientPool().keySet()){
            if(wsServer.getClientPool().get(id).size() > 0 && redis.exists((RKey+id).getBytes())){
                notices.clear();
                List<byte[]> bytes = redis.lrange((RKey+id).getBytes(), 0, -1);
                redis.del((RKey + id).getBytes());
                for(byte[] tmp : bytes){
                    notice = (MsgNotice) SerializeUtil.unserialize(tmp);
                    if(notice != null){
                        notices.add(notice);
                    }
                }

                try {
                    WSServer.instance().sendToOne(notices, "mul-msg", id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return notices;
    }



    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(3000);

                sendNotice();
            //    WSServer.instance().broadcast("we are good kids.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
