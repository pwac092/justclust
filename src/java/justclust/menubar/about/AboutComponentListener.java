package justclust.menubar.about;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class AboutComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        AboutJDialog.classInstance.aboutJTextArea
                .setBounds(
                10,
                220,
                AboutJDialog.classInstance.aboutJPanel.getWidth() - 20,
                AboutJDialog.classInstance.aboutJPanel.getHeight() - (10 + 200 + 10 + 10));

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
