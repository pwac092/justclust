/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.searchnetwork;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author wuaz008
 */
public class SearchNetworkMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {

        if (me.getComponent() == SearchNetworkJDialog.classInstance.searchNetworkHelpButton) {
            new SearchNetworkHelpJDialog();
        }

        if (me.getComponent() == SearchNetworkJDialog.classInstance.nodesJTable) {
            SearchNetworkJDialog.classInstance.nodeOptionsJButton.setEnabled(true);
        }

        if (me.getComponent() == SearchNetworkJDialog.classInstance.edgesJTable) {
            SearchNetworkJDialog.classInstance.edgeOptionsJButton.setEnabled(true);
        }

        if (me.getComponent() == SearchNetworkJDialog.classInstance.clustersJTable) {
            SearchNetworkJDialog.classInstance.clusterOptionsJButton.setEnabled(true);
        }

    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }
}
