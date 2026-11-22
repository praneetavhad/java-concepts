# Exception handling in Java

Start with [TryCatchDemo.java](TryCatchDemo.java), then run [CheckedExceptionDemo.java](CheckedExceptionDemo.java) and [CustomExceptionDemo.java](CustomExceptionDemo.java). Each file has its own `main` method.

## Keywords and syntax

| Syntax | What it does |
| --- | --- |
| `try` | Wraps code that might throw an exception. |
| `catch (ExceptionType exception)` | Handles an exception of the named type or one of its subclasses. |
| `finally` | Runs as control leaves the `try` and any selected `catch` during normal Java execution. |
| `throw` | Raises a specific exception object at that point in the code. |
| `throws` | Declares exceptions that can leave a method. |
| `try (resource)` | Opens a resource for the block and automatically closes it afterward. |
| `getMessage()` | Reads the detail message stored in an exception. |
| `super(message)` | Passes the message to the parent exception constructor. |

## 1. Catch an exception and continue

An **exception** is an object that reports a problem during execution. If a statement throws, Java skips the rest of that `try` block and looks for a matching `catch`. The first matching handler runs. If the current method has no matching handler, the exception goes back to its caller. This is called **propagation**.

In the division demo, parsing the input and doing integer division can fail for different reasons.

```java
try {
    int first = Integer.parseInt(numerator);
    int second = Integer.parseInt(denominator);
    System.out.println("Result = " + first / second);
} catch (NumberFormatException exception) {
    System.out.println("Enter valid whole numbers.");
} catch (ArithmeticException exception) {
    System.out.println("Cannot divide by zero.");
} finally {
    System.out.println("Finished this attempt.");
}
```

| Inputs | What happens |
| --- | --- |
| `"12", "3"` | Prints 4 and skips both handlers. |
| `"12", "0"` | Parsing succeeds. Integer division throws `ArithmeticException`. |
| `"twelve", "3"` | The first parse throws `NumberFormatException`, so division never runs. |

All three attempts print the `finally` message. When catching related exception types, put the more specific handler first. For example, a handler for `NumberFormatException` must come before a handler for its parent type, `IllegalArgumentException`.

**Practice.** Add `divide("7", "2")` and `divide("7", "two")`. Predict the result or handler before running. Integer division discards the fractional part, so the first call prints 3.

## 2. Checked and unchecked exceptions

| Kind | What Java checks | Example |
| --- | --- | --- |
| Checked | The compiler requires a handler or a declaration in `throws`. | `IOException` when a file cannot be read. |
| Unchecked | The compiler allows the code without a handler or a `throws` declaration. | `NumberFormatException`, `ArithmeticException`, and other subclasses of `RuntimeException`. |

Both kinds can occur while the program is running. **Checked** refers to the compiler checking how your code deals with the exception. Java's `Error` types are also unchecked and generally describe serious problems outside ordinary application error handling.

### Read a file and close it automatically

```java
private static String readFirstLine(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
        return reader.readLine();
    }
}
```

`throws IOException` lets the file error reach `main`, which catches it and prints the file path and exception message. Try-with-resources closes the reader after a successful return or an exception once the reader has opened. An empty file returns `null` from `readLine()`.

**Practice.** Run the demo from the project root to read `src/Main.java`. Then pass a nonexistent filename as a program argument and see the handler run.

## 3. Throw your own exception

A custom exception gives a problem a name specific to your program. In the booking demo, requesting more seats than are available throws a `NotEnoughSeatsException`.

```java
class NotEnoughSeatsException extends Exception {
    NotEnoughSeatsException(String message) {
        super(message);
    }
}
```

It extends `Exception` directly, so it is checked. The constructor passes the message to the parent class, and the booking method can print that message with `getMessage()`.

```java
private static void checkAvailability(int requested, int available)
        throws NotEnoughSeatsException {
    if (requested > available) {
        throw new NotEnoughSeatsException("Only " + available + " seats are available.");
    }
}
```

Read the method from top to bottom. `throws NotEnoughSeatsException` tells callers what may happen. The `throw new ...` statement creates and raises the exception only when the request is too large. The caller catches it and gives the user a message.

**Practice.** Add `bookSeats(5, 5)` and predict what happens. Then try `bookSeats(0, 5)`. The current check only covers availability. Add a separate validation that throws `IllegalArgumentException` when the request is zero or negative, and handle it with a clear message.

## Quick revision

- **What runs after a statement in a `try` block throws?** The matching handler, if there is one, followed by the `finally` block if present.
- **Who handles `IOException` in the file demo?** `main` catches it. `readFirstLine` declares that it can propagate the exception.
- **Which syntax raises an exception?** `throw`. The method declaration uses `throws` to describe what can reach the caller.
- **Which syntax closes the reader?** Try-with-resources closes it automatically.
