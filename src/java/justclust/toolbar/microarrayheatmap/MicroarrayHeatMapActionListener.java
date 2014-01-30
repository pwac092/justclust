/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import justclust.DialogSizesAndPositions;

/**
 *
 * @author wuaz008
 */
public class MicroarrayHeatMapActionListener implements ActionListener {

    public void actionPerformed(ActionEvent ae) {
        MicroarrayHeatMapJDialog.classInstance.microarrayHeatMapJPanel.repaint();
        DialogSizesAndPositions.microarrayHeatMapIncludeLabels = MicroarrayHeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected();
    }
    
}
