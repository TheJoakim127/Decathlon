package JunitTester;

import com.example.decathlon.deca.DecaLongJump;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class SubAleksandaecaLongJumpTest {
    @Test
    void testLongJump500cm(){
        DecaLongJump longJump = new DecaLongJump();
        int actual = longJump.calculateResult(500);
        int  expected = 379;
        assertEquals(expected, actual);
    }

    @Test
    void testLongJump700cm(){
        DecaLongJump longJump = new DecaLongJump();
        int actual = longJump.calculateResult(700);
        int  expected = 816;
        assertEquals(expected, actual);
    }
    @Test
    void testLongJump850cm(){
        DecaLongJump longJump = new DecaLongJump();
        int actual = longJump.calculateResult(850);
        int  expected = 1181;
        assertEquals(expected, actual);
    }

    @Test
    void testValueTooLowMessage() {

        String simulatedInput = "500\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture console output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaLongJump longJump = new DecaLongJump();

        int result = longJump.calculateResult(200);

        String printed = output.toString();

        assertTrue(printed.contains("Value too low"));
        assertTrue(result > 0);
    }

    @Test
    void testValueTooHighMessage() {

        String simulatedInput = "700\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        DecaLongJump longJump = new DecaLongJump();

        int result = longJump.calculateResult(1200);

        String printed = output.toString();

        assertTrue(printed.contains("Value too high"));
        assertTrue(result > 0);
    }
}
