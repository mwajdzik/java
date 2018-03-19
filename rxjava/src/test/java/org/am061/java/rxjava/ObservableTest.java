package org.am061.java.rxjava;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
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

    @Test
    public void transformationsGroupBy() {
        result = "|";

        Observable.range(0, 10)
                .groupBy(i -> 0 == (i % 2) ? "EVEN" : "ODD")
                .subscribe(group ->
                        group.subscribe((number) -> {
                            if (group.getKey().equals("EVEN")) {
                                result = number + result;
                            } else {
                                result = result + number;
                            }
                        })
                );

        assertEquals("86420|13579", result);
    }

    @Test
    public void transformationsFilter() {
        Observable.range(0, 10)
                .filter(i -> (i % 2 == 1))
                .subscribe(i -> result += i);

        assertEquals("13579", result);
    }

    @Test
    public void conditionalOperators() {
        Observable.empty()
                .defaultIfEmpty("Observable is empty")
                .subscribe(s -> result += s);

        assertEquals("Observable is empty", result);

        result = "";
        Observable.fromArray("a", "b", "c")
                .defaultIfEmpty("Observable is empty")
                .firstElement()
                .subscribe(s -> result += s);

        assertEquals("a", result);

        result = "";

        Observable.range(0, 10)
                .takeWhile(i -> i < 5)
                .subscribe(i -> result += i);

        assertEquals("01234", result);
    }

    // A ConnectableObservable resembles an ordinary Observable, except that it doesn’t begin emitting items
    // when it is subscribed to, but only when the connect operator is applied to it.
    @Test
    public void connectableObservables() throws InterruptedException {
        ConnectableObservable<Long> connectable = Observable.interval(200, MILLISECONDS).publish();
        connectable.subscribe(i -> result += i);

        assertEquals("", result);

        connectable.connect();
        Thread.sleep(500);

        assertEquals("01", result);
    }

    @Test
    public void single() {
        Maybe<String> single = Observable.just("Hello")
                .singleElement()
                .doOnSuccess(i -> result += i)
                .doOnError(error -> {
                    throw new RuntimeException(error.getMessage());
                });

        single.subscribe();

        assertEquals("Hello", result);
    }

    // A Subject is simultaneously two elements, a subscriber and an observable.
    // As a subscriber, a subject can be used to publish the events coming from more than one observable.
    // And because it’s also observable, the events from multiple subscribers can be reemitted
    // as its events to anyone observing it.
    private Integer value1 = 0;
    private Integer value2 = 0;

    @Test
    public void subject() {
        Observer<Integer> firstObserver = new Observer<Integer>() {
            @Override
            public void onNext(Integer value) {
                value1 += value;
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("Subscriber1 completed");
            }
        };

        Observer<Integer> secondObserver = new Observer<Integer>() {
            @Override
            public void onNext(Integer value) {
                value2 += value;
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("Subscriber2 completed");
            }
        };

        PublishSubject<Integer> subject = PublishSubject.create();
        subject.subscribe(firstObserver);
        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);
        subject.subscribe(secondObserver);
        subject.onNext(4);
        subject.onNext(5);
        subject.onComplete();

        assertEquals((Integer) 15, value1);
        assertEquals((Integer) 9, value2);
    }
}