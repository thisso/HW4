import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackSolitaire {
    private final Card[] grid;
    private final Deck deck;
    private int discardsRemaining;
    private static final int GRID_SIZE = 20; //16 scoring + 4 discards
    private static final int SCORING_POSITIONS = 16; //represent the number of positions that count for scoring

    public BlackjackSolitaire() {
        grid = new Card[GRID_SIZE];
        deck = new Deck();
        discardsRemaining = 4;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        int filledPositions = 0;

        while (filledPositions < SCORING_POSITIONS) {
            Card currentCard = deck.drawCard();
            if (currentCard == null) {
                System.out.println("No more cards in deck!");
                break;
            }

            displayGame();
            System.out.println("Current card: " + currentCard);
            System.out.println("Discards remaining: " + discardsRemaining);

            int position = getValidPosition(scanner);
            grid[position - 1] = currentCard;

            if (position >= 17) {
                discardsRemaining--;
            }
            filledPositions++;
        }

        int finalScore = calculateScore();
        System.out.println("Game over! You scored " + finalScore + " points.");
    }

    private void displayGame() {
        System.out.println();
        // Row 1
        for (int i = 0; i < 5; i++) {
            System.out.print(formatCell(i) + "\t");
        }
        System.out.println();

        // Row 2
        for (int i = 5; i < 10; i++) {
            System.out.print(formatCell(i) + "\t");
        }
        System.out.println();

        // Row 3
        System.out.print("\t");
        for (int i = 10; i < 13; i++) {
            System.out.print(formatCell(i) + "\t");
        }
        System.out.println();

        // Row 4
        System.out.print("\t");
        for (int i = 13; i < 16; i++) {
            System.out.print(formatCell(i) + "\t");
        }
        System.out.println();
    }

    private String formatCell(int index) {
        if (grid[index] == null) {
            return String.valueOf(index + 1);  // Empty cell shows position number
        } else {
            return grid[index].toString();     // Filled cell shows card
        }
    }

    private int getValidPosition(Scanner scanner) {
        while (true) {
            System.out.print("Enter position (1-20): ");
            try {
                int position = Integer.parseInt(scanner.nextLine());
                if (position < 1 || position > 20) {
                    System.out.println("Invalid position! Please enter a number between 1 and 20.");
                    continue;
                }
                if (grid[position - 1] != null) {
                    System.out.println("Position already occupied, choose another position.");
                    continue;
                }
                if (position >= 17 && discardsRemaining <= 0) {
                    System.out.println("No discards remaining, choose a scoring position.");
                    continue;
                }
                return position;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
    }

    private int calculateScore() {
        int totalScore = 0;

        // Score rows
        totalScore += scoreHand(0, 1, 2, 3, 4);       // Row 1
        totalScore += scoreHand(5, 6, 7, 8, 9);       // Row 2
        totalScore += scoreHand(10, 11, 12);          // Row 3
        totalScore += scoreHand(13, 14, 15);          // Row 4

        // Score columns
        totalScore += scoreHand(0, 5);                // Column 1
        totalScore += scoreHand(1, 6, 10, 13);        // Column 2
        totalScore += scoreHand(2, 7, 11, 14);        // Column 3
        totalScore += scoreHand(3, 8, 12, 15);        // Column 4
        totalScore += scoreHand(4, 9);                // Column 5

        return totalScore;
    }

    private int scoreHand(int... positions) {
        List<Card> hand = new ArrayList<>();
        for (int pos : positions) {
            if (grid[pos] != null) {
                hand.add(grid[pos]);
            }
        }

        int sum = 0;
        int aces = 0;

        // Count non-ace cards first
        for (Card card : hand) {
            if (card.getValue() == 11) {
                aces++;
            } else {
                sum += card.getValue();
            }
        }

        // Add aces
        for (int i = 0; i < aces; i++) {
            if (sum + 11 <= 21) {
                sum += 11;
            } else {
                sum += 1;
            }
        }

        // Return points based on hand value
        if (sum > 21) return 0;  // Bust
        if (hand.size() == 2 && sum == 21) return 10;  // Blackjack
        if (sum == 21) return 7;  // 21 with 3+ cards
        if (sum == 20) return 5;
        if (sum == 19) return 4;
        if (sum == 18) return 3;
        if (sum == 17) return 2;
        return 1;  // 16 or less
    }
}
