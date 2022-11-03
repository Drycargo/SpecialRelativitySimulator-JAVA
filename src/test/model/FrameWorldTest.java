package model;

import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrameWorldTest {
    private FrameWorld world1;

    @BeforeEach
    public void setUp() {
        try {
            world1 = new FrameWorld();
        } catch (FrameConstructException frameConstructException) {
            fail("FrameWorldTest Initialization failed");
        }
    }

    @Test
    public void testConstructor() {
        assertEquals(FrameWorld.CURRENT_FRAMEWORLD_NAME, world1.getName());
        assertEquals(world1.getAbsoluteStationaryFrame(), world1.getCurrentReference());
        assertTrue(world1.getEventList().isEmpty());
        assertEquals(1, world1.getFrameList().size());
    }

    @Test
    public void testFindFrameByNameNotFound() {
        assertNull(world1.findFrameByName("No Such Frame"));
    }

    @Test
    public void testFindEventByNameNotFound() {
        assertNull(world1.findFrameByName("No Such Event"));
    }

    @Test
    public void testFindFrameByNameFoundOnlyElement() {
        assertEquals(world1.getAbsoluteStationaryFrame(),
                world1.findFrameByName("Absolute Stationary Frame"));
    }

    @Test
    public void testFindEventByNameFoundOnlyElement() {
        assertEquals(world1.getAbsoluteStationaryFrame(),
                world1.findFrameByName("Absolute Stationary Frame"));
    }

    @Test
    public void testFindByNameInMultipleFrames() {
        try {
            RefFrame frameTarget = new RefFrame("Target Frame", 0, 0, 0, 0);
            world1.addFrame(frameTarget);
            for (int i = 0; i < 10; i++) {
                world1.addFrame(new RefFrame("Frame" + i, i, i, i, i * 0.01));
            }

            assertEquals(frameTarget, world1.findFrameByName("Target Frame"));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAddFrameOneSuccess() {
        try {
            RefFrame frameTarget = new RefFrame("Target Frame", 0, 0, 0, 0);
            world1.addFrame(frameTarget);
            assertEquals(frameTarget, world1.findFrameByName("Target Frame"));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAddEventOneSuccess() {
        try {
            PhysicalEvent physicalEventTarget = new PhysicalEvent("Target Event", 0, 0,
                    world1.getAbsoluteStationaryFrame());
            world1.addEvent(physicalEventTarget);
            assertEquals(physicalEventTarget, world1.findEventByName("Target Event"));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }


    @Test
    public void testAddFrameOneFail() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 0, 0, 0, 0);
            RefFrame sameNameWithFrame1 = new RefFrame("Frame1", 2, 3, 4, 0.5);

            world1.addFrame(frame1);
            assertEquals(frame1, world1.findFrameByName("Frame1"));

            world1.addFrame(sameNameWithFrame1);
            fail("SameNameException should be thrown");
        } catch (SameNameException sameNameException) {
            //expected
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAddEventOneFail() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 0, 0,
                    world1.getAbsoluteStationaryFrame());
            world1.addEvent(physicalEvent1);
            assertEquals(physicalEvent1, world1.findEventByName("Event1"));

            PhysicalEvent physicalEventSameName = new PhysicalEvent("Event1", 0, 0,
                    world1.getAbsoluteStationaryFrame());
            world1.addEvent(physicalEventSameName);
            fail("SameNameException should be thrown");
        } catch (SameNameException sameNameException) {
            //expected
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAddFrameMultipleSuccess() {
        try {
            for (int i = 0; i < 10; i++) {
                RefFrame newFrame = new RefFrame("Frame" + i, i, i, i, i * 0.01);
                world1.addFrame(newFrame);
            }

            for (int i = 0; i < 10; i++) {
                RefFrame targetFrame = world1.findFrameByName("Frame" + i);
                assertEquals("Frame" + i, targetFrame.getName());
                assertEquals(i, targetFrame.getInitialPosX());
                assertEquals(i * 0.01, targetFrame.getInitialVelocity());
            }
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAddEventSameNameAsFrame() {
        try {
            RefFrame testFrame = new RefFrame("One", 10, 20, 30, 0);
            PhysicalEvent testPhysicalEvent = new PhysicalEvent("One", 10, 20, testFrame);

            world1.addFrame(testFrame);
            world1.addEvent(testPhysicalEvent);

            assertEquals(testFrame, world1.findFrameByName("One"));
            assertEquals(testPhysicalEvent, world1.findEventByName("One"));
        } catch (SameNameException sameNameException) {
            fail("SameNameException should not be thrown");
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not br thrown");
        }
    }

    @Test
    public void testSetCurrentReferenceAndUpdate() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            RefFrame frame2 = new RefFrame("Frame2", -9, -8, -7, -0.35);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("Event2", -6, -10, frame2);

            RefFrame stationaryFrame = world1.getAbsoluteStationaryFrame();
            RefFrame stationaryDuplicate = stationaryFrame.duplicateFrame("Stationary Duplicate");
            RefFrame frame1Duplicate = frame1.duplicateFrame("Frame1 Duplicate");
            PhysicalEvent physicalEvent1Duplicate = new PhysicalEvent("Event1", 10, 20, frame1);
            PhysicalEvent physicalEvent2Duplicate = new PhysicalEvent("Event2", -6, -10, frame2);

            world1.addFrame(frame1);
            world1.addFrame(frame2);
            world1.addEvent(physicalEvent1);
            world1.addEvent(physicalEvent2);

            world1.setCurrentReferenceAndUpdate(frame2);
            frame1Duplicate.viewInGivenFrame(frame2);
            stationaryDuplicate.viewInGivenFrame(frame2);
            physicalEvent1Duplicate.viewInGivenFrame(frame2);
            physicalEvent2Duplicate.viewInGivenFrame(frame2);

            assertEquals(frame1Duplicate.getPosX(), frame1.getPosX());
            assertEquals(frame1Duplicate.getProperTime(), frame1.getProperTime());
            assertEquals(frame1Duplicate.getVelocity(), frame1.getVelocity());

            assertEquals(stationaryDuplicate.getPosX(), stationaryFrame.getPosX());
            assertEquals(stationaryDuplicate.getProperTime(), stationaryFrame.getProperTime());
            assertEquals(stationaryDuplicate.getVelocity(), stationaryFrame.getVelocity());

            assertTrue(physicalEvent1.equals(physicalEvent1Duplicate));
            assertTrue(physicalEvent2.equals(physicalEvent2Duplicate));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testSetCurrentReferenceProperTime() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);

            RefFrame stationaryFrame = world1.getAbsoluteStationaryFrame();
            RefFrame stationaryDuplicate = stationaryFrame.duplicateFrame("Stationary Duplicate");
            PhysicalEvent physicalEvent1Duplicate = new PhysicalEvent("Event1", 10, 20, frame1);

            world1.addFrame(frame1);
            world1.addEvent(physicalEvent1);
            world1.setCurrentReferenceAndUpdate(frame1);
            world1.setCurrentReferenceProperTime(-300);
            stationaryDuplicate.viewInGivenFrame(frame1);
            physicalEvent1Duplicate.viewInGivenFrame(frame1);

            assertEquals(-300, frame1.getProperTime());
            assertEquals(stationaryDuplicate.getPosX(), stationaryFrame.getPosX());
            assertEquals(stationaryDuplicate.getProperTime(), stationaryFrame.getProperTime());
            assertEquals(stationaryDuplicate.getVelocity(), stationaryFrame.getVelocity());
            assertTrue(physicalEvent1.equals(physicalEvent1Duplicate));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testDeleteFrame() {
        try {
            RefFrame stationaryFrame = world1.getAbsoluteStationaryFrame();
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            world1.addFrame(frame1);
            PhysicalEvent physicalEvent0 = new PhysicalEvent("event0", 2, 3, stationaryFrame);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("event2", 0, 0, stationaryFrame);
            PhysicalEvent physicalEvent3 = new PhysicalEvent("event3", 1, 2, frame1);
            PhysicalEvent physicalEvent4 = new PhysicalEvent("event4", 2, 3, stationaryFrame);
            world1.addEvent(physicalEvent0);
            world1.addEvent(physicalEvent1);
            world1.addEvent(physicalEvent2);
            world1.addEvent(physicalEvent3);
            world1.addEvent(physicalEvent4);

            assertEquals(frame1, world1.findFrameByName("Frame1"));
            assertEquals(physicalEvent0, world1.findEventByName("event0"));
            assertEquals(physicalEvent1, world1.findEventByName("event1"));
            assertEquals(physicalEvent2, world1.findEventByName("event2"));
            assertEquals(physicalEvent3, world1.findEventByName("event3"));
            assertEquals(physicalEvent4, world1.findEventByName("event4"));

            world1.deleteFrame(frame1);
            assertNull(world1.findFrameByName("Frame1"));
            assertNull(world1.findEventByName("event1"));
            assertNull(world1.findEventByName("event3"));
            assertEquals(physicalEvent0, world1.findEventByName("event0"));
            assertEquals(physicalEvent2, world1.findEventByName("event2"));
            assertEquals(physicalEvent4, world1.findEventByName("event4"));
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testDeleteFrameSwitchReference() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            world1.addFrame(frame1);
            world1.setCurrentReferenceAndUpdate(frame1);
            assertEquals(frame1, world1.findFrameByName("Frame1"));
            assertEquals(frame1, world1.getCurrentReference());

            world1.deleteFrame(frame1);
            assertNull(world1.findFrameByName("Frame1"));
            assertEquals(world1.getAbsoluteStationaryFrame(), world1.getCurrentReference());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testDeleteEvent() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            world1.addFrame(frame1);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);
            world1.addEvent(physicalEvent1);
            assertEquals(physicalEvent1, world1.findEventByName("Event1"));
            assertEquals(physicalEvent1, frame1.findEventByName("Event1"));

            world1.deleteEvent(physicalEvent1);
            assertNull(world1.findEventByName("Event1"));
            assertNull(frame1.findEventByName("Event1"));
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAllFramesInfo() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.6);
            RefFrame frame2 = new RefFrame("Frame2", -9, -8, -7, -0.6);
            world1.addFrame(frame1);
            world1.addFrame(frame2);

            String allInfo = "[Reference Frame]<--Absolute Stationary Frame-->\n" +
                    "X: 0.0c*s\n" +
                    "Clock Reading: 0.0s\n" +
                    "Velocity: 0.0c\n\n" +
                    "<--Frame1-->\n" +
                    "X: -2.0c*s\n" +
                    "Clock Reading: 14.0s\n" +
                    "Velocity: 0.6c\n\n" +
                    "<--Frame2-->\n" +
                    "X: -13.8c*s\n" +
                    "Clock Reading: -0.6s\n" +
                    "Velocity: -0.6c\n\n";

            assertEquals(allInfo, world1.allFramesInfo());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testAllEventsInfo() {
        try {
            world1.setCurrentReferenceProperTime(20);
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.6);
            world1.addFrame(frame1);

            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("Event2", 10, 100, frame1);
            PhysicalEvent physicalEvent3 = new PhysicalEvent("Event3", 50, 0, world1.getAbsoluteStationaryFrame());
            world1.addEvent(physicalEvent1);
            world1.addEvent(physicalEvent2);
            world1.addEvent(physicalEvent3);
            world1.setCurrentReferenceAndUpdate(frame1);

            String allInfo = "{--Event1--}(First observed in Frame1)\n" +
                    "Occurred at position: 10.0c*s\n" +
                    "Occurred at time: 20.0s\n\n" +
                    "{--Event2--}(First observed in Frame1)\n" +
                    "Not observed yet\n\n" +
                    "{--Event3--}(First observed in Absolute Stationary Frame)\n" +
                    "Occurred at position: 65.0c*s\n" +
                    "Occurred at time: -25.0s\n\n";

            assertEquals(allInfo, world1.allEventsInfo());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testGetFrameNames() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.6);
            RefFrame frame2 = new RefFrame("Frame2", -9, -8, -7, -0.6);
            world1.addFrame(frame1);
            world1.addFrame(frame2);

            List<String> allNames = new ArrayList<String>();
            allNames.add("Absolute Stationary Frame");
            allNames.add("Frame1");
            allNames.add("Frame2");
            assertEquals(allNames, world1.getFrameNames());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testFrameNameList() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.6);
            RefFrame frame2 = new RefFrame("Frame2", -9, -8, -7, -0.6);
            world1.addFrame(frame1);
            world1.addFrame(frame2);

            String allNames = "Absolute Stationary Frame\nFrame1\nFrame2\n";
            assertEquals(allNames, world1.frameNameList());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEventNameList() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.6);
            world1.addFrame(frame1);

            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("Event2", 10, 100, frame1);
            PhysicalEvent physicalEvent3 = new PhysicalEvent("Event3", 50, 0, world1.getAbsoluteStationaryFrame());
            world1.addEvent(physicalEvent1);
            world1.addEvent(physicalEvent2);
            world1.addEvent(physicalEvent3);


            String allNames = "Event1\nEvent2\nEvent3\n";
            assertEquals(allNames, world1.eventNameList());
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsFailNull() {
        assertFalse(world1.equals(null));

        FrameWorld nullWorld = null;
        assertFalse(world1.equals(nullWorld));
    }

    @Test
    public void testEqualsFailWrongType() {
        assertFalse(world1.equals(new String()));
        assertFalse(world1.equals(world1.getAbsoluteStationaryFrame()));
    }

    @Test
    public void testEqualsSuccessSameObject() {
        assertTrue(world1.equals(world1));
    }

    @Test
    public void testEqualsFailDifferentFrameList() {
        try {
            FrameWorld newWorld = new FrameWorld();
            newWorld.addFrame(new RefFrame("new frame", 10, 20, 30, 0.4));
            assertFalse(world1.equals(newWorld));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsFailDifferentEventList() {
        try {
            FrameWorld newWorld = new FrameWorld();
            newWorld.addEvent(
                    new PhysicalEvent("new event", 10, 20, newWorld.getAbsoluteStationaryFrame()));
            assertFalse(world1.equals(newWorld));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsFailDifferentStationaryFrame() {
        try {
            FrameWorld newWorld = new FrameWorld();
            newWorld.setCurrentReferenceAndUpdate(newWorld.getAbsoluteStationaryFrame());
            newWorld.setCurrentReferenceProperTime(10);
            assertFalse(world1.equals(newWorld));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsFailDifferentCurrentReference() {
        try {
            FrameWorld newWorld = new FrameWorld();
            RefFrame frame1 = new RefFrame("new frame", 0, 0, 0, 0);
            RefFrame frame1Duplicate = new RefFrame("new frame", 0, 0, 0, 0);

            world1.addFrame(frame1);
            newWorld.addFrame(frame1Duplicate);
            assertTrue(world1.equals(newWorld));
            newWorld.setCurrentReferenceAndUpdate(frame1Duplicate);
            assertFalse(world1.equals(newWorld));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeSame() {
        try {
            FrameWorld world1Duplicate = new FrameWorld();
            assertEquals(world1.hashCode(), world1Duplicate.hashCode());
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeDifferent() {
        try {
            FrameWorld world1Duplicate = new FrameWorld();
            world1Duplicate.setCurrentReferenceProperTime(10);
            assertNotEquals(world1.hashCode(), world1Duplicate.hashCode());
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testMarkAsCurrentFrameWorld() {
        // set false
        world1.markAsCurrentFrameWorld(false);
        assertEquals(FrameWorld.TEMPORARY_FRAMEWORLD, world1.getName());

        // set true
        world1.markAsCurrentFrameWorld(true);
        assertEquals(FrameWorld.CURRENT_FRAMEWORLD_NAME, world1.getName());
    }
}
