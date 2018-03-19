package org.am061.java.camel.routes

import org.am061.java.camel.model.JobDefinition
import org.apache.camel.impl.DefaultCamelContext
import spock.lang.Specification

import static java.time.LocalDate.of

class SplitAggregateRouterSpec extends Specification {

    def context, producer

    def setup() {
        context = new DefaultCamelContext()
        context.addRoutes(new SplitAndCombineRouter())
        context.start()

        producer = context.createProducerTemplate()
    }

    def cleanup() {
        context.stop()
    }

    def 'test -- split and aggregation'() {
        given:
        def jobDef = JobDefinition.builder()
                .startDate(of(2018, 1, 1))
                .endDate(of(2018, 1, 5))
                .build()

        when:
        producer.sendBody('direct:test-input', jobDef)

        then:
        true
    }
}
