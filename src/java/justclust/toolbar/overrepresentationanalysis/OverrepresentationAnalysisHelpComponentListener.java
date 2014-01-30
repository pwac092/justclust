package justclust.toolbar.overrepresentationanalysis;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class OverrepresentationAnalysisHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        OverrepresentationAnalysisHelpJDialog.classInstance.overrepresentationAnalysisHelpJScrollPane
                .setBounds(
                10,
                10,
                OverrepresentationAnalysisHelpJDialog.classInstance.overrepresentationAnalysisHelpJPanel.getWidth() - 20,
                OverrepresentationAnalysisHelpJDialog.classInstance.overrepresentationAnalysisHelpJPanel.getHeight() - 20);

        // the validate method is called so that overrepresentationAnalysisHelpJTextArea
        // is resized correctly within overrepresentationAnalysisHelpJScrollPane when the
        // overrepresentationAnalysisHelpJDialog is maximized
        OverrepresentationAnalysisHelpJDialog.classInstance.overrepresentationAnalysisHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
