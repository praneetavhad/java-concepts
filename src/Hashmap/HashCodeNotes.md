# HashMap, equals(), and hashCode()

These examples use [Employee.java](Employee.java) and [HashMapDemo.java](HashMapDemo.java). `Employee` currently uses `id` to decide whether two employees are equal.

## Comparing employees

```java
Employee e1 = new Employee();
e1.setId(1);
e1.setName("John");

Employee e2 = new Employee();
e2.setId(1);
e2.setName("John");

System.out.println(e1 == e2);                       // false
System.out.println(e1.equals(e2));                  // true
System.out.println(e1.hashCode() == e2.hashCode()); // true
```

`==` checks whether both references point to the same object. Each `new Employee()` creates a separate object. `equals()` applies the rule defined in `Employee`. Calling `e1.equals(e2)` directly runs that comparison without calling `hashCode()`.

```java
@Override
public boolean equals(Object other) {
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    if (this == other) {
        return true;
    }
    Employee employee = (Employee) other;
    return getId() == employee.getId();
}

@Override
public int hashCode() {
    return getId();
}
```

Choose the equality rule first, then implement a hash code consistent with it. The methods can appear in either order in the class.

## The equality and hash code contract

**If `a.equals(b)` is true, `a` and `b` must have the same hash code.** Unequal objects can share a hash code. A hash code must also stay consistent while the information used for equality stays the same.

| Implementation | What happens for `e1` and `e2` |
| --- | --- |
| Override only `equals()` to compare IDs | Equality returns `true`, but the inherited hash codes can differ. A `HashMap` lookup with `e2` can miss an entry stored under `e1`. |
| Override only `hashCode()` to return the ID | Both hashes are `1`, but the inherited `equals()` compares object identity. The map can store both as separate keys. |
| Override both using the ID | The hashes match and `equals()` returns `true`, so both objects identify the same entry. |

Define the overrides once in your class when you want data-based equality. The methods inherited from `Object` already work when each instance should have its own identity. Types like `String` and `Integer` already implement both methods. In a `Map<Integer, Employee>`, key lookup uses the `Integer`, so `Employee` can be stored as a value without defining either method for that purpose.

## Equality based on multiple fields

If your rule changes so that both ID and name must match, keep the type checks and cast above, then use this comparison. `Objects` comes from `java.util.Objects`.

```java
return id == employee.id && Objects.equals(name, employee.name);
```

`Objects.equals()` compares the names by value and handles `null`. A matching `hashCode()` can use both fields:

```java
@Override
public int hashCode() {
    return Objects.hash(id, name);
}
```

Returning just `id` still follows the contract because employees equal by both fields necessarily have the same ID. With an ID-only hash, `(1, "John")` and `(1, "Jane")` always share a hash. Using both fields can reduce such collisions. With the current ID-only **equality** rule, including `name` in the hash could break the contract because those two employees are equal even though their names differ.

## Using Employee as a HashMap key

Add the following inside `main` after creating `e1` and `e2`. The demo already imports `java.util.Map` and `java.util.HashMap`. `Employee` is the key type and `String` is the department value type.

```java
Map<Employee, String> departments = new HashMap<>();

departments.put(e1, "Engineering");
System.out.println(departments.get(e1)); // Engineering
System.out.println(departments.get(e2)); // Engineering

departments.put(e2, "Sales");
System.out.println(departments.get(e1)); // Sales
System.out.println(departments.size());  // 1
```

Set the ID before inserting an employee and keep it fixed while the employee is a key. The map stores the hash computed at insertion. Changing the ID can make a later `get()` or `remove()` miss the entry, even when you pass the original object. If equality uses the name too, keep both fields fixed.

## How HashMap works internally

A `HashMap` uses an array of **buckets**. Each entry stores its key, value, and an internal hash. Multiple entries can land in the same bucket.

1. **Compute the hash.** For a non-null key, `get()` and `put()` call `key.hashCode()`. OpenJDK mixes its bits with `h ^ (h >>> 16)` to help distribute keys. A `null` key uses an internal hash of `0`.
2. **Find the bucket.** With a table length that is a power of two, OpenJDK uses `(table.length - 1) & hash` as the bucket index. For a table of length `16`, employee IDs `1` and `17` both land in bucket `1`, even though their internal hashes differ.
3. **Find a matching key.** In the bucket, the map checks the stored hash first. If it matches, the map accepts the same key reference immediately or calls `incomingKey.equals(storedKey)` for a non-null incoming key. An empty bucket or a different stored hash requires no `equals()` call.
4. **Return or store the value.** `get()` returns the matching value or `null` if no key matches. `put()` updates the value for a matching key or adds an entry when no match exists.

In the example, `put(e1, "Engineering")` inserts into an empty bucket. `get(e1)` can match by reference. `get(e2)` reaches the same bucket, sees the matching hash, and calls `e2.equals(e1)`. `put(e2, "Sales")` finds that same entry and replaces its value.

### Collisions and resizing

- **Collisions.** Entries in the same bucket usually form a linked list. If a bucket grows long enough, OpenJDK can convert it into a red-black tree. The treeification threshold constant is `8`, and the table must have at least `64` buckets for conversion. If the table is smaller when treeification is attempted, it grows instead.
- **Resizing.** A default map starts with `16` buckets when its table is first allocated and uses a load factor of `0.75`. Its usual resize threshold is `16 × 0.75 = 12`, so adding a 13th distinct key triggers growth if the table still has 16 buckets. The table normally doubles, and the map redistributes entries using their stored hashes.
- **Performance.** With well-distributed hashes, `get()` takes expected constant time and `put()` takes expected amortized constant time. An individual insertion can take longer when it triggers a resize.

The hash mixing formula, bucket indexing, and thresholds above are OpenJDK implementation details. [OpenJDK HashMap source](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/HashMap.java)
