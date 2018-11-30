package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class DirectSedaRouter extends RouteBuilder {

    private static final String DIRECT_1 = "direct:1";
    private static final String DIRECT_2 = "direct:2";
    private static final String SEDA_3 = "seda:3?concurrentConsumers=10";

    @Override
    public void configure() {
        from("file:///tmp/camel3")
                .log(LoggingLevel.ERROR, "START")
                .split(body().tokenize()).parallelProcessing()
                /**/.to(DIRECT_1, DIRECT_2)
                /**/.to(SEDA_3)
                .end()
                .log(LoggingLevel.ERROR, "DONE");

        from(DIRECT_1)
                .log("direct:1 - " + body());

        from(DIRECT_2)
                .log("direct:2 - " + body());

        from(SEDA_3)
                .log("seda:3   - " + body());
    }
}
