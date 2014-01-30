/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust;

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
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesRowHeaderTableModel;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesRowHeaderTableModel;
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
            JustclustJFrame.classInstance.exportGraphJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.exportNetworkJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.exportClusteringJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.colouriseClustersJMenu.setEnabled(false);
        } else {
            JustclustJFrame.classInstance.exportGraphJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.exportNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.exportClusteringJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(false);
            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(true);
            JustclustJFrame.classInstance.colouriseClustersJMenu.setEnabled(true);
        }

        // update any open dialogs to display information about the current
        // graph.
        // close any dialogs which shouldn't be open.
        if (NetworkDetailsJDialog.classInstance != null) {
            if (data.networkNodes == null) {
                NetworkDetailsJDialog.classInstance.dispose();
            } else {
            NetworkDetailsJDialog.classInstance.updateTextArea();
            NetworkDetailsJDialog.classInstance.networkDetailsDialogJTextArea.repaint();
            }
        }
        if (NetworkNodesJDialog.classInstance != null) {
            if (data.networkNodes == null) {
                NetworkNodesJDialog.classInstance.dispose();
            } else {
            ((NetworkNodesTableModel) NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getModel()).updateTable();
            ((NetworkNodesRowHeaderTableModel) NetworkNodesJDialog.classInstance.networkNodesRowHeaderJTable.getModel()).updateTable();
            NetworkNodesJDialog.classInstance.networkNodesDialogJTable.repaint();
            }
        }
        if (NetworkEdgesJDialog.classInstance != null) {
            if (data.networkNodes == null) {
                NetworkEdgesJDialog.classInstance.dispose();
            } else {
            ((NetworkEdgesTableModel) NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.getModel()).updateTable();
            ((NetworkEdgesRowHeaderTableModel) NetworkEdgesJDialog.classInstance.networkEdgesRowHeaderJTable.getModel()).updateTable();
            NetworkEdgesJDialog.classInstance.networkEdgesDialogJTable.repaint();
            }
        }
        if (NetworkClustersJDialog.classInstance != null) {
            if (data.networkClusters == null) {
                NetworkClustersJDialog.classInstance.dispose();
            } else {
                ((NetworkClustersTableModel) NetworkClustersJDialog.classInstance.networkClustersDialogJTable.getModel()).updateTable();
                ((NetworkClustersRowHeaderTableModel) NetworkClustersJDialog.classInstance.networkClustersRowHeaderJTable.getModel()).updateTable();
                NetworkClustersJDialog.classInstance.networkClustersDialogJTable.repaint();
            }
        }
        if (SearchNetworkJDialog.classInstance != null) {
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
        if (FilterClustersJDialog.classInstance != null) {
            if (data.networkClusters == null) {
                FilterClustersJDialog.classInstance.dispose();
            }
        }
        if (OverrepresentationAnalysisJDialog.classInstance != null) {
            OverrepresentationAnalysisJDialog.classInstance.dispose();
        }
        if (HeatMapJDialog.classInstance != null) {
            HeatMapJDialog.classInstance.dispose();
        }
        if (MicroarrayHeatMapJDialog.classInstance != null) {
            MicroarrayHeatMapJDialog.classInstance.dispose();
        }
        if (DendrogramJDialog.classInstance != null) {
            DendrogramJDialog.classInstance.dispose();
        }

    }
}
