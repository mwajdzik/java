package org.am061.java.camel.services;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

    @Handler
    public void handle() {
        System.out.println("Error handled...");
    }
}
