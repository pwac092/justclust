package justclust.toolbar.dendrogram;

import justclust.toolbar.heatmap.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;

/**
 * This class has a method which rearranges the components of a
 * DendrogramJDialog whenever the DendrogramJDialog is resized.
 */
public class DendrogramComponentListener implements
        ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
        DialogSizesAndPositions.dendrogramXCoordinate = DendrogramJDialog.classInstance.getX();
        DialogSizesAndPositions.dendrogramYCoordinate = DendrogramJDialog.classInstance.getY();
    }

    /**
     * This method rearranges the components of a DendrogramJDialog whenever the
     * DendrogramJDialog is resized.
     */
    public void componentResized(ComponentEvent componentEvent) {

        DialogSizesAndPositions.dendrogramWidth = DendrogramJDialog.classInstance.getWidth();
        DialogSizesAndPositions.dendrogramHeight = DendrogramJDialog.classInstance.getHeight();

        DendrogramJDialog.classInstance.helpButton.setBounds(
                DendrogramJDialog.classInstance.jPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        DendrogramJDialog.classInstance.jScrollPane.setBounds(
                10,
                10 + 16 + 10,
                DendrogramJDialog.classInstance.jPanel.getWidth() - (10 + 10),
                DendrogramJDialog.classInstance.jPanel.getHeight() - (10 + 16 + 10 + 10));

        // the validate method is called so that dendrogramJPanel
        // is resized correctly within jScrollPane when the
        // DendrogramJDialog is maximized
        DendrogramJDialog.classInstance.jScrollPane.validate();

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
