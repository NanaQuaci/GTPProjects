package Projects.Week1.assignment2;

import java.util.Scanner;

public class matrixMultiplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read dimensions of matrix A
        System.out.println("Enter the number of rows in matrix A: ");
        int n = scanner.nextInt();
        System.out.println("Enter the number of columns in matrix A: ");
        int m = scanner.nextInt();

        // Initialize matrix A
        int[][] A = new int[n][m];
        System.out.println("Enter the elements of matrix A:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = scanner.nextInt();
            }
        }

        int m2, p;
        while (true) {
            // Read dimensions of matrix B
            System.out.println("Enter the number of rows in matrix B: ");
            m2 = scanner.nextInt();
            System.out.println("Enter the number of columns in matrix B: ");
            p = scanner.nextInt();

            // Ensure the inner dimensions match for matrix multiplication
            if (m != m2) {
                System.out.println("Error: The number of columns in matrix A must be equal to the number of rows in matrix B. Please re-enter the dimensions of matrix B.");
            } else {
                break;
            }
        }

        // Initialize matrix B
        int[][] B = new int[m2][p];
        System.out.println("Enter the elements of matrix B:");
        for (int i = 0; i < m2; i++) {
            for (int j = 0; j < p; j++) {
                B[i][j] = scanner.nextInt();
            }
        }

        // Initialize matrix C
        int[][] C = new int[n][p];

        // Perform matrix multiplication
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < m; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        // Print the result
        System.out.println("Matrix C:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }
}