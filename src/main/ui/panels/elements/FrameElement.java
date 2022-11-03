package ui.panels.elements;

import model.PhysicalObject;
import model.RefFrame;
import ui.panels.containers.ContainerPanel;
import ui.panels.containers.FrameListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*Information panel of an individual RefFrame Object*/
public class FrameElement extends PhysicalObjectElement {
    public static final int WIDTH = FrameListPanel.WIDTH;
    public static final int HEIGHT = 100;

    private static ActionListener setAsReferenceAction;
    private static ActionListener editAction;

    private JTextField posXDatum;
    private JTextField properTimeDatum;
    private JTextField velocityDatum;

    private JButton editionButton;
    private JButton setReferenceButton;

    // REQUIRES: frame has been successfully added to FrameList
    // EFFECTS: construct a new FrameElement
    public FrameElement(PhysicalObject frame, ContainerPanel parent, boolean changeable) {
        super(frame, parent);
        if (!changeable) {
            editionButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }

    // MODIFIES: this
    // EFFECTS: initialize fields for this
    @Override
    protected void initializeFields(PhysicalObject physicalObject) {
        super.initializeFields(physicalObject);

        // info labels and textAreas
        createDataLabel("Position(c*s):");
        posXDatum = createDataTextField();
        createDataLabel("Clock Reading(s):");
        properTimeDatum = createDataTextField();
        createDataLabel("Speed(c):");
        velocityDatum = createDataTextField();

        // Edition buttons
        editionButton = new JButton("Edit Initial Status");
        setReferenceButton = new JButton("Set as New Reference");
    }

    // MODIFIES: this
    // EFFECTS: initialize the graphic components for this:
    //          - name label
    //          - information fields
    //          - 3 edition buttons (set as reference, edit, remove)
    @Override
    public void initializeGraphics() {
        super.initializeGraphics();
        add(nameLabel, BorderLayout.PAGE_START);
        add(editionPanel, BorderLayout.AFTER_LINE_ENDS);

        this.editionPanel.setLayout(new BorderLayout());
        editionPanel.add(setReferenceButton, BorderLayout.NORTH);
        editionPanel.add(editionButton, BorderLayout.CENTER);
        editionPanel.add(removeButton, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: initialize the remove, edit and set as reference functions for buttons
    @Override
    protected void initializeFunctions() {
        removeAction = e -> {
            parent.getSimulator().getFrameWorld().deleteFrame((RefFrame) this.dataSource);
            parent.deleteElement(this);
            parent.getSimulator().updateAll();
        };

        removeButton.addActionListener(removeAction);

        editAction = e -> {
            ((FrameListPanel) parent).getEditor().display((RefFrame) this.getDataSource());
        };

        editionButton.addActionListener(editAction);

        setAsReferenceAction = e -> {
            parent.getSimulator().getFrameWorld().setCurrentReferenceAndUpdate((RefFrame) this.dataSource);
            parent.getSimulator().getDisplayCanvas().resetOrigin();
            parent.getSimulator().updateAll();
        };

        setReferenceButton.addActionListener(setAsReferenceAction);
    }

    // MODIFIES: this
    // EFFECTS: implement designed visual styles for this
    @Override
    public void initializeStyle() {
        super.initializeStyle();
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
    }

    // MODIFIES: this
    // EFFECTS: update the visual display of this' s info panel according to data source
    @Override
    public void updateInfo() {
        RefFrame frame = (RefFrame) dataSource;
        this.nameLabel.setText(frame.getName());
        this.posXDatum.setText(String.valueOf(PhysicalObject.roundUp(frame.getPosX())));
        this.properTimeDatum.setText(String.valueOf(PhysicalObject.roundUp(frame.getProperTime())));
        this.velocityDatum.setText(String.valueOf(
                Math.max(Math.min(0.9999, PhysicalObject.roundUp(frame.getVelocity())), -0.9999)));
    }


}
