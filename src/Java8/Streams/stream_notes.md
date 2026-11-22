# Streams in Java 8

A **stream** processes elements from a source such as a list or an array. A **pipeline** connects a source, zero or more intermediate operations, and one terminal operation. Methods such as `filter` and `map` are API operations. They are ordinary Java methods.

```java
List<String> result = names.stream()              // Source
        .filter(name -> name.length() >= 4)       // Keep matching names
        .map(name -> name.toUpperCase(Locale.ROOT)) // Transform each name
        .collect(Collectors.toList());            // Produce a new list
```

For `[Priya, Raj, Aman]`, the result is `[PRIYA, AMAN]`. The pipeline reads from the original list, which still contains its original names. Intermediate operations are **lazy**. They describe the work, and the terminal operation drives it. Some terminal operations can finish early. Once a pipeline has been used, start with a fresh stream to ask another question about the same list.

## 1. Create a stream

Run [StreamSourcesDemo.java](StreamSourcesDemo.java).

| Operation | How it works | When to use it |
| --- | --- | --- |
| `collection.stream()` | Reads a collection as a sequential stream. | Start with a list, set, or other collection. For a map, start with a view such as `map.entrySet().stream()`. |
| `collection.parallelStream()` | Creates a stream requesting parallel execution. | Consider it for independent work on a large dataset after measuring whether it helps. The terminal demo uses it to illustrate ordering. |
| `Arrays.stream(array)` | Streams the elements of an array. The `int[]`, `long[]`, and `double[]` overloads produce primitive streams. | Process data already stored in an array. |
| `Stream.of(value)`, `Stream.of(a, b, ...)` | Creates a stream from one element or several elements. | Use a small, fixed set of examples without building a list first. |
| `Stream.empty()` | Creates a stream with zero elements. | Return an empty result that callers can still process normally. |
| `Stream.builder()` | Returns a builder. Add elements with `add()` or `accept()`, then call `build()` once to create the stream. | Assemble a small stream when elements are chosen incrementally. |
| `Stream.concat(first, second)` | Produces the elements of the first stream followed by those of the second when both are ordered. | Process two sources in a single pipeline. Use the resulting stream for traversal. |
| `Stream.iterate(seed, next)` | Starts with the seed. Each following element comes from applying the function to the previous element. | Generate a sequence such as 2, 4, 6, 8. Add a bound such as `limit(4)` before collecting. |
| `Stream.generate(supplier)` | Calls a supplier each time another element is needed. The source is unordered and unbounded. | Generate repeated or independently produced values. Add a bound before collecting. |

## 2. Intermediate operations

Run [StreamIntermediateDemo.java](StreamIntermediateDemo.java). Read each stage in order and track the names that reach the next stage.

| Operation | How it works | When to use it |
| --- | --- | --- |
| `filter(predicate)` | Passes through the elements for which `Predicate.test()` returns `true`. | Keep names with at least four letters. |
| `map(function)` | Transforms each element into one output value with `Function.apply()`. The result can have a different type. | Uppercase names or turn objects into selected fields. |
| `flatMap(function)` | Maps each element to a stream and flattens those streams into one. | Turn a list of teams into one stream of members. |
| `mapToInt(function)` | Produces one primitive `int` per element. | Sum name lengths with `sum()` or calculate an average. |
| `mapToLong(function)` | Produces one primitive `long` per element. | Work with large integer values, such as file sizes. |
| `mapToDouble(function)` | Produces one primitive `double` per element. | Work with fractional values or use primitive numeric operations. |
| `flatMapToInt(function)` | Maps each element to an `IntStream` and flattens the results. | Combine the values from several `int[]` arrays. |
| `flatMapToLong(function)` | Maps each element to a `LongStream` and flattens the results. | Combine several groups of long measurements. |
| `flatMapToDouble(function)` | Maps each element to a `DoubleStream` and flattens the results. | Combine batches of fractional measurements. |
| `distinct()` | Removes duplicates according to `equals()`. Ordered streams keep the first occurrence. | Deduplicate results. In the demo it runs after uppercasing, so it compares the uppercase strings. |
| `sorted()` | Orders elements by their natural order, such as alphabetical order for the sample strings. | Sort objects with a suitable `Comparable` implementation. |
| `sorted(comparator)` | Orders elements by the supplied `Comparator`. | Sort by name length or a field such as a student's age. |
| `peek(consumer)` | Observes elements that actually pass through this stage. It runs only as traversal demands. | Debug a pipeline. Use a terminal operation for actions that you need to perform on every result. |
| `skip(n)` | Drops the first `n` elements of an ordered stream. | Skip earlier results when paging. |
| `limit(n)` | Passes at most `n` elements, taking the first `n` on ordered streams. | Take a page, bound an infinite source, or take a small sample. |

