package org.am061.java.concurrency;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.am061.java.concurrency.ReadWriteMutex.*;

public class ReadWriteMutex {

    static ReentrantReadWriteLock marker = new ReentrantReadWriteLock();
    static Lock readMarker = marker.readLock();
    static Lock writeMarker = marker.writeLock();

    static int value = 0;

    @SneakyThrows
    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            Reader reader = new Reader("Reader#" + i);
            threads.add(reader);
            reader.start();
        }

        Writer writer = new Writer("Writer");
        threads.add(writer);
        writer.start();

        for (Thread th : threads) {
            th.join();
        }
    }
}

class Reader extends Thread {

    public Reader(String name) {
        super(name);
    }

    @SneakyThrows
    public void run() {
        for (int i = 0; i < 1_000; i++) {
            readMarker.lock();
            System.out.println(this.getName() + " reads : " + value + "\t\tRead Lock Count: " + marker.getReadLockCount());
            readMarker.unlock();

            sleep(1);
        }
    }
}


class Writer extends Thread {

    public Writer(String name) {
        super(name);
    }

    @SneakyThrows
    public void run() {
        for (int i = 0; i < 200; i++) {
            ReadWriteMutex.writeMarker.lock();
            System.out.println("\n>>> Writer in action <<<\n");
            value++;
            ReadWriteMutex.writeMarker.unlock();

            sleep(5);
        }
    }
}
