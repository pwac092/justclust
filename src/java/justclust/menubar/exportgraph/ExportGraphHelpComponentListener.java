package justclust.menubar.exportgraph;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ExportGraphHelpComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
    }

    public void componentResized(ComponentEvent componentEvent) {

        ExportGraphHelpJDialog.classInstance.exportGraphHelpJScrollPane
                .setBounds(
                10,
                10,
                ExportGraphHelpJDialog.classInstance.exportGraphHelpJPanel.getWidth() - 20,
                ExportGraphHelpJDialog.classInstance.exportGraphHelpJPanel.getHeight() - 20);

        // the validate method is called so that exportGraphHelpJTextArea
        // is resized correctly within exportGraphHelpJScrollPane when the
        // exportGraphHelpJDialog is maximized
        ExportGraphHelpJDialog.classInstance.exportGraphHelpJScrollPane.validate();

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
