package justclust.menubar.loadsession;

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

public class LoadSessionJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static LoadSessionJDialog classInstance;
    /**
     * This field contains a panel for a LoadSessionJDialog.
     */
    public LoadSessionJPanel loadSessionDialogJPanel;
    public HelpButton loadSessionHelpButton;
    /**
     * This field contains a label for a file name.
     */
    public JLabel fileNameJLabel;
    /**
     * This field contains a text field for a file name.
     */
    public JTextField fileNameJTextField;
    /**
     * This field contains a button for browsing to find a file.
     */
    public BrowseButton browseButton;
    /**
     * This field contains a button for loading a session.
     */
    public JButton loadSessionJButton;
    /**
     * This field contains a progress bar for loading a session.
     */
    public JProgressBar loadSessionJProgressBar;
    // loadSessionJDialogComponentListener is a field so that the
    // LoadSessionJDialogActionListener can use it to reposition the components
    // of the LoadSessionJDialog when the loadSessionJProgressBar is added
    LoadSessionComponentListener loadSessionJDialogComponentListener;
//    // this field contains the current file
//    public File file;

    /**
     * This constructor initialises the fields of this class.
     */
    public LoadSessionJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Load Session");
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

        loadSessionDialogJPanel = new LoadSessionJPanel();
        add(loadSessionDialogJPanel);
        loadSessionDialogJPanel.setLayout(null);
        
        LoadSessionMouseListener loadSessionMouseListener = new LoadSessionMouseListener();
        
        loadSessionHelpButton = new HelpButton();
        loadSessionHelpButton.addMouseListener(loadSessionMouseListener);
        loadSessionDialogJPanel.add(loadSessionHelpButton);
        
        Font font = new Font("Dialog", Font.PLAIN, 12);

        fileNameJLabel = new JLabel("File Name:");
        fileNameJLabel.setFont(font);
        loadSessionDialogJPanel.add(fileNameJLabel);

        fileNameJTextField = new JTextField();
        loadSessionDialogJPanel.add(fileNameJTextField);
//        fileNameJTextField.setEnabled(false);
//        fileNameJTextField.setDisabledTextColor(Color.BLACK);

        browseButton = new BrowseButton();
        loadSessionDialogJPanel.add(browseButton);
        browseButton.addMouseListener(loadSessionMouseListener);

        loadSessionJButton = new JButton("Load Session");
        loadSessionJButton.setFont(font);
        loadSessionDialogJPanel.add(loadSessionJButton);
        LoadSessionActionListener loadSessionActionListener = new LoadSessionActionListener();
        loadSessionJButton.addActionListener(loadSessionActionListener);
        // when the user presses the enter key, the loadSessionJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Load Session");
        getRootPane().getActionMap().put("Load Session", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                loadSessionJButton.doClick();
            }
        });

        // the loadSessionJProgressBar is added to the loadSessionDialogJPanel
        // when a session is being loaded
        loadSessionJProgressBar = new JProgressBar();

        loadSessionJDialogComponentListener = new LoadSessionComponentListener();
        addComponentListener(loadSessionJDialogComponentListener);

        // the setVisible method is called to make the LoadSessionJDialog
        // appear
        setVisible(true);

    }
}