package ui.panels.editors;

import model.PhysicalObject;
import model.RefFrame;
import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import ui.GraphicRelativitySimulator;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*Dialogue panel for editing an existing frame, relative to Absolute Stationary Frame*/
public class FrameEditor extends FrameCreator {
    private RefFrame frameToEdit;

    // constructor
    // EFFECTS: construct a frame editor, allows user to edit the initial conditions of selected frame relative to
    //          Absolute Stationary Frame
    public FrameEditor(GraphicRelativitySimulator simulator) {
        super(simulator);
        referenceDatum.addItem("Absolute Stationary Frame");
        referenceDatum.setEnabled(false);
        creatorDialog.setTitle("Edit Frame");
    }

    // MODIFIES: this
    // EFFECTS: display the initial status of the selected RefFrame
    public void display(RefFrame newFrameToEdit) {
        frameToEdit = newFrameToEdit;
        nameDatum.setText(frameToEdit.getName());
        occurTimeDatum.setText(String.valueOf(PhysicalObject.roundUp(frameToEdit.getOccurTime())));
        initialPosXDatum.setText(String.valueOf(PhysicalObject.roundUp(frameToEdit.getInitialPosX())));
        initialProperTimeDatum.setText(String.valueOf(PhysicalObject.roundUp(frameToEdit.getInitialProperTime())));
        initialVelocityDatum.setText(String.valueOf(PhysicalObject.roundUp(frameToEdit.getInitialVelocity())));
        creatorDialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initialize the function of editing selected Frame of the confirm Button
    @Override
    protected void initializeConfirmButton() {
        initializeConfirmButton(new EditFrameAction());
    }

    /*Inner class responsible for implementing the editing Frame action*/
    private class EditFrameAction extends AbstractAction {
        // constructor
        // EFFECTS: construct new EditFrameAction
        public EditFrameAction() {
            super("Edit Existing Frame");
        }

        // MODIFIES: this, simulator
        // EFFECTS: edit the selected frame according to the current inputs to frameWorld in simulator
        //          if any error occurs, report the error via dialogs and do not edit frame
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (simulator.getFrameWorld().findFrameByName(nameDatum.getText()) != null
                        && !nameDatum.getText().equals(frameToEdit.getName())) {
                    throw new SameNameException();
                }
                RefFrame mirrorFrame = new RefFrame(nameDatum.getText(), Double.parseDouble(initialPosXDatum.getText()),
                        Double.parseDouble(occurTimeDatum.getText()),
                        Double.parseDouble(initialProperTimeDatum.getText()),
                        Double.parseDouble(initialVelocityDatum.getText()));

                frameToEdit.setName(mirrorFrame.getName());
                frameToEdit.setInitialPosX(mirrorFrame.getInitialPosX());
                frameToEdit.setOccurTime(mirrorFrame.getOccurTime());
                frameToEdit.setInitialProperTime(mirrorFrame.getInitialProperTime());
                frameToEdit.setInitialVelocity(mirrorFrame.getInitialVelocity());

                simulator.updateAll();
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
