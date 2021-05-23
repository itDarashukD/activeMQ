package com.darashuk.activeMQ.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

    @JmsListener(destination = "Standalone.queue")
    public void createConsumer(String message) {
        System.out.println("Receved message " + message);
    }
}
