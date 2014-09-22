/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
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
public class MicroarrayHeatMapHelpJDialog extends JDialog {

    public static MicroarrayHeatMapHelpJDialog classInstance;
    public JPanel jPanel;
    public JEditorPane jEditorPane;
    public JScrollPane jScrollPane;
    MicroarrayHeatMapHelpComponentListener heatMapHelpComponentListener;

    public MicroarrayHeatMapHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Heat Map Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        jPanel = new JPanel();
        add(jPanel);
        jPanel.setLayout(null);

        jEditorPane = new JEditorPane("text/html", "");
        jEditorPane.setEditable(false);
        jScrollPane = new JScrollPane(jEditorPane);
        jPanel.add(jScrollPane);

        String details = "The colours in the heat map represent the values in the microarray data."
                + "<br>"
                + "Red represents a high value."
                + "<br>"
                + "Green represent a low value.";
        jEditorPane.setText(details);
        // this makes the scrollbar of the jScrollPane start at the top
        jEditorPane.setCaretPosition(0);

        heatMapHelpComponentListener = new MicroarrayHeatMapHelpComponentListener();
        addComponentListener(heatMapHelpComponentListener);

        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        MicroarrayHeatMapHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the MicroarrayHeatMapHelpJDialog
        // appear
        setVisible(true);

    }
}
