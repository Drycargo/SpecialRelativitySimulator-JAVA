package model;


import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

/*A physical universe containing a list of Frames and Events;
 *it must include an absolute stationary frame;
 *all frames must have different names so that they can be traced by their name*/
public class FrameWorld implements ContainsEvents, Writable {
    public static final String ABSOLUTE_STATIONARY_FRAME_NAME = "Absolute Stationary Frame";
    public static final String CURRENT_FRAMEWORLD_NAME = "Current FrameWorld";
    public static final String TEMPORARY_FRAMEWORLD = "Saved FrameWorld";

    private String name; // name used to indicate whether this is the current frame world or a temporary loaded one
    private List<PhysicalObject> frameList; //the list of frames
    private List<PhysicalObject> eventList; //the list of events
    private RefFrame absoluteStationaryFrame; //the stationary frame(ideal)
    private RefFrame currentReference; //the current reference frame

    // EFFECTS: construct the frameWorld by initializing the frameList (containing Absolute Stationary Frame which is
    //          set to be the current reference frame) and an empty eventList.
    //          the name of this is CURRENT_FRAMEWORLD_NAME by default
    //          The two lists are set as LinkedList to keep the order of insertion and allow convenience for deletion
    //          of elements; maps could not work well because it is not possible to change the key of elements without
    //          disturbing the order
    public FrameWorld() throws FrameConstructException {
        name = CURRENT_FRAMEWORLD_NAME;
        frameList = new LinkedList<PhysicalObject>();
        eventList = new LinkedList<PhysicalObject>();
        absoluteStationaryFrame = new RefFrame(ABSOLUTE_STATIONARY_FRAME_NAME, 0, 0, 0, 0);
        frameList.add(absoluteStationaryFrame);
        currentReference = absoluteStationaryFrame;
    }

    // EFFECTS: Look for frame with given frameName; if found, return the frame object; if not, return null
    public RefFrame findFrameByName(String frameName) {
        return (RefFrame) findPhysicalObjectByName(frameName, frameList);
    }

    // EFFECTS: Look for event with given eventName; if found, return the events object; if not, return null
    @Override
    public PhysicalEvent findEventByName(String eventName) {
        return (PhysicalEvent) findPhysicalObjectByName(eventName, eventList);
    }

    // private helper method
    // REQUIRES: physicalObjects is either frameList or eventList
    // EFFECTS: return the PhysicalObject with name in physicalObjects; if not found, return null
    private PhysicalObject findPhysicalObjectByName(String name, List<PhysicalObject> physicalObjects) {
        for (PhysicalObject nextPhysicalObject : physicalObjects) {
            if (name.equals(nextPhysicalObject.getName())) {
                return nextPhysicalObject;
            }
        }

        return null;
    }

    // REQUIRES: newFrame is constructed with respect to the current reference frame
    // MODIFIES: this
    // EFFECTS: add newFrame to the end of frameList
    //          throws SameNameException if there exists frame in frameList with the same name as newFrame
    public void addFrame(PhysicalObject newFrame) throws SameNameException {
        addPhysicalObject(newFrame, frameList);
    }

    // REQUIRES: newEvent is constructed with respect to the current reference frame
    // MODIFIES: this
    // EFFECTS: add newEvent to the end of eventList
    //          throws SameNameException if there exists event in eventList with the same name as newEvent
    public void addEvent(PhysicalObject newEvent) throws SameNameException {
        addPhysicalObject(newEvent, eventList);
    }

    // private helper
    // MODIFIES: this
    // EFFECTS: add newPhysicalObject to the end of eventList
    //          throws SameNameException if there exists physicalObject in given physicalObjects with the
    //          same name as newPhysicalObject
    private void addPhysicalObject(PhysicalObject newPhysicalObject, List<PhysicalObject> physicalObjects)
            throws SameNameException {
        if (findPhysicalObjectByName(newPhysicalObject.getName(), physicalObjects) == null) {
            newPhysicalObject.viewInGivenFrame(currentReference);
            physicalObjects.add(newPhysicalObject);
            EventLog.getInstance().logEvent(
                    new Event(newPhysicalObject.getClass().getSimpleName() + " "
                            + newPhysicalObject.name + " added to " + this.name));
        } else {
            throw new SameNameException();
        }
    }

