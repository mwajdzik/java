package org.am061.java.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class ArrayListAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        List<Integer> list = oldExchange != null ?
                oldExchange.getIn().getBody(ArrayList.class) :
                new ArrayList<>();

        list.add(newExchange.getIn().getBody(Integer.class));
        newExchange.getIn().setBody(list);
        return newExchange;
    }
}