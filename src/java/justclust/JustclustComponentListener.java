package justclust;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;

/**
 * This class has a method which rearranges the components of a JustclustJFrame
 * whenever the JustclustJFrame is resized.
 */
public class JustclustComponentListener implements ComponentListener {

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
     * This method rearranges the components of a JustclustJFrame whenever the
     * JustclustJFrame is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // the CustomBirdsEyeView is no longer used
//        JustclustJFrame.classInstance.customBirdsEyeView
//                .setBounds(
//                10,
//                JustclustJFrame.classInstance.justclustJPanel.getHeight() - 25 - 200 + 10,
//                200 - (10 + 10),
//                200 - (10 + 10));

        JustclustJFrame.classInstance.justclustJTabbedPane
                .setBounds(
                46,
                0,
                (int) Math.round((double) JustclustJFrame.classInstance.justclustJPanel
                .getWidth() * 4 / 4)
                - 46,
                JustclustJFrame.classInstance.justclustJPanel
                .getHeight() - 25);

        JustclustJFrame.classInstance.statusBarJLabel
                .setBounds(
                (int) Math.round((double) JustclustJFrame.classInstance.justclustJPanel
                .getWidth() * 0 / 4),
                JustclustJFrame.classInstance.justclustJPanel
                .getHeight() - 25,
                (int) Math.round((double) JustclustJFrame.classInstance.justclustJPanel
                .getWidth() * 4 / 4)
                - (int) Math
                .round((double) JustclustJFrame.classInstance.justclustJPanel
                .getWidth() * 0 / 4), 25);

        // the minimum size ensures that the components are not squashed
        // together
        JustclustJFrame.classInstance.setMinimumSize(new Dimension(
                JustclustJFrame.classInstance.getInsets().left + 3 + 40 + 3 + JustclustJFrame.classInstance.getInsets().right,
                JustclustJFrame.classInstance.getInsets().top + JustclustJFrame.classInstance.justclustJMenuBar.getHeight() + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 10 + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 40 + 3 + 10 + 3 + 40 + 3 + JustclustJFrame.classInstance.statusBarJLabel.getHeight() + JustclustJFrame.classInstance.getInsets().bottom));

        JustclustJFrame.classInstance.setVisible(true);

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
