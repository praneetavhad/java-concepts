package Java8.Lamdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaInPracticeDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(Arrays.asList("Priya", "Raj", "Aman"));

        Comparator<String> byLength = (first, second) ->
                Integer.compare(first.length(), second.length());
        names.sort(byLength);
        System.out.println("Sorted by length: " + names);

        Predicate<String> hasAtLeastFourLetters = name -> name.length() >= 4;
        Function<String, Integer> nameLength = name -> name.length();
        Consumer<String> printName = name -> System.out.println("Name: " + name);
        Supplier<String> fallbackName = () -> "Guest";

        System.out.println("Priya has at least four letters: "
                + hasAtLeastFourLetters.test("Priya"));
        System.out.println("Length of Raj: " + nameLength.apply("Raj"));
        System.out.println("Fallback: " + fallbackName.get());
        names.forEach(printName);
    }
}
