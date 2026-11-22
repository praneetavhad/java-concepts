# Java multithreading

This guide follows the topics in your Multithreading.pdf. Run each demo from its own `main` method in IntelliJ. Thread scheduling can change the order of output between runs.

## Start here

| Term | Meaning |
| --- | --- |
| Process | A running instance of a program, such as the JVM running one demo. |
| Thread | A path of execution inside a process. Threads have their own call stacks and can access objects they share. |
| Task | Work to perform. A `Runnable` describes work without a return value. |
| Multitasking | The operating system lets multiple tasks or processes make progress. |
| Multithreading | A process uses multiple threads to get work done. |
| Concurrency | Tasks make progress over overlapping periods. On one core the operating system can interleave runnable threads. |
| Parallelism | Work runs at the same instant, for example on separate CPU cores. |
| Time slicing | The scheduler can give runnable threads turns on a core. |
| Context switch | The system saves one thread's execution state and restores another's so it can continue. |

## 1. Create and start a thread

[ThreadCreationDemo.java](ThreadCreationDemo.java) shows two ways to define work. Extend `Thread` and override `run()`, or implement `Runnable` and pass the task to a `Thread`. A `Runnable` keeps the work reusable, including in a thread pool.

```java
Thread second = new Thread(task, "runnable-worker");
second.run();    // Runs on main as a regular method call.
second.start();  // Starts a separate thread that calls run().
second.join();   // The caller waits for that thread to finish.
```

The same thread object can be started once. `Thread.currentThread().getName()` identifies the thread actually executing the code. In the demo, the direct call prints `main`. The worker lines after `start()` may appear in either order, and the final line comes after both joins.

## 2. Thread states and interruption

[ThreadLifecycleDemo.java](ThreadLifecycleDemo.java) uses `getState()` before starting and after joining. A latch lets main wait until the worker reaches its first instruction. Main then requests interruption so it can finish without waiting for the full sleep.

| Java state | Example |
| --- | --- |
| `NEW` | Thread created but still waiting for its first `start()`. |
| `RUNNABLE` | Ready for CPU time or actively executing. Java groups these together. |
| `BLOCKED` | Waiting to acquire an intrinsic monitor, such as when entering a busy `synchronized` block. |
| `WAITING` | Waiting without a timeout, for example in `Object.wait()` or `Thread.join()`. |
| `TIMED_WAITING` | Waiting for up to a specified time, for example in `Thread.sleep(...)`. |
| `TERMINATED` | The thread has finished. |

The middle state is a snapshot. If main checks before the worker sleeps, it can see `RUNNABLE`. If it checks during the sleep, it can see `TIMED_WAITING`. `interrupt()` requests cooperation. An interrupted `sleep()` throws `InterruptedException` and clears the interrupt status, so the worker restores the status and lets `run()` finish.

## 3. Race conditions and synchronization

[SynchronizationDemo.java](SynchronizationDemo.java) uses the PDF's bank account example. Two users share a balance of 1,000 and try to withdraw 700 and 500. If both can check the old balance before either deducts money, both can approve a withdrawal. The result then depends on the timing of those operations. This is a **race condition**.

```java
synchronized (this) {
    if (balance < amount) {
        return false;
    }
    balance -= amount;
    return true;
}
```

The check and deduction form one **critical section**. Both users lock the same account's monitor, so one completes the check and deduction before the other enters. Only one withdrawal succeeds. The final balance is 300 or 500 depending on who gets the lock first.

An instance method declared `synchronized` also locks `this`, as shown by `getBalance()`. A synchronized block lets you choose the lock object and the statements it protects. Java releases the monitor when control leaves the synchronized section, including when an exception occurs.

## 4. Explicit locks and read/write locks

[ReentrantLockDemo.java](ReentrantLockDemo.java) uses a `ReentrantLock`. The method `outer()` acquires the lock and calls `inner()`, which acquires it again on the same thread. This is **reentrancy**. The hold counts printed are 1 and 2. Each successful acquisition needs its own release.

```java
lock.lock();
try {
    // Work protected by the lock.
} finally {
    lock.unlock();
}
```

| Operation | Behavior |
| --- | --- |
| `lock()` | Waits until it can acquire the lock. |
| `tryLock()` | Tries immediately and returns `false` if busy. |
| `tryLock(100, TimeUnit.MILLISECONDS)` | Waits for up to 100 milliseconds and can throw `InterruptedException`. |
| `lockInterruptibly()` | Waits for the lock and can stop waiting if interrupted. |
| `unlock()` | Releases one hold. Call it only after a successful acquisition. |

The demo keeps the lock busy while another thread makes an immediate attempt and a timed attempt. `new ReentrantLock(true)` requests a fair lock, which favors longer-waiting threads under contention. Thread scheduling can still vary, and the untimed `tryLock()` can acquire an available fair lock ahead of queued threads.

