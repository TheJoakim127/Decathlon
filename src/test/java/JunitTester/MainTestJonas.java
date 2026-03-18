package JunitTester;

import com.example.decathlon.deca.DecaDiscusThrow;
import com.example.decathlon.deca.DecaHighJump;
import com.example.decathlon.deca.DecaLongJump;
import com.example.decathlon.deca.DecaShotPut;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTestJonas {

    @Test // Testar att ett giltigt värde (700 cm) ger korrekt beräknad poäng enligt formeln
    public void testLongJumpValidValue() {
        DecaLongJump longJump = new DecaLongJump();

        int result = longJump.calculateResult(700);

        assertEquals(814, result);
    }

    @Test // Testar att en bättre prestation (längre hopp) ger högre poäng än en sämre prestation
    public void testLongJumpBetterResultHigherScore() {
        DecaLongJump jump1 = new DecaLongJump();
        DecaLongJump jump2 = new DecaLongJump();

        int result1 = jump1.calculateResult(600);
        int result2 = jump2.calculateResult(700);

        assertTrue(result2 > result1);
    }

    @Test // Testa att vi får samma resultat även flera gånger
    public void testLongJumpSameInputSameOutput() {
        DecaLongJump jump1 = new DecaLongJump();
        DecaLongJump jump2 = new DecaLongJump();

        int result1 = jump1.calculateResult(700);
        int result2 = jump2.calculateResult(700);

        assertEquals(result1, result2);
    }


    @Test // Testar att ett längre diskuskast ger högre poäng
    public void testDiscusHigherThrowHigherScore() {
        DecaDiscusThrow discus1 = new DecaDiscusThrow();
        DecaDiscusThrow discus2 = new DecaDiscusThrow();

        int result1 = discus1.calculateResult(30.0);
        int result2 = discus2.calculateResult(40.0);

        assertTrue(result2 > result1);
    }

    @Test // Testar att ett högre höjdhopp ger högre poäng
    public void testHighJumpHigherJumpHigherScore() {
        DecaHighJump jump1 = new DecaHighJump();
        DecaHighJump jump2 = new DecaHighJump();

        int result1 = jump1.calculateResult(80);
        int result2 = jump2.calculateResult(90);

        assertTrue(result2 > result1);
    }

     @Test // Testar att ett längre kulstöt (bättre prestation) ger högre poäng
    public void testShotPutHigherThrowHigherScore() {
        DecaShotPut shot1 = new DecaShotPut();
        DecaShotPut shot2 = new DecaShotPut();

        int result1 = shot1.calculateResult(12.0);
        int result2 = shot2.calculateResult(14.0);

        assertTrue(result2 > result1);
    }

    @Test // Testar att samma objekt kan användas flera gånger och fortfarande ge korrekta resultat
    public void testShotPutSameObjectReuse() {
        DecaShotPut shot = new DecaShotPut();

        int result1 = shot.calculateResult(12.0);
        int result2 = shot.calculateResult(14.0);

        assertTrue(result2 > result1);
    }

    @Test // Fungerar om nytt objekt skapas varje gång
    public void testShotPutWorksWithNewObjects() {
        int result1 = new DecaShotPut().calculateResult(12.0);
        int result2 = new DecaShotPut().calculateResult(14.0);

        assertTrue(result2 > result1); // PASSAR
    }
}