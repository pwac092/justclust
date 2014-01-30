package justclust.toolbar.networkdetails;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkDetailsHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NetworkDetailsHelpJDialog.classInstance.networkDetailsHelpJScrollPane
                .setBounds(
                10,
                10,
                NetworkDetailsHelpJDialog.classInstance.networkDetailsHelpJPanel.getWidth() - 20,
                NetworkDetailsHelpJDialog.classInstance.networkDetailsHelpJPanel.getHeight() - 20);

        // the validate method is called so that networkDetailsHelpJTextArea
        // is resized correctly within networkDetailsHelpJScrollPane when the
        // networkDetailsHelpJDialog is maximized
        NetworkDetailsHelpJDialog.classInstance.networkDetailsHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
