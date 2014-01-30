/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkdetails;

import java.awt.Dialog;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import justclust.datastructures.Cluster;
import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.JustclustJFrame;
import justclust.toolbar.networkdetails.NetworkDetailsComponentListener;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import static justclust.toolbar.networkdetails.NetworkDetailsJDialog.classInstance;

/**
 *
 * @author wuaz008
 */
public class NetworkDetailsHelpJDialog extends JDialog {

    public static NetworkDetailsHelpJDialog classInstance;
    public JPanel networkDetailsHelpJPanel;
    public JEditorPane networkDetailsHelpJEditorPane;
    public JScrollPane networkDetailsHelpJScrollPane;
    NetworkDetailsHelpComponentListener networkDetailsHelpComponentListener;

    public NetworkDetailsHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Network Details Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        networkDetailsHelpJPanel = new JPanel();
        add(networkDetailsHelpJPanel);
        networkDetailsHelpJPanel.setLayout(null);

        networkDetailsHelpJEditorPane = new JEditorPane("text/html", "");
        networkDetailsHelpJEditorPane.setEditable(false);
        networkDetailsHelpJScrollPane = new JScrollPane(networkDetailsHelpJEditorPane);
        networkDetailsHelpJPanel.add(networkDetailsHelpJScrollPane);

        String details = "Term descriptions:"
                + "<table border=\"1\">"
                + "<tr>"
                + "<td><b>File</b></td>"
                + "<td>the input file used to create the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>File parser</b></td>"
                + "<td>the file parser used to create the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Time taken to create network</b></td>"
                + "<td>the time taken to create the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Nodes</b></td>"
                + "<td>the amount of nodes in the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Edges</b></td>"
                + "<td>the amount of edges in the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Average edges per node</b></td>"
                + "<td>if x is the amount of edges connected to a specific node, this is the average x for all nodes</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Smallest edge weight</b></td>"
                + "<td>the smallest weight of an edge</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Largest edge weight</b></td>"
                + "<td>the largest weight of an edge</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Average edge weight</b></td>"
                + "<td>the average weight of an edge</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Clustering algorithm</b></td>"
                + "<td>the clustering algorithm used to cluster the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Time taken to create clustering</b></td>"
                + "<td>the time taken to cluster the current network</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Clusters</b></td>"
                + "<td>the amount of clusters in the current clustering</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Average nodes per cluster</b></td>"
                + "<td>if x is the amount of nodes contained in a specific cluster, this is the average x for all clusters</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Internal edges</b></td>"
                + "<td>the amount of edges which connect two nodes which both belong to the same cluster</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>External edges</b></td>"
                + "<td>the amount of edges which connect two nodes which do not both belong to the same cluster</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Average internal edges per cluster</b></td>"
                + "<td>if x is the amount of edges which are internal to a specific cluster, this is the average x for all clusters</td>"
                + "</tr>"
                + "</table>";
        networkDetailsHelpJEditorPane.setText(details);
        // this makes the scrollbar of the networkDetailsHelpJScrollPane start
        // at the top
        networkDetailsHelpJEditorPane.setCaretPosition(0);

        networkDetailsHelpComponentListener = new NetworkDetailsHelpComponentListener();
        addComponentListener(networkDetailsHelpComponentListener);

        // the setBounds method must be called after the
        // networkDetailsHelpComponentListener is registered so that the
        // networkDetailsHelpJTextArea is always visible within the
        // networkDetailsHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkDetailsHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NetworkDetailsHelpJDialog
        // appear
        setVisible(true);

    }
}
