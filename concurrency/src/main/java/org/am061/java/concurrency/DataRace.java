package org.am061.java.concurrency;

public class DataRace {

    public static void main(String[] args) throws InterruptedException {
        MyThread th1 = new MyThread();
        MyThread th2 = new MyThread();

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        System.out.println("The unsafe counter: " + MyThread.unsafeCounter);
    }
}

class MyThread extends Thread {

    static int unsafeCounter = 0;

    @Override
    public void run() {
        for (int i = 0; i < 1_000_000; i++) {
            unsafeCounter++;
        }
    }
}
