/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.savesession;

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
public class SaveSessionHelpJDialog extends JDialog {

    public static SaveSessionHelpJDialog classInstance;
    public JPanel saveSessionHelpJPanel;
    public JEditorPane saveSessionHelpJEditorPane;
    public JScrollPane saveSessionHelpJScrollPane;
    SaveSessionHelpComponentListener saveSessionHelpComponentListener;

    public SaveSessionHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Save Session Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        saveSessionHelpJPanel = new JPanel();
        add(saveSessionHelpJPanel);
        saveSessionHelpJPanel.setLayout(null);

        saveSessionHelpJEditorPane = new JEditorPane("text/html", "");
        saveSessionHelpJEditorPane.setEditable(false);
        saveSessionHelpJScrollPane = new JScrollPane(saveSessionHelpJEditorPane);
        saveSessionHelpJPanel.add(saveSessionHelpJScrollPane);

        String details = "To save a session:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>File Name</b> text field and choose a file</li>"
                + "<li>Click on the <b>Save Session</b> button</li>"
                + "</ol>";
        saveSessionHelpJEditorPane.setText(details);
        // this makes the scrollbar of the saveSessionHelpJScrollPane start
        // at the top
        saveSessionHelpJEditorPane.setCaretPosition(0);

        saveSessionHelpComponentListener = new SaveSessionHelpComponentListener();
        addComponentListener(saveSessionHelpComponentListener);

        // the setBounds method must be called after the
        // saveSessionHelpComponentListener is registered so that the
        // saveSessionHelpJTextArea is always visible within the
        // saveSessionHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        SaveSessionHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the SaveSessionHelpJDialog
        // appear
        setVisible(true);

    }
}