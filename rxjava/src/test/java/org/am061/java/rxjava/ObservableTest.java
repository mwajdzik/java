package org.am061.java.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

public class ObservableTest {

    private static final String[] LETTERS = {"a", "b", "c", "d", "e", "f", "g"};

    private String result = "";

    @Test
    public void createSimpleObservable() {
        Observable<String> observable = Observable.just("Hello");
        Disposable observer = observable.subscribe(s -> result = s);
        observer.dispose();

        assertEquals("Hello", result);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createObservableUsingDifferentMethods() {
        Observable.just("Hello");
        Observable.fromArray("Hello", "world");
        Observable.fromIterable(Arrays.asList("Hello", "world"));
        Observable.interval(100, TimeUnit.MILLISECONDS);

        Observable.create(emitter -> {
            try {
                IntStream.range(0, 10)
                        .boxed()
                        .forEach(emitter::onNext);

                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @Test
    public void callbacks() {
        Observable<String> observable = Observable.fromArray(LETTERS);
        Disposable observer = observable.subscribe(
                c -> result += c,                   // OnNext - called on our observer each time a new event is published to the attached Observable
                Throwable::printStackTrace,         // OnError - called when an unhandled exception is thrown during the RxJava framework code or our event handling code
                () -> result += "_Completed"        // OnCompleted - called when the sequence of events associated with an Observable is complete
        );
        observer.dispose();

        assertEquals("abcdefg_Completed", result);
    }

    @Test
    public void transformationsMap() {
        Disposable observer = Observable.fromArray(LETTERS)
                .map(String::toUpperCase)
                .subscribe(letter -> result += letter);

        observer.dispose();
        assertEquals("ABCDEFG", result);
    }

    @Test
    public void transformationsFlatMapFilterMap() {
        Disposable observer = Observable.just("book1", "book2")
                .flatMap(s -> Observable.fromArray(s.split("")))
                .filter(s -> Character.isLetter(s.charAt(0)))
                .map(s -> s + "*")
                .subscribe(l -> result += l);

        observer.dispose();
        assertEquals("b*o*o*k*b*o*o*k*", result);
    }

    // Flowable = Observable with back-pressure
    @Test
    public void flowable() throws InterruptedException {
        Disposable disposable = Flowable.fromArray(LETTERS)
                .map(String::toUpperCase)
                .subscribe(letter -> result += letter);

        disposable.dispose();
        assertEquals("ABCDEFG", result);
    }

    @Test
    public void schedulers() {
        Observable.range(0, 100)
                .unsubscribeOn(Schedulers.newThread())
                .map(i -> {
                    result += " " + i;
                    return i;
                })
                .subscribe();

        System.out.println(result);
    }

    // scan() allows us to carry forward state from event to event
    @Test
    public void transformationsScan() {
        String[] letters = {"a", "b", "c"};

        Disposable observer = Observable.fromArray(letters)
                .scan(new StringBuilder(), StringBuilder::append)
                .subscribe(total -> result += total.toString());

        observer.dispose();
        assertEquals("aababc", result);
    }

    @Test
    public void transformationsGroupBy() {
        result = "|";

        Disposable observer = Observable.range(0, 10)
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

        observer.dispose();
        assertEquals("86420|13579", result);
    }

    @Test
    public void conditionalOperators() {
        Disposable observer = Observable.empty()
                .defaultIfEmpty("Observable is empty")
                .subscribe(s -> result += s);

        observer.dispose();
        assertEquals("Observable is empty", result);

        result = "";
        Disposable observer1 = Observable.fromArray("a", "b", "c")
                .defaultIfEmpty("Observable is empty")
                .firstElement()
                .subscribe(s -> result += s);

        observer1.dispose();
        assertEquals("a", result);

        result = "";

        Disposable observer2 = Observable.range(0, 10)
                .takeWhile(i -> i < 5)
                .subscribe(i -> result += i);

        observer2.dispose();
        assertEquals("01234", result);
    }

    // A ConnectableObservable resembles an ordinary Observable, except that it doesn't begin emitting items
    // when it is subscribed to, but only when the connect operator is applied to it.
    @Test
    public void connectableObservables() throws InterruptedException {
        ConnectableObservable<Long> connectable = Observable.interval(200, MILLISECONDS).publish();
        Disposable observer = connectable.subscribe(i -> result += i);

        assertEquals("", result);

        connectable.connect();
        Thread.sleep(1100);

        observer.dispose();
        assertEquals("01234", result);
    }

    @Test
    public void single() {
        Maybe<String> single = Observable.just("Hello")
                .singleElement()
                .doOnSuccess(s -> result += s)
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