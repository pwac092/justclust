package justclust.toolbar.networkclusters;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkClustersColourComponentListener implements
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
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        NetworkClustersColourJDialog.classInstance.jColorChooser
                .setBounds(
                10,
                10,
                NetworkClustersColourJDialog.classInstance.jPanel.getWidth() - 20,
                NetworkClustersColourJDialog.classInstance.jPanel.getHeight() - 55);

        NetworkClustersColourJDialog.classInstance.okJButton
                .setBounds(
                (int) Math.round((double) NetworkClustersColourJDialog.classInstance.jPanel.getWidth() / 2 - 160),
                NetworkClustersColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkClustersColourJDialog.classInstance.cancelJButton
                .setBounds(
                (int) Math.round((double) NetworkClustersColourJDialog.classInstance.jPanel.getWidth() / 2 - 50),
                NetworkClustersColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkClustersColourJDialog.classInstance.randomJButton
                .setBounds(
                (int) Math.round((double) NetworkClustersColourJDialog.classInstance.jPanel.getWidth() / 2 + 60),
                NetworkClustersColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        // if the validate method is not called, jColorChooser does not display
        // when the NetworkClustersColourJDialog is created and shown for unknown
        // reasons
        NetworkClustersColourJDialog.classInstance.jColorChooser.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
