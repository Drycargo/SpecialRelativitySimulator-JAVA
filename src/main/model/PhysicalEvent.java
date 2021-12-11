package model;

import model.exceptions.EmptyNameException;
import org.json.JSONObject;

import java.util.Objects;

/*An Event occurring in the frame world. Note that its is not a super or subclass of MeetEvent class,
 * because it is an actual event with name and frame that it is observed in.
 * It has the following attributes:
 * - Name
 * - The given frame in which it is observed
 * - position where it occurs in the given frame (initialPosX)
 * - time when it occurs in the given frame (occurTime)
 * - position where it occurs in the current reference frame
 * - time when it occurs in the current reference frame
 * - status of whether it has occurred in the current reference frame or not
 * */
public class PhysicalEvent extends PhysicalObject {
    private RefFrame initialFrame;
    private double currentOccurX;
    private double currentOccurTime;
    private boolean hasOccurred;

    // REQUIRES: referenceFrame is the current reference frame in frameWorld
    // EFFECTS: create an event with given name occurring at (x, t) = (initialPox, occurTime) in initialFrame;
    //          updates its status to satisfy the current proper time in referenceFrame
    public PhysicalEvent(String name, double initialPosX, double occurTime, RefFrame referenceFrame)
            throws EmptyNameException {
        super(name, initialPosX, occurTime);
        this.initialFrame = referenceFrame;
        this.hasOccurred = false;
        referenceFrame.addObservedEvent(this);
    }

    // REQUIRES: referenceFrame is the current reference frame in the frame world (i.e., initialFrame has been
    //           updated so that it is viewed in referenceFrame)
    // MODIFIES: this
    // EFFECTS: view the event in given referenceFrame by giving its position and time in referenceFrame when it
    //          occurred, and whether it has occurred or not;
    //          if this changed from not occurred to hasOccurred, log this event
    @Override
    public void viewInGivenFrame(RefFrame referenceFrame) {
        if (this.initialFrame.getInitialVelocity() == referenceFrame.getInitialVelocity()) {
            this.currentOccurX = this.initialPosX + this.initialFrame.getPosX();
            this.currentOccurTime = this.occurTime
                    - (this.initialFrame.getProperTime() - referenceFrame.getProperTime());
        } else {
            MeetEvent meetEvent = this.initialFrame.meetingEvent(referenceFrame);
            double refProperTimeWhenMeet = referenceFrame.properTimeAtEvent(meetEvent);
            double thisFrameProperTimeWhenMeet = this.initialFrame.properTimeAtEvent(meetEvent);
            double deltaX = this.initialPosX;
            double deltaT = this.occurTime - thisFrameProperTimeWhenMeet;
            double velocity = this.initialFrame.getVelocity();
            this.currentOccurTime = refProperTimeWhenMeet + inverseLorentzTime(deltaT, deltaX, velocity);
            this.currentOccurX = inverseLorentzX(deltaT, deltaX, velocity);
        }

        if (referenceFrame.getProperTime() >= this.currentOccurTime) {
            if (!this.hasOccurred) {
                EventLog.getInstance().logEvent(new Event("Event " + this.name + " is observed to occur at: ("
                        + this.currentOccurX + "c*s, " + this.currentOccurTime + "s) in " + referenceFrame.getName()));
            }
            this.hasOccurred = true;
        } else {
            this.hasOccurred = false;
        }
        //this.hasOccurred = (referenceFrame.getProperTime() >= this.currentOccurTime);
        // Event has occurred if the proper time in the current reference >= its occur time
    }

    @Override
    public String currentInfo() {
        StringBuilder currentInfo = new StringBuilder();
        currentInfo.append("{--" + this.name + "--}");
        currentInfo.append("(First observed in " + this.initialFrame.getName() + ")\n");
        if (this.hasOccurred) {
            currentInfo.append("Occurred at position: " + roundUp(this.getCurrentOccurX()) + "c*s\n"
                    + "Occurred at time: " + roundUp(this.getCurrentOccurTime()) + "s\n");
        } else {
            currentInfo.append("Not observed yet\n");
        }
        return currentInfo.toString();
    }

    public double getCurrentOccurX() {
        return this.currentOccurX;
    }

    public double getCurrentOccurTime() {
        return this.currentOccurTime;
    }

    public boolean hasOccurred() {
        return this.hasOccurred;
    }

    public RefFrame getInitialFrame() {
        return initialFrame;
    }

    // EFFECTS: return this as a JSONObjective
    //          initialFrame is stored as the name of the frame, so that we can refer to the Frame object directly
    //          by findFrameByName
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("initialFrame", this.initialFrame.getName());
        return json;
    }

    // EFFECTS: return true if o has same features as this (the initialFrame is only checked by its
    //          name assuming that frameWorld is validly constructed)
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        PhysicalEvent physicalEvent = (PhysicalEvent) o;
        return initialFrame.getName().equals(physicalEvent.initialFrame.getName());
    }

    // EFFECTS: return the hashCode for this
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), initialFrame, currentOccurX, currentOccurTime, hasOccurred);
    }
}
