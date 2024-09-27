package ch.heigvd.amt.calculator;

/**
 * A calculator interface.
 */
public interface Calculator {

    /**
     * Adds two integers.
     *
     * @param a the first integer
     * @param b the second integer
     * @return the sum of the two integers
     */
    int add(int a, int b);

    /**
     * Subtracts two integers.
     *
     * @param a the first integer
     * @param b the second integer
     * @return the difference between the two integers
     */
    int subtract(int a, int b);
}