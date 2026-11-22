package multithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int value;

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockDemo store = new ReadWriteLockDemo();
        Thread writer = new Thread(() -> store.write(42), "writer");
        Thread readerOne = new Thread(() -> printValue(store), "reader-one");
        Thread readerTwo = new Thread(() -> printValue(store), "reader-two");


        readerOne.start();
        readerOne.join();

        writer.start();
        writer.join();

        readerTwo.start();
        readerTwo.join();
        System.out.println("Final value: " + store.read());
    }

    private static void printValue(ReadWriteLockDemo store) {
        System.out.println(Thread.currentThread().getName() + " read " + store.read());
    }

    private int read() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return value;
        } finally {
            readLock.unlock();
        }
    }

    private void write(int newValue) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            value = newValue;
        } finally {
            writeLock.unlock();
        }
    }
}
