package justclust.toolbar.searchnetwork;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networknodes.NetworkNodesJDialog;

public class SearchNetworkHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        SearchNetworkHelpJDialog.classInstance.searchNetworkHelpJScrollPane
                .setBounds(
                10,
                10,
                SearchNetworkHelpJDialog.classInstance.searchNetworkHelpJPanel.getWidth() - 20,
                SearchNetworkHelpJDialog.classInstance.searchNetworkHelpJPanel.getHeight() - 20);

        // the validate method is called so that searchNetworkHelpJTextArea
        // is resized correctly within searchNetworkHelpJScrollPane when the
        // searchNetworkHelpJDialog is maximized
        SearchNetworkHelpJDialog.classInstance.searchNetworkHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
