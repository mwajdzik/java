package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.SPLIT_COMPLETE;

@Slf4j
@Component
class SplitAggregateRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("file:///tmp/camel1")
                .onException(IllegalArgumentException.class)
                /**/.handled(true)
                .end()

                .log(LoggingLevel.ERROR, "START")
                .split(body().tokenize())
                /**/.streaming()                                                        // important for huge files (lower memory consumption)
                /**/.parallelProcessing()
                /**/.timeout(10)
                /**/.aggregationStrategy(new MyAggregation())
                /**/.filter(exchange -> exchange.getIn().getBody(Integer.class) > 5)
                /*-----*/.process(new MyProcessor())
                /**/.end()
                .end()
                .log(LoggingLevel.ERROR, "DONE");
    }

    static class MyProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            Integer value = exchange.getIn().getBody(Integer.class);
            Thread thread = Thread.currentThread();
            String name = thread.getName();

            log.info("[{}] IN: {}", name, value);

            if (value % 5 == 0) {
                log.info("[{}] EX: {}", name, value);
                throw new IllegalArgumentException();
            }

            // Parallel processing timed out after 10 millis for number 18. This task will be cancelled and will not be aggregated.
            if (value % 7 == 0) {
                log.warn("[{}] TIMING OUT: {}", name, value);
                Thread.sleep(100);
            }

            log.info("[{}] OUT: {}", name, value);
        }
    }

    static class MyAggregation implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Integer item = newExchange.getIn().getBody(Integer.class);
            Thread thread = Thread.currentThread();
            String name = thread.getName();

            if (newExchange.getProperty(SPLIT_COMPLETE, Boolean.class)) {
                log.error("[{}] Agg: {} - SPLIT_COMPLETE", name, item);
            }

            if (newExchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
                log.warn("[{}] Agg: {} - skipping because of the exception thrown", name, item);
                return oldExchange;
            }

            if (!newExchange.getProperty(Exchange.FILTER_MATCHED, boolean.class)) {
                log.warn("[{}] Agg: {} - skipping because of the filter", name, item);
                return oldExchange;
            }

            log.info("[{}] Agg: {} - processing", name, item);
            return null;
        }
    }
}
