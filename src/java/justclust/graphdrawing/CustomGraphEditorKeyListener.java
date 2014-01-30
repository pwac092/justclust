/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.graphdrawing;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class CustomGraphEditorKeyListener implements KeyListener {

    public CustomGraphEditor customGraphEditor;

    CustomGraphEditorKeyListener(CustomGraphEditor customGraphEditor) {
        this.customGraphEditor = customGraphEditor;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_UP
                && !PanningAndZoomingThread.panUp) {
            // timeAtActivation is set before panUp so that, if the
            // PanningAndZoomingThread interrupts this thread after panUp is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panUp = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT
                && !PanningAndZoomingThread.panRight) {
            // timeAtActivation is set before panRight so that, if the
            // PanningAndZoomingThread interrupts this thread after panRight is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panRight = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN
                && !PanningAndZoomingThread.panDown) {
            // timeAtActivation is set before panDown so that, if the
            // PanningAndZoomingThread interrupts this thread after panDown is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panDown = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT
                && !PanningAndZoomingThread.panLeft) {
            // timeAtActivation is set before panLeft so that, if the
            // PanningAndZoomingThread interrupts this thread after panLeft is
            // set, the panning won't end immediately because the
            // timeAtActivation contains an old time (it hasn't been set
            // yet)
            PanningAndZoomingThread.timeAtActivation = System.currentTimeMillis();
            PanningAndZoomingThread.panLeft = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
            CustomGraphEditorMouseListener.controlKeyPressed = true;
            customGraphEditor.repaint();
            if (CustomGraphEditorMouseListener.panningToolMode) {
                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } else {
                if (CustomGraphEditorMouseListener.mouseOverCustomGraphEditor) {
                    JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
//        if (ke.getKeyCode() == KeyEvent.VK_UP) {
//            PanningAndZoomingThread.panUp = false;
//        }
//        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
//            PanningAndZoomingThread.panRight = false;
//        }
//        if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
//            PanningAndZoomingThread.panDown = false;
//        }
//        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
//            PanningAndZoomingThread.panLeft = false;
//        }
        if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
            CustomGraphEditorMouseListener.controlKeyPressed = false;
            customGraphEditor.repaint();
            if (CustomGraphEditorMouseListener.panningToolMode) {
                if (CustomGraphEditorMouseListener.mouseOverCustomGraphEditor) {
                    JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            } else {
                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
}
