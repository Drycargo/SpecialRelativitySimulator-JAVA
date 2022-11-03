package ui.panels.display;

import model.PhysicalEvent;
import model.PhysicalObject;
import model.RefFrame;
import ui.GraphicRelativitySimulator;
import ui.panels.containers.ContainerPanel;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

/*Canvas for displaying the visualized frames and events*/
public class DisplayCanvas extends JPanel implements MouseInputListener {
    public static final String REFERENCE_FRAME_INDICATOR = "[CURRENT REFERENCE]";
    public static final String EVENT_UNOCCURRED_INDICATOR = "???";
    public static final double STEP = 2.5;
    public static final int WIDTH = (int) (GraphicRelativitySimulator.WIDTH - ContainerPanel.WIDTH);
    public static final int HEIGHT = ((int) GraphicRelativitySimulator.WIDTH / 2 - 260);
    public static final int MARGIN = 10;
    public static final int FRAME_SEPARATION = 70;
    public static final Color NORMAL_FRAME_COLOR = Color.CYAN;
    public static final Color REF_FRAME_COLOR = Color.WHITE;
    public static final Color EVENT_COLOR_OCCURRED = Color.GREEN;
    public static final Color EVENT_COLOR_NOT_OCCURRED = Color.LIGHT_GRAY;

    private GraphicRelativitySimulator simulator;
    private Point origin;
    private Point startDragDistance;
    private boolean showEvents;

    // constructor
    // EFFECTS: set the black background for this
    public DisplayCanvas(GraphicRelativitySimulator simulator) {
        this.simulator = simulator;
        this.origin = new Point(WIDTH / 2, HEIGHT / 2);
        this.showEvents = true;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    // MODIFIES: this, graphics
    // EFFECTS: paint frames and events on the black background
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        int counter;
        // paint all frames
        List<PhysicalObject> frames = simulator.getFrameWorld().getFrameList();
        int indexOfRef = frames.indexOf(simulator.getFrameWorld().getCurrentReference());
        counter = 0;
        for (PhysicalObject nextFrame : frames) {
            boolean isReference = nextFrame.equals(simulator.getFrameWorld().getCurrentReference());
            paintFrame(graphics, (RefFrame) nextFrame, FRAME_SEPARATION * (counter - indexOfRef), isReference);
            counter++;
        }

        // paint all events if showEvents is selected
        if (showEvents) {
            List<PhysicalObject> events = simulator.getFrameWorld().getEventList();
            int indexOfMid = events.size() / 2;
            counter = 0;
            for (PhysicalObject nextEvent : events) {
                paintEvent(graphics, (PhysicalEvent) nextEvent, FRAME_SEPARATION * ((counter++) - indexOfMid));
            }
        }
    }

