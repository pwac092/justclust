/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.graphdrawing;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import justclust.JustclustJFrame;
import justclust.datastructures.Data;

/**
 *
 * @author wuaz008
 */
public class CustomGraphEditorMouseListener implements MouseListener, MouseMotionListener {

    public static boolean panningToolMode = true;
    public static boolean controlKeyPressed = false;
    public static boolean mouseOverCustomGraphEditor = false;
    public CustomGraphEditor customGraphEditor;

    CustomGraphEditorMouseListener(CustomGraphEditor customGraphEditor) {
        this.customGraphEditor = customGraphEditor;
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {

        // the panUp, panRight, panDown, panLeft, zoomOut, and zoomIn fields
        // of CustomGraphEditorKeyListener are set so that it's repeating loop can handle
        // zooming and panning from mouse clicks as well as key presses

        int x = me.getX();
        int y = me.getY();
        // the coordinates are those for the buttons for panning, zooming, and
        // resizing nodes in the main graphical view.
        // subtracting 2 from y is done because the coordinates are off by
        // 2 for some unkown reason.
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panUpButtonCoordinates.y
                && y - 2 <= customGraphEditor.panUpButtonCoordinates.y + 39
                && !PanningAndZoomingThread.panUp) {
            // timeAtActivation is set before panUp so that, if the
            // PanningAndZoomingThread interrupts this thread after panUp is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panUp = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panRightButtonCoordinates.y
                && y - 2 <= customGraphEditor.panRightButtonCoordinates.y + 39
                && !PanningAndZoomingThread.panRight) {
            // timeAtActivation is set before panRight so that, if the
            // PanningAndZoomingThread interrupts this thread after panRight is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panRight = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panDownButtonCoordinates.y
                && y - 2 <= customGraphEditor.panDownButtonCoordinates.y + 39
                && !PanningAndZoomingThread.panDown) {
            // timeAtActivation is set before panDown so that, if the
            // PanningAndZoomingThread interrupts this thread after panDown is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panDown = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panLeftButtonCoordinates.y
                && y - 2 <= customGraphEditor.panLeftButtonCoordinates.y + 39
                && !PanningAndZoomingThread.panLeft) {
            // timeAtActivation is set before panLeft so that, if the
            // PanningAndZoomingThread interrupts this thread after panLeft is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panLeft = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomInButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomInButtonCoordinates.y + 39
                && !PanningAndZoomingThread.zoomIn) {
            // timeAtActivation is set before zoomIn so that, if the
            // PanningAndZoomingThread interrupts this thread after zoomIn is
            // set, the zooming won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.zoomIn = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomOutButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomOutButtonCoordinates.y + 39
                && !PanningAndZoomingThread.zoomOut) {
            // timeAtActivation is set before zoomOut so that, if the
            // PanningAndZoomingThread interrupts this thread after zoomOut is
            // set, the zooming won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.zoomOut = true;
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.growNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.growNodeSizeButtonCoordinates.y + 39) {
            for (int i = 1; i < customGraphEditor.nodeLayer.getAllNodes().size(); i++) {
                PPath node = ((ArrayList<PPath>) customGraphEditor.nodeLayer.getAllNodes()).get(i);
                double xCentre = node.getX() + node.getOffset().getX() + node.getWidth() / 2;
                double yCentre = node.getY() + node.getOffset().getY() + node.getHeight() / 2;
                node.setOffset(0, 0);
                node.setWidth(node.getWidth() + 20);
                node.setHeight(node.getHeight() + 20);
                node.setX(xCentre - node.getWidth() / 2);
                node.setY(yCentre - node.getHeight() / 2);
                PText label = ((ArrayList<PText>) node.getAttribute("labels")).get(0);
                label.setFont(new Font("SansSerif", Font.PLAIN, label.getFont().getSize() + 8));
                label.setX(node.getFullBoundsReference().getCenter2D().getX() - label.getWidth() / 2);
                label.setY(node.getFullBoundsReference().getCenter2D().getY() - label.getHeight() / 2);
            }
            customGraphEditor.repaint();
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.shrinkNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.shrinkNodeSizeButtonCoordinates.y + 39) {
            if (customGraphEditor.nodeLayer.getAllNodes().size() >= 1) {
                PPath node = ((ArrayList<PPath>) customGraphEditor.nodeLayer.getAllNodes()).get(1);
                if (node.getWidth() >= 40) {
                    for (int i = 1; i < customGraphEditor.nodeLayer.getAllNodes().size(); i++) {
                        node = ((ArrayList<PPath>) customGraphEditor.nodeLayer.getAllNodes()).get(i);
                        double xCentre = node.getX() + node.getOffset().getX() + node.getWidth() / 2;
                        double yCentre = node.getY() + node.getOffset().getY() + node.getHeight() / 2;
                        node.setOffset(0, 0);
                        node.setWidth(node.getWidth() - 20);
                        node.setHeight(node.getHeight() - 20);
                        node.setX(xCentre - node.getWidth() / 2);
                        node.setY(yCentre - node.getHeight() / 2);
                        PText label = ((ArrayList<PText>) node.getAttribute("labels")).get(0);
                        label.setFont(new Font("SansSerif", Font.PLAIN, label.getFont().getSize() - 8));
                        label.setX(node.getFullBoundsReference().getCenter2D().getX() - label.getWidth() / 2);
                        label.setY(node.getFullBoundsReference().getCenter2D().getY() - label.getHeight() / 2);
                    }
                    customGraphEditor.repaint();
                }
            }
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panningToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.panningToolButtonCoordinates.y + 39
                && (!panningToolMode && !controlKeyPressed || panningToolMode && controlKeyPressed)) {
            panningToolMode = !panningToolMode;
            customGraphEditor.repaint();
        }
        if (x >= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.selectionToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.selectionToolButtonCoordinates.y + 39
                && (panningToolMode && !controlKeyPressed || !panningToolMode && controlKeyPressed)) {
            panningToolMode = !panningToolMode;
            customGraphEditor.repaint();
        }

    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
        mouseOverCustomGraphEditor = true;
        if (panningToolMode && !controlKeyPressed || !panningToolMode && controlKeyPressed) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseExited(MouseEvent me) {
        mouseOverCustomGraphEditor = false;
        JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseDragged(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {

        int x = me.getX();
        int y = me.getY();
        // the coordinates are those for the arrow buttons and minus and plus
        // buttons in the main graphical view.
        // subtracting 2 from y is done because the coordinates are off by
        // 2 for some unkown reason.
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panUpButtonCoordinates.y
                && y - 2 <= customGraphEditor.panUpButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panRightButtonCoordinates.y
                && y - 2 <= customGraphEditor.panRightButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panDownButtonCoordinates.y
                && y - 2 <= customGraphEditor.panDownButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panLeftButtonCoordinates.y
                && y - 2 <= customGraphEditor.panLeftButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomInButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomInButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomOutButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomOutButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.growNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.growNodeSizeButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.shrinkNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.shrinkNodeSizeButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panningToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.panningToolButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else if (x >= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.selectionToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.selectionToolButtonCoordinates.y + 39) {
            JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            if (panningToolMode && !controlKeyPressed || !panningToolMode && controlKeyPressed) {
                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }

    }
}
