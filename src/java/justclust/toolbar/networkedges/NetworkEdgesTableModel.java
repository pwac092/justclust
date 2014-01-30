package justclust.toolbar.networkedges;

import edu.umd.cs.piccolo.PNode;
import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesTableModel;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;

public class NetworkEdgesTableModel extends AbstractTableModel {

    String[] columnNames;
    Object[][] tableData;

    public NetworkEdgesTableModel() {
        updateTable();
    }

    public void updateTable() {

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // this code populates the NetworkEdgesTableModel with information about
        // the Edges in the current network

        // create the headers for the table
        columnNames = new String[]{"Label", "Visible", "Colour", "Node 1", "Node 2", "Weight"};

        tableData = new Object[data.networkEdges.size() + 1][7];
        tableData[0][0] = "";

        // this code handles the check box in the table for the visibility
        // of all Edges.
        // if all Edges are now visible, this should be ticked.
        // if any Edge is not visible, this should not be ticked.
        tableData[0][1] = true;
        for (Edge edge : data.networkEdges) {
            // Edges are only shown if their visible field is true, and
            // their nodes' visible fields are true.
            // this is so that Edges are not floating without a Node.
            if (!edge.visible || !edge.node1.visible || !edge.node2.visible) {
                tableData[0][1] = false;
                break;
            }
        }

        // this code handles the cell in the table for the colour of all
        // Edges.
        // if all Edges have the same colour, this cell should be
        // that colour.
        // if any Edge does not have the same colour as another,
        // this cell should be black (the default Edge colour).
        if (data.networkEdges.size() >= 1) {
            tableData[0][2] = data.networkEdges.get(0).colour;
        }
        for (Edge edge : data.networkEdges) {
            if (!edge.colour.equals(data.networkEdges.get(0).colour)) {
                tableData[0][2] = Color.BLACK;
                break;
            }
        }

        // the 'All Edges' row does not have node1, node2, or weight
        // fields to populate the table with
        tableData[0][3] = "";
        tableData[0][4] = "";
        tableData[0][5] = "";

        // for each Edge in the network, populate the table with the
        // contents of the Edge's label, visible, colour, node1, node2, and
        // weight fields
        for (int i = 0; i < data.networkEdges.size(); i++) {
            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0:
                        tableData[i + 1][j] = data.networkEdges.get(i).label;
                        break;
                    case 1:
                        // Edges are only shown if their visible field is
                        // true, and their nodes' visible fields are true.
                        // this is so that Edges are not floating without a
                        // Node.
                        tableData[i + 1][j] = data.networkEdges.get(i).visible
                                && data.networkEdges.get(i).node1.visible
                                && data.networkEdges.get(i).node2.visible;
                        break;
                    case 2:
                        tableData[i + 1][j] = data.networkEdges.get(i).colour;
                        break;
                    case 3:
                        tableData[i + 1][j] = data.networkEdges.get(i).node1.label;
                        break;
                    case 4:
                        tableData[i + 1][j] = data.networkEdges.get(i).node2.label;
                        break;
                    case 5:
                        tableData[i + 1][j] = new DecimalFormat("#.#####").format(data.networkEdges.get(i).weight);
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
        if (row == 0 && col == 0 || col == 3 || col == 4 || col == 5) {
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

        // the label of an Edge was changed
        if (col == 0) {

            data.networkEdges.get(row - 1).label = (String) value;
            for (Edge otherVersion : data.networkEdges.get(row - 1).otherVersions) {
                otherVersion.label = (String) value;
            }

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphLabels();
            }

        }

        // the visibility of an Edge/all Edges was changed
        if (col == 1) {

            if (NetworkEdgesJDialog.classInstance.networkEdgesListSelectionModel.isSelectedIndex(0)) {

                // the visibility of all Edges was changed

                for (int i = 0; i < data.networkEdges.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkEdges.get(i).visible = (boolean) value;
                    for (Edge otherVersion : data.networkEdges.get(i).otherVersions) {
                        otherVersion.visible = (boolean) value;
                    }
                }
                // all Nodes which are connected to Edges are made visible
                // as well so that the Edges are not floating with no Node
                for (Node node : data.networkNodes) {
                    if (node.edges.size() > 0) {
                        node.visible = true;
                        for (Node otherVersion : node.otherVersions) {
                            otherVersion.visible = true;
                        }
                    }
                }

            }

            for (int i = 0; i < data.networkEdges.size(); i++) {
                if (NetworkEdgesJDialog.classInstance.networkEdgesListSelectionModel.isSelectedIndex(i + 1)) {

                    // the visibility of an Edge was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkEdges.get(i).visible = (boolean) value;
                    for (Edge otherVersion : data.networkEdges.get(i).otherVersions) {
                        otherVersion.visible = (boolean) value;
                    }

                    // if the Edge is now visible and a Node which it connects
                    // isn't, make the Node visible, and make all Edges which
                    // are connected to the Node invisible so that they do not
                    // appear
                    if ((boolean) value == true) {
                        if (!data.networkEdges.get(i).node1.visible) {
                            data.networkEdges.get(i).node1.visible = true;
                            for (Node otherVersion : data.networkEdges.get(i).node1.otherVersions) {
                                otherVersion.visible = true;
                            }
                            for (Edge edge : data.networkEdges.get(i).node1.edges) {
                                if (edge != data.networkEdges.get(i)) {
                                    edge.visible = false;
                                    for (Edge otherVersion : edge.otherVersions) {
                                        otherVersion.visible = false;
                                    }
                                }
                            }
                        }
                        if (!data.networkEdges.get(i).node2.visible) {
                            data.networkEdges.get(i).node2.visible = true;
                            for (Node otherVersion : data.networkEdges.get(i).node2.otherVersions) {
                                otherVersion.visible = true;
                            }
                            for (Edge edge : data.networkEdges.get(i).node2.edges) {
                                if (edge != data.networkEdges.get(i)) {
                                    edge.visible = false;
                                    for (Edge otherVersion : edge.otherVersions) {
                                        otherVersion.visible = false;
                                    }
                                }
                            }
                        }
                    }

                }
            }

            // update the check box in the table for the visibility of
            // all Edges.
            // if all Edges are now visible, this should be ticked.
            // if any Edge is not visible, this should not be ticked.
            tableData[0][col] = true;
            for (int i = 0; i < data.networkEdges.size(); i++) {
                if (!data.networkEdges.get(i).visible) {
                    tableData[0][col] = false;
                    break;
                }
            }
            // fireTableCellUpdated updates the appearance of the cell
            fireTableCellUpdated(0, col);

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphVisibility();
            }

        }

        // the colour of an Edge/all Edges was changed.
        // if the OK button of the NetworkEdgesColorJDialog was clicked,
        // changes should be made.
        // if the Cancel button was clicked, no changes should be made.
        if (col == 2 && NetworkEdgesTableCellEditor.classInstance.okButtonClicked) {

            if (NetworkEdgesJDialog.classInstance.networkEdgesListSelectionModel.isSelectedIndex(0)) {

                // the colour of all Edges was changed

                for (int i = 0; i < data.networkEdges.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkEdges.get(i).colour = (Color) value;
                    for (Edge otherVersion : data.networkEdges.get(i).otherVersions) {
                        otherVersion.colour = (Color) value;
                    }
                }

            }

            for (int i = 0; i < data.networkEdges.size(); i++) {
                if (NetworkEdgesJDialog.classInstance.networkEdgesListSelectionModel.isSelectedIndex(i + 1)) {

                    // the colour of an Edge was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkEdges.get(i).colour = (Color) value;
                    for (Edge otherVersion : data.networkEdges.get(i).otherVersions) {
                        otherVersion.colour = (Color) value;
                    }

                }
            }

            // update the cell in the table for the colour of all Edges.
            // if all Edges have the same colour, this cell should be
            // that colour.
            // if any Edge does not have the same colour as another,
            // this cell should be black (the default Edge colour).
            if (data.networkEdges.size() >= 1) {
                tableData[0][col] = data.networkEdges.get(0).colour;
            }
            for (Edge edge : data.networkEdges) {
                if (!edge.colour.equals(data.networkEdges.get(0).colour)) {
                    tableData[0][col] = Color.BLACK;
                    break;
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
