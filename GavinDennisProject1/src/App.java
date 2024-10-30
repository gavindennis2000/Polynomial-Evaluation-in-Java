// Gavin Dennis
// CS 3130 - Design and Analysis of Algorithms
// Dr. He
// Due 2024 October 27 23:59

import java.math.BigInteger;  // used for very large number evaluations
import java.util.Random;  // for random number generation when writing to external file
import java.util.Arrays;  // for Arrays.fill
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
        for (int i = 0; i < 2; i++) {
            /* first array will be two numbers between 5 and 10
             * second will be two numbers between 5 and 10 times 10
             * third will be two numbers between 5 and 10 times 100
             */
            dataArr[i][0] = (5 + random.nextInt(10)) * (int)(Math.pow(10,i*2.3));
            dataArr[i][1] = (5 + random.nextInt(10)) * (int)(Math.pow(10,i*2));
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

    public static void evaluatePolynomial(String method, int n, int x) {
        /* evaluates polynomial using the specified method:
         *      overflow - uses brute force with ints and BigInteger to demonstrate overflow and fix
         *      bf - demonstrates brute force and function time
         *      rs - demonstrates repeated squaring and function time
         *      hm - demonstrates horner's method and function time
         */

        int intResult = 0;  // stores result for overflow demonstration with brute-force
        BigInteger result = BigInteger.valueOf(0);  // stores result for brute-force, repeated squaring, and horner
        long start, end, totalTime;  // long ints used to measure time in milliseconds of each algorithm

        // find start time for function
        start = System.currentTimeMillis();

        // run evaluation function
        switch(method) {
            case "overflow":
            default:
                intResult = bruteForceEvaluationInt(n, x);
                result = bruteForceEvaluationBigInt(n, x);
                break;

            case "bf":
                result = bruteForceEvaluationBigInt(n, x);
                break;

            case "rs":
                // create array to store powers of x when repeated squaring
                BigInteger[] xPower = new BigInteger[n+1];  // create array for storing all known values of x^n
                Arrays.fill(xPower, BigInteger.valueOf(-1));  // initialize each unknown element in xPower to -1
                xPower[0] = BigInteger.ZERO; xPower[1] = BigInteger.valueOf(x);  // update the values of x^0 and x^1
                result = repeatedSquaringEvaluation(n, x, xPower);
                break;

            case "hm":
                result = hornersMethodEvaluation(n, x, 1);
                break;
        }

        // find end time for function
        end = System.currentTimeMillis();
        totalTime = end - start;

        // print out the results
        // if testing for overflows
        if (method == "overflow") {
            // integer result
            System.out.println(
                "Evaluation for x=" + x + "; n=" + n + 
                " (With Integers):\n" + intResult
            );
            // print message if there is an integer overflow
            if (intResult < 0) { System.out.println("Integer overflow has occured!\n"); }
            // bigInteger result
            System.out.println(
                "Evaluation for x=" + x + "; n=" + n + 
                " (With BigIntegers):\n" + result
            );
        }
        // for everything else
        else {        
            System.out.println(
                "Evaluation for x=" + x + "; n=" + n + 
                ":\n" + result
            );

            // print out the algorithm time
            System.out.println(
                "\nTotal function time: " + totalTime + "ms\n"
            );
        }
    }

    public static int bruteForceEvaluationInt(int n, int x) {
        /* evaluates polynomial using brute force algorithm with integers */
        /* demonstrates overflow issue by printing a negative result
        from a polynomial with positive numbers when n and x are certain 
        values */

        int result = 1;  // regular integer that represents summation of polynomial.

        // create the polynomial by adding each value of x dependent on the size of n
        // i represents the value of the coefficient and power of the current monomial being evaluated
        for (int i = 0; i <= n; i++) {
            // manually evaluating the monomial nx^n with brute force for exponents
            int base = x, power = i, monomial = x;
            
            while (power > 1) {
                monomial *= base;
                power--;
            }
            monomial *= i;  // multiply by coefficient to get the final monomial to add
            
            result += monomial;  // p += nx^n from 0 to n
        }

        return result;
    }

    public static BigInteger bruteForceEvaluationBigInt(int n, int x) {
        /* evaluates polynomial using brute force algorithm with BigIntegers*/

        BigInteger result = new BigInteger("1");  // big integer that represents summation of polynomial

        // create the polynomial by adding each value of x dependent on the size of n
        // i represents the value of the coefficient and power of the current monomial being evaluated
        for (int i = 0; i <= n; i++) {
            // manually evaluating the monomial nx^n with brute-force for exponents
            int base = x, power = i;
            BigInteger monomial= BigInteger.valueOf(x);
            
            while (power > 1) {
                monomial = monomial.multiply(BigInteger.valueOf(base));
                power--;
            }
            monomial = monomial.multiply(BigInteger.valueOf(i));  // multiply by coefficient to get the final monomial to add
            
            result = result.add(monomial);
        }

        return result;
    }

    public static BigInteger repeatedSquaringEvaluation(int n, int x, BigInteger[] xPower) {
        /* evaluates polynomial using repeated squaring algorithm with BigIntegers */

        // i represents the value of the coefficient and power of the current monomial being evaluated
        if (n == 0) {
            return BigInteger.ONE;
        }
        else if (n == 1) {
            return xPower[1].add(repeatedSquaringEvaluation(n-1, x, xPower));
        }
        else {
            if (xPower[n] != BigInteger.valueOf(-1)) {  // if x^n is already known, return it
                return (xPower[n].multiply(BigInteger.valueOf(n))).add(repeatedSquaringEvaluation(n-1, x, xPower));
            }
            else {
                int power = 1;
                while (power*2 < n) {  // repeatedly square x until the power is greater than n
                    power *= 2;
                    xPower[power] = xPower[power/2].multiply(xPower[power/2]);
                }
                while (power < n) {
                    power ++;
                    xPower[power] = xPower[power-1].multiply(xPower[1]);
                }
                return (xPower[n].multiply(BigInteger.valueOf(n))).add(repeatedSquaringEvaluation(n-1, x, xPower));
            }
        }
    }

    public static BigInteger hornersMethodEvaluation(int n, int x, int i) {
        /* evaluates polynomial using horner's method algorithm with BigIntegers 
        P(x) = 1 + x + 2x^2 + ... + nx^n ->
        P(x) = 1 + x(1 + 2x + 3x^2 + ... + (n-1)x^n) ->
        P(x) = 1 + x(1 + x(2 + x(3 + ... + x( (n-1) + nx)))) */

        BigInteger result = BigInteger.valueOf(x);  // big integer that represents summation of polynomial

        // create the polynomial by adding each value of x dependent on the size of n
        // i represents the value of the coefficient and power of the current monomial being evaluated
        if (n == 0) {
            return BigInteger.ONE;
        }
        else if (n == 1) {
            result = (BigInteger.valueOf(x)).multiply(BigInteger.valueOf(n));
            return result.add(BigInteger.ONE);
        }
        else if (n > 1 && i < n) {

            return BigInteger.valueOf(x).multiply(BigInteger.valueOf(i).add(hornersMethodEvaluation(n, x, ++i)));
        }
        else if (n > 1 && i == n) {
            result = (BigInteger.valueOf(x)).multiply(BigInteger.valueOf(n));
            return result.add(BigInteger.ONE);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        /* int 'choice' picks which functions to run
        1: use brute-force to evaluate polynomial and cause integer overflow
        2: generate random numbers for x and n and write them to 'data.txt'
        3: evaluate polynomials using three algorithms to test time efficiency:
            brute-force with BigInteger
            repeated squaring
            horner's method
        4: exit the program */


        // System.exit(0);

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
                "\n\t\thorner's method" + 
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
                // then evaluate with BigInteger to show fix
                evaluatePolynomial("overflow", n, x);
            }
            
            else if (choice == 2) {
                // generate random numbers for x and n and write them to 'data.txt'
                cPrint("\nAttempting to generate data to 'data.txt'.\n");
                writeToExternalFile();
            }

            else if (choice == 3) {
                /* read integers into x and n arrays from 'data.txt', then use them
                to evaluate polynomials with brute-force, repeated squaring, and 
                horner's method */

                // generate random numbers to evaluate polynomials
                cPrint("\nAttempting to extract data from 'data.txt'");

                // read generated numbers into arrays for n and x
                int[] nArr = new int[2];
                int[] xArr = new int[2];

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
                cPrint("\nNumbers for x: " + xArr[0] + ", "+ xArr[1]);
                cPrint("Numbers for n: " + nArr[0] + ", "+ nArr[1] + "\n");

                // evaluate polynomials with each set of numbers
                for (int i = 0; i < nArr.length; i++) {

                    // introduce experiment
                    cPrint(
                        "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * " +
                        "\nRound " + (i+1) + ":\n"
                    );
                    // brute-force
                    cPrint("Evaluating with brute-force algorithm.\n");
                    evaluatePolynomial("bf", nArr[i], xArr[i]);

                    // repeated squaring
                    cPrint("Evaluating with repeated-squaring algorithm.\n");
                    evaluatePolynomial("rs", nArr[i], xArr[i]);

                    // horner
                    cPrint("Evaluating with horner's method algorithm.\n");
                    evaluatePolynomial("hm", nArr[i], xArr[i]);
                }
            }
        }

        // terminate the program
        input.close();
        System.exit(0);  // terminate the program
    }
}
