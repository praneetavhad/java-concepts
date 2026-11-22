# Lambda expressions in Java 8

Read [LambdaBasicsDemo.java](LambdaBasicsDemo.java) first, then [LambdaInPracticeDemo.java](../LambdaInPracticeDemo.java).

## 1. What a lambda is and why we use it

A **lambda expression** is a compact way to supply behavior. For example, the first demo defines how to add two integers and stores that behavior in an `IntOperation` variable. You can also pass behavior into a method. The second demo passes a comparison rule to `List.sort()`.

## 2. The functional interface gives the lambda its type

A **functional interface** has one abstract method. It can also have methods with bodies, such as `default` or `static` methods. `@FunctionalInterface` is optional and tells the compiler to check that the interface qualifies.

```java
@FunctionalInterface
interface IntOperation {
    int apply(int first, int second);
}

IntOperation addition = (first, second) -> first + second;
System.out.println(addition.apply(4, 3)); // 7
```

The assignment tells Java the **target type** is `IntOperation`. Its `apply` signature tells Java to expect two `int` parameters and an `int` result. The lambda body runs when you call `apply(4, 3)`. The parameter names in the lambda are your choice.

In the first demo, `new IntOperation() { ... }` uses an anonymous class and spells out the implementation of `apply`. The lambda supplies that same addition behavior with just the parameters and body.

## 3. Read the syntax from left to right

The general shape is `(parameters) -> body`. The left side receives inputs and the right side describes the work.

| Shape | Example | How to read it |
| --- | --- | --- |
| No parameters | `() -> "Guest"` | Return the fallback name. Empty parentheses are required. |
| One inferred parameter | `name -> name.length()` | Take one name and return its length. Parentheses are optional here. |
| Two inferred parameters | `(first, second) -> first + second` | Take two numbers and return their sum. |
| Explicit types and a block | `(int first, int second) -> { return first * second; }` | Take two integers and return their product. |

An **expression body** uses the expression's value as the result when the target method expects a value. A **block body** uses braces and needs an explicit `return` on each normal path when the method expects a value. A `void` target such as `Runnable.run()` can use a call to `println` as its body.

## 4. Capture a local variable

```java
String message = "Hello from a captured local variable.";
Runnable printMessage = () -> System.out.println(message);
printMessage.run();
```

The lambda uses `message` from the enclosing method. A captured local variable must be **final** or **effectively final**. Here, `message` is effectively final because it is assigned once and never reassigned. Try adding another assignment to it and see what the compiler reports.

The direct `run()` call executes on the current thread, just like in the multithreading demo. To use the same task on a new thread, pass it to `new Thread(printMessage)` and call `start()`.

## 5. Use lambdas with Java's functional interfaces

| Interface | Call the behavior with | Purpose in the demo |
| --- | --- | --- |
| `Comparator<String>` | `compare(first, second)` | Decide which name comes first by length. |
| `Predicate<String>` | `test(name)` | Return `true` if the name has at least four letters. |
| `Function<String, Integer>` | `apply(name)` | Turn a name into its length. |
| `Consumer<String>` | `accept(name)` | Print a name. `forEach` calls it for every item. |
| `Supplier<String>` | `get()` | Provide the fallback name without input. |
| `Runnable` | `run()` | Run work with no input or returned value. |

Here is the sorting rule from the second demo. It has the same role as the anonymous comparators in your `Comparable_Comparator` package.

```java
Comparator<String> byLength = (first, second) ->
        Integer.compare(first.length(), second.length());
names.sort(byLength);
```

`List.sort` calls the comparator as needed. A negative result puts the first name earlier, zero means they tie by length, and a positive result puts the first name later. The result is `[Raj, Aman, Priya]`.

When a lambda simply calls an existing method, a **method reference** can express the same behavior. For example, `Function<String, Integer> nameLength = String::length;` supplies the same operation as `name -> name.length()`.

## Practice

1. Add a subtraction lambda to the first demo and call `apply(4, 3)`. Expect `1`.
2. Swap the lengths passed to `Integer.compare` in the comparator. Predict the descending order before running it.
3. Replace `name -> name.length()` with `String::length` and compare the output.
4. Add `if (hasAtLeastFourLetters.test(name))` inside a loop over the names. Predict which names pass.
