package Comparable_Comparator;
import java.util.Arrays;

public class Comparable {
    public static void main(String[] args) {
        Student[] students = {
                new Student(1, "Aman", 22),
                new Student(2, "Priya", 19),
                new Student(3, "Raj", 21)
        };
        Arrays.sort(students);
        System.out.println("Natural ordering (age):");
        for (Student student : students) {
            System.out.println(
                    student.rollno + " " + student.name + " " + student.age
            );
        }
    }
}
