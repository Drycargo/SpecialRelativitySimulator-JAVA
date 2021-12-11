package ui.panels.display;

import model.PhysicalObject;
import model.RefFrame;
import ui.GraphicRelativitySimulator;
import ui.panels.containers.ContainerPanel;

import javax.swing.*;
import java.awt.*;

/*Controller of the current reference frame;
responsible for setting the proper time (and therefore play the program)*/
public class ReferenceController extends JPanel {
    public static final int HEIGHT = 50;
    public static final int TIMER_INTERVAL = 30;

    private JLabel nameLabel;
    private JTextField properTimeDatum;
    private JButton confirmButton;
    private JButton playButton;
    private RefFrame currentReference;
    private int currentTimerStep;

    private Timer timer;

    private GraphicRelativitySimulator simulator;

    // constructor
    // EFFECTS: initialize the control panel
    public ReferenceController(GraphicRelativitySimulator simulator) {
        initializeFields(simulator);
        initializeGraphics();
        initializeFunctions();
    }

    // MODIFIES: this
    // EFFECTS: initialize the simulator, name label, proper time input area, confirm and play buttons
    //          for this
    private void initializeFields(GraphicRelativitySimulator simulator) {
        this.simulator = simulator;
        nameLabel = new JLabel("");
        properTimeDatum = new JTextField();
        confirmButton = new JButton("Set proper time");
        playButton = new JButton("Play");
        currentTimerStep = 1;
    }

    // MODIFIES: this
    // EFFECTS: initialize the graphic components for this
    private void initializeGraphics() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(ContainerPanel.WIDTH, HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);

        add(nameLabel, BorderLayout.NORTH);

        JPanel properTimePanel = new JPanel(new BorderLayout());
        properTimePanel.add(new JLabel("Clock Reading(s): "), BorderLayout.WEST);
        properTimePanel.add(properTimeDatum, BorderLayout.CENTER);
        properTimePanel.add(confirmButton, BorderLayout.EAST);
        add(properTimePanel, BorderLayout.CENTER);
        add(playButton, BorderLayout.EAST);
    }


    // MODIFIES: this, simulator
    // EFFECTS: implement the functions for setting the proper time (and update) and playing
    //          simulation
    @SuppressWarnings("methodlength")
    private void initializeFunctions() {
        this.confirmButton.addActionListener(e -> {
            try {
                double newProperTime = Double.parseDouble(properTimeDatum.getText());
                setNewProperTime(newProperTime);
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(null,
                        "Wrong format: number input must be double", "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        this.timer = new Timer(TIMER_INTERVAL, e -> {
            setNewProperTime(currentReference.getProperTime() + currentTimerStep);
        });

        this.playButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                this.playButton.setText("Play");
                this.properTimeDatum.setEnabled(true);
            } else {
                timer.start();
                this.playButton.setText("Pause");
                this.properTimeDatum.setEnabled(false);
            }
        });
    }

    // MODIFIES: this, simulator
    // EFFECTS: set simulator' s reference frame time to new proper time
    private void setNewProperTime(double newProperTime) {
        simulator.getFrameWorld().setCurrentReferenceProperTime(newProperTime);
        simulator.updateAll();
    }

    // MODIFIES: this
    // EFFECTS: display the information of the currentReference frame
    public void setCurrentReference(RefFrame currentReference) {
        this.currentReference = currentReference;
        this.nameLabel.setText("Current Reference Frame: " + currentReference.getName());
        this.properTimeDatum.setText(String.valueOf(PhysicalObject.roundUp(currentReference.getProperTime())));
    }

    // MODIFIES: this
    // EFFECTS: reverse the propagation direction of the timer
    public void reverseTimer() {
        currentTimerStep = -currentTimerStep;
    }
}
