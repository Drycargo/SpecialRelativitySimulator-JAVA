package ui.panels.editors;

import ui.GraphicRelativitySimulator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*Creator panel for Physical Objects*/
public abstract class PhysicalObjectCreator extends JPanel {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    protected JDialog creatorDialog;
    protected Dimension screen;
    protected GraphicRelativitySimulator simulator;

    protected JTextField nameDatum;
    protected JPanel dataCollector;
    protected JButton confirmButton;
    protected JButton cancelButton;
    protected JComboBox<String> referenceDatum;

    // constructor
    // EFFECTS: construct a creator for Physical Objects
    public PhysicalObjectCreator(GraphicRelativitySimulator simulator) {
        this.simulator = simulator;
        Toolkit kit = Toolkit.getDefaultToolkit();
        screen = kit.getScreenSize();

        setLayout(new BorderLayout());
        initializeFields();
        initializeDataCollector();
        initializeButtons();
    }

    // MODIFIES: this
    // EFFECTS: initialize the name textField, data collector panel and a combo box of existing RefFrames
    protected void initializeFields() {
        nameDatum = new JTextField("");
        dataCollector = new JPanel();
        referenceDatum = new JComboBox<String>();
    }

    // MODIFIES: this
    // EFFECTS: initialize the data collector panel by adding the corresponding data textFields and labels to it:
    //          - name
    //          - all existing frames
    protected void initializeDataCollector() {
        add(dataCollector, BorderLayout.CENTER);
        dataCollector.add(new JLabel("Name:"));
        dataCollector.add(nameDatum);
        dataCollector.add(new JLabel("Observed By:"));
        dataCollector.add(referenceDatum);
    }

    // MODIFIES: this
    // EFFECTS: create and add the confirm and cancel buttons
    protected void initializeButtons() {
        initializeConfirmButton();
        initializeCancelButton();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: implement the function for the confirm button (add new element)
    protected abstract void initializeConfirmButton();

    // MODIFIES: this
    // EFFECTS: implement the function for the confirm button (add new element) by adding abstractAction to it and
    //          initialize its text to be "OK"
    protected void initializeConfirmButton(AbstractAction abstractAction) {
        confirmButton = new JButton("OK");
        confirmButton.addActionListener(abstractAction);
    }

    // MODIFIES: this
    // EFFECTS: initialize the function and text for cancelButton
    private void initializeCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> creatorDialog.setVisible(false));
    }

    // MODIFIES: this
    // EFFECTS: create the pop-up dialog panel for this
    protected void initializeDialog(String type) {
        creatorDialog = new JDialog(simulator, type, true);
        creatorDialog.add(this);
        creatorDialog.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        creatorDialog.setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
        creatorDialog.pack();
        creatorDialog.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: display the dialog panel; include all existing frames in the reference datum combo box
    public void display() {
        nameDatum.setText("");

        List<String> framesList = simulator.getFrameWorld().getFrameNames();
        referenceDatum.removeAllItems();
        for (String frameName : framesList) {
            referenceDatum.addItem(frameName);
        }
        creatorDialog.setVisible(true);
    }

    // EFFECTS: pop up the dialog for error message
    protected void errorMessage(String message) {
        GraphicRelativitySimulator.errorMessage(message);
    }
}
