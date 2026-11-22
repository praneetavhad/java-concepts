package Java8.Streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class StreamTerminalDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman");

        System.out.println("Count: " + names.stream().count());
        System.out.println("Min: " + names.stream().min(Comparator.naturalOrder()).orElse("none"));
        System.out.println("Max: " + names.stream().max(Comparator.naturalOrder()).orElse("none"));
        System.out.println("Any short name: " + names.stream().anyMatch(name -> name.length() == 3));
        System.out.println("All nonempty: " + names.stream().allMatch(name -> !name.isEmpty()));
        System.out.println("None empty: " + names.stream().noneMatch(String::isEmpty));
        System.out.println("First: " + names.stream().findFirst().orElse("none"));
        System.out.println("Any: " + names.parallelStream().findAny().orElse("none"));

        Object[] objects = names.stream().toArray();
        String[] strings = names.stream().toArray(String[]::new);
        System.out.println("Object array: " + Arrays.toString(objects));
        System.out.println("String array: " + Arrays.toString(strings));

        System.out.println("With identity: " + Stream.of(2, 3, 4).reduce(0, Integer::sum));
        System.out.println("Without identity: " + Stream.of(2, 3, 4).reduce(Integer::sum).orElse(0));
        int totalLength = names.parallelStream().reduce(
                0, (total, name) -> total + name.length(), Integer::sum);
        System.out.println("Total length: " + totalLength);

        List<String> copied = names.stream().collect(ArrayList::new, List::add, List::addAll);
        System.out.println("Collected with three arguments: " + copied);
        names.stream().forEach(name -> System.out.println("Visit: " + name));
        names.parallelStream().forEachOrdered(name -> System.out.println("In order: " + name));
    }
}
