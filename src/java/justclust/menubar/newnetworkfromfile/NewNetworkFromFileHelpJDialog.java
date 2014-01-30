/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.newnetworkfromfile;

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
public class NewNetworkFromFileHelpJDialog extends JDialog {

    public static NewNetworkFromFileHelpJDialog classInstance;
    public JPanel newNetworkFromFileHelpJPanel;
    public JEditorPane newNetworkFromFileHelpJEditorPane;
    public JScrollPane newNetworkFromFileHelpJScrollPane;
    NewNetworkFromFileHelpComponentListener newNetworkFromFileHelpComponentListener;

    public NewNetworkFromFileHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("New Network from File Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        newNetworkFromFileHelpJPanel = new JPanel();
        add(newNetworkFromFileHelpJPanel);
        newNetworkFromFileHelpJPanel.setLayout(null);

        newNetworkFromFileHelpJEditorPane = new JEditorPane("text/html", "");
        newNetworkFromFileHelpJEditorPane.setEditable(false);
        newNetworkFromFileHelpJScrollPane = new JScrollPane(newNetworkFromFileHelpJEditorPane);
        newNetworkFromFileHelpJPanel.add(newNetworkFromFileHelpJScrollPane);

        String details = "To create a new network from a file:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Input File</b> text field and choose a file</li>"
                + "<li>Click on the <b>File Parser</b> combo-box and choose a file parser</li>"
                + "<li>Click on the <b>Create Network</b> button</li>"
                + "</ol>";
        newNetworkFromFileHelpJEditorPane.setText(details);
        // this makes the scrollbar of the newNetworkFromFileHelpJScrollPane start
        // at the top
        newNetworkFromFileHelpJEditorPane.setCaretPosition(0);

        newNetworkFromFileHelpComponentListener = new NewNetworkFromFileHelpComponentListener();
        addComponentListener(newNetworkFromFileHelpComponentListener);

        // the setBounds method must be called after the
        // NewNetworkFromFileHelpComponentListener is registered so that the
        // newNetworkFromFileHelpJTextArea is always visible within the
        // newNetworkFromFileHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NewNetworkFromFileHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NewNetworkFromFileHelpJDialog
        // appear
        setVisible(true);

    }
}
