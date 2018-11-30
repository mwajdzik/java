package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.am061.java.camel.util.ArrayListAggregationStrategy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ExternalAggregateRouter extends RouteBuilder {

    private static final String SEDA_SEND = "seda:send?" +
            "concurrentConsumers=10&" +
            "size=10&" +
            "blockWhenFull=true&" +
            "timeout=120000&" +
            "waitForTaskToComplete=Never";

    @Override
    public void configure() {
        from("file:///tmp/camel4")
                .log(LoggingLevel.ERROR, "START")
                .split(body().tokenize())
                /**/.streaming().parallelProcessing()
                /**/.log("BEFORE Agg: " + body())
                /**/.aggregate(constant(1), new ArrayListAggregationStrategy())
                /*    */.completionSize(5)
                /*    */.completionTimeout(1000)
                /*    */.log(LoggingLevel.WARN, "AFTER AGG:  " + body())
                /*    */.to(SEDA_SEND)
                /**/.end()
                /**/.log("DONE Ind:   " + body())
                .end()
                .log(LoggingLevel.ERROR, "DONE All");

        from(SEDA_SEND)
                .log(LoggingLevel.WARN, "SEDA:       " + body());
    }
}
