package justclust.menubar.exportnetwork;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ExportNetworkHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        ExportNetworkHelpJDialog.classInstance.exportNetworkHelpJScrollPane
                .setBounds(
                10,
                10,
                ExportNetworkHelpJDialog.classInstance.exportNetworkHelpJPanel.getWidth() - 20,
                ExportNetworkHelpJDialog.classInstance.exportNetworkHelpJPanel.getHeight() - 20);

        // the validate method is called so that exportNetworkHelpJTextArea
        // is resized correctly within exportNetworkHelpJScrollPane when the
        // exportNetworkHelpJDialog is maximized
        ExportNetworkHelpJDialog.classInstance.exportNetworkHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
