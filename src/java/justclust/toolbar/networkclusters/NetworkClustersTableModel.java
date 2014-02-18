package justclust.toolbar.networkclusters;

import edu.umd.cs.piccolo.PNode;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesTableModel;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;

public class NetworkClustersTableModel extends AbstractTableModel {

    String[] columnNames;
    Object[][] tableData;

    public NetworkClustersTableModel() {
        updateTable();
    }

    public void updateTable() {

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // this code populates the NetworkClustersTableModel with information about
        // the Clusters in the current network

        // find the maximum amount of Nodes in a Cluster to determine how
        // many columns the table should have
        int maxClusterSize = 0;
        for (Cluster cluster : data.networkClusters) {
            maxClusterSize = Math.max(maxClusterSize, cluster.nodes.size());
        }

        // create the headers for the table
        columnNames = new String[maxClusterSize + 6];
        columnNames[0] = "Label";
        columnNames[1] = "Nodes Visible";
        columnNames[2] = "Edges Visible";
        columnNames[3] = "Nodes Colour";
        columnNames[4] = "Edges Colour";
        columnNames[5] = "Node Amount";
        for (int i = 0; i < maxClusterSize; i++) {
            columnNames[i + 6] = "Node " + (i + 1);
        }

        tableData = new Object[data.networkClusters.size() + 1][maxClusterSize + 6];
        tableData[0][0] = "";

        // this code handles the check box in the table for the visibility
        // of all Clusters' Nodes.
        // if all such Nodes are visible, this should be ticked.
        // if any such Node is not visible, this should not be ticked.
        tableData[0][1] = true;
        LOOP:
        for (Cluster cluster : data.networkClusters) {
            for (Node node : cluster.nodes) {
                if (!node.visible) {
                    tableData[0][1] = false;
                    break LOOP;
                }
            }
        }

        // this code handles the check box in the table for the visibility
        // of all Clusters' Edges.
        // if all such Edges are visible, this should be ticked.
        // if any such Edge is not visible, this should not be ticked.
        tableData[0][2] = true;
        LOOP:
        for (Cluster cluster : data.networkClusters) {
            for (Node node : cluster.nodes) {
                for (Edge edge : node.edges) {
                    // Edges are only shown if their visible field is true, and
                    // their nodes' visible fields are true.
                    // this is so that Edges are not floating without a Node.
                    if (!edge.visible || !edge.node1.visible || !edge.node2.visible) {
                        if (edge.node1 == node && edge.node2.cluster == cluster) {
                            tableData[0][2] = false;
                            break LOOP;
                        }
                        if (edge.node2 == node && edge.node1.cluster == cluster) {
                            tableData[0][2] = false;
                            break LOOP;
                        }
                    }
                }
            }
        }

        // this code handles the cell in the table for the colour of all
        // Clusters' Nodes.
        // if all such Nodes have the same colour, this cell should be
        // that colour.
        // if any such Node does not have the same colour as another,
        // this cell should be white (the default Node colour).
        if (data.networkClusters.size() >= 1) {
            tableData[0][3] = data.networkClusters.get(0).nodes.get(0).colour;
        }
        LOOP:
        for (Cluster cluster : data.networkClusters) {
            for (Node node : cluster.nodes) {
                if (!node.colour.equals(data.networkClusters.get(0).nodes.get(0).colour)) {
                    tableData[0][3] = Color.WHITE;
                    break LOOP;
                }
            }
        }

        // this code handles the cell in the table for the colour of all
        // Clusters' Edges.
        // if all such Edges have the same colour, this cell should be
        // that colour.
        // if any such Edge does not have the same colour as another,
        // this cell should be black (the default Edge colour).
        Edge comparisonEdge = null;
        LOOP:
        for (Cluster cluster : data.networkClusters) {
            for (Node node : cluster.nodes) {
                for (Edge edge : node.edges) {
                    if (edge.node1 == node && edge.node2.cluster == cluster) {
                        comparisonEdge = edge;
                        break LOOP;
                    }
                    if (edge.node2 == node && edge.node1.cluster == cluster) {
                        comparisonEdge = edge;
                        break LOOP;
                    }
                }
            }
        }
        if (comparisonEdge != null) {
            tableData[0][4] = comparisonEdge.colour;
            LOOP:
            for (Cluster cluster : data.networkClusters) {
                for (Node node : cluster.nodes) {
                    for (Edge edge : node.edges) {
                        if (edge.node1 == node
                                && edge.node2.cluster == cluster
                                && !edge.colour.equals(comparisonEdge.colour)) {
                            tableData[0][4] = Color.BLACK;
                            break LOOP;
                        }
                        if (edge.node2 == node
                                && edge.node1.cluster == cluster
                                && !edge.colour.equals(comparisonEdge.colour)) {
                            tableData[0][4] = Color.BLACK;
                            break LOOP;
                        }
                    }
                }
            }
        } else {
            tableData[0][4] = Color.BLACK;
        }

        tableData[0][5] = "";

        // the 'All Clusters' row does not have a nodes field to populate
        // the table with
        for (int i = 0; i < maxClusterSize; i++) {
            tableData[0][i + 6] = "";
        }

        // for each Cluster in the network, populate the table with details
        // about the cluster
        for (int i = 0; i < data.networkClusters.size(); i++) {
            for (int j = 0; j < maxClusterSize + 6; j++) {
                switch (j) {
                    case 0:
                        tableData[i + 1][j] = data.networkClusters.get(i).label;
                        break;
                    case 1:
                        tableData[i + 1][j] = true;
                        for (Node node : data.networkClusters.get(i).nodes) {
                            if (!node.visible) {
                                tableData[i + 1][j] = false;
                                break;
                            }
                        }
                        break;
                    case 2:
                        tableData[i + 1][j] = true;
                        LOOP:
                        for (Node node : data.networkClusters.get(i).nodes) {
                            for (Edge edge : node.edges) {
                                // Edges are only shown if their visible field is true, and
                                // their nodes' visible fields are true.
                                // this is so that Edges are not floating without a Node.
                                if (!edge.visible || !edge.node1.visible || !edge.node2.visible) {
                                    if (edge.node1 == node
                                            && edge.node2.cluster == data.networkClusters.get(i)) {
                                        tableData[i + 1][j] = false;
                                        break LOOP;
                                    }
                                    if (edge.node2 == node
                                            && edge.node1.cluster == data.networkClusters.get(i)) {
                                        tableData[i + 1][j] = false;
                                        break LOOP;
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        tableData[i + 1][j] = data.networkClusters.get(i).nodes.get(0).colour;
                        for (Node node : data.networkClusters.get(i).nodes) {
                            if (!node.colour.equals(data.networkClusters.get(i).nodes.get(0).colour)) {
                                tableData[i + 1][j] = Color.WHITE;
                                break;
                            }
                        }
                        break;
                    case 4:
                        comparisonEdge = null;
                        LOOP:
                        for (Node node : data.networkClusters.get(i).nodes) {
                            for (Edge edge : node.edges) {
                                if (edge.node1 == node && edge.node2.cluster == data.networkClusters.get(i)) {
                                    comparisonEdge = edge;
                                    break LOOP;
                                }
                                if (edge.node2 == node && edge.node1.cluster == data.networkClusters.get(i)) {
                                    comparisonEdge = edge;
                                    break LOOP;
                                }
                            }
                        }
                        if (comparisonEdge != null) {
                            tableData[i + 1][j] = comparisonEdge.colour;
                            LOOP:
                            for (Node node : data.networkClusters.get(i).nodes) {
                                for (Edge edge : node.edges) {
                                    if (edge.node1 == node
                                            && edge.node2.cluster == data.networkClusters.get(i)
                                            && !edge.colour.equals(comparisonEdge.colour)) {
                                        tableData[i + 1][j] = Color.BLACK;
                                        break LOOP;
                                    }
                                    if (edge.node2 == node
                                            && edge.node1.cluster == data.networkClusters.get(i)
                                            && !edge.colour.equals(comparisonEdge.colour)) {
                                        tableData[i + 1][j] = Color.BLACK;
                                        break LOOP;
                                    }
                                }
                            }
                        } else {
                            // when a Cluster doesn't have an Edge, the
                            // colour for all of its Edges should be the
                            // colour for all Clusters' Edges because, if
                            // all Clusters' Edges are set to a colour, it
                            // would look like an error if some Clusters
                            // didn't have that colour for their Edges.
                            // this is because it is not immediately obvious
                            // that a Cluster has no Edges.
                            tableData[i + 1][j] = tableData[0][j];
                        }
                        break;
                    case 5:
                        tableData[i + 1][j] = data.networkClusters.get(i).nodes.size();
                        break;
                    default:
                        if (data.networkClusters.get(i).nodes.size() > j - 6) {
                            tableData[i + 1][j] = data.networkClusters.get(i).nodes.get(j - 6).label;
                        } else {
                            tableData[i + 1][j] = "";
                        }
                        break;
                }
            }
        }

    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return tableData.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        //Note that the tableData/cell address is constant,
        //no matter where the cell appears onscreen.
        if (row == 0 && col == 0 || col == 5 || col >= 6) {
            return false;
        } else {
            return true;
        }
    }

    // this method is called when the user inputs tableData into the table
    public void setValueAt(Object value, int row, int col) {

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // update the cell which was directly input into
        tableData[row][col] = value;
        // fireTableCellUpdated updates the appearance of the cell
        fireTableCellUpdated(row, col);

        // the label of a Cluster was changed
        if (col == 0) {
            data.networkClusters.get(row - 1).label = (String) value;
            // update the appearance of the current graph
            JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).updateGraphLabels();
        }

        // the visibility of a Cluster's/all Clusters' Nodes was changed
        if (col == 1) {

            if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(0)) {

                // the visibility of all Clusters' Nodes was changed

                for (int i = 0; i < data.networkClusters.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    for (Node node : data.networkClusters.get(i).nodes) {
                        node.visible = (boolean) value;
                        for (Node otherVersion : node.otherVersions) {
                            otherVersion.visible = (boolean) value;
                        }
                    }
                }

            }

            for (int i = 0; i < data.networkClusters.size(); i++) {
                if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(i + 1)) {

                    // the visibility of a Cluster's Nodes was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    for (Node node : data.networkClusters.get(i).nodes) {
                        node.visible = (boolean) value;
                        for (Node otherVersion : node.otherVersions) {
                            otherVersion.visible = (boolean) value;
                        }
                    }

                }
            }

            // this code handles the check box in the table for the visibility
            // of all Clusters' Nodes.
            // if all such Nodes are visible, this should be ticked.
            // if any such Node is not visible, this should not be ticked.
            tableData[0][col] = true;
            LOOP:
            for (Cluster cluster : data.networkClusters) {
                for (Node node : cluster.nodes) {
                    if (!node.visible) {
                        tableData[0][col] = false;
                        break LOOP;
                    }
                }
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, col);

            // this code handles the check box in the table for the visibility
            // of all Clusters' Edges.
            // if all such Edges are visible, this should be ticked.
            // if any such Edge is not visible, this should not be ticked.
            // this code also handles the check box in the table for
            // the visibility of each Cluster's Edges.
            tableData[0][2] = true;
            for (int i = 0; i < data.networkClusters.size(); i++) {
                tableData[i + 1][2] = true;
                LOOP:
                for (Node node : data.networkClusters.get(i).nodes) {
                    for (Edge edge : node.edges) {
                        if (!edge.visible || !edge.node1.visible || !edge.node2.visible) {
                            if (edge.node1.cluster == data.networkClusters.get(i)
                                    && edge.node2.cluster == data.networkClusters.get(i)) {
                                tableData[0][2] = false;
                                tableData[i + 1][2] = false;
                                break LOOP;
                            }
                        }
                    }
                }
                // fireTableCellUpdated updates the appearance of the cell
                fireTableCellUpdated(i + 1, 2);
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, 2);

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphVisibility();
            }

        }

