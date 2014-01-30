/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.manageplugins;

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
public class ManagePluginsHelpJDialog extends JDialog {

    public static ManagePluginsHelpJDialog classInstance;
    public JPanel managePluginsHelpJPanel;
    public JEditorPane managePluginsHelpJEditorPane;
    public JScrollPane managePluginsHelpJScrollPane;
    ManagePluginsHelpComponentListener managePluginsHelpComponentListener;

    public ManagePluginsHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Manage Plug-ins Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        managePluginsHelpJPanel = new JPanel();
        add(managePluginsHelpJPanel);
        managePluginsHelpJPanel.setLayout(null);

        managePluginsHelpJEditorPane = new JEditorPane("text/html", "");
        managePluginsHelpJEditorPane.setEditable(false);
        managePluginsHelpJScrollPane = new JScrollPane(managePluginsHelpJEditorPane);
        managePluginsHelpJPanel.add(managePluginsHelpJScrollPane);

        String details = "To load parsing plug-ins:"
                + "<ol>"
                + "<li>Click on the <b>File Parsers</b> tab</li>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Parsing Plug-ins Path</b> text field and choose a folder which contains the parsing plug-ins you want to load</li>"
                + "<li>Click on the <b>Load Parsing Plug-ins</b> button</li>"
                + "<li>Click on the <b>Loaded Parsing Plug-ins</b> combo-box to see the parsing plug-ins which have been loaded</li>"
                + "</ol>"
                + "To load clustering plug-ins:"
                + "<ol>"
                + "<li>Click on the <b>Clustering Algorithms</b> tab</li>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Clustering Plug-ins Path</b> text field and choose a folder which contains the clustering plug-ins you want to load</li>"
                + "<li>Click on the <b>Load Clustering Plug-ins</b> button</li>"
                + "<li>Click on the <b>Loaded Clustering Plug-ins</b> combo-box to see the clustering plug-ins which have been loaded</li>"
                + "</ol>"
                + "To load visualisation plug-ins:"
                + "<ol>"
                + "<li>Click on the <b>Visualisation Layouts</b> tab</li>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Visualisation Plug-ins Path</b> text field and choose a folder which contains the visualisation plug-ins you want to load</li>"
                + "<li>Click on the <b>Load Visualisation Plug-ins</b> button</li>"
                + "<li>Click on the <b>Loaded Visualisation Plug-ins</b> combo-box to see the visualisation plug-ins which have been loaded</li>"
                + "</ol>";
        managePluginsHelpJEditorPane.setText(details);
        // this makes the scrollbar of the managePluginsHelpJScrollPane start
        // at the top
        managePluginsHelpJEditorPane.setCaretPosition(0);

        managePluginsHelpComponentListener = new ManagePluginsHelpComponentListener();
        addComponentListener(managePluginsHelpComponentListener);

        // the setBounds method must be called after the
        // managePluginsHelpComponentListener is registered so that the
        // managePluginsHelpJTextArea is always visible within the
        // managePluginsHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        ManagePluginsHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the ManagePluginsHelpJDialog
        // appear
        setVisible(true);

    }
}
