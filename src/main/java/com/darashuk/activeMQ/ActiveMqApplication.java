package com.darashuk.activeMQ;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ActiveMqApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ActiveMqApplication.class).web(WebApplicationType.NONE).run(args);
    }
}