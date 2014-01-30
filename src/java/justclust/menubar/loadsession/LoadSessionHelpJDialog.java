/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.loadsession;

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
public class LoadSessionHelpJDialog extends JDialog {

    public static LoadSessionHelpJDialog classInstance;
    public JPanel loadSessionHelpJPanel;
    public JEditorPane loadSessionHelpJEditorPane;
    public JScrollPane loadSessionHelpJScrollPane;
    LoadSessionHelpComponentListener loadSessionHelpComponentListener;

    public LoadSessionHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Load Session Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        loadSessionHelpJPanel = new JPanel();
        add(loadSessionHelpJPanel);
        loadSessionHelpJPanel.setLayout(null);

        loadSessionHelpJEditorPane = new JEditorPane("text/html", "");
        loadSessionHelpJEditorPane.setEditable(false);
        loadSessionHelpJScrollPane = new JScrollPane(loadSessionHelpJEditorPane);
        loadSessionHelpJPanel.add(loadSessionHelpJScrollPane);

        String details = "To load a Session:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>File Name</b> text field and choose a file</li>"
                + "<li>Click on the <b>Load Session</b> button</li>"
                + "</ol>";
        loadSessionHelpJEditorPane.setText(details);
        // this makes the scrollbar of the loadSessionHelpJScrollPane start
        // at the top
        loadSessionHelpJEditorPane.setCaretPosition(0);

        loadSessionHelpComponentListener = new LoadSessionHelpComponentListener();
        addComponentListener(loadSessionHelpComponentListener);

        // the setBounds method must be called after the
        // LoadSessionHelpComponentListener is registered so that the
        // loadSessionHelpJTextArea is always visible within the
        // loadSessionHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        LoadSessionHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the LoadSessionHelpJDialog
        // appear
        setVisible(true);

    }
}