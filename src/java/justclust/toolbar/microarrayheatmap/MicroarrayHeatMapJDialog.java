package justclust.toolbar.microarrayheatmap;

import java.awt.Color;
import justclust.toolbar.heatmap.*;
import java.awt.Cursor;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import justclust.datastructures.Node;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class MicroarrayHeatMapJDialog extends JDialog {

    public static MicroarrayHeatMapJDialog classInstance;
    public JPanel jPanel;
    public HelpButton helpButton;
    public JLabel includeLabelsJLabel;
    public JCheckBox includeLabelsJCheckBox;
    public JLabel microarrayValueJLabel;
    public JTextField microarrayValueJTextField;
    public MicroarrayHeatMapJPanel microarrayHeatMapJPanel;
    public JScrollPane jScrollPane;
    MicroarrayHeatMapComponentListener microarrayHeatMapComponentListener;

    public MicroarrayHeatMapJDialog(JFrame parent) {

        super(parent, "Heat Map");

        classInstance = this;

        JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        JustclustJFrame.classInstance.statusBarJLabel.setText("Loading heat map...");
        JustclustJFrame.classInstance.statusBarJLabel.repaint();

        // creating the dialog in the event dispatch thread allows the
        // statusBarJLabel to update before
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                MicroarrayHeatMapJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.microarrayHeatMapXCoordinate,
                        DialogSizesAndPositions.microarrayHeatMapYCoordinate,
                        DialogSizesAndPositions.microarrayHeatMapWidth,
                        DialogSizesAndPositions.microarrayHeatMapHeight);
                ImageIcon img = new ImageIcon("img/justclust_icon.png");
                setIconImage(img.getImage());

                jPanel = new JPanel();
                add(jPanel);
                jPanel.setLayout(null);

                helpButton = new HelpButton();
                MicroarrayHeatMapMouseListener heatMapMouseListener = new MicroarrayHeatMapMouseListener();
                helpButton.addMouseListener(heatMapMouseListener);
                jPanel.add(helpButton);

                includeLabelsJLabel = new JLabel("Include Labels:");
                jPanel.add(includeLabelsJLabel);

                includeLabelsJCheckBox = new JCheckBox();
                includeLabelsJCheckBox.setSelected(DialogSizesAndPositions.microarrayHeatMapIncludeLabels);
                includeLabelsJCheckBox.addActionListener(new MicroarrayHeatMapActionListener());
                jPanel.add(includeLabelsJCheckBox);

                microarrayValueJLabel = new JLabel("Microarray Value:");
                jPanel.add(microarrayValueJLabel);

                microarrayValueJTextField = new JTextField();
                microarrayValueJTextField.setEnabled(false);
                microarrayValueJTextField.setDisabledTextColor(Color.BLACK);
                jPanel.add(microarrayValueJTextField);

                // get the current Data instance for the following code to use
                int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
                Data data = Data.data.get(currentCustomGraphEditorIndex);
                MicroarrayHeatMapMatrix microarrayHeatMapMatrix;
                String[] labels;
                if (data.networkClusters == null) {

                    double[][] matrix = new double[data.networkNodes.size()][data.microarrayHeaders.size()];
                    for (int i = 0; i < data.networkNodes.size(); i++) {
                        for (int j = 0; j < data.microarrayHeaders.size(); j++) {
                            matrix[i][j] = data.networkNodes.get(i).microarrayValues.get(j);
                        }
                    }
                    microarrayHeatMapMatrix = new MicroarrayHeatMapMatrix(matrix);

                    labels = new String[data.networkNodes.size()];
                    for (int i = 0; i < data.networkNodes.size(); i++) {
                        labels[i] = data.networkNodes.get(i).label;
                    }

                    microarrayHeatMapJPanel = new MicroarrayHeatMapJPanel(microarrayHeatMapMatrix, labels, data.microarrayHeaders, null);

                } else {

                    ArrayList<Node> nodes = new ArrayList<Node>();
                    for (Cluster cluster : data.networkClusters) {
                        nodes.addAll(cluster.nodes);
                    }
                    double[][] matrix = new double[nodes.size()][data.microarrayHeaders.size()];
                    for (int i = 0; i < nodes.size(); i++) {
                        for (int j = 0; j < data.microarrayHeaders.size(); j++) {
                            matrix[i][j] = nodes.get(i).microarrayValues.get(j);
                        }
                    }
                    microarrayHeatMapMatrix = new MicroarrayHeatMapMatrix(matrix);

                    labels = new String[nodes.size()];
                    for (int i = 0; i < nodes.size(); i++) {
                        labels[i] = nodes.get(i).label;
                    }
                    
                    ArrayList<Integer> nodeAmountPerCluster = new ArrayList<Integer>();
                    for (Cluster cluster : data.networkClusters) {
                        nodeAmountPerCluster.add(cluster.nodes.size());
                    }

                    microarrayHeatMapJPanel = new MicroarrayHeatMapJPanel(microarrayHeatMapMatrix, labels, data.microarrayHeaders, nodeAmountPerCluster);

                }
                microarrayHeatMapJPanel.addMouseMotionListener(new MicroarrayHeatMapMouseMotionListener());
                jScrollPane = new JScrollPane(microarrayHeatMapJPanel);
                jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
                jPanel.add(jScrollPane);

                microarrayHeatMapComponentListener = new MicroarrayHeatMapComponentListener();
                addComponentListener(microarrayHeatMapComponentListener);

                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                JustclustJFrame.classInstance.statusBarJLabel.setText("Heat map loaded");
                JustclustJFrame.classInstance.statusBarJLabel.repaint();

                // the setVisible method is called to make the MicroarrayHeatMapJDialog appear
                setVisible(true);

            }
        });

    }
}
