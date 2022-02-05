package com.example.interceptor;

import com.example.model.WebSocketUser;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * @author Javen
 * @date 2022/2/5
 */
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public boolean preReceive(MessageChannel channel) {
        System.out.println("This is preReceive " + channel);
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("This is preSend " + message + channel);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // some pre logic
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            System.out.println(accessor.getMessageId());
//             这里可以设置stomp header中的simpleUser
//            accessor.setUser(() -> "Gallen");
            accessor.setUser(new WebSocketUser("Gallen", "Beautiful"));
        }
//        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
////            System.out.println(accessor.getMessageId());
//            // 这里可以设置stomp header中的simpleUser
//            accessor.setUser(() -> "Gallen");
//        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
