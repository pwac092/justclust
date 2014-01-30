/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.overrepresentationanalysis;

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
public class OverrepresentationAnalysisHelpJDialog extends JDialog {

    public static OverrepresentationAnalysisHelpJDialog classInstance;
    public JPanel overrepresentationAnalysisHelpJPanel;
    public JEditorPane overrepresentationAnalysisHelpJEditorPane;
    public JScrollPane overrepresentationAnalysisHelpJScrollPane;
    OverrepresentationAnalysisHelpComponentListener overrepresentationAnalysisHelpComponentListener;

    public OverrepresentationAnalysisHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Over-representation Analysis Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        overrepresentationAnalysisHelpJPanel = new JPanel();
        add(overrepresentationAnalysisHelpJPanel);
        overrepresentationAnalysisHelpJPanel.setLayout(null);

        overrepresentationAnalysisHelpJEditorPane = new JEditorPane("text/html", "");
        overrepresentationAnalysisHelpJEditorPane.setEditable(false);
        overrepresentationAnalysisHelpJScrollPane = new JScrollPane(overrepresentationAnalysisHelpJEditorPane);
        overrepresentationAnalysisHelpJPanel.add(overrepresentationAnalysisHelpJScrollPane);

        String details = "To analyse over-representation for a cluster:"
                + "<ol>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Gene Ontology</b> text field and choose a file</li>"
                + "<li>Click on the browse button (folder icon) to the right of the <b>Gene Ontology Annotations</b> text field and choose a file</li>"
                + "<li>Click on the <b>Cluster to Analyse</b> table to chose a cluster</li>"
                + "<li>Click on the <b>Analyse Over-representation</b> button</li>"
                + "</ol>";
        overrepresentationAnalysisHelpJEditorPane.setText(details);
        // this makes the scrollbar of the overrepresentationAnalysisHelpJScrollPane start
        // at the top
        overrepresentationAnalysisHelpJEditorPane.setCaretPosition(0);

        overrepresentationAnalysisHelpComponentListener = new OverrepresentationAnalysisHelpComponentListener();
        addComponentListener(overrepresentationAnalysisHelpComponentListener);

        // the setBounds method must be called after the
        // overrepresentationAnalysisHelpComponentListener is registered so that the
        // overrepresentationAnalysisHelpJTextArea is always visible within the
        // overrepresentationAnalysisHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        OverrepresentationAnalysisHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the OverrepresentationAnalysisHelpJDialog
        // appear
        setVisible(true);

    }
}
