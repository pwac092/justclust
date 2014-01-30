package justclust.menubar.exportnetwork;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * This class has a method which rearranges the components of a
 * ExportNetworkJDialog whenever the ExportNetworkJDialog is resized.
 */
public class ExportNetworkComponentListener implements ComponentListener {

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
     * This method rearranges the components of a ExportNetworkJDialog whenever
     * the ExportNetworkJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // progressBarVerticalDisplacement is based on whether the
        // exportNetworkJProgressBar has been added to the ExportNetworkJDialog
        // because a network is being exported.
        // if the exportNetworkJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ExportNetworkJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the exportNetworkJProgressBar is the
        // exportNetworkDialogJPanel, then the exportNetworkJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ExportNetworkJDialog.classInstance.exportNetworkJProgressBar.getParent() == ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        ExportNetworkJDialog.classInstance.exportNetworkHelpButton
                .setBounds(
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - progressBarVerticalDisplacement / 2),
                16,
                16);
        ExportNetworkJDialog.classInstance.fileNameJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        ExportNetworkJDialog.classInstance.fileNameJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        ExportNetworkJDialog.classInstance.browseButton
                .setBounds(
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                25,
                25);
        ExportNetworkJDialog.classInstance.exportNetworkJButton
                .setBounds(
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the exportNetworkJProgressBar is set here.
        // if the parent of the exportNetworkJProgressBar is the
        // exportNetworkDialogJPanel, then the exportNetworkJProgressBar has been
        // added.
        if (ExportNetworkJDialog.classInstance.exportNetworkJProgressBar.getParent() == ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel) {
            ExportNetworkJDialog.classInstance.exportNetworkJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    - progressBarVerticalDisplacement / 2),
                    ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getWidth() - (10 + 10),
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
