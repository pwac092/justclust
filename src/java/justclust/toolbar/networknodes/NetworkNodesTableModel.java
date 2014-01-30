package justclust.toolbar.networknodes;

import edu.umd.cs.piccolo.PNode;
import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
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
import javax.swing.JColorChooser;
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
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.toolbar.networkclusters.NetworkClustersChangeListener;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersListSelectionModel;
import justclust.toolbar.networkclusters.NetworkClustersTableCellEditor;
import justclust.toolbar.networkclusters.NetworkClustersTableCellRenderer;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkedges.NetworkEdgesChangeListener;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesListSelectionModel;
import justclust.toolbar.networkedges.NetworkEdgesTableCellEditor;
import justclust.toolbar.networkedges.NetworkEdgesTableCellRenderer;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkTableCellEditor;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;

public class NetworkNodesTableModel extends AbstractTableModel {

    String[] columnNames;
    Object[][] tableData;

    public NetworkNodesTableModel() {
        updateTable();
    }

    public void updateTable() {

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        // this code populates the NetworkNodesTableModel with information about
        // the Nodes in the current network

        // create the headers for the table
        columnNames = new String[]{"Label", "Visible", "Colour", "Search Database"};

        tableData = new Object[data.networkNodes.size() + 1][4];
        tableData[0][0] = "";

        // this code handles the check box in the table for the visibility
        // of all Nodes.
        // if all Nodes are now visible, this should be ticked.
        // if any Node is not visible, this should not be ticked.
        tableData[0][1] = true;
        for (Node node : data.networkNodes) {
            if (!node.visible) {
                tableData[0][1] = false;
                break;
            }
        }

        // this code handles the cell in the table for the colour of all
        // Nodes.
        // if all Nodes have the same colour, this cell should be
        // that colour.
        // if any Node does not have the same colour as another,
        // this cell should be white (the default Node colour).
        if (data.networkNodes.size() >= 1) {
            tableData[0][2] = data.networkNodes.get(0).colour;
        }
        for (Node node : data.networkNodes) {
            if (!node.colour.equals(data.networkNodes.get(0).colour)) {
                tableData[0][2] = Color.WHITE;
                break;
            }
        }

        // the 'All Nodes' row does not have a label to search UniProtKB with
        tableData[0][3] = "";

        // for each Node in the network, populate the table with the
        // contents of the Node's label, visible, and colour fields
        for (int i = 0; i < data.networkNodes.size(); i++) {
            for (int j = 0; j < 4; j++) {
                switch (j) {
                    case 0:
                        tableData[i + 1][j] = data.networkNodes.get(i).label;
                        break;
                    case 1:
                        tableData[i + 1][j] = data.networkNodes.get(i).visible;
                        break;
                    case 2:
                        tableData[i + 1][j] = data.networkNodes.get(i).colour;
                        break;
                    case 3:
                        tableData[i + 1][j] = "";
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
        if (row == 0 && (col == 0 || col == 3)) {
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

        // the label of a node was changed
        if (col == 0) {

            data.networkNodes.get(row - 1).label = (String) value;
            for (Node otherVersion : data.networkNodes.get(row - 1).otherVersions) {
                otherVersion.label = (String) value;
            }

            // update the appearance of all customGraphEditors.
            // all customGraphEditors are included incase a change in a graph, other than
            // the current graph, has been made.
            for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
                customGraphEditor.updateGraphLabels();
            }

        }

        // the visibility of a node/all nodes was changed
        if (col == 1) {

            if (NetworkNodesJDialog.classInstance.networkNodesListSelectionModel.isSelectedIndex(0)) {

                // the visibility of all nodes was changed

                for (int i = 0; i < data.networkNodes.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkNodes.get(i).visible = (boolean) value;
                    for (Node otherVersion : data.networkNodes.get(i).otherVersions) {
                        otherVersion.visible = (boolean) value;
                    }
                }

            }

            for (int i = 0; i < data.networkNodes.size(); i++) {
                if (NetworkNodesJDialog.classInstance.networkNodesListSelectionModel.isSelectedIndex(i + 1)) {

                    // the visibility of a node was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkNodes.get(i).visible = (boolean) value;
                    for (Node otherVersion : data.networkNodes.get(i).otherVersions) {
                        otherVersion.visible = (boolean) value;
                    }

                }
            }

            // update the check box in the table for the visibility of
            // all nodes.
            // if all nodes are now visible, this should be ticked.
            // if any node is not visible, this should not be ticked.
            tableData[0][col] = true;
            for (Node node : data.networkNodes) {
                if (!node.visible) {
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

        // the colour of a node/all nodes was changed.
        // if the OK button of the NetworkNodesColorJDialog was clicked,
        // changes should be made.
        // if the Cancel button was clicked, no changes should be made.
        if (col == 2 && NetworkNodesTableCellEditor.classInstance.okButtonClicked) {

            if (NetworkNodesJDialog.classInstance.networkNodesListSelectionModel.isSelectedIndex(0)) {

                // the colour of all nodes was changed

                for (int i = 0; i < data.networkNodes.size(); i++) {
                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkNodes.get(i).colour = (Color) value;
                    for (Node otherVersion : data.networkNodes.get(i).otherVersions) {
                        otherVersion.colour = (Color) value;
                    }
                }

            }

            for (int i = 0; i < data.networkNodes.size(); i++) {
                if (NetworkNodesJDialog.classInstance.networkNodesListSelectionModel.isSelectedIndex(i + 1)) {

                    // the colour of a node was changed

                    tableData[i + 1][col] = value;
                    // fireTableCellUpdated updates the appearance of the cell
                    fireTableCellUpdated(i + 1, col);
                    data.networkNodes.get(i).colour = (Color) value;
                    for (Node otherVersion : data.networkNodes.get(i).otherVersions) {
                        otherVersion.colour = (Color) value;
                    }

                }
            }

            // update the cell in the table for the colour of all nodes.
            // if all nodes have the same colour, this cell should be
            // that colour.
            // if any node does not have the same colour as another,
            // this cell should be white (the default node colour).
            if (data.networkNodes.size() >= 1) {
                tableData[0][col] = data.networkNodes.get(0).colour;
            }
            for (Node node : data.networkNodes) {
                if (!node.colour.equals(data.networkNodes.get(0).colour)) {
                    tableData[0][col] = Color.WHITE;
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
