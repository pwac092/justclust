package justclust.menubar.savesession;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class SaveSessionComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
    }

    public void componentResized(ComponentEvent componentEvent) {

        // progressBarVerticalDisplacement is based on whether the
        // saveSessionJProgressBar has been added to the SaveSessionJDialog
        // because a session is being saved.
        // if the saveSessionJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // SaveSessionJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the saveSessionJProgressBar is the
        // saveSessionDialogJPanel, then the saveSessionJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (SaveSessionJDialog.classInstance.saveSessionJProgressBar.getParent() == SaveSessionJDialog.classInstance.saveSessionDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        SaveSessionJDialog.classInstance.saveSessionHelpButton
                .setBounds(
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - progressBarVerticalDisplacement / 2),
                16,
                16);
        SaveSessionJDialog.classInstance.fileNameJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        SaveSessionJDialog.classInstance.fileNameJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        SaveSessionJDialog.classInstance.browseButton
                .setBounds(
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - progressBarVerticalDisplacement / 2),
                25,
                25);
        SaveSessionJDialog.classInstance.saveSessionJButton
                .setBounds(
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the saveSessionJProgressBar is set here.
        // if the parent of the saveSessionJProgressBar is the
        // saveSessionDialogJPanel, then the saveSessionJProgressBar has been
        // added.
        if (SaveSessionJDialog.classInstance.saveSessionJProgressBar.getParent() == SaveSessionJDialog.classInstance.saveSessionDialogJPanel) {
            SaveSessionJDialog.classInstance.saveSessionJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    - progressBarVerticalDisplacement / 2),
                    SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getWidth() - (10 + 10),
                    25);
        }

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}