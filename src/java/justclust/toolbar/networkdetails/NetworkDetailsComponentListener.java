package justclust.toolbar.networkdetails;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class NetworkDetailsComponentListener implements
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
        DialogSizesAndPositions.networkDetailsXCoordinate = NetworkDetailsJDialog.classInstance.getX();
        DialogSizesAndPositions.networkDetailsYCoordinate = NetworkDetailsJDialog.classInstance.getY();
    }

    /**
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        DialogSizesAndPositions.networkDetailsWidth = NetworkDetailsJDialog.classInstance.getWidth();
        DialogSizesAndPositions.networkDetailsHeight = NetworkDetailsJDialog.classInstance.getHeight();

        NetworkDetailsJDialog.classInstance.networkDetailsHelpButton
                .setBounds(
                NetworkDetailsJDialog.classInstance.networkDetailsDialogJPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        NetworkDetailsJDialog.classInstance.networkDetailsDialogJScrollPane
                .setBounds(
                10,
                10 + 16 + 10,
                NetworkDetailsJDialog.classInstance.networkDetailsDialogJPanel.getWidth() - (10 + 10),
                NetworkDetailsJDialog.classInstance.networkDetailsDialogJPanel.getHeight() - (10 + 16 + 10 + 10));

        // the validate method is called so that networkDetailsDialogJTextArea
        // is resized correctly within networkDetailsDialogJScrollPane when the
        // NetworkDetailsJDialog is maximized
        NetworkDetailsJDialog.classInstance.networkDetailsDialogJScrollPane.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
