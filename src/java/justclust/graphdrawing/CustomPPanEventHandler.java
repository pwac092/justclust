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

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import justclust.JustclustJFrame;

/**
 * <b>PPanEventHandler</b> provides event handlers for basic panning of the
 * canvas view with the left mouse. The interaction is that clicking and
 * dragging the mouse translates the view so that the point on the surface stays
 * under the mouse.
 * <P>
 *
 * @version 1.0
 * @author Jesse Grosjean
 */
public class CustomPPanEventHandler extends PDragSequenceEventHandler {

    private static final int DEFAULT_MAX_AUTOPAN_SPEED = 750;
    private static final int DEFAULT_MIN_AUTOPAN_SPEED = 250;
    private boolean autopan;
    private double minAutopanSpeed = DEFAULT_MIN_AUTOPAN_SPEED;
    private double maxAutopanSpeed = DEFAULT_MAX_AUTOPAN_SPEED;
    public CustomGraphEditor customGraphEditor;

    /**
     * Constructs a Pan Event Handler that will by default perform auto-panning.
     */
    public CustomPPanEventHandler(CustomGraphEditor customGraphEditor) {

        super();
        setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
        setAutopan(true);

        this.customGraphEditor = customGraphEditor;

    }

    /**
     * Updates the view in response to a user initiated drag event.
     *
     * @param event event responsible for the drag
     */
    protected void drag(final PInputEvent event) {
        super.drag(event);
        pan(event);
    }

    /**
     * Pans the camera in response to the pan event provided.
     *
     * @param event contains details about the drag used to translate the view
     */
    protected void pan(final PInputEvent event) {

        // if the selection tool button has been pressed, panning should not be
        // allowed so this method returns.
        // panningToolMode represents which of the panning tool button and
        // selection tool button have been pressed.
        // if the control key is pressed, this changes which mode is in use.
        if (!customGraphEditor.customGraphEditorMouseListener.panningToolMode
                && !customGraphEditor.customGraphEditorMouseListener.controlKeyPressed
                || customGraphEditor.customGraphEditorMouseListener.panningToolMode
                && customGraphEditor.customGraphEditorMouseListener.controlKeyPressed) {
            return;
        }

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

        JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        final PCamera c = event.getCamera();
        final Point2D l = event.getPosition();

        if (c.getViewBounds().contains(l)) {
            final PDimension d = event.getDelta();
            c.translateView(d.getWidth(), d.getHeight());
        }
    }

    // ****************************************************************
    // Auto Pan
    // ****************************************************************
    /**
     * Determines if auto-panning will occur or not.
     *
     * @param autopan true if auto-panning functionality will be active
     */
    public void setAutopan(final boolean autopan) {
        this.autopan = autopan;
    }

    /**
     * Returns whether the auto-panning functoinality is enabled.
     *
     * @return true if auto-panning is enabled
     */
    public boolean getAutopan() {
        return autopan;
    }

    /**
     * Set the minAutoPan speed in pixels per second.
     *
     * @param minAutopanSpeed number of pixels to assign as the minimum the
     * autopan feature can pan the view
     */
    public void setMinAutopanSpeed(final double minAutopanSpeed) {
        this.minAutopanSpeed = minAutopanSpeed;
    }

    /**
     * Set the maxAutoPan speed in pixels per second.
     *
     * @param maxAutopanSpeed number of pixels to assign as the maximum the
     * autopan feature can pan the view
     */
    public void setMaxAutopanSpeed(final double maxAutopanSpeed) {
        this.maxAutopanSpeed = maxAutopanSpeed;
    }

    /**
     * Returns the minAutoPan speed in pixels per second.
     *
     * @since 1.3
     * @return minimum distance the autopan feature can pan the view
     */
    public double getMinAutoPanSpeed() {
        return minAutopanSpeed;
    }

    /**
     * Returns the maxAutoPan speed in pixels per second.
     *
     * @since 1.3
     * @return max distance the autopan feature can pan the view by
     */
    public double getMaxAutoPanSpeed() {
        return maxAutopanSpeed;
    }

    /**
     * Performs auto-panning if enabled, even when the mouse is not moving.
     *
     * @param event current drag relevant details about the drag activity
     */
    protected void dragActivityStep(final PInputEvent event) {

        if (!autopan) {
            return;
        }

        final PCamera c = event.getCamera();
        final PBounds b = c.getBoundsReference();
        final Point2D l = event.getPositionRelativeTo(c);
        final int outcode = b.outcode(l);
        final PDimension delta = new PDimension();

        if ((outcode & Rectangle2D.OUT_TOP) != 0) {
            delta.height = validatePanningSpeed(-1.0 - 0.5 * Math.abs(l.getY() - b.getY()));
        } else if ((outcode & Rectangle2D.OUT_BOTTOM) != 0) {
            delta.height = validatePanningSpeed(1.0 + 0.5 * Math.abs(l.getY() - (b.getY() + b.getHeight())));
        }

        if ((outcode & Rectangle2D.OUT_RIGHT) != 0) {
            delta.width = validatePanningSpeed(1.0 + 0.5 * Math.abs(l.getX() - (b.getX() + b.getWidth())));
        } else if ((outcode & Rectangle2D.OUT_LEFT) != 0) {
            delta.width = validatePanningSpeed(-1.0 - 0.5 * Math.abs(l.getX() - b.getX()));
        }

        c.localToView(delta);

        if (delta.width != 0 || delta.height != 0) {
            c.translateView(delta.width, delta.height);
        }
    }

    /**
     * Clips the panning speed to the minimum and maximum auto-pan speeds
     * assigned. If delta is below the threshold, it will be increased. If
     * above, it will be decreased.
     *
     * @param delta auto-pan delta to be clipped
     * @return clipped delta value.
     */
    protected double validatePanningSpeed(final double delta) {
        final double stepsPerSecond = 1000d / getDragActivity().getStepRate();
        final double minDelta = minAutopanSpeed / stepsPerSecond;
        final double maxDelta = maxAutopanSpeed / stepsPerSecond;

        final double absDelta = Math.abs(delta);

        final double clippedDelta;
        if (absDelta < minDelta) {
            clippedDelta = minDelta;
        } else if (absDelta > maxDelta) {
            clippedDelta = maxDelta;
        } else {
            clippedDelta = delta;
        }

        if (delta < 0) {
            return -clippedDelta;
        } else {
            return clippedDelta;
        }
    }
}