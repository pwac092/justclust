/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.searchnetwork;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;

/**
 *
 * @author wuaz008
 */
public class SearchNetworkActionListener implements ActionListener {

    // these ArrayLists are fields so that they can be accessed at different
    // times
    ArrayList<Node> foundNodes;
    ArrayList<Edge> foundEdges;
    ArrayList<Cluster> foundClusters;

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Search")) {

            foundNodes = new ArrayList<Node>();
            foundEdges = new ArrayList<Edge>();
            foundClusters = new ArrayList<Cluster>();

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            // find any Nodes whose labels each contain the search term and add
            // them, their Edges, and their Clusters to the ArrayLists of found
            // items
            for (Node node : data.networkNodes) {
                if (node.label.toLowerCase().contains(SearchNetworkJDialog.classInstance.searchJTextField.getText().toLowerCase())) {
                    foundNodes.add(node);
                    for (Edge edge : node.edges) {
                        if (!foundEdges.contains(edge)) {
                            foundEdges.add(edge);
                        }
                    }
                    if (!foundClusters.contains(node.cluster) && node.cluster != null) {
                        foundClusters.add(node.cluster);
                    }
                }
            }

            // find any Edges whose labels each contain the search term and add
            // them to the ArrayLists of found items
            for (Edge edge : data.networkEdges) {
                // an edge may have a label field containing null and this should
                // count as an empty string because they have the same appearance
                // graphically
                if (edge.label == null && SearchNetworkJDialog.classInstance.searchJTextField.getText().equals("")
                        || edge.label != null && edge.label.toLowerCase().contains(SearchNetworkJDialog.classInstance.searchJTextField.getText().toLowerCase())) {
                    if (!foundEdges.contains(edge)) {
                        foundEdges.add(edge);
                    }
                }
            }

            // find any Clusters whose labels each contain the search term and add
            // them to the ArrayLists of found items
            if (data.networkClusters != null) {
                for (Cluster cluster : data.networkClusters) {
                    // a cluster may have a label field containing null and this should
                    // count as an empty string because they have the same appearance
                    // graphically
                    if (cluster.label == null && SearchNetworkJDialog.classInstance.searchJTextField.getText().equals("")
                            || cluster.label != null && cluster.label.toLowerCase().contains(SearchNetworkJDialog.classInstance.searchJTextField.getText().toLowerCase())) {
                        if (!foundClusters.contains(cluster)) {
                            foundClusters.add(cluster);
                        }
                    }
                }
            }

            // populate the tables with the found nodes, edges, and clusters

            String[] columnNames;
            String[][] tableData;
            TableColumnModel tableColumnModel;
            SearchNetworkTableCellEditor searchNetworkTableCellEditor = new SearchNetworkTableCellEditor();

