package multithreading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class VolatileAndAtomicDemo {
    private volatile boolean ready;
    private String message;

    public static void main(String[] args) throws InterruptedException {
        VolatileAndAtomicDemo demo = new VolatileAndAtomicDemo();
        demo.showVisibility();
        showAtomicUpdates();
    }

    private void showVisibility() throws InterruptedException {
        Thread reader = new Thread(() -> {
            try {
                while (!ready) {
                    Thread.sleep(1);
                }
                System.out.println(message);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "reader");
        reader.start();
        message = "The worker sees the published message.";
        ready = true;
        reader.join();
    }

    private static void showAtomicUpdates() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Runnable increment = () -> {
            for (int i = 0; i < 10_000; i++) {
                count.incrementAndGet();
            }
        };
        Thread first = new Thread(increment);
        Thread second = new Thread(increment);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println("Atomic count: " + count.get());

        String pending = "PENDING";
        AtomicReference<String> status = new AtomicReference<>(pending);
        System.out.println("Changed to DONE: " + status.compareAndSet(pending, "DONE"));
        System.out.println("Changed from PENDING again: " + status.compareAndSet(pending, "FAILED"));
        System.out.println("Final status: " + status.get());
    }
}
