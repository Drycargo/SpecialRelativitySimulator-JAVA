package ui.panels.elements;

import model.PhysicalEvent;
import model.PhysicalObject;
import ui.panels.containers.ContainerPanel;
import ui.panels.containers.EventListPanel;

import javax.swing.*;
import java.awt.*;

/*Information panel of an individual Element Object*/
public class EventElement extends PhysicalObjectElement {
    public static final int WIDTH = EventListPanel.WIDTH;
    public static final String NOT_OBSERVED_INFO = "Not Observed Yet";
    public static final int HEIGHT = 100;
    private JTextField currentPosXDatum;
    private JTextField currentTimeDatum;
    private JTextField initialInfoDatum;

    // constructor
    // EFFECTS: initialize the fields, graphic components and functions of a graphic event element
    public EventElement(PhysicalObject event, ContainerPanel parent) {
        super(event, parent);
    }

    // MODIFIES: this
    // EFFECTS: initialize the data textFields and corresponding labels
    @Override
    public void initializeFields(PhysicalObject event) {
        super.initializeFields(event);
        createDataLabel("Position (unit: c*s):");
        currentPosXDatum = createDataTextField();
        createDataLabel("Time (unit:s):");
        currentTimeDatum = createDataTextField();
        createDataLabel("Initial Observer:");
        initialInfoDatum = createDataTextField();
        setInitialStatus((PhysicalEvent) event);
    }

    // MODIFIES: this
    // EFFECTS: set the initial info for this
    private void setInitialStatus(PhysicalEvent physicalEvent) {
        initialInfoDatum.setText(physicalEvent.getInitialFrame().getName() + " at "
                + "(" + PhysicalObject.roundUp(physicalEvent.getInitialPosX()) + " c*s, "
                + PhysicalObject.roundUp(physicalEvent.getOccurTime()) + " s)");
    }

    // MODIFIES: this
    // EFFECTS: initialize the graphic layouts for this
    @Override
    public void initializeGraphics() {
        super.initializeGraphics();
        editionPanel.setLayout(new GridLayout(1, 2));
        editionPanel.add(nameLabel);
        editionPanel.add(removeButton);

        add(editionPanel, BorderLayout.NORTH);
        infoPanel.setLayout(new GridLayout(3, 2));
    }

    // MODIFIES: this
    // EFFECTS: initialize the remove function for this
    @Override
    protected void initializeFunctions() {
        removeAction = e -> {
            parent.getSimulator().getFrameWorld().deleteEvent((PhysicalEvent) this.dataSource);
            parent.deleteElement(this);
            parent.getSimulator().updateAll();
        };

        removeButton.addActionListener(removeAction);
    }

    // MODIFIES: this
    // EFFECTS: initialize the designed visual style
    @Override
    public void initializeStyle() {
        super.initializeStyle();
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
    }

    // MODIFIES: this
    // EFFECTS: update the current position and time info of this according to the current status of data source
    @Override
    public void updateInfo() {
        PhysicalEvent physicalEvent = (PhysicalEvent) dataSource;
        if (physicalEvent.hasOccurred()) {
            this.currentPosXDatum.setText(String.valueOf(PhysicalObject.roundUp(physicalEvent.getCurrentOccurX())));
            this.currentTimeDatum.setText(String.valueOf(PhysicalObject.roundUp(physicalEvent.getCurrentOccurTime())));
        } else {
            this.currentPosXDatum.setText(NOT_OBSERVED_INFO);
            this.currentTimeDatum.setText(NOT_OBSERVED_INFO);
        }
        setInitialStatus(physicalEvent);
    }
}
