package Comparable_Comparator;

class Student implements java.lang.Comparable<Student> {
    int rollno;
    String name;
    int age;

    Student(int rollno, String name, int age){
        this.rollno = rollno;
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Student other){
        return Integer.compare(this.age, other.age);
    }





}
