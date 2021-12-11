package persistence;

import model.PhysicalEvent;
import model.FrameWorld;
import model.RefFrame;
import model.exceptions.FrameConstructException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/* JSONReader that reads frameWorld from JSON data stored in destination file
 * Most of the codes are written referring to JsonSerializationDemo provided by CPSC 210 course:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
 * see individual methods for detail*/
public class JsonReader {
    private String fileToRead;

    // EFFECTS: initialize the name of the file to read
    public JsonReader(String fileName) {
        this.fileToRead = fileName;
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // EFFECTS: reads FrameWorld from file and returns it
    //          throws IOException if an error occurs reading data from file
    //          throws FrameConstructException if failed to construct frames and events
    public FrameWorld read() throws IOException, FrameConstructException {
        String jsonData = readFile(fileToRead);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFrameWorld(jsonObject);
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // EFFECTS: reads source file as string and returns it
    //          throws IOException if failed to read file
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: return the parsed frameWorld according to given jsonFrameWorld
    //          throws FrameConstructException if failed to construct frames and events
    private FrameWorld parseFrameWorld(JSONObject jsonFrameWorld) throws FrameConstructException {
        FrameWorld frameWorld = new FrameWorld();
        frameWorld.markAsCurrentFrameWorld(false);
        createFrames(frameWorld, jsonFrameWorld);
        createEvents(frameWorld, jsonFrameWorld);

        String currentReferenceName = jsonFrameWorld.getString("currentReference");
        RefFrame currentReference = frameWorld.findFrameByName(currentReferenceName);
        if (currentReference == null) {
            throw new FrameConstructException();
        }
        frameWorld.setCurrentReferenceAndUpdate(currentReference);

        Double referenceProperTime = jsonFrameWorld.getDouble("referenceProperTime");
        frameWorld.setCurrentReferenceProperTime(referenceProperTime);

        frameWorld.markAsCurrentFrameWorld(true);
        return frameWorld;
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // MODIFIES: frameWorld
    // EFFECTS: add frames that are parsed from jsonFrameWorld to frameWorld
    //          throws FrameConstructException if fail to construct frame/event, or fail to find
    private void createFrames(FrameWorld frameWorld, JSONObject jsonFrameWorld) throws FrameConstructException {
        JSONArray frameArray = jsonFrameWorld.getJSONArray("frameList");
        for (Object frameJson : frameArray) {
            JSONObject nextFrameJson = (JSONObject) frameJson;
            createFrame(frameWorld, nextFrameJson);
        }
    }

    // MODIFIES: frameWorld
    // EFFECTS: add frame that is parsed from nextFrameJson to frameWorld
    //          throws FrameConstructException if fail to construct frame
    private void createFrame(FrameWorld frameWorld, JSONObject nextFrameJson) throws FrameConstructException {
        String name = nextFrameJson.getString("name");
        Double initialPosX = nextFrameJson.getDouble("initialPosX");
        Double occurTime = nextFrameJson.getDouble("occurTime");
        Double initialProperTime = nextFrameJson.getDouble("initialProperTime");
        Double initialVelocity = nextFrameJson.getDouble("initialVelocity");
        RefFrame nextFrame = new RefFrame(name, initialPosX, occurTime, initialProperTime, initialVelocity);
        frameWorld.addFrame(nextFrame);
        // nextFrame will be automatically updated according to a newly initialized Absolute Stationary Frame
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // REQUIRES: frameWorld is already updated and has all frames
    // MODIFIES: frameWorld
    // EFFECTS: add events that are parsed from jsonFrameWorld to frameWorld
    //          throws FrameConstructException if fail to construct event
    private void createEvents(FrameWorld frameWorld, JSONObject jsonFrameWorld) throws FrameConstructException {
        JSONArray eventArray = jsonFrameWorld.getJSONArray("eventList");
        for (Object eventJson : eventArray) {
            JSONObject nextEventJson = (JSONObject) eventJson;
            createEvent(frameWorld, nextEventJson);
        }
    }

    // MODIFIES: frameWorld
    // EFFECTS: add event that is parsed from nextEventJson to frameWorld
    //          throws FrameConstructException if fail to construct event
    private void createEvent(FrameWorld frameWorld, JSONObject nextEventJson) throws FrameConstructException {
        String name = nextEventJson.getString("name");
        Double initialPosX = nextEventJson.getDouble("initialPosX");
        Double occurTime = nextEventJson.getDouble("occurTime");

        String initialFrameName = nextEventJson.getString("initialFrame");
        RefFrame initialFrame = frameWorld.findFrameByName(initialFrameName);

        PhysicalEvent nextPhysicalEvent = new PhysicalEvent(name, initialPosX, occurTime, initialFrame);
        frameWorld.addEvent(nextPhysicalEvent);
    }
}
