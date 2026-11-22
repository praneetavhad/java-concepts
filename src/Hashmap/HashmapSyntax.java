package Hashmap;

import java.util.HashMap;
import java.util.Map;

public class HashmapSyntax {
    public static void main(String[] args) {
        // Map<KeyType, ValueType>. Each fruit name maps to a quantity.
        Map<String, Integer> quantities = new HashMap<>();

        System.out.println("Add and look up entries");
        quantities.put("apple", 3);
        quantities.put("banana", 2);
        quantities.put("apple", 5); // Replaces the quantity for apple.
        quantities.putIfAbsent("apple", 10); // Apple stays at 5.
        quantities.putIfAbsent("orange", 1); // Adds orange.

        System.out.println(quantities.get("apple")); // 5
        System.out.println(quantities.get("pear")); // null
        System.out.println(quantities.getOrDefault("pear", 0)); // 0
        System.out.println(quantities.containsKey("pear")); // false
        System.out.println(quantities.containsValue(2)); // true

        System.out.println("\nUpdate quantities");
        // Read the old count, or start at zero, then store the new count.
        quantities.put("pear", quantities.getOrDefault("pear", 0) + 1);
        // merge inserts 1 for an absent key or adds 1 to the existing count.
        quantities.merge("pear", 1, Integer::sum);
        quantities.replace("banana", 4); // Updates banana because it exists.
        System.out.println(quantities.get("pear")); // 2
        System.out.println(quantities.size()); // 4

        // HashMap does not guarantee the order of iteration.
        System.out.println("\nKeys with keySet()");
        for (String fruit : quantities.keySet()) {
            System.out.println(fruit);
        }

        System.out.println("\nValues with values()");
        for (int quantity : quantities.values()) {
            System.out.println(quantity);
        }

        System.out.println("\nKeys and values with entrySet()");
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("\nKeys and values with forEach()");
        quantities.forEach((fruit, quantity) -> System.out.println(fruit + " = " + quantity));

        System.out.println("\nRemove entries");
        System.out.println(quantities.remove("orange")); // 1, the removed value
        System.out.println(quantities.remove("apple", 3)); // false, apple maps to 5
        System.out.println(quantities.remove("apple", 5)); // true
        quantities.clear();
        System.out.println(quantities.isEmpty()); // true
    }
}
