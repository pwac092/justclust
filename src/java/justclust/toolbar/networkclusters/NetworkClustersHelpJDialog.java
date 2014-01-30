/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkclusters;

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
public class NetworkClustersHelpJDialog extends JDialog {

    public static NetworkClustersHelpJDialog classInstance;
    public JPanel networkClustersHelpJPanel;
    public JEditorPane networkClustersHelpJEditorPane;
    public JScrollPane networkClustersHelpJScrollPane;
    NetworkClustersHelpComponentListener networkClustersHelpComponentListener;

    public NetworkClustersHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Network Clusters Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        networkClustersHelpJPanel = new JPanel();
        add(networkClustersHelpJPanel);
        networkClustersHelpJPanel.setLayout(null);

        networkClustersHelpJEditorPane = new JEditorPane("text/html", "");
        networkClustersHelpJEditorPane.setEditable(false);
        networkClustersHelpJScrollPane = new JScrollPane(networkClustersHelpJEditorPane);
        networkClustersHelpJPanel.add(networkClustersHelpJScrollPane);

        String details = "The label, node visibility, edge visibility, node colour, and edge colour of a cluster can be changed."
                + "<br>"
                + "To change the node visibility, edge visibility, node colour, and edge colour of multiple clusters at once, left-click a row in the table and drag the mouse with the left-mouse button held down to select multiple rows.";
        networkClustersHelpJEditorPane.setText(details);
        // this makes the scrollbar of the networkClustersHelpJScrollPane start
        // at the top
        networkClustersHelpJEditorPane.setCaretPosition(0);

        networkClustersHelpComponentListener = new NetworkClustersHelpComponentListener();
        addComponentListener(networkClustersHelpComponentListener);

        // the setBounds method must be called after the
        // networkClustersHelpComponentListener is registered so that the
        // networkClustersHelpJTextArea is always visible within the
        // networkClustersHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkClustersHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NetworkClustersHelpJDialog
        // appear
        setVisible(true);

    }
}
