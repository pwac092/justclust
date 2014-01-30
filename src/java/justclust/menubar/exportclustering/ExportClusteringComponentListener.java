package justclust.menubar.exportclustering;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * This class has a method which rearranges the components of a
 * ExportClusteringJDialog whenever the ExportClusteringJDialog is resized.
 */
public class ExportClusteringComponentListener implements
        ComponentListener {

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    /**
     * This method rearranges the components of a ExportClusteringJDialog whenever
     * the ExportClusteringJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // progressBarVerticalDisplacement is based on whether the
        // exportClusteringJProgressBar has been added to the ExportClusteringJDialog
        // because a clustering is being exported.
        // if the exportClusteringJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ExportClusteringJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the exportClusteringJProgressBar is the
        // exportClusteringDialogJPanel, then the exportClusteringJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ExportClusteringJDialog.classInstance.exportClusteringJProgressBar.getParent() == ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        ExportClusteringJDialog.classInstance.exportClusteringHelpButton
                .setBounds(
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - progressBarVerticalDisplacement / 2),
                16,
                16);
        ExportClusteringJDialog.classInstance.fileNameJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        ExportClusteringJDialog.classInstance.fileNameJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        ExportClusteringJDialog.classInstance.browseButton
                .setBounds(
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                25,
                25);
        ExportClusteringJDialog.classInstance.exportClusteringJButton
                .setBounds(
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the exportClusteringJProgressBar is set here.
        // if the parent of the exportClusteringJProgressBar is the
        // exportClusteringDialogJPanel, then the exportClusteringJProgressBar has been
        // added.
        if (ExportClusteringJDialog.classInstance.exportClusteringJProgressBar.getParent() == ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel) {
            ExportClusteringJDialog.classInstance.exportClusteringJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    - progressBarVerticalDisplacement / 2),
                    ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getWidth() - (10 + 10),
                    25);
        }

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
