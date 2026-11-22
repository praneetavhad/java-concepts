package Java8.Streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamControlsDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman");
        Stream<String> pipeline = names.stream().parallel().unordered();
        System.out.println("Parallel: " + pipeline.isParallel());
        Stream<String> sequential = pipeline.sequential();
        System.out.println("After sequential(): " + sequential.isParallel());
        System.out.println("Count: " + sequential.count());

        names.stream().iterator().forEachRemaining(name -> System.out.println("Iterator: " + name));
        names.stream().spliterator().tryAdvance(name -> System.out.println("Spliterator: " + name));

        // Leaving the try block calls close(), which runs the handler.
        try (Stream<String> tracked = names.stream().onClose(() -> System.out.println("Closed"))) {
            System.out.println("Tracked count: " + tracked.count());
        }

        System.out.println("Range: " + IntStream.range(1, 4).boxed().collect(Collectors.toList()));
        System.out.println("Inclusive sum: " + IntStream.rangeClosed(1, 4).sum());
        System.out.println("Average: " + IntStream.rangeClosed(1, 4).average().orElse(0));
        System.out.println("Statistics: " + IntStream.rangeClosed(1, 4).summaryStatistics());
    }
}
