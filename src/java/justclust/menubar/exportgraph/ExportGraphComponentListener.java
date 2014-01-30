package justclust.menubar.exportgraph;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

// this class has a method which rearranges the components of a
// ExportGraphJDialog whenever the ExportGraphJDialog is resized
public class ExportGraphComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
    }

    // this method rearranges the components of a ExportGraphJDialog whenever
    // the ExportGraphJDialog is resized
    public void componentResized(ComponentEvent componentEvent) {

        // progressBarVerticalDisplacement is based on whether the
        // exportGraphJProgressBar has been added to the ExportGraphJDialog
        // because a graph is being exported.
        // if the exportGraphJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ExportGraphJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the exportGraphJProgressBar is the
        // exportGraphDialogJPanel, then the exportGraphJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ExportGraphJDialog.classInstance.exportGraphJProgressBar.getParent() == ExportGraphJDialog.classInstance.exportGraphJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        ExportGraphJDialog.classInstance.exportGraphHelpButton
                .setBounds(
                ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - progressBarVerticalDisplacement / 2),
                16,
                16);
        ExportGraphJDialog.classInstance.fileNameJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        ExportGraphJDialog.classInstance.fileNameJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        ExportGraphJDialog.classInstance.browseButton
                .setBounds(
                ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                25,
                25);
        ExportGraphJDialog.classInstance.exportGraphJButton
                .setBounds(
                ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the exportGraphJProgressBar is set here.
        // if the parent of the exportGraphJProgressBar is the
        // exportGraphDialogJPanel, then the exportGraphJProgressBar has been
        // added.
        if (ExportGraphJDialog.classInstance.exportGraphJProgressBar.getParent() == ExportGraphJDialog.classInstance.exportGraphJPanel) {
            ExportGraphJDialog.classInstance.exportGraphJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    - progressBarVerticalDisplacement / 2),
                    ExportGraphJDialog.classInstance.exportGraphJPanel.getWidth() - (10 + 10),
                    25);
        }

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
