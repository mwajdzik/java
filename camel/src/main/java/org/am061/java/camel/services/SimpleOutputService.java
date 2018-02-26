package org.am061.java.camel.services;

import org.springframework.stereotype.Component;

@Component
public class SimpleOutputService {

    public String performSomeOtherSimpleStringTask(String str) {
        return str + str + str + str;
    }
}