The paging demo sorts the unique uppercase names to get `[AMAN, NEHA, PRIYA]`. It skips `AMAN` and returns `[NEHA, PRIYA]`. Placing `limit(2)` earlier would limit which elements reach the later stages. When sorting an infinite source, bound it **before** `sorted()` so the sort has finite input.

## 3. Terminal operations

Run [StreamTerminalDemo.java](StreamTerminalDemo.java). Each example gets a fresh stream.

| Operation | How it works | When to use it |
| --- | --- | --- |
| `count()` | Returns the number of elements as a `long`. | Count the results of a filter. |
| `min(comparator)`, `max(comparator)` | Finds the smallest or largest element by the given rule. An empty stream produces an empty `Optional`. | Find an extreme without sorting all the elements. |
| `anyMatch(predicate)` | Returns `true` as soon as an element matches. An empty stream gives `false`. | Ask whether at least one element meets a condition. |
| `allMatch(predicate)` | Returns `false` as soon as an element fails. An empty stream gives `true`. | Check whether every element meets a rule. |
| `noneMatch(predicate)` | Returns `false` as soon as an element matches. An empty stream gives `true`. | Check that no element violates a rule. |
| `findFirst()` | Returns the first element in encounter order when the stream has one, wrapped in an `Optional`. | Get the first matching item after a filter. |
| `findAny()` | Returns any available element as an `Optional`. An empty stream gives an empty result. | Get a match when its position is irrelevant, especially in parallel. |
| `toArray()`, `toArray(String[]::new)` | The first form returns an `Object[]`. The second uses an array constructor to return the requested type. | Pass results to an API that takes an array. |
| `forEach(consumer)` | Runs an action for each element. Parallel execution can run actions in varying order. | Print or perform another per-element action when order is irrelevant. |
| `forEachOrdered(consumer)` | Runs actions in encounter order when the stream has one. | Preserve list order even when the pipeline is parallel. |
| `reduce(identity, operator)` | Combines values using an associative operator and a neutral starting value. The identity is returned for empty input. | Sum using `reduce(0, Integer::sum)` or multiply using identity `1`. |
| `reduce(operator)` | Combines values with an associative operator and returns an `Optional`. | Combine elements when there is no suitable identity or when empty input should remain distinguishable. |
| `reduce(identity, accumulator, combiner)` | Builds a result that can have a different type. The accumulator folds an element into a partial result and the combiner merges partial results. | Sum string lengths as integers. For numeric sums, `mapToInt(...).sum()` is usually easier to read. |
| `collect(collector)` | Follows a reusable recipe to accumulate elements and finish a result. | Build a list, join text, or create groups with `Collectors`. |
| `collect(supplier, accumulator, combiner)` | Creates mutable containers, adds elements, and merges partial containers if the work splits. | Define a custom collection step, such as `collect(ArrayList::new, List::add, List::addAll)`. |

`Optional` represents a result that may be absent. In `findFirst().orElse("none")`, `orElse` gives you a fallback for an empty stream. Match and find operations can **short-circuit** by stopping once they have an answer. For parallel reductions, use an associative operation such as addition. The combiner must agree with the accumulator. Subtraction can give different answers when inputs are split differently.

## 4. Collectors

Run [StreamCollectorsDemo.java](StreamCollectorsDemo.java). A `Collector` is the recipe given to `collect()`. It describes how to accumulate elements, combine partial results, and produce the final result.

