// Gavin Dennis
// CS 3130 - Design and Analysis of Algorithms
// Dr. He
// Due 2024 October 27 23:59

import java.math.BigInteger;  // used for very large number evaluations

public class App {
    public static void evaluatePolynomial(int n, int x, boolean useBigInt) {
        /* demonstrates overflow issue by printing a negative result
        from a polynomial with positive numbers 
        
        used for overflow problem and fix */

        int intResult = 1;  // regular int that represents polynomial.
        BigInteger bigResult = new BigInteger("1");
        String pString = "";  // string that visually represents polynomial summation

        // create the polynomial by adding each value of x dependent on the size of n
        for (int i = 0; i <= n; i++) {
            int monomial = (int) (i * (Math.pow(x, i)));  // integer representation of current monomial being added (e.g. x, 2x^2, 10x^10, etc.)
            intResult += monomial;  // p += nx^n from 0 to n
            bigResult = bigResult.add(BigInteger.valueOf(monomial));
            
            // append pString for visual representation of polynomial
            if (i == 0) { pString += "1 + "; }
            else if (i == n) { pString += i + "x^" + i; }
            else if (i == 1) { pString += "x + ";}
            else { pString += i + "x^" + i + " + "; }
        }

        // print the results
        System.out.println(
            "\nThe polynomial is: " + pString + "\n" +
            "This evaluation result uses " + ((useBigInt) ? "BigInteger\n" : "standard integer\n") +
            "The result of the polynomial evaluation for n=" + n + " and x=" + x + " is: " +
            ((useBigInt) ? bigResult : intResult)
        );
    }
    public static void 

    public static void main(String[] args) throws Exception {
        // Project 1
        int n = 28, x = 180;

        // 2. observe the overflow problem
        // evaluate the polynomial with a standard integer result to try and cause integer overflow
        evaluatePolynomial(n, x, false);

        // 3. Eliminate the computation errors and overflow
        // evaluate the polynomial with BigInteger to get a proper result
        evaluatePolynomial(n, x, true);
    }
}
