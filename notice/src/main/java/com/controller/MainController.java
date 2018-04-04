package com.controller;

import com.ws.WSServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lb on 2017/12/13.
 */
@RestController
public class MainController {


    @RequestMapping("/test")
    public Object test(){
        return "wonder";
    }

    @RequestMapping("/listClients")
    public Object  index(){
        String result = "";
        for(String key : WSServer.instance().getClientPool().keySet()){
            result += "userId:" + key + "   cnt:" + WSServer.instance().getClientPool().get(key).size() + "<br>";
        }
        return result;
    }

}
