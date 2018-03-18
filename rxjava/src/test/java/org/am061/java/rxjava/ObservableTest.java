package org.am061.java.rxjava;

import io.reactivex.Observable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ObservableTest {

    private String result = "";

    @Test
    public void methods() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};

        Observable<String> observable = Observable.fromArray(letters);
        observable.subscribe(
                i -> result += i,               // OnNext
                Throwable::printStackTrace,     // OnError
                () -> result += "_Completed"    // OnCompleted
        );

        assertTrue(result.equals("abcdefg_Completed"));
    }
}