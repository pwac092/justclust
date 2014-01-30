/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkclusters;

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
public class NetworkClustersChangeListener implements ChangeListener {

    public void stateChanged(ChangeEvent changeEvent) {

        // this code finds the row which is at the top of the currently visible
        // part of the networkClustersDialogJTable, every time the
        // networkClustersDialogJScrollPane is adjusted.
        // when the NetworkClustersJDialog is closed and opened, this value will
        // be used to scroll the networkClustersDialogJScrollPane so that it
        // shows the same part of the networkClustersDialogJTable as before.
        DialogSizesAndPositions.networkClustersRowToScrollTo =
                (NetworkClustersJDialog.classInstance.networkClustersDialogJScrollPane.getVerticalScrollBar().getValue()
                + NetworkClustersJDialog.classInstance.networkClustersDialogJTable.getRowHeight() / 2)
                / NetworkClustersJDialog.classInstance.networkClustersDialogJTable.getRowHeight();

    }
}
