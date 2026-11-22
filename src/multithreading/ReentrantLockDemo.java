package multithreading;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private final ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        demo.outer();

        Thread contender = new Thread(demo::tryWhileBusy, "contender");
        demo.lock.lock();
        try {
            contender.start();
            // The contender uses bounded waits and can finish while main owns the lock.
            contender.join();
        } finally {
            demo.lock.unlock();
        }
    }

    private void outer() {
        lock.lock();
        try {
            System.out.println("Outer hold count: " + lock.getHoldCount());
            inner();
        } finally {
            lock.unlock();
        }
    }

    private void inner() {
        lock.lock();
        try {
            System.out.println("Inner hold count: " + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }

    private void tryWhileBusy() {
        if (lock.tryLock()) {
            lock.unlock();
        } else {
            System.out.println("Immediate attempt: lock is busy.");
        }

        try {
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                lock.unlock();
            } else {
                System.out.println("Timed attempt: gave up waiting.");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
