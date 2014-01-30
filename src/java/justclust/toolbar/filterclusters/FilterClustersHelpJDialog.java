/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.filterclusters;

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
public class FilterClustersHelpJDialog extends JDialog {

    public static FilterClustersHelpJDialog classInstance;
    public JPanel filterClustersHelpJPanel;
    public JEditorPane filterClustersHelpJEditorPane;
    public JScrollPane filterClustersHelpJScrollPane;
    FilterClustersHelpComponentListener filterClustersHelpComponentListener;

    public FilterClustersHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Filter Clusters Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        filterClustersHelpJPanel = new JPanel();
        add(filterClustersHelpJPanel);
        filterClustersHelpJPanel.setLayout(null);

        filterClustersHelpJEditorPane = new JEditorPane("text/html", "");
        filterClustersHelpJEditorPane.setEditable(false);
        filterClustersHelpJScrollPane = new JScrollPane(filterClustersHelpJEditorPane);
        filterClustersHelpJPanel.add(filterClustersHelpJScrollPane);

        String details = "To filter clusters:"
                + "<ol>"
                + "<li>Input a number into any of the text fields</li>"
                + "<li>Click on the <b>Filter Clusters</b> button</li>"
                + "</ol>"
                + "Text field descriptions:"
                + "<table border=\"1\">"
                + "<tr>"
                + "<td><b>Show Largest Clusters</b></td>"
                + "<td>if the number input is x, the x largest clusters will be made visible</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Hide Smallest Clusters</b></td>"
                + "<td>if the number input is x, the x smallest clusters will be made invisible</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Hide Clusters Above Node Amount</b></td>"
                + "<td>if the number input is x, all clusters containing more than x nodes will be made invisible</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Hide Clusters Below Node Amount</b></td>"
                + "<td>if the number input is x, all clusters containing less than x nodes will be made invisible</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Hide Clusters Below Density Threshold</b></td>"
                + "<td>if the amount of nodes which are contained in a cluster is x, and the amount of edges which are internal to the cluster is y, the density of the cluster is y / (x * (x – 1) / 2). if the number input is z, all clusters with a density less than z will be made invisible</td>"
                + "</tr>"
                + "</table>";
        filterClustersHelpJEditorPane.setText(details);
        // this makes the scrollbar of the filterClustersHelpJScrollPane start
        // at the top
        filterClustersHelpJEditorPane.setCaretPosition(0);

        filterClustersHelpComponentListener = new FilterClustersHelpComponentListener();
        addComponentListener(filterClustersHelpComponentListener);

        // the setBounds method must be called after the
        // filterClustersHelpComponentListener is registered so that the
        // filterClustersHelpJTextArea is always visible within the
        // filterClustersHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        FilterClustersHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the FilterClustersHelpJDialog
        // appear
        setVisible(true);

    }
}
