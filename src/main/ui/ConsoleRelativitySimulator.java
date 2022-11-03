package ui;

import model.PhysicalEvent;
import model.RefFrame;
import model.FrameWorld;
import model.exceptions.EmptyNameException;
import model.exceptions.ExceedSpeedOfLightException;
import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*User interface of the Special Relativity Simulator*/
public class ConsoleRelativitySimulator {
    public static final String SOURCE_FILE = "./data/ourUniverse.json";
    public static final String SELECT_REFERENCE = "a";
    public static final String SET_TIME = "b";
    public static final String VIEW_ALL = "c";
    public static final String ADD_FRAME = "d";
    public static final String DELETE_FRAME = "e";
    public static final String EDIT_FRAME = "f";
    public static final String ADD_EVENT = "g";
    public static final String DELETE_EVENT = "h";
    public static final String LOAD_FILE = "i";
    public static final String SAVE_FILE = "j";
    public static final String QUIT = "q";

    //sub-commends
    public static final String INITIAL_POS_X = "1";
    public static final String OCCUR_TIME = "2";
    public static final String INITIAL_PROPER_TIME = "3";
    public static final String INITIAL_VELOCITY = "4";
    public static final String NAME = "5";
    public static final String DONE = "0";

    private FrameWorld frameWorld;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: construct special relativity simulator
    public ConsoleRelativitySimulator() {
        runSimulation();
    }

    // MODIFIES: this
    // EFFECTS: run special relativity simulation
    public void runSimulation() {
        boolean simulationRunning = true;
        String command;

        try {
            initialize();
        } catch (FrameConstructException frameConstructException) {
            System.out.println("Initialize failed -- exiting the program.");
            return;
        }

        while (simulationRunning) {
            displayMenu();

            command = input.next();
            command = command.toLowerCase();
            printSeparationBar();
            if (command.equals("q")) {
                simulationRunning = false;
                askSaveFile();
            } else {
                processCommand(command);
                printSeparationBar();
            }
        }

        System.out.println("Farewell and have a nice day with Physics!");
    }

