package JunitTester;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.heptathlon.Hep100MHurdles;
import com.example.decathlon.heptathlon.Hep200M;
import com.example.decathlon.heptathlon.Hep800M;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MainTesterYiling {
    public MainTesterYiling() {
    }

    @Test
    public void test_formeln_calculateTrack_for_Hep100MHurdles() { //testa formeln
        CalcTrackAndField calc = new CalcTrackAndField();
        int result = calc.calculateTrack(
                9.23076,
                26.7,
                1.835,
                26.4);
        assertEquals(1, result);
    }

    @Test
    public void test_formeln_calculateField_for_HepHighJump() { // Testar formeln
        CalcTrackAndField calc = new CalcTrackAndField();
        int result = calc.calculateField(
                1.84523,
                75,
                1.348,
                76);
        assertEquals(1, result);
    }

    @Test
    public void testHep100MHurdles_shouldReturnMaxPoints_lowerLimit() { // Testar grenklassen
        Hep100MHurdles hep = new Hep100MHurdles();
        int result = hep.calculateResult(10);
        assertEquals(1617, result);
    }

    @Test
    public void testHep100MHurdles_shouldReturn0Points_upperLimit() { // Testar grenklassen
        Hep100MHurdles hep = new Hep100MHurdles();
        int result = hep.calculateResult(30);
        assertEquals(0, result);
    }

    @Test
    public void testHep100MHurdles_shouldReturn1Point() { // Testar grenklassen
        Hep100MHurdles hep = new Hep100MHurdles();
        int result = hep.calculateResult(26.4);
        assertEquals(1, result);
    }

    @Test
    public void testHep200metres_shouldReturnMaxPoints_lowerLimit() { // Testar grenklassen
        Hep200M hep = new Hep200M();
        int result = hep.calculateResult(20);
        assertEquals(1398, result);
    }

    @Test
    public void testHep200metres_shouldReturn0Points_upperLimit() { // Testar grenklassen
        Hep200M hep = new Hep200M();
        int result = hep.calculateResult(100);
        assertEquals(0, result);
    }

    @Test
    public void testHep200metres_shouldReturn1Point() { // Testar grenklassen
        Hep200M hep = new Hep200M();
        int result = hep.calculateResult(42.08);
        assertEquals(1, result);
    }

    @Test
    public void testHep800metres_shouldReturnMaxPoints_lowerLimit() { // Testar grenklassen
        Hep800M hep = new Hep800M();
        int result = hep.calculateResult(70);
        assertEquals(2026, result);
    }

    @Test
    public void testHep800metres_shouldReturn0Points_upperLimit() { // Testar grenklassen
        Hep800M hep = new Hep800M();
        int result = hep.calculateResult(250);
        assertEquals(1, result);
    }

    @Test
    public void testHep800metres_outofboundry() {
        Hep800M hep = new Hep800M();
        // nu går det inte att testa "Value too high" med vanlig assertEquals,
        // eftersom meddelandet bara skrivs ut i konsolen och inte returneras.
    }

}


