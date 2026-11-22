package exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CheckedExceptionDemo {
    public static void main(String[] args) {
        Path path = Paths.get(args.length == 0 ? "src/Main.java" : args[0]);
        try {
            System.out.println("First line = " + readFirstLine(path));
        } catch (IOException exception) {
            System.out.println("Could not read " + path + ": " + exception.getMessage());
        }
    }

    private static String readFirstLine(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.readLine();
        }
    }
}
