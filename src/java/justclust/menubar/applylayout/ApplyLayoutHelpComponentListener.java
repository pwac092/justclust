package justclust.menubar.applylayout;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ApplyLayoutHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        ApplyLayoutHelpJDialog.classInstance.applyLayoutHelpJScrollPane
                .setBounds(
                10,
                10,
                ApplyLayoutHelpJDialog.classInstance.applyLayoutHelpJPanel.getWidth() - 20,
                ApplyLayoutHelpJDialog.classInstance.applyLayoutHelpJPanel.getHeight() - 20);

        // the validate method is called so that applyLayoutHelpJTextArea
        // is resized correctly within applyLayoutHelpJScrollPane when the
        // ApplyLayoutHelpJDialog is maximized
        ApplyLayoutHelpJDialog.classInstance.applyLayoutHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
