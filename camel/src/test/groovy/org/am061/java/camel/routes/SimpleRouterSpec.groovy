package org.am061.java.camel.routes

import org.am061.java.camel.services.SimpleInputService
import org.am061.java.camel.services.SimpleOutputService
import org.apache.camel.builder.AdviceWithRouteBuilder
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.model.ModelCamelContext
import org.apache.camel.model.RouteDefinition
import spock.lang.Specification

class SimpleRouterSpec extends Specification {

    def context, producer
    def ENDPOINT = 'mock:log:out'

    def mockSimpleInputService = Mock(SimpleInputService)
    def mockSimpleOutputService = Mock(SimpleOutputService)


    def setup() {
        def router = new SimpleRouter(simpleInputService: mockSimpleInputService,
                simpleOutputService: mockSimpleOutputService)

        context = new DefaultCamelContext()
        context.addRoutes(router)
        context.start()

        context.routeDefinitions.toList().each { RouteDefinition routeDefinition ->
            routeDefinition.adviceWith(context as ModelCamelContext, new AdviceWithRouteBuilder() {
                @Override
                void configure() throws Exception {
                    mockEndpointsAndSkip('log:out')
                }
            })
        }

        producer = context.createProducerTemplate()
    }

    def cleanup() {
        context.stop()
    }

    def 'test -- empty message body with no headers'() {
        given:
        def mockTestOutputEndpoint = MockEndpoint.resolve(context, ENDPOINT)
        mockTestOutputEndpoint.expectedCount = 0

        when:
        producer.sendBody('direct:test-input', '')

        then:
        1 * mockSimpleInputService.performSimpleStringTask('')
        0 * mockSimpleOutputService.performSomeOtherSimpleStringTask(_)
        mockTestOutputEndpoint.assertIsSatisfied()
    }

    def 'test -- empty message body && valid output header'() {
        given:
        def mockTestOutputEndpoint = MockEndpoint.resolve(context, ENDPOINT)
        mockTestOutputEndpoint.expectedCount = 1

        when:
        producer.sendBodyAndHeaders('direct:test-input', '', ['SEND_OUT': true])

        then:
        1 * mockSimpleInputService.performSimpleStringTask('')
        1 * mockSimpleOutputService.performSomeOtherSimpleStringTask(_)
        mockTestOutputEndpoint.assertIsSatisfied()
    }
}