| Collector | How it works | When to use it |
| --- | --- | --- |
| `toList()` | Collects elements into a list in encounter order. The API leaves the exact list type unspecified. | Keep the pipeline results for later. In Java 8 write `collect(Collectors.toList())`. |
| `toSet()` | Collects unique items into a set with no promised iteration order. | Need unique elements and membership checks. |
| `toCollection(TreeSet::new)` | Uses the supplied collection factory. A `TreeSet` keeps sorted unique elements. | Choose a specific result type and its behavior. |
| `joining()`, `joining(delimiter)`, `joining(delimiter, prefix, suffix)` | Combines text with optional separators and surrounding text. | Make one readable string from several names. |
| `groupingBy(classifier)` | Maps each classification key to a list. Overloads accept a downstream collector and a map factory. | Group names by length, or use `groupingBy(String::length, counting())` to count each group. |
| `partitioningBy(predicate)` | Separates values into Boolean groups for passing and failing the condition. An overload accepts a downstream collector. | Split names into those with at least four letters and the rest. |
| `toMap(keyMapper, valueMapper, ...)` | Maps each element to a key and value. By default duplicate keys throw. A merge function decides what to do when keys repeat, and another overload accepts a map factory. | Build a lookup or count each repeated name. The demo uses `Integer::sum` to merge counts. |
| `counting()` | Counts elements as a `Long`. | Get a count per group when used inside `groupingBy`. |
| `summingInt`, `summingLong`, `summingDouble` | Extract a numeric value from each element and add the values. | Get a total, including totals inside groups. |
| `averagingInt`, `averagingLong`, `averagingDouble` | Extract numeric values and calculate the average as a `Double`. An empty input gives zero. | Get an average per group. |
| `summarizingInt`, `summarizingLong`, `summarizingDouble` | Compute count, min, max, sum, and average in one pass. | Produce a numeric summary, including one per group. |
| `minBy(comparator)`, `maxBy(comparator)` | Finds an extreme and wraps it in an `Optional`. | Find the shortest or longest value inside each group. |

Example from the demo: `Collectors.toMap(name -> name, name -> 1, Integer::sum, TreeMap::new)` creates a sorted map of name counts. Each occurrence starts at 1, so two occurrences of Raj merge into `Raj=2`.

## 5. Pipeline controls, traversal, and primitive streams

Run [StreamControlsDemo.java](StreamControlsDemo.java).

| Operation | How it works | When to use it |
| --- | --- | --- |
| `parallel()`, `sequential()` | Set how the pipeline will execute. The mode at terminal execution applies across the pipeline. | Switch execution mode explicitly. Use sequential execution for straightforward examples and parallel execution only for suitable work. |
| `isParallel()` | Reports the current execution mode without traversing elements. | Inspect the mode while learning or debugging. |
| `unordered()` | Removes the requirement to preserve encounter order. | Use when element order is irrelevant and ordering would constrain processing. |
| `iterator()` | Gives a terminal iterator so the caller can drive sequential traversal. | Hand a stream to code written around an iterator. |
| `spliterator()` | Gives a terminal traversal object that may split its work. `tryAdvance()` consumes one element if available. | Inspect or integrate with lower-level traversal code. |
| `onClose(handler)`, `close()` | Registers a handler and runs it when the stream is closed. Try-with-resources calls `close()` when leaving the block. | Release resources with streams backed by open files, such as `Files.lines(path)`. A terminal operation alone does not close such a stream. |
| `IntStream.range(start, end)`, `rangeClosed(start, end)` | Generate consecutive integers. `range` excludes the end and `rangeClosed` includes it. | Generate index ranges or numeric test data. `LongStream` has the same pair. |
| `boxed()` | Wraps primitive stream values as objects, such as `int` as `Integer`. | Collect numbers into a generic list or pass them to an object-based API. |
| `sum()`, `average()`, `summaryStatistics()` | Add values, return an optional average, or collect count, min, max, sum, and average together. | Use primitive aggregations after `mapToInt`, `mapToLong`, or `mapToDouble`. |

Keep actions inside parallel lambdas independent of shared mutable state. For example, collect results with `collect(Collectors.toList())` when you need a list. An ordinary `ArrayList` shared by calls to parallel `forEach` is susceptible to races.

## Practice in order

1. Generate the first five multiples of three with `iterate` and `limit`.
2. Predict the paging result if you move `limit(2)` before `skip(1)`. The result becomes `[NEHA]`.
3. Use `filter` and `findFirst` to get the first four-letter name in the terminal demo. It returns `Aman`.
4. Group names by their first character. Then use `counting()` to see how many names start with each character.
5. Use `IntStream.rangeClosed(1, 5).sum()` and check that it returns 15.
