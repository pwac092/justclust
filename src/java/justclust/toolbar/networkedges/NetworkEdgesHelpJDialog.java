/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkedges;

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
public class NetworkEdgesHelpJDialog extends JDialog {

    public static NetworkEdgesHelpJDialog classInstance;
    public JPanel networkEdgesHelpJPanel;
    public JEditorPane networkEdgesHelpJEditorPane;
    public JScrollPane networkEdgesHelpJScrollPane;
    NetworkEdgesHelpComponentListener networkEdgesHelpComponentListener;

    public NetworkEdgesHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Network Edges Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        networkEdgesHelpJPanel = new JPanel();
        add(networkEdgesHelpJPanel);
        networkEdgesHelpJPanel.setLayout(null);

        networkEdgesHelpJEditorPane = new JEditorPane("text/html", "");
        networkEdgesHelpJEditorPane.setEditable(false);
        networkEdgesHelpJScrollPane = new JScrollPane(networkEdgesHelpJEditorPane);
        networkEdgesHelpJPanel.add(networkEdgesHelpJScrollPane);

        String details = "The label, visibility, and colour of an edge can be changed."
                + "<br>"
                + "To change the visibility and colour of multiple edges at once, left-click a row in the table and drag the mouse with the left-mouse button held down to select multiple rows.";
        networkEdgesHelpJEditorPane.setText(details);
        // this makes the scrollbar of the networkEdgesHelpJScrollPane start
        // at the top
        networkEdgesHelpJEditorPane.setCaretPosition(0);

        networkEdgesHelpComponentListener = new NetworkEdgesHelpComponentListener();
        addComponentListener(networkEdgesHelpComponentListener);

        // the setBounds method must be called after the
        // networkEdgesHelpComponentListener is registered so that the
        // networkEdgesHelpJTextArea is always visible within the
        // networkEdgesHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkEdgesHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NetworkEdgesHelpJDialog
        // appear
        setVisible(true);

    }
}
