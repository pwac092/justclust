package justclust.menubar.newnetworkfromfile;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.TextFieldControl;

/**
 * This class has a method which rearranges the components of a
 * NewNetworkFromFileJDialog whenever the NewNetworkFromFileJDialog is resized.
 */
public class NewNetworkFromFileComponentListener implements ComponentListener {

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    /**
     * This method rearranges the components of a NewNetworkFromFileJDialog
     * whenever the NewNetworkFromFileJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // pluginDetailsVerticalDisplacement is based on how many components are added to the
        // NewNetworkFromFileJDialog by plug-ins to configure and describe them.
        // if components have been added, this affects the vertical positioning
        // of all the components in the NewNetworkFromFileJDialog.
        double pluginDetailsVerticalDisplacement = 0;
        // a positive selected index for the fileParserJComboBox indicates that a
        // plug-in has been selected and therefore some components have been
        // added to the NewNetworkFromFileJDialog to discribe the plug-in
        if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {
            pluginDetailsVerticalDisplacement = 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10;
        }

        // progressBarVerticalDisplacement is based on whether the
        // createNetworkJProgressBar has been added to the NewNetworkFromFileJDialog
        // because a network is being created.
        // if the createNetworkJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // NewNetworkFromFileJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the createNetworkJProgressBar is the
        // newNetworkDialogJPanel, then the createNetworkJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar.getParent() == NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        NewNetworkFromFileJDialog.classInstance.newNetworkFromFileHelpButton
                .setBounds(
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                16,
                16);
        NewNetworkFromFileJDialog.classInstance.inputFileJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        NewNetworkFromFileJDialog.classInstance.inputFileJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);
        NewNetworkFromFileJDialog.classInstance.inputFileBrowseButton
                .setBounds(
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                25,
                25);
        NewNetworkFromFileJDialog.classInstance.fileTypeJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        // the positions of the pluginDetailsJLabel, pluginDescriptionJLabel,
        // and pluginDescriptionJScrollPane are set here.
        // a positive item count for the fileParserJComboBox indicates that a
        // plug-in has been selected (by default or manually) and therefore some components have been
        // added to the NewNetworkFromFileJDialog to describe the plug-in.
        if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getItemCount() > 0) {
            NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    60);
        }

        // the pluginConfigurationJComponents are laid out in order
        if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls != null) {
            for (int i = 0; i < NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size(); i++) {

                // CheckBoxControls, ComboBoxControls, and TextFieldControls have the same layout for
                // thier JComponents and so there components are laid out with the
                // same code
                if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof CheckBoxControl
                        || NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof ComboBoxControl
                        || NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof TextFieldControl) {
                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                            - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                            - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                }

                // the JComponents for FileSystemPathControls are laid out here
                if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl) {
                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                            - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                            - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                            25);
                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(2).setBounds(
                            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                            (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                            - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            25,
                            25);
                }

            }
        }

        NewNetworkFromFileJDialog.classInstance.createNetworkJButton.setBounds(
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the createNetworkJProgressBar is set here.
        // if the parent of the createNetworkJProgressBar is the
        // newNetworkDialogJPanel, then the createNetworkJProgressBar has been
        // added.
        if (NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar.getParent() == NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
            NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getWidth() - (10 + 10),
                    25);
        }

        // the validate method is called so that arrow button on
        // fileParserJComboBox is repositioned correctly when the
        // NewNetworkFromFileJDialog is maximized or minimized
        NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.validate();

        // the repaint method is called so that the borders on the
        // newNetworkDialogJPanel are updated properly
        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.repaint();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
