package com;

import com.util.MsgThread;
import com.ws.RequestMappingHandlerConfig;
import com.ws.WSDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

/**
 * Created by lb on 2018/1/2.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class AppStarter {

    @Autowired
    private RequestMappingHandlerConfig requestMappingHandlerConfig;

    public static void main(String[] args) {
        SpringApplication springApplication =new SpringApplication(AppStarter.class);
//        springApplication.addListeners(new WSDispatcher());
        springApplication.run(args);

        System.out.println("============>" + "启动消息线程...");
        new Thread(new MsgThread()).start();
    }

    @PostConstruct
    public void detectHandlerMethods(){
        WSDispatcher.init(requestMappingHandlerConfig.requestMappingHandlerMapping());
        System.out.println(WSDispatcher.webSocketMapping.size());
    }


}
