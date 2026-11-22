package multithreading;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Runnable message = () -> System.out.println("Runnable ran on "
                    + Thread.currentThread().getName());
            pool.execute(message);

            Callable<Integer> calculation = () -> 6 * 7;
            Future<Integer> result = pool.submit(calculation);
            System.out.println("Callable result: " + result.get());
        } finally {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                    List<Runnable> waiting = pool.shutdownNow();
                    System.out.println("Tasks that had not started: " + waiting.size());
                }
            } catch (InterruptedException exception) {
                pool.shutdownNow();
                throw exception;
            }
        }
    }
}
