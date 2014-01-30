/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.about;

import java.awt.Dialog;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
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
public class AboutJDialog extends JDialog {

    public static AboutJDialog classInstance;
    public AboutJPanel aboutJPanel;
    public JTextArea aboutJTextArea;
    AboutComponentListener aboutComponentListener;

    public AboutJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("About");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        aboutJPanel = new AboutJPanel();
        add(aboutJPanel);
        aboutJPanel.setLayout(null);

        aboutJTextArea = new JTextArea();
        aboutJTextArea.setEditable(false);
        aboutJTextArea.setOpaque(false);
        aboutJPanel.add(aboutJTextArea);

        String details = "JustClust is a tool for analysing biological data with cluster analysis."
                + ' '
                + "JustClust can handle many formats of data and cluster the data with many state-of-the-art techniques."
                + ' '
                + "The aim of JustClust is to provide an easy-to-use application which can perform any analysis on any data."
                + '\n'
                + '\n'
                + "Version: 1.0"
                + '\n'
                + "Author: Kit Lawes"
                + '\n'
                + "Year: 2013"
                + '\n'
                + "Contact: support@paccanarolab.org"
                + '\n'
                + "Website: www.paccanarolab.org";
        aboutJTextArea.setText(details);
        aboutJTextArea.setLineWrap(true);
        aboutJTextArea.setWrapStyleWord(true);
        // this makes the scrollbar of the aboutJScrollPane
        // start at the top
        aboutJTextArea.setCaretPosition(0);

        aboutComponentListener = new AboutComponentListener();
        addComponentListener(aboutComponentListener);

        // the setBounds method must be called after the
        // AboutComponentListener is registered so that the
        // aboutJTextArea is always visible within the
        // aboutJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        AboutJDialog.classInstance.setBounds(
                (int) Math.round(((double) devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round(((double) devices[0].getDisplayMode().getHeight() - 500) / 2),
                500,
                500);

        // the setVisible method is called to make the AboutJDialog
        // appear
        setVisible(true);

    }
}
