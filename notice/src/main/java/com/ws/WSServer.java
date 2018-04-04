package com.ws;

import net.sf.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WSServer {

	private final String charset = "UTF-8";
	private static WSServer server = new WSServer();
	
	private ConcurrentHashMap<String, WebSocketSession> wsClients = new ConcurrentHashMap();
	private ConcurrentHashMap <String, ArrayList<WebSocketSession>> clientPool = new ConcurrentHashMap();
	
	
	
	private WSServer(){

	}
	
	public static WSServer instance(){
		return server;
	}
	
	
	public WebSocketSession getSession(String id){
		WebSocketSession session = wsClients.get(id);
		if(session == null || !session.isOpen()){
			wsClients.remove(id);
			return null;
		}
		return wsClients.get(id);
	}
	
	public ArrayList<WebSocketSession> getSessionPool(String id){
		ArrayList<WebSocketSession> sessionPool = clientPool.get(id);
		if(sessionPool != null) {
			for(WebSocketSession session : sessionPool){
				if(!session.isOpen()){
					clientPool.remove(session);
				}
			}
		}
		return sessionPool;
	}
	
	/*
	 * 注入用户，一个用户对应一个session
	 * 以sessionid唯一标识
	 */
	public synchronized void connect(WebSocketSession session){
		if(wsClients.get(session.getId()) == null){
			wsClients.put(session.getId(), session);
		}
	}
	
	/*
	 * 注入用户，一个用户对应多个session
	 * 以参数id唯一标识
	 */
	public synchronized void connect(String id, WebSocketSession session){
		if(clientPool.get(id) == null){
			ArrayList<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
			sessions.add(session);
			clientPool.put(id, sessions);
		}else{
			ArrayList<WebSocketSession> sessions = clientPool.get(id);
			sessions.add(session);
		}
	}
	
	/*
	 * 注销单个session
	 */
	public synchronized void disconnect(WebSocketSession session){
		for(Object key : wsClients.keySet()){
			if(session.equals(wsClients.get(key))){
				wsClients.remove(key);
			}
		}
		
		for(Object key : clientPool.keySet()){
			ArrayList<WebSocketSession> sessionPool = clientPool.get(key);
			sessionPool.remove(session);
		}
	}
	
	/*
	 * 注销用户的所有session
	 * 
	 */
	public synchronized boolean disconnect(String id){
		return (wsClients.remove(id) != null || clientPool.remove(id) != null);
	}
	
	
	/*
	 * 广播，邮戳默认为broadcast
	 */
	public void broadcast(Object parameter) throws UnsupportedEncodingException, IOException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", parameter);
		jsonObj.put("stamp", "broadcast");
		sendToAll(jsonObj.toString());
	}
	
	public void broadcast(Object parameter, String stamp) throws UnsupportedEncodingException, IOException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", parameter);
		jsonObj.put("stamp", stamp);
		sendToAll(jsonObj.toString());
	}
	
	public void sendToOne(Object parameter, String stamp, WebSocketSession session) throws UnsupportedEncodingException, IOException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", parameter);
		jsonObj.put("stamp", stamp);
		session.sendMessage(new TextMessage(jsonObj.toString().getBytes(charset)));
	}

	public void sendToOne(Object parameter, String stamp, String id) throws UnsupportedEncodingException, IOException{

		WebSocketSession session = getSession(id);
		ArrayList<WebSocketSession> sessionPool = getSessionPool(id);
		if(session != null){
			sendToOne(parameter, stamp, session);
		}
		if(sessionPool != null){
			for(WebSocketSession tmp : sessionPool){
				sendToOne(parameter, stamp, tmp);
			}
		}
	}

	private void sendToAll(String response) throws UnsupportedEncodingException, IOException{
		for(WebSocketSession session : wsClients.values()){
			session.sendMessage(new TextMessage(response.getBytes(charset)));
		}
		
		for(ArrayList<WebSocketSession> sessions : clientPool.values()){
			for(WebSocketSession session : sessions){
				session.sendMessage(new TextMessage(response.getBytes(charset)));
			}
		}
	}


	public ConcurrentHashMap<String, WebSocketSession> getWsClients() {
		return wsClients;
	}


	public ConcurrentHashMap<String, ArrayList<WebSocketSession>> getClientPool() {
		return clientPool;
	}

}
