package org.am061.java.concurrency;

import lombok.SneakyThrows;

import static java.lang.Thread.sleep;

public class ThreadLifecycle {

    static class MyThread extends Thread {

        @SneakyThrows
        @Override
        public void run() {
            log("runnable");
            log("sleeping...");
            sleep(3000);
            log("done");
        }
    }

    // new, runnable, blocked, terminated, timed_waiting

    @SneakyThrows
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long usedKb = (runtime.totalMemory() - runtime.freeMemory()) / 1024;

        System.out.println("Thread Count: " + Thread.activeCount());
        System.out.println("Memory Usage: " + usedKb + "kb");

        MyThread myThread = new MyThread();
        logState(myThread);

        log("starting my thread");
        myThread.start();
        logState(myThread);

        sleep(1000);
        logState(myThread);

        log("waiting for myThread to join");
        myThread.join();
        logState(myThread);

        log("myThread is done");
    }

    private static void log(String message) {
        Thread thread = Thread.currentThread();
        System.out.printf("%s: %s\n", thread.getName(), message);
    }

    private static void logState(Thread thread) {
        System.out.printf("\t%s: %s\n", thread.getName(), thread.getState());
    }
}
