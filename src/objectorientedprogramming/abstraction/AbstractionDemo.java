package objectorientedprogramming.abstraction;

public class AbstractionDemo {
    public static void main(String[] args) {
        Vehicle car = new Car();
        Vehicle electricCar = new EvCar();
        car.start();
        electricCar.start();
    }
}

abstract class Vehicle {
    // A common operation that each concrete vehicle must support.
    abstract void start();

    void fastenSeatBelt() {
        System.out.println("Seat belt fastened.");
    }
}

class Car extends Vehicle {
    @Override
    void start() {
        fastenSeatBelt();
        System.out.println("Starting the engine.");
    }
}

class EvCar extends Vehicle {
    @Override
    void start() {
        fastenSeatBelt();
        System.out.println("Turning on the electric motor.");
    }
}