    // MODIFIES: this
    // EFFECTS: adjust the posX, properTime and velocity of all existing frames to those viewed from the perspective of
    //          the current reference frame;
    //          adjust the occurTime, position and hasOccurred of all events to those viewed from the perspective of
    //          the current reference frame;
    public void viewAllInCurrentReference() {
        viewPhysicalObjectsInCurrentReference(frameList);
        viewPhysicalObjectsInCurrentReference(eventList);
    }

    // private helper
    // REQUIRES: physicalObjects is either frameList or eventList
    // MODIFIES: this
    // EFFECTS: adjust identities of all PhysicalObjects in physicalObjects to be viewed in current reference frame
    private void viewPhysicalObjectsInCurrentReference(List<PhysicalObject> physicalObjects) {
        for (PhysicalObject next : physicalObjects) {
            next.viewInGivenFrame(currentReference);
        }
    }

    // MODIFIES: this
    // EFFECTS: change the proper time of the current reference frame, then update all frames
    public void setCurrentReferenceProperTime(double newProperTime) {
        this.currentReference.setProperTime(newProperTime);
        viewAllInCurrentReference();
        EventLog.getInstance().logEvent(
                new Event("Proper Time of reference frame (" + this.currentReference.getName() + ") of "
                        + this.name + " is set to " + PhysicalObject.roundUp(newProperTime) + "s"));
    }

    // REQUIRES: newReference exists in frameList
    // MODIFIES: this
    // EFFECTS: set the current reference frame to the chosen newReference
    public void setCurrentReferenceAndUpdate(RefFrame newReference) {
        this.currentReference = newReference;
        viewAllInCurrentReference();
        EventLog.getInstance().logEvent(
                new Event(newReference.getName() + " is set as the new reference frame of " + this.name));
    }

    // REQUIRES: frameToDelete exists in frameList, and is not the stationaryFrame
    // MODIFIES: this
    // EFFECTS: remove frameToDelete from frameList; if frameToDelete is the current reference Frame, reset the current
    //          reference frame to Absolute Stationary Frame and update
    //          All events observed in frameToDelete are removed at the same time
    public void deleteFrame(RefFrame frameToDelete) {
        if (currentReference.equals(frameToDelete)) {
            setCurrentReferenceAndUpdate(absoluteStationaryFrame);
        }

        List<PhysicalEvent> eventsToDelete = new LinkedList<>(frameToDelete.getObservedEvents());
        for (PhysicalEvent eventToDelete : eventsToDelete) {
            deleteEvent(eventToDelete);
        }

        frameList.remove(frameToDelete);

        EventLog.getInstance().logEvent(
                new Event("Frame " + frameToDelete.getName() + " is deleted from " + this.name));
    }

    // REQUIRES: eventToDelete exists in frameList
    // MODIFIES: this, eventToDelete.getInitialFrame()
    // EFFECTS: remove eventToDelete from eventList and the observedEvents from its initialFrame
    public void deleteEvent(PhysicalEvent physicalEventToDelete) {
        RefFrame targetFrame = physicalEventToDelete.getInitialFrame();
        targetFrame.deleteEvent(physicalEventToDelete);
        eventList.remove(physicalEventToDelete);
        EventLog.getInstance().logEvent(
                new Event("Event " + physicalEventToDelete.getName() + " is deleted from " + this.name));
    }

    // EFFECTS: return the apparent status (position, proper time, velocity) of all frames under the perspective of
    //          the current reference frame; emphasize the current reference
    public String allFramesInfo() {
        StringBuilder allInfo = new StringBuilder();
        for (PhysicalObject nextFrame : frameList) {
            if (nextFrame.getName().equals(currentReference.getName())) {
                allInfo.append("[Reference Frame]");
            }
            allInfo.append(nextFrame.currentInfo());
            allInfo.append("\n");
        }
        return allInfo.toString();
    }

    // EFFECTS: return the apparent status (initial frame, position and occur time if occurred in current frame) of
    //          all events under the perspective of the current reference frame
    public String allEventsInfo() {
        StringBuilder allInfo = new StringBuilder();
        for (PhysicalObject nextEvent : eventList) {
            allInfo.append(nextEvent.currentInfo());
            allInfo.append("\n");
        }
        return allInfo.toString();
    }

    // EFFECTS: return a list containing names of all frames
    public List<String> getFrameNames() {
        List<String> frameNames = new ArrayList<String>();
        for (PhysicalObject nextFrame : frameList) {
            frameNames.add(nextFrame.getName());
        }
        return frameNames;
    }

    // EFFECTS: return the list of names of all frames
    public String frameNameList() {
        return physicalObjectNameList(frameList);
    }

    // EFFECTS: return the names of all events
    @Override
    public String eventNameList() {
        return physicalObjectNameList(eventList);
    }

    // EFFECTS: return the names of all physicalObjects in chosen list
    private String physicalObjectNameList(List<PhysicalObject> physicalObjects) {
        StringBuilder allNames = new StringBuilder();
        for (PhysicalObject next : physicalObjects) {
            allNames.append(next.getName());
            allNames.append("\n");
        }
        return allNames.toString();
    }

    public RefFrame getCurrentReference() {
        return currentReference;
    }

    public RefFrame getAbsoluteStationaryFrame() {
        return absoluteStationaryFrame;
    }

    // EFFECTS: return this as a JSONObjective
    //          currentReference is stored as the name of the frame, so that we can refer to the Frame object directly
    //          by findFrameByName
    //          The proper time of currentReference is stored to update the frames and events
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("frameList", physicalObjectsToJson(frameList));
        json.put("eventList", physicalObjectsToJson(eventList));
        json.put("currentReference", this.currentReference.getName());
        json.put("referenceProperTime", this.currentReference.getProperTime());

        return json;
    }

    // EFFECTS: return the list of PhysicalObjects as a corresponding JSON array;
    //          Note that information of absolute stationary frame will not be recorded because it always gets
    //          initialized when frameWorld is constructed
    private JSONArray physicalObjectsToJson(List<PhysicalObject> physicalObjects) {
        JSONArray jsonArray = new JSONArray();
        for (PhysicalObject next : physicalObjects) {
            if (next.getName().equals(ABSOLUTE_STATIONARY_FRAME_NAME)) {
                continue;
            }
            jsonArray.put(next.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: return an unmodifiable list containing all frames in FrameWorld
    public List<PhysicalObject> getFrameList() {
        return getPhysicalObjectList(frameList);
    }

    // EFFECTS: return an unmodifiable list containing all events in FrameWorld
    public List<PhysicalObject> getEventList() {
        return getPhysicalObjectList(eventList);
    }

    // EFFECTS: return an unmodifiable list containing all indicated PhysicalObjects in FrameWorld
    private List<PhysicalObject> getPhysicalObjectList(List<PhysicalObject> physicalObjects) {
        return Collections.unmodifiableList(physicalObjects);
    }

    // EFFECTS: return true if all parts of o and this are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrameWorld that = (FrameWorld) o;
        return getFrameList().equals(that.getFrameList())
                && getEventList().equals(that.getEventList())
                && currentReference.equals(that.currentReference)
                && Double.compare(currentReference.getProperTime(), that.currentReference.getProperTime()) == 0;
    }

    // EFFECTS: return the hashCode for this
    @Override
    public int hashCode() {
        return Objects.hash(frameList, eventList, currentReference, currentReference.getProperTime());
    }

    public String getName() {
        return this.name;
    }

    // MODIFIES: this
    // EFFECTS: if isCurrentFrameWorld == TRUE, set the name of this as CURRENT_FRAMEWORLD_NAME;
    //          otherwise, set the name as TEMPORARY_FRAMEWORLD
    public void markAsCurrentFrameWorld(boolean isCurrentFrameWorld) {
        this.name = (isCurrentFrameWorld ? CURRENT_FRAMEWORLD_NAME : TEMPORARY_FRAMEWORLD);
    }
}