    // MODIFIES: this
    // EFFECTS: ask user whether to save data to file or not
    private void askSaveFile() {
        System.out.println("Do you want to save current data to file" + SOURCE_FILE + " ? (Y/N)");
        while (true) {
            String command = input.next();
            if (command.equalsIgnoreCase("Y")) {
                saveFrameWorld();
                break;
            } else if (command.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Unknown command. Please input (Y/N)");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initialize the simulator and print welcome words
    //          throws FrameConstructException if frameWorld failed to be instantiated
    public void initialize() throws FrameConstructException {
        frameWorld = new FrameWorld();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(SOURCE_FILE);
        jsonReader = new JsonReader(SOURCE_FILE);
        System.out.println("Welcome to Special Relativity Simulator!");
    }

    // EFFECTS: display general choice menu
    public void displayMenu() {
        System.out.println("---Select Action:---");
        System.out.println("\t" + SELECT_REFERENCE + " : Select new reference frame");
        System.out.println("\t" + SET_TIME + " : Set the proper time (reading of clock) "
                + "in the current reference frame");
        System.out.println("\t" + VIEW_ALL + " : View all frame and event info in current reference frame");
        System.out.println("\t" + ADD_FRAME + " : Add new frame");
        System.out.println("\t" + DELETE_FRAME + " : Delete existing frame (and observed events)");
        System.out.println("\t" + EDIT_FRAME + " : Edit initial status of selected frame"
                + " with respect to Absolute Stationary Frame");
        System.out.println("\t" + ADD_EVENT + " : Add new event");
        System.out.println("\t" + DELETE_EVENT + " : Delete existing event");
        System.out.println("\t" + LOAD_FILE + " : Load FrameWorld data from file");
        System.out.println("\t" + SAVE_FILE + " : Save FrameWorld data to file");
        System.out.println("\t" + QUIT + " : Quit Simulation");
    }

    // MODIFIES: this
    // EFFECTS: operate based on given command
    @SuppressWarnings("methodlength")
    public void processCommand(String command) {
        switch (command) {
            case SELECT_REFERENCE:
                selectReference();
                break;
            case SET_TIME:
                setReferenceProperTime();
                break;
            case VIEW_ALL:
                viewAllFramesAndEvents();
                break;
            case ADD_FRAME:
                addNewFrame();
                break;
            case DELETE_FRAME:
                deleteChosenFrame();
                break;
            case EDIT_FRAME:
                editFrame();
                break;
            case ADD_EVENT:
                addNewEvent();
                break;
            case DELETE_EVENT:
                deleteChosenEvent();
                break;
            case LOAD_FILE:
                loadFrameWorld();
                break;
            case SAVE_FILE:
                saveFrameWorld();
                break;
            default:
                System.out.println("Unknown Command.");
                break;
        }
    }


    // MODIFIES: this
    // EFFECTS: set new reference frame; give warning and do nothing if the selected frame does not exist yet
    private void selectReference() {
        printAllFrameNames();
        System.out.println("Please enter the name of the new reference frame:");
        String newReferenceName = input.next();

        RefFrame newReference = frameWorld.findFrameByName(newReferenceName);
        if (newReference != null) {
            frameWorld.setCurrentReferenceAndUpdate(newReference);
            System.out.println("\n" + newReferenceName + " is set as the current reference frame.");
        } else {
            System.out.println("\n");
            printNotExist(newReferenceName);
        }
    }

    // MODIFIES: this
    // EFFECTS: set the proper time of the current reference frame and update all frames' posX and properTime; if input
    //          is not of double type, give warning and do not change this
    private void setReferenceProperTime() {
        RefFrame currentReference = frameWorld.getCurrentReference();
        System.out.println("The current reference frame is: " + currentReference.getName());
        System.out.println("Its proper time(reading of the clock) is: " + currentReference.getProperTime() + "s");
        System.out.println("Please set the proper time:");
        try {
            double newProperTime = Double.parseDouble(input.next());
            frameWorld.setCurrentReferenceProperTime(newProperTime);
            System.out.println(currentReference.getName() + "'s proper time is set to "
                    + currentReference.getProperTime() + "s");
            System.out.println("All frames' and events' status under the perspective of "
                    + currentReference.getName() + " have been updated.");
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Wrong type of input: Proper Time must be a number.");
        }
    }

    // EFFECTS: print the current status of all reference frames under the perspective of the current reference frame;
    //          emphasize the current reference frame
    private void viewAllFramesAndEvents() {
        RefFrame currentReference = frameWorld.getCurrentReference();
        System.out.println("The frames' status under the perspective of " + currentReference.getName() + ":");
        System.out.println(frameWorld.allFramesInfo());
        System.out.println("The events' status under the perspective of " + currentReference.getName() + ":");
        String allEventsInfo = frameWorld.allEventsInfo();
        if (allEventsInfo.isEmpty()) {
            System.out.println("No events yet.");
        } else {
            System.out.println(frameWorld.allEventsInfo());
        }
    }


    //MODIFIES: this
    //EFFECTS: add a new frame to frameList with respect to a chosen frame; if given frame's name is the same
    //         as any existing frame, give warning and do not modify this
    @SuppressWarnings("methodlength")
    private void addNewFrame() {
        System.out.print("Enter the name of the new frame: ");
        String name = input.next();

        printAllFrameNames();
        System.out.print("Which existing frame is this new frame constructed with respect to: ");
        String parentFrameName = input.next();
        RefFrame parentFrame = frameWorld.findFrameByName(parentFrameName);
        if (parentFrame == null) {
            printNotExist(parentFrameName);
            return;
        }

        try {
            RefFrame newFrame;
            System.out.print("Where is the new frame observed in " + parentFrameName + "(unit: c*s): ");
            double initialPosX = Double.parseDouble(input.next());
            System.out.print("When is the new frame observed in " + parentFrameName + "(unit: s): ");
            double occurTime = Double.parseDouble(input.next());
            System.out.print("What is the reading of the clock in this new frame when it is observed(unit: s): ");
            double initialProperTime = Double.parseDouble(input.next());
            System.out.print("What is the new frame's velocity relative to" + parentFrameName + " (unit: c): ");
            double initialVelocity = Double.parseDouble(input.next());

            if (parentFrameName.equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME)) {
                newFrame = new RefFrame(name, initialPosX, occurTime, initialProperTime, initialVelocity);
            } else {
                newFrame = new RefFrame(name, initialPosX, occurTime, initialProperTime, initialVelocity, parentFrame);
            }

            frameWorld.addFrame(newFrame);
            System.out.println("Frame " + name + " is successfully added.");
        } catch (NumberFormatException numberFormatException) {
            printWrongData();
        } catch (EmptyNameException emptyNameException) {
            printEmptyNameWarning();
        } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
            printVelocityExceedLightWarning();
        } catch (SameNameException sameNameException) {
            System.out.println("Frame named " + name + " already exists.");
        }
    }

    //MODIFIES: this
    //EFFECTS: delete chosen frame from the simulator, as well as all the events observed by the chosen frame.
    //         If the chosen frame does not exist, give warning and do not change this.
    //         If the chosen frame is the reference frame, set reference frame to Absolute Stationary Frame.
    //         Absolute Stationary Frame cannot be deleted.
    private void deleteChosenFrame() {
        printAllFrameNames();
        System.out.print("Please enter the name of the frame you want to delete: ");
        String frameToDeleteName = input.next();
        if (frameToDeleteName.equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME)) {
            System.out.println("You cannot delete the Absolute Stationary Frame.");
            return;
        }

        RefFrame frameToDelete = frameWorld.findFrameByName(frameToDeleteName);
        if (frameToDelete == null) {
            printNotExist(frameToDeleteName);
            return;
        }

        if (frameToDelete.equals(frameWorld.getCurrentReference())) {
            System.out.println("Current Reference is reset to be Absolute Stationary Frame.");
        }

        frameWorld.deleteFrame(frameToDelete);
        System.out.println(frameToDeleteName + " is deleted.\n");
        System.out.println("The following events are deleted:");
        System.out.println(frameToDelete.eventNameList());
    }