    // MODIFIES: graphics, this
    // EFFECTS: paint the oval frame, name, occur time and occur position for the Event object
    @SuppressWarnings("methodlength")
    private void paintEvent(Graphics graphics, PhysicalEvent physicalEvent, int posY) {
        String occurredStatus;
        String name = "Event: " + physicalEvent.getName();
        if (physicalEvent.hasOccurred()) {
            graphics.setColor(EVENT_COLOR_OCCURRED);
            occurredStatus = "(" + PhysicalObject.roundUp(physicalEvent.getCurrentOccurX()) + "c*s, "
                    + PhysicalObject.roundUp(physicalEvent.getCurrentOccurTime()) + "s)";
        } else {
            graphics.setColor(EVENT_COLOR_NOT_OCCURRED);
            occurredStatus = EVENT_UNOCCURRED_INDICATOR;
        }

        int lineHeight = graphics.getFontMetrics().getHeight();

        int containerHeight = 2 * lineHeight + MARGIN;
        int nameStringLength = (int) graphics.getFontMetrics().getStringBounds(name, graphics).getWidth();
        int statusStringLength = (int) graphics.getFontMetrics().getStringBounds(occurredStatus, graphics).getWidth();

        int maxLength = Math.max(nameStringLength, statusStringLength) + 2 * MARGIN;

        int coordinateX = (int) (origin.getX() + STEP * physicalEvent.getCurrentOccurX() - maxLength / 2);
        int coordinateY = (int) (origin.getY() + posY - lineHeight);

        graphics.fillOval(coordinateX, coordinateY, maxLength, containerHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawString(name, coordinateX + (maxLength - nameStringLength) / 2,
                coordinateY + lineHeight);
        graphics.drawString(occurredStatus, coordinateX + (maxLength - statusStringLength) / 2,
                coordinateY + 2 * lineHeight);
    }


    // MODIFIES: graphics, this
    // EFFECTS: paint the rectangular frame, name, proper time and reference frame indicator
    //          (if applicable) for the RefFrame object
    @SuppressWarnings("methodlength")
    private void paintFrame(Graphics graphics, RefFrame frame, int posY, boolean isReference) {
        int startingLine = 1;
        int maxLength;
        int containerHeight;
        String name = "Frame: " + frame.getName();
        int nameStringLength = (int) graphics.getFontMetrics().getStringBounds(name, graphics).getWidth();

        String time = "Time: " + PhysicalObject.roundUp(frame.getProperTime()) + "s";
        int timeStringLength = (int) graphics.getFontMetrics().getStringBounds(time, graphics).getWidth();

        int indicatorLength = (int) graphics.getFontMetrics().getStringBounds(
                REFERENCE_FRAME_INDICATOR, graphics).getWidth();

        int lineHeight = graphics.getFontMetrics().getHeight();

        if (isReference) {
            graphics.setColor(REF_FRAME_COLOR);
            maxLength = Math.max(Math.max(nameStringLength, timeStringLength), indicatorLength) + 2 * MARGIN;
            containerHeight = 3 * lineHeight + MARGIN;
        } else {
            graphics.setColor(NORMAL_FRAME_COLOR);
            maxLength = Math.max(nameStringLength, timeStringLength) + 2 * MARGIN;
            containerHeight = 2 * lineHeight + MARGIN;
        }

        int coordinateX = (int) (origin.getX() + STEP * frame.getPosX() - maxLength / 2);
        int coordinateY = (int) (origin.getY() + posY - lineHeight);

        if (isReference) {
            graphics.drawString(REFERENCE_FRAME_INDICATOR, coordinateX + (maxLength - indicatorLength) / 2,
                    coordinateY + (startingLine++) * lineHeight);
        }

        graphics.drawRect(coordinateX, coordinateY, maxLength, containerHeight);
        graphics.drawString(name, coordinateX + (maxLength - nameStringLength) / 2,
                coordinateY + (startingLine++) * lineHeight);
        graphics.drawString(time, coordinateX + (maxLength - timeStringLength) / 2,
                coordinateY + startingLine * lineHeight);
    }

    // MODIFIES: this
    // EFFECTS: reset the status of showEvents and update
    public void setShowEvents(boolean showEvents) {
        this.showEvents = showEvents;
        this.repaint();
    }

    // MODIFIES: this
    // EFFECTS: reset the origin to the very centre of this
    public void resetOrigin() {
        origin.setLocation(WIDTH / 2, HEIGHT / 2);
        this.repaint();
    }

    // MODIFIES: this
    // EFFECTS: drag the origin (therefore the whole canvas) according to Mouse's action;
    //          set cursor to move style
    @Override
    public void mouseDragged(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        if (startDragDistance == null) {
            startDragDistance = e.getPoint();
            startDragDistance.setLocation(startDragDistance.getX() - origin.getX(),
                    startDragDistance.getY() - origin.getY());
        }
        origin.setLocation((e.getX() - startDragDistance.getX()),
                (e.getY() - startDragDistance.getY()));
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // default action
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // default action
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // default action
    }

    // MODIFIES: this
    // EFFECTS: reset the distance from the starting drag point to the current origin to null;
    //          set cursor to default style
    @Override
    public void mouseReleased(MouseEvent e) {
        startDragDistance = null;
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // default action
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // default action
    }
}
