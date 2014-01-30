/*
 * Copyright (c) 2008-2011, Piccolo2D project, http://piccolo2d.org
 * Copyright (c) 1998-2008, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * None of the name of the University of Maryland, the name of the Piccolo2D project, or the names of its
 * contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package justclust.graphdrawing;

import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;

/**
 * <b>ZoomEventhandler</b> provides event handlers for basic zooming of the
 * canvas view with the right (third) button. The interaction is that the
 * initial mouse press defines the zoom anchor point, and then moving the mouse
 * to the right zooms with a speed proportional to the amount the mouse is moved
 * to the right of the anchor point. Similarly, if the mouse is moved to the
 * left, the the view is zoomed out.
 * <P>
 * On a Mac with its single mouse button one may wish to change the standard
 * right mouse button zooming behavior. This can be easily done with the
 * PInputEventFilter. For example to zoom with button one and shift you would do
 * this:
 * <P>
 * <code>
 * <pre>
 * zoomEventHandler.getEventFilter().setAndMask(InputEvent.BUTTON1_MASK |
 *                                              InputEvent.SHIFT_MASK);
 * </pre>
 * </code>
 * <P>
 *
 * @version 1.0
 * @author Jesse Grosjean
 */
public class CustomPZoomEventHandler extends PDragSequenceEventHandler {

    /**
     * A constant used to adjust how sensitive the zooming will be to mouse
     * movement. The larger the number, the more each delta pixel will affect
     * zooming.
     */
    private static final double ZOOM_SENSITIVITY = 0.001;
    public double minScale = 0;
    public double maxScale = Double.MAX_VALUE;
    private Point2D viewZoomPoint;
    public CustomGraphEditor customGraphEditor;

    /**
     * Creates a new zoom handler.
     */
    public CustomPZoomEventHandler(CustomGraphEditor customGraphEditor) {
        super();
        setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
        this.customGraphEditor = customGraphEditor;
    }

    // ****************************************************************
    // Zooming
    // ****************************************************************
    /**
     * Returns the minimum view magnification factor that this event handler is
     * bound by. The default is 0.
     *
     * @return the minimum camera view scale
     */
    public double getMinScale() {
        return minScale;
    }

    /**
     * Sets the minimum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>minScale</code> is larger than the current scale.
     *
     * @param minScale the minimum scale, must not be negative.
     */
    public void setMinScale(final double minScale) {
        this.minScale = minScale;
    }

    /**
     * Returns the maximum view magnification factor that this event handler is
     * bound by. The default is Double.MAX_VALUE.
     *
     * @return the maximum camera view scale
     */
    public double getMaxScale() {
        return maxScale;
    }

    /**
     * Sets the maximum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>maxScale</code> is smaller than the current scale. Use
     * Double.MAX_VALUE to specify the largest possible scale.
     *
     * @param maxScale the maximum scale, must not be negative.
     */
    public void setMaxScale(final double maxScale) {
        this.maxScale = maxScale;
    }

    /**
     * Records the start point of the zoom. Used when calculating the delta for
     * zoom speed.
     *
     * @param event event responsible for starting the zoom interaction
     */
    protected void dragActivityFirstStep(final PInputEvent event) {
        viewZoomPoint = event.getPosition();
        super.dragActivityFirstStep(event);
    }

    /**
     * Updates the current zoom periodically, regardless of whether the mouse
     * has moved recently.
     *
     * @param event contains information about the current state of the mouse
     */
    protected void dragActivityStep(final PInputEvent event) {

        double x = event.getCanvasPosition().getX();
        double y = event.getCanvasPosition().getY();
        // this method is exited if the controls for panning or zooming etc.
        // have been clicked.
        // this is so that regular mouse panning cannot be done when the user is
        // trying to pan with the arrow controls.
        // subtracting 2 from y is done because the coordinates are off by
        // 2 for some unkown reason.
        if (x >= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panUpButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panUpButtonCoordinates.y
                && y - 2 <= customGraphEditor.panUpButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panRightButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panRightButtonCoordinates.y
                && y - 2 <= customGraphEditor.panRightButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panDownButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panDownButtonCoordinates.y
                && y - 2 <= customGraphEditor.panDownButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panLeftButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panLeftButtonCoordinates.y
                && y - 2 <= customGraphEditor.panLeftButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomInButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomInButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomInButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.zoomOutButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.zoomOutButtonCoordinates.y
                && y - 2 <= customGraphEditor.zoomOutButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.growNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.growNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.growNodeSizeButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.shrinkNodeSizeButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.shrinkNodeSizeButtonCoordinates.y
                && y - 2 <= customGraphEditor.shrinkNodeSizeButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.panningToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.panningToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.panningToolButtonCoordinates.y + 39
                || x >= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x
                && x <= customGraphEditor.getWidth() + customGraphEditor.selectionToolButtonCoordinates.x + 39
                && y - 2 >= customGraphEditor.selectionToolButtonCoordinates.y
                && y - 2 <= customGraphEditor.selectionToolButtonCoordinates.y + 39) {
            return;
        }

        final PCamera camera = event.getCamera();

        // final double dx = event.getCanvasPosition().getX() - getMousePressedCanvasPoint().getX();
        // double scaleDelta = 1.0 + ZOOM_SENSITIVITY * dx;

        // the Y coordinates are compared so that zooming occurs when the mouse
        // is moved vertically rather than horizontally as this feels more
        // intuitive
        final double dy = event.getCanvasPosition().getY() - getMousePressedCanvasPoint().getY();
        double scaleDelta = 1.0 + ZOOM_SENSITIVITY * -dy;

        final double currentScale = camera.getViewScale();
        final double newScale = currentScale * scaleDelta;

        if (newScale < minScale) {
            scaleDelta = minScale / currentScale;
        }
        if (maxScale > 0 && newScale > maxScale) {
            scaleDelta = maxScale / currentScale;
        }

        camera.scaleViewAboutPoint(scaleDelta, viewZoomPoint.getX(), viewZoomPoint.getY());
    }
}
