/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.filterclusters;

import edu.umd.cs.piccolo.PNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.JustclustJFrame;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesTableModel;

/**
 *
 * @author wuaz008
 */
public class FilterClustersActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        // cancel the filtering if there are any errors
        try {
            
            Integer.parseInt(FilterClustersJDialog.classInstance.showLargestClustersJTextField.getText());
            Integer.parseInt(FilterClustersJDialog.classInstance.hideSmallestClustersJTextField.getText());
            Integer.parseInt(FilterClustersJDialog.classInstance.hideClustersAboveNodeAmountJTextField.getText());
            Integer.parseInt(FilterClustersJDialog.classInstance.hideClustersBelowNodeAmountJTextField.getText());
            Double.parseDouble(FilterClustersJDialog.classInstance.hideClustersBelowDensityThresholdJTextField.getText());
            
        } catch (NumberFormatException numberFormatException) {

            JOptionPane.showMessageDialog(FilterClustersJDialog.classInstance, "Filtering could not be completed due to error");

            return;

        }

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // set all Nodes to be invisible.
        // they will be shown if necessary by the code later.
        for (Node node : data.networkNodes) {
            node.visible = false;
            for (Node otherVersion : node.otherVersions) {
                otherVersion.visible = false;
            }
        }

        // set all Edges to be invisible
        for (Edge edge : data.networkEdges) {
            edge.visible = false;
            for (Edge otherVersion : edge.otherVersions) {
                otherVersion.visible = false;
            }
        }

        // hide all of the Nodes in any clusters which should be made invisible.
        for (Cluster cluster : data.networkClusters) {

            boolean visible = true;

            if (data.networkClusters.indexOf(cluster) + 1 > Integer.parseInt(FilterClustersJDialog.classInstance.showLargestClustersJTextField.getText())) {
                visible = false;
            }

            if (cluster.nodes.size() > Integer.parseInt(FilterClustersJDialog.classInstance.hideClustersAboveNodeAmountJTextField.getText())) {
                visible = false;
            }

            if (cluster.nodes.size() < Integer.parseInt(FilterClustersJDialog.classInstance.hideClustersBelowNodeAmountJTextField.getText())) {
                visible = false;
            }

            ArrayList<Edge> clusterEdges = new ArrayList<Edge>();
            for (Node node : cluster.nodes) {
                LOOP:
                for (Edge edge : node.edges) {
                    for (Edge clusterEdge : clusterEdges) {
                        if (clusterEdge.node1 == edge.node1 && clusterEdge.node2 == edge.node2
                                || clusterEdge.node1 == edge.node2 && clusterEdge.node2 == edge.node1) {
                            continue LOOP;
                        }
                    }
                    if (cluster.nodes.contains(edge.node1) && cluster.nodes.contains(edge.node2)) {
                        clusterEdges.add(edge);
                    }
                }
            }
            double clusterDensity;
            if (cluster.nodes.size() == 1) {
                clusterDensity = 1;
            } else {
                clusterDensity = (double) clusterEdges.size() / (cluster.nodes.size() * (cluster.nodes.size() - 1) / 2);
            }
            if (clusterDensity < Double.parseDouble(FilterClustersJDialog.classInstance.hideClustersBelowDensityThresholdJTextField.getText())) {
                visible = false;
            }

            if (visible) {
                for (Node node : cluster.nodes) {
                    node.visible = true;
                    for (Edge edge : node.edges) {
                        if (edge.node1.cluster == edge.node2.cluster) {
                            edge.visible = true;
                            for (Edge otherVersion : edge.otherVersions) {
                                otherVersion.visible = true;
                            }
                        }
                    }
                    for (Node otherVersion : node.otherVersions) {
                        otherVersion.visible = true;
                    }
                }
            }

        }

        // unselect all nodes so that any invisible nodes cannot be moved
        CustomGraphEditor currentCustomGraphEditor = JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex);
        for (int i = 1; i < currentCustomGraphEditor.nodeLayer.getAllNodes().size(); i++) {
            PNode pNode = ((ArrayList<PNode>) currentCustomGraphEditor.nodeLayer.getAllNodes()).get(i);
            currentCustomGraphEditor.customPSelectionEventHandler.unselect(pNode);
        }

        // update the appearance of all customGraphEditors.
        // all customGraphEditors are included incase a change in a graph, other than the
        // current graph, has been made.
        for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
            customGraphEditor.updateGraphVisibility();
        }

        // update any dialogs which are open
        if (NetworkNodesJDialog.classInstance != null) {
            ((NetworkNodesTableModel) NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getModel()).updateTable();
            NetworkNodesJDialog.classInstance.networkNodesDialogJTable.repaint();
        }
        if (NetworkEdgesJDialog.classInstance != null) {
            ((NetworkEdgesTableModel) NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.getModel()).updateTable();
            NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.repaint();
        }
        if (NetworkClustersJDialog.classInstance != null && data.networkClusters != null) {
            ((NetworkClustersTableModel) NetworkClustersJDialog.classInstance.networkClustersDialogJTable.getModel()).updateTable();
            NetworkClustersJDialog.classInstance.networkClustersDialogJTable.repaint();
        }
        if (HeatMapJDialog.classInstance != null) {
            HeatMapJDialog.classInstance.dispose();
        }
        if (DendrogramJDialog.classInstance != null) {
            DendrogramJDialog.classInstance.dispose();
        }

    }
}
