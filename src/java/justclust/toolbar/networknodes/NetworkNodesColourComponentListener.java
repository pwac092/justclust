package justclust.toolbar.networknodes;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkNodesColourComponentListener implements
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

        NetworkNodesColourJDialog.classInstance.jColorChooser
                .setBounds(
                10,
                10,
                NetworkNodesColourJDialog.classInstance.jPanel.getWidth() - 20,
                NetworkNodesColourJDialog.classInstance.jPanel.getHeight() - 55);

        NetworkNodesColourJDialog.classInstance.okJButton
                .setBounds(
                (int) Math.round((double) NetworkNodesColourJDialog.classInstance.jPanel.getWidth() / 2 - 160),
                NetworkNodesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkNodesColourJDialog.classInstance.cancelJButton
                .setBounds(
                (int) Math.round((double) NetworkNodesColourJDialog.classInstance.jPanel.getWidth() / 2 - 50),
                NetworkNodesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkNodesColourJDialog.classInstance.randomJButton
                .setBounds(
                (int) Math.round((double) NetworkNodesColourJDialog.classInstance.jPanel.getWidth() / 2 + 60),
                NetworkNodesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        // if the validate method is not called, jColorChooser does not display
        // when the NetworkNodesColourJDialog is created and shown for unknown
        // reasons
        NetworkNodesColourJDialog.classInstance.jColorChooser.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
