import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final String WRONG_INPUT_FILE_MESSAGE = "Wrong file";
    public static final String INPUT_FILENAME = "input";

    public static void main(String[] args) throws IOException {
        final int result = extractInputLines().stream()
                .map(Main::toCard)
                .mapToInt(Card::getPoints)
                .sum();

        System.out.println("A-ha! The number of points the cards are worth is: " + result);
    }

    private static Card toCard(String inputLine) {
        String inputLineBody = inputLine.substring(inputLine.indexOf(':') + 1).trim();
        String[] winningNumbersAndHand = inputLineBody.split(" \\| ");

        return new Card(Arrays.stream(winningNumbersAndHand[0].trim().split("\\s+"))
                              .map(Integer::parseInt)
                              .collect(Collectors.toSet()),
                        Arrays.stream(winningNumbersAndHand[1].trim().split("\\s+"))
                              .map(Integer::parseInt)
                              .toList());
    }

    private static List<String> extractInputLines() throws IOException {
        try (InputStream resource = Main.class.getResourceAsStream(INPUT_FILENAME)) {
            if (resource == null) {
                throw new RuntimeException(WRONG_INPUT_FILE_MESSAGE);
            }

            return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))
                    .lines()
                    .toList();
        }
    }

    static class Card {
        Set<Integer> winningNumbers;
        List<Integer> numbersYouHave;
        int points;

        public Card(Set<Integer> winningNumbers, List<Integer> numbersYouHave) {
            this.winningNumbers = winningNumbers;
            this.numbersYouHave = numbersYouHave;

            calculatePoints();
        }

        public int getPoints() {
            return points;
        }

        private void calculatePoints() {
            int points = 0;
            boolean isFirstMatch = true;

            for (Integer number : numbersYouHave) {
                if (winningNumbers.contains(number)) {
                    if (isFirstMatch) {
                        points++;
                        isFirstMatch = false;
                        continue;
                    }

                    points *= 2;
                }
            }

            this.points = points;
        }
    }
}
