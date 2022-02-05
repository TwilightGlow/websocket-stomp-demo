package com.example.controller;

import com.example.model.Greeting;
import com.example.model.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

/**
 * @author Javen
 * @date 2022/2/3
 */
@Slf4j
@Controller
// 客户端有两种模式
// 1. send，会mapping到服务端的 @MessageMapping
// 2. subscribe，会mapping到服务端的 @SubscribeMapping
// @MessageMapping表示指定目标，而 @SubscribeMapping仅表示对订阅消息的兴趣。
// @MessageMapping的方法的返回值被序列化后，会发送到“brokerChannel”
// @SubscribeMapping的方法的返回值被序列化后，会发送到“clientOutboundChannel”，直接回复到客户端，而不是通过代理进行广播。
// 这对于实现一次性的、请求-应答消息交换非常有用，并且从不占用订阅。这种模式的常见场景是当数据必须加载和呈现时应用程序初始化。
// @SendTo注释@SubscribeMapping方法，在这种情况下，返回值被发送到带有显式指定目标目的地的“brokerChannel”。
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello") // @MessageMapping仅用于响应客户端SEND方法，会匹配/app前缀，客户端一次订阅多次发送
    @SendTo("/topic/greetings") // 仅对订阅了/topic/greetings的客户生效
    // @SendTo注解的作用是广播给订阅了/topic/greeting的所有subscribers
    // 这里返回值不是返回给客户端的，而是转发给消息代理的
    public Greeting greeting(Principal principal, @Payload HelloMessage message) throws Exception {
        Thread.sleep(2000); // simulated delay
        log.info("客户端Send");
        log.info(principal.getName());
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    // @SubscribeMapping仅用于响应客户端SUBSCRIBE方法，会匹配/app前缀，客户端订阅某一个目的地，然后预期在这个目的地上获得一个一次性的响应
    // 这种请求-回应模式与HTTP GET的请求-响应模式的关键区别在于HTTP GET请求是同步的，而订阅的请求-回应模式则是异步的
    @SubscribeMapping("/hello")
    public String subscribe() throws Exception {
        Thread.sleep(2000);
        log.info("客户端Subscribe");
        return "你好";
    }

    // 会匹配客户端send '/app/topic/greetings'
    @SubscribeMapping("/topic/greetings")
    public void subscribe123() throws Exception {
        log.info("有人订阅");
    }

    //广播推送消息
    @Scheduled(fixedRate = 10000)
    public void sendTopicMessage() {
//        WebSocketStompClient webSocketStompClient = new WebSocketStompClient();
        System.out.println("后台广播推送！");
        Greeting broadcast = new Greeting("这是一条广播");
        // 广播给所有订阅了/topic/greetings的clients
        simpMessagingTemplate.convertAndSend("/topic/greetings", broadcast);
    }

    //一对一推送消息
    // 默认发送到订阅了/user/{userId}/queue/greeting的客户
    @Scheduled(fixedRate = 5000)
    public void sendQueueMessage() {
        System.out.println("后台一对一推送！");
        Greeting message1 = new Greeting("这是一条给一个Javen发的消息");
        Greeting message2 = new Greeting("这是一条给一个Gallen发的消息");
        simpMessagingTemplate.convertAndSendToUser("Javen", "/queue/greetings", message1);
        simpMessagingTemplate.convertAndSendToUser("Gallen", "/queue/greetings", message2);
    }

//    @SubscribeMapping("/user/1/greetings")
//    public void test() {
//        log.info("订阅了！！！！！！！！！！！！！！！！！！！！！");
//    }
}
