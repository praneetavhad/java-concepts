package multithreading;

public class DeadlockPreventionDemo {
    private final Object pen = new Object();
    private final Object paper = new Object();

    public static void main(String[] args) throws InterruptedException {
        DeadlockPreventionDemo desk = new DeadlockPreventionDemo();
        Thread first = new Thread(desk::write, "Person A");
        Thread second = new Thread(desk::write, "Person B");

        second.start();
        first.start();
        first.join();
        second.join();
        System.out.println("Both people finished.");
    }

    private void write() {
        // Both threads take the locks in the same order.
        synchronized (pen) {
            synchronized (paper) {
                System.out.println(Thread.currentThread().getName() + " has pen and paper.");
            }
        }
    }
}
