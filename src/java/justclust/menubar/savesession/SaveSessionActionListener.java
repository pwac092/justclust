package justclust.menubar.savesession;

import edu.umd.cs.piccolo.util.PObjectOutputStream;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import justclust.datastructures.Cluster;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.datastructures.Session;
import justclust.menubar.filefilters.CSVFileFilter;
import justclust.plugins.configurationcontrols.FileSystemPathControl;

public class SaveSessionActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a
     * SaveSessionJDialog.
     */
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Save Session")) {

            SaveSessionJDialog.classInstance.saveSessionJButton.setEnabled(false);

            // this code starts a new thread to save the session.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // session is being saved in the new thread.
            SaveSessionThread saveSessionThread = new SaveSessionThread();
            saveSessionThread.start();

        }

    }

    class SaveSessionThread extends Thread {

        public void run() {

            // the saveSessionJProgressBar is added to the
            // saveSessionDialogJPanel
            SaveSessionJDialog.classInstance.saveSessionDialogJPanel.add(SaveSessionJDialog.classInstance.saveSessionJProgressBar);

            // set the y coordinate of the SaveSessionJDialog so that it remains
            // centered around the point it currently is around when its height
            // is increased.
            // the difference in height between the new height and old height
            // is halved and taken away from the current y coordinate of the
            // SaveSessionJDialog.
            // also, increase the height of the SaveSessionJDialog.
            SaveSessionJDialog.classInstance.setBounds(
                    SaveSessionJDialog.classInstance.getLocation().x,
                    SaveSessionJDialog.classInstance.getLocation().y
                    - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    - SaveSessionJDialog.classInstance.saveSessionDialogJPanel.getHeight()) / 2),
                    SaveSessionJDialog.classInstance.getWidth(),
                    SaveSessionJDialog.classInstance.getInsets().top
                    + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    + SaveSessionJDialog.classInstance.getInsets().bottom);

            SaveSessionJDialog.classInstance.saveSessionJProgressBar.setIndeterminate(true);

            SaveSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Saving session...");

            try (OutputStream fileOutputStream = new FileOutputStream(SaveSessionJDialog.classInstance.fileNameJTextField.getText());
                    OutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);) {

                // create a copy of everything in the current session and store
                // it in an instance of the Session class.
                // then write this session object to the file specified by the
                // user.
                // the reason that everything is copied is that some things
                // do not implement Serializable and so cannot be written to the
                // file.
                // these are not included in the copy and are added later, when
                // the file is read to load the session.
                Session session = new Session();
                ArrayList<Data> dataArrayList = new ArrayList<Data>();
                session.nodeXCoordinates = new ArrayList<ArrayList<Double>>();
                session.nodeYCoordinates = new ArrayList<ArrayList<Double>>();
                for (Data data : Data.data) {
                    Data dataCopy = new Data();
                    if (data.networkNodes != null) {
                        dataCopy.networkNodes = new ArrayList<Node>();
                        ArrayList<Double> nodeXCoordinates = new ArrayList<Double>();
                        ArrayList<Double> nodeYCoordinates = new ArrayList<Double>();
                        for (Node node : data.networkNodes) {
                            Node nodeCopy = new Node();
                            nodeCopy.label = node.label;
                            nodeCopy.visible = node.visible;
                            nodeCopy.colour = node.colour;
                            nodeCopy.data = dataCopy;
                            nodeCopy.microarrayValues = node.microarrayValues;
                            dataCopy.networkNodes.add(nodeCopy);
                            nodeXCoordinates.add(node.getGraphicalNodeXCoordinate());
                            nodeYCoordinates.add(node.getGraphicalNodeYCoordinate());
                        }
                        session.nodeXCoordinates.add(nodeXCoordinates);
                        session.nodeYCoordinates.add(nodeYCoordinates);
                    }
                    if (data.networkEdges != null) {
                        dataCopy.networkEdges = new ArrayList<Edge>();
                        for (Edge edge : data.networkEdges) {
                            Edge edgeCopy = new Edge();
                            edgeCopy.label = edge.label;
                            edgeCopy.visible = edge.visible;
                            edgeCopy.colour = edge.colour;
                            int i = data.networkNodes.indexOf(edge.node1);
                            edgeCopy.node1 = dataCopy.networkNodes.get(i);
                            i = data.networkNodes.indexOf(edge.node2);
                            edgeCopy.node2 = dataCopy.networkNodes.get(i);
                            edgeCopy.weight = edge.weight;
                            edgeCopy.data = dataCopy;
                            dataCopy.networkEdges.add(edgeCopy);
                        }
                    }
                    if (data.networkClusters != null) {
                        dataCopy.networkClusters = new ArrayList<Cluster>();
                        for (Cluster cluster : data.networkClusters) {
                            Cluster clusterCopy = new Cluster();
                            clusterCopy.label = cluster.label;
                            clusterCopy.nodes = new ArrayList<Node>();
                            for (Node node : cluster.nodes) {
                                int i = data.networkNodes.indexOf(node);
                                clusterCopy.nodes.add(dataCopy.networkNodes.get(i));
                            }
                            dataCopy.networkClusters.add(clusterCopy);
                        }
                    }
                    dataCopy.fileName = data.fileName;
                    dataCopy.fileParser = data.fileParser;
                    dataCopy.clusteringAlgorithm = data.clusteringAlgorithm;
                    dataCopy.timeTakenToCreateNetwork = data.timeTakenToCreateNetwork;
                    dataCopy.timeTakenToClusterNetwork = data.timeTakenToClusterNetwork;
                    dataCopy.timeTakenToApplyLayout = data.timeTakenToApplyLayout;
                    dataCopy.microarrayData = data.microarrayData;
                    dataCopy.microarrayHeaders = data.microarrayHeaders;
                    dataCopy.hierarchicalClustering = data.hierarchicalClustering;
                    dataCopy.rootDendrogramClusters = data.rootDendrogramClusters;
                    dataArrayList.add(dataCopy);
                }
                // the otherVersion fields for Nodes and Edges are copied.
                // this is done after all Nodes and Edges are copied so that
                // their copies exist.
                for (int i = 0; i < Data.data.size(); i++) {
                    for (int j = 0; j < Data.data.get(i).networkNodes.size(); j++) {
                        dataArrayList.get(i).networkNodes.get(j).otherVersions = new ArrayList<Node>();
                        for (Node node : Data.data.get(i).networkNodes.get(j).otherVersions) {
                            Data dataCopy = dataArrayList.get(Data.data.indexOf(node.data));
                            Node nodeCopy = dataCopy.networkNodes.get(node.data.networkNodes.indexOf(node));
                            dataArrayList.get(i).networkNodes.get(j).otherVersions.add(nodeCopy);
                        }
                    }
                    for (int j = 0; j < Data.data.get(i).networkEdges.size(); j++) {
                        dataArrayList.get(i).networkEdges.get(j).otherVersions = new ArrayList<Edge>();
                        for (Edge edge : Data.data.get(i).networkEdges.get(j).otherVersions) {
                            Data dataCopy = dataArrayList.get(Data.data.indexOf(edge.data));
                            Edge edgeCopy = dataCopy.networkEdges.get(edge.data.networkEdges.indexOf(edge));
                            dataArrayList.get(i).networkEdges.get(j).otherVersions.add(edgeCopy);
                        }
                    }
                }
                session.data = dataArrayList;
                session.tabTitles = JustclustJFrame.classInstance.tabTitles;
                objectOutputStream.writeObject(session);

            } catch (IOException | NullPointerException exception) {

                SaveSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the saveSessionJProgressBar is removed from the
                // saveSessionDialogJPanel
                SaveSessionJDialog.classInstance.saveSessionDialogJPanel.remove(SaveSessionJDialog.classInstance.saveSessionJProgressBar);

                // set the y coordinate of the SaveSessionJDialog so that it remains
                // centered around the point it currently is around when its height
                // is decrease.
                // the difference in height between the new height and old height
                // is halved and added to the current y coordinate of the
                // SaveSessionJDialog.
                // also, decrease the height of the SaveSessionJDialog.
                SaveSessionJDialog.classInstance.setBounds(
                        SaveSessionJDialog.classInstance.getLocation().x,
                        SaveSessionJDialog.classInstance.getLocation().y
                        + Math.round((10 + 25) / 2),
                        SaveSessionJDialog.classInstance.getWidth(),
                        SaveSessionJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + SaveSessionJDialog.classInstance.getInsets().bottom);

                SaveSessionJDialog.classInstance.saveSessionJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Saving could not be completed due to error");

                return;

            }

            SaveSessionJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            SaveSessionJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Session saved");

        }
    }
}