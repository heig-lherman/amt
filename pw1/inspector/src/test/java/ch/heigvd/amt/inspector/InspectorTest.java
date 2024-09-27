package ch.heigvd.amt.inspector;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {

    @Test
    void main() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Inspector.main(new String[0]);
        assertEquals("""
                Class name: ch.heigvd.amt.inspector.Dog
                Fields:
                 - age: 5
                 - name: Buddy
                Methods:
                 - bark
                 - getAge
                 - growl
                """, out.toString().replaceAll(System.lineSeparator(), "\n"));
    }
}