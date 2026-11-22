package Java8.StreamVsLoop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexAndStateDemo {
    public static void main(String[] args) {
        int[] dailySales = {4, 7, 7, 10, 3};
        List<Integer> loopDays = new ArrayList<>();

        // Start at index 1 so every day has a previous day to compare with.
        for (int index = 1; index < dailySales.length; index++) {
            if (dailySales[index] > dailySales[index - 1]) {
                loopDays.add(index + 1); // Display day numbers starting at 1.
            }
        }

        List<Integer> streamDays = IntStream.range(1, dailySales.length)
                .filter(index -> dailySales[index] > dailySales[index - 1])
                .map(index -> index + 1)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("Daily sales: " + Arrays.toString(dailySales));
        System.out.println("Loop days with an increase: " + loopDays);
        System.out.println("Stream days with an increase: " + streamDays);
        System.out.println("First day to reach 15 cumulative sales: "
                + firstDayToReachTarget(dailySales, 15));
    }

    private static int firstDayToReachTarget(int[] dailySales, int target) {
        int total = 0;
        for (int index = 0; index < dailySales.length; index++) {
            total += dailySales[index];
            if (total >= target) {
                return index + 1;
            }
        }
        return -1; // The target was never reached.
    }
}
