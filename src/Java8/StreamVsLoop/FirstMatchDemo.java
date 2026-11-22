package Java8.StreamVsLoop;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FirstMatchDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Raj", "Priya", "Aman", "Neha");
        System.out.println("Loop first match: " + findWithLoop(names).orElse("none"));
        System.out.println("Stream first match: " + findWithStream(names).orElse("none"));
    }

    private static Optional<String> findWithLoop(List<String> names) {
        for (String name : names) {
            if (name.length() >= 4) {
                return Optional.of(name);
            }
        }
        return Optional.empty();
    }

    private static Optional<String> findWithStream(List<String> names) {
        return names.stream()
                .filter(name -> name.length() >= 4)
                .findFirst();
    }
}
