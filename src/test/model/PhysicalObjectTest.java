package model;

import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhysicalObjectTest {
    static final double DELTA = pow(10, -13);

    @Test
    public void testRoundUp() {
        assertEquals(0, PhysicalObject.roundUp(0));
        assertEquals(10.8, PhysicalObject.roundUp(10.8));
        assertEquals(123.1241, PhysicalObject.roundUp(123.12414)); //positive round down
        assertEquals(123.1242, PhysicalObject.roundUp(123.124151)); //positive round up
        assertEquals(-123.1241, PhysicalObject.roundUp(-123.12415)); //negative round up
        assertEquals(-123.1242, PhysicalObject.roundUp(-123.12416)); //negative round down
        assertEquals(1, PhysicalObject.roundUp(0.999999));
    }

    @Test
    public void testGamma() {
        assertEquals(1, PhysicalObject.gamma(0));
        assertEquals(1.25, PhysicalObject.gamma(0.6)); // 1/(1-0.6^2)^0.5
        assertEquals(1.25, PhysicalObject.gamma(-0.6)); // 1/(1-0.6^2)^0.5
        assertEquals(2, PhysicalObject.gamma(sqrt(3) / 2), DELTA);
    }

    @Test
    public void testLorentzTimeZeroVelocity() {
        assertEquals(0, PhysicalObject.lorentzTime(0, 0, 0));
        assertEquals(10, PhysicalObject.lorentzTime(10, 0, 0));
        assertEquals(0, PhysicalObject.lorentzTime(0, -10, 0));
        assertEquals(-50, PhysicalObject.lorentzTime(-50, 100, 0));
    }

    @Test
    public void testLorentzTimeNonZeroVelocity() {
        assertEquals(PhysicalObject.lorentzTime(0, 0, 0.6),
                0);
        assertEquals(PhysicalObject.lorentzTime(10, 0, 0.6),
                1.25 * 10);
        assertEquals(PhysicalObject.lorentzTime(0, 20, 0.6),
                1.25 * 20 * -0.6);
        assertEquals(PhysicalObject.lorentzTime(10.5, -5, 0.6),
                1.25 * (10.5 - (-5 * 0.6)));
        assertEquals(PhysicalObject.lorentzTime(10.5, -5, -0.6),
                1.25 * (10.5 - (-5 * -0.6)));
    }

    @Test
    public void testInverseLorentzTimeZeroVelocity() {
        assertEquals(0, PhysicalObject.inverseLorentzTime(0, 0, 0));
        assertEquals(10, PhysicalObject.inverseLorentzTime(10, 0, 0));
        assertEquals(0, PhysicalObject.inverseLorentzTime(0, -10, 0));
        assertEquals(-50, PhysicalObject.inverseLorentzTime(-50, 100, 0));
    }

    @Test
    public void testInverseLorentzTimeNonZeroVelocity() {
        assertEquals(0, PhysicalObject.inverseLorentzTime(0, 0, 0.6));
        assertEquals(1.25 * 10, PhysicalObject.inverseLorentzTime(10, 0, 0.6));
        assertEquals(1.25 * 20 * 0.6, PhysicalObject.inverseLorentzTime(0, 20, 0.6));
        assertEquals(1.25 * (10.5 + (-5 * 0.6)), PhysicalObject.inverseLorentzTime(10.5, -5, 0.6));
        assertEquals(1.25 * (10.5 + (-5 * -0.6)), PhysicalObject.inverseLorentzTime(10.5, -5, -0.6));
    }

    @Test
    public void testLorentzXZeroVelocity() {
        assertEquals(0, PhysicalObject.lorentzX(0, 0, 0));
        assertEquals(10, PhysicalObject.lorentzX(0, 10, 0));
        assertEquals(0, PhysicalObject.lorentzX(-10, 0, 0));
        assertEquals(100, PhysicalObject.lorentzX(-50, 100, 0));
    }

    @Test
    public void testLorentzXNonZeroVelocity() {
        assertEquals(0, PhysicalObject.lorentzX(0, 0, 0.6));
        assertEquals(1.25 * -0.6 * 10, PhysicalObject.lorentzX(10, 0, 0.6));
        assertEquals(1.25 * 20, PhysicalObject.lorentzX(0, 20, 0.6));
        assertEquals(1.25 * (-5 - (10.5 * 0.6)), PhysicalObject.lorentzX(10.5, -5, 0.6));
        assertEquals(1.25 * (-5 - (10.5 * -0.6)), PhysicalObject.lorentzX(10.5, -5, -0.6));
    }

    @Test
    public void testInverseLorentzXZeroVelocity() {
        assertEquals(0, PhysicalObject.inverseLorentzX(0, 0, 0));
        assertEquals(10, PhysicalObject.inverseLorentzX(0, 10, 0));
        assertEquals(0, PhysicalObject.inverseLorentzX(-10, 0, 0));
        assertEquals(100, PhysicalObject.inverseLorentzX(-50, 100, 0));
    }

    @Test
    public void testInverseLorentzXNonZeroVelocity() {
        assertEquals(0, PhysicalObject.inverseLorentzX(0, 0, 0.6));
        assertEquals(1.25 * 0.6 * 10, PhysicalObject.inverseLorentzX(10, 0, 0.6));
        assertEquals(1.25 * 20, PhysicalObject.inverseLorentzX(0, 20, 0.6));
        assertEquals(1.25 * (-5 - (10.5 * -0.6)), PhysicalObject.inverseLorentzX(10.5, -5, 0.6));
        assertEquals(1.25 * (-5 - (10.5 * 0.6)), PhysicalObject.inverseLorentzX(10.5, -5, -0.6));
    }
}