[ReadWriteLockDemo.java](ReadWriteLockDemo.java) uses `readLock()` and `writeLock()` from the same `ReentrantReadWriteLock`. Multiple readers can hold the read lock together. A writer needs exclusive access. The readers may see 0 or 42 depending on when they acquire the lock, and the final read after all joins sees 42.

## 5. Deadlock, starvation, and livelock

A **deadlock** can occur when Person A holds the pen and waits for paper, while Person B holds the paper and waits for the pen. The four conditions are all present.

1. **Mutual exclusion** means one person can hold each resource at a time.
2. **Hold and wait** means each person keeps one resource while requesting another.
3. **No preemption** means the resource cannot be taken away from its current holder.
4. **Circular wait** means A waits for B and B waits for A.

[DeadlockPreventionDemo.java](DeadlockPreventionDemo.java) breaks the cycle by having both people acquire the pen before the paper. The demo finishes regardless of who gets the pen first.

| Problem | What you would observe |
| --- | --- |
| Deadlock | Each person is stuck waiting for the other to release a resource. |
| Starvation | One person keeps missing out on the pen while other people continue writing. |
| Livelock | Two people keep reacting to one another, like repeatedly stepping to the same side in a hallway, and neither gets past. |

## 6. Thread communication with a producer and consumer

[ProducerConsumerDemo.java](ProducerConsumerDemo.java) uses a one-slot mailbox. The producer waits if the slot is full and the consumer waits if it is empty.

```java
synchronized String take() throws InterruptedException {
    while (message == null) {
        wait();
    }
    String result = message;
    message = null;
    notifyAll();
    return result;
}
```

`wait()` releases the mailbox monitor while the thread waits and reacquires it before returning. `Thread.sleep()` keeps any monitor the thread already holds. Call `wait()`, `notify()`, and `notifyAll()` while holding that object's monitor. `notify()` wakes one waiter and `notifyAll()` wakes all waiters, who can compete for the monitor after it is released. The `while` loop checks the condition again because the state could have changed or a thread could wake without a notification.

## 7. Reuse workers with a thread pool

[ThreadPoolDemo.java](ThreadPoolDemo.java) creates an `ExecutorService` with two workers. A pool reuses workers across tasks and controls how many tasks can run at once.

```java
pool.execute(message);                       // Runnable, no Future returned.
Callable<Integer> calculation = () -> 6 * 7;
Future<Integer> result = pool.submit(calculation);
System.out.println("Callable result: " + result.get());
```

`Runnable.run()` returns no value. `Callable.call()` can return a value and throw a checked exception. `submit()` returns a `Future` for either task type, and `get()` waits if necessary. If the submitted task fails, `get()` throws `ExecutionException` with the task failure as its cause.

`shutdown()` stops accepting new tasks and lets accepted tasks finish. `awaitTermination()` waits for the pool to terminate. If the wait times out, `shutdownNow()` attempts to interrupt running tasks and returns queued tasks that never started.

## 8. Visibility and atomic updates

[VolatileAndAtomicDemo.java](VolatileAndAtomicDemo.java) shows two different needs when threads share data.

- **Visibility with `volatile`.** Main writes the message and then sets the volatile flag `ready` to `true`. Once the worker reads `true`, it can also see the earlier message write. The write to the flag publishes the message.
- **Atomic updates with `AtomicInteger`.** `count++` reads, increments, and writes the count. Two threads can read the same old value and overwrite each other's updates, even if the field is volatile. `incrementAndGet()` performs the update atomically. Two workers increment 10,000 times each, so the demo prints a final count of 20,000.
- **Compare-and-set with `AtomicReference`.** `compareAndSet(pending, "DONE")` updates the stored reference only if it still points to the expected object. The second attempt expects the old reference and returns `false`. CAS stands for **compare and set**. An atomic reference protects operations on the reference itself. Fields inside the referenced object may still need their own synchronization.

Use `synchronized` or a shared lock when several steps must work together as one operation, such as checking and updating the bank balance.

## Practice in order

1. In the creation demo, start with the direct `run()` call and predict which thread name it prints. Then remove it and compare the output.
2. Run the lifecycle demo a few times. Which two states are fixed by where the code checks them? What can change in the middle?
3. In the bank demo, change the withdrawals to 400 and 500. Predict how many succeed and what remains.
4. In the pen-and-paper demo, trace what would happen if the second person acquired the locks in reverse order. Draw who holds what and who waits for what.
5. Add a third message to the producer and a third `take()` to the consumer. Predict the message order.
6. Change the pool calculation to throw an exception. Observe what `Future.get()` reports.
7. Change the atomic increment loop to 5,000 iterations and predict the final count.

**Quick answers.** The direct call runs on main. The first and last lifecycle states are `NEW` and `TERMINATED`. Both smaller withdrawals succeed and leave 100. Reversing the lock order can introduce a circular wait. The mailbox delivers messages in the order they are put. A failing submitted task is reported as `ExecutionException`. Two loops of 5,000 increments produce 10,000.
