package ui.panels.containers;

import model.PhysicalObject;
import ui.GraphicRelativitySimulator;
import ui.panels.editors.PhysicalObjectCreator;
import ui.panels.elements.PhysicalObjectElement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/*A panel containing a list of elements*/
public abstract class ContainerPanel extends JPanel {
    public static final int WIDTH = (int) (GraphicRelativitySimulator.WIDTH / 2.2);
    public static final int HEIGHT = GraphicRelativitySimulator.HEIGHT;
    protected JLabel panelNameLabel;
    protected GraphicRelativitySimulator simulator;
    protected JButton additionButton;
    protected JPanel infoPanel;
    protected JScrollPane scrollPane;

    protected List<PhysicalObjectElement> elements;

    protected PhysicalObjectCreator creator;

    // constructor
    // EFFECTS: construct container panel with name, button (named "Add elementType"), !!!
    public ContainerPanel(String name, String elementType, GraphicRelativitySimulator simulator) {
        initializeField(name, elementType, simulator);
        initializeGraphics();
        initializeStyle();
        initializeFunction();
    }

    // MODIFIES: this
    // EFFECTS: initialize the name label, simulator, button for adding new element, information panel and a new
    //          element list
    private void initializeField(String name, String elementType, GraphicRelativitySimulator simulator) {
        this.panelNameLabel = new JLabel(name);
        this.simulator = simulator;
        this.additionButton = new JButton("Add " + elementType);
        this.infoPanel = new JPanel();
        this.elements = new LinkedList<PhysicalObjectElement>();
    }

    // MODIFIES: this
    // EFFECTS: set up the graphics
    protected void initializeGraphics() {
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(panelNameLabel);
        topPanel.add(additionButton);
        this.add(topPanel, BorderLayout.PAGE_START);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(infoPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));

        this.add(scrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: set up the visual style of this
    protected void initializeStyle() {
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
        this.setBorder(blackBorder);
    }

    // MODIFIES: this
    // EFFECTS: update all elements according to their data source
    //          if the data source of an element no longer exists in the frameWorld, remove the element
    public void updateAll() {
        List<PhysicalObjectElement> elementsToDelete = new LinkedList<>();
        for (PhysicalObjectElement nextElement : elements) {
            if (simulator.getFrameWorld().findFrameByName(nextElement.getDataSource().getName()) == null
                    && simulator.getFrameWorld().findEventByName(nextElement.getDataSource().getName()) == null) {
                elementsToDelete.add(nextElement);
                this.infoPanel.remove(nextElement);
                continue;
            }
            nextElement.updateInfo();
            nextElement.validate();
        }
        this.elements.removeAll(elementsToDelete);
    }

    // MODIFIES: this
    // EFFECTS: add new element to this
    protected void addPhysicalObjectElement(PhysicalObjectElement newElement) {
        this.infoPanel.add(newElement);
        this.infoPanel.validate();
        this.scrollPane.validate();
        elements.add(newElement);
        simulator.getDisplayCanvas().repaint();
    }

    // MODIFIES: this
    // EFFECTS: clear all previous elements; render the new physicalObjects and add them to the list of elements
    public void reRenderAll(List<PhysicalObject> physicalObjects) {
        this.infoPanel.removeAll();
        elements.clear();
        for (PhysicalObject next : physicalObjects) {
            addNewElement(next);
        }
        this.infoPanel.revalidate();
    }

    // this is the abstract version of addPhysicalObjectElement, which can introduce cohesiveness to subclasses
    // MODIFIES: this
    // EFFECTS: add new element to this
    protected abstract void addNewElement(PhysicalObject physicalObject);

    // MODIFIES: this
    // EFFECTS: initialize the function for the button for adding new elements
    protected void initializeFunction() {
        this.additionButton.addActionListener(event -> creator.display());
    }

    public GraphicRelativitySimulator getSimulator() {
        return simulator;
    }

    // MODIFIES: this
    // EFFECTS: delete given element
    public void deleteElement(PhysicalObjectElement objectToDelete) {
        if (this.elements.contains(objectToDelete)) {
            this.infoPanel.remove(objectToDelete);
            this.elements.remove(objectToDelete);
            this.infoPanel.validate();
            this.setVisible(false);
            this.setVisible(true);
        }
    }
}
