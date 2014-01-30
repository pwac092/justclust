package justclust.toolbar.networkclusters;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.manageplugins.ManagePluginsJDialog;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class NetworkClustersComponentListener implements
        ComponentListener {

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentMoved(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.networkClustersXCoordinate = NetworkClustersJDialog.classInstance.getX();
        DialogSizesAndPositions.networkClustersYCoordinate = NetworkClustersJDialog.classInstance.getY();
        
    }

    /**
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.networkClustersWidth = NetworkClustersJDialog.classInstance.getWidth();
        DialogSizesAndPositions.networkClustersHeight = NetworkClustersJDialog.classInstance.getHeight();

        NetworkClustersJDialog.classInstance.networkClustersHelpButton
                .setBounds(
                NetworkClustersJDialog.classInstance.networkClustersDialogJPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        NetworkClustersJDialog.classInstance.networkClustersDialogJScrollPane
                .setBounds(
                10,
                10 + 16 + 10,
                NetworkClustersJDialog.classInstance.networkClustersDialogJPanel.getWidth() - (10 + 10),
                NetworkClustersJDialog.classInstance.networkClustersDialogJPanel.getHeight() - (10 + 16 + 10 + 10));

        // the validate method is called so that networkClustersDialogJTable
        // is resized correctly within networkClustersDialogJScrollPane when the
        // NetworkClustersJDialog is maximized
        NetworkClustersJDialog.classInstance.networkClustersDialogJScrollPane.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
