package ui.panels.containers;

import model.PhysicalObject;
import ui.GraphicRelativitySimulator;
import ui.panels.editors.EventCreator;
import ui.panels.elements.EventElement;

import java.awt.*;

/*The panel containing all FramePanels*/
public class EventListPanel extends ContainerPanel {
    // constructor
    // EFFECTS: construct a new EventListPanel
    public EventListPanel(GraphicRelativitySimulator simulator) {
        super("Events", "Event", simulator);
        creator = new EventCreator(simulator);
        this.setPreferredSize(new Dimension(WIDTH, (int) (GraphicRelativitySimulator.HEIGHT / 2.2)));
    }

    // REQUIRES: newEvent is successfully added to FrameWorld in simulator
    // MODIFIES: this
    // EFFECTS: create and add a new EventElement to this
    @Override
    public void addNewElement(PhysicalObject newEvent) {
        EventElement newEventElement = new EventElement(newEvent, this);
        addPhysicalObjectElement(newEventElement);
    }

}
