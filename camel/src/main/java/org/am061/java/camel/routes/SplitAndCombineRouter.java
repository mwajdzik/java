package org.am061.java.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.am061.java.camel.model.JobDefinition;
import org.am061.java.camel.services.JobSplitterAggregator;
import org.am061.java.camel.services.JobSplitterIteratorFactory;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.apache.camel.LoggingLevel.ERROR;
import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.LoggingLevel.WARN;

@Slf4j
@Component
class SplitAndCombineRouter extends RouteBuilder {

    private JobSplitterIteratorFactory jobSplitterIteratorFactory = new JobSplitterIteratorFactory();

    @Override
    public void configure() {
        onException(RuntimeException.class)
                .handled(true)
                .log(ERROR, "ERROR action goes here")
                .end();

        from("direct:test-input")
                .log(WARN, "Starting...")
                .split()
                    .method(jobSplitterIteratorFactory)
                    .aggregationStrategy(new JobSplitterAggregator())
                    .stopOnException().streaming()
                    .process(exchange -> {
                        LocalDate date = exchange.getIn().getBody(JobDefinition.class).getStartDate();
                        log.info("Processing {}", date);

                        if (date.toString().equals("2018-01-04")) {
                            throw new RuntimeException("Error thrown on " + date);
                        }
                    })
                .end()
                .log(INFO, "SHOULD BE CALLED ONCE AT THE END")
                .end();
    }
}