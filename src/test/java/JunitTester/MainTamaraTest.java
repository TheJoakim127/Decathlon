package JunitTester;

import com.example.decathlon.common.CalcTrackAndField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTamaraTest {

        @Test
        public void testHighJumpVlue1() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(1.84523, 75, 1.348, 76);
            assertEquals(1, result);

        }

        @Test
        public void testHighJumpVluel0() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(1.84523, 75, 1.348, 75);
            assertEquals(0, result);


        }

        @Test
        public void testHighJumpVluelHigh() {
            CalcTrackAndField calc = new CalcTrackAndField();

            int result = calc.calculateField(1.84523, 75, 1.348, 280);

            assertEquals("Value too high", result);
        }

        @Test
        public void testHeptJavelinThrowVlue0() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(15.9803, 3.8, 1.04, 0);
            assertEquals(0, result);
        }

        @Test
        public void testHeptJavelinThrowVlue90() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(15.9803, 3.8, 1.04, 90);
            assertEquals(1646, result);
        }

        @Test
        public void testHeptJavelinThrowVlue101() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(15.9803, 3.8, 1.04, 101);
            assertEquals("Value too high", result);
        }

        @Test
        public void testHeptLongJumpVlue0() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(0.188807, 210, 1.41, 0);
            assertEquals(0, result);
        }

        @Test
        public void testHeptLongJumpVlue300() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(0.188807, 210, 1.41, 300);
            assertEquals(107, result);
        }

        @Test
        public void testHeptLongJumpVlue500() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(0.188807, 210, 1.41, 500);
            assertEquals("Value too high", result);
        }

        @Test
        public void testHeptShotPutVlue0() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(56.0211, 1.5, 1.05, 0);
            assertEquals("Value too low", result);
        }

        @Test
        public void testHeptShotPutVlue5() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(56.0211, 1.5, 1.05, 5);
            assertEquals(208, result);
        }
        @Test
        public void testHeptShotPutVlue101() {
            CalcTrackAndField calc = new CalcTrackAndField();
            int result = calc.calculateField(56.0211, 1.5, 1.05, 101);
            assertEquals("Value too high", result);
        }
    }

