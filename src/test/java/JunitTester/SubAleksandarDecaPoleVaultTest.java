package JunitTester;

import com.example.decathlon.deca.DecaPoleVault;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class SubAleksandarDecaPoleVaultTest {

    @Test
    void testPoleVault200cm() {
        DecaPoleVault poleVault = new DecaPoleVault();
        int actual = poleVault.calculateResult(200);
        int expected = 140;
        assertEquals(expected, actual);
    }

    @Test
    void testPoleVault500cm() {
        DecaPoleVault poleVault = new DecaPoleVault();
        int actual = poleVault.calculateResult(500);
        int expected = 907;
        assertEquals(expected, actual);
    }

    @Test
    void testPoleVault900cm() {
        DecaPoleVault poleVault = new DecaPoleVault();
        int actual = poleVault.calculateResult(900);
        int expected = 2341;
        assertEquals(expected, actual);
    }

    @Test
    void testValueTooLowMessage() {

        String simulatedInput = "200\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaPoleVault poleVault = new DecaPoleVault();

        int result = poleVault.calculateResult(1);

        String printed = output.toString();

        assertTrue(printed.contains("Value too low"));
        assertTrue(result > 0);
    }

    @Test
    void testValueTooHighMessage() {

        String simulatedInput = "500\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture console output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaPoleVault poleVault = new DecaPoleVault();

        int result = poleVault.calculateResult(1200);

        String printed = output.toString();

        assertTrue(printed.contains("Value too high"));
        assertTrue(result > 0);
    }
}
