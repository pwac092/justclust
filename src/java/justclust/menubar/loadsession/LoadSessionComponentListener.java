package justclust.menubar.loadsession;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class LoadSessionComponentListener implements ComponentListener {

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // progressBarVerticalDisplacement is based on whether the
        // loadSessionJProgressBar has been added to the LoadSessionJDialog
        // because a session is being loaded.
        // if the loadSessionJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // LoadSessionJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the loadSessionJProgressBar is the
        // loadSessionDialogJPanel, then the loadSessionJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (LoadSessionJDialog.classInstance.loadSessionJProgressBar.getParent() == LoadSessionJDialog.classInstance.loadSessionDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        LoadSessionJDialog.classInstance.loadSessionHelpButton
                .setBounds(
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - progressBarVerticalDisplacement / 2),
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        LoadSessionJDialog.classInstance.fileNameJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        LoadSessionJDialog.classInstance.fileNameJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        LoadSessionJDialog.classInstance.browseButton
                .setBounds(
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                25,
                25);
        LoadSessionJDialog.classInstance.loadSessionJButton
                .setBounds(
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the loadSessionJProgressBar is set here.
        // if the parent of the loadSessionJProgressBar is the
        // loadSessionDialogJPanel, then the loadSessionJProgressBar has been
        // added.
        if (LoadSessionJDialog.classInstance.loadSessionJProgressBar.getParent() == LoadSessionJDialog.classInstance.loadSessionDialogJPanel) {
            LoadSessionJDialog.classInstance.loadSessionJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    - progressBarVerticalDisplacement / 2),
                    LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getWidth() - (10 + 10),
                    25);
        }

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}