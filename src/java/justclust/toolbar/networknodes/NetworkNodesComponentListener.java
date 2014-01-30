package justclust.toolbar.networknodes;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class NetworkNodesComponentListener implements
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
        
        DialogSizesAndPositions.networkNodesXCoordinate = NetworkNodesJDialog.classInstance.getX();
        DialogSizesAndPositions.networkNodesYCoordinate = NetworkNodesJDialog.classInstance.getY();
        
    }

    /**
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.networkNodesWidth = NetworkNodesJDialog.classInstance.getWidth();
        DialogSizesAndPositions.networkNodesHeight = NetworkNodesJDialog.classInstance.getHeight();

        NetworkNodesJDialog.classInstance.networkNodesHelpButton
                .setBounds(
                NetworkNodesJDialog.classInstance.networkNodesDialogJPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        NetworkNodesJDialog.classInstance.networkNodesDialogJScrollPane
                .setBounds(
                10,
                10 + 16 + 10,
                NetworkNodesJDialog.classInstance.networkNodesDialogJPanel.getWidth() - (10 + 10),
                NetworkNodesJDialog.classInstance.networkNodesDialogJPanel.getHeight() - (10 + 16 + 10 + 10));

        // the validate method is called so that networkNodesDialogJTable
        // is resized correctly within networkNodesDialogJScrollPane when the
        // NetworkNodesJDialog is maximized
        NetworkNodesJDialog.classInstance.networkNodesDialogJScrollPane.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
