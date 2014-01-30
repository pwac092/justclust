package justclust.toolbar.networknodes;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkNodesHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NetworkNodesHelpJDialog.classInstance.networkNodesHelpJScrollPane
                .setBounds(
                10,
                10,
                NetworkNodesHelpJDialog.classInstance.networkNodesHelpJPanel.getWidth() - (10 + 10),
                NetworkNodesHelpJDialog.classInstance.networkNodesHelpJPanel.getHeight() - (10 + 10));

        // the validate method is called so that networkNodesHelpJTextArea
        // is resized correctly within networkNodesHelpJScrollPane when the
        // networkNodesHelpJDialog is maximized
        NetworkNodesHelpJDialog.classInstance.networkNodesHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
