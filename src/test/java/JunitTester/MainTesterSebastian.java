package JunitTester;


import com.example.decathlon.deca.Deca100M;
import com.example.decathlon.deca.Deca400M;
import com.example.decathlon.deca.DecaHighJump;
import com.example.decathlon.deca.DecaPoleVault;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTesterSebastian {
    
    @Test
    public void testDecaPoleVaultNormalValue() {
        DecaPoleVault poleV = new DecaPoleVault();

        int actual = poleV.calculateResult(600);
        int expected = 1231;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaPoleVaultHighestLimit() {
        DecaPoleVault poleV = new DecaPoleVault();

        int actual = poleV.calculateResult(1000);
        int expected = 2722;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaPoleVaultLowestLimit() {
        DecaPoleVault poleV = new DecaPoleVault();

        int actual = poleV.calculateResult(2);
        int expected = 0;

        assertEquals(expected, actual);
    }

    @Test
    public void testDeca400mNormalValue() {
        Deca400M run400m = new Deca400M();

        int actual = run400m.calculateResult(60);
        int expected = 413;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca400mHighestLimit() {
        Deca400M run400m = new Deca400M();

        int actual = run400m.calculateResult(100);
        int expected = 0;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca400mLowestLimit() {
        Deca400M run400m = new Deca400M();

        int actual = run400m.calculateResult(20);
        int expected = 2698;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca100mNormalValue() {
        Deca100M run100m = new Deca100M();

        int actual = run100m.calculateResult(10);
        int expected = 1096;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca100mHighestLimit() {
        Deca100M run100m = new Deca100M();

        int actual = run100m.calculateResult(17.8);
        int expected = 1;

        assertEquals(expected, actual);
    }
    @Test
    public void testDeca100mLowestLimit() {
        Deca100M run100m = new Deca100M();

        int actual = run100m.calculateResult(5);
        int expected = 2640;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaHighJumpNormalValue() {
        DecaHighJump HiJump = new DecaHighJump();

        int actual = HiJump.calculateResult(80);
        int expected = 8;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaHighJumpHighestLimit() {
        DecaHighJump HiJump = new DecaHighJump();

        int actual = HiJump.calculateResult(100);
        int expected = 81;

        assertEquals(expected, actual);
    }
    @Test
    public void testDecaHighJumpLowestLimit() {
        DecaHighJump HiJump = new DecaHighJump();

        int actual = HiJump.calculateResult(0);
        int expected = 0;

        assertEquals(expected, actual);
    }

}

