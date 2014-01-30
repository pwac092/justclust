package justclust.toolbar.networkedges;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NetworkEdgesColourComponentListener implements
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

        NetworkEdgesColourJDialog.classInstance.jColorChooser
                .setBounds(
                10,
                10,
                NetworkEdgesColourJDialog.classInstance.jPanel.getWidth() - 20,
                NetworkEdgesColourJDialog.classInstance.jPanel.getHeight() - 55);

        NetworkEdgesColourJDialog.classInstance.okJButton
                .setBounds(
                (int) Math.round((double) NetworkEdgesColourJDialog.classInstance.jPanel.getWidth() / 2 - 160),
                NetworkEdgesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkEdgesColourJDialog.classInstance.cancelJButton
                .setBounds(
                (int) Math.round((double) NetworkEdgesColourJDialog.classInstance.jPanel.getWidth() / 2 - 50),
                NetworkEdgesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        NetworkEdgesColourJDialog.classInstance.randomJButton
                .setBounds(
                (int) Math.round((double) NetworkEdgesColourJDialog.classInstance.jPanel.getWidth() / 2 + 60),
                NetworkEdgesColourJDialog.classInstance.jPanel.getHeight() - 35,
                100,
                25);

        // if the validate method is not called, jColorChooser does not display
        // when the NetworkEdgesColourJDialog is created and shown for unknown
        // reasons
        NetworkEdgesColourJDialog.classInstance.jColorChooser.validate();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
