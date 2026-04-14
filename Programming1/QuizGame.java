import java.util.Scanner; // Required library for user input 

/**
 * QuizGame.java
 * Purpose: Simulates a multiple-choice quiz with score tracking and input validation.
 * Criteria met: Logical flow, proper variable initialization, and arithmetic accuracy. 
 */
public class QuizGame {

    public static void main(String[] args) {
        // Variable Declaration and Initialization 
        int correctAnswers = 0;
        final int TOTAL_QUESTIONS = 5; 
        Scanner input = new Scanner(System.in);

        System.out.println("=== Java Programming Quiz ===");
        System.out.println("Instructions: Type the letter (A, B, C, or D) and press Enter.\n");

        // --- Question 1 ---
        System.out.println("1. Which data type is used for whole numbers?");
        System.out.println("A) float\nB) int\nC) char\nD) boolean");
        correctAnswers += getValidatedAnswer(input, 'B');

        // --- Question 2 ---
        System.out.println("\n2. Which of these is a Reference data type?");
        System.out.println("A) String\nB) double\nC) byte\nD) long");
        correctAnswers += getValidatedAnswer(input, 'A');

        // --- Question 3 ---
        System.out.println("\n3. What is the value of 5 + 2 * 3?");
        System.out.println("A) 21\nB) 10\nC) 11\nD) 15");
        correctAnswers += getValidatedAnswer(input, 'C');

        // --- Question 4 ---
        System.out.println("\n4. Which statement is used to exit a switch case?");
        System.out.println("A) stop\nB) exit\nC) break\nD) terminate");
        correctAnswers += getValidatedAnswer(input, 'C');

        // --- Question 5 ---
        System.out.println("\n5. Which operator is used for 'equal to' in conditionals?");
        System.out.println("A) =\nB) !=\nC) ==\nD) <=");
        correctAnswers += getValidatedAnswer(input, 'C');

        // --- Logic and Computation ---
        // Uses 100.0 to ensure floating-point arithmetic for accuracy 
        double scorePercentage = (correctAnswers * 100.0) / TOTAL_QUESTIONS;

        // Final Output Display
        System.out.println("\n----------------------------");
        System.out.println("Final Score: " + correctAnswers + " / " + TOTAL_QUESTIONS);
        System.out.println("Percentage: " + scorePercentage + "%");
        System.out.println("----------------------------");

        input.close();
    }

    /**
     * Handles input validation and uses switch-case/if logic to verify answers. 
     */
    public static int getValidatedAnswer(Scanner sc, char correctAnswer) {
        char userChoice = ' ';
        boolean isValid = false;

        // Input Validation Loop: Ensures input is within range A-D 
        while (!isValid) {
            System.out.print("Your Answer: ");
            String rawInput = sc.next().toUpperCase();
            
            if (rawInput.length() == 1) {
                userChoice = rawInput.charAt(0);
                if (userChoice >= 'A' && userChoice <= 'D') {
                    isValid = true;
                } else {
                    System.out.println("Invalid range. Please choose A, B, C, or D.");
                }
            } else {
                System.out.println("Invalid input. Please enter only one letter.");
            }
        }

        // Logic Comparison using Switch 
        switch (userChoice) {
            case 'A': case 'B': case 'C': case 'D':
                if (userChoice == correctAnswer) {
                    return 1; // Correct
                }
                break;
        }
        return 0; // Incorrect
    }
}