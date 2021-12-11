package model;

import model.exceptions.EmptyNameException;
import model.exceptions.ExceedSpeedOfLightException;
import model.exceptions.FrameConstructException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.*;

class RefFrameTest {
    RefFrame stationaryFrame;
    RefFrame setterExperimentFrame;
    static final double DELTA = pow(10, -13);

    @BeforeEach
    public void setUp() {
        try {
            stationaryFrame = new RefFrame("stationaryFrame", 0, 0, 0, 0);
            setterExperimentFrame = new RefFrame("setterFrame", 0, 0, 0, 0);
        } catch (Exception e) {
            fail("FrameTest initialization failed.");
        }
    }

    @Test
    public void testConstructFrameSuccess() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 10, 20, 0.5);
            assertEquals("frame1", frame1.getName());
            assertEquals(0, frame1.getInitialPosX());
            assertEquals(10, frame1.getOccurTime());
            assertEquals(20, frame1.getProperTime());
            assertEquals(0, frame1.getPosX());
            assertEquals(0.5, frame1.getInitialVelocity());
            assertEquals(0.5, frame1.getVelocity());
            assertEquals(0, frame1.getEventsSize());
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testConstructFrameWithEmptyName() {
        try {
            RefFrame frameEmptyName = new RefFrame("", 0, 20, 30, 0.5);
            fail("EmptyNameException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            //expected
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            fail("ExceedSpeedOfLightException should not be thrown");
        }
    }

    @Test
    public void testConstructFrameExceedC() {
        try {
            RefFrame frameExceedC = new RefFrame("frameC", 0, 20, 30, 1);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            RefFrame frameExceedC = new RefFrame("frameC", 0, 20, 30, -1);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            RefFrame frameExceedC = new RefFrame("frameC", 0, 20, 30, 5);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            RefFrame frameExceedC = new RefFrame("frameC", 0, 20, 30, -3);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }
    }


    @Test
    public void testMeetingEventStartSame() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 20, 0.6);
            MeetEvent meetEvent = frame1.meetingEvent(stationaryFrame);

            assertEquals(0, meetEvent.getPosX());
            assertEquals(0, meetEvent.getOccurTime());
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testMeetingEventStartDifferent() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 20, -0.6);
            RefFrame frame2 = new RefFrame("frame2", -60, 10, 40, 0.4);
            MeetEvent meetEvent = frame1.meetingEvent(frame2);

            assertEquals(-60 - 114 * -0.4, meetEvent.getPosX());
            assertEquals(124, meetEvent.getOccurTime());
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testRelativeVelocity() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 20, -0.6);
            RefFrame frame2 = new RefFrame("frame2", -60, 10, 40, 0.4);

            assertEquals((-0.6 - 0.4) / (1 - (-0.6 * 0.4)), frame1.relativeVelocity(frame2));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testProperTimeAtEvent() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 0, 0, 10, 0.6);
            MeetEvent meetEvent1 = new MeetEvent(6, 10);

            assertEquals(10 + 10 / RefFrame.gamma(0.6), frame1.properTimeAtEvent(meetEvent1));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInTheFrameWhichConstructionRefersTo() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 20, 0.2);
            frame1.setProperTime(40);

            RefFrame frame2 = new RefFrame("frame2", 80, 10, 30, 0.5, frame1);
            frame2.viewInGivenFrame(frame1);

            assertEquals(80 + 0.5 * (40 - 10), frame2.getPosX());
            assertEquals(0.5, frame2.getVelocity(), DELTA);
            assertEquals(30 + (40 - 10) / RefFrame.gamma(0.5), frame2.getProperTime());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInGivenFrameSameV() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 20, 0.5);
            RefFrame frame2 = new RefFrame("frame2", 80, 10, 30, 0.5);
            frame2.setProperTime(40);
            frame1.viewInGivenFrame(frame2);

            assertEquals(-15 * PhysicalObject.gamma(0.5), frame1.getPosX());
            assertEquals(0, frame1.getVelocity());
            assertEquals(30, frame1.getProperTime());
            assertEquals(60, frame1.getInitialPosX());
            assertEquals(0, frame1.getOccurTime());
            assertEquals(0.5, frame1.getInitialVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInStationaryFrame() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 20, 0.6);
            frame1.viewInGivenFrame(stationaryFrame);
            assertEquals(60, frame1.getPosX());
            assertEquals(0.6, frame1.getVelocity());
            assertEquals(20, frame1.getProperTime());
            assertEquals(60, frame1.getInitialPosX());
            assertEquals(0, frame1.getOccurTime());
            assertEquals(0.6, frame1.getInitialVelocity());

            stationaryFrame.setProperTime(10);
            frame1.viewInGivenFrame(stationaryFrame);
            assertEquals(60 + 0.6 * 10, frame1.getPosX());
            assertEquals(0.6, frame1.getVelocity());
            assertEquals(20 + 10 * 0.8, frame1.getProperTime()); // 1/gamma(0.6c) = (1-0.6^2)^0.5 = 0.8
            assertEquals(60, frame1.getInitialPosX());
            assertEquals(0, frame1.getOccurTime());
            assertEquals(0.6, frame1.getInitialVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInSameFrame() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 50, 10, 30, -0.75);
            frame1.viewInGivenFrame(frame1);
            assertEquals(0, frame1.getPosX());
            assertEquals(0, frame1.getVelocity());
            assertEquals(30, frame1.getProperTime());
            assertEquals(50, frame1.getInitialPosX());
            assertEquals(10, frame1.getOccurTime());
            assertEquals(-0.75, frame1.getInitialVelocity());

            frame1.setProperTime(-10);
            frame1.viewInGivenFrame(frame1);
            assertEquals(0, frame1.getPosX());
            assertEquals(0, frame1.getVelocity());
            assertEquals(-10, frame1.getProperTime());
            assertEquals(50, frame1.getInitialPosX());
            assertEquals(10, frame1.getOccurTime());
            assertEquals(-0.75, frame1.getInitialVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInFrameOccurAndInitialTimeZero() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 60, 0, 0, -0.2);
            RefFrame frame2 = new RefFrame("frame2", 0, 0, 0, 0.6);
            double relV = (-0.2 - 0.6) / (1 - 0.6 * (-0.2));
            frame1.viewInGivenFrame(frame2);
        /*In this case, when proper time of frame 2 is 0 from the stationary perspective, suppose frame1 has reached
        (x, t) = (60 - 0.2t, t); according to Lorentz Transformation, 0 = gamma(0.6c) * (t - (60 - 0.2t)*0.6), which
        allows us to calculate time t corresponding to the frame2 time when proper time is 0 and frame 1 is spotted*/
            assertEquals(1.25 * (60 - 0.2 * 36 / 1.12 - 0.6 * 36 / 1.12), frame1.getPosX());
            assertEquals(relV, frame1.getVelocity());
            assertEquals((36 / 1.12) / RefFrame.gamma(-0.2), frame1.getProperTime(), DELTA);
            assertEquals(60, frame1.getInitialPosX());
            assertEquals(0, frame1.getOccurTime());
            assertEquals(-0.2, frame1.getInitialVelocity());

            frame2.setProperTime(10);
            frame1.viewInGivenFrame(frame2);
            double time = (36 + 10 / 1.25) / 1.12;
            double posX = 60 - 0.2 * time;
            assertEquals(1.25 * (posX - 0.6 * time), frame1.getPosX(), DELTA);
            assertEquals(relV, frame1.getVelocity());
            assertEquals(time / RefFrame.gamma(-0.2), frame1.getProperTime(), DELTA);
            assertEquals(60, frame1.getInitialPosX());
            assertEquals(0, frame1.getOccurTime());
            assertEquals(-0.2, frame1.getInitialVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testViewInArbitraryFrame() {
        try {
            double frame1X = 18;
            double frame1OccurTime = -9;
            double frame1ProperTime = 27;
            double frame1V = 0.75;
            double frame2X = -3;
            double frame2OccurTime = 15.4;
            double frame2ProperTime = 4.5;
            double frame2V = 0.5;
            RefFrame frame1 = new RefFrame("frame1", frame1X, frame1OccurTime, frame1ProperTime, frame1V);
            RefFrame frame2 = new RefFrame("frame2", frame2X, frame2OccurTime, frame2ProperTime, frame2V);
            double relV = (frame1V - frame2V) / (1 - frame1V * frame2V);
            frame1.viewInGivenFrame(frame2);
            double gamma = RefFrame.gamma(frame2V);
            double deltaX = (frame1X - frame2X);
            double deltaOccurT = (frame1OccurTime - frame2OccurTime);
            double frame1PosXPrime = gamma * (deltaX - frame2V * deltaOccurT);
            double frame1OccurTPrime = gamma * (deltaOccurT - frame2V * deltaX);

            assertEquals(frame1PosXPrime + (frame2.getProperTime() - frame2ProperTime - frame1OccurTPrime) * relV,
                    frame1.getPosX());
            assertEquals(relV, frame1.getVelocity());
            assertEquals(frame1ProperTime +
                            (frame2.getProperTime() - frame2ProperTime - frame1OccurTPrime) / RefFrame.gamma(relV),
                    frame1.getProperTime(),
                    DELTA);
            assertEquals(frame1X, frame1.getInitialPosX());
            assertEquals(frame1OccurTime, frame1.getOccurTime());
            assertEquals(frame1V, frame1.getInitialVelocity());

            frame2.setProperTime(18.27);
            frame1.viewInGivenFrame(frame2);
            assertEquals(frame1.getPosX(),
                    frame1PosXPrime + (frame2.getProperTime() - frame2ProperTime - frame1OccurTPrime) * relV);
            assertEquals(frame1.getVelocity(), relV);
            assertEquals(frame1ProperTime +
                            (frame2.getProperTime() - frame2ProperTime - frame1OccurTPrime) / RefFrame.gamma(relV),
                    frame1.getProperTime(),
                    DELTA);
            assertEquals(frame1X, frame1.getInitialPosX());
            assertEquals(frame1OccurTime, frame1.getOccurTime());
            assertEquals(frame1V, frame1.getInitialVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testPositionWhenZero() {
        try {
            RefFrame frame2 = new RefFrame("frame2", 80, 10, 30, 0.5);
            assertEquals(80 - 10 * 0.5, frame2.positionWhenZero());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testDuplicateFrameSuccess() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 98, -234, 13, 0.8);
            RefFrame frame2 = frame1.duplicateFrame("Frame2");

            assertEquals("Frame2", frame2.getName());
            assertEquals(frame1.getOccurTime(), frame2.getOccurTime());
            assertEquals(frame1.getInitialVelocity(), frame2.getInitialVelocity());
            assertEquals(frame1.getInitialPosX(), frame2.getInitialPosX());
            assertEquals(frame1.getPosX(), frame2.getPosX());
            assertEquals(frame1.getProperTime(), frame2.getProperTime());
            assertEquals(frame1.getVelocity(), frame2.getVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testDuplicateFrameEmptyName() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 98, -234, 13, 0.8);
            RefFrame frameEmptyName = frame1.duplicateFrame("");
            fail("EmptyNameException should be thrown");
        } catch (FrameConstructException frameConstructException) {
            //expected
        }
    }

    @Test
    public void testDuplicateFrameAfterUpdate() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 98, -234, 13, 0.8);
            stationaryFrame.setProperTime(10);
            frame1.viewInGivenFrame(stationaryFrame);
            RefFrame frame2 = frame1.duplicateFrame("Frame2");

            assertEquals("Frame2", frame2.getName());
            assertEquals(frame1.getOccurTime(), frame2.getOccurTime());
            assertEquals(frame1.getInitialVelocity(), frame2.getInitialVelocity());
            assertEquals(frame1.getInitialPosX(), frame2.getInitialPosX());
            assertEquals(frame1.getPosX(), frame2.getPosX());
            assertEquals(frame1.getProperTime(), frame2.getProperTime());
            assertEquals(frame1.getVelocity(), frame2.getVelocity());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testSetNameSuccess() {
        try {
            setterExperimentFrame.setName("New Name");

            assertEquals("New Name", setterExperimentFrame.getName());
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }
    }

    @Test
    public void testSetEmptyName() {
        try {
            setterExperimentFrame.setName("");
            fail("EmptyNameException should be thrown");
        } catch (EmptyNameException emptyNameException) {
            //expected
        }

        assertEquals("setterFrame", setterExperimentFrame.getName());
    }

    @Test
    public void testSetInitialVelocitySuccess() {
        try {
            setterExperimentFrame.setInitialVelocity(0.5);
            assertEquals(0.5, setterExperimentFrame.getInitialVelocity());
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            fail("ExceedSpeedOfLightException should not be thrown");
        }
    }

    @Test
    public void testSetInitialVelocityFail() {
        try {
            setterExperimentFrame.setInitialVelocity(1);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            setterExperimentFrame.setInitialVelocity(-1);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            setterExperimentFrame.setInitialVelocity(1.1);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        try {
            setterExperimentFrame.setInitialVelocity(-2);
            fail("ExceedSpeedOfLightException should be thrown");
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            //expected
        }

        assertEquals(0, setterExperimentFrame.getInitialVelocity());
    }

    @Test
    public void testCurrentInfo() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 0, 30, 0.6);
            String currentInfo = frame1.currentInfo();
            assertEquals("<--Frame1-->\n" +
                    "X: 10.0c*s\n" +
                    "Clock Reading: 30.0s\n" +
                    "Velocity: 0.6c\n", currentInfo);
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testInitialInfo() {
        try {
            RefFrame frame1 = new RefFrame("Frame1", 10, 0, 30, 0.6);
            String initialInfo = frame1.initialInfo();
            String realInfo = "<--Frame1-->\n" +
                    "X when it is observed in stationary frame: 10.0c*s\n" +
                    "Time when it is observed: 0.0s\n" +
                    "Initial Clock Reading: 30.0s\n" +
                    "Initial Velocity: 0.6c\n";
            assertEquals(realInfo, initialInfo);

            frame1.setProperTime(10);
            assertEquals(realInfo, initialInfo);
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void findEventByNameSuccess() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, stationaryFrame);
            assertEquals(1, stationaryFrame.getEventsSize());
            assertEquals(physicalEvent1, stationaryFrame.findEventByName("event1"));
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }
    }

    @Test
    public void findEventByNameFails() {
        assertEquals(0, stationaryFrame.getEventsSize());
        assertNull(stationaryFrame.findEventByName("event1"));
    }

    @Test
    public void testGetEventsSize() {
        try {
            assertEquals(0, stationaryFrame.getEventsSize());
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, stationaryFrame);
            assertEquals(1, stationaryFrame.getEventsSize());
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }

        try {
            assertEquals(1, stationaryFrame.getEventsSize());
            for (int i = 1; i <= 50; i++) {
                PhysicalEvent nextPhysicalEvent = new PhysicalEvent("event" + i, 0, 0, stationaryFrame);
            }
            assertEquals(51, stationaryFrame.getEventsSize());
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }
    }

    @Test
    public void testDeleteEvent() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, stationaryFrame);
            assertEquals(physicalEvent1, stationaryFrame.findEventByName("event1"));
            stationaryFrame.deleteEvent(physicalEvent1);
            assertNull(stationaryFrame.findEventByName("event1"));
        } catch (EmptyNameException emptyNameException) {
            fail("EmptyNameException should not be thrown");
        }
    }

    @Test
    public void testRemoveAllEvents() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 10, 20, 30, 0.5);
            PhysicalEvent physicalEvent0 = new PhysicalEvent("event0", 2, 3, stationaryFrame);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("event2", 0, 0, stationaryFrame);
            PhysicalEvent physicalEvent3 = new PhysicalEvent("event3", 1, 2, frame1);
            PhysicalEvent physicalEvent4 = new PhysicalEvent("event4", 2, 3, stationaryFrame);
            List<PhysicalObject> eventList = new LinkedList<PhysicalObject>();
            eventList.add(physicalEvent0);
            eventList.add(physicalEvent1);
            eventList.add(physicalEvent2);
            eventList.add(physicalEvent3);
            eventList.add(physicalEvent4);
            assertEquals(physicalEvent1, frame1.findEventByName("event1"));
            assertEquals(physicalEvent3, frame1.findEventByName("event3"));
            assertEquals(5, eventList.size());

            frame1.removeAllEventsInGivenEventList(eventList);
            assertEquals(3, eventList.size());
            assertTrue(eventList.contains(physicalEvent0));
            assertFalse(eventList.contains(physicalEvent1));
            assertTrue(eventList.contains(physicalEvent2));
            assertFalse(eventList.contains(physicalEvent3));
            assertTrue(eventList.contains(physicalEvent4));
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEventNameList() {
        try {
            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, stationaryFrame);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("event2", 1, 2, stationaryFrame);
            assertEquals(physicalEvent1, stationaryFrame.findEventByName("event1"));
            assertEquals(physicalEvent2, stationaryFrame.findEventByName("event2"));

            String events = "event1\nevent2\n";
            assertEquals(events, stationaryFrame.eventNameList());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testGetObservedEvents() {
        try {
            assertTrue(stationaryFrame.getObservedEvents().isEmpty());

            PhysicalEvent physicalEvent1 = new PhysicalEvent("event1", 0, 0, stationaryFrame);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("event2", 1, 2, stationaryFrame);
            assertEquals(physicalEvent1, stationaryFrame.findEventByName("event1"));
            assertEquals(physicalEvent2, stationaryFrame.findEventByName("event2"));

            assertEquals(2, stationaryFrame.getObservedEvents().size());
            assertEquals(physicalEvent1, stationaryFrame.getObservedEvents().get(0));
            assertEquals(physicalEvent2, stationaryFrame.getObservedEvents().get(1));
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }


    @Test
    public void testEqualsFailNull() {
        assertFalse(stationaryFrame.equals(null));

        RefFrame nullFrame = null;
        assertFalse(stationaryFrame.equals(nullFrame));
    }

    @Test
    public void testEqualsFailWrongType() {
        assertFalse(stationaryFrame.equals(new String()));
        try {
            assertFalse(stationaryFrame.equals(new FrameWorld()));
        } catch (FrameConstructException e) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testEqualsSuccessSameObject() {
        assertTrue(stationaryFrame.equals(stationaryFrame));
    }

    @Test
    public void testEqualsFailDifferentField() {
        try {
            RefFrame newFrame = new RefFrame("New Frame", 0, 0, 0, 0);
            RefFrame newFrameDuplicate = new RefFrame("New Frame", 0, 0, 0, 0);

            // Occur Time
            newFrame.setOccurTime(10);
            assertFalse(newFrame.equals(newFrameDuplicate));

            // InitialProperTime
            newFrame.setOccurTime(0);
            newFrame.setInitialProperTime(10);
            assertFalse(newFrame.equals(newFrameDuplicate));

            // InitialPosX
            newFrame.setInitialProperTime(0);
            newFrame.setInitialPosX(10);
            assertFalse(newFrame.equals(newFrameDuplicate));

            // Initial Velocity
            newFrame.setInitialPosX(0);
            newFrame.setInitialVelocity(0.5);
            assertFalse(newFrame.equals(newFrameDuplicate));

        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeSame() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 10, 20, 30, 0.5);
            RefFrame frame1Duplicate = new RefFrame("frame1", 10, 20, 30, 0.5);

            assertEquals(frame1.hashCode(), frame1Duplicate.hashCode());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    public void testHashCodeDifferent() {
        try {
            RefFrame frame1 = new RefFrame("frame1", 10, 20, 30, 0.5);
            RefFrame frame1Duplicate = new RefFrame("frame2", -10, -20, -30, -0.5);


            assertNotEquals(frame1.hashCode(), frame1Duplicate.hashCode());
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }
}