package model;

import model.exceptions.EmptyNameException;
import model.exceptions.ExceedSpeedOfLightException;
import model.exceptions.FrameConstructException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/*A relativistic inertial reference frame (abbreviated as frame) in Physics.
 *There exists an "absolute" stationary frame, which we refer to as the default frame with velocity equal to 0*/
public class RefFrame extends PhysicalObject implements ContainsEvents {
    private double initialProperTime; // Reading of the clock contained in the frame when it is first observed
    private double initialVelocity; // Velocity of the Frame relative to the stationary frame

    private double velocity; // Relative velocity of Frame, viewed from the current reference frame

    // The following field can be directly changed by User when Frame is selected as the reference frame
    private double properTime; // Current reading of the clock in the Frame

    // The following fields cannot be directly changed by User; they tend to change when frame or time is changed
    private double posX; // Position of Frame, viewed from the current reference frame

    private List<PhysicalEvent> observedPhysicalEvents; // Events that are observed by this

    // EFFECTS: constructs a frame related to the absolute stationary frame, with given name, initial position,
    //          time when it is observed (occurTime), initial proper time and velocity; its current position, proper
    //          time and velocity are set to given initial position, initial proper time and initial velocity
    //          throws ExceedSpeedOfLightException if given initialVelocity >= 1 or initialVelocity <= -1
    //          throws EmptyNameException if given name is empty
    public RefFrame(String name, double initialPosX, double occurTime, double initialProperTime, double initialVelocity)
            throws EmptyNameException, ExceedSpeedOfLightException {
        super(name, initialPosX, occurTime);

        if (initialVelocity >= 1 || initialVelocity <= -1) {
            throw new ExceedSpeedOfLightException();
        }

        this.initialProperTime = initialProperTime;
        this.initialVelocity = initialVelocity;
        this.posX = initialPosX;
        this.properTime = initialProperTime;
        this.velocity = initialVelocity;
        this.observedPhysicalEvents = new LinkedList<PhysicalEvent>();
    }

    // REQUIRES: referenceFrame already exists in FrameWorld
    // EFFECTS: construct a frame related to referenceFrame
    //          throws ExceedSpeedOfLightException if given initialVelocity >= 1 or initialVelocity <= -1
    //          throws EmptyNameException if given name is empty
    public RefFrame(String name, double initialPosX, double occurTime, double initialProperTime, double initialVelocity,
                    RefFrame referenceFrame) throws EmptyNameException, ExceedSpeedOfLightException {
        this(name, initialPosX, occurTime, initialProperTime, initialVelocity);
        RefFrame mirrorFrame = new RefFrame("mirror", 0, 0, 0, 0);
        mirrorFrame.viewInGivenFrame(referenceFrame); //mirrorFrame is how stationary frame looks from referenceFrame
        mirrorFrame.initialPosX = mirrorFrame.getPosX();
        mirrorFrame.initialVelocity = mirrorFrame.getVelocity();
        mirrorFrame.occurTime = referenceFrame.getProperTime();
        mirrorFrame.initialProperTime = mirrorFrame.getProperTime();

        //Now regard reference frame as stationary, and mirrorFrame(original stationary frame) as a new reference
        this.viewInGivenFrame(mirrorFrame);
        this.initialPosX = this.posX;
        this.initialVelocity = this.velocity;
        this.occurTime = mirrorFrame.getProperTime();
        this.initialProperTime = this.properTime;
    }

    // REQUIRES: referenceFrame is an existing frame
    // MODIFIES: this
    // EFFECT: change the position, proper time and velocity as viewed from the given reference frame
    @Override
    public void viewInGivenFrame(RefFrame referenceFrame) {
        if (this.initialVelocity == referenceFrame.initialVelocity) {
            this.posX = (this.positionWhenZero() - referenceFrame.positionWhenZero())
                    * gamma(referenceFrame.initialVelocity);
            this.velocity = 0;
            this.properTime = this.initialProperTime + referenceFrame.properTime - referenceFrame.initialProperTime;
        } else {
            MeetEvent meetEvent = this.meetingEvent(referenceFrame);
            double relativeV = this.relativeVelocity(referenceFrame);
            double refProperTimeWhenMeet = referenceFrame.properTimeAtEvent(meetEvent);
            double thisProperTimeWhenMeet = this.properTimeAtEvent(meetEvent);
            double deltaT = referenceFrame.getProperTime() - refProperTimeWhenMeet; //time changed in reference frame
            this.posX = deltaT * relativeV;
            this.properTime = thisProperTimeWhenMeet + deltaT / gamma(relativeV);
            this.velocity = relativeV;
        }
    }


    // EFFECTS: return the position of this when time is 0 from the perspective of the stationary frame
    public double positionWhenZero() {
        return this.initialPosX - this.occurTime * this.initialVelocity;
    }

    // REQUIRES: this has passed the location of event at the same time that event occurs
    // EFFECTS: return this' s proper time (reading of the clock moving along with this) from the perspective of the
    //          stationary frame
    public double properTimeAtEvent(MeetEvent meetEvent) {
        double deltaT = meetEvent.getOccurTime() - this.getOccurTime();
        double deltaX = meetEvent.getPosX() - this.getInitialPosX();
        return lorentzTime(deltaT, deltaX, this.initialVelocity) + this.initialProperTime;
    }

    // EFFECT: calculate the (relativistic) relative velocity of this from the perspective of the given referenceFrame
    //         based on formula: u' = (u - v) / (1 - u * v / c^2), c^2 is cancelled out by the unit of u and v
    //         Due to mathematical nature of the formula, this function will never throw ExceedSpeedOfLightException
    public double relativeVelocity(RefFrame referenceFrame) {
        double u = this.initialVelocity;
        double v = referenceFrame.initialVelocity;
        return (u - v) / (1 - u * v);
    }

    // REQUIRES: frame1 and frame2 do not have same velocity
    // EFFECT: produce the event(location and time) of the two frames meeting from the perspective of
    //         the stationary frame. It is calculated based on the simple equation:
    //         InitialX1 + V1 * (meetTime - OccurTime1) = InitialX2 + V2 * (meetTime - OccurTime2)
    public MeetEvent meetingEvent(RefFrame frame1) {
        double v1 = frame1.initialVelocity;
        double t1 = frame1.occurTime;
        double x1 = frame1.initialPosX;
        double v2 = this.initialVelocity;
        double t2 = this.occurTime;
        double x2 = this.initialPosX;

        double meetTime = (x2 - x1 + v1 * t1 - v2 * t2) / (v1 - v2) + 0.0; //+0.0 to prevent test failure due to -0.0
        double meetPosX = x1 + v1 * (meetTime - t1) + 0.0;

        return new MeetEvent(meetPosX, meetTime);
    }

    // EFFECTS: create a new Frame with all features the same as this, except for name (which will be newName instead)
    //          return the new frame
    //          throws FrameConstructException if given newName is empty
    //          The function will never throw ExceedSpeedOfLightException because this is validly constructed; for
    //          convenience of testing and code coverage, whenever duplicate Frame throws FrameConstructException,
    //          it will be interpreted as throwing EmptyNameException
    public RefFrame duplicateFrame(String newName) throws FrameConstructException {
        RefFrame newFrame = new RefFrame(newName, this.initialPosX, this.occurTime,
                this.initialProperTime, this.initialVelocity);
        newFrame.properTime = this.properTime;
        newFrame.posX = this.posX;
        newFrame.velocity = this.velocity;

        return newFrame;
    }

    // EFFECTS: return the current status(position, proper time, velocity) of this
    @Override
    public String currentInfo() {
        StringBuilder currentInfo = new StringBuilder();
        currentInfo.append("<--" + this.name + "-->\n");
        currentInfo.append("X: " + roundUp(this.posX) + "c*s\n");
        currentInfo.append("Clock Reading: " + roundUp(this.properTime) + "s\n");
        currentInfo.append("Velocity: " + roundUp(this.velocity) + "c\n");

        return currentInfo.toString();
    }

    // EFFECTS: return the initial status(initial position, occur time, initial Proper time, initial velocity) of this
    public String initialInfo() {
        StringBuilder initialInfo = new StringBuilder();
        initialInfo.append("<--" + this.getName() + "-->\n");
        initialInfo.append("X when it is observed in stationary frame: " + roundUp(this.initialPosX) + "c*s\n");
        initialInfo.append("Time when it is observed: " + roundUp(this.occurTime) + "s\n");
        initialInfo.append("Initial Clock Reading: " + roundUp(this.initialProperTime) + "s\n");
        initialInfo.append("Initial Velocity: " + roundUp(this.initialVelocity) + "c\n");

        return initialInfo.toString();
    }

    public double getVelocity() {
        return velocity;
    }

    // MODIFIES: this
    // EFFECTS: set this' s occurTime to newOccurTime
    //          log this change
    public void setOccurTime(double newOccurTime) {
        this.occurTime = newOccurTime;
        EventLog.getInstance().logEvent(
                new Event("Frame " + this.name + "'s occur time is set to: " + newOccurTime + "s"));
    }

    // MODIFIES: this
    // EFFECTS: set this' s initialPosX to newInitialPosX
    //          log this change
    public void setInitialPosX(double newInitialPosX) {
        this.initialPosX = newInitialPosX;
        EventLog.getInstance().logEvent(
                new Event("Frame " + this.name + "'s initial position is set to: " + newInitialPosX + "c*s"));
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getPosX() {
        return posX;
    }

    // MODIFIES: this
    // EFFECTS: set this' s initialVelocity to newInitialVelocity
    //          log this change
    //          throws ExceedSpeedOfLightException if newInitialVelocity's absolute value is larger than or equal to 1
    public void setInitialVelocity(double newInitialVelocity) throws ExceedSpeedOfLightException {
        if (newInitialVelocity >= 1 || newInitialVelocity <= -1) {
            throw new ExceedSpeedOfLightException();
        }
        this.initialVelocity = newInitialVelocity;
        EventLog.getInstance().logEvent(
                new Event("Frame " + this.name + "'s initial velocity is set to: " + newInitialVelocity + "c"));
    }

    public double getInitialProperTime() {
        return this.initialProperTime;
    }

    // MODIFIES: this
    // EFFECTS: set this' s initialProperTime to newInitialProperTime
    //          log this change
    public void setInitialProperTime(double newInitialProperTime) {
        this.initialProperTime = newInitialProperTime;
        EventLog.getInstance().logEvent(
                new Event("Frame " + this.name + "'s initial Proper Time is set to: " + newInitialProperTime + "s"));
    }

    public double getProperTime() {
        return properTime;
    }

    public void setProperTime(double newProperTime) {
        this.properTime = newProperTime;
    }

    // MODIFIES: this
    // EFFECTS: set this' s name to newName
    //          log this change
    //          throws EmptyNameException if newName is empty
    public void setName(String newName) throws EmptyNameException {
        if (newName.isEmpty()) {
            throw new EmptyNameException();
        }
        EventLog.getInstance().logEvent(
                new Event("Frame " + this.name + "'s name is set to: " + newName));
        this.name = newName;
    }


    // EFFECTS: return the size of observedEvents
    public int getEventsSize() {
        return this.observedPhysicalEvents.size();
    }


    // MODIFIES: this
    // EFFECTS: add newEvent to observedEvents
    public void addObservedEvent(PhysicalEvent newPhysicalEvent) {
        this.observedPhysicalEvents.add(newPhysicalEvent);
    }

    // EFFECTS: if event named eventName exists in this' s observedEvents, return the found event;
    //          otherwise, return null
    @Override
    public PhysicalEvent findEventByName(String eventName) {
        for (PhysicalEvent nextPhysicalEvent : observedPhysicalEvents) {
            if (nextPhysicalEvent.getName().equals(eventName)) {
                return nextPhysicalEvent;
            }
        }
        return null;
    }

    // REQUIRES: eventToDelete exists in observedEvents
    // MODIFIES: this
    // EFFECTS: remove eventToDelete from this' s observedEvents
    public void deleteEvent(PhysicalEvent physicalEventToDelete) {
        this.observedPhysicalEvents.remove(physicalEventToDelete);
    }

    // REQUIRES: eventList contains all elements of observedEvents
    // MODIFIES: eventList
    // EFFECTS: remove all events in this' s observedEvents from eventList
    public void removeAllEventsInGivenEventList(List<PhysicalObject> eventList) {
        eventList.removeAll(this.observedPhysicalEvents);
    }

    // EFFECTS: return an unmodifiable list of this' s observed events
    public List<PhysicalEvent> getObservedEvents() {
        return Collections.unmodifiableList(this.observedPhysicalEvents);
    }

    // EFFECTS: return the names of all observedEvents
    @Override
    public String eventNameList() {
        StringBuilder allEventNames = new StringBuilder();
        for (PhysicalEvent nextPhysicalEvent : observedPhysicalEvents) {
            allEventNames.append(nextPhysicalEvent.getName());
            allEventNames.append("\n");
        }
        return allEventNames.toString();
    }

    // EFFECTS: return this as a JSONObjective
    //          information of events are stored individually in the events instead of in each frame
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("initialProperTime", this.initialProperTime);
        json.put("initialVelocity", this.initialVelocity);

        return json;
    }

    // EFFECTS: return true if o has same features as this (the observedEvents are not checked and
    //          left as the task for frameWorld)
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        RefFrame frame = (RefFrame) o;
        return Double.compare(frame.getInitialProperTime(), this.getInitialProperTime()) == 0
                && Double.compare(frame.getInitialVelocity(), this.getInitialVelocity()) == 0;
    }

    // EFFECTS: return the hashCode for this
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), initialProperTime, initialVelocity);
    }
}
