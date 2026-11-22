package Java8.Lamdas;

public class LambdaBasicsDemo {
    public static void main(String[] args) {
        // The anonymous class and lambda implement the same interface method.
        IntOperation anonymousAddition = new IntOperation() {
            @Override
            public int apply(int first, int second) {
                return first + second;
            }
        };
        IntOperation lambdaAddition = (first, second) -> first + second;

        System.out.println("Anonymous class: " + anonymousAddition.apply(4, 3));
        System.out.println("Expression lambda: " + lambdaAddition.apply(4, 3));

        IntOperation multiply = (int first, int second) -> {
            int result = first * second;
            return result;
        };
        System.out.println("Block lambda: " + multiply.apply(4, 3));

        String message = "Hello from a captured local variable.";
        Runnable printMessage = () -> System.out.println(message);
        printMessage.run();
    }
}

@FunctionalInterface
interface IntOperation {
    int apply(int first, int second);
}
