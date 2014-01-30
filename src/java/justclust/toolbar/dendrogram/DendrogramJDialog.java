package justclust.toolbar.dendrogram;

import java.awt.Cursor;
import justclust.toolbar.heatmap.*;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.ImageIcon;

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
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.DialogSizesAndPositions;
import justclust.customcomponents.HelpButton;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class DendrogramJDialog extends JDialog {

    public static DendrogramJDialog classInstance;
    public JPanel jPanel;
    public HelpButton helpButton;
    public DendrogramJPanel dendrogramJPanel;
    public JScrollPane jScrollPane;
    DendrogramComponentListener dendrogramComponentListener;

    public DendrogramJDialog(JFrame parent) {

        super(parent, "Dendrogram");

        classInstance = this;

        JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        JustclustJFrame.classInstance.statusBarJLabel.setText("Loading dendrogram...");
        JustclustJFrame.classInstance.statusBarJLabel.repaint();

        // creating the dialog in the event dispatch thread allows the
        // statusBarJLabel to update before
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                DendrogramJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.dendrogramXCoordinate,
                        DialogSizesAndPositions.dendrogramYCoordinate,
                        DialogSizesAndPositions.dendrogramWidth,
                        DialogSizesAndPositions.dendrogramHeight);
                ImageIcon img = new ImageIcon("img/justclust_icon.png");
                setIconImage(img.getImage());

                jPanel = new JPanel();
                add(jPanel);
                jPanel.setLayout(null);

                helpButton = new HelpButton();
                DendrogramMouseListener dendrogramMouseListener = new DendrogramMouseListener();
                helpButton.addMouseListener(dendrogramMouseListener);
                jPanel.add(helpButton);

                // get the current Data instance for the following code to use
                int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
                Data data = Data.data.get(currentCustomGraphEditorIndex);
                String[] labels = new String[data.networkNodes.size()];
                for (int i = 0; i < data.networkNodes.size(); i++) {
                    labels[i] = data.networkNodes.get(i).label;
                }
                Dendrogram dendrogram = new Dendrogram(data.rootDendrogramClusters, labels, data.networkNodes.size());
                dendrogramJPanel = new DendrogramJPanel();
                dendrogramJPanel.set(dendrogram);
                jScrollPane = new JScrollPane(dendrogramJPanel);
                jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
                jPanel.add(jScrollPane);

                dendrogramComponentListener = new DendrogramComponentListener();
                addComponentListener(dendrogramComponentListener);

                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                JustclustJFrame.classInstance.statusBarJLabel.setText("Dendrogram loaded");
                JustclustJFrame.classInstance.statusBarJLabel.repaint();

                // the setVisible method is called to make the DendrogramJDialog appear
                setVisible(true);

            }
        });

    }
}
