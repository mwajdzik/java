package org.am061.java.camel.services;

import org.springframework.stereotype.Component;

@Component
public class SimpleInputService {

    public String performSimpleStringTask(String str) {
        return str + str;
    }
}
