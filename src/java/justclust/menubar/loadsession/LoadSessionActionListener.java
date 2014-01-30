package justclust.menubar.loadsession;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import justclust.datastructures.Cluster;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.datastructures.Session;
import justclust.ButtonTabComponent;
import justclust.JustclustMouseListener;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.menubar.filefilters.CSVFileFilter;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.filterclusters.FilterClustersJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;
import justclust.toolbar.microarrayheatmap.MicroarrayHeatMapJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.overrepresentationanalysis.OverrepresentationAnalysisJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;

public class LoadSessionActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Load Session")) {

            LoadSessionJDialog.classInstance.loadSessionJButton.setEnabled(false);

            // this code starts a new thread to load a session.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // session is being loaded in the new thread.
            LoadSessionThread loadSessionThread = new LoadSessionThread();
            loadSessionThread.start();

        }

    }

    /**
     * This inner class has a method which loads a session with a new thread.
     */
    class LoadSessionThread extends Thread {

        /**
         * This method loads a session with a new thread.
         */
        public void run() {

            // the loadSessionJProgressBar is added to the
            // loadSessionDialogJPanel
            LoadSessionJDialog.classInstance.loadSessionDialogJPanel.add(LoadSessionJDialog.classInstance.loadSessionJProgressBar);

            // set the y coordinate of the LoadSessionJDialog so that it remains
            // centered around the point it currently is around when its height
            // is increased.
            // the difference in height between the new height and old height
            // is halved and taken away from the current y coordinate of the
            // LoadSessionJDialog.
            // also, increase the height of the LoadSessionJDialog.
            LoadSessionJDialog.classInstance.setBounds(
                    LoadSessionJDialog.classInstance.getLocation().x,
                    LoadSessionJDialog.classInstance.getLocation().y
                    - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    - LoadSessionJDialog.classInstance.loadSessionDialogJPanel.getHeight()) / 2),
                    LoadSessionJDialog.classInstance.getWidth(),
                    LoadSessionJDialog.classInstance.getInsets().top
                    + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    + LoadSessionJDialog.classInstance.getInsets().bottom);

            LoadSessionJDialog.classInstance.loadSessionJProgressBar.setIndeterminate(true);

            LoadSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Loading session...");

            Session session;

            try (InputStream fileInputStream = new FileInputStream(new File(LoadSessionJDialog.classInstance.fileNameJTextField.getText()));
                    InputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    ObjectInput objectInputStream = new ObjectInputStream(bufferedInputStream);) {

                // this loads the session saved in the file
                session = (Session) objectInputStream.readObject();

            } catch (IOException | NullPointerException | ClassNotFoundException exception) {

                LoadSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the loadSessionJProgressBar is removed from the
                // loadSessionDialogJPanel
                LoadSessionJDialog.classInstance.loadSessionDialogJPanel.remove(LoadSessionJDialog.classInstance.loadSessionJProgressBar);

                // set the y coordinate of the LoadSessionJDialog so that it remains
                // centered around the point it currently is around when its height
                // is decreased.
                // the difference in height between the new height and old height
                // is halved and added to the current y coordinate of the
                // LoadSessionJDialog.
                // also, decrease the height of the LoadSessionJDialog.
                LoadSessionJDialog.classInstance.setBounds(
                        LoadSessionJDialog.classInstance.getLocation().x,
                        LoadSessionJDialog.classInstance.getLocation().y
                        + Math.round((10 + 25) / 2),
                        LoadSessionJDialog.classInstance.getWidth(),
                        LoadSessionJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + LoadSessionJDialog.classInstance.getInsets().bottom);

                LoadSessionJDialog.classInstance.loadSessionJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Loading could not be completed due to error");

                return;

            }

            // remove any open dialogs
            if (NetworkDetailsJDialog.classInstance != null) {
                NetworkDetailsJDialog.classInstance.dispose();
            }
            if (NetworkNodesJDialog.classInstance != null) {
                NetworkNodesJDialog.classInstance.dispose();
            }
            if (NetworkEdgesJDialog.classInstance != null) {
                NetworkEdgesJDialog.classInstance.dispose();
            }
            if (NetworkClustersJDialog.classInstance != null) {
                NetworkClustersJDialog.classInstance.dispose();
            }
            if (SearchNetworkJDialog.classInstance != null) {
                SearchNetworkJDialog.classInstance.dispose();
            }
            if (FilterClustersJDialog.classInstance != null) {
                FilterClustersJDialog.classInstance.dispose();
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

            // populate all the relevant global variables with data from the
            // file specified by the user
            Data.data = session.data;
            for (int i = 0; i < JustclustJFrame.classInstance.customGraphEditors.size(); i++) {
                // the PanningAndZoomingThread is killed before the
                // Graph is removed.
                // this is because the PanningAndZoomingThread will throw an
                // error if it runs after the Graph is removed.
                JustclustJFrame.classInstance.customGraphEditors.get(i).panningAndZoomingThread.shouldRun = false;
                try {
                    JustclustJFrame.classInstance.customGraphEditors.get(i).panningAndZoomingThread.join();
                } catch (InterruptedException ex) {
                }
            }
            JustclustJFrame.classInstance.customGraphEditors.clear();
            JustclustJFrame.classInstance.justclustJTabbedPane.removeAll();
            JustclustJFrame.classInstance.tabTitles.clear();
            for (Data data : Data.data) {

                if (data.networkNodes != null) {
                    // iterate through each Node and create an ArrayList of Edges
                    // for the Node
                    for (Node node : data.networkNodes) {
                        node.edges = new ArrayList<Edge>();
                    }
                    // iterate through each Edge and, for each Edge, iterate
                    // through each Node.
                    // add the Edge to the Node's list of Edges.
                    for (Edge edge : data.networkEdges) {
                        edge.node1.edges.add(edge);
                        // check that the edge isn't a loop and therefore already added
                        if (edge.node1 != edge.node2) {
                            edge.node2.edges.add(edge);
                        }
                    }
                }

                if (data.networkClusters != null) {
                    // iterate through each Cluster and, for each Cluster, iterate
                    // through each Node.
                    // set the Node's cluster field to the current Cluster.
                    for (Cluster cluster : data.networkClusters) {
                        for (Node node : cluster.nodes) {
                            node.cluster = cluster;
                        }
                    }
                }

                // create a new graph to display a graphical representation of the
                // network
                CustomGraphEditor customGraphEditor = new CustomGraphEditor(data.networkNodes, data.networkEdges, data.networkClusters);
                JustclustJFrame.classInstance.customGraphEditors.add(customGraphEditor);
                // register the customGraphEditor with a
                // JustclustActionListener so that the
                // JustclustActionListener can respond to buttons on the main
                // graphical view being clicked
                JustclustMouseListener justclustMouseListener = new JustclustMouseListener();
                customGraphEditor.addMouseListener(justclustMouseListener);
                // passing an empty String to the setToolTipText method enables tooltips
                // for customGraphEditor
                customGraphEditor.setToolTipText("");
                int i = Data.data.indexOf(data);
                String title = session.tabTitles.get(i);
                if (title.length() > 15) {
                    title = title.substring(0, 12) + "...";
                }
                JustclustJFrame.classInstance.tabTitles.add(session.tabTitles.get(i));
                JustclustJFrame.classInstance.justclustJTabbedPane.addTab(title, customGraphEditor);
                int graphAmount = JustclustJFrame.classInstance.customGraphEditors.size();
                JustclustJFrame.classInstance.justclustJTabbedPane.setSelectedIndex(graphAmount - 1);
                ButtonTabComponent buttonTabComponent = new ButtonTabComponent(JustclustJFrame.classInstance.justclustJTabbedPane);
                JustclustJFrame.classInstance.justclustJTabbedPane.setTabComponentAt(graphAmount - 1, buttonTabComponent);
                JustclustJFrame.classInstance.justclustJTabbedPane.setToolTipTextAt(graphAmount - 1, session.tabTitles.get(i));

                // the createGraph method produces a graphical representation of the
                // current network for the Graph in the first tab
                customGraphEditor.createGraph();

                if (data.networkClusters != null) {
                    // this method creates a label for each cluster in the current network
                    customGraphEditor.createLabelsForClusters();
                }

                // lay out the nodes as they were when the session was
                // saved.
                // the shouldRepaint field is false while the layout is being
                // applied so that the customGraphEditor is not repainted during the
                // layout.
                // this would cause an error for some unkown reason.
                customGraphEditor.shouldRepaint = false;
                if (data.networkNodes != null) {
                    i = Data.data.indexOf(data);
                    for (int j = 0; j < data.networkNodes.size(); j++) {
                        data.networkNodes.get(j).setGraphicalNodeXCoordinate(session.nodeXCoordinates.get(i).get(j));
                        data.networkNodes.get(j).setGraphicalNodeYCoordinate(session.nodeYCoordinates.get(i).get(j));
                    }
                }
                customGraphEditor.shouldRepaint = true;

                // position the edges in the new layout
                ArrayList<PNode> edges = (ArrayList<PNode>) customGraphEditor.edgeLayer.getAllNodes();
                for (int j = 1; j < customGraphEditor.edgeLayer.getAllNodes().size(); j++) {
                    PPath edge = (PPath) edges.get(j);
                    PPath node1 = (PPath) edge.getAttribute("node1");
                    PPath node2 = (PPath) edge.getAttribute("node2");
//                    Point2D start = node1.getFullBoundsReference().getCenter2D();
//                    Point2D end = node2.getFullBoundsReference().getCenter2D();
//                    edge.reset();
//                    edge.moveTo((float) start.getX(), (float) start.getY());
//                    edge.lineTo((float) end.getX(), (float) end.getY());
                    edge.reset();
                    edge.moveTo((float) node1.getX() + 10, (float) node1.getY() + 10);
                    edge.lineTo((float) node2.getX() + 10, (float) node2.getY() + 10);
                }

                // the entire graph is fitted into the main graphical view
                final Rectangle2D drag_bounds = customGraphEditor.getCamera().getUnionOfLayerFullBounds();
                customGraphEditor.getCamera().animateViewToCenterBounds(drag_bounds, true, 0);

                // due to zooming the graph cannot be more than 2 times smaller or more
                // than 50 times bigger than its initial size
                customGraphEditor.customPZoomEventHandler.setMinScale(customGraphEditor.getCamera().getViewScale() / 2);
                customGraphEditor.customPZoomEventHandler.setMaxScale(customGraphEditor.getCamera().getViewScale() * 50);

                customGraphEditor.updateGraphLabels();
                customGraphEditor.updateGraphVisibility();
                customGraphEditor.updateGraphColour();

                // the repaint method updates the buttons on the toolbar so that
                // they are no longer disabled now that there is information about
                // the current network
                JustclustJFrame.classInstance.repaint();

            }

            // the repaint method updates the buttons on the toolbar so that
            // they are no longer disabled if there is now information about the
            // current network or clustering
            JustclustJFrame.classInstance.repaint();

            LoadSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            LoadSessionJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Session loaded");

        }
    }
}