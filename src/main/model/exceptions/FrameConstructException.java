package model.exceptions;

/*Exceptions due to issues with constructing the frame*/
public class FrameConstructException extends Exception {
    public FrameConstructException() {
        super("FrameConstruction Failed");
    }

    public FrameConstructException(String message) {
        super(message);
    }
}
