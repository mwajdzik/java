package org.am061.java.concurrency;

public class ThreadInfo {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long usedKb = (runtime.totalMemory() - runtime.freeMemory()) / 1024;

        System.out.println("Thread Count: " + Thread.activeCount());
        System.out.println("Memory Usage: " + usedKb + "kb");
    }
}
