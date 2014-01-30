/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networknodes;

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
public class NetworkNodesHelpJDialog extends JDialog {

    public static NetworkNodesHelpJDialog classInstance;
    public JPanel networkNodesHelpJPanel;
    public JEditorPane networkNodesHelpJEditorPane;
    public JScrollPane networkNodesHelpJScrollPane;
    NetworkNodesHelpComponentListener networkNodesHelpComponentListener;

    public NetworkNodesHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Network Nodes Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        networkNodesHelpJPanel = new JPanel();
        add(networkNodesHelpJPanel);
        networkNodesHelpJPanel.setLayout(null);

        networkNodesHelpJEditorPane = new JEditorPane("text/html", "");
        networkNodesHelpJEditorPane.setEditable(false);
        networkNodesHelpJScrollPane = new JScrollPane(networkNodesHelpJEditorPane);
        networkNodesHelpJPanel.add(networkNodesHelpJScrollPane);

        String details = "The label, visibility, and colour of a node can be changed."
                + "<br>"
                + "To change the visibility and colour of multiple nodes at once, left-click a row in the table and drag the mouse with the left-mouse button held down to select multiple rows.";
        networkNodesHelpJEditorPane.setText(details);
        // this makes the scrollbar of the networkNodesHelpJScrollPane start
        // at the top
        networkNodesHelpJEditorPane.setCaretPosition(0);

        networkNodesHelpComponentListener = new NetworkNodesHelpComponentListener();
        addComponentListener(networkNodesHelpComponentListener);

        // the setBounds method must be called after the
        // networkNodesHelpComponentListener is registered so that the
        // networkNodesHelpJTextArea is always visible within the
        // networkNodesHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkNodesHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NetworkNodesHelpJDialog
        // appear
        setVisible(true);

    }
}
