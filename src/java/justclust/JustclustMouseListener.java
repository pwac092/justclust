package justclust;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.applylayout.ApplyLayoutJDialog;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;
import justclust.graphdrawing.CustomGraphEditorKeyListener;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.filterclusters.FilterClustersJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;
import justclust.toolbar.manageplugins.ManagePluginsJDialog;
import justclust.toolbar.microarrayheatmap.MicroarrayHeatMapJDialog;
import justclust.toolbar.overrepresentationanalysis.OverrepresentationAnalysisJDialog;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;

public class JustclustMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {

        // if the justclustJPanel was clicked
        if (me.getComponent() == JustclustJFrame.classInstance.justclustJPanel) {

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data;
            if (currentCustomGraphEditorIndex >= 0) {
                data = Data.data.get(currentCustomGraphEditorIndex);
            } else {
                data = new Data();
            }

            int x = me.getX();
            int y = me.getY();
            // the coordinates are those for the buttons in the toolbar
            if (x >= JustclustJFrame.classInstance.justclustJPanel.detailsButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.detailsButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.detailsButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.detailsButtonCoordinates.y + 39) {
                // the network details button will only be accessible when a network
                // has been created
                if (data.networkNodes != null) {
                    if (NetworkDetailsJDialog.classInstance == null || !NetworkDetailsJDialog.classInstance.isShowing()) {
                        // if there has never been a NetworkDetailsJDialog or
                        // the last one was closed, a new one is created
                        new NetworkDetailsJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a NetworkDetailsJDialog is already open, this gets
                        // the focus
                        NetworkDetailsJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.nodesButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.nodesButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.nodesButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.nodesButtonCoordinates.y + 39) {
                // the network Nodes button will only be accessible when a network
                // has been created
                if (data.networkNodes != null) {
                    if (NetworkNodesJDialog.classInstance == null || !NetworkNodesJDialog.classInstance.isShowing()) {
                        // if there has never been a NetworkNodesJDialog or
                        // the last one was closed, a new one is created
                        new NetworkNodesJDialog(JustclustJFrame.classInstance, 0, new ArrayList<Integer>());
                    } else {
                        // if a NetworkNodesJDialog is already open, this gets
                        // the focus
                        NetworkNodesJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.edgesButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.edgesButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.edgesButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.edgesButtonCoordinates.y + 39) {
                // the network Edges button will only be accessible when a network
                // has been created
                if (data.networkNodes != null) {
                    if (NetworkEdgesJDialog.classInstance == null || !NetworkEdgesJDialog.classInstance.isShowing()) {
                        // if there has never been a NetworkEdgesJDialog or
                        // the last one was closed, a new one is created
                        new NetworkEdgesJDialog(JustclustJFrame.classInstance, 0, new ArrayList<Integer>());
                    } else {
                        // if a NetworkEdgesJDialog is already open, this gets
                        // the focus
                        NetworkEdgesJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.clustersButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.clustersButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.clustersButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.clustersButtonCoordinates.y + 39) {
                // the network Clusters button will only be accessible when the
                // current network has been clustered
                if (data.networkClusters != null) {
                    if (NetworkClustersJDialog.classInstance == null || !NetworkClustersJDialog.classInstance.isShowing()) {
                        // if there has never been a NetworkClustersJDialog or
                        // the last one was closed, a new one is created
                        new NetworkClustersJDialog(JustclustJFrame.classInstance, 0, new ArrayList<Integer>());
                    } else {
                        // if a NetworkClustersJDialog is already open, this gets
                        // the focus
                        NetworkClustersJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.searchButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.searchButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.searchButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.searchButtonCoordinates.y + 39) {
                // the search network button will only be accessible when a
                // network has been created
                if (data.networkNodes != null) {
                    if (SearchNetworkJDialog.classInstance == null || !SearchNetworkJDialog.classInstance.isShowing()) {
                        // if there has never been a SearchNetworkJDialog or
                        // the last one was closed, a new one is created
                        new SearchNetworkJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a SearchNetworkJDialog is already open, this gets
                        // the focus
                        SearchNetworkJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.filterButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.filterButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.filterButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.filterButtonCoordinates.y + 39) {
                // the filter Clusters button will only be accessible when the
                // current network has been clustered
                if (data.networkClusters != null) {
                    if (FilterClustersJDialog.classInstance == null || !FilterClustersJDialog.classInstance.isShowing()) {
                        // if there has never been a FilterClustersJDialog or
                        // the last one was closed, a new one is created
                        new FilterClustersJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a FilterClustersJDialog is already open, this gets
                        // the focus
                        FilterClustersJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.overrepresentationAnalysisButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.overrepresentationAnalysisButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.overrepresentationAnalysisButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.overrepresentationAnalysisButtonCoordinates.y + 39) {
                // the over-representation analysis button will only be accessible when the
                // current network has been clustered
                if (data.networkClusters != null) {
                    if (OverrepresentationAnalysisJDialog.classInstance == null || !OverrepresentationAnalysisJDialog.classInstance.isShowing()) {
                        // if there has never been a OverrepresentationAnalysisJDialog or
                        // the last one was closed, a new one is created
                        new OverrepresentationAnalysisJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a OverrepresentationAnalysisJDialog is already open, this gets
                        // the focus
                        OverrepresentationAnalysisJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.heatMapButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.heatMapButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.heatMapButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.heatMapButtonCoordinates.y + 39) {
                // the heat map button will only be accessible when a network
                // has been created
                if (data.networkNodes != null) {
                    if (HeatMapJDialog.classInstance == null || !HeatMapJDialog.classInstance.isShowing()) {
                        // if there has never been a HeatMapJDialog or
                        // the last one was closed, a new one is created
                        new HeatMapJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a HeatMapJDialog is already open, this gets
                        // the focus
                        HeatMapJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.microarrayHeatMapButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.microarrayHeatMapButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.microarrayHeatMapButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.microarrayHeatMapButtonCoordinates.y + 39) {
                // the microarry heat map button will only be accessible when a network
                // has been created with microarray data
                if (data.networkNodes != null && data.microarrayData) {
                    if (MicroarrayHeatMapJDialog.classInstance == null || !MicroarrayHeatMapJDialog.classInstance.isShowing()) {
                        // if there has never been a MicroarrayHeatMapJDialog or
                        // the last one was closed, a new one is created
                        new MicroarrayHeatMapJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a MicroarrayHeatMapJDialog is already open, this gets
                        // the focus
                        MicroarrayHeatMapJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.dendrogramButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.dendrogramButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.dendrogramButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.dendrogramButtonCoordinates.y + 39) {
                // the dendrogram button will only be accessible when the
                // current network has been clustered
                if (data.networkClusters != null) {
                    if (DendrogramJDialog.classInstance == null || !DendrogramJDialog.classInstance.isShowing()) {
                        // if there has never been a DendrogramJDialog or
                        // the last one was closed, a new one is created
                        new DendrogramJDialog(JustclustJFrame.classInstance);
                    } else {
                        // if a DendrogramJDialog is already open, this gets
                        // the focus
                        DendrogramJDialog.classInstance.requestFocus();
                    }
                }
            }
            if (x >= JustclustJFrame.classInstance.justclustJPanel.managePluginsButtonCoordinates.x
                    && x <= JustclustJFrame.classInstance.justclustJPanel.managePluginsButtonCoordinates.x + 39
                    && y - 2 >= JustclustJFrame.classInstance.justclustJPanel.managePluginsButtonCoordinates.y
                    && y - 2 <= JustclustJFrame.classInstance.justclustJPanel.managePluginsButtonCoordinates.y + 39) {
                if (ManagePluginsJDialog.classInstance == null || !ManagePluginsJDialog.classInstance.isShowing()) {
                    // if there has never been a ManagePluginsJDialog or
                    // the last one was closed, a new one is created
                    new ManagePluginsJDialog(JustclustJFrame.classInstance);
                } else {
                    // if a ManagePluginsJDialog is already open, this gets
                    // the focus
                    ManagePluginsJDialog.classInstance.requestFocus();
                }
            }

        }

    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }
}
