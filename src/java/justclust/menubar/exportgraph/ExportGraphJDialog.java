package justclust.menubar.exportgraph;

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

// this class has an instance which represent a dialog for exporting the current
// graph
public class ExportGraphJDialog extends JDialog {

    // this field contains the current instance of this class
    public static ExportGraphJDialog classInstance;
    // this field contains a panel for a ExportGraphJDialog
    public ExportGraphJPanel exportGraphJPanel;
    public HelpButton exportGraphHelpButton;
    // this field contains a label for a file name
    public JLabel fileNameJLabel;
    // this field contains a text field for a file name
    public JTextField fileNameJTextField;
    // this field contains a button for browsing to export a file
    public BrowseButton browseButton;
    // this field contains a button for exporting the current graph
    public JButton exportGraphJButton;
    // this field contains a progress bar for exporting the current graph
    public JProgressBar exportGraphJProgressBar;
    // exportGraphJDialogComponentListener is a field so that the
    // ExportGraphJDialogActionListener can use it to reposition the components
    // of the ExportGraphJDialog when the exportGraphJProgressBar is added
    ExportGraphComponentListener exportGraphJDialogComponentListener;
//    // this field contains the current file
//    public File file;

    // this constructor initialises the fields of this class.
    public ExportGraphJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Export Graph");
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

        exportGraphJPanel = new ExportGraphJPanel();
        add(exportGraphJPanel);
        exportGraphJPanel.setLayout(null);
        
        ExportGraphMouseListener exportGraphMouseListener = new ExportGraphMouseListener();
        
        exportGraphHelpButton = new HelpButton();
        exportGraphHelpButton.addMouseListener(exportGraphMouseListener);
        exportGraphJPanel.add(exportGraphHelpButton);
        
        Font font = new Font("Dialog", Font.PLAIN, 12);

        fileNameJLabel = new JLabel("File Name:");
        fileNameJLabel.setFont(font);
        exportGraphJPanel.add(fileNameJLabel);

        fileNameJTextField = new JTextField();
        exportGraphJPanel.add(fileNameJTextField);
//        fileNameJTextField.setEnabled(false);
//        fileNameJTextField.setDisabledTextColor(Color.BLACK);

        browseButton = new BrowseButton();
        exportGraphJPanel.add(browseButton);
        browseButton.addMouseListener(exportGraphMouseListener);

        exportGraphJButton = new JButton("Export Graph");
        exportGraphJButton.setFont(font);
        exportGraphJPanel.add(exportGraphJButton);
        ExportGraphActionListener exportGraphActionListener = new ExportGraphActionListener();
        exportGraphJButton.addActionListener(exportGraphActionListener);
        // when the user presses the enter key, the exportGraphJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Export Graph");
        getRootPane().getActionMap().put("Export Graph", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                exportGraphJButton.doClick();
            }
        });

        // the exportGraphJProgressBar is added to the exportGraphJPanel when a
        // graph is being exported
        exportGraphJProgressBar = new JProgressBar();

        exportGraphJDialogComponentListener = new ExportGraphComponentListener();
        addComponentListener(exportGraphJDialogComponentListener);

        // the setVisible method is called to make the ExportGraphJDialog appear
        setVisible(true);

    }
}
