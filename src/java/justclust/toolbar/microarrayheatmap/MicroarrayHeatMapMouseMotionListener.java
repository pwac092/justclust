/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import justclust.JustclustJFrame;
import justclust.datastructures.Data;

/**
 *
 * @author wuaz008
 */
public class MicroarrayHeatMapMouseMotionListener implements MouseMotionListener {

    public void mouseDragged(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {
        if (me.getX() > MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetX
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border
                && me.getX() <= MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetX
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.width
                * MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.squareWidth
                && me.getY() > MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetY
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border
                && me.getY() <= MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetY
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border
                + MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.height
                * MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.squareHeight) {
            int row = (me.getY() - MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetY
                    - MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border)
                    / MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.squareHeight;
            int column = (me.getX() - MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.offsetX
                    - MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.border)
                    / MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.squareWidth;
            if (row < MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.matrix.getNumRows()
                    && column < MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.matrix.getNumCols()) {
            MicroarrayHeatMapJDialog.classInstance.microarrayValueJTextField.setText(
                    new DecimalFormat("#.##").format(MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.matrix.get(row, column)));
            }
        }
    }
}
