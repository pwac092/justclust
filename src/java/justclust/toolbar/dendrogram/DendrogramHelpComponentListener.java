package justclust.toolbar.dendrogram;

import justclust.toolbar.heatmap.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class DendrogramHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        DendrogramHelpJDialog.classInstance.jScrollPane
                .setBounds(
                10,
                10,
                DendrogramHelpJDialog.classInstance.jPanel.getWidth() - 20,
                DendrogramHelpJDialog.classInstance.jPanel.getHeight() - 20);

        // the validate method is called so that jTextArea
        // is resized correctly within jScrollPane when the
        // dendrogramHelpJDialog is maximized
        DendrogramHelpJDialog.classInstance.jScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
