package com.wscontroller;

import com.ws.WSController;
import com.ws.WSServer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * Created by lb on 2018/2/27.
 */

@Component
@WSController()
@RequestMapping("/log")
public class LoginController {


    @RequestMapping("/login")
    public String login(Map map, WebSocketSession session){
        if(map.get("uid") == null)
            return "parameter without userId";

        WSServer.instance().connect(String.valueOf(map.get("uid")), session);
        return "login success!";
    }

}
