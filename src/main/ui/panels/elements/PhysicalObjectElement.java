package ui.panels.elements;

import model.PhysicalObject;
import ui.panels.containers.ContainerPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

/*Information panel of an individual Physical Object*/
public abstract class PhysicalObjectElement extends JPanel {
    protected static ActionListener removeAction;

    protected JButton removeButton;
    protected JLabel nameLabel;
    protected JPanel infoPanel;
    protected JPanel editionPanel;
    protected PhysicalObject dataSource;
    protected ContainerPanel parent;

    // constructor
    // EFFECTS: construct a graphic element of physicalObject
    public PhysicalObjectElement(PhysicalObject physicalObject, ContainerPanel parent) {
        this.parent = parent;
        initializeFields(physicalObject);
        initializeGraphics();
        initializeFunctions();
        initializeStyle();
        updateInfo();
    }

    // MODIFIES: this
    // EFFECTS: initialize the fields of this:
    //          - the data source that this refers to;
    //          - the infoPanel containing current info of data source
    //          - the editionPanel containing buttons for operation
    //          - the button for removing this
    //          - the name label of this
    protected void initializeFields(PhysicalObject frame) {
        this.dataSource = frame;

        this.infoPanel = new JPanel();
        this.editionPanel = new JPanel();

        this.removeButton = new JButton("Remove");

        nameLabel = new JLabel(frame.getName());
    }


    // MODIFIES: this
    // EFFECTS: initialize the designed graphics for this
    protected void initializeGraphics() {
        this.setLayout(new BorderLayout());
        infoPanel.setLayout(new GridLayout(3, 2));
        add(infoPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: initialize the functions for buttons
    protected abstract void initializeFunctions();

    // MODIFIES: this
    // EFFECTS: implement the designed style for this
    protected void initializeStyle() {
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
        this.setBorder(blackBorder);
    }

    // MODIFIES: this
    // EFFECTS: update the information displayed referring to the current info of data source
    public abstract void updateInfo();

    // MODIFIES: this
    // EFFECTS: create and add a label for name of a certain data to this' s infoPanel;
    //          return the label
    protected JLabel createDataLabel(String labelName) {
        JLabel newLabel = new JLabel(labelName);
        this.infoPanel.add(newLabel);
        return newLabel;
    }

    // MODIFIES: this
    // EFFECTS: create and add a non-editable TextField for name of a certain data to this' s infoPanel;
    //          return the TextField
    protected JTextField createDataTextField() {
        JTextField newField = new JTextField();
        newField.setEditable(false);
        this.infoPanel.add(newField);
        return newField;
    }

    // EFFECTS: return the data source of this
    public PhysicalObject getDataSource() {
        return dataSource;
    }
}
