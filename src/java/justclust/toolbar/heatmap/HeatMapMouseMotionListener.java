/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.heatmap;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import justclust.JustclustJFrame;
import justclust.datastructures.Data;

/**
 *
 * @author wuaz008
 */
public class HeatMapMouseMotionListener implements MouseMotionListener {

    public void mouseDragged(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {
        if (me.getX() > HeatMapJDialog.classInstance.heatMapJPanel.offsetX
                + HeatMapJDialog.classInstance.heatMapJPanel.border
                && me.getX() <= HeatMapJDialog.classInstance.heatMapJPanel.offsetX
                + HeatMapJDialog.classInstance.heatMapJPanel.border
                + HeatMapJDialog.classInstance.heatMapJPanel.width
                * HeatMapJDialog.classInstance.heatMapJPanel.squareWidth
                && me.getY() > HeatMapJDialog.classInstance.heatMapJPanel.offsetY
                + HeatMapJDialog.classInstance.heatMapJPanel.border
                && me.getY() <= HeatMapJDialog.classInstance.heatMapJPanel.offsetY
                + HeatMapJDialog.classInstance.heatMapJPanel.border
                + HeatMapJDialog.classInstance.heatMapJPanel.height
                * HeatMapJDialog.classInstance.heatMapJPanel.squareHeight) {
            int row = (me.getY() - HeatMapJDialog.classInstance.heatMapJPanel.offsetY
                    - HeatMapJDialog.classInstance.heatMapJPanel.border)
                    / HeatMapJDialog.classInstance.heatMapJPanel.squareHeight;
            int column = (me.getX() - HeatMapJDialog.classInstance.heatMapJPanel.offsetX
                    - HeatMapJDialog.classInstance.heatMapJPanel.border)
                    / HeatMapJDialog.classInstance.heatMapJPanel.squareWidth;
            if (row < HeatMapJDialog.classInstance.heatMapJPanel.matrix.getNumRows()
                    && column < HeatMapJDialog.classInstance.heatMapJPanel.matrix.getNumCols()) {
            HeatMapJDialog.classInstance.edgeWeightJTextField.setText(
                    new DecimalFormat("#.##").format(HeatMapJDialog.classInstance.heatMapJPanel.matrix.get(row, column)));
            }
        }
    }
}
