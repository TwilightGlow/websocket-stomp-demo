package com.example.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * @author Javen
 * @date 2022/2/4
 */
@Service
public class StompEventListener {

    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        String destination = (String) headers.get("simpDestination");
        Object simpUser = headers.get("simpUser");
        String sessionId = (String) headers.get("simpSessionId");
        System.out.println("监听订阅");
        System.out.println(destination);
        System.out.println(simpUser);
        System.out.println(sessionId);
        System.out.println(headers);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        String destination = (String) headers.get("simpDestination");
        System.out.println("监听断开");
        System.out.println(destination);
        System.out.println(headers);
    }
}
