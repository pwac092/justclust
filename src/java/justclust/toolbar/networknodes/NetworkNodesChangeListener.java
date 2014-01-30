/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networknodes;

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
public class NetworkNodesChangeListener implements ChangeListener {

    public void stateChanged(ChangeEvent changeEvent) {

        // this code finds the row which is at the top of the currently visible
        // part of the networkNodesDialogJTable, every time the
        // networkNodesDialogJScrollPane is adjusted.
        // when the NetworkNodesJDialog is closed and opened, this value will
        // be used to scroll the networkNodesDialogJScrollPane so that it
        // shows the same part of the networkNodesDialogJTable as before.
        DialogSizesAndPositions.networkNodesRowToScrollTo =
                (NetworkNodesJDialog.classInstance.networkNodesDialogJScrollPane.getVerticalScrollBar().getValue()
                + NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getRowHeight() / 2)
                / NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getRowHeight();

    }
}
