package ch.heigvd.amt.calculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An invocation handler that logs method calls and delegates them to an original instance of a calculator.
 */
public class CalculatorHandler implements InvocationHandler {

    private final Calculator original;

    /**
     * Creates a new calculator handler.
     *
     * @param original the original calculator instance
     */
    public CalculatorHandler(Calculator original) {
        this.original = original;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.printf(
                "Invoking method: %s(%s)%n",
                method.getName(),
                Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "))
        );

        try {
            Object res = method.invoke(original, args);

            switch (method.getName()) {
                case "add": {
                    int x = (int) args[0];
                    int y = (int) args[1];
                    int r = (int) res;

                    if (((x ^ r) & (y ^ r)) < 0) {
                        System.out.printf("Overflow: %d%n", r);
                        return 0;
                    }

                    break;
                }
                case "subtract": {
                    int x = (int) args[0];
                    int y = (int) args[1];
                    int r = (int) res;

                    if (((x ^ y) & (x ^ r)) < 0) {
                        System.out.printf("Overflow: %d%n", r);
                        return 0;
                    }

                    break;
                }
            }

            return res;
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("An error occurred while invoking the method");
        }

        return 0;
    }

    /**
     * Creates a proxy instance of a calculator.
     *
     * @param original the original calculator instance
     * @return the proxy instance
     */
    public static Calculator createProxy(Calculator original) {
        return (Calculator) Proxy.newProxyInstance(
                original.getClass().getClassLoader(),
                new Class[]{ Calculator.class },
                new CalculatorHandler(original)
        );
    }

    /**
     * Demonstrate the use of the calculator handler by creating a proxy instance of a calculator.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create an original calculator instance
        Calculator originalCalculator = new BasicCalculator();

        // Create a proxy instance
        Calculator proxyCalculator = createProxy(originalCalculator);

        // Use the proxy instance
        System.out.println("Result of addition: " + proxyCalculator.add(1, 2));
        System.out.println("Result of subtraction: " + proxyCalculator.subtract(4, 2));
        System.out.println("Result of overflow: " + proxyCalculator.add(Integer.MAX_VALUE, 1));
    }
}
