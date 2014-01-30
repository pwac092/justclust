package justclust.toolbar.heatmap;

import java.awt.Color;
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

public class HeatMapJDialog extends JDialog {

    public static HeatMapJDialog classInstance;
    public JPanel jPanel;
    public HelpButton helpButton;
    public JLabel includeLabelsJLabel;
    public JCheckBox includeLabelsJCheckBox;
    public JLabel edgeWeightJLabel;
    public JTextField edgeWeightJTextField;
    public HeatMapJPanel heatMapJPanel;
    public JScrollPane jScrollPane;
    HeatMapComponentListener heatMapComponentListener;

    public HeatMapJDialog(JFrame parent) {

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
                HeatMapJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.heatMapXCoordinate,
                        DialogSizesAndPositions.heatMapYCoordinate,
                        DialogSizesAndPositions.heatMapWidth,
                        DialogSizesAndPositions.heatMapHeight);
                ImageIcon img = new ImageIcon("img/justclust_icon.png");
                setIconImage(img.getImage());

                jPanel = new JPanel();
                add(jPanel);
                jPanel.setLayout(null);

                helpButton = new HelpButton();
                HeatMapMouseListener heatMapMouseListener = new HeatMapMouseListener();
                helpButton.addMouseListener(heatMapMouseListener);
                jPanel.add(helpButton);

                includeLabelsJLabel = new JLabel("Include Labels:");
                jPanel.add(includeLabelsJLabel);

                includeLabelsJCheckBox = new JCheckBox();
                includeLabelsJCheckBox.setSelected(DialogSizesAndPositions.heatMapIncludeLabels);
                includeLabelsJCheckBox.addActionListener(new HeatMapActionListener());
                jPanel.add(includeLabelsJCheckBox);

                edgeWeightJLabel = new JLabel("Edge Weight:");
                jPanel.add(edgeWeightJLabel);

                edgeWeightJTextField = new JTextField();
                edgeWeightJTextField.setEnabled(false);
                edgeWeightJTextField.setDisabledTextColor(Color.BLACK);
                jPanel.add(edgeWeightJTextField);

                // get the current Data instance for the following code to use
                int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
                Data data = Data.data.get(currentCustomGraphEditorIndex);
                HeatMapMatrix heatMapMatrix;
                String[] labels;
                if (data.networkClusters == null) {

                    double[][] matrix = new double[data.networkNodes.size()][data.networkNodes.size()];
                    double maxEdgeWeight = 0;
                    for (Edge edge : data.networkEdges) {
                        maxEdgeWeight = Math.max(maxEdgeWeight, edge.weight);
                    }
                    for (int i = 0; i < data.networkNodes.size(); i++) {
                        for (int j = i; j < data.networkNodes.size(); j++) {
                            matrix[i][j] = 0;
                            matrix[j][i] = 0;
                            if (i == j) {
                                matrix[i][j] = 1;
                            }
                            Node node1 = data.networkNodes.get(i);
                            Node node2 = data.networkNodes.get(j);
                            for (Edge edge : node1.edges) {
                                if (edge.node1 == node1 && edge.node2 == node2
                                        || edge.node1 == node2 && edge.node2 == node1) {
                                    matrix[i][j] = edge.weight / maxEdgeWeight;
                                    matrix[j][i] = edge.weight / maxEdgeWeight;
                                    break;
                                }
                            }
                        }
                    }
                    heatMapMatrix = new HeatMapMatrix(matrix);

                    labels = new String[data.networkNodes.size()];
                    for (int i = 0; i < data.networkNodes.size(); i++) {
                        labels[i] = data.networkNodes.get(i).label;
                    }

                    heatMapJPanel = new HeatMapJPanel(heatMapMatrix, labels, null);

                } else {

                    ArrayList<Node> nodes = new ArrayList<Node>();
                    for (Cluster cluster : data.networkClusters) {
                        nodes.addAll(cluster.nodes);
                    }
                    double[][] matrix = new double[nodes.size()][nodes.size()];
                    double maxEdgeWeight = 0;
                    // the first node in the otherVersions ArrayList is always a
                    // node in the original network obtained from parsing the
                    // data because the nodes from the original network are
                    // added to the otherVersions ArrayList first.
                    // we need a node from the original network in order to get
                    // the edge weights from the original network, which may
                    // have been removed from the current network (a
                    // clustering).
                    for (Edge edge : data.networkNodes.get(0).otherVersions.get(0).data.networkEdges) {
                        maxEdgeWeight = Math.max(maxEdgeWeight, edge.weight);
                    }
                    for (int i = 0; i < nodes.size(); i++) {
                        for (int j = i; j < nodes.size(); j++) {
                            matrix[i][j] = 0;
                            matrix[j][i] = 0;
                            if (i == j) {
                                matrix[i][j] = 1;
                            }
                            ArrayList<Node> networkNodes = nodes.get(i).otherVersions.get(0).data.networkNodes;
                            int nodeIndex1 = data.networkNodes.indexOf(nodes.get(i));
                            int nodeIndex2 = data.networkNodes.indexOf(nodes.get(j));
                            for (Edge edge : nodes.get(i).otherVersions.get(0).edges) {
                                int nodeIndex3 = networkNodes.indexOf(edge.node1);
                                int nodeIndex4 = networkNodes.indexOf(edge.node2);
                                if (nodeIndex3 == nodeIndex1
                                        && nodeIndex4 == nodeIndex2
                                        || nodeIndex3 == nodeIndex2
                                        && nodeIndex4 == nodeIndex1) {
                                    matrix[i][j] = edge.weight / maxEdgeWeight;
                                    matrix[j][i] = edge.weight / maxEdgeWeight;
                                    break;
                                }
                            }
                        }
                    }
                    heatMapMatrix = new HeatMapMatrix(matrix);

                    labels = new String[nodes.size()];
                    for (int i = 0; i < nodes.size(); i++) {
                        labels[i] = nodes.get(i).label;
                    }

                    ArrayList<Integer> nodeAmountPerCluster = new ArrayList<Integer>();
                    for (Cluster cluster : data.networkClusters) {
                        nodeAmountPerCluster.add(cluster.nodes.size());
                    }

                    heatMapJPanel = new HeatMapJPanel(heatMapMatrix, labels, nodeAmountPerCluster);

                }
                heatMapJPanel.addMouseMotionListener(new HeatMapMouseMotionListener());
                jScrollPane = new JScrollPane(heatMapJPanel);
                jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
                jPanel.add(jScrollPane);

                heatMapComponentListener = new HeatMapComponentListener();
                addComponentListener(heatMapComponentListener);

                JustclustJFrame.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                JustclustJFrame.classInstance.statusBarJLabel.setText("Heat map loaded");
                JustclustJFrame.classInstance.statusBarJLabel.repaint();

                // the setVisible method is called to make the HeatMapJDialog appear
                setVisible(true);

            }
        });

    }
}
