/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.graphdrawing;

import edu.umd.cs.piccolo.PCamera;
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class PanningAndZoomingThread extends Thread {

    public static long timeAtActivation;
    public static boolean panUp = false;
    public static boolean panRight = false;
    public static boolean panDown = false;
    public static boolean panLeft = false;
    public static boolean zoomIn = false;
    public static boolean zoomOut = false;
    public CustomGraphEditor customGraphEditor;
    public PCamera pCamera;
    // when shouldRun is false, the PanningAndZoomingThread stops running
    public boolean shouldRun = true;

    public PanningAndZoomingThread(CustomGraphEditor customGraphEditor) {
        this.customGraphEditor = customGraphEditor;
        pCamera = customGraphEditor.getCamera();
    }

    public void translateView() {

        while (shouldRun) {

            // set currentCustomGraphEditor to the Graph which is
            // currently visible
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            CustomGraphEditor currentCustomGraphEditor = JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex);
            PCamera pCamera = currentCustomGraphEditor.getCamera();

            // check that the Graph of this KeyListener is the
            // Graph currently showing
            if (currentCustomGraphEditor == customGraphEditor && timeAtActivation + 100 > System.currentTimeMillis()) {

                // the amount used to translate the view is based on
                // pCamera.getViewScale() so that the contents of the main graphical
                // view pan at the same speed regardless of the scale (how much they
                // have been zoomed)
                if (panUp) {
                    pCamera.translateView(0, 20 / pCamera.getViewScale());
                }
                if (panRight) {
                    pCamera.translateView(-20 / pCamera.getViewScale(), 0);
                }
                if (panDown) {
                    pCamera.translateView(0, -20 / pCamera.getViewScale());
                }
                if (panLeft) {
                    pCamera.translateView(20 / pCamera.getViewScale(), 0);
                }
                if (zoomOut) {
                    // a check is done to make sure that the new scale is not below
                    // the minimum allowed
                    if (pCamera.getViewScale() * 0.9 >= currentCustomGraphEditor.customPZoomEventHandler.getMinScale()) {
                        // zooming occurs about the centre of the camera.
                        // this is achieved by finding the x coordinate of the camera's
                        // bounds and adding half the width of the camera's bounds to that.
                        // the y coordinate is computed similarly.
                        pCamera.scaleViewAboutPoint(
                                0.9,
                                pCamera.getViewBounds().x + pCamera.getViewBounds().width / 2,
                                pCamera.getViewBounds().y + pCamera.getViewBounds().height / 2);
                    } else {
                        // when the minimum scale is reached, the camera's scale is
                        // set to it so that it is not possible to zoom further with
                        // right-clicking
                        pCamera.scaleViewAboutPoint(
                                currentCustomGraphEditor.customPZoomEventHandler.getMinScale() / pCamera.getViewScale(),
                                pCamera.getViewBounds().x + pCamera.getViewBounds().width / 2,
                                pCamera.getViewBounds().y + pCamera.getViewBounds().height / 2);
                    }
                }
                if (zoomIn) {
                    // a check is done to make sure that the new scale is not above
                    // the maximum allowed
                    if (pCamera.getViewScale() / 0.9 <= currentCustomGraphEditor.customPZoomEventHandler.getMaxScale()) {
                        // zooming occurs about the centre of the camera.
                        // this is achieved by finding the x coordinate of the camera's
                        // bounds and adding half the width of the camera's bounds to that.
                        // the y coordinate is computed similarly.
                        pCamera.scaleViewAboutPoint(
                                1 / 0.9,
                                pCamera.getViewBounds().x + pCamera.getViewBounds().width / 2,
                                pCamera.getViewBounds().y + pCamera.getViewBounds().height / 2);
                    } else {
                        // when the maximum scale is reached, the camera's scale is
                        // set to it so that it is not possible to zoom further with
                        // right-clicking
                        pCamera.scaleViewAboutPoint(
                                currentCustomGraphEditor.customPZoomEventHandler.getMaxScale() / pCamera.getViewScale(),
                                pCamera.getViewBounds().x + pCamera.getViewBounds().width / 2,
                                pCamera.getViewBounds().y + pCamera.getViewBounds().height / 2);
                    }
                }
                // the main graphical view is updated to show the panning
                pCamera.repaint();
                // the CustomBirdsEyeView is no longer used
//            // the panning view is updated to show the panning
//            JustclustJFrame.classInstance.customBirdsEyeView.updateFromViewed();

            }

            if (timeAtActivation + 100 <= System.currentTimeMillis()) {
                // the flags which describe the panning and zooming which should
                // occur are reset so that they can be activated now that 100
                // milliseconds have elapsed
                PanningAndZoomingThread.panUp = false;
                PanningAndZoomingThread.panRight = false;
                PanningAndZoomingThread.panDown = false;
                PanningAndZoomingThread.panLeft = false;
                PanningAndZoomingThread.zoomOut = false;
                PanningAndZoomingThread.zoomIn = false;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException interruptedException) {
            }

        }

    }

    public void run() {
        translateView();
    }
}
