package justclust.menubar.newnetworkfromfile;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NewNetworkFromFileHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NewNetworkFromFileHelpJDialog.classInstance.newNetworkFromFileHelpJScrollPane
                .setBounds(
                10,
                10,
                NewNetworkFromFileHelpJDialog.classInstance.newNetworkFromFileHelpJPanel.getWidth() - 20,
                NewNetworkFromFileHelpJDialog.classInstance.newNetworkFromFileHelpJPanel.getHeight() - 20);

        // the validate method is called so that newNetworkFromFileHelpJTextArea
        // is resized correctly within newNetworkFromFileHelpJScrollPane when the
        // NewNetworkFromFileHelpJDialog is maximized
        NewNetworkFromFileHelpJDialog.classInstance.newNetworkFromFileHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
