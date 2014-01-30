/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.exportnetwork;

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
public class ExportNetworkHelpJDialog extends JDialog {

    public static ExportNetworkHelpJDialog classInstance;
    public JPanel exportNetworkHelpJPanel;
    public JEditorPane exportNetworkHelpJEditorPane;
    public JScrollPane exportNetworkHelpJScrollPane;
    ExportNetworkHelpComponentListener exportNetworkHelpComponentListener;

    public ExportNetworkHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Export Network Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        exportNetworkHelpJPanel = new JPanel();
        add(exportNetworkHelpJPanel);
        exportNetworkHelpJPanel.setLayout(null);

        exportNetworkHelpJEditorPane = new JEditorPane("text/html", "");
        exportNetworkHelpJEditorPane.setEditable(false);
        exportNetworkHelpJScrollPane = new JScrollPane(exportNetworkHelpJEditorPane);
        exportNetworkHelpJPanel.add(exportNetworkHelpJScrollPane);

        String details = "To export a network:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>File Name</b> text field and choose a file</li>"
                + "<li>Click on the <b>Export Network</b> button</li>"
                + "</ol>";
        exportNetworkHelpJEditorPane.setText(details);
        // this makes the scrollbar of the exportNetworkHelpJScrollPane start
        // at the top
        exportNetworkHelpJEditorPane.setCaretPosition(0);

        exportNetworkHelpComponentListener = new ExportNetworkHelpComponentListener();
        addComponentListener(exportNetworkHelpComponentListener);

        // the setBounds method must be called after the
        // exportNetworkHelpComponentListener is registered so that the
        // exportNetworkHelpJTextArea is always visible within the
        // exportNetworkHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        ExportNetworkHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the ExportNetworkHelpJDialog
        // appear
        setVisible(true);

    }
}
