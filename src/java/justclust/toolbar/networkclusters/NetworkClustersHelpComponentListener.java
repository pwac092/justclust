package justclust.toolbar.networkclusters;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkClustersHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NetworkClustersHelpJDialog.classInstance.networkClustersHelpJScrollPane
                .setBounds(
                10,
                10,
                NetworkClustersHelpJDialog.classInstance.networkClustersHelpJPanel.getWidth() - 20,
                NetworkClustersHelpJDialog.classInstance.networkClustersHelpJPanel.getHeight() - 20);

        // the validate method is called so that networkClustersHelpJTextArea
        // is resized correctly within networkClustersHelpJScrollPane when the
        // networkClustersHelpJDialog is maximized
        NetworkClustersHelpJDialog.classInstance.networkClustersHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
