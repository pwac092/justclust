/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkedges;

import java.awt.FontMetrics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import justclust.DialogSizesAndPositions;

/**
 *
 * @author wuaz008
 */
public class NetworkEdgesChangeListener implements ChangeListener {

    public void stateChanged(ChangeEvent changeEvent) {

        // this code finds the row which is at the top of the currently visible
        // part of the networkEdgesDialogJTable, every time the
        // networkEdgesDialogJScrollPane is adjusted.
        // when the NetworkEdgesJDialog is closed and opened, this value will
        // be used to scroll the networkEdgesDialogJScrollPane so that it
        // shows the same part of the networkEdgesDialogJTable as before.
        DialogSizesAndPositions.networkEdgesRowToScrollTo =
                (NetworkEdgesJDialog.classInstance.networkEdgesDialogJScrollPane.getVerticalScrollBar().getValue()
                + NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.getRowHeight() / 2)
                / NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.getRowHeight();
        
    }
    
}
