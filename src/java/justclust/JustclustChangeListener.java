/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import justclust.datastructures.Data;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.filterclusters.FilterClustersJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;
import justclust.toolbar.microarrayheatmap.MicroarrayHeatMapJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersRowHeaderTableModel;
import justclust.toolbar.networkclusters.NetworkClustersTableCellEditor;
import justclust.toolbar.networkclusters.NetworkClustersTableCellRenderer;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesListSelectionModel;
import justclust.toolbar.networkedges.NetworkEdgesRowHeaderTableModel;
import justclust.toolbar.networkedges.NetworkEdgesTableCellEditor;
import justclust.toolbar.networkedges.NetworkEdgesTableCellRenderer;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesRowHeaderTableModel;
import justclust.toolbar.networknodes.NetworkNodesTableCellEditor;
import justclust.toolbar.networknodes.NetworkNodesTableCellRenderer;
import justclust.toolbar.networknodes.NetworkNodesTableModel;
import justclust.toolbar.overrepresentationanalysis.OverrepresentationAnalysisJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;

// an instance of this class responds to the user changing between tabs in the
// justclustJTabbedPane
public class JustclustChangeListener implements ChangeListener {

    public void stateChanged(ChangeEvent ce) {

        // the repaint method updates the buttons on the toolbar so that
        // they are enabled or disabled according to whether the current
        // network has clusters or not
        JustclustJFrame.classInstance.repaint();

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data;
        if (currentCustomGraphEditorIndex >= 0) {
            data = Data.data.get(currentCustomGraphEditorIndex);
        } else {
            data = new Data();
        }

        // enable or disable menu items as appropriate to the currently visible
        // graph
        if (data.networkNodes == null) {
            JustclustJFrame.classInstance.exportGraphJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.exportNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.exportClusteringJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.colouriseClustersJMenu.setEnabled(false);
        } else if (data.networkClusters == null) {
            JustclustJFrame.classInstance.exportGraphJMenuItem.setEnabled(data.graphShown);
            JustclustJFrame.classInstance.exportNetworkJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.exportClusteringJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(data.graphShown);
            JustclustJFrame.classInstance.colouriseClustersJMenu.setEnabled(false);
        } else {
            JustclustJFrame.classInstance.exportGraphJMenuItem.setEnabled(data.graphShown);
            JustclustJFrame.classInstance.exportNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.exportClusteringJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(data.graphShown);
            JustclustJFrame.classInstance.colouriseClustersJMenu.setEnabled(data.graphShown);
        }

        // update any open dialogs to display information about the current
        // graph.
        // close any dialogs which shouldn't be open.
        if (NetworkDetailsJDialog.classInstance != null && NetworkDetailsJDialog.classInstance.isShowing()) {
            if (data.networkNodes == null) {
                NetworkDetailsJDialog.classInstance.dispose();
            } else {
                NetworkDetailsJDialog.classInstance.updateTextArea();
                NetworkDetailsJDialog.classInstance.networkDetailsDialogJTextArea.repaint();
            }
        }
        if (NetworkNodesJDialog.classInstance != null && NetworkNodesJDialog.classInstance.isShowing()) {
            if (data.networkNodes == null) {
                NetworkNodesJDialog.classInstance.dispose();
            } else {

                // the table model, table cell editor, and table renderer are
                // updated in case the current network has a graph shown and the
                // new network doesn't or vice versa. these two possibilities
                // have different table models, table cell editors, and table
                // renderers.
                JTable networkNodesDialogJTable = NetworkNodesJDialog.classInstance.networkNodesDialogJTable;
                NetworkNodesTableModel networkNodesTableModel = new NetworkNodesTableModel();
                networkNodesDialogJTable.setModel(networkNodesTableModel);
                //Set up renderer and editor
                NetworkNodesTableCellEditor networkNodesTableCellEditor = new NetworkNodesTableCellEditor();
                NetworkNodesTableCellRenderer networkNodesTableCellRenderer = new NetworkNodesTableCellRenderer(true);
                if (data.graphShown) {
                    networkNodesDialogJTable.getColumnModel().getColumn(2).setCellEditor(networkNodesTableCellEditor);
                    networkNodesDialogJTable.getColumnModel().getColumn(2).setCellRenderer(networkNodesTableCellRenderer);
                    networkNodesDialogJTable.getColumnModel().getColumn(3).setCellEditor(networkNodesTableCellEditor);
                    networkNodesDialogJTable.getColumnModel().getColumn(3).setCellRenderer(networkNodesTableCellRenderer);
                } else {
                    networkNodesDialogJTable.getColumnModel().getColumn(1).setCellEditor(networkNodesTableCellEditor);
                    networkNodesDialogJTable.getColumnModel().getColumn(1).setCellRenderer(networkNodesTableCellRenderer);
                }
                if (data.graphShown) {
                    networkNodesDialogJTable.getColumnModel().getColumn(1).setMaxWidth(50);
                    networkNodesDialogJTable.getColumnModel().getColumn(2).setMaxWidth(50);
                }

                ((NetworkNodesTableModel) NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getModel()).updateTable();
                ((NetworkNodesRowHeaderTableModel) NetworkNodesJDialog.classInstance.networkNodesRowHeaderJTable.getModel()).updateTable();
                NetworkNodesJDialog.classInstance.networkNodesDialogJTable.repaint();
            }
        }
        if (NetworkEdgesJDialog.classInstance != null && NetworkEdgesJDialog.classInstance.isShowing()) {
//            if (data.networkNodes == null) {
//                NetworkEdgesJDialog.classInstance.dispose();
//            } else {
//                
//                // the table model, table cell editor, and table renderer are
//                // updated in case the current network has a graph shown and the
//                // new network doesn't or vice versa. these two possibilities
//                // have different table models, table cell editors, and table
//                // renderers.
//                JTable networkEdgesDialogJTable = NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable;
//                networkEdgesDialogJTable.setModel(new NetworkEdgesTableModel());
//                //Set up renderer and editor
//                if (data.graphShown) {
//                    NetworkEdgesTableCellEditor networkEdgesTableCellEditor = new NetworkEdgesTableCellEditor();
//                    NetworkEdgesTableCellRenderer networkEdgesTableCellRenderer = new NetworkEdgesTableCellRenderer(true);
//                    networkEdgesDialogJTable.getColumnModel().getColumn(2).setCellEditor(networkEdgesTableCellEditor);
//                    networkEdgesDialogJTable.getColumnModel().getColumn(2).setCellRenderer(networkEdgesTableCellRenderer);
//                }
//                networkEdgesDialogJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//                for (int i = 0; i < networkEdgesDialogJTable.getColumnCount(); i++) {
//                    networkEdgesDialogJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
//                }
//                if (data.graphShown) {
//                    networkEdgesDialogJTable.getColumnModel().getColumn(1).setMaxWidth(50);
//                    networkEdgesDialogJTable.getColumnModel().getColumn(2).setMaxWidth(50);
//                }
//                
//                ((NetworkEdgesTableModel) NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.getModel()).updateTable();
//                ((NetworkEdgesRowHeaderTableModel) NetworkEdgesJDialog.classInstance.networkEdgesRowHeaderJTable.getModel()).updateTable();
//                NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.repaint();
//                
//            }
            NetworkEdgesJDialog.classInstance.dispose();
        }
        if (NetworkClustersJDialog.classInstance != null && NetworkClustersJDialog.classInstance.isShowing()) {
            if (data.networkClusters == null) {
                NetworkClustersJDialog.classInstance.dispose();
            } else {

                // the table model, table cell editor, and table renderer are
                // updated in case the current network has a graph shown and the
                // new network doesn't or vice versa. these two possibilities
                // have different table models, table cell editors, and table
                // renderers.
                JTable networkClustersDialogJTable = NetworkClustersJDialog.classInstance.networkClustersDialogJTable;
                networkClustersDialogJTable.setModel(new NetworkClustersTableModel());
                //Set up renderer and editor
                if (data.graphShown) {
                    NetworkClustersTableCellEditor networkClustersTableCellEditor = new NetworkClustersTableCellEditor();
                    NetworkClustersTableCellRenderer networkClustersTableCellRenderer = new NetworkClustersTableCellRenderer(true);
                    networkClustersDialogJTable.getColumnModel().getColumn(3).setCellEditor(networkClustersTableCellEditor);
                    networkClustersDialogJTable.getColumnModel().getColumn(3).setCellRenderer(networkClustersTableCellRenderer);
                    networkClustersDialogJTable.getColumnModel().getColumn(4).setCellEditor(networkClustersTableCellEditor);
                    networkClustersDialogJTable.getColumnModel().getColumn(4).setCellRenderer(networkClustersTableCellRenderer);
                }

                ((NetworkClustersTableModel) NetworkClustersJDialog.classInstance.networkClustersDialogJTable.getModel()).updateTable();
                ((NetworkClustersRowHeaderTableModel) NetworkClustersJDialog.classInstance.networkClustersRowHeaderJTable.getModel()).updateTable();
                NetworkClustersJDialog.classInstance.networkClustersDialogJTable.repaint();

            }
        }
        if (SearchNetworkJDialog.classInstance != null && SearchNetworkJDialog.classInstance.isShowing()) {
            if (data.networkNodes == null) {
                SearchNetworkJDialog.classInstance.dispose();
            } else {

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
        }
        if (FilterClustersJDialog.classInstance != null && FilterClustersJDialog.classInstance.isShowing()) {
            if (data.networkClusters == null) {
                FilterClustersJDialog.classInstance.dispose();
            }
        }
        if (OverrepresentationAnalysisJDialog.classInstance != null && OverrepresentationAnalysisJDialog.classInstance.isShowing()) {
            OverrepresentationAnalysisJDialog.classInstance.dispose();
        }
        if (HeatMapJDialog.classInstance != null && HeatMapJDialog.classInstance.isShowing()) {
            HeatMapJDialog.classInstance.dispose();
        }
        if (MicroarrayHeatMapJDialog.classInstance != null && MicroarrayHeatMapJDialog.classInstance.isShowing()) {
            MicroarrayHeatMapJDialog.classInstance.dispose();
        }
        if (DendrogramJDialog.classInstance != null && DendrogramJDialog.classInstance.isShowing()) {
            DendrogramJDialog.classInstance.dispose();
        }

    }
}
