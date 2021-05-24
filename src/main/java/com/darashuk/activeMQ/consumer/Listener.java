package com.darashuk.activeMQ.consumer;

import com.darashuk.activeMQ.entity.Click;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

//    @JmsListener(destination = "music_player_queue")
//    public void createConsumer(String message) {
//        System.out.println("Receved message " + message);
//    }

//    @JmsListener(destination = "music_player_queue")
//    public void createConsumerJson(Click click){
//        log.info(click+" receved");
//    }
}
