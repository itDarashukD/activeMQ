package com.darashuk.activeMQ.activeMQ;

import com.darashuk.activeMQ.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Listener {

    @Autowired
    private IListenerService listenerService;

    @JmsListener(destination = "music_player_queue", containerFactory = "jsaFactory")
    public void receive(Source source) {
        log.info("IN class Listener receive() : object received : {}", source.getName() + " " + source.getStorage_types());

        listenerService.getFileBySourceFromListener(source);
    }
}

