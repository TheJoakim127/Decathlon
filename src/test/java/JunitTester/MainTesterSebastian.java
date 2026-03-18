package JunitTester;


import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.deca.Deca100M;
import com.example.decathlon.deca.Deca400M;
import com.example.decathlon.deca.DecaHighJump;
import com.example.decathlon.deca.DecaPoleVault;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTesterSebastian {

    @Test
    public void testLongJumpNormalValue() {
        CalcTrackAndField calc = new CalcTrackAndField();
        int actual = calc.calculateField(
                0.14354,
                220,
                1.4,
                700
        );
        int expected = 814;

        assertEquals(expected, actual);
    }
    @Test
    public void testNegativeValueDistance() {
        CalcTrackAndField calc = new CalcTrackAndField();
        int result = calc.calculateField(
                0.14354,
                220,
                1.4,
                -700
        );
        assertEquals(0, result);
    }
    @Test
    public void testBetterResultHigherPoint() {
        CalcTrackAndField calc = new CalcTrackAndField();
        int result1 = calc.calculateField(
                0.14354,
                220,
                1.4,
                600);
        int result2 = calc.calculateField(
                0.14354,
                220,
                1.4,
                700
        );
        assertTrue(result2 > result1);
    }
    @Test
    public void testDecaPoleVaultNormalValue() {
        DecaPoleVault poleV = new DecaPoleVault();
        int result = poleV.calculateResult(1);

        assertEquals(1231, result);
    }
    @Test
    public void testDecaPoleVaultHigherJumpMorePoints() {
        DecaPoleVault poleV = new DecaPoleVault();
        int resultHigh = poleV.calculateResult(700);

        poleV = new DecaPoleVault();
        int resultLow = poleV.calculateResult(500);

        assertTrue(resultHigh > resultLow);
    }
    @Test
    public void testDeca400mNormalValue() {
        Deca400M run400m = new Deca400M();

        int actual = run400m.calculateResult(60);
        int expected = 413;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca400mLessTimeMorePoints() {
        Deca400M run400m = new Deca400M();
        int fastTime = run400m.calculateResult(50);

        run400m = new Deca400M();
        int slowTime = run400m.calculateResult(65);

        assertTrue(fastTime > slowTime);

    }
    @Test
    public void testDeca100mNormalValue() {
        Deca100M run100m = new Deca100M();

        int actual = run100m.calculateResult(10);
        int expected = 1096;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca100mLessTimeMorePoints() {
        Deca100M run100m = new Deca100M();
        int fastTime = run100m.calculateResult(10);

        run100m = new Deca100M();
        int slowTime = run100m.calculateResult(12);

        assertTrue(fastTime > slowTime);
    }

    @Test
    public void testDecaHighJumpNormalValue() {
        DecaHighJump HiJump = new DecaHighJump();

        int actual = HiJump.calculateResult(190);
        int expected = 714;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaHighJumpShorterHeightLessPoints() {
        DecaHighJump hiJump = new DecaHighJump();
        int higher = hiJump.calculateResult(200);

        hiJump = new DecaHighJump();
        int lower = hiJump.calculateResult(190);

        assertTrue(higher > lower);

    }

}

