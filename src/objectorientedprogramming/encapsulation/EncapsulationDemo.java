package objectorientedprogramming.encapsulation;

public class EncapsulationDemo {
    public static void main(String[] args) {
        Car car = new Car();
        car.setSpeed(40);
        System.out.println("Speed: " + car.getSpeed() + " km/h");

        try {
            car.setSpeed(-10);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("Speed after the rejected update: " + car.getSpeed() + " km/h");
    }
}

class Vehicle {
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
}

class Car extends Vehicle {
}
