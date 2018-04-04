package com.ws;


import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.regex.Pattern;

@Component
public class TWSHandler extends TextWebSocketHandler {

	private final String charset = "UTF-8";

	

	@Override
	public void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		String msg = message.getPayload();
		System.out.println("handlerText===========>" + msg);
		JSONObject result = null;

		Object stamp = null;	//时间戳，用来标识返回结果
		Pattern pattern = Pattern.compile("^\\{(\"\\w+\":\\S+,{0,1})+\\}$");
		if(pattern.matcher(msg).matches()){
			JSONObject json = JSONObject.fromObject(message.getPayload());
			stamp = json.get("stamp");
			result = WSDispatcher.dispatch(json, session);
		}
		
		String response = "";
		if(result == null)	response = "404";
		else{
			result.put("stamp", stamp);
			response = String.valueOf(result);
		}
		session.sendMessage(new TextMessage(response.getBytes(charset)));

	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
		WSServer.instance().disconnect(session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		WSServer.instance().disconnect(session);
	}
	
}
