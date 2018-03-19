package org.am061.java.rxjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RxJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RxJavaApplication.class, args);
    }
}

/*
    http://www.baeldung.com/rxjava-tutorial

    At a glance, the RxJava may look similar to Java 8 Streams, but in fact,
    it is much more flexible and fluent, making it a powerful programming paradigm.

    On one side, functional programming is the process of building software by composing pure functions,
    avoiding shared state, mutable data, and side-effects. On the other side, reactive programming is
    an asynchronous programming paradigm concerned with data streams and the propagation of change.

    Together, functional reactive programming forms a combination of functional and reactive techniques that
    can represent an elegant approach to event-driven programming – with values that change over time and
    where the consumer reacts to the data as it comes in.

    Reactive Manifesto:
        Responsive – systems should respond in a timely manner
        Message Driven – systems should use async message-passing between components to ensure loose coupling
        Elastic – systems should stay responsive under high load
        Resilient – systems should stay responsive when some components fail

    Observable represents any object that can get data from a data source and whose state may be of interest
    in a way that other objects may register an interest.

    An observer is any object that wishes to be notified when the state of another object changes

    An observer subscribes to an Observable sequence.
    The sequence sends items to the observer one at a time.
    The observer handles each one before processing the next one.
    If many events come in asynchronously, they must be stored in a queue or dropped.

    In Rx, an observer will never be called with an item out of order or
    called before the callback has returned for the previous item.

    Two types of Observable:

        Non-Blocking – asynchronous execution is supported and is allowed to unsubscribe at any point in the event stream.
        Blocking – all onNext observer calls will be synchronous, and it is not possible to unsubscribe
                   in the middle of an event stream. We can always convert an Observable into a Blocking Observable, using the method toBlocking


    An operator is a function that takes one Observable (the source) as its first argument
    and returns another Observable (the destination). Then for every item that the source observable emits,
    it will apply a function to that item, and then emit the result on the destination Observable.

    Operators can be chained together to create complex data flows that filter event based on certain criteria.
    Multiple operators can be applied to the same observable.

    It is not difficult to get into a situation in which an Observable is emitting items faster
    than an operator or observer can consume them - back-pressure problem.
 */
