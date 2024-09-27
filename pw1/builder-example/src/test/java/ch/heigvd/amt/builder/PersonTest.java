package ch.heigvd.amt.builder;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void main() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Person.main(new String[0]);
        assertEquals("""
                Person{name='John Doe', age=42}
                """, out.toString().replaceAll(System.lineSeparator(), "\n"));
    }
}