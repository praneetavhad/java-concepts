package objectorientedprogramming.inheritance;

public class InheritanceDemo {
    public static void main(String[] args) {
        Car car = new Car("Honda");
        car.start(); // Inherited from Vehicle.
        car.openTrunk();

        Truck truck = new Truck("Volvo");
        truck.start();
        truck.loadCargo();

        Bike bike = new Bike("Yamaha");
        bike.start();
        bike.parkOnStand();

        EvCar electricCar = new EvCar("Nissan");
        electricCar.start(); // Inherited through Car.
        electricCar.openTrunk(); // Inherited from Car.
        Chargeable chargeable = electricCar;
        chargeable.charge(); // Calls EvCar's implementation.
    }
}

class Vehicle {
    private final String brand;

    Vehicle(String brand) {
        this.brand = brand;
    }

    void start() {
        System.out.println(brand + " is ready to drive.");
    }
}

class Car extends Vehicle {
    Car(String brand) {
        super(brand); // Calls the Vehicle constructor.
    }

    void openTrunk() {
        System.out.println("Opening the trunk.");
    }
}

class Truck extends Vehicle {
    Truck(String brand) {
        super(brand);
    }

    void loadCargo() {
        System.out.println("Loading cargo.");
    }
}

class Bike extends Vehicle {
    Bike(String brand) {
        super(brand);
    }

    void parkOnStand() {
        System.out.println("Parking the bike on its stand.");
    }
}

interface Chargeable {
    void charge();
}

class EvCar extends Car implements Chargeable {
    EvCar(String brand) {
        super(brand);
    }

    @Override
    public void charge() {
        System.out.println("Charging the electric car.");
    }
}
