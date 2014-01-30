/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.filterclusters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import justclust.datastructures.Data;
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class FilterClustersKeyListener implements KeyListener {

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {

        // cancel the method if there are any errors
        try {
            Integer.parseInt(FilterClustersJDialog.classInstance.showLargestClustersJTextField.getText());
            Integer.parseInt(FilterClustersJDialog.classInstance.hideSmallestClustersJTextField.getText());
        } catch (NumberFormatException numberFormatException) {
            return;
        }
        
        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        if (ke.getComponent() == FilterClustersJDialog.classInstance.showLargestClustersJTextField) {
            int hideSmallestClusters = data.networkClusters.size() - Integer.parseInt(FilterClustersJDialog.classInstance.showLargestClustersJTextField.getText());
            FilterClustersJDialog.classInstance.hideSmallestClustersJTextField.setText(String.valueOf(hideSmallestClusters));
        }

        if (ke.getComponent() == FilterClustersJDialog.classInstance.hideSmallestClustersJTextField) {
            int showLargestClusters = data.networkClusters.size() - Integer.parseInt(FilterClustersJDialog.classInstance.hideSmallestClustersJTextField.getText());
            FilterClustersJDialog.classInstance.showLargestClustersJTextField.setText(String.valueOf(showLargestClusters));
        }

    }
}
