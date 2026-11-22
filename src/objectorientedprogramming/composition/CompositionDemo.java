package objectorientedprogramming.composition;

public class CompositionDemo {
    public static void main(String[] args) {
        Car car = new Car();
        car.start();
    }
}

class Vehicle {
    void start() {
        System.out.println("Preparing the vehicle.");
    }
}

class Car extends Vehicle {
    // A Car is a Vehicle and has an Engine that it creates and owns.
    private final Engine engine = new Engine();

    @Override
    void start() {
        engine.start();
    }
}

class Engine {
    void start() {
        System.out.println("The car's engine is running.");
    }
}
