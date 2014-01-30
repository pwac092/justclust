/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.applylayout;

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
public class ApplyLayoutHelpJDialog extends JDialog {

    public static ApplyLayoutHelpJDialog classInstance;
    public JPanel applyLayoutHelpJPanel;
    public JEditorPane applyLayoutHelpJEditorPane;
    public JScrollPane applyLayoutHelpJScrollPane;
    ApplyLayoutHelpComponentListener applyLayoutHelpComponentListener;

    public ApplyLayoutHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Apply Layout Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        applyLayoutHelpJPanel = new JPanel();
        add(applyLayoutHelpJPanel);
        applyLayoutHelpJPanel.setLayout(null);

        applyLayoutHelpJEditorPane = new JEditorPane("text/html", "");
        applyLayoutHelpJEditorPane.setEditable(false);
        applyLayoutHelpJScrollPane = new JScrollPane(applyLayoutHelpJEditorPane);
        applyLayoutHelpJPanel.add(applyLayoutHelpJScrollPane);

        String details = "To apply a layout:"
                + "<ol>"
                + "<li>Click on the <b>Visualisation Layout</b> combo-box and choose a visualisation layout</li>"
                + "<li>Click on the <b>Apply Layout</b> button</li>"
                + "</ol>";
        applyLayoutHelpJEditorPane.setText(details);
        // this makes the scrollbar of the applyLayoutHelpJScrollPane
        // start at the top
        applyLayoutHelpJEditorPane.setCaretPosition(0);

        applyLayoutHelpComponentListener = new ApplyLayoutHelpComponentListener();
        addComponentListener(applyLayoutHelpComponentListener);

        // the setBounds method must be called after the
        // ApplyLayoutHelpComponentListener is registered so that the
        // applyLayoutHelpJTextArea is always visible within the
        // applyLayoutHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        ApplyLayoutHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the NetworkDetailsJDialog
        // appear
        setVisible(true);

    }
}
