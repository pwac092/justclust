package justclust.menubar.exportclustering;

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

public class ExportClusteringJPanel extends JPanel {

    // this method extends the super class' paint method with additional
    // graphics to display in the ExportClusteringJPanel
    public void paint(Graphics g) {

        // the paint method of the super class is called so that the parts of
        // the ExportClusteringJPanel which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        // the background is painted
        g.setColor(new Color(239, 239, 239));
        g.fillRect(0, 0, getWidth(), getHeight());

        // progressBarVerticalDisplacement is based on whether the
        // exportClusteringJProgressBar has been added to the ExportClusteringJDialog
        // because a clustering is being exported.
        // if the exportClusteringJProgressBar has been added, this affects
        // the vertical positioning of all the components in the
        // ExportClusteringJDialog.
        double progressBarVerticalDisplacement = 0;
        // if the parent of the exportClusteringJProgressBar is the
        // exportClusteringDialogJPanel, then the exportClusteringJProgressBar has been
        // added and it takes up 25 + 10 pixels vertically
        if (ExportClusteringJDialog.classInstance.exportClusteringJProgressBar.getParent() == this) {
            progressBarVerticalDisplacement = 25 + 10;
        }

        // the borders are painted
        g.setColor(new Color(191, 191, 191));
        g.fillRect(10,
                (int) Math.round((double) getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10)
                - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 10),
                1 + 10 + 25 + 10 + 25 + 10 + 1);
        g.setColor(new Color(247, 247, 247));
        g.fillRect(10 + 1,
                (int) Math.round((double) getHeight() * 1 / 2
                - 81.5 + (10 + 16 + 10 + 1)
                - progressBarVerticalDisplacement / 2),
                getWidth() - (10 + 1 + 1 + 10),
                10 + 25 + 10 + 25 + 10);

        // the components are painted over the borders
        paintComponents(g);

    }
}
