package Projects.Week1.assignment1;

import java.util.Arrays;
import java.util.Scanner;

public class statisticsOfGrades {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //variable declaration
        int N;
        int maxGrade, minGrade;
        double averageGrade;
        int sumOfGrades = 0;
        int[] stats = new int[5];

        //taking number of students from user
        System.out.println("Enter the number of students: ");
        N = scanner.nextInt();

        int[] scores = new int[N];

        //taking input for scores of each student
        System.out.println("Enter the scores of all the students: ");
        for (int i = 0; i < N; i++) {
            scores[i] = getValidScore(scanner, i+1);
        }

        maxGrade = Arrays.stream(scores).max().getAsInt();
        minGrade = Arrays.stream(scores).min().getAsInt();

        for (int score : scores) {
            sumOfGrades += score;

        //categorize grades into different ranges
        if (score > 80) {
            stats[4]++;
        } else if (score >= 61) {
            stats[3]++;
        } else if (score >= 41) {
            stats[2]++;
        } else if (score >= 21) {
            stats[1]++;
        } else {
            stats[0]++;
        }
    }

        //compute average and display alongside min and max values
        averageGrade = (double) sumOfGrades / N;
            System.out.println("Values:\n");
            System.out.println("The maximum grade is " + maxGrade);
            System.out.println("The minimum grade is " + minGrade);
            System.out.println("The average grade is " + averageGrade);

        //Display Graph
        System.out.println("\nGraph:\n");
        int maxHeight = getMax(stats);
        for (int i = maxHeight; i > 0; i--){
            System.out.printf("%3d >", i);
            for (int stat : stats) {
                if (stat >= i) {
                    System.out.print("  ####### ");
                } else {
                    System.out.print("          ");
                }
            }

            System.out.println();
        }
        System.out.println("    +-----------+---------+---------+---------+---------+");
        System.out.println("     I    0-20   I  21-40  I   41-60  I  61-80  I  81-100 I");
    }

    //making sure only valid data entry is made for each student
    private static int getValidScore(Scanner scanner, int studentNumber) {
        while (true) {
            System.out.printf("Enter the score for student %d (0-100): ", studentNumber);
            try {
                int score = scanner.nextInt();
                if (score >= 0 && score <= 100) {
                    return score;
                } else {
                    System.out.println("Invalid input. Please enter a score between 0 and 100.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    //method to find the height of the graph
    private static int getMax(int[] array) {
        int max = array[0];
        for (int value : array){
            if (value > max){
                max = value;
            }
        }
        return max;
    }
}

