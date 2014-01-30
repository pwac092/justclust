package justclust.menubar.clusternetwork;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ClusterNetworkHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        ClusterNetworkHelpJDialog.classInstance.clusterNetworkHelpJScrollPane
                .setBounds(
                10,
                10,
                ClusterNetworkHelpJDialog.classInstance.clusterNetworkHelpJPanel.getWidth() - 20,
                ClusterNetworkHelpJDialog.classInstance.clusterNetworkHelpJPanel.getHeight() - 20);

        // the validate method is called so that clusterNetworkHelpJTextArea
        // is resized correctly within clusterNetworkHelpJScrollPane when the
        // ClusterNetworkHelpJDialog is maximized
        ClusterNetworkHelpJDialog.classInstance.clusterNetworkHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
