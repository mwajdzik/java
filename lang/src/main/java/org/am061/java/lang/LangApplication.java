package org.am061.java.lang;

import static org.am061.java.lang.Fibonacci.fib;

public class LangApplication {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        int n = 35;
        int result = fib(n);
        long t2 = System.currentTimeMillis();

        System.out.printf("Fibonacci number for %d is %d, computed in %d ms\n", n, result, t2 - t1);
    }
}
