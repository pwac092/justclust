/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.exportclustering;

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
public class ExportClusteringHelpJDialog extends JDialog {

    public static ExportClusteringHelpJDialog classInstance;
    public JPanel exportClusteringHelpJPanel;
    public JEditorPane exportClusteringHelpJEditorPane;
    public JScrollPane exportClusteringHelpJScrollPane;
    ExportClusteringHelpComponentListener exportClusteringHelpComponentListener;

    public ExportClusteringHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Export Clustering Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        exportClusteringHelpJPanel = new JPanel();
        add(exportClusteringHelpJPanel);
        exportClusteringHelpJPanel.setLayout(null);

        exportClusteringHelpJEditorPane = new JEditorPane("text/html", "");
        exportClusteringHelpJEditorPane.setEditable(false);
        exportClusteringHelpJScrollPane = new JScrollPane(exportClusteringHelpJEditorPane);
        exportClusteringHelpJPanel.add(exportClusteringHelpJScrollPane);

        String details = "To export a clustering:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>File Name</b> text field and choose a file</li>"
                + "<li>Click on the <b>Export Clustering</b> button</li>"
                + "</ol>";
        exportClusteringHelpJEditorPane.setText(details);
        // this makes the scrollbar of the exportClusteringHelpJScrollPane start
        // at the top
        exportClusteringHelpJEditorPane.setCaretPosition(0);

        exportClusteringHelpComponentListener = new ExportClusteringHelpComponentListener();
        addComponentListener(exportClusteringHelpComponentListener);

        // the setBounds method must be called after the
        // exportClusteringHelpComponentListener is registered so that the
        // exportClusteringHelpJTextArea is always visible within the
        // exportClusteringHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        ExportClusteringHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the ExportClusteringHelpJDialog
        // appear
        setVisible(true);

    }
}
