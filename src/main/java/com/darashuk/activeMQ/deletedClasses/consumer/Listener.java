//package com.darashuk.activeMQ.consumer;
//
//import com.darashuk.activeMQ.entity.Click;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.TextMessage;
//
//
//@Component
//@Slf4j
//public class Listener {
//
//    @Autowired
//    private JmsTemplate jmsTemplate;
//
////    @JmsListener(destination = "music_player_queue")
////    public void createConsumer(String message) {
////        System.out.println("Receved message " + message);
////    }
//
////    @JmsListener(destination = "music_player_queue")
////    public void createConsumerJson(Click click){
////        log.info(click+" receved");
////    }
//
//    @JmsListener(destination = "music_player_queue", containerFactory="jsaFactory")
//    public void receive(Click click)  {
//        log.info("Application : object received : {}",click.toString());
//    }
//}
//
