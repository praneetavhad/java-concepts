# Streams vs. for loops in Java 8

A **for loop** gives you control over each step of iteration. You decide where to keep a running total, when to skip an item, and when to stop. A **stream** expresses a sequence of operations on data. You describe which elements to keep, how to transform them, and what result to produce.

Both styles can process the same data and return the same answer. Choose the one that makes the intent and the stopping conditions easiest to see.

## 1. Filter, transform, and sum

Run [FilterMapSumDemo.java](FilterMapSumDemo.java). It keeps names with at least four letters, uppercases them, and totals their lengths.

```java
List<String> loopNames = new ArrayList<>();
for (String name : names) {
    if (name.length() >= 4) {
        loopNames.add(name.toUpperCase(Locale.ROOT));
    }
}

List<String> streamNames = names.stream()
        .filter(name -> name.length() >= 4)
        .map(name -> name.toUpperCase(Locale.ROOT))
        .collect(Collectors.toList());
```

Both produce `[PRIYA, AMAN, NEHA]`. In the stream, `filter` is the condition, `map` creates a transformed value, and `collect` builds the result list. These stages are easy to scan when the task is a straight pipeline.

The demo also reports a total length of `13`. The loop builds the list and adds to the total in one traversal. The stream version uses one pipeline for the list and a second pipeline for the total with `mapToInt(String::length).sum()`. A custom collector could do both in one traversal. For two simple accumulators like these, the loop expresses that work directly.

## 2. Find the first match and stop

Run [FirstMatchDemo.java](FirstMatchDemo.java). Both versions return `Priya` from `[Raj, Priya, Aman, Neha]`.

- The loop checks names in order and returns as soon as a name passes. Use `break` if you need to leave the loop and continue the enclosing method.
- The stream uses `filter(...).findFirst()`. With this sequential list stream, `findFirst` short-circuits at the first match. Use `anyMatch` if you only need a yes-or-no answer.
- Both methods return `Optional.empty()` if nothing matches. The caller uses `orElse("none")` for display.

Either approach is clear for a single search condition. A loop is easier to follow when different conditions require you to skip, stop, or take different actions. Prefer `findFirst` or a match operation when you want an early result from a stream. `forEach` is for visiting elements and has no `break` operation.

## 3. Work with indexes and running state

Run [IndexAndStateDemo.java](IndexAndStateDemo.java). Both approaches compare each day's sales with the preceding day and report days `[2, 4]`.

- The indexed loop starts at index 1 and compares `dailySales[index]` with `dailySales[index - 1]`.
- The stream starts with `IntStream.range(1, dailySales.length)`, filters indexes using the same condition, and adds 1 to turn each index into a display day. `boxed()` converts the primitive results to `Integer` values for the result list.

The second exercise asks for the first day when **cumulative** sales reach 15. The running totals are 4, 11, 18, 28, and 31, so the answer is day 3. The loop updates a local total on each iteration and returns as soon as the target is met. That makes the dependency on previous days explicit. Avoid using a shared mutable counter inside a stream filter to imitate this loop, especially if the stream may run in parallel.

## Which should I choose?

| Task | Good starting point | Why |
| --- | --- | --- |
| Filter and transform values into a new list | Stream | `filter → map → collect` describes the result directly. |
| Group, count by key, or join text | Stream | Built-in collectors express the operation and handle accumulation. |
| Sum or average one numeric field | Either | A primitive stream gives you `sum` and `average`. A loop gives you an explicit accumulator. |
| Get the first match or check whether any match | Either | An early `return`, `findFirst`, and `anyMatch` can all stop early. |
| Work with indexes, neighboring elements, or several running totals | Loop | Indexes and state updates stay visible. |
| Follow several branches with `break`, `continue`, or checked exceptions | Loop | Ordinary control flow and `try`/`catch` can sit next to the relevant work. |
| Update elements in place or coordinate several side effects | Loop | The order and conditions of each update are easy to trace. |
| Explore independent work on a large dataset | Sequential first | Try a parallel stream only when the operations are safe to run concurrently and measurement shows a benefit. |

### Performance and debugging

- Intermediate stream operations are lazy. A simple `filter → map → collect` pipeline can traverse the input once and does not create an intermediate list for each stage. Operations like sorting or grouping can still require extra memory.
- A tight loop over a primitive array can have less overhead. If you choose a numeric stream, use `IntStream` or `mapToInt` to avoid boxing during arithmetic. Measure with representative data when speed matters.
- A loop makes it easy to step through each iteration in the debugger. A pipeline makes transformations easier to read when each stage has a clear purpose.

## Practice

1. Change the minimum length to five in both versions of the first demo. Predict the list and total.
2. Search for a six-letter name in the second demo. Check that both versions print `none`.
3. Change the cumulative target to 40. Trace why the loop returns `-1`.
