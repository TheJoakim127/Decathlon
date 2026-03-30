package JunitTester;


import com.example.decathlon.common.InputName;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTesterJoakim {

    @Test
    public void testInputName() {
        String input = "Jonas\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        InputName name = new InputName();
        String actual = name.addCompetitor();

        assertEquals("Jonas", actual);
    }

    @Test
    public void testInputNameBigLetters() {
        String input = "JoNaS\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        InputName name = new InputName();
        String actual = name.addCompetitor();

        assertEquals("Jonas", actual);
    }
    @Test
    public void testInputNameNumberErrorMessage() {
        String input = "2\nJonas\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        InputName inputName = new InputName();
        inputName.addCompetitor();

        String output = outContent.toString();

        assertTrue(output.contains("Only use letters when putting in competitors name."));
    }



@Test
public void testAddCompetitor_validInput() {
    String input = "JoNaS\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    InputName Name = new InputName();
    String result = Name.addCompetitor();

    assertEquals("Jonas", result);
}


}




















