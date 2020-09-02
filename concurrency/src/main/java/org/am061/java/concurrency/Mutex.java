package org.am061.java.concurrency;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mutex {

    @SneakyThrows
    public static void main(String[] args) {
        Shopper th1 = new Shopper();
        Shopper th2 = new Shopper();

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        System.out.println(Shopper.unsafeCounter);
        System.out.println(Shopper.safeCounter);
        System.out.println(Shopper.atomicCounter.get());
        System.out.println(Shopper.synchronizedCounter);
        System.out.println((Shopper.tryCounter + th1.givingUpCounter + th2.givingUpCounter) + ", " +
                Shopper.tryCounter + ", " + th1.givingUpCounter + ", " + th2.givingUpCounter);
    }
}

class Shopper extends Thread {

    static int unsafeCounter = 0;

    static int safeCounter = 0;
    static Lock mutex = new ReentrantLock();

    static AtomicLong atomicCounter = new AtomicLong();

    static int synchronizedCounter = 0;

    static int tryCounter = 0;
    int givingUpCounter = 0;
    static Lock tryMutex = new ReentrantLock();

    public void run() {
        for (int i = 0; i < 1_000_000; i++) {
            unsafeCounter++;

            mutex.lock();
            safeCounter++;
            mutex.unlock();

            atomicCounter.incrementAndGet();

            synchronized (Shopper.class) {
                synchronizedCounter++;
            }

            boolean lockInquired = tryMutex.tryLock();
            if (lockInquired) {
                tryCounter++;
                tryMutex.unlock();
            } else {
                givingUpCounter++;
            }
        }
    }
}
