package com.ws;

import com.util.SpringContextUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class WSDispatcher  {

	
	public static Map<Object, HandlerMethod> webSocketMapping = new HashMap<Object, HandlerMethod>();



	public static void init(RequestMappingHandlerMapping requestMappingHandlerMapping){
		webSocketMapping.clear();
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
			RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            //无WSController注解的过滤掉
            if(method.getBeanType().getDeclaredAnnotationsByType(WSController.class).length == 0)
            	continue;
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            if(patterns.size()>0){
            	System.out.println(patterns.toArray()[0]);
            	webSocketMapping.put(patterns.toArray()[0], method);
            }
		}
	}

	

	/*
	 * 接口分发
	 * 注意：参数只匹配第一项
	 */
	public static Object dispatch(String url, Object parameter, WebSocketSession session){

		HandlerMethod method = webSocketMapping.get(url);
		if(method != null){
			try{
				Class<?> cls = method.getMethod().getDeclaringClass();
				
				Object controllerObj = SpringContextUtil.getBean(cls);
				
				Object[] args = new Object[method.getMethod().getParameterCount()];
				Class<?>[] argTypes = method.getMethod().getParameterTypes();
				for(int i=0; i<args.length; i++){
					if(argTypes[i].equals(Map.class))	args[i] = parameter;
					else if(argTypes[i].equals(WebSocketSession.class))	args[i] = session;
				}
				if(args.length == 0)
					return method.getMethod().invoke(controllerObj);
				else
					return method.getMethod().invoke(controllerObj, args);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	/*
	 * 接口分发
	 * 注意：json参数必须包含url和params
	 */
	public static JSONObject dispatch(JSONObject json, WebSocketSession session){
		Object response = dispatch(String.valueOf(json.get("url")), json.get("params"), session);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", response);
		return jsonObj;
	}
}
