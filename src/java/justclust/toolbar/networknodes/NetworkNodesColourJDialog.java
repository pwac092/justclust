/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networknodes;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author wuaz008
 */
public class NetworkNodesColourJDialog extends JDialog {

    public static NetworkNodesColourJDialog classInstance;
    public JPanel jPanel;
    public JColorChooser jColorChooser;
    public JButton okJButton;
    public JButton cancelJButton;
    public JButton randomJButton;

    NetworkNodesColourJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Colour Chooser");
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        setBounds(
                (int) Math.round((double) devices[0].getDisplayMode().getWidth() / 3),
                (int) Math.round((double) devices[0].getDisplayMode().getHeight() / 3),
                (int) Math.round((double) devices[0].getDisplayMode().getWidth() / 3),
                (int) Math.round((double) devices[0].getDisplayMode().getHeight() / 3));
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        jPanel = new JPanel();
        jPanel.setLayout(null);
        add(jPanel);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        NetworkNodesColourActionListener networkNodesColourJDialogActionListener = new NetworkNodesColourActionListener();

        jColorChooser = new JColorChooser();
        // set the initial colour of jColorChooser to the colour of the clicked
        // cell in the NetworkNodesJDialog
        jColorChooser.setColor(NetworkNodesTableCellEditor.classInstance.currentColor);
        jPanel.add(jColorChooser);

        okJButton = new JButton("OK");
        okJButton.addActionListener(networkNodesColourJDialogActionListener);
        okJButton.setFont(font);
        jPanel.add(okJButton);

        cancelJButton = new JButton("Cancel");
        cancelJButton.addActionListener(networkNodesColourJDialogActionListener);
        cancelJButton.setFont(font);
        jPanel.add(cancelJButton);

        randomJButton = new JButton("Random");
        randomJButton.addActionListener(networkNodesColourJDialogActionListener);
        randomJButton.setFont(font);
        jPanel.add(randomJButton);

        NetworkNodesColourComponentListener networkNodesColourJDialogComponentListener = new NetworkNodesColourComponentListener();
        addComponentListener(networkNodesColourJDialogComponentListener);

        setVisible(true);

    }
}
