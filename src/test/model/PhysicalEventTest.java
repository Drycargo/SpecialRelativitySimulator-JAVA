package model;

import model.exceptions.EmptyNameException;
import model.exceptions.FrameConstructException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicalEventTest {
    PhysicalEvent testPhysicalEvent;
    RefFrame stationaryFrame;
    static final double DELTA = pow(10, -13);

    @BeforeEach
    public void SetUp() {
        try {
            stationaryFrame = new RefFrame("stationaryFrame", 0, 0, 0, 0);
            testPhysicalEvent = new PhysicalEvent("testEvent", 0, 0, stationaryFrame);
        } catch (FrameConstructException frameConstructException) {
            fail("Test initialization failed.");
        }
    }

    @Test
    public void constructEventSuccess() {
        try {
            PhysicalEvent newPhysicalEvent = new PhysicalEvent("newEvent", 10, 20, stationaryFrame);
            assertEquals(20, newPhysicalEvent.getOccurTime());
            assertEquals(10, newPhysicalEvent.getInitialPosX());
            // current data not initialized
            assertEquals(0, newPhysicalEvent.getCurrentOccurTime());
            assertEquals(0, newPhysicalEvent.getCurrentOccurX());
            assertFalse(newPhysicalEvent.hasOccurred());
            assertEquals(2, stationaryFrame.getEventsSize());
            assertEquals(newPhysicalEvent, stationaryFrame.findEventByName("newEvent"));
            assertEquals(testPhysicalEvent, stationaryFrame.findEventByName("testEvent"));
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }
    }

    @Test
    public void constructEventWithEmptyName() {
        try {
            PhysicalEvent physicalEventEmptyName = new PhysicalEvent("", 10, 20, stationaryFrame);
            fail("EmptyNameException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            //expected
        }
    }

    @Test
    public void testViewInGivenFrameAllPropertiesZero() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 0, 0);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(10, physicalEvent1.getCurrentOccurX());
            assertEquals(20, physicalEvent1.getCurrentOccurTime());
            assertFalse(physicalEvent1.hasOccurred());

            frame1.setProperTime(20);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(10, physicalEvent1.getCurrentOccurX());
            assertEquals(20, physicalEvent1.getCurrentOccurTime());
            assertTrue(physicalEvent1.hasOccurred());

        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInGivenFrameStartSame() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 0, 0.6);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(1.25 * (10 - 20 * 0.6), physicalEvent1.getCurrentOccurX());
            assertEquals(1.25 * (20 - 10 * 0.6), physicalEvent1.getCurrentOccurTime());
            assertFalse(physicalEvent1.hasOccurred());

            frame1.setProperTime(20);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(1.25 * (10 - 20 * 0.6), physicalEvent1.getCurrentOccurX());
            assertEquals(1.25 * (20 - 10 * 0.6), physicalEvent1.getCurrentOccurTime());
            assertTrue(physicalEvent1.hasOccurred());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInGivenFrameArbitrary() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 100, -10, 10, 0.6);
            RefFrame frame2 = new RefFrame("frame2", -6, -10, 0, 0.6, frame1);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 10 / PhysicalObject.gamma(0.6), frame2);
            frame2.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(0, physicalEvent1.getCurrentOccurX(), DELTA);
            assertEquals(0, physicalEvent1.getCurrentOccurTime(), DELTA);
            assertTrue(physicalEvent1.hasOccurred());

            frame1.setProperTime(-20);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(0, physicalEvent1.getCurrentOccurX(), DELTA);
            assertEquals(0, physicalEvent1.getCurrentOccurTime(), DELTA);
            assertFalse(physicalEvent1.hasOccurred());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testCurrentInfoNotObserved() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 0, 0);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(10, physicalEvent1.getCurrentOccurX());
            assertEquals(20, physicalEvent1.getCurrentOccurTime());
            assertFalse(physicalEvent1.hasOccurred());

            String info = "{--event1--}(First observed in stationaryFrame)\n"
                    + "Not observed yet\n";
            assertEquals(info, physicalEvent1.currentInfo());

        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testCurrentInfoObserved() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 0, 0);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            frame1.setProperTime(20);
            stationaryFrame.viewInGivenFrame(frame1);
            physicalEvent1.viewInGivenFrame(frame1);
            assertEquals(10, physicalEvent1.getCurrentOccurX());
            assertEquals(20, physicalEvent1.getCurrentOccurTime());
            assertTrue(physicalEvent1.hasOccurred());

            String info = "{--event1--}(First observed in stationaryFrame)\n"
                    + "Occurred at position: 10.0c*s\n"
                    + "Occurred at time: 20.0s\n";
            assertEquals(info, physicalEvent1.currentInfo());

        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsFailDifferentField() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);

            // initial frame
            RefFrame newFrame = new RefFrame("newframe", 0, 0, 0, 0);
            PhysicalEvent newPhysicalEvent = new PhysicalEvent("event1", 10, 20, newFrame);
            assertFalse(newPhysicalEvent.equals(physicalEvent1));

            newFrame.setInitialProperTime(20);
            newPhysicalEvent.viewInGivenFrame(newFrame);
            assertFalse(newPhysicalEvent.equals(physicalEvent1));
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeSame() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            PhysicalEvent physicalEvent1Duplicate = new PhysicalEvent("event1", 10, 20, stationaryFrame);

            assertEquals(physicalEvent1.hashCode(), physicalEvent1Duplicate.hashCode());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeDifferent() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 10, 20, stationaryFrame);
            PhysicalEvent physicalEvent1Duplicate = new PhysicalEvent("event2", -10, -20, stationaryFrame);

            assertNotEquals(physicalEvent1.hashCode(), physicalEvent1Duplicate.hashCode());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }
}
