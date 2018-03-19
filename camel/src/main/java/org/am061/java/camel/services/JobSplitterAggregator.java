package org.am061.java.camel.services;

import lombok.extern.slf4j.Slf4j;
import org.am061.java.camel.model.JobDefinition;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.time.LocalDate;

@Slf4j
public class JobSplitterAggregator implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        LocalDate date = newExchange.getIn().getBody(JobDefinition.class).getStartDate();
        log.info("Aggregating " + date);
        return newExchange;
    }
}
