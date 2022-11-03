package model;

/*An event specifically describes when and where two frames meet in the absolute stationary frame. Note that
although it is semantically an event, it differs from Event class because we do not need to specify its name
or the frame it refers to, and do not update with any changes of the system; it is a temporary tool used for
us to assist the Lorentz Transformations
It has the following attributes:
* position where it occurs in the Absolute Stationary Frame
* time when it occurs in the Absolute Stationary Frame*/
public class MeetEvent {
    private double posX;
    private double occurTime;

    //EFFECTS: create an event relative to Absolute Stationary Frame
    public MeetEvent(double posX, double occurTime) {
        this.posX = posX;
        this.occurTime = occurTime;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getOccurTime() {
        return this.occurTime;
    }
}
