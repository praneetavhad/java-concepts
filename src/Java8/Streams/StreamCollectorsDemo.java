package Java8.Streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StreamCollectorsDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Priya", "Raj", "Aman", "Raj");
        System.out.println("List: " + names.stream().collect(Collectors.toList()));
        System.out.println("Set: " + names.stream().collect(Collectors.toSet()));
        System.out.println("TreeSet: " + names.stream().collect(Collectors.toCollection(TreeSet::new)));
        System.out.println("Joined: " + names.stream().collect(Collectors.joining(", ", "[", "]")));
        System.out.println("By length: " + names.stream().collect(
                Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList())));
        System.out.println("Count by length: " + names.stream().collect(
                Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting())));
        System.out.println("Split by condition: " + names.stream().collect(
                Collectors.partitioningBy(name -> name.length() >= 4)));
        System.out.println("Count by name: " + names.stream().collect(
                Collectors.toMap(name -> name, name -> 1, Integer::sum, TreeMap::new)));

        System.out.println("Total length: " + names.stream().collect(Collectors.summingInt(String::length)));
        System.out.println("Average length: " + names.stream().collect(Collectors.averagingInt(String::length)));
        System.out.println("Length statistics: " + names.stream().collect(Collectors.summarizingInt(String::length)));
        System.out.println("Shortest: " + names.stream().collect(
                Collectors.minBy(Comparator.comparingInt(String::length))).orElse("none"));
        System.out.println("Longest: " + names.stream().collect(
                Collectors.maxBy(Comparator.comparingInt(String::length))).orElse("none"));
    }
}
