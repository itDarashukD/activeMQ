package com.darashuk.activeMQ.controller;

import com.darashuk.activeMQ.entity.Click;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;

@RestController
@RequestMapping("/rest/publish")
@Slf4j
public class ActiveMqController {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue queue;


    @GetMapping(value = "/{message}", produces = "text/html")
    public String publish(@PathVariable("message") final String message) {

        Click click = new Click( );
        click.setCount(11);
        click.setName(message);
        jmsTemplate.convertAndSend(queue, click);
        return "send is Ok!" + message;
    }
}
