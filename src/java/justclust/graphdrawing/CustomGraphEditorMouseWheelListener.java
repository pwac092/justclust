/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.graphdrawing;

import edu.umd.cs.piccolo.PCamera;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class CustomGraphEditorMouseWheelListener implements MouseWheelListener {

    public CustomGraphEditor customGraphEditor;

    CustomGraphEditorMouseWheelListener(CustomGraphEditor customGraphEditor) {
        this.customGraphEditor = customGraphEditor;
    }

    public void mouseWheelMoved(MouseWheelEvent mwe) {

        PCamera pCamera = customGraphEditor.getCamera();
        Point2D viewCoordinates = pCamera.localToView(new Point(mwe.getX(), mwe.getY()));

        if (mwe.getPreciseWheelRotation() > 0) {
            // a check is done to make sure that the new scale is not below
            // the minimum allowed
            if (pCamera.getViewScale() * 0.8 >= customGraphEditor.customPZoomEventHandler.getMinScale()) {
                // zooming occurs about the cursor's position
                pCamera.scaleViewAboutPoint(0.8, viewCoordinates.getX(), viewCoordinates.getY());
            } else {
                // when the minimum scale is reached, the camera's scale is
                // set to it so that it is not possible to zoom further with
                // right-clicking
                pCamera.scaleViewAboutPoint(customGraphEditor.customPZoomEventHandler.getMinScale() / pCamera.getViewScale(), viewCoordinates.getX(), viewCoordinates.getY());
            }
        } else {
            // a check is done to make sure that the new scale is not above
            // the maximum allowed
            if (pCamera.getViewScale() / 0.8 <= customGraphEditor.customPZoomEventHandler.getMaxScale()) {
                // zooming occurs about the cursor's position
                pCamera.scaleViewAboutPoint(1 / 0.8, viewCoordinates.getX(), viewCoordinates.getY());
            } else {
                // when the maximum scale is reached, the camera's scale is
                // set to it so that it is not possible to zoom further with
                // right-clicking
                pCamera.scaleViewAboutPoint(customGraphEditor.customPZoomEventHandler.getMaxScale() / pCamera.getViewScale(), viewCoordinates.getX(), viewCoordinates.getY());
            }
        }

    }
}
