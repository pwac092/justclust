/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.searchnetwork;

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
public class SearchNetworkHelpJDialog extends JDialog {

    public static SearchNetworkHelpJDialog classInstance;
    public JPanel searchNetworkHelpJPanel;
    public JEditorPane searchNetworkHelpJEditorPane;
    public JScrollPane searchNetworkHelpJScrollPane;
    SearchNetworkHelpComponentListener searchNetworkHelpComponentListener;

    public SearchNetworkHelpJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Search Network Help");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        searchNetworkHelpJPanel = new JPanel();
        add(searchNetworkHelpJPanel);
        searchNetworkHelpJPanel.setLayout(null);

        searchNetworkHelpJEditorPane = new JEditorPane("text/html", "");
        searchNetworkHelpJEditorPane.setEditable(false);
        searchNetworkHelpJScrollPane = new JScrollPane(searchNetworkHelpJEditorPane);
        searchNetworkHelpJPanel.add(searchNetworkHelpJScrollPane);

        String details = "To search a network:"
                + "<ol>"
                + "<li>Input a search term into the <b>Search</b> text field</li>"
                + "<li>Click on the <b>Search</b> button</li>"
                + "</ol>"
                + "The results will contain nodes, edges, and clusters which are associated in some way with a label which contains the search term you input.";
        searchNetworkHelpJEditorPane.setText(details);
        // this makes the scrollbar of the saveNetworkHelpJScrollPane start
        // at the top
        searchNetworkHelpJEditorPane.setCaretPosition(0);

        searchNetworkHelpComponentListener = new SearchNetworkHelpComponentListener();
        addComponentListener(searchNetworkHelpComponentListener);

        // the setBounds method must be called after the
        // searchNetworkHelpComponentListener is registered so that the
        // searchNetworkHelpJTextArea is always visible within the
        // searchNetworkHelpJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        SearchNetworkHelpJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 250) / 2),
                500,
                250);

        // the setVisible method is called to make the SearchNetworkHelpJDialog
        // appear
        setVisible(true);

    }
}
