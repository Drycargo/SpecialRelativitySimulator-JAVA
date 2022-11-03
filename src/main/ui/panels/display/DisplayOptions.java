package ui.panels.display;

import ui.GraphicRelativitySimulator;

import javax.swing.*;
import java.awt.*;

/*Options for the Display Canvas*/
public class DisplayOptions extends JPanel {
    private GraphicRelativitySimulator simulator;
    private JCheckBox showEventOption;
    private JCheckBox reverseTimeOption;
    private JButton resetOriginButton;

    // constructor
    // EFFECTS: construct an option panel for Display
    public DisplayOptions(GraphicRelativitySimulator simulator) {
        this.simulator = simulator;
        initializeFields();
        initializeGraphics();
        initializeFunctions();
    }

    // MODIFIES: this
    // EFFECTS: initialize the options and button for this
    private void initializeFields() {
        showEventOption = new JCheckBox("Show Events");
        reverseTimeOption = new JCheckBox("Reverse Time");

        resetOriginButton = new JButton("Reset Origin");
    }

    // MODIFIES: this
    // EFFECTS: initialize the graphic options and button and graphic layout for this
    private void initializeGraphics() {
        this.setLayout(new GridLayout(1, 3));

        showEventOption.setSelected(true);
        add(showEventOption);

        reverseTimeOption.setSelected(false);
        add(reverseTimeOption);

        add(resetOriginButton);
    }

    // MODIFIES: this
    // EFFECTS: initialize the show Event, reverse playtime, reset origin funcitons for this
    private void initializeFunctions() {
        showEventOption.addItemListener(e -> {
            simulator.getDisplayCanvas().setShowEvents(showEventOption.isSelected());
        });

        reverseTimeOption.addItemListener(e -> {
            simulator.getReferenceController().reverseTimer();
        });

        resetOriginButton.addActionListener(e -> {
            simulator.getDisplayCanvas().resetOrigin();
        });
    }
}
