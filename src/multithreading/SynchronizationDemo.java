package multithreading;

public class SynchronizationDemo {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        Thread first = new Thread(() -> attempt(account, 700), "User 1");
        Thread second = new Thread(() -> attempt(account, 500), "User 2");

        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println("Final balance: " + account.getBalance());
    }

    private static void attempt(BankAccount account, int amount) {
        boolean withdrawn = account.withdraw(amount);
        String result = withdrawn ? " withdrew " : " could not withdraw ";
        System.out.println(Thread.currentThread().getName() + result + amount);
    }

    private static class BankAccount {
        private int balance = 1_000;

        boolean withdraw(int amount) {
            synchronized (this) {
                if (balance < amount) {
                    return false;
                }
                balance -= amount;
                return true;
            }
        }

        synchronized int getBalance() {
            return balance;
        }
    }
}
