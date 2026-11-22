package Comparable_Comparator;

import java.util.Arrays;

public class Comparator {
    public static void main(String[] args) {
        Student[] students = {
                new Student(1, "Aman", 22),
                new Student(2, "Priya", 19),
                new Student(3, "Raj", 21)
        };

        java.util.Comparator<Student> byRoll = new java.util.Comparator<Student>() {
            @Override
            public int compare(Student first, Student second) {
                return Integer.compare(first.rollno, second.rollno);
            }
        };
        Arrays.sort(students, byRoll);
        System.out.println("Sorted by roll number:");
        for (Student student : students) {
            System.out.println(
                    student.rollno + " " + student.name + " " + student.age
            );
        }

        java.util.Comparator<Student> byName = new java.util.Comparator<Student>() {
            @Override
            public int compare(Student first, Student second) {
                return first.name.compareTo(second.name);
            }
        };

        Arrays.sort(students, byName);
        System.out.println("\nSorted by name:");
        for (Student student : students) {
            System.out.println(
                    student.rollno + " " + student.name + " " + student.age
            );
        }

        java.util.Comparator<Student> byAge = new java.util.Comparator<Student>() {
            @Override
            public int compare(Student first, Student second) {
                return Integer.compare(first.age, second.age);
            }
        };

        Arrays.sort(students, byAge);

        System.out.println("\nSorted by age:");
        for (Student student : students) {
            System.out.println(
                    student.rollno + " " + student.name + " " + student.age
            );
        }
    }
}
