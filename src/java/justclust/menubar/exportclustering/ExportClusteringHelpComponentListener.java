package justclust.menubar.exportclustering;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ExportClusteringHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        ExportClusteringHelpJDialog.classInstance.exportClusteringHelpJScrollPane
                .setBounds(
                10,
                10,
                ExportClusteringHelpJDialog.classInstance.exportClusteringHelpJPanel.getWidth() - 20,
                ExportClusteringHelpJDialog.classInstance.exportClusteringHelpJPanel.getHeight() - 20);

        // the validate method is called so that exportClusteringHelpJTextArea
        // is resized correctly within exportClusteringHelpJScrollPane when the
        // exportClusteringHelpJDialog is maximized
        ExportClusteringHelpJDialog.classInstance.exportClusteringHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
