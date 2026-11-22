package objectorientedprogramming.polymorphism;

public class PolymorphismDemo {
    public static void main(String[] args) {
        Vehicle[] vehicles = {new Car(), new Truck(), new Bike(), new EvCar()};

        for (Vehicle vehicle : vehicles) {
            // Java chooses the override based on the actual object at runtime.
            vehicle.move();
        }
    }
}

class Vehicle {
    void move() {
        System.out.println("The vehicle moves.");
    }
}

class Car extends Vehicle {
    @Override
    void move() {
        System.out.println("The car drives down the road.");
    }
}

class Truck extends Vehicle {
    @Override
    void move() {
        System.out.println("The truck hauls cargo.");
    }
}

class Bike extends Vehicle {
    @Override
    void move() {
        System.out.println("The bike cruises down the road.");
    }
}

class EvCar extends Car {
    @Override
    void move() {
        System.out.println("The electric car drives using battery power.");
    }
}
