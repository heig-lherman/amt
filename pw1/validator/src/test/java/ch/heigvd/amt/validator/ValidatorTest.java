package ch.heigvd.amt.validator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void main() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Validator.main(new String[0]);
        assertEquals("""
                Validating Person{username='john', age=42, email='john@example.com', phoneNumber='+41 79 123 45 67'}
                - username is valid
                - age is valid
                - email is valid
                - phoneNumber is valid
                Validating Person{username='null', age=200, email='john@example', phoneNumber='079 123 45 67'}
                - username is null
                - age is not in range [0.0, 100.0]
                - email does not match regex [a-z]+@[a-z]+\\.[a-z]+
                - phoneNumber does not match regex \\+[0-9]{2} [0-9]{2} [0-9]{3} [0-9]{2} [0-9]{2}
                """, out.toString().replaceAll(System.lineSeparator(), "\n"));
    }
}