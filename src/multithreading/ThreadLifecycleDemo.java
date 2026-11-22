package multithreading;

import java.util.concurrent.CountDownLatch;

public class ThreadLifecycleDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        Thread worker = new Thread(() -> {
            started.countDown();
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.out.println("Worker received the stop request.");
            }
        }, "sleeping-worker");

        System.out.println("Before start: " + worker.getState());
        worker.start();
        try {
            started.await();
            System.out.println("After start (snapshot): " + worker.getState());
        } finally {
            worker.interrupt();
            worker.join();
        }
        System.out.println("After join: " + worker.getState());
    }
}
