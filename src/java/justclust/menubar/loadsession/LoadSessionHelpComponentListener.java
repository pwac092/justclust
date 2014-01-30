package justclust.menubar.loadsession;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class LoadSessionHelpComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        LoadSessionHelpJDialog.classInstance.loadSessionHelpJScrollPane
                .setBounds(
                10,
                10,
                LoadSessionHelpJDialog.classInstance.loadSessionHelpJPanel.getWidth() - 20,
                LoadSessionHelpJDialog.classInstance.loadSessionHelpJPanel.getHeight() - 20);

        // the validate method is called so that loadSessionHelpJTextArea
        // is resized correctly within loadSessionHelpJScrollPane when the
        // LoadSessionHelpJDialog is maximized
        LoadSessionHelpJDialog.classInstance.loadSessionHelpJScrollPane.validate();

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}