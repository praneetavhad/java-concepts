package Java8.Streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamSourcesDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman");
        System.out.println("List: " + names.stream().
                                    collect(Collectors.toList()));
        System.out.println("Array: " + Arrays.stream(new String[]{"Raj", "Aman"}).count());
        System.out.println("Empty: " + Stream.empty().count());
        System.out.println("Fixed values: " + Stream.of("Raj", "Aman").count());

        Stream.Builder<String> builder = Stream.builder();
        builder.add("Raj").add("Aman");
        System.out.println("Builder: " + builder.build().collect(Collectors.toList()));

        System.out.println("Concatenated: " + Stream.concat(Stream.of("Raj"), Stream.of("Aman"))
                .collect(Collectors.toList()));
        System.out.println("Iterated: " + Stream.iterate(2, number -> number + 2)
                .limit(4).collect(Collectors.toList()));
        System.out.println("Generated: " + Stream.generate(() -> "Guest")
                .limit(3).collect(Collectors.toList()));
    }
}
