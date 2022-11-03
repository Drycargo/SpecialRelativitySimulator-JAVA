package ui.panels.editors;

import model.FrameWorld;
import model.RefFrame;
import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import ui.GraphicRelativitySimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*Dialogue panel for adding new frames*/
public class FrameCreator extends PhysicalObjectCreator {
    protected JTextField occurTimeDatum;
    protected JTextField initialPosXDatum;
    protected JTextField initialProperTimeDatum;
    protected JTextField initialVelocityDatum;

    // constructor
    // EFFECTS: initialize the frame creator panel
    public FrameCreator(GraphicRelativitySimulator simulator) {
        super(simulator);
        initializeDialog("Add Frame");
    }

    // MODIFIES: this
    // EFFECTS: initialize the occurTime, initialPosX, initialProperTime and initialVelocity textFields for this
    @Override
    protected void initializeFields() {
        super.initializeFields();
        occurTimeDatum = new JTextField("");
        initialPosXDatum = new JTextField("");
        initialProperTimeDatum = new JTextField("");
        initialVelocityDatum = new JTextField("");
    }

    // MODIFIES: this
    // EFFECTS: add the initialized textFields and corresponding labels to the data collector
    @Override
    protected void initializeDataCollector() {
        super.initializeDataCollector();
        dataCollector.add(new JLabel("Observed Time (unit: s):"));
        dataCollector.add(occurTimeDatum);
        dataCollector.add(new JLabel("Observed Position (unit: c*s):"));
        dataCollector.add(initialPosXDatum);
        dataCollector.add(new JLabel("Clock reading when observed (unit: s):"));
        dataCollector.add(initialProperTimeDatum);
        dataCollector.add(new JLabel("Relative Velocity (unit: c):"));
        dataCollector.add(initialVelocityDatum);
        dataCollector.setLayout(new GridLayout(6, 2));
    }

    // MODIFIES: this
    // EFFECTS: initialize the function of adding new Frames of the confirm Button
    @Override
    protected void initializeConfirmButton() {
        initializeConfirmButton(new AddNewFrameAction());
    }

    // MODIFIES: this
    // EFFECTS: reset the textAreas to blank and display the panel
    @Override
    public void display() {
        occurTimeDatum.setText("");
        initialPosXDatum.setText("");
        initialProperTimeDatum.setText("");
        initialVelocityDatum.setText("");
        super.display();
    }

    /*Inner class responsible for implementing the adding Frame action*/
    private class AddNewFrameAction extends AbstractAction {
        // constructor
        // EFFECTS: construct new AddNewFrameAction
        public AddNewFrameAction() {
            super("Add New Frame");
        }

        // MODIFIES: this, simulator
        // EFFECTS: add new frame according to the current inputs to frameWorld in simulator
        //          if any error occurs, report the error via dialogs and do not add frame
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FrameWorld frameWorld = simulator.getFrameWorld();
                RefFrame newFrame;
                RefFrame reference = frameWorld.findFrameByName((String) referenceDatum.getSelectedItem());

                if (reference.equals(frameWorld.getAbsoluteStationaryFrame())) {
                    newFrame = new RefFrame(nameDatum.getText(),
                            Double.parseDouble(initialPosXDatum.getText()),
                            Double.parseDouble(occurTimeDatum.getText()),
                            Double.parseDouble(initialProperTimeDatum.getText()),
                            Double.parseDouble(initialVelocityDatum.getText()));
                } else {
                    newFrame = new RefFrame(nameDatum.getText(),
                            Double.parseDouble(initialPosXDatum.getText()),
                            Double.parseDouble(occurTimeDatum.getText()),
                            Double.parseDouble(initialProperTimeDatum.getText()),
                            Double.parseDouble(initialVelocityDatum.getText()),
                            reference);
                }

                frameWorld.addFrame(newFrame);
                simulator.getFrameListPanel().addNewElement(newFrame);
                creatorDialog.setVisible(false);
            } catch (NumberFormatException numberFormatException) {
                errorMessage("Wrong format: number input must be double");
            } catch (SameNameException sameNameException) {
                errorMessage("Frame named " + nameDatum.getText() + " already exists.");
            } catch (FrameConstructException frameConstructException) {
                errorMessage(frameConstructException.getMessage());
            }
        }
    }

}
