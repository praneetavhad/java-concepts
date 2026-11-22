package Java8.Streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamIntermediateDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman", "Priya", "Neha");

        List<String> page = names.stream()
                .filter(name -> name.length() >= 4)
                .map(name -> name.toUpperCase(Locale.ROOT))
                .distinct()
                .sorted()
                .peek(name -> System.out.println("Reached paging: " + name))
                .skip(1)
                .limit(2)
                .collect(Collectors.toList());

        System.out.println("Page: " + page);
        System.out.println("By length: " + names.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList()));

        List<List<String>> teams = Arrays.asList(
                Arrays.asList("Raj", "Aman"), Arrays.asList("Priya", "Neha"));
        System.out.println("Flattened teams: " + teams.stream()
                .flatMap(List::stream).collect(Collectors.toList()));

        System.out.println("mapToInt sum: " + names.stream().mapToInt(String::length).sum());
        System.out.println("mapToLong sum: " + names.stream().mapToLong(String::length).sum());
        System.out.println("mapToDouble average: "
                + names.stream().mapToDouble(String::length).average().orElse(0));

        System.out.println("flatMapToInt: " + Stream.of(new int[]{1, 2}, new int[]{3})
                .flatMapToInt(IntStream::of).sum());
        System.out.println("flatMapToLong: " + Stream.of(new long[]{1, 2}, new long[]{3})
                .flatMapToLong(LongStream::of).sum());
        System.out.println("flatMapToDouble: " + Stream.of(new double[]{1.5}, new double[]{2.5})
                .flatMapToDouble(DoubleStream::of).sum());
    }
}