        // the visibility of a Cluster's/all Clusters' Edges was changed
        if (col == 2) {

            if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(0)) {

                // the visibility of all Clusters' Edges was changed

                for (int i = 0; i < data.networkClusters.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                }

                // the showing of a hidden Edge should involve showing any
                // hidden Nodes which the Edge connects so that the Edge is
                // not floating without a Node.
                // when such a Node is shown, all the Edges connected to it
                // should be hidden so that the showing of the original
                // Edge does not cause the showing of others.
                // the original Edge is shown in the code below this code.
                if ((boolean) value == true) {
                    for (Cluster cluster : data.networkClusters) {
                        for (Node node : cluster.nodes) {
                            if (!node.visible) {
                                node.visible = true;
                                for (Node otherVersion : node.otherVersions) {
                                    otherVersion.visible = true;
                                }
                                for (Edge edge : node.edges) {
                                    edge.visible = false;
                                    for (Edge otherVersion : edge.otherVersions) {
                                        otherVersion.visible = false;
                                    }
                                }
                            }
                        }
                    }
                }

                // update the visible field of all Clusters' Edges
                for (Cluster cluster : data.networkClusters) {
                    for (Node node : cluster.nodes) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.visible = (boolean) value;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.visible = (boolean) value;
                                }
                            }
                        }
                    }
                }

            }

            for (int i = 0; i < data.networkClusters.size(); i++) {
                if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(i + 1)) {

                    // the visibility of a Cluster's Edges was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);

                    // the showing of a hidden Edge should involve showing any
                    // hidden Nodes which the Edge connects so that the Edge is
                    // not floating without a Node.
                    // when such a Node is shown, all the Edges connected to it
                    // should be hidden so that the showing of the original
                    // Edge does not cause the showing of others.
                    // the original Edge is shown in the code below this code.
                    if ((boolean) value == true) {
                        for (Node node : data.networkClusters.get(row - 1).nodes) {
                            if (!node.visible) {
                                node.visible = true;
                                for (Node otherVersion : node.otherVersions) {
                                    otherVersion.visible = true;
                                }
                                for (Edge edge : node.edges) {
                                    edge.visible = false;
                                    for (Edge otherVersion : edge.otherVersions) {
                                        otherVersion.visible = false;
                                    }
                                }
                            }
                        }
                    }

                    // update the visible field of the Cluster's Edges
                    for (Node node : data.networkClusters.get(row - 1).nodes) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.visible = (boolean) value;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.visible = (boolean) value;
                                }
                            }
                        }
                    }

                }
            }

            // update the check box in the table for the visibility of
            // all Clusters' Edges.
            // if all such Edges are now visible, this should be ticked.
            // if any such Edge is not visible, this should not be ticked.
            tableData[0][col] = true;
            LOOP:
            for (Cluster cluster : data.networkClusters) {
                for (Node node : cluster.nodes) {
                    for (Edge edge : node.edges) {
                        // Edges are only shown if their visible field is true, and
                        // their nodes' visible fields are true.
                        // this is so that Edges are not floating without a Node.
                        if (!edge.visible || !edge.node1.visible || !edge.node2.visible) {
                            if (edge.node1 == node && edge.node2.cluster == cluster) {
                                tableData[0][col] = false;
                                break LOOP;
                            }
                            if (edge.node2 == node && edge.node1.cluster == cluster) {
                                tableData[0][col] = false;
                                break LOOP;
                            }
                        }
                    }
                }
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, col);

            // this code handles the check box in the table for the visibility
            // of all Clusters' Nodes.
            // if all such Nodeds are visible, this should be ticked.
            // if any such Node is not visible, this should not be ticked.
            // this code also handles the check box in the table for
            // the visibility of each Cluster's Node.
            tableData[0][1] = true;
            for (int i = 0; i < data.networkClusters.size(); i++) {
                tableData[i + 1][1] = true;
                LOOP:
                for (Node node : data.networkClusters.get(i).nodes) {
                    for (Edge edge : node.edges) {
                        if (!node.visible) {
                            tableData[0][1] = false;
                            tableData[i + 1][1] = false;
                            break LOOP;
                        }
                    }
                }
                // fireTableCellUpdated updates the appearance of the cell
                fireTableCellUpdated(i + 1, 1);
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, 1);

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphVisibility();
            }

        }

        // the colour of a Cluster's/all Clusters' nodes was changed.
        // if the OK button of the NetworkClustersColorJDialog was clicked,
        // changes should be made.
        // if the Cancel button was clicked, no changes should be made.
        if (col == 3 && NetworkClustersTableCellEditor.classInstance.okButtonClicked) {

            if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(0)) {

                // the colour of all Clusters' Nodes was changed

                for (int i = 0; i < data.networkClusters.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    for (Node node : data.networkClusters.get(i).nodes) {
                        node.colour = (Color) value;
                        for (Node otherVersion : node.otherVersions) {
                            otherVersion.colour = (Color) value;
                        }
                    }
                }

            }

            for (int i = 0; i < data.networkClusters.size(); i++) {
                if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(i + 1)) {

                    // the colour of a Cluster's Nodes was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);

                    for (Node node : data.networkClusters.get(row - 1).nodes) {
                        node.colour = (Color) value;
                        for (Node otherVersion : node.otherVersions) {
                            otherVersion.colour = (Color) value;
                        }
                    }

                }
            }

            // update the cell in the table for the colour of all Clusters' Nodes.
            // if all such Nodes have the same colour, this cell should be
            // that colour.
            // if any such Node does not have the same colour as another,
            // this cell should be white (the default Node colour).
            if (data.networkClusters.size() >= 1) {
                tableData[0][col] = data.networkClusters.get(0).nodes.get(0).colour;
            }
            LOOP:
            for (Cluster cluster : data.networkClusters) {
                for (Node node : cluster.nodes) {
                    if (!node.colour.equals(data.networkClusters.get(0).nodes.get(0).colour)) {
                        tableData[0][col] = Color.WHITE;
                        break LOOP;
                    }
                }
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, col);

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphColour();
            }

        }

        // the colour of a Cluster's/all Clusters' Edges was changed.
        // if the OK button of the NetworkClustersColorJDialog was clicked,
        // changes should be made.
        // if the Cancel button was clicked, no changes should be made.
        if (col == 4 && NetworkClustersTableCellEditor.classInstance.okButtonClicked) {

            if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(0)) {

                // the colour of all Clusters' Edges was changed

                for (int i = 0; i < data.networkClusters.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    for (Node node : data.networkClusters.get(i).nodes) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.colour = (Color) value;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.colour = (Color) value;
                                }
                            }
                        }
                    }
                }

            }

            for (int i = 0; i < data.networkClusters.size(); i++) {
                if (NetworkClustersJDialog.classInstance.networkClustersListSelectionModel.isSelectedIndex(i + 1)) {

                    // the colour of a Cluster's Edges was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);

                    for (Node node : data.networkClusters.get(row - 1).nodes) {
                        for (Edge edge : node.edges) {
                            if (edge.node1.cluster == edge.node2.cluster) {
                                edge.colour = (Color) value;
                                for (Edge otherVersion : edge.otherVersions) {
                                    otherVersion.colour = (Color) value;
                                }
                            }
                        }
                    }

                }
            }

            // update the cell in the table for the colour of all Clusters' Edges.
            // if all such Edges have the same colour, this cell should be
            // that colour.
            // if any such Edge does not have the same colour as another,
            // this cell should be black (the default Edge colour).
            Edge comparisonEdge = null;
            LOOP:
            for (Cluster cluster : data.networkClusters) {
                for (Node node : cluster.nodes) {
                    for (Edge edge : node.edges) {
                        if (edge.node1 == node && edge.node2.cluster == cluster) {
                            comparisonEdge = edge;
                            break LOOP;
                        }
                        if (edge.node2 == node && edge.node1.cluster == cluster) {
                            comparisonEdge = edge;
                            break LOOP;
                        }
                    }
                }
            }
            if (comparisonEdge != null) {
                tableData[0][col] = comparisonEdge.colour;
                LOOP:
                for (Cluster cluster : data.networkClusters) {
                    for (Node node : cluster.nodes) {
                        for (Edge edge : node.edges) {
                            if (edge.node1 == node
                                    && edge.node2.cluster == cluster
                                    && !edge.colour.equals(comparisonEdge.colour)) {
                                tableData[0][col] = Color.BLACK;
                                break LOOP;
                            }
                            if (edge.node2 == node
                                    && edge.node1.cluster == cluster
                                    && !edge.colour.equals(comparisonEdge.colour)) {
                                tableData[0][col] = Color.BLACK;
                                break LOOP;
                            }
                        }
                    }
                }
            } else {
                tableData[0][col] = Color.BLACK;
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, col);

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphColour();
            }

        }
        
        // unselect all nodes so that any invisible nodes cannot be moved
        CustomGraphEditor currentCustomGraphEditor = JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex);
        for (int i = 1; i < currentCustomGraphEditor.nodeLayer.getAllNodes().size(); i++) {
            PNode pNode = ((ArrayList<PNode>) currentCustomGraphEditor.nodeLayer.getAllNodes()).get(i);
            currentCustomGraphEditor.customPSelectionEventHandler.unselect(pNode);
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
        if (SearchNetworkJDialog.classInstance != null) {

            String[] columnNames;
            String[][] tableData;
            columnNames = new String[]{"Label"};
            tableData = new String[0][columnNames.length];
            SearchNetworkJDialog.classInstance.nodesJTable.setModel(new DefaultTableModel(tableData, columnNames));
            columnNames = new String[]{"Label", "Node 1", "Node 2", "Weight"};
            tableData = new String[0][columnNames.length];
            SearchNetworkJDialog.classInstance.edgesJTable.setModel(new DefaultTableModel(tableData, columnNames));
            // create the headers for the table
            columnNames = new String[]{"Label"};
            tableData = new String[0][columnNames.length];
            SearchNetworkJDialog.classInstance.clustersJTable.setModel(new DefaultTableModel(tableData, columnNames));
            for (int i = 0; i < SearchNetworkJDialog.classInstance.clustersJTable.getColumnCount(); i++) {
                SearchNetworkJDialog.classInstance.clustersJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            }

            // disable the 'node options', 'edge options', and 'cluster options'
            // buttons because the tables have been repopulated and so no rows
            // are selected
            SearchNetworkJDialog.classInstance.nodeOptionsJButton.setEnabled(false);
            SearchNetworkJDialog.classInstance.edgeOptionsJButton.setEnabled(false);
            SearchNetworkJDialog.classInstance.clusterOptionsJButton.setEnabled(false);

        }
        if (HeatMapJDialog.classInstance != null) {
            HeatMapJDialog.classInstance.dispose();
        }
        if (DendrogramJDialog.classInstance != null) {
            DendrogramJDialog.classInstance.dispose();
        }

    }
}
