package multithreading;

public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        Mailbox mailbox = new Mailbox();
        Thread producer = new Thread(() -> {
            try {
                mailbox.put("First message");
                mailbox.put("Second message");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "producer");
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("Received: " + mailbox.take());
                System.out.println("Received: " + mailbox.take());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        consumer.start();
        producer.start();
        try {
            producer.join();
            consumer.join();
        } finally {
            producer.interrupt();
            consumer.interrupt();
        }
    }

    private static class Mailbox {
        private String message;

        synchronized void put(String newMessage) throws InterruptedException {
            while (message != null) {
                wait();
            }
            message = newMessage;
            notifyAll();
        }

        synchronized String take() throws InterruptedException {
            while (message == null) {
                wait();
            }
            String result = message;
            message = null;
            notifyAll();
            return result;
        }
    }
}
