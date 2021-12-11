package ui.panels.containers;

import model.PhysicalEvent;
import model.FrameWorld;
import model.PhysicalObject;
import model.RefFrame;
import ui.GraphicRelativitySimulator;
import ui.panels.editors.FrameCreator;
import ui.panels.editors.FrameEditor;
import ui.panels.elements.FrameElement;
import ui.panels.elements.PhysicalObjectElement;

/* The panel containing all FramePanels*/
public class FrameListPanel extends ContainerPanel {
    private FrameEditor editor;

    // constructor
    // EFFECTS: construct a new FrameListPanel
    public FrameListPanel(GraphicRelativitySimulator parent) {
        super("Frames", "Frame", parent);
        creator = new FrameCreator(simulator);
        editor = new FrameEditor(simulator);
    }

    // REQUIRES: newFrame is successfully added to FrameWorld in simulator
    // MODIFIES: this
    // EFFECTS: create and add a new FrameElement to this
    @Override
    public void addNewElement(PhysicalObject newFrame) {
        boolean changeable = !(newFrame.getName().equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME));
        FrameElement newElement = new FrameElement(newFrame, this, changeable);
        addPhysicalObjectElement(newElement);
    }

    // REQUIRES: data source of objectToDelete is already deleted from simulator's frameWorld
    // MODIFIES: simulator, this
    // EFFECTS: remove objectToDelete, and corresponding Event Elements from simulator
    @Override
    public void deleteElement(PhysicalObjectElement objectToDelete) {
        RefFrame frameObjectToDelete = (RefFrame) objectToDelete.getDataSource();
        for (PhysicalEvent nextPhysicalEvent : frameObjectToDelete.getObservedEvents()) {
            for (PhysicalObjectElement nextEventElement : simulator.getEventListPanel().elements) {
                if (nextEventElement.getDataSource().equals(nextPhysicalEvent)) {
                    simulator.getEventListPanel().deleteElement(nextEventElement);
                    break;
                }
            }
        }

        super.deleteElement(objectToDelete);
    }

    public FrameEditor getEditor() {
        return this.editor;
    }
}
