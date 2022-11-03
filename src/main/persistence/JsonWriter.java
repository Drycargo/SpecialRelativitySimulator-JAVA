package persistence;

import model.FrameWorld;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*JSONWriter that writes FrameWorld
 * Most of the codes are written referring to JsonSerializationDemo provided by CPSC 210 course:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
 * see individual methods for detail*/
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String fileToWrite;

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String fileName) {
        this.fileToWrite = fileName;
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //          be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(fileToWrite));
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // MODIFIES: this
    // EFFECTS: write world in form of JSON into destination file
    public void write(FrameWorld world) {
        JSONObject json = world.toJson();
        String formattedJsonString = json.toString(TAB);
        writer.print(formattedJsonString);
    }

    // This method is written referring to JsonSerializationDemo provided by CPSC 210 course, with very minor changes:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // MODIFIES: this
    // EFFECTS: close the writer
    public void close() {
        writer.close();
    }
}
