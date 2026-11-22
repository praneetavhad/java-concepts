package exception;

public class CustomExceptionDemo {
    public static void main(String[] args) {
        bookSeats(2, 5);
        bookSeats(6, 5);
    }

    private static void bookSeats(int requested, int available) {
        try {
            checkAvailability(requested, available);
            System.out.println("Booked " + requested + " seats.");
        } catch (NotEnoughSeatsException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void checkAvailability(int requested, int available)
            throws NotEnoughSeatsException {
        if (requested > available) {
            throw new NotEnoughSeatsException("Only " + available + " seats are available.");
        }
    }
}

class NotEnoughSeatsException extends Exception {
    NotEnoughSeatsException(String message) {
        super(message);
    }
}
