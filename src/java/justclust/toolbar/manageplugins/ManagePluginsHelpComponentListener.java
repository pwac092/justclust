package justclust.toolbar.manageplugins;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ManagePluginsHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        ManagePluginsHelpJDialog.classInstance.managePluginsHelpJScrollPane
                .setBounds(
                10,
                10,
                ManagePluginsHelpJDialog.classInstance.managePluginsHelpJPanel.getWidth() - 20,
                ManagePluginsHelpJDialog.classInstance.managePluginsHelpJPanel.getHeight() - 20);

        // the validate method is called so that managePluginsHelpJTextArea
        // is resized correctly within managePluginsHelpJScrollPane when the
        // managePluginsHelpJDialog is maximized
        ManagePluginsHelpJDialog.classInstance.managePluginsHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
