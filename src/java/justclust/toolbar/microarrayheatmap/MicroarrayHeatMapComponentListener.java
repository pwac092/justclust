package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;

/**
 * This class has a method which rearranges the components of a MicroarrayHeatMapJDialog
 * whenever the MicroarrayHeatMapJDialog is resized.
 */
public class MicroarrayHeatMapComponentListener implements
        ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
        DialogSizesAndPositions.microarrayHeatMapXCoordinate = MicroarrayHeatMapJDialog.classInstance.getX();
        DialogSizesAndPositions.microarrayHeatMapYCoordinate = MicroarrayHeatMapJDialog.classInstance.getY();
    }

    /**
     * This method rearranges the components of a MicroarrayHeatMapJDialog whenever the
     * MicroarrayHeatMapJDialog is resized.
     */
    public void componentResized(ComponentEvent componentEvent) {

        DialogSizesAndPositions.heatMapWidth = MicroarrayHeatMapJDialog.classInstance.getWidth();
        DialogSizesAndPositions.heatMapHeight = MicroarrayHeatMapJDialog.classInstance.getHeight();

        MicroarrayHeatMapJDialog.classInstance.helpButton.setBounds(
                MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        MicroarrayHeatMapJDialog.classInstance.includeLabelsJLabel.setBounds(
                10 + (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 0 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4)
                - (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 0 / 4),
                25);

        MicroarrayHeatMapJDialog.classInstance.includeLabelsJCheckBox.setBounds(
                10 + (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4)
                - (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4),
                25);

        MicroarrayHeatMapJDialog.classInstance.microarrayValueJLabel.setBounds(
                10 + (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4)
                - (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4),
                25);

        MicroarrayHeatMapJDialog.classInstance.microarrayValueJTextField.setBounds(
                10 + (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 4 / 4)
                - (int) Math.round((double) (MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4),
                25);

        MicroarrayHeatMapJDialog.classInstance.jScrollPane.setBounds(
                10,
                10 + 16 + 10 + 25 + 10,
                MicroarrayHeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10),
                MicroarrayHeatMapJDialog.classInstance.jPanel.getHeight() - (10 + 16 + 10 + 25 + 10 + 10));

        // the validate method is called so that heatMapJPanel
        // is resized correctly within the jScrollPane when the
        // MicroarrayHeatMapJDialog is maximized
        MicroarrayHeatMapJDialog.classInstance.jScrollPane.validate();

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
