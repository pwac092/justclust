package justclust.toolbar.searchnetwork;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class SearchNetworkComponentListener implements
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
        
        DialogSizesAndPositions.searchNetworkXCoordinate = SearchNetworkJDialog.classInstance.getX();
        DialogSizesAndPositions.searchNetworkYCoordinate = SearchNetworkJDialog.classInstance.getY();
        
    }

    /**
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        SearchNetworkJDialog.classInstance.searchNetworkHelpButton
                .setBounds(
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10)),
                16,
                16);
        
        SearchNetworkJDialog.classInstance.searchJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        SearchNetworkJDialog.classInstance.searchJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        SearchNetworkJDialog.classInstance.searchJButton
                .setBounds(
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10)),
                200,
                25);

        SearchNetworkJDialog.classInstance.nodesJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        SearchNetworkJDialog.classInstance.nodesJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                130);

        SearchNetworkJDialog.classInstance.nodeOptionsJButton
                .setBounds(
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10)),
                200,
                25);

        SearchNetworkJDialog.classInstance.edgesJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        SearchNetworkJDialog.classInstance.edgesJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                130);

        SearchNetworkJDialog.classInstance.edgeOptionsJButton
                .setBounds(
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10)),
                200,
                25);

        SearchNetworkJDialog.classInstance.clustersJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        SearchNetworkJDialog.classInstance.clustersJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10)),
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                130);

        SearchNetworkJDialog.classInstance.clusterOptionsJButton
                .setBounds(
                SearchNetworkJDialog.classInstance.searchNetworkJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) SearchNetworkJDialog.classInstance.searchNetworkJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10)),
                200,
                25);

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
