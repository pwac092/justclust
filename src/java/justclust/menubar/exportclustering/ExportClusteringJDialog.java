package justclust.menubar.exportclustering;

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
import justclust.customcomponents.BrowseButton;
import justclust.customcomponents.HelpButton;

/**
 * This class has an instance which represent a dialog for saving the current
 * clustering.
 */
public class ExportClusteringJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static ExportClusteringJDialog classInstance;
    /**
     * This field contains a panel for a ExportClusteringJDialog.
     */
    public ExportClusteringJPanel exportClusteringDialogJPanel;
    public HelpButton exportClusteringHelpButton;
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
    public JButton exportClusteringJButton;
    /**
     * This field contains a progress bar for exporting the current network.
     */
    public JProgressBar exportClusteringJProgressBar;
    // exportClusteringJDialogComponentListener is a field so that the
    // ExportClusteringJDialogActionListener can use it to reposition the components
    // of the ExportClusteringJDialog when the exportClusteringJProgressBar is added
    ExportClusteringComponentListener exportClusteringJDialogComponentListener;
//    // this field contains the current file
//    public File file;

    /**
     * This constructor initialises the fields of this class.
     */
    public ExportClusteringJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Export Clustering");
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

        exportClusteringDialogJPanel = new ExportClusteringJPanel();
        add(exportClusteringDialogJPanel);
        exportClusteringDialogJPanel.setLayout(null);
        
        ExportClusteringMouseListener exportClusteringMouseListener = new ExportClusteringMouseListener();
        
        exportClusteringHelpButton = new HelpButton();
        exportClusteringHelpButton.addMouseListener(exportClusteringMouseListener);
        exportClusteringDialogJPanel.add(exportClusteringHelpButton);
        
        Font font = new Font("Dialog", Font.PLAIN, 12);

        fileNameJLabel = new JLabel("File Name:");
        fileNameJLabel.setFont(font);
        exportClusteringDialogJPanel.add(fileNameJLabel);

        fileNameJTextField = new JTextField();
        exportClusteringDialogJPanel.add(fileNameJTextField);
//        fileNameJTextField.setEnabled(false);
//        fileNameJTextField.setDisabledTextColor(Color.BLACK);

        browseButton = new BrowseButton();
        exportClusteringDialogJPanel.add(browseButton);
        browseButton.addMouseListener(exportClusteringMouseListener);

        exportClusteringJButton = new JButton("Export Clustering");
        exportClusteringJButton.setFont(font);
        exportClusteringDialogJPanel.add(exportClusteringJButton);
        ExportClusteringActionListener exportClusteringActionListener = new ExportClusteringActionListener();
        exportClusteringJButton.addActionListener(exportClusteringActionListener);
        // when the user presses the enter key, the exportClusteringJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Export Clustering");
        getRootPane().getActionMap().put("Export Clustering", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                exportClusteringJButton.doClick();
            }
        });

        // the exportClusteringJProgressBar is added to the exportClusteringDialogJPanel
        // when a clustering is being exported
        exportClusteringJProgressBar = new JProgressBar();

        exportClusteringJDialogComponentListener = new ExportClusteringComponentListener();
        addComponentListener(exportClusteringJDialogComponentListener);

        // the setVisible method is called to make the ExportClusteringJDialog
        // appear
        setVisible(true);

    }
}
