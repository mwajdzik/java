package org.am061.java.camel.routes

import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.impl.DefaultCamelContext
import spock.lang.Specification

class DirectSedaRouterTest extends Specification {

    def context, producer

    def setup() {
        def router = new ControlRouter()
        router.setFromUri('direct:start')
        router.setToUri('mock:end')

        context = new DefaultCamelContext()
        context.addRoutes(router)
        context.start()

        producer = context.createProducerTemplate()
    }

    def cleanup() {
        context.stop()
    }

    def 'test -- split and aggregation'() {
        given:
        def mockTestOutputEndpoint = MockEndpoint.resolve(context, 'mock:end')
        mockTestOutputEndpoint.expectedCount = 1

        when:
        producer.sendBody('direct:start', '1\n2\n3')

        then:
        mockTestOutputEndpoint.assertIsSatisfied()
    }
}
