package multithreading;

public class ThreadCreationDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread first = new NamedWorker();
        Runnable task = new PrintTask();
        Thread second = new Thread(task, "runnable-worker");

        System.out.println("Calling run() directly:");
        second.run(); // An ordinary method call on the main thread.

        System.out.println("Calling start():");
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println("Both workers finished.");
    }

    private static class NamedWorker extends Thread {
        NamedWorker() {
            super("thread-subclass");
        }

        @Override
        public void run() {
            System.out.println("Subclass runs on " + Thread.currentThread().getName());
        }
    }

    private static class PrintTask implements Runnable {
        @Override
        public void run() {
            System.out.println("Task runs on " + Thread.currentThread().getName());
        }
    }
}
