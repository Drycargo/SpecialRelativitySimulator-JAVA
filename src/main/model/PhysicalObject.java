package model;

import model.exceptions.EmptyNameException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

import static java.lang.Math.sqrt;

/* A physical object with name, initial position and occur time when it is observed (either relative to a reference
   frame or the absolute stationary frame by default).
 - Note: although the calculations should ideally throw ExceedSpeedOfLightExceptions if input v is unreasonable, the
   application of these methods will ensure that such exceptions are never thrown because all frames must be validly
   constructed. Therefore, the REQUIRES clauses are kept*/
public abstract class PhysicalObject implements Writable {
    public static final int ROUND_UP_STANDARD = 10000; // round numbers to the 4th decimal place

    // sThe following fields do not change with time or reference frame
    protected String name; // Name of the Frame
    protected double initialPosX; // Position of the Frame when it is first observed from the stationary frame
    protected double occurTime; // Time when the Frame is first observed from the stationary frame


    // EFFECTS: assign name, initial position and occurTime to the new Physical Object
    public PhysicalObject(String name, double initialPosX, double occurTime) throws EmptyNameException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        }
        this.name = name;
        this.initialPosX = initialPosX;
        this.occurTime = occurTime;
    }

    // EFFECTS: round the decimal number up to standard (4 decimal places)
    public static double roundUp(double rawNumber) {
        return (double) Math.round(rawNumber * ROUND_UP_STANDARD) / ROUND_UP_STANDARD;
    }

    // REQUIRES: -1 < velocity < 1
    // EFFECT: do Lorentz Transformation of time based on given change in time, position and velocity
    public static double lorentzTime(double deltaTime, double deltaX, double velocity) {
        return gamma(velocity) * (deltaTime - deltaX * velocity);
    }

    // REQUIRES: -1 < velocity < 1
    // EFFECT: do inverse Lorentz Transformation of time based on given change in time, position and velocity
    public static double inverseLorentzTime(double deltaTime, double deltaX, double velocity) {
        return lorentzTime(deltaTime, deltaX, -velocity);
    }

    // REQUIRES: -1 < velocity < 1
    // EFFECT: do Lorentz Transformation of X based on given change in time, position and velocity
    public static double lorentzX(double deltaTime, double deltaX, double velocity) {
        return gamma(velocity) * (deltaX - deltaTime * velocity);
    }

    // REQUIRES: -1 < velocity < 1
    // EFFECT: do inverse Lorentz Transformation of X based on given change in time, position and velocity
    public static double inverseLorentzX(double deltaTime, double deltaX, double velocity) {
        return lorentzX(deltaTime, deltaX, -velocity);
    }

    // REQUIRES: -1 < velocity < 1
    // EFFECT: calculate the Lorentz Factor gamma based on given velocity
    public static double gamma(double velocity) {
        return 1 / sqrt(1 - velocity * velocity);
    }

    // REQUIRES: referenceFrame is an existing frame
    // MODIFIES: this
    // EFFECT: change this to be viewed from the given reference frame
    public abstract void viewInGivenFrame(RefFrame referenceFrame);

    // EFFECTS: return the current status(position, proper time, velocity) of this
    public abstract String currentInfo();


    public double getOccurTime() {
        return occurTime;
    }

    public double getInitialPosX() {
        return initialPosX;
    }

    public String getName() {
        return this.name;
    }

    // EFFECTS: return this as a JSONObjective
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("initialPosX", this.initialPosX);
        json.put("occurTime", this.occurTime);

        return json;
    }

    // EFFECTS: return true if o has same name, initial position, occur Time and name as this
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhysicalObject that = (PhysicalObject) o;
        return Double.compare(that.initialPosX, initialPosX) == 0
                && Double.compare(that.occurTime, occurTime) == 0
                && name.equals(that.name);
    }

    // EFFECTS: return the hashCode for this
    @Override
    public int hashCode() {
        return Objects.hash(name, initialPosX, occurTime);
    }
}
