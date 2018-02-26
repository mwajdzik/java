package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.am061.java.camel.services.SimpleInputService;
import org.am061.java.camel.services.SimpleOutputService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
class SimpleRouter extends RouteBuilder {

    @Resource
    private SimpleInputService simpleInputService;

    @Resource
    private SimpleOutputService simpleOutputService;

    @Override
    public void configure() {
        from("direct:test-input")
                .log("Received message on test-input")
                .bean(simpleInputService)
                .choice()
                .when(header("SEND_OUT").isNotNull())
                .log("Message is valid and will be sent to direct:test-output")
                .to("direct:test-output")
                .endChoice();

        from("direct:test-output")
                .log("Received message on test-output")
                .bean(simpleOutputService)
                .to("log:out");
    }
}