# Object-oriented programming in Java

## Java keywords and syntax used in the demos

| Keyword or syntax | What it means in our code |
| --- | --- |
| `package` | Groups related classes. Each demo has its own package, so each can define a version of `Vehicle`. |
| `class` | Defines a type with fields and methods. `Car` is a class. |
| `new` | Creates an object by calling a constructor. `new Car("Honda")` creates a car. |
| `extends` | Names the parent class. `EvCar extends Car` lets `EvCar` inherit accessible behavior from `Car`. |
| `super` | Refers to the parent class. `super(brand)` calls the immediate parent constructor. The syntax `super.start()` calls a parent method. |
| `this` | Refers to the current object. In `this.speed = speed`, the left side is its field and the right side is the method parameter. |
| `abstract` | Marks a class you cannot instantiate directly or a method declared without a body. A concrete subclass must provide or inherit an implementation for each abstract method. |
| `interface` | Defines a type that classes can implement. `Chargeable` declares the `charge()` operation. |
| `implements` | Declares that a class fulfills an interface. `EvCar implements Chargeable` makes an `EvCar` usable wherever a `Chargeable` is expected. |
| `@Override` | An annotation that asks the compiler to check that a method overrides an inherited method or implements an interface method. |
| `public` | Makes a member accessible to callers that can access its class. The implementation of `Chargeable.charge()` is public. |
| `private` | Restricts access to the declaring class in these examples. Only `Vehicle` directly accesses its private `speed` field. |
| No access modifier | Gives the classes and members shown here package access. For example, classes in the inheritance package can call `Car.openTrunk()`. |
| `final` | Prevents reassignment after initialization. In `private final Engine engine`, it keeps the reference pointing to the same engine. |
| `static` | Makes a member belong to the class. Java can call the demo's `main` without creating a demo object. |
| `void` and `return` | A `void` method like `charge()` returns no value. `return speed;` sends the current speed back to the caller. |
| `int` and `String` | `int` is the type used for whole numbers such as speed. `String` is the class used for text such as a brand name. |
| `if`, `throw`, `try`, and `catch` | `if` checks whether speed is negative. `throw` signals the invalid input. The demo uses `try` and `catch` to handle that exception and continue. |
| `for (Vehicle vehicle : vehicles)` | An enhanced `for` loop that visits every vehicle in the array. |

A **constructor** initializes a new object. It has the same name as the class and has no return type. A **field** stores an object's data, and a **method** defines an action or returns information. In our inheritance demo, `Vehicle(String brand)` is a constructor, `brand` is a field, and `start()` is a method.

## Start here

Object-oriented programming (OOP) organizes a program around objects that hold data and perform actions. A **class** defines what an object of that type can store and do. An **object** is one instance of a class. For example, `Car car = new Car("Honda");` creates a car object and stores a reference to it in the variable `car`. Another call to `new Car("Honda")` creates a separate object, even with the same brand.

The four pillars are **encapsulation**, **inheritance**, **abstraction**, and **polymorphism**. Interfaces help express abstraction and enable polymorphism. Composition describes how objects can be built from other objects. Each demo has its own package, so read its classes together. Code marked as a revision example introduces syntax beyond the existing demos.

## 1. Encapsulation

**What it is.** Encapsulation groups data and the methods that manage it inside a class. The class decides what other code can read or change.

**Why use it?** When every update goes through a method, that method can keep the object in a valid state. A vehicle can enforce a speed limit rule in one place even if many callers try to change its speed.

In [EncapsulationDemo.java](encapsulation/EncapsulationDemo.java), `Vehicle` keeps its speed private and controls how callers update it.

```java
private int speed;

public int getSpeed() {
    return speed;
}

public void setSpeed(int speed) {
    if (speed < 0) {
        throw new IllegalArgumentException("Speed must be zero or greater.");
    }
    this.speed = speed;
}
```

`car.setSpeed(40)` stores 40. When the demo tries `car.setSpeed(-10)`, the setter throws before assigning the new value. The demo catches the exception, and `car.getSpeed()` still returns 40. The setter puts the validation rule in one place.

A **getter** reads a value and a **setter** updates it. Add a setter when callers should be able to change the value. In the inheritance demo, `brand` is set in the constructor and stored in a private final field, so the brand is fixed after construction. A method such as `accelerate(int amount)` can also offer a controlled way to update state.

## 2. Inheritance

**What it is.** Inheritance lets a subclass build on a parent class using `extends`. It models an **is-a** relationship. A `Truck` is a `Vehicle`.

**Why use it?** Put behavior that all vehicles share in `Vehicle`, then give each subclass its own behavior where needed. Here, every vehicle can use the same `start()` method, while a truck can also load cargo. A method that accepts a `Vehicle` can receive a `Truck`, a `Car`, or another subclass.

In [InheritanceDemo.java](inheritance/InheritanceDemo.java), `Car`, `Truck`, and `Bike` extend `Vehicle`. `EvCar` extends `Car`, so it can use accessible methods from both `Car` and `Vehicle`.

```java
EvCar electricCar = new EvCar("Nissan");
electricCar.start();     // Defined in Vehicle.
electricCar.openTrunk(); // Defined in Car.
```

Creating an `EvCar` passes the brand from the `EvCar` constructor to the `Car` constructor and then to the `Vehicle` constructor through `super(brand)`. `Vehicle` stores the brand and uses it when `start()` prints `Nissan is ready to drive.`

Constructors belong to their own classes. The `EvCar` constructor invokes the parent constructor to initialize the parent part of the object. The private `brand` field is accessed inside `Vehicle.start()`, so every subclass can use that behavior through the inherited method.

### Types of inheritance

| Type | Vehicle example | How it works in Java |
| --- | --- | --- |
| Single | `Car extends Vehicle` | One class has one direct parent class. |
| Multilevel | `EvCar extends Car`, and `Car extends Vehicle` | A class has a parent and a grandparent through an inheritance chain. |
| Hierarchical | `Car`, `Truck`, and `Bike` each extend `Vehicle` | Several subclasses share one parent class. |
| Multiple | An electric car implements both `Chargeable` and a hypothetical `Trackable` | A class can implement several interfaces. Java allows one direct superclass per class. |
| Hybrid | The actual `EvCar extends Car implements Chargeable` | A design can combine a class hierarchy with one or more interfaces. The term describes the structure. |

The inheritance demo shows single, multilevel, and hierarchical inheritance at the same time. They describe different parts of the same class hierarchy.

## 3. Abstraction

**What it is.** Abstraction gives callers a useful operation while keeping its implementation details inside the object. A caller can ask any vehicle to `start()` without looking up its startup procedure.

**Why use it?** One caller can work with different vehicle types through the same operation. The startup instructions can change inside `Car` or `EvCar` while the caller still uses `vehicle.start()`.

### Abstract classes and methods

An **abstract class** is a base type that cannot be instantiated directly. It can have fields, constructors, ordinary methods with bodies, and abstract methods. An **abstract method** declares an operation without a body. A concrete subclass must provide or inherit an implementation.

In [AbstractionDemo.java](abstraction/AbstractionDemo.java), an abstract class defines an operation that callers can use without knowing the details of how each vehicle starts.

```java
abstract class Vehicle {
    abstract void start();

    void fastenSeatBelt() {
        System.out.println("Seat belt fastened.");
    }
}
```

`Car` and `EvCar` each implement `start()` and reuse `fastenSeatBelt()`. The car prints `Starting the engine.` and the electric car prints `Turning on the electric motor.` This demo has both classes extend its abstract `Vehicle` directly.

```java
Vehicle electricCar = new EvCar();
electricCar.start();
```

The declared type is the abstract class `Vehicle`, and the created object is a concrete `EvCar`. Use an abstract class when related subclasses need a shared base with common code or data while supplying their own behavior for some operations.

**Encapsulation and abstraction answer different questions.** Encapsulation asks how the vehicle protects its speed. Abstraction asks what operation the caller needs to know about, such as `start()`.

## 4. Interfaces

**What it is.** An interface defines a type around a set of operations. A class that implements it can be used through that interface type.

**Why use it?** Code that needs to charge something can accept a `Chargeable` and call `charge()`. An electric bike could implement the same interface and work with that code even if it belonged to a different class hierarchy.

In [InheritanceDemo.java](inheritance/InheritanceDemo.java), the interface describes something an object can do.

```java
interface Chargeable {
    void charge();
}
```

`EvCar extends Car implements Chargeable` combines class inheritance with a capability. The interface method in this example is implicitly `public abstract`, so `EvCar` provides a `public void charge()` that prints `Charging the electric car.`

```java
Chargeable chargeable = electricCar;
chargeable.charge();
```

The variable refers to the same electric car. Its declared type gives the caller access to `charge()`. An abstract class can supply shared fields and behavior to its subclasses. An interface like `Chargeable` can describe the same capability for classes from different hierarchies. A Java class can extend one class and implement multiple interfaces.

| Question | Abstract class | Interface |
| --- | --- | --- |
| How does a class use it? | `extends Vehicle` | `implements Chargeable` |
| Can it have a constructor and per-object fields? | Yes | Interfaces have no constructors or per-object fields. |
| Can it provide method bodies? | Yes, through concrete methods | Yes, through methods such as `default` methods. Our interface only declares `charge()`. |
| When is it useful? | Related objects share a base and may share implementation or state. | Classes need to promise a capability. |

