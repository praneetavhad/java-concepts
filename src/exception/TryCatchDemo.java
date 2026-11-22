package exception;

public class TryCatchDemo {
    public static void main(String[] args) {
        divide("12", "3");
        divide("12", "0");
        divide("twelve", "3");
    }

    private static void divide(String numerator, String denominator) {
        System.out.println("Trying " + numerator + " / " + denominator);
        try {
            int first = Integer.parseInt(numerator);
            int second = Integer.parseInt(denominator);
            System.out.println("Result = " + first / second);
        } catch (NumberFormatException exception) {
            System.out.println("Enter valid whole numbers.");
        } catch (ArithmeticException exception) {
            System.out.println("Cannot divide by zero.");
        } finally {
            System.out.println("Finished this attempt.");
        }
    }
}