    //MODIFIES: this
    //EFFECTS: edit the initial status of selected frame with respect to Absolute Stationary Frame, and update it
    @SuppressWarnings("methodlength")
    private void editFrame() {
        String subCommand = ""; //initialize
        printAllFrameNames();
        System.out.print("Please enter the name of the frame you want to edit: ");
        String frameToEditName = input.next();
        if (frameToEditName.equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME)) {
            System.out.println("You cannot edit the Absolute Stationary Frame.");
            return;
        }

        RefFrame frameToEdit = frameWorld.findFrameByName(frameToEditName);
        if (frameToEdit == null) {
            printNotExist(frameToEditName);
            return;
        }

        System.out.println("This is the initial state of " + frameToEdit.getName()
                + " with respect to Absolute Stationary Frame:");
        System.out.println(frameToEdit.initialInfo());
        while (!subCommand.equals("0")) {
            printEditMenu();
            subCommand = input.next();
            try {
                makeEdition(subCommand, frameToEdit);
            } catch (NumberFormatException numberFormatException) {
                printWrongData();
            } catch (ExceedSpeedOfLightException exceedSpeedOfLightException) {
                printVelocityExceedLightWarning();
            } catch (EmptyNameException emptyNameException) {
                printEmptyNameWarning();
            }

            if (!frameToEditName.equals(frameToEdit.getName())) {
                System.out.println("This is the new Initial status of " + frameToEdit.getName()
                        + "(initially named " + frameToEditName + "):");
            } else {
                System.out.println("This is the new Initial status of " + frameToEdit.getName() + ":");
            }
            System.out.println(frameToEdit.initialInfo());
            System.out.println("Its current status is:");
            System.out.println(frameToEdit.currentInfo());
        }
    }

    //REQUIRES: frameToEdit exists in frameWorld and update its current status
    //MODIFIES: this
    //EFFECTS: modify the given frame
    @SuppressWarnings("methodlength")
    private void makeEdition(String subCommand, RefFrame frameToEdit) throws NumberFormatException, EmptyNameException,
            ExceedSpeedOfLightException {
        switch (subCommand) {
            case INITIAL_POS_X:
                System.out.println("Input new initial position:");
                frameToEdit.setInitialPosX(Double.parseDouble(input.next()));
                break;
            case OCCUR_TIME:
                System.out.println("Input new occur time:");
                frameToEdit.setOccurTime(Double.parseDouble(input.next()));
                break;
            case INITIAL_PROPER_TIME:
                System.out.println("Input new initial clock reading:");
                frameToEdit.setInitialProperTime(Double.parseDouble(input.next()));
                break;
            case INITIAL_VELOCITY:
                System.out.println("Input new initial velocity:");
                double newVel = Double.parseDouble(input.next());
                frameToEdit.setInitialVelocity(newVel);
                break;
            case NAME:
                System.out.println("Set new name:");
                String newName = input.next();
                if (frameWorld.findFrameByName(newName) != null) {
                    System.out.println(newName + "already exists.");
                } else {
                    frameToEdit.setName(newName);
                }
                break;
            case DONE:
                System.out.println("Edition done.");
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
        frameWorld.viewAllInCurrentReference();
    }

    //MODIFIES: this
    //EFFECTS: add a new event to frameList with respect to a chosen frame; if given event' s name is the same
    //         as any existing event, give warning and do not modify this
    @SuppressWarnings("methodlength")
    private void addNewEvent() {
        System.out.print("Enter the name of the new event: ");
        String name = input.next();

        printAllFrameNames();
        System.out.print("Which existing frame is this new event observed in: ");
        String parentFrameName = input.next();
        RefFrame parentFrame = frameWorld.findFrameByName(parentFrameName);
        if (parentFrame == null) {
            printNotExist(parentFrameName);
            return;
        }

        try {
            PhysicalEvent newPhysicalEvent;
            System.out.print("Where is the new event observed in " + parentFrameName + "(unit: c*s): ");
            double initialPosX = Double.parseDouble(input.next());
            System.out.print("When is the new event observed in " + parentFrameName + "(unit: s): ");
            double occurTime = Double.parseDouble(input.next());

            newPhysicalEvent = new PhysicalEvent(name, initialPosX, occurTime, parentFrame);

            frameWorld.addEvent(newPhysicalEvent);
            System.out.println("Event " + name + " is successfully added.");
        } catch (NumberFormatException numberFormatException) {
            printWrongData();
        } catch (EmptyNameException emptyNameException) {
            printEmptyNameWarning();
        } catch (SameNameException sameNameException) {
            System.out.println("Event named " + name + " already exists.");
        }
    }

    //MODIFIES: this
    //EFFECTS: delete chosen event from the simulator, as well as all the corresponding event slot in its initialFrame
    //         If the chosen frame does not exist, give warning and do not change this.
    private void deleteChosenEvent() {
        printAllEventNames();
        System.out.print("Please enter the name of the event you want to delete: ");
        String eventToDeleteName = input.next();

        PhysicalEvent physicalEventToDelete = frameWorld.findEventByName(eventToDeleteName);
        if (physicalEventToDelete == null) {
            printNotExist(eventToDeleteName);
            return;
        }

        frameWorld.deleteEvent(physicalEventToDelete);
        System.out.println(eventToDeleteName + " is deleted.\n");
    }

    // EFFECTS: save the current FrameWorld to resource file
    @SuppressWarnings("methodlength")
    private void saveFrameWorld() {
        try {
            System.out.println("Data in file " + SOURCE_FILE + " will be overwritten by current data. "
                    + "Are you sure to save to file? (Y/N)");
            while (true) {
                String command = input.next();
                if (command.equalsIgnoreCase("Y")) {
                    jsonWriter.open();
                    jsonWriter.write(frameWorld);
                    jsonWriter.close();
                    System.out.println("Data successfully saved to file " + SOURCE_FILE);
                    break;
                } else if (command.equalsIgnoreCase("N")) {
                    System.out.println("Saving cancelled");
                    break;
                } else {
                    System.out.println("Unknown command. Please input (Y/N)");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + SOURCE_FILE + " not found.");
        }
    }


    // MODIFIES: this
    // EFFECTS: load data from SOURCE_FILE and overwrite current frameWorld
    @SuppressWarnings("methodlength")
    private void loadFrameWorld() {
        try {
            FrameWorld savedWorld = jsonReader.read();
            if (!savedWorld.equals(frameWorld)) {
                System.out.println("Your current data will be overwritten by the saved data.\n"
                        + "Are you sure to load from file? (Y/N)");
                while (true) {
                    String command = input.next();
                    if (command.equalsIgnoreCase("Y")) {
                        frameWorld = savedWorld;
                        System.out.println("File from " + SOURCE_FILE + " successfully loaded");
                        break;
                    } else if (command.equalsIgnoreCase("N")) {
                        System.out.println("Loading cancelled");
                        break;
                    } else {
                        System.out.println("Unknown command. Please input (Y/N)");
                    }
                }
            } else {
                frameWorld = savedWorld;
                System.out.println("File from " + SOURCE_FILE + " successfully loaded");
            }
        } catch (IOException e) {
            System.out.println("Failed to load file " + SOURCE_FILE);
        } catch (FrameConstructException e) {
            System.out.println("Frame Construction failed: data in file " + SOURCE_FILE
                    + " might have been manually modified and invalidated");
        } catch (JSONException e) {
            System.out.println("FAILED TO LOAD DATA: JSON File structure is broken.");
        }
    }

    // EFFECTS: print the edit menu
    public void printEditMenu() {
        printSeparationBar();
        System.out.println("Select the status that you want to change:");
        System.out.println("\t" + INITIAL_POS_X + ": initial position");
        System.out.println("\t" + OCCUR_TIME + ": occur time");
        System.out.println("\t" + INITIAL_PROPER_TIME + ": initial clock reading");
        System.out.println("\t" + INITIAL_VELOCITY + ": initial velocity");
        System.out.println("\t" + NAME + ": name");
        System.out.println("\t" + DONE + ": Done");
    }


    // EFFECTS: print separation bar("--------------")
    private void printSeparationBar() {
        System.out.println("-----------------------------------");
    }

    // EFFECTS: print "physicalObjectName does not exist yet."
    private void printNotExist(String physicalObjectName) {
        System.out.println(physicalObjectName + " does not exist yet.");
    }

    // EFFECTS: print warning when wrong type of data is given
    private void printWrongData() {
        System.out.println("Wrong data type input: you should input a number.");
    }

    // EFFECTS: print warning when magnitude of input velocity is larger than or equal to c
    private void printVelocityExceedLightWarning() {
        System.out.println("!!!YOU VIOLATED RELATIVITY: FRAMES CANNOT MOVE AT VELOCITY LARGER THAN/EQUAL TO C!!!");
    }

    // EFFECTS: print warning when given name is empty
    private void printEmptyNameWarning() {
        System.out.println("Name should not be empty.");
    }


    // EFFECTS: print the names of all existing frames
    private void printAllFrameNames() {
        System.out.println("These are names of currently existing frames:");
        System.out.println(frameWorld.frameNameList());
    }

    // EFFECTS: print the names of all existing events
    private void printAllEventNames() {
        System.out.println("These are names of currently existing event:");
        System.out.println(frameWorld.eventNameList());
    }
}
