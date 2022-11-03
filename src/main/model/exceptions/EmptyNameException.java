package model.exceptions;

/*Exception corresponding to cases when user inputs an empty name*/
public class EmptyNameException extends FrameConstructException {
    public EmptyNameException() {
        super("PhysicalObject should not have an empty name");
    }
}
