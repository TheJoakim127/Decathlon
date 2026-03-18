package JunitTester;

import com.example.decathlon.deca.DecaShotPut;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainAleksandarDecaShotPutTest {

    @Test
    void testShotPut10m() {
        DecaShotPut shotPut = new DecaShotPut();
        int actual = shotPut.calculateResult(10);
        int expected = 486;
        assertEquals(expected, actual);
    }

    @Test
    void testShotPut15m() {
        DecaShotPut shotPut = new DecaShotPut();
        int actual = shotPut.calculateResult(15);
        int expected = 790;
        assertEquals(expected, actual);
    }

    @Test
    void testShotPut20m() {
        DecaShotPut shotPut = new DecaShotPut();
        int actual = shotPut.calculateResult(20);
        int expected = 1100;
        assertEquals(expected, actual);
    }

    @Test
    void testValueTooLowMessage() {

        String simulatedInput = "10\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaShotPut shotPut = new DecaShotPut();

        int result = shotPut.calculateResult(-5);

        String printed = output.toString();

        assertTrue(printed.contains("Value too low"));
        assertTrue(result > 0);
    }

    @Test
    void testValueTooHighMessage() {

        String simulatedInput = "12\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaShotPut shotPut = new DecaShotPut();

        int result = shotPut.calculateResult(35);

        String printed = output.toString();

        assertTrue(printed.contains("Value too high"));
        assertTrue(result > 0);
    }

}