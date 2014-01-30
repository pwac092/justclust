package justclust.menubar.clusternetwork;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.TextFieldControl;

/**
 * This class has a method which rearranges the components of a
 * ClusterNetworkJDialog whenever the ClusterNetworkJDialog is resized.
 */
public class ClusterNetworkComponentListener implements
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
     * This method rearranges the components of a ClusterNetworkJDialog whenever
     * the ClusterNetworkJDialog is resized.
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {

        // pluginDetailsVerticalDisplacement is based on how many components are added to the
        // ClusterNetworkJDialog by plug-ins to configure and describe them.
        // if components have been added, this affects the vertical positioning
        // of all the components in the ClusterNetworkJDialog.
        double pluginDetailsVerticalDisplacement = 0;
        // a positive selected index for the clusteringAlgorithmJComboBox indicates that a
        // plug-in has been selected and therefore some components have been
        // added to the ClusterNetworkJDialog to discribe the plug-in
        if (ClusterNetworkJDialog.classInstance.clusteringAlgorithmJComboBox.getSelectedIndex() > 0) {
            pluginDetailsVerticalDisplacement = 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ClusterNetworkJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10;
        }

        // progressBarVerticalDisplacement is based on whether the
        // clusterNetworkJProgressBar has been added to the ClusterNetworkJDialog
        // because a network is being clustered.
        // if the clusterNetworkJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ClusterNetworkJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the clusterNetworkJProgressBar is the
        // clusterNetworkDialogJPanel, then the clusterNetworkJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ClusterNetworkJDialog.classInstance.clusterNetworkJProgressBar.getParent() == ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        ClusterNetworkJDialog.classInstance.clusterNetworkHelpButton
                .setBounds(
                ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                16,
                16);
        ClusterNetworkJDialog.classInstance.clusteringAlgorithmJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);
        ClusterNetworkJDialog.classInstance.clusteringAlgorithmJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        // the positions of the pluginDetailsJLabel, pluginDescriptionJLabel,
        // and pluginDescriptionJScrollPane are set here.
        // a positive item count for the clusteringAlgorithmJComboBox indicates that a
        // plug-in has been selected (by default or manually) and therefore some components have been
        // added to the ClusterNetworkJDialog to describe the plug-in.
        if (ClusterNetworkJDialog.classInstance.clusteringAlgorithmJComboBox.getItemCount() > 0) {
            ClusterNetworkJDialog.classInstance.pluginDetailsJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            ClusterNetworkJDialog.classInstance.pluginDescriptionJLabel.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    25);
            ClusterNetworkJDialog.classInstance.pluginDescriptionJScrollPane.setBounds(
                    10 + 1 + 10,
                    (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                    60);
        }

        // the pluginConfigurationJComponents are laid out in order
        if (ClusterNetworkJDialog.classInstance.pluginConfigurationControls != null) {
            for (int i = 0; i < ClusterNetworkJDialog.classInstance.pluginConfigurationControls.size(); i++) {

                // CheckBoxControls, ComboBoxControls, and TextFieldControls have the same layout for
                // thier JComponents and so there components are laid out with the
                // same code
                if (ClusterNetworkJDialog.classInstance.pluginConfigurationControls.get(i) instanceof CheckBoxControl
                        || ClusterNetworkJDialog.classInstance.pluginConfigurationControls.get(i) instanceof ComboBoxControl
                        || ClusterNetworkJDialog.classInstance.pluginConfigurationControls.get(i) instanceof TextFieldControl) {
                    ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                }

                // the JComponents for FileSystemPathControls are laid out here
                if (ClusterNetworkJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl) {
                    ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents.get(i).get(0).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                            25);
                    ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1).setBounds(
                            10 + 1 + 10,
                            (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                            25);
                    ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents.get(i).get(2).setBounds(
                            ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 1 + 10 + 25),
                            (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                            - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * i + 25 + 10)
                            - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                            25,
                            25);
                }

            }
        }

        ClusterNetworkJDialog.classInstance.clusterNetworkJButton
                .setBounds(
                ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 200),
                (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                200,
                25);

        // the positions of the clusterNetworkJProgressBar is set here.
        // if the parent of the clusterNetworkJProgressBar is the
        // clusterNetworkDialogJPanel, then the clusterNetworkJProgressBar has been
        // added.
        if (ClusterNetworkJDialog.classInstance.clusterNetworkJProgressBar.getParent() == ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel) {
            ClusterNetworkJDialog.classInstance.clusterNetworkJProgressBar
                    .setBounds(
                    10,
                    (int) Math.round((double) ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getHeight() * 1 / 2
                    - 81.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                    + pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.getWidth() - (10 + 10),
                    25);
        }

        // the validate method is called so that arrow button on
        // clusteringAlgorithmJComboBox is repositioned correctly when the
        // ClusterNetworkJDialog is maximized or minimized
        ClusterNetworkJDialog.classInstance.clusteringAlgorithmJComboBox.validate();

        // the repaint method is called so that the borders on the
        // clusterNetworkDialogJPanel are updated properly
        ClusterNetworkJDialog.classInstance.clusterNetworkDialogJPanel.repaint();

    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    @Override
    public void componentShown(ComponentEvent componentEvent) {
    }
}
