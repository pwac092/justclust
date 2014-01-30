package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MicroarrayHeatMapHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        MicroarrayHeatMapHelpJDialog.classInstance.jScrollPane
                .setBounds(
                10,
                10,
                MicroarrayHeatMapHelpJDialog.classInstance.jPanel.getWidth() - 20,
                MicroarrayHeatMapHelpJDialog.classInstance.jPanel.getHeight() - 20);

        // the validate method is called so that jTextArea
        // is resized correctly within jScrollPane when the
        // heatMapHelpJDialog is maximized
        MicroarrayHeatMapHelpJDialog.classInstance.jScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
