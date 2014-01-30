package justclust.toolbar.networkedges;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkEdgesHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NetworkEdgesHelpJDialog.classInstance.networkEdgesHelpJScrollPane
                .setBounds(
                10,
                10,
                NetworkEdgesHelpJDialog.classInstance.networkEdgesHelpJPanel.getWidth() - 20,
                NetworkEdgesHelpJDialog.classInstance.networkEdgesHelpJPanel.getHeight() - 20);

        // the validate method is called so that networkEdgesHelpJTextArea
        // is resized correctly within networkEdgesHelpJScrollPane when the
        // networkEdgesHelpJDialog is maximized
        NetworkEdgesHelpJDialog.classInstance.networkEdgesHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
