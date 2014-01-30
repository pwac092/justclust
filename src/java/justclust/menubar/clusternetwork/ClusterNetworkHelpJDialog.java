/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.clusternetwork;

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
public class ClusterNetworkHelpJDialog extends JDialog {

    public static ClusterNetworkHelpJDialog classInstance;
    public JPanel clusterNetworkHelpJPanel;
    public JEditorPane clusterNetworkHelpJEditorPane;
    public JScrollPane clusterNetworkHelpJScrollPane;
    ClusterNetworkHelpComponentListener clusterNetworkHelpComponentListener;

    public ClusterNetworkHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Cluster Network Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        clusterNetworkHelpJPanel = new JPanel();
        add(clusterNetworkHelpJPanel);
        clusterNetworkHelpJPanel.setLayout(null);

        clusterNetworkHelpJEditorPane = new JEditorPane("text/html", "");
        clusterNetworkHelpJEditorPane.setEditable(false);
        clusterNetworkHelpJScrollPane = new JScrollPane(clusterNetworkHelpJEditorPane);
        clusterNetworkHelpJPanel.add(clusterNetworkHelpJScrollPane);

        String details = "To cluster a network:"
                + "<ol>"
                + "<li>Click on the <b>Clustering Algorithm</b> combo-box and choose a clustering algorithm</li>"
                + "<li>Click on the <b>Cluster Network</b> button</li>"
                + "</ol>";
        clusterNetworkHelpJEditorPane.setText(details);
        // this makes the scrollbar of the clusterNetworkHelpJScrollPane start
        // at the top
        clusterNetworkHelpJEditorPane.setCaretPosition(0);

        clusterNetworkHelpComponentListener = new ClusterNetworkHelpComponentListener();
        addComponentListener(clusterNetworkHelpComponentListener);

        // the setBounds method must be called after the
        // ClusterNetworkHelpComponentListener is registered so that the
        // clusterNetworkHelpJTextArea is always visible within the
        // clusterNetworkHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        ClusterNetworkHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the ClusterNetworkJDialog
        // appear
        setVisible(true);

    }
}
