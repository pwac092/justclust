package justclust.toolbar.filterclusters;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;

public class FilterClustersComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.filterClustersXCoordinate = FilterClustersJDialog.classInstance.getX();
        DialogSizesAndPositions.filterClustersYCoordinate = FilterClustersJDialog.classInstance.getY();
        
    }

    public void componentResized(ComponentEvent componentEvent) {

        FilterClustersJDialog.classInstance.filterClustersHelpButton
                .setBounds(
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10)),
                16,
                16);

        FilterClustersJDialog.classInstance.showLargestClustersJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.showLargestClustersJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideSmallestClustersJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideSmallestClustersJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersAboveNodeAmountJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersAboveNodeAmountJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersBelowNodeAmountJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersBelowNodeAmountJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersBelowDensityThresholdJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.hideClustersBelowDensityThresholdJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)),
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        FilterClustersJDialog.classInstance.filterClustersJButton
                .setBounds(
                FilterClustersJDialog.classInstance.filterClustersJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) FilterClustersJDialog.classInstance.filterClustersJPanel.getHeight() * 1 / 2
                - 243.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)),
                200,
                25);

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
