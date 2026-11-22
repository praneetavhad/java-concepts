package Java8.StreamVsLoop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FilterMapSumDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman", "Neha");

        List<String> loopNames = new ArrayList<>();
        int loopTotal = 0;
        for (String name : names) {
            if (name.length() >= 4) {
                loopNames.add(name.toUpperCase(Locale.ROOT));
                loopTotal += name.length();
            }
        }

        List<String> streamNames = names.stream()
                .filter(name -> name.length() >= 4)
                .map(name -> name.toUpperCase(Locale.ROOT))
                .collect(Collectors.toList());
        int streamTotal = names.stream()
                .filter(name -> name.length() >= 4)
                .mapToInt(String::length)
                .sum();

        System.out.println("Loop names: " + loopNames);
        System.out.println("Stream names: " + streamNames);
        System.out.println("Loop total length: " + loopTotal);
        System.out.println("Stream total length: " + streamTotal);
    }
}
