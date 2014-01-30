package justclust.toolbar.heatmap;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class HeatMapHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        HeatMapHelpJDialog.classInstance.jScrollPane
                .setBounds(
                10,
                10,
                HeatMapHelpJDialog.classInstance.jPanel.getWidth() - 20,
                HeatMapHelpJDialog.classInstance.jPanel.getHeight() - 20);

        // the validate method is called so that jTextArea
        // is resized correctly within jScrollPane when the
        // heatMapHelpJDialog is maximized
        HeatMapHelpJDialog.classInstance.jScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
