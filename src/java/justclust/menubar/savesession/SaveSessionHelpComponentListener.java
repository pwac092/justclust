package justclust.menubar.savesession;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class SaveSessionHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        SaveSessionHelpJDialog.classInstance.saveSessionHelpJScrollPane
                .setBounds(
                10,
                10,
                SaveSessionHelpJDialog.classInstance.saveSessionHelpJPanel.getWidth() - 20,
                SaveSessionHelpJDialog.classInstance.saveSessionHelpJPanel.getHeight() - 20);

        // the validate method is called so that saveSessionHelpJTextArea
        // is resized correctly within saveSessionHelpJScrollPane when the
        // saveSessionHelpJDialog is maximized
        SaveSessionHelpJDialog.classInstance.saveSessionHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}