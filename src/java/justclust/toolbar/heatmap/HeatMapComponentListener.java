package justclust.toolbar.heatmap;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;

/**
 * This class has a method which rearranges the components of a HeatMapJDialog
 * whenever the HeatMapJDialog is resized.
 */
public class HeatMapComponentListener implements
        ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
        DialogSizesAndPositions.heatMapXCoordinate = HeatMapJDialog.classInstance.getX();
        DialogSizesAndPositions.heatMapYCoordinate = HeatMapJDialog.classInstance.getY();
    }

    /**
     * This method rearranges the components of a HeatMapJDialog whenever the
     * HeatMapJDialog is resized.
     */
    public void componentResized(ComponentEvent componentEvent) {

        DialogSizesAndPositions.heatMapWidth = HeatMapJDialog.classInstance.getWidth();
        DialogSizesAndPositions.heatMapHeight = HeatMapJDialog.classInstance.getHeight();

        HeatMapJDialog.classInstance.helpButton.setBounds(
                HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        HeatMapJDialog.classInstance.includeLabelsJLabel.setBounds(
                10 + (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 0 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4)
                - (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 0 / 4),
                25);

        HeatMapJDialog.classInstance.includeLabelsJCheckBox.setBounds(
                10 + (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4)
                - (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 1 / 4),
                25);

        HeatMapJDialog.classInstance.edgeWeightJLabel.setBounds(
                10 + (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4)
                - (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 2 / 4),
                25);

        HeatMapJDialog.classInstance.edgeWeightJTextField.setBounds(
                10 + (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4),
                10 + 16 + 10,
                (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 4 / 4)
                - (int) Math.round((double) (HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10)) * 3 / 4),
                25);

        HeatMapJDialog.classInstance.jScrollPane.setBounds(
                10,
                10 + 16 + 10 + 25 + 10,
                HeatMapJDialog.classInstance.jPanel.getWidth() - (10 + 10),
                HeatMapJDialog.classInstance.jPanel.getHeight() - (10 + 16 + 10 + 25 + 10 + 10));

        // the validate method is called so that heatMapJPanel
        // is resized correctly within the jScrollPane when the
        // HeatMapJDialog is maximized
        HeatMapJDialog.classInstance.jScrollPane.validate();

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
