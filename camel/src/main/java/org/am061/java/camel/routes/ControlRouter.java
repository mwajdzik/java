package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ControlRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("file:///tmp/camel2")
                .log(LoggingLevel.ERROR, "START")
                .split(body().tokenize())
                /**/.process(new MyProcessor())
                /**/.bean(new MyBean())
                /**/.pipeline("direct:pipeline_1", "direct:pipeline_2")
                /**/.multicast()
                /*----*/.parallelProcessing()
                /*----*/.to("direct:multicast_1", "direct:multicast_2")             // the order can change (parallel)
                /**/.end()
                /**/.choice()                                                       // Content Based Router
                /*----*/.when(e -> e.getIn().getBody(Integer.class) < 25)
                /*--------*/.to("direct:a")
                /*----*/.when(e -> e.getIn().getBody(Integer.class) < 100)
                /*-------*/.to("direct:b")
                /*----*/.otherwise()
                /*--------*/.to("direct:c")
                /*----*/.end()
                /**/.process(new MyProcessor())
                .end()
                .log(LoggingLevel.ERROR, "DONE");

        from("direct:a")
                .log("Less than 25");

        from("direct:b")
                .log("Less than 100");

        from("direct:c")
                .log("More than 100");

        from("direct:multicast_1")
                .log("From multicast_1");

        from("direct:multicast_2")
                .log("From multicast_2");

        from("direct:pipeline_1")
                .log("From pipeline_1");

        from("direct:pipeline_2")
                .log("From pipeline_2");
    }

    public static class MyProcessor implements Processor {

        @Override
        public void process(Exchange exchange) {
            log.info("Value: {}", exchange.getIn().getBody(Integer.class));
        }
    }

    public static class MyBean {

        @Handler
        public Integer handler(Integer value) {
            return value * value;
        }
    }
}
