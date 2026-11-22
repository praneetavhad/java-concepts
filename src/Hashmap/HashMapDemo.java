package Hashmap;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo {
    public static void main(String[] args) {
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setName("John");

        Employee e2 = new Employee();
        e2.setId(1);
        e2.setName("John");

        //comparing memory
        System.out.println("Shallow comparison " + (e1 == e2));

        //Comparing internal details
        System.out.println("Shallow comparison " + (e1.equals(e2)));

        Map<Employee, String> departments = new HashMap<>();

        departments.put(e1, "Engineering");

        System.out.println(departments.get(e1)); // Engineering
        System.out.println(departments.get(e2)); // Engineering


    }
}
