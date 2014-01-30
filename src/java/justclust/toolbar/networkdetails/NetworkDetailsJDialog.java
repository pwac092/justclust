package justclust.toolbar.networkdetails;

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

public class NetworkDetailsJDialog extends JDialog {

    public static NetworkDetailsJDialog classInstance;
    public JPanel networkDetailsDialogJPanel;
    public HelpButton networkDetailsHelpButton;
    public JTextArea networkDetailsDialogJTextArea;
    public JScrollPane networkDetailsDialogJScrollPane;
    NetworkDetailsComponentListener networkDetailsComponentListener;
    public String details;

    public NetworkDetailsJDialog(JFrame parent) {

        super(parent, "Network Details");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        NetworkDetailsJDialog.classInstance.setBounds(
                DialogSizesAndPositions.networkDetailsXCoordinate,
                DialogSizesAndPositions.networkDetailsYCoordinate,
                DialogSizesAndPositions.networkDetailsWidth,
                DialogSizesAndPositions.networkDetailsHeight);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        networkDetailsDialogJPanel = new JPanel();
        add(networkDetailsDialogJPanel);
        networkDetailsDialogJPanel.setLayout(null);

        networkDetailsHelpButton = new HelpButton();
        NetworkDetailsMouseListener networkDetailsMouseListener = new NetworkDetailsMouseListener();
        networkDetailsHelpButton.addMouseListener(networkDetailsMouseListener);
        networkDetailsDialogJPanel.add(networkDetailsHelpButton);

        networkDetailsDialogJTextArea = new JTextArea();
        networkDetailsDialogJTextArea.setEditable(false);
        networkDetailsDialogJScrollPane = new JScrollPane(networkDetailsDialogJTextArea);
        NetworkDetailsChangeListener networkDetailsChangeListener = new NetworkDetailsChangeListener();
        networkDetailsDialogJScrollPane.getViewport().addChangeListener(networkDetailsChangeListener);
        networkDetailsDialogJPanel.add(networkDetailsDialogJScrollPane);
        updateTextArea();
        // this makes the scrollbar of the networkDetailsDialogJScrollPane
        // start where it was previously
        networkDetailsDialogJTextArea.setCaretPosition(DialogSizesAndPositions.networkDetailsCaretPositionToScrollTo);

        networkDetailsComponentListener = new NetworkDetailsComponentListener();
        addComponentListener(networkDetailsComponentListener);

        // the setVisible method is called to make the NetworkDetailsJDialog
        // appear
        setVisible(true);

    }

    public void updateTextArea() {

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // This code populates networkDetailsDialogJTextArea with details about
        // the current network.
        // the first if statement executes when the current network hasn't been
        // clustered.
        // the second if statement executes when the current network has been
        // clustered.
        if (data.networkClusters == null) {
            Double smallestEdgeWeight = null;
            Double largestEdgeWeight = null;
            if (data.networkEdges.size() >= 1) {
                smallestEdgeWeight = data.networkEdges.get(0).weight;
                largestEdgeWeight = data.networkEdges.get(0).weight;
            }
            double totalEdgesWeight = 0;
            for (Edge edge : data.networkEdges) {
                if (smallestEdgeWeight > edge.weight) {
                    smallestEdgeWeight = edge.weight;
                }
                if (largestEdgeWeight < edge.weight) {
                    largestEdgeWeight = edge.weight;
                }
                totalEdgesWeight += edge.weight;
            }
            details =
                    "File" + '\n'
                    + data.fileName + '\n'
                    + '\n'
                    + "File parser" + '\n'
                    + data.fileParser + '\n'
                    + '\n'
                    + "Time taken to create network" + '\n'
                    + String.valueOf(data.timeTakenToCreateNetwork) + " milliseconds" + '\n'
                    + '\n'
                    + "Nodes" + '\n'
                    + String.valueOf(data.networkNodes.size()) + '\n'
                    + '\n'
                    + "Edges" + '\n'
                    + String.valueOf(data.networkEdges.size()) + '\n'
                    + '\n'
                    + "Average edges per node" + '\n'
                    + String.valueOf((double) data.networkEdges.size() / data.networkNodes.size()) + '\n'
                    + '\n'
                    + "Smallest edge weight" + '\n'
                    + String.valueOf(smallestEdgeWeight) + '\n'
                    + '\n'
                    + "Largest edge weight" + '\n'
                    + String.valueOf(largestEdgeWeight) + '\n'
                    + '\n'
                    + "Average edge weight" + '\n'
                    + String.valueOf(totalEdgesWeight / data.networkEdges.size());
        } else {
            Double smallestEdgeWeight = null;
            Double largestEdgeWeight = null;
            if (data.networkEdges.size() >= 1) {
                smallestEdgeWeight = data.networkEdges.get(0).weight;
                largestEdgeWeight = data.networkEdges.get(0).weight;
            }
            double totalEdgesWeight = 0;
            for (Edge edge : data.networkEdges) {
                if (smallestEdgeWeight > edge.weight) {
                    smallestEdgeWeight = edge.weight;
                }
                if (largestEdgeWeight < edge.weight) {
                    largestEdgeWeight = edge.weight;
                }
                totalEdgesWeight += edge.weight;
            }
            details =
                    "File" + '\n'
                    + data.fileName + '\n'
                    + '\n'
                    + "File parser" + '\n'
                    + data.fileParser + '\n'
                    + '\n'
                    + "Time taken to create network" + '\n'
                    + String.valueOf(data.timeTakenToCreateNetwork) + " milliseconds" + '\n'
                    + '\n'
                    + "Nodes" + '\n'
                    + String.valueOf(data.networkNodes.size()) + '\n'
                    + '\n'
                    + "Edges" + '\n'
                    + String.valueOf(data.networkEdges.size()) + '\n'
                    + '\n'
                    + "Average edges per node" + '\n'
                    + String.valueOf((double) data.networkEdges.size() / data.networkNodes.size()) + '\n'
                    + '\n'
                    + "Smallest edge weight" + '\n'
                    + String.valueOf(smallestEdgeWeight) + '\n'
                    + '\n'
                    + "Largest edge weight" + '\n'
                    + String.valueOf(largestEdgeWeight) + '\n'
                    + '\n'
                    + "Average edge weight" + '\n'
                    + String.valueOf(totalEdgesWeight / data.networkEdges.size()) + '\n'
                    + '\n'
                    + "Clustering algorithm" + '\n'
                    + data.clusteringAlgorithm + '\n'
                    + '\n'
                    + "Time taken to create clustering" + '\n'
                    + String.valueOf(data.timeTakenToClusterNetwork) + " milliseconds" + '\n'
                    + '\n'
                    + "Clusters" + '\n'
                    + String.valueOf(data.networkClusters.size()) + '\n'
                    + '\n'
                    + "Average nodes per cluster" + '\n'
                    + String.valueOf((double) data.networkNodes.size() / data.networkClusters.size()) + '\n'
                    + '\n'
                    + "Average edges per cluster" + '\n'
                    + String.valueOf((double) data.networkEdges.size() / data.networkClusters.size());
        }

        networkDetailsDialogJTextArea.setText(details);

    }
}
