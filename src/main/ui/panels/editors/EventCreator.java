package ui.panels.editors;

import model.PhysicalEvent;
import model.FrameWorld;
import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import ui.GraphicRelativitySimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*Dialogue panel for adding new events*/
public class EventCreator extends PhysicalObjectCreator {
    private JTextField initialPosDatum;
    private JTextField initialTimeDatum;

    // constructor
    // EFFECTS: initialize the event creator panel
    public EventCreator(GraphicRelativitySimulator simulator) {
        super(simulator);
        initializeDialog("Add Event");
    }

    // MODIFIES: this
    // EFFECTS: initialize the intialPostion, initialTime textFields for this
    @Override
    protected void initializeFields() {
        super.initializeFields();
        initialPosDatum = new JTextField("");
        initialTimeDatum = new JTextField("");
    }

    // MODIFIES: this
    // EFFECTS: reset the textAreas to blank and display the panel
    @Override
    public void display() {
        initialPosDatum.setText("");
        initialTimeDatum.setText("");
        super.display();
    }

    // MODIFIES: this
    // EFFECTS: initialize the function of adding new Events of the confirm Button
    @Override
    protected void initializeConfirmButton() {
        initializeConfirmButton(new AddNewEventAction());
    }

    // MODIFIES: this
    // EFFECTS: add the initialized textFields and corresponding labels to the data collector
    @Override
    protected void initializeDataCollector() {
        super.initializeDataCollector();
        dataCollector.add(new JLabel("Observed Time (unit: s):"));
        dataCollector.add(initialTimeDatum);
        dataCollector.add(new JLabel("Observed Position (unit: c*s):"));
        dataCollector.add(initialPosDatum);
        dataCollector.setLayout(new GridLayout(4, 2));
    }

    /*Inner class responsible for implementing the adding Frame action*/
    private class AddNewEventAction extends AbstractAction {
        // constructor
        // EFFECTS: construct new AddNewEventAction
        public AddNewEventAction() {
            super("Add New Event");
        }

        // MODIFIES: this, simulator
        // EFFECTS: add new event according to the current inputs to frameWorld in simulator
        //          if any error occurs, report the error via dialogs and do not add event
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FrameWorld frameWorld = simulator.getFrameWorld();

                PhysicalEvent newPhysicalEvent = new PhysicalEvent(nameDatum.getText(),
                        Double.parseDouble(initialPosDatum.getText()),
                        Double.parseDouble(initialTimeDatum.getText()),
                        frameWorld.findFrameByName((String) referenceDatum.getSelectedItem()));

                frameWorld.addEvent(newPhysicalEvent);
                simulator.getEventListPanel().addNewElement(newPhysicalEvent);
                creatorDialog.setVisible(false);
            } catch (NumberFormatException numberFormatException) {
                errorMessage("Wrong format: number input must be double");
            } catch (SameNameException sameNameException) {
                errorMessage("Event named " + nameDatum.getText() + " already exists.");
            } catch (FrameConstructException frameConstructException) {
                errorMessage(frameConstructException.getMessage());
            }
        }
    }
}
