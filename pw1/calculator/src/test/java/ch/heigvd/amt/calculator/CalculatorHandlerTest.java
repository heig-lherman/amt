package ch.heigvd.amt.calculator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorHandlerTest {

    @Test
    void main() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CalculatorHandler.main(new String[0]);
        assertEquals("""
                Invoking method: add(1, 2)
                Result of addition: 3
                Invoking method: subtract(4, 2)
                Result of subtraction: 2
                Invoking method: add(2147483647, 1)
                Overflow: -2147483648
                Result of overflow: 0
                """, out.toString().replaceAll(System.lineSeparator(), "\n"));
    }
}