package model.exceptions;

/*Exception corresponding to cases when user tries to input set the speed of the frame to be larger than
* or equal to c;
* This exception should not be thrown if the velocity appears to be 1c or -1c due to round up*/
public class ExceedSpeedOfLightException extends FrameConstructException {
    public ExceedSpeedOfLightException() {
        super("!!!PHYSICAL LAW VIOLATED: NO MASSIVE OBJECT EXCEEDS SPEED OF LIGHT!!!");
    }
}