            columnNames = new String[]{"Label"};
            tableData = new String[foundNodes.size()][columnNames.length];
            for (int i = 0; i < foundNodes.size(); i++) {
                tableData[i][0] = foundNodes.get(i).label;
            }
            SearchNetworkJDialog.classInstance.nodesJTable.setModel(new DefaultTableModel(tableData, columnNames));
            tableColumnModel = SearchNetworkJDialog.classInstance.nodesJTable.getColumnModel();
            for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
                TableColumn tableColumn = tableColumnModel.getColumn(i);
                tableColumn.setCellEditor(searchNetworkTableCellEditor);
            }
            columnNames = new String[]{"Label", "Node 1", "Node 2", "Weight"};
            tableData = new String[foundEdges.size()][columnNames.length];
            for (int i = 0; i < foundEdges.size(); i++) {
                tableData[i][0] = foundEdges.get(i).label;
                tableData[i][1] = foundEdges.get(i).node1.label;
                tableData[i][2] = foundEdges.get(i).node2.label;
                tableData[i][3] = new DecimalFormat("#.#####").format(foundEdges.get(i).weight);;
            }
            SearchNetworkJDialog.classInstance.edgesJTable.setModel(new DefaultTableModel(tableData, columnNames));
            tableColumnModel = SearchNetworkJDialog.classInstance.edgesJTable.getColumnModel();
            for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
                TableColumn tableColumn = tableColumnModel.getColumn(i);
                tableColumn.setCellEditor(searchNetworkTableCellEditor);
            }

            // find the maximum amount of Nodes in a Cluster to determine how
            // many columns the table should have
            int maxClusterSize = 0;
            for (Cluster cluster : foundClusters) {
                if (cluster.nodes != null) {
                    maxClusterSize = Math.max(maxClusterSize, cluster.nodes.size());
                }
            }
            // create the headers for the table
            columnNames = new String[maxClusterSize + 1];
            columnNames[0] = "Label";
            for (int i = 0; i < maxClusterSize; i++) {
                columnNames[i + 1] = "Node " + (i + 1);
            }
            tableData = new String[foundClusters.size()][columnNames.length];
            for (int i = 0; i < foundClusters.size(); i++) {
                tableData[i][0] = foundClusters.get(i).label;
                for (int j = 0; j < maxClusterSize; j++) {
                    if (foundClusters.get(i).nodes.size() > j) {
                        tableData[i][j + 1] = foundClusters.get(i).nodes.get(j).label;
                    } else {
                        tableData[i][j + 1] = "";
                    }
                }
            }
            SearchNetworkJDialog.classInstance.clustersJTable.setModel(new DefaultTableModel(tableData, columnNames));
            for (int i = 0; i < SearchNetworkJDialog.classInstance.clustersJTable.getColumnCount(); i++) {
                SearchNetworkJDialog.classInstance.clustersJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
            tableColumnModel = SearchNetworkJDialog.classInstance.clustersJTable.getColumnModel();
            for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
                TableColumn tableColumn = tableColumnModel.getColumn(i);
                tableColumn.setCellEditor(searchNetworkTableCellEditor);
            }

            // disable the 'node options', 'edge options', and 'cluster options'
            // buttons because the tables have been repopulated and so no rows
            // are selected
            SearchNetworkJDialog.classInstance.nodeOptionsJButton.setEnabled(false);
            SearchNetworkJDialog.classInstance.edgeOptionsJButton.setEnabled(false);
            SearchNetworkJDialog.classInstance.clusterOptionsJButton.setEnabled(false);

        }

        if (actionEvent.getActionCommand().equals("Node Options...")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            Node node = foundNodes.get(SearchNetworkJDialog.classInstance.nodesJTable.getSelectedRow());
            int rowToScrollTo = data.networkNodes.indexOf(node) + 1;
            ArrayList<Integer> rowsToHighlight = new ArrayList<Integer>();
            rowsToHighlight.add(rowToScrollTo);
            new NetworkNodesJDialog(JustclustJFrame.classInstance, rowToScrollTo, rowsToHighlight);

        }

        if (actionEvent.getActionCommand().equals("Edge Options...")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            Edge edge = foundEdges.get(SearchNetworkJDialog.classInstance.edgesJTable.getSelectedRow());
            int rowToScrollTo = data.networkEdges.indexOf(edge) + 1;
            ArrayList<Integer> rowsToHighlight = new ArrayList<Integer>();
            rowsToHighlight.add(rowToScrollTo);
            new NetworkEdgesJDialog(JustclustJFrame.classInstance, rowToScrollTo, rowsToHighlight);

        }

        if (actionEvent.getActionCommand().equals("Cluster Options...")) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            Cluster cluster = foundClusters.get(SearchNetworkJDialog.classInstance.clustersJTable.getSelectedRow());
            int rowToScrollTo = data.networkClusters.indexOf(cluster) + 1;
            ArrayList<Integer> rowsToHighlight = new ArrayList<Integer>();
            rowsToHighlight.add(rowToScrollTo);
            new NetworkClustersJDialog(JustclustJFrame.classInstance, rowToScrollTo, rowsToHighlight);

        }

    }
}
