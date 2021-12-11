package persistence;

import model.PhysicalEvent;
import model.RefFrame;
import model.FrameWorld;
import model.exceptions.FrameConstructException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            FrameWorld frameWorld = new FrameWorld();
            JsonWriter writer = new JsonWriter("./data/invalid\0File.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    void testWriterNewFrameWorld() {
        try {
            FrameWorld frameWorld = new FrameWorld();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(frameWorld);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            FrameWorld readFrameWorld = reader.read();
            assertTrue(readFrameWorld.equals(frameWorld));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }

    @Test
    void testWriterGeneralFrameWorld() {
        try {
            FrameWorld frameWorld = new FrameWorld();
            RefFrame frame1 = new RefFrame("Frame1", 10, 20, 30, 0.5);
            RefFrame frame2 = new RefFrame("Frame2", -9, -8, -7, -0.35);
            PhysicalEvent physicalEvent1 = new PhysicalEvent("Event1", 10, 20, frame1);
            PhysicalEvent physicalEvent2 = new PhysicalEvent("Event2", -6, -10, frame2);

            frameWorld.addFrame(frame1);
            frameWorld.addFrame(frame2);
            frameWorld.addEvent(physicalEvent1);
            frameWorld.addEvent(physicalEvent2);

            frameWorld.setCurrentReferenceAndUpdate(frame2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFrameWorld.json");
            writer.open();
            writer.write(frameWorld);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFrameWorld.json");
            FrameWorld readFrameWorld = reader.read();
            assertTrue(readFrameWorld.equals(frameWorld));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (FrameConstructException frameConstructException) {
            fail("FrameConstructException should not be thrown");
        }
    }
}
