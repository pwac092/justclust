/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.heatmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import justclust.DialogSizesAndPositions;

/**
 *
 * @author wuaz008
 */
public class HeatMapActionListener implements ActionListener {

    public void actionPerformed(ActionEvent ae) {
        HeatMapJDialog.classInstance.heatMapJPanel.repaint();
        DialogSizesAndPositions.heatMapIncludeLabels = HeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected();
    }
    
}
