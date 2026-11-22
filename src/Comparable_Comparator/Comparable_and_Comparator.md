# Comparable and Comparator in Java

## What a comparison returns

Both interfaces use an `int` to say how two values should be ordered.

| Result | Meaning |
| --- | --- |
| Negative | The first value comes before the second. |
| Zero | The two values tie under this ordering. |
| Positive | The first value comes after the second. |

Only the sign matters. Two different students can tie when the rule compares only their ages.

## Comparable defines a natural order

`java.lang.Comparable<T>` is implemented by the class being sorted. Its method is `int compareTo(T other)`. In `a.compareTo(b)`, `a` is the current object (`this`) and `b` is the argument.

```java
class Student implements java.lang.Comparable<Student> {
    int age;

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.age, other.age);
    }
}
```

Given a `Student[] students`, `Arrays.sort(students)` uses that natural order and puts younger students first. The snippet shows the parts of `Student` that define this order.

## Comparator defines an order you can choose

`java.util.Comparator<T>` represents a sorting rule. Its main method is `int compare(T first, T second)`. Both objects are arguments, so the rule can be defined outside the class being sorted. The same type can have several comparators.

```java
java.util.Comparator<Student> byRoll = new java.util.Comparator<Student>() {
    @Override
    public int compare(Student first, Student second) {
        return Integer.compare(first.rollno, second.rollno);
    }
};

Arrays.sort(students, byRoll);
```

Your `byRoll` example uses an anonymous class to implement `compare` where the rule is declared. It puts lower roll numbers first. For example, comparing Aman (roll number 1) with Priya (roll number 2) returns a negative value, so Aman comes first under this rule.

| | Comparable | Comparator |
| --- | --- | --- |
| Where the rule lives | In the class being sorted | In a separate rule supplied by the caller |
| Method call | `first.compareTo(second)` | `rule.compare(first, second)` |
| Array sort | `Arrays.sort(items)` | `Arrays.sort(items, rule)` |
| When to use it | The type has a meaningful natural order | The caller needs a specific order |

## How it works in this package

### Student.java

`Student` implements `java.lang.Comparable<Student>`. Its `compareTo` returns `Integer.compare(this.age, other.age)`, so younger students come first. For example, `priya.compareTo(aman)` compares 19 with 22 and returns a negative value. Two students of the same age tie under this rule.

### Comparable.java

The demo creates Aman (22), Priya (19), and Raj (21). It calls `Arrays.sort(students)`, which uses `Student.compareTo`. The printed order is **Priya → Raj → Aman**.

### Comparator.java

The demo creates the same three students and defines three anonymous `java.util.Comparator<Student>` instances. Each call to `Arrays.sort(students, rule)` sorts the same array in place with the supplied rule.

| Rule | Comparison inside `compare` | Printed order |
| --- | --- | --- |
| `byRoll` | `Integer.compare(first.rollno, second.rollno)` | Aman → Priya → Raj |
| `byName` | `first.name.compareTo(second.name)` | Aman → Priya → Raj |
| `byAge` | `Integer.compare(first.age, second.age)` | Priya → Raj → Aman |

The roll number and name sorts match because this sample assigns roll numbers 1, 2, and 3 to names already in alphabetical order. The name rule calls `String.compareTo` on the names. The age rule yields the same order as `Student.compareTo` because both compare ages in ascending order.

The two demo classes are named `Comparable` and `Comparator`. The source spells out `java.lang.Comparable<Student>` and `java.util.Comparator<Student>` to identify the Java interfaces explicitly.
