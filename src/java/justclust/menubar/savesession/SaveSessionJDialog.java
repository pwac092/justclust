package justclust.menubar.savesession;

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

public class SaveSessionJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static SaveSessionJDialog classInstance;
    /**
     * This field contains a panel for a SaveSessionJDialog.
     */
    public SaveSessionJPanel saveSessionDialogJPanel;
    public HelpButton saveSessionHelpButton;
    /**
     * This field contains a label for a file name.
     */
    public JLabel fileNameJLabel;
    /**
     * This field contains a text field for a file name.
     */
    public JTextField fileNameJTextField;
    /**
     * This field contains a button for browsing to save a file.
     */
    public BrowseButton browseButton;
    /**
     * This field contains a button for saving the current session.
     */
    public JButton saveSessionJButton;
    /**
     * This field contains a progress bar for saving the current session.
     */
    public JProgressBar saveSessionJProgressBar;
    // saveSessionJDialogComponentListener is a field so that the
    // SaveSessionJDialogActionListener can use it to reposition the components
    // of the SaveSessionJDialog when the saveSessionJProgressBar is added
    SaveSessionComponentListener saveSessionJDialogComponentListener;
//    // this field contains the current file
//    public File file;

    /**
     * This constructor initialises the fields of this class.
     */
    public SaveSessionJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Save Session");
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

        saveSessionDialogJPanel = new SaveSessionJPanel();
        add(saveSessionDialogJPanel);
        saveSessionDialogJPanel.setLayout(null);
        
        SaveSessionMouseListener saveSessionMouseListener = new SaveSessionMouseListener();
        
        saveSessionHelpButton = new HelpButton();
        saveSessionHelpButton.addMouseListener(saveSessionMouseListener);
        saveSessionDialogJPanel.add(saveSessionHelpButton);
        
        Font font = new Font("Dialog", Font.PLAIN, 12);

        fileNameJLabel = new JLabel("File Name:");
        fileNameJLabel.setFont(font);
        saveSessionDialogJPanel.add(fileNameJLabel);

        fileNameJTextField = new JTextField();
        saveSessionDialogJPanel.add(fileNameJTextField);
//        fileNameJTextField.setEnabled(false);
//        fileNameJTextField.setDisabledTextColor(Color.BLACK);

        browseButton = new BrowseButton();
        saveSessionDialogJPanel.add(browseButton);
        browseButton.addMouseListener(saveSessionMouseListener);

        saveSessionJButton = new JButton("Save Session");
        saveSessionJButton.setFont(font);
        saveSessionDialogJPanel.add(saveSessionJButton);
        SaveSessionActionListener saveSessionActionListener = new SaveSessionActionListener();
        saveSessionJButton.addActionListener(saveSessionActionListener);
        // when the user presses the enter key, the saveSessionJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Save Session");
        getRootPane().getActionMap().put("Save Session", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveSessionJButton.doClick();
            }
        });

        // the saveSessionJProgressBar is added to the saveSessionDialogJPanel
        // when a session is being saved
        saveSessionJProgressBar = new JProgressBar();

        saveSessionJDialogComponentListener = new SaveSessionComponentListener();
        addComponentListener(saveSessionJDialogComponentListener);

        // the setVisible method is called to make the SaveSessionJDialog
        // appear
        setVisible(true);

    }
}