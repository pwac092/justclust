package justclust.menubar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import justclust.datastructures.Cluster;
import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.JustclustJFrame;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.menubar.exportclustering.ExportClusteringJDialog;
import justclust.menubar.exportnetwork.ExportNetworkJDialog;
import justclust.menubar.filefilters.PNGFileFilter;
import justclust.menubar.exportnetwork.ExportNetworkJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesTableModel;

public class ColouriseClustersJMenuActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Apply Colour to Nodes")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            for (Cluster cluster : data.networkClusters) {
                Random random = new Random();
                Color colour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                ArrayList<Node> nodes = new ArrayList<Node>();
                nodes.add(cluster.nodes.get(0));
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).data == data) {
                        for (Node node : nodes.get(i).cluster.nodes) {
                            if (!nodes.contains(node)) {
                                nodes.add(node);
                            }
                        }
                    }
                    for (Node node : nodes.get(i).otherVersions) {
                        if (!nodes.contains(node)) {
                            nodes.add(node);
                        }
                    }
                }
                for (Node node : nodes) {
                    node.colour = colour;
                }
            }

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphColour();
            }

        }

        if (actionEvent.getActionCommand().equals("Apply Colour to Edges")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            for (Cluster cluster : data.networkClusters) {
                Random random = new Random();
                Color colour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                ArrayList<Node> nodes = new ArrayList<Node>();
                nodes.add(cluster.nodes.get(0));
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).data == data) {
                        for (Node node : nodes.get(i).cluster.nodes) {
                            if (!nodes.contains(node)) {
                                nodes.add(node);
                            }
                        }
                    }
                    for (Node node : nodes.get(i).otherVersions) {
                        if (!nodes.contains(node)) {
                            nodes.add(node);
                        }
                    }
                }
                for (Node node : nodes) {
                    if (node.data == data) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.colour = colour;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.colour = colour;
                                }
                            }
                        }
                    }
                }
            }

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphColour();
            }

        }

        if (actionEvent.getActionCommand().equals("Apply Colour to Nodes and Edges")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            for (Cluster cluster : data.networkClusters) {
                Random random = new Random();
                Color colour = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                ArrayList<Node> nodes = new ArrayList<Node>();
                nodes.add(cluster.nodes.get(0));
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).data == data) {
                        for (Node node : nodes.get(i).cluster.nodes) {
                            if (!nodes.contains(node)) {
                                nodes.add(node);
                            }
                        }
                    }
                    for (Node node : nodes.get(i).otherVersions) {
                        if (!nodes.contains(node)) {
                            nodes.add(node);
                        }
                    }
                }
                for (Node node : nodes) {
                    node.colour = colour;
                    if (node.data == data) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.colour = colour;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.colour = colour;
                                }
                            }
                        }
                    }
                }
            }

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphColour();
            }

        }

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

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

    }
}
