import lombok.SneakyThrows;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
To deal with the deadlock we change the order of (give different priority to) locks.
This technique is called Lock Ordering - ensure locks are always taken in the same order by any thread.
Other solution to deal with deadlock can be Lock Timeout - put a timeout on lock attempts.
If a thread cannot acquire all locks within time limit:
- back up and free all locks taken
- wait for a random amount of time
- try again
 */
public class Deadlock {

    static int mealCount = 1_000;

    @SneakyThrows
    public static void main(String[] args) {
        Lock fork1 = new ReentrantLock();
        Lock fork2 = new ReentrantLock();
        Lock fork3 = new ReentrantLock();

        Philosopher p1 = new Philosopher("Maciek", fork1, fork2);
        Philosopher p2 = new Philosopher("Madzia", fork2, fork3);

        // deadlock
        // Philosopher p3 = new Philosopher("Kuba", fork3, fork1);

        // no deadlock
        Philosopher p3 = new Philosopher("Kuba", fork1, fork3);

        p1.start();
        p2.start();
        p3.start();

        p1.join();
        p2.join();
        p3.join();
    }
}

class Philosopher extends Thread {

    private Lock firstFork, secondFork;

    public Philosopher(String name, Lock firstFork, Lock secondFork) {
        super(name);
        this.firstFork = firstFork;
        this.secondFork = secondFork;
    }

    @Override
    public void run() {
        while (Deadlock.mealCount > 0) {
            firstFork.lock();
            secondFork.lock();

            if (Deadlock.mealCount > 0) {
                Deadlock.mealCount--;
                System.out.println(this.getName() + " took a piece! Remaining: " + Deadlock.mealCount);
            }

            secondFork.unlock();
            firstFork.unlock();
        }
    }
}
