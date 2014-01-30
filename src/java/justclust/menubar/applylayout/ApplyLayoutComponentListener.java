package justclust.menubar.applylayout;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.JustclustJFrame;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.TextFieldControl;

/**
 * This class has a method which rearranges the components of a
 * ApplyLayoutJDialog whenever the ApplyLayoutJDialog is resized.
 */
public class ApplyLayoutComponentListener implements
        ComponentListener {

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
     * This method rearranges the components of a ApplyLayoutJDialog whenever
     * the ApplyLayoutJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // pluginDetailsVerticalDisplacement is based on how many components are added to the
        // ApplyLayoutJDialog by plug-ins to configure and describe them.
        // if components have been added, this affects the vertical positioning
        // of all the components in the ApplyLayoutJDialog.
        double pluginDetailsVerticalDisplacement = 0;
        // a positive selected index for the visualisationLayoutJComboBox indicates that a
        // plug-in has been selected and therefore some components have been
        // added to the ApplyLayoutJDialog to discribe the plug-in
        if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0) {
            pluginDetailsVerticalDisplacement = 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10;
        }

        // progressBarVerticalDisplacement is based on whether the
        // applyLayoutJProgressBar has been added to the ApplyLayoutJDialog
        // because a layout is being applied.
        // if the applyLayoutJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ApplyLayoutJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the applyLayoutJProgressBar is the
        // applyLayoutDialogJPanel, then the applyLayoutJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar.getParent() == ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        ApplyLayoutJDialog.classInstance.applyLayoutHelpButton
                .setBounds(
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                16,
                16);
        ApplyLayoutJDialog.classInstance.visualisationLayoutJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        // the positions of the pluginDetailsJLabel, pluginDescriptionJLabel,
        // and pluginDescriptionJScrollPane are set here.
        // a positive item count for the visualisationLayoutJComboBox indicates that a
        // plug-in has been selected (by default or manually) and therefore some components have been
        // added to the ApplyLayoutJDialog to describe the plug-in.
        if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getItemCount() > 0) {
            ApplyLayoutJDialog.classInstance.pluginDetailsJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            ApplyLayoutJDialog.classInstance.pluginDescriptionJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            ApplyLayoutJDialog.classInstance.pluginDescriptionJScrollPane.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    60);
        }

        // the pluginConfigurationJComponents are laid out in order
        if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls != null) {
            for (int i = 0; i < ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size(); i++) {

                // CheckBoxControls, ComboBoxControls, and TextFieldControls have the same layout for
                // thier JComponents and so there components are laid out with the
                // same code
                if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof CheckBoxControl
                        || ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof ComboBoxControl
                        || ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof TextFieldControl) {
                    ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                }

                // the JComponents for FileSystemPathControls are laid out here
                if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl) {
                    ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                            25);
                    ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(2).setBounds(
                            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                            (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            25,
                            25);
                }

            }
        }

        ApplyLayoutJDialog.classInstance.applyLayoutJButton
                .setBounds(
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the applyLayoutJProgressBar is set here.
        // if the parent of the applyLayoutJProgressBar is the
        // applyLayoutDialogJPanel, then the applyLayoutJProgressBar has been
        // added.
        if (ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar.getParent() == ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
            ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getWidth() - (10 + 10),
                    25);
        }
        
        // the validate method is called so that arrow button on
        // visualisationLayoutJComboBox is repositioned correctly when the
        // ApplyLayoutJDialog is maximized or minimized
        ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.validate();

        // the repaint method is called so that the borders on the
        // applyLayoutDialogJPanel are updated properly
        ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.repaint();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
