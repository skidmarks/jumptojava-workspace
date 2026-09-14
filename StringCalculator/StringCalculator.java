import java.util.ArrayList;
import java.util.List;

public class StringCalculator {
    public int add(String input) {

        String delimiter = "[,:]";
        String numberPart = input;
        List<Integer> negatives = new ArrayList<>();

        if (input.isEmpty() || input ==  null) {
            return 0;
        }

        if (input.startsWith("//")) {
            int newlineIndex = input.indexOf("\n");
            delimiter = input.substring(2, newlineIndex);
            numberPart = input.substring(newlineIndex + 1);
        }

        String[] parts = numberPart.split(delimiter);

        int sum = 0;
        for (String numberString : parts) {
            int number = Integer.parseInt(numberString);
            if (number < 0) {
                negatives.add(number);
            }
            sum = sum + number;
        }

        String message = negatives.toString().replace("[", "").replace("]", "");

        if (!negatives.isEmpty()) {
            throw new RuntimeException("negative numbers not allowed: " + message);
        }

        return sum;
    }
}
