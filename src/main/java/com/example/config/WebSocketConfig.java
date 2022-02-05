package com.example.config;

import com.example.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author Javen
 * @date 2022/2/3
 */
@Configuration
//注解开启使用STOMP协议来传输基于代理(message broker)的消息,这时控制器支持使用@MessageMapping,就像使用@RequestMapping一样
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    //配置消息代理(Message Broker)
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 代表 broker的前缀
        // 点对点应配置一个/queue消息代理，
        // 广播式应配置一个/topic消息代理, 群发，单独聊天
        config.enableSimpleBroker("/topic", "/queue", "/mass", "/alone");
        // 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        config.setUserDestinationPrefix("/user");
        // client发送请求的前缀如果有/app，会定向到 @MessageMapping和 @SubscribeMapping，这种情况不会定向到broker
        config.setApplicationDestinationPrefixes("/app");
//        config.enableStompBrokerRelay("xxx"); // Use External Broker
    }

    @Override
    //注册STOMP协议的节点(endpoint),并映射指定的url
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket", "/myBroker").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
//        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(webSocketInterceptor);
    }
}
