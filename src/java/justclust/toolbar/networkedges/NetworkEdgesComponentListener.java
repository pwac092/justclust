package justclust.toolbar.networkedges;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class NetworkEdgesComponentListener implements
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
        
        DialogSizesAndPositions.networkEdgesXCoordinate = NetworkEdgesJDialog.classInstance.getX();
        DialogSizesAndPositions.networkEdgesYCoordinate = NetworkEdgesJDialog.classInstance.getY();
        
    }

    /**
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.networkEdgesWidth = NetworkEdgesJDialog.classInstance.getWidth();
        DialogSizesAndPositions.networkEdgesHeight = NetworkEdgesJDialog.classInstance.getHeight();

        NetworkEdgesJDialog.classInstance.networkEdgesHelpButton
                .setBounds(
                NetworkEdgesJDialog.classInstance.networkEdgesDialogJPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        NetworkEdgesJDialog.classInstance.networkEdgesDialogJScrollPane
                .setBounds(
                10,
                10 + 16 + 10,
                NetworkEdgesJDialog.classInstance.networkEdgesDialogJPanel.getWidth() - (10 + 10),
                NetworkEdgesJDialog.classInstance.networkEdgesDialogJPanel.getHeight() - (10 + 16 + 10 + 10));

        // the validate method is called so that networkEdgesDialogJTable
        // is resized correctly within networkEdgesDialogJScrollPane when the
        // NetworkEdgesJDialog is maximized
        NetworkEdgesJDialog.classInstance.networkEdgesDialogJScrollPane.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
