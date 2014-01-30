package justclust.toolbar.filterclusters;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FilterClustersHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        FilterClustersHelpJDialog.classInstance.filterClustersHelpJScrollPane
                .setBounds(
                10,
                10,
                FilterClustersHelpJDialog.classInstance.filterClustersHelpJPanel.getWidth() - 20,
                FilterClustersHelpJDialog.classInstance.filterClustersHelpJPanel.getHeight() - 20);

        // the validate method is called so that filterClustersHelpJTextArea
        // is resized correctly within filterClustersHelpJScrollPane when the
        // filterClustersHelpJDialog is maximized
        FilterClustersHelpJDialog.classInstance.filterClustersHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
