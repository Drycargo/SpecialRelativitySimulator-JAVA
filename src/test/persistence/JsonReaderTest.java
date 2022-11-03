package persistence;

import model.PhysicalEvent;
import model.RefFrame;
import model.FrameWorld;
import model.exceptions.FrameConstructException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void testReaderWrongFilePath() {
        JsonReader reader = new JsonReader("./data/wrongFilePath.json");
        try {
            FrameWorld frameWorld = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructionException should not be thrown");
        }
    }

    @Test
    void testReaderNullReference() {
        JsonReader reader = new JsonReader("./data/testReaderNullReference.json");
        try {
            FrameWorld frameWorld = reader.read();
            fail("FrameConstructionException expected");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (FrameConstructException frameConstructException) {
            // expected
        }
    }


    @Test
    void testReaderNewFrameWorld() {
        JsonReader reader = new JsonReader("./data/testReaderNewFrameWorld.json");
        try {
            FrameWorld frameWorld1 = reader.read();
            FrameWorld frameWorld2 = new FrameWorld();
            assertTrue(frameWorld1.equals(frameWorld2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructionException should not be thrown");
        }
    }

    @Test
    void testReaderGeneralFrameWorld() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFrameWorld.json");
        try {
            FrameWorld frameWorld1 = reader.read();
            FrameWorld frameWorld2 = new FrameWorld();
            RefFrame frameA = new RefFrame("Frame A", 2, 8, 5, 0.5);
            RefFrame frameB = new RefFrame("Frame B", -50, -20, -10, -0.7);
            frameWorld2.addFrame(frameA);
            frameWorld2.addFrame(frameB);
            frameWorld2.addEvent(new PhysicalEvent("Event 1", 50, 0, frameB));
            frameWorld2.addEvent(new PhysicalEvent("Event 2", -6, -10, frameA));
            frameWorld2.setCurrentReferenceAndUpdate(frameA);
            frameWorld2.setCurrentReferenceProperTime(10);

            assertTrue(frameWorld1.equals(frameWorld2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructionException should not be thrown");
        }
    }
}
