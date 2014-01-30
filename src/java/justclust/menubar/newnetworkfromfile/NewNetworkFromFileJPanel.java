package justclust.menubar.newnetworkfromfile;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import justclust.datastructures.Data;
import justclust.customcomponents.BrowseButton;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class NewNetworkFromFileJPanel extends JPanel {

    // this method extends the super class' paint method with additional
    // graphics to display in the NewNetworkFromFileJPanel
    public void paint(Graphics g) {

        // the paint method of the super class is called so that the parts of
        // the NewNetworkFromFileJPanel which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        // the background is painted
        g.setColor(new Color(239, 239, 239));
        g.fillRect(0, 0, getWidth(), getHeight());

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
        if (NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar.getParent() == this) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        // the borders are painted
        
        g.setColor(new Color(191, 191, 191));
        g.fillRect(10,
                (int) Math.round((double) getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 10),
                1 + 10 + 25 + 10 + 25 + 10 + 1);
        g.setColor(new Color(247, 247, 247));
        g.fillRect(10 + 1,
                (int) Math.round((double) getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 1 + 1 + 10),
                10 + 25 + 10 + 25 + 10);
        
        g.setColor(new Color(191, 191, 191));
        g.fillRect(10,
                (int) Math.round((double) getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 10),
                1 + 10 + 25 + 10 + 25 + 10 + 1);
        g.setColor(new Color(247, 247, 247));
        g.fillRect(10 + 1,
                (int) Math.round((double) getHeight() * 1 / 2
                - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1)
                - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 1 + 1 + 10),
                10 + 25 + 10 + 25 + 10);
        
        if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {
            g.setColor(new Color(191, 191, 191));
            g.fillRect(10,
                    (int) Math.round((double) getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    getWidth() - (10 + 10),
                    1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1);
            g.setColor(new Color(247, 247, 247));
            g.fillRect(10 + 1,
                    (int) Math.round((double) getHeight() * 1 / 2
                    - 127.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1)
                    - pluginDetailsVerticalDisplacement / 2 - progressBarVerticalDisplacement / 2),
                    getWidth() - (10 + 1 + 1 + 10),
                    10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size());
        }

        // the components are painted over the borders
        paintComponents(g);

    }
}
