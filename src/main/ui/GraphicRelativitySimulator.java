package ui;

import model.Event;
import model.*;
import model.exceptions.FrameConstructException;
import model.exceptions.SameNameException;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.panels.containers.ContainerPanel;
import ui.panels.containers.EventListPanel;
import ui.panels.containers.FrameListPanel;
import ui.panels.display.DisplayCanvas;
import ui.panels.display.DisplayOptions;
import ui.panels.display.ReferenceController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*Graphic interface of Special Relativity Simulator*/
public class GraphicRelativitySimulator extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 950;
    public static final String SOURCE_FILE = "./data/ourUniverse.json";
    public static final Font labelFont = new Font("Calibri", Font.BOLD, 16);

    private FrameWorld frameWorld;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private SaveOnExitListener saveOnExitListener;

    // Graphics
    private JPanel overallContainer;
    private ContainerPanel frameListPanel;
    private ContainerPanel eventListPanel;
    private ReferenceController referenceController;
    private DisplayCanvas displayCanvas;
    private DisplayOptions displayOptions;
    private ImageIcon icon;

    // constructor
    // EFFECTS: generate GUI of special relativity simulator
    public GraphicRelativitySimulator() {
        super("Special Relativity Simulator");
        try {
            initializeFields();
        } catch (FrameConstructException frameConstructException) {
            frameConstructException.printStackTrace();
        }
        initializeGraphics();
        generateNewElements();
    }

    // initialize fields; this function mainly refers to !!!
    // MODIFIES: this
    // EFFECTS: initialize the graphic frame of simulator by creating a frame of (minimum size) WIDTH
    //          and HEIGHT, set at the centre of the screen and visible.
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        createPanels();
        createMenu();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initialize the simulator and print welcome words
    //          throws FrameConstructException if frameWorld failed to be instantiated
    private void initializeFields() throws FrameConstructException {
        frameWorld = new FrameWorld();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(SOURCE_FILE);
        jsonReader = new JsonReader(SOURCE_FILE);
        saveOnExitListener = new SaveOnExitListener(this);
        this.addWindowListener(saveOnExitListener);
        icon = new ImageIcon((new ImageIcon("./data/Icon.png")).getImage().getScaledInstance(
                600,400, Image.SCALE_SMOOTH));
    }

    // MODIFIES: this
    // EFFECTS: create basic panels
    private void createPanels() {
        overallContainer = new JPanel(new BorderLayout());
        frameListPanel = new FrameListPanel(this);
        eventListPanel = new EventListPanel(this);
        overallContainer.add(frameListPanel, BorderLayout.EAST);

        JPanel leftDisplayContainer = new JPanel(new BorderLayout());
        leftDisplayContainer.add(eventListPanel, BorderLayout.SOUTH);

        JPanel displayPanel = new JPanel(new BorderLayout());
        referenceController = new ReferenceController(this);
        displayPanel.add(referenceController, BorderLayout.SOUTH);

        displayCanvas = new DisplayCanvas(this);
        displayPanel.add(displayCanvas, BorderLayout.CENTER);

        displayOptions = new DisplayOptions(this);
        displayPanel.add(displayOptions, BorderLayout.NORTH);

        leftDisplayContainer.add(displayPanel, BorderLayout.CENTER);

        overallContainer.add(leftDisplayContainer, BorderLayout.CENTER);

        add(overallContainer);
    }

    // MODIFIES: this
    // EFFECTS: create menu bar
    @SuppressWarnings("methodlength")
    private void createMenu() {
        JMenuBar operationMenuBar = new JMenuBar();
        // File menu
        JMenu fileMenu = new JMenu("File");

        // load file option
        JMenuItem loadFile = new JMenuItem("Load from file");
        loadFile.addActionListener(new LoadFileAction(this));
        loadFile.setAccelerator(KeyStroke.getKeyStroke("control L"));
        fileMenu.add(loadFile);

        // save file option
        JMenuItem saveFile = new JMenuItem("Save to file");
        saveFile.addActionListener(new SaveFileAction(this));
        saveFile.setAccelerator(KeyStroke.getKeyStroke("control S"));
        fileMenu.add(saveFile);

        fileMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> this.saveOnExitListener.windowClosing(
                new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        fileMenu.add(exit);

        operationMenuBar.add(fileMenu);

        // Info Menu
        JMenu infoMenu = new JMenu("Info");

        JMenuItem about = new JMenuItem("About");
        JPanel aboutPane = createAboutPane();

        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, aboutPane, "About", JOptionPane.PLAIN_MESSAGE);
        });

        infoMenu.add(about);
        operationMenuBar.add(infoMenu);

        add(operationMenuBar, BorderLayout.NORTH);
    }

    // EFFECTS: generate the "about" panel
    @SuppressWarnings("methodlength")
    private JPanel createAboutPane() {
        JPanel aboutPane = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Special Relativity Simulator");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        JLabel tributeLabel = new JLabel("--tribute to Albert Einstein--");
        tributeLabel.setFont(new Font("Serif", Font.ITALIC, 25));
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(tributeLabel);
        aboutPane.add(titlePanel, BorderLayout.NORTH);

        aboutPane.add(new JLabel(icon), BorderLayout.CENTER);

        JPanel personalInfoPanel = new JPanel();
        personalInfoPanel.setLayout(new BoxLayout(personalInfoPanel, BoxLayout.Y_AXIS));
        personalInfoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JLabel nameLabel = new JLabel("Made by: George C");
        nameLabel.setFont(labelFont);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        personalInfoPanel.add(namePanel);

        personalInfoPanel.add(createLabelAndURL("Email", "chengyx@student.ubc.ca",
                "mailto:chengyx@student.ubc.ca"));
        personalInfoPanel.add(createLabelAndURL("Github", "https://github.com/Drycargo",
                "https://github.com/Drycargo"));

        JPanel specialThanks = new JPanel(new BorderLayout());
        specialThanks.setBorder(BorderFactory.createEtchedBorder());
        specialThanks.add(new JLabel("Special Thanks to:"), BorderLayout.NORTH);

        DefaultListModel<String> specialThanksModel = new DefaultListModel<>();
        specialThanksModel.addElement("Prof. Douglas Scott");
        specialThanksModel.addElement("Mr. Frank & Ms. Annie");
        specialThanksModel.addElement("Mr. Martin");
        specialThanksModel.addElement("Prof. Janis Mckenna");
        specialThanksModel.addElement("Prof. Ian Affleck");

        JList<String> specialThanksList = new JList<>(specialThanksModel);
        specialThanksList.setEnabled(false);
        specialThanksList.setBackground(new Color(148, 255, 231));
        specialThanksList.setFont(labelFont);

        JScrollPane specialThanksScroll = new JScrollPane(specialThanksList);
        specialThanksScroll.setPreferredSize(new Dimension(600, 300));

        specialThanks.add(specialThanksScroll);
        personalInfoPanel.add(specialThanks);

        aboutPane.add(personalInfoPanel, BorderLayout.SOUTH);

        return aboutPane;
    }

    // this method refers to tutorial on
    // https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing
    // EFFECTS: return a panel containing label and corresponding, working url
    private JPanel createLabelAndURL(String label, String urlText, String url) {
        JPanel urlPanel = new JPanel();

        JLabel labelLabel = new JLabel(label + ": ");
        labelLabel.setFont(labelFont);
        urlPanel.add(labelLabel);

        JLabel urlLabel = new JLabel("<html><a href=''>" + urlText + "</a></html>");
        urlLabel.setFont(labelFont);
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e1) {
                    errorMessage("Failed to open " + url);
                }
            }
        });

        urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlPanel.add(urlLabel);

        return urlPanel;
    }

    // MODIFIES: this
    // EFFECTS: clear previous elements, generate new elements according to the current
    //          frameWorld
    private void generateNewElements() {
        frameListPanel.reRenderAll(frameWorld.getFrameList());
        eventListPanel.reRenderAll(frameWorld.getEventList());
        referenceController.setCurrentReference(frameWorld.getCurrentReference());
        displayCanvas.resetOrigin();
    }

    public FrameWorld getFrameWorld() {
        return frameWorld;
    }

    // MODIFIES: this
    // EFFECTS: update all info panels' data according to the current frameWorld's status, viewed in the current
    //          reference frame
    public void updateAll() {
        frameWorld.viewAllInCurrentReference();
        frameListPanel.updateAll();
        eventListPanel.updateAll();
        referenceController.setCurrentReference(frameWorld.getCurrentReference());
        displayCanvas.repaint();
    }

    public FrameListPanel getFrameListPanel() {
        return (FrameListPanel) frameListPanel;
    }

    public EventListPanel getEventListPanel() {
        return (EventListPanel) eventListPanel;
    }

    public DisplayCanvas getDisplayCanvas() {
        return displayCanvas;
    }

    public ReferenceController getReferenceController() {
        return referenceController;
    }

    /*Action for loading frameWorld from file*/
    private class LoadFileAction extends AbstractAction {
        private GraphicRelativitySimulator simulator;

        // constructor
        // EFFECTS: initialize this' s simulator
        public LoadFileAction(GraphicRelativitySimulator simulator) {
            this.simulator = simulator;
        }

        // MODIFIES: simulator
        // EFFECTS: read the existing frameWorld from file and update
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FrameWorld savedWorld = jsonReader.read();
                if (!savedWorld.equals(frameWorld)) {
                    int response = JOptionPane.showConfirmDialog(this.simulator,
                            "Your current data will be overwritten by the saved data.\n"
                                    + "Are you sure to load from file?",
                            "Load from file",
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        buildNewFrameWorld(savedWorld);
                    }
                } else {
                    buildNewFrameWorld(savedWorld);
                }
            } catch (IOException ioException) {
                errorMessage("Failed to load file " + SOURCE_FILE);
            } catch (FrameConstructException frameConstructException) {
                errorMessage("Frame Construction failed: data in file " + SOURCE_FILE
                        + " might have been manually modified and invalidated");
            } catch (JSONException jsonException) {
                errorMessage("FAILED TO LOAD DATA: JSON File structure is broken.");
            }
        }

        // MODIFIES: simulator
        // EFFECTS: load data from file and re-render all PhysicalObjectElements according to the new frameWorld
        private void buildNewFrameWorld(FrameWorld savedWorld) throws SameNameException {
            mergeFrameWorld(savedWorld);
            simulator.generateNewElements();
            JOptionPane.showMessageDialog(null,
                    "File from " + SOURCE_FILE + " successfully loaded",
                    "Load from file",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // MODIFIES: simulator
        // EFFECTS: merge the information of savedWorld into current frameWorld
        //          note that this method is redundant in essence: it is used specifically for logging
        //          the deletion and addition of physical Objects while loading file
        private void mergeFrameWorld(FrameWorld savedWorld) throws SameNameException {
            FrameWorld frameWorld = simulator.frameWorld;
            List<PhysicalObject> framesToDelete = new LinkedList<>(frameWorld.getFrameList());
            List<PhysicalObject> eventsToDelete = new LinkedList<>(frameWorld.getEventList());
            // remove all physical objects except Absolute Stationary Frame
            for (PhysicalObject nextFrame : framesToDelete) {
                if (!nextFrame.getName().equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME)) {
                    frameWorld.deleteFrame((RefFrame) nextFrame);
                }
            }
            for (PhysicalObject nextEvent : eventsToDelete) {
                frameWorld.deleteEvent((PhysicalEvent) nextEvent);
            }

            // add all physical objects except Absolute Stationary Frame
            List<PhysicalObject> framesToAdd = new LinkedList<>(savedWorld.getFrameList());
            List<PhysicalObject> eventsToAdd = new LinkedList<>(savedWorld.getEventList());
            for (PhysicalObject nextFrame : framesToAdd) {
                if (!nextFrame.getName().equals(FrameWorld.ABSOLUTE_STATIONARY_FRAME_NAME)) {
                    frameWorld.addFrame(nextFrame);
                }
            }
            for (PhysicalObject nextEvent : eventsToAdd) {
                frameWorld.addEvent(nextEvent);
            }

            frameWorld.setCurrentReferenceAndUpdate(savedWorld.getCurrentReference());
        }
    }


    /*Action for saving current world to file*/
    private class SaveFileAction implements ActionListener {
        private GraphicRelativitySimulator simulator;

        // constructor
        // EFFECTS: initialize this' s simulator
        public SaveFileAction(GraphicRelativitySimulator simulator) {
            this.simulator = simulator;
        }

        // MODIFIES: simulator
        // EFFECTS: load the existing frameWorld to file
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FrameWorld savedWorld = jsonReader.read();
                if (!savedWorld.equals(frameWorld)) {
                    int response = JOptionPane.showConfirmDialog(this.simulator,
                            "Data in file " + SOURCE_FILE + " will be overwritten by current data. "
                                    + "Are you sure to save to file?",
                            "Save to file",
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        saveToFile();
                    }
                } else {
                    saveToFile();
                }
            } catch (IOException | FrameConstructException | JSONException exception) {
                saveToFile();
            }
        }
    }

    // MODIFIES: file with the link SOURCE_FILE
    // EFFECTS: save data to source file
    private void saveToFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(frameWorld);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Data successfully saved to file " + SOURCE_FILE,
                    "Save to file",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            errorMessage("File " + SOURCE_FILE + " not found.");
        }
    }

    // EFFECTS: show error dialogue with message
    public static void errorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "System Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: print the recorded eventLog and exit
    private void exit() {
        EventLog eventLog = EventLog.getInstance();
        for (Event nextEvent : eventLog) {
            System.out.println(nextEvent + "\n");
        }
        System.exit(0);
    }

    /*WindowAdaptor to warn user of saving the frameWorld before exit*/
    private class SaveOnExitListener extends WindowAdapter {
        private GraphicRelativitySimulator simulator;

        // constructor
        // EFFECTS: initialize this' s simulator
        public SaveOnExitListener(GraphicRelativitySimulator simulator) {
            this.simulator = simulator;
        }

        // EFFECTS: if the current frameWorld does not equal the frameWorld stored in file (including cases in
        //          which the file is broken), ask user whether to store data or not before exiting; otherwise
        //          do nothing
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            try {
                FrameWorld savedWorld = jsonReader.read();
                if (!savedWorld.equals(frameWorld)) {
                    int response = JOptionPane.showConfirmDialog(this.simulator,
                            "Do you want to save data to file before exiting?",
                            "Exiting simulator",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        saveToFile();
                        exit();
                    } else if (response == JOptionPane.NO_OPTION) {
                        exit();
                    }
                } else {
                    exit();
                }
            } catch (IOException | FrameConstructException | JSONException exception) {
                saveToFile();
                exit();
            }
        }
    }
}