## 5. Polymorphism

**What it is.** Polymorphism lets a common operation work in more than one way. In Java revision material, **compile-time polymorphism** usually refers to method overloading, and **runtime polymorphism** refers to method overriding through a parent or interface reference.

### Compile-time polymorphism with overloading

**Why use it?** Give related actions the same name when callers can supply different inputs. This vehicle example is for revision and is separate from the Java demos.

```java
class Car {
    void start() {
        System.out.println("Starting the car.");
    }

    void start(String driverName) {
        System.out.println("Starting the car for " + driverName + ".");
    }
}
```

`car.start()` calls the zero-argument overload. `car.start("Aman")` calls the overload with a `String` parameter. The compiler selects the overload using the number and declared types of the arguments. Overloads can differ in the number, types, or order of parameter types. Changing only the return type does not create an overload.

### Runtime polymorphism with overriding

**Why use it?** A caller can work with different kinds of vehicles through one method call. Each vehicle supplies its own behavior, so the loop can stay the same as new vehicle types are added.

In [PolymorphismDemo.java](polymorphism/PolymorphismDemo.java), a loop makes the same method call on several types of vehicle.

```java
Vehicle[] vehicles = {new Car(), new Truck(), new Bike(), new EvCar()};

for (Vehicle vehicle : vehicles) {
    vehicle.move();
}
```

The declared type `Vehicle` gives the loop access to `move()`. At runtime, Java chooses the override based on the actual object. The `Truck` prints that it hauls cargo, and the `EvCar` prints that it drives using battery power. The `chargeable.charge()` call in the interface example uses the same idea.

An override implements the same operation with the same name and parameter types. In our demo, `Truck.move()` overrides `Vehicle.move()`. With `Vehicle vehicle = new Truck();`, the compiler checks that `Vehicle` offers `move()`, and the running program calls `Truck.move()`. The declared type also limits which methods you can call. A `Vehicle` variable in the inheritance demo cannot call `loadCargo()` directly, even when it refers to a `Truck`.

| Question | Overloading | Overriding |
| --- | --- | --- |
| What changes? | The parameter list | The implementation of the same operation in a subtype |
| When is the choice made? | The compiler selects the overload. | At runtime, Java selects the override of the chosen instance method. |
| Example | `start()` and `start(String driverName)` | `Vehicle.move()` and `Truck.move()` |

## 6. Composition

**What it is.** Composition builds an object from parts it owns. It describes a **has-a** relationship. In our example a car has an engine.

**Why use it?** Each part can handle a clear responsibility. The car coordinates startup and the engine handles starting the engine. This lets the code for the engine live in its own class.

In [CompositionDemo.java](composition/CompositionDemo.java), a car creates and owns an engine. The following members are inside `Car`.

```java
private final Engine engine = new Engine();

@Override
void start() {
    engine.start();
}
```

When the demo calls `car.start()`, the car delegates part of its work by calling `engine.start()`. The engine prints `The car's engine is running.` Each car creates its own engine, and the final field keeps that reference fixed. A `Car` **is a** `Vehicle` through inheritance and **has an** `Engine` through composition.

## Quick revision

| Concept | Question to ask | Answer from the demos |
| --- | --- | --- |
| Encapsulation | Who controls changes to the data? | `Vehicle.setSpeed()` validates the update. |
| Inheritance | What kind of thing is this object? | An `EvCar` is a `Car`, which is a `Vehicle`. |
| Abstraction | What does the caller need to know? | Call `start()`. Each vehicle handles the details. |
| Interface | What capability does this object promise? | A `Chargeable` can `charge()`. |
| Polymorphism | Which implementation runs? | `Vehicle vehicle = new Truck();` calls `Truck.move()` when you run `vehicle.move()`. |
| Composition | What parts does this object own? | A `Car` owns an `Engine`. |

### Check your understanding

1. The speed is 40 and you call `setSpeed(-10)`. What is the speed after the exception is caught?
2. Where are `start()` and `openTrunk()` defined in the inheritance demo?
3. Can you create `new Vehicle()` in the abstraction demo? Can you write `Vehicle car = new Car();`?
4. Do `start()` and `start(String driverName)` demonstrate overloading or overriding? What about `Truck.move()` and `Vehicle.move()`?
5. When `Car.start()` calls `engine.start()`, which object prints the message?

**Answers**

1. The speed stays at 40 because validation throws before assignment.
2. `start()` is defined in `Vehicle` and `openTrunk()` in `Car`. `EvCar` inherits both.
3. You can create a concrete `Car` and refer to it through `Vehicle`. Instantiating the abstract `Vehicle` directly is a compile-time error.
4. The two `start` signatures are overloads. `Truck.move()` overrides `Vehicle.move()`.
5. The `Engine` object prints it.
