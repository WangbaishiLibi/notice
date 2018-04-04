package com.wscontroller;


import com.ws.WSController;
import com.ws.WSServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;


@Component
@WSController()
@RequestMapping("/msg")
public class MessageController {


	@RequestMapping("/test")
	public String countTicket(){
		System.out.println("msg----------");
		return "wonderful";
	}
	
	

	
	
	@RequestMapping("/hello")

	public String hello(Map map, WebSocketSession session){
		System.out.println(session.getId());
		System.out.println("msg-----hello---------" + map);
		return "from hello";
	}
	
	
	@RequestMapping("/login")
	public String login(Map map, WebSocketSession session){
		System.out.println("msg-----login---------" + map);
//		WSServer.instance().connect(map.get("uid"), session);
		return "from login";
	}
	
	/*
	 * 广播给所有用户
	 */
	@RequestMapping("/broadcast")
	public void broadcast(){
		System.out.println("msg-----broadcast---------");
		try {
			WSServer.instance().broadcast(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
