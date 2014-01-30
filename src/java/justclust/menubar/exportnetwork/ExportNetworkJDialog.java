package justclust.menubar.exportnetwork;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import justclust.datastructures.Data;
import justclust.customcomponents.BrowseButton;
import justclust.customcomponents.HelpButton;

/**
 * This class has an instance which represent a dialog for saving the current
 * network.
 */
public class ExportNetworkJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static ExportNetworkJDialog classInstance;
    /**
     * This field contains a panel for a ExportNetworkJDialog.
     */
    public ExportNetworkJPanel exportNetworkDialogJPanel;
    public HelpButton exportNetworkHelpButton;
    /**
     * This field contains a label for a file name.
     */
    public JLabel fileNameJLabel;
    /**
     * This field contains a text field for a file name.
     */
    public JTextField fileNameJTextField;
    /**
     * This field contains a button for browsing to export a file.
     */
    public BrowseButton browseButton;
    /**
     * This field contains a button for exporting the current network.
     */
    public JButton exportNetworkJButton;
    /**
     * This field contains a progress bar for exporting the current network.
     */
    public JProgressBar exportNetworkJProgressBar;
    // exportNetworkJDialogComponentListener is a field so that the
    // ExportNetworkJDialogActionListener can use it to reposition the components
    // of the ExportNetworkJDialog when the exportNetworkJProgressBar is added
    ExportNetworkComponentListener exportNetworkJDialogComponentListener;
//    // this field contains the current file
//    public File file;

    /**
     * This constructor initialises the fields of this class.
     */
    public ExportNetworkJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Export Network");
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        pack();
        setBounds(
                (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2),
                (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                - getInsets().top - getInsets().bottom) / 2),
                500,
                10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                + getInsets().top + getInsets().bottom);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        exportNetworkDialogJPanel = new ExportNetworkJPanel();
        add(exportNetworkDialogJPanel);
        exportNetworkDialogJPanel.setLayout(null);
        
        ExportNetworkMouseListener exportNetworkMouseListener = new ExportNetworkMouseListener();
        
        exportNetworkHelpButton = new HelpButton();
        exportNetworkHelpButton.addMouseListener(exportNetworkMouseListener);
        exportNetworkDialogJPanel.add(exportNetworkHelpButton);
        
        Font font = new Font("Dialog", Font.PLAIN, 12);

        fileNameJLabel = new JLabel("File Name:");
        fileNameJLabel.setFont(font);
        exportNetworkDialogJPanel.add(fileNameJLabel);

        fileNameJTextField = new JTextField();
        exportNetworkDialogJPanel.add(fileNameJTextField);
//        fileNameJTextField.setEnabled(false);
//        fileNameJTextField.setDisabledTextColor(Color.BLACK);

        browseButton = new BrowseButton();
        exportNetworkDialogJPanel.add(browseButton);
        browseButton.addMouseListener(exportNetworkMouseListener);

        exportNetworkJButton = new JButton("Export Network");
        exportNetworkJButton.setFont(font);
        exportNetworkDialogJPanel.add(exportNetworkJButton);
        ExportNetworkActionListener exportNetworkActionListener = new ExportNetworkActionListener();
        exportNetworkJButton.addActionListener(exportNetworkActionListener);
        // when the user presses the enter key, the exportNetworkJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Export Network");
        getRootPane().getActionMap().put("Export Network", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                exportNetworkJButton.doClick();
            }
        });

        // the exportNetworkJProgressBar is added to the exportNetworkDialogJPanel
        // when a network is being exported
        exportNetworkJProgressBar = new JProgressBar();

        exportNetworkJDialogComponentListener = new ExportNetworkComponentListener();
        addComponentListener(exportNetworkJDialogComponentListener);

        // the setVisible method is called to make the ExportNetworkJDialog
        // appear
        setVisible(true);

    }
}
