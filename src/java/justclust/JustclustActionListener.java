package justclust;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
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
import justclust.menubar.about.AboutJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;

public class JustclustActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a JMenu.
     */
    // each of the Strings in the if statements corresponds to a menu option
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("New Network from File...")) {
            new NewNetworkFromFileJDialog();
        }

        if (actionEvent.getActionCommand().equals("Cluster Network...")) {

            // This code creates a ClusterNetworkJDialog within the event
            // dispatch thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ClusterNetworkJDialog();
                }
            });

        }

        // unclustering the network is no longer an option because clusterings
        // are now separate from the network
//        if (actionEvent.getActionCommand().equals("Uncluster Network")) {
//
//            // the list of clusters for each node is cset to null
//            for (Node node : Data.classInstance.networkNodes) {
//                node.clusters = null;
//            }
//
//            // the networkClusters field of the Data class represents whether
//            // the network has been clustered.
//            // null means that it hasn't.
//            Data.classInstance.networkClusters = null;
//
//            // this method shows the current clustering by making all the nodes and
//            // edges visible except for edges which connect nodes which do not belong to
//            // the same cluster
//            JustclustJFrame.classInstance.customGraphEditor.showClusteringWithNodeAndEdgeVisibility();
//            
//            // disable menu items which were enabled but should now be
//            // inaccessible
//            JustclustJFrame.classInstance.saveClusteringJMenuItem.setEnabled(false);
//            JustclustJFrame.classInstance.unclusterNetworkJMenuItem.setEnabled(false);
//
//        }

        if (actionEvent.getActionCommand().equals("Exit")) {
            System.exit(0);
        }

        if (actionEvent.getActionCommand().equals("Apply Layout...")) {
            new ApplyLayoutJDialog();
        }

        if (actionEvent.getActionCommand().equals("About...")) {
            new AboutJDialog();
        }

    }
}
