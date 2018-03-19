package org.am061.java.rxjava;

import io.reactivex.Observable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObservableTest {

    private String result = "";

    @Test
    public void create() {
        Observable<String> observable = Observable.just("Hello");
        observable.subscribe(s -> result = s);

        assertEquals("Hello", result);
    }

    @Test
    public void callbacks() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};

        Observable<String> observable = Observable.fromArray(letters);
        observable.subscribe(
                i -> result += i,               // OnNext - called on our observer each time a new event is published to the attached Observable
                Throwable::printStackTrace,     // OnError - called when an unhandled exception is thrown during the RxJava framework code or our event handling code
                () -> result += "_Completed"    // OnCompleted - called when the sequence of events associated with an Observable is complete
        );

        assertEquals("abcdefg_Completed", result);
    }

    @Test
    public void transformationsMap() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};

        Observable.fromArray(letters)
                .map(String::toUpperCase)
                .subscribe(letter -> result += letter);

        assertEquals("ABCDEFG", result);
    }

    @Test
    public void transformationsFlatMap() {
        Observable.just("book1", "book2")
                .flatMap(s -> Observable.fromArray(s.split("")))
                .subscribe(l -> result += l);

        assertEquals("book1book2", result);
    }

    // scan() allows us to carry forward state from event to event
    @Test
    public void transformationsScan() {
        String[] letters = {"a", "b", "c"};

        Observable.fromArray(letters)
                .scan(new StringBuilder(), StringBuilder::append)
                .subscribe(total -> result += total.toString());

        assertEquals("aababc", result);
    }
}