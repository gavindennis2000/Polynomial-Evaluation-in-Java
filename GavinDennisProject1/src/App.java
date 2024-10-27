// Gavin Dennis
// CS 3130 - Design and Analysis of Algorithms
// Dr. He
// Due 2024 October 27 23:59

import java.math.BigInteger;  // used for very large number evaluations
import java.util.Random;  // for random number generation when writing to external file
import java.io.File;  // for external file directory
import java.io.IOException;  // for throwing exception when file writing fails
import java.io.FileNotFoundException;  // for throwing exception when failing to open data.txt
import java.io.PrintWriter;  // for writing random numbers to data.txt
import java.util.Scanner;  // for reading data from external file

public class App {
    public static void cPrint(String message) {
        // replicates C style print message for easier Console println

        System.out.println(message);
    }

    public static void writeToExternalFile() {
        /* generates 3 sets of numbers to use for polynomial evaluation: small, medium, and large 
        and writes them to 'data.txt' */

        // generate 3 sets of random numbers for evaluation
        Random random = new Random();
        int[][] dataArr = new int[3][2];

        // populate array
        for (int i = 0; i < 3; i++) {
            /* first array will be two numbers between 5 and 10
             * second will be two numbers between 5 and 10 times 10
             * third will be two numbers between 5 and 10 times 100
             */
            dataArr[i][0] = (5 + random.nextInt(10)) * (int)(Math.pow(10,i));
            dataArr[i][1] = (5 + random.nextInt(10)) * (int)(Math.pow(10,i));
        }

        // try writing to file
        try {
            PrintWriter writer = new PrintWriter(new File("data.txt"));

            // write each element to a separate line
            for (int i = 0; i < dataArr.length; i++) {
                writer.println(dataArr[i][0]);
                writer.println(dataArr[i][1]);
            }

            writer.close();
        }
        catch(IOException e) {
            System.out.println("ERROR: Couldn't print to file.");
            e.printStackTrace();
        }
        System.out.println("Output to file successful.");
    }

    public static void bruteForceEvaluationInt(int n, int x) {
        /* evaluates polynomial using brute force algorithm with integers */
        /* demonstrates overflow issue by printing a negative result
        from a polynomial with positive numbers when n and x are certain 
        values */

        int result = 1;  // regular integer that represents summation of polynomial.

        // create the polynomial by adding each value of x dependent on the size of n
        // i represents the value of the coefficient and power of the current monomial being evaluated
        for (int i = 0; i <= n; i++) {
            // manually evaluating the monomial nx^n
            int base = x, power = i, monomial = x;
            
            while (power > 1) {
                monomial *= base;
                power--;
            }
            monomial *= i;  // multiply by coefficient to get the final monomial to add
            
            result += monomial;  // p += nx^n from 0 to n
        }

        // print out the result
        System.out.println(
            "Brute-force evaluation (integer) for x=" + x + "; n=" + n + 
            ";\n" + result
        );

        // print message if there is an integer overflow
        if (result < 0) { System.out.println("Integer overflow has occured!\n"); }
    }

    public static void bruteForceEvaluationBigInt(int n, int x) {
        /* evaluates polynomial using brute force algorithm with BigIntegers*/

        BigInteger result = new BigInteger("1");  // big integer that represents summation of polynomial

        // find start time for function
        long start = System.currentTimeMillis();

        // create the polynomial by adding each value of x dependent on the size of n
        // i represents the value of the coefficient and power of the current monomial being evaluated
        for (int i = 0; i <= n; i++) {
            // manually evaluating the monomial nx^n
            int base = x, power = i;
            BigInteger monomial= BigInteger.valueOf(x);
            
            while (power > 1) {
                monomial = monomial.multiply(BigInteger.valueOf(base));
                power--;
            }
            monomial = monomial.multiply(BigInteger.valueOf(i));  // multiply by coefficient to get the final monomial to add
            
            result = result.add(monomial);
        }

        long end = System.currentTimeMillis();
        long totalTime = end - start;

        // print out the result
        System.out.println(
            "Brute-force evaluation (BigInteger) for x=" + x + "; n=" + n + 
            ";\n" + result
        );

        // output end time to the screen
        System.out.println("\nTotal time for brute-force evaluation: " + totalTime + "ms\n");
    }

    public static void repeatedSquaringEvaluation(int n, int x) {
        /* uses repeated squaring to evaluate polynomial; exclusively uses big
        int as there is no need to demonstrate integer overflow */


    }

    public static void main(String[] args) throws Exception {
        /* int 'choice' picks which functions to run
        1: use brute-force to evaluate polynomial and cause integer overflow
        2: generate random numbers for x and n and write them to 'data.txt'
        3: evaluate polynomials using three algorithms to test time efficiency:
            brute-force with BigInteger
            repeated squaring
            horner's rule
        4: exit the program */

        int choice = 0;  // integer for user to input function choice
        Scanner input = new Scanner(System.in);  // scanner for user input

        while (choice != 4) {

            // print out message to user and get input
            cPrint(
                "\n............................................................................." +
                "\nSelect an option:" +
                "\n\t1: use brute-force to evaluate polynomial and cause integer overflow" +
                "\n\t2: generate random numbers for x and n and write them to 'data.txt'" + 
                "\n\t3: evaluate polynomials using three algorithms to test time efficiency:" +
                "\n\t\tbrute-force with BigInteger" + 
                "\n\t\trepeated squaring" + 
                "\n\t\thorner's rule" + 
                "\n\t4: exit the program\n"
            );

            choice = input.nextInt();

            if (choice == 1) {
                /* evaluate using brute-force method twice. The first time will use
                * integers and cause an overflow, leading to a negative result; the
                * second time will use BigIntegers to mitigate this issue
                */
                
                // message to user
                cPrint("\nDemonstrating integer overflow with brute-force evaluation.\n");

                // values for n and x to test integer overflow
                int n = 21, x = 181;  

                // evaluate the polynomial with a standard integer result to try and cause integer overflow
                bruteForceEvaluationInt(n, x);

                // evaluate the polynomial with BigInteger to eliminate the overflow issue
                bruteForceEvaluationBigInt(n, x);
            }
            
            else if (choice == 2) {
                // generate random numbers for x and n and write them to 'data.txt'
                cPrint("\nAttempting to generate data to 'data.txt'.\n");
                writeToExternalFile();
            }

            else if (choice == 3) {
                /* read integers into x and n arrays from 'data.txt', then use them
                to evaluate polynomials with brute-force, repeated squaring, and 
                horner's rule */

                // generate random numbers to evaluate polynomials
                cPrint("\nAttempting to extract data from 'data.txt'");

                // read generated numbers into arrays for n and x
                int[] nArr = new int[3];
                int[] xArr = new int[3];

                String path = "data.txt";
                try (Scanner scanner = new Scanner(new File(path))) {
                    for (int i = 0; i < nArr.length; i++) {
                        // read each line to get the numbers for n and x

                        nArr[i] = Integer.parseInt( scanner.nextLine() );
                        xArr[i] = Integer.parseInt( scanner.nextLine() );
                    }
                }
                catch(FileNotFoundException e) {
                    System.out.println("ERROR: Couldn't open data.txt. File may not exist.");
                    e.printStackTrace();
                }
                // print out numbers to check n and x are right
                cPrint("\nNumbers for n: " + nArr[0] + ", "+ nArr[1] + ", "+ nArr[2]);
                cPrint("\nNumbers for x: " + xArr[0] + ", "+ xArr[1] + ", "+ xArr[2]);

                // evaluate polynomials with each set of numbers
                for (int i = 0; i < nArr.length; i++) {
                    // brute-force
                    bruteForceEvaluationBigInt(nArr[i],xArr[i]);
                }
            }
        }

        // terminate the program
        input.close();
        System.exit(0);  // terminate the program
    }
}
