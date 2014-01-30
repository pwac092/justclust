package justclust.menubar.newnetworkfromfile;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import justclust.datastructures.Data;
import justclust.customcomponents.BrowseButton;
import justclust.customcomponents.HelpButton;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;

/**
 * This class has an instance which represent a dialog for creating a network.
 */
public class NewNetworkFromFileJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static NewNetworkFromFileJDialog classInstance;
    /**
     * This field contains a panel for a NewNetworkFromFileJDialog.
     */
    public NewNetworkFromFileJPanel newNetworkDialogJPanel;
    public HelpButton newNetworkFromFileHelpButton;
    /**
     * This field contains a label for a file name.
     */
    public JLabel inputFileJLabel;
    /**
     * This field contains a text field for a file name.
     */
    public JTextField inputFileJTextField;
    /**
     * This field contains a button for browsing to find a file.
     */
    public BrowseButton inputFileBrowseButton;
    /**
     * This field contains a label for file parser plug-ins.
     */
    public JLabel fileTypeJLabel;
    /**
     * This field contains a combo box for file parser plug-ins.
     */
    public JComboBox fileParserJComboBox;
    public JLabel pluginDetailsJLabel;
    public JLabel pluginDescriptionJLabel;
    public JTextArea pluginDescriptionJTextArea;
    public JScrollPane pluginDescriptionJScrollPane;
    // pluginConfigurationControls contains PluginConfigurationControls which
    // are updated by the NewNetworkJDialogActionListener when the
    // createNetworkJButton is clicked.
    // they are updated to contain the information input by the user to
    // configure the plug-in being used.
    public ArrayList<PluginConfigurationControl> pluginConfigurationControls;
    // pluginConfigurationJComponents contains JComponents which are used by the
    // user to enter information to configure the plug-in being used.
    // each entry of the outer ArrayList (an inner ArrayList) corresponds to a
    // pluginConfigurationControl.
    // each entry of the inner ArrayList is a JComponent which makes up the
    // pluginConfigurationControl.
    public ArrayList<ArrayList<JComponent>> pluginConfigurationJComponents;
    /**
     * This field contains a button for creating a network.
     */
    public JButton createNetworkJButton;
    /**
     * This field contains a progress bar for creating a network.
     */
    public JProgressBar createNetworkJProgressBar;
    // newNetworkJDialogComponentListener is a field so that the
    // NewNetworkJDialogActionListener can use it to reposition the components
    // of the NewNetworkFromFileJDialog when components for configuring plug-ins are
    // changed
    public NewNetworkFromFileComponentListener newNetworkJDialogComponentListener;
//    // this field contains the current file
//    public File file;
    // pluginClass and pluginClassInstance contain the plug-in class and class
    // instance which has been used to get the configuration controls.
    // they are fields so that, when a new network is created, the code
    // which handles that will have access to them and will be able to update
    // its fields with the configuration information input by the user.
    public Class<?> pluginClass;
    public Object pluginClassInstance;

    /**
     * This constructor initialises the fields of this class.
     */
    public NewNetworkFromFileJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("New Network from File");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        this.setResizable(false);

        newNetworkDialogJPanel = new NewNetworkFromFileJPanel();
        add(newNetworkDialogJPanel);
        newNetworkDialogJPanel.setLayout(null);
        newNetworkDialogJPanel.setBackground(new Color(239, 239, 239));

        final NewNetworkFromFileMouseListener newNetworkMouseListener = new NewNetworkFromFileMouseListener();

        newNetworkFromFileHelpButton = new HelpButton();
        newNetworkFromFileHelpButton.addMouseListener(newNetworkMouseListener);
        newNetworkDialogJPanel.add(newNetworkFromFileHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        inputFileJLabel = new JLabel("Input File:");
        inputFileJLabel.setFont(font);
        newNetworkDialogJPanel.add(inputFileJLabel);

        inputFileJTextField = new JTextField();
        newNetworkDialogJPanel.add(inputFileJTextField);
//        inputFileJTextField.setEnabled(false);
//        inputFileJTextField.setDisabledTextColor(Color.BLACK);

        inputFileBrowseButton = new BrowseButton();
        newNetworkDialogJPanel.add(inputFileBrowseButton);
        inputFileBrowseButton.addMouseListener(newNetworkMouseListener);

        fileTypeJLabel = new JLabel("File Type:");
        fileTypeJLabel.setFont(font);
        newNetworkDialogJPanel.add(fileTypeJLabel);

        NewNetworkFromFileActionListener newNetworkActionListener = new NewNetworkFromFileActionListener();

        fileParserJComboBox = new JComboBox();
        fileParserJComboBox.setFont(font);
        newNetworkDialogJPanel.add(fileParserJComboBox);
        fileParserJComboBox.addActionListener(newNetworkActionListener);

        // the pluginDetailsJLabel is added to the newNetworkDialogJPanel when
        // a plug-in is loaded
        pluginDetailsJLabel = new JLabel("Plug-in Details");
        pluginDetailsJLabel.setFont(font);
        pluginDetailsJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // the pluginDescriptionJLabel is added to the newNetworkDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJLabel = new JLabel("Plug-in Description:");
        pluginDescriptionJLabel.setFont(font);

        // the pluginDescriptionJScrollPane is added to the newNetworkDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJTextArea = new JTextArea("");
        pluginDescriptionJTextArea.setEditable(false);
        pluginDescriptionJTextArea.setLineWrap(true);
        // this makes words start on a new line instead of spanning multiple
        // lines
        pluginDescriptionJTextArea.setWrapStyleWord(true);
        pluginDescriptionJScrollPane = new JScrollPane(pluginDescriptionJTextArea);

        createNetworkJButton = new JButton("Create Network");
        createNetworkJButton.setFont(font);
        newNetworkDialogJPanel.add(createNetworkJButton);
        createNetworkJButton.addActionListener(newNetworkActionListener);
        // when the user presses the enter key, the createNetworkJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Create Network");
        getRootPane().getActionMap().put("Create Network", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createNetworkJButton.doClick();
            }
        });

        // the createNetworkJProgressBar is added to the newNetworkDialogJPanel
        // when a network is being created
        createNetworkJProgressBar = new JProgressBar();

        newNetworkJDialogComponentListener = new NewNetworkFromFileComponentListener();
        addComponentListener(newNetworkJDialogComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // NewNetworkFromFileJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the NewNetworkFromFileJDialog correctly.
        // without this specific order and combination, the
        // NewNetworkFromFileJDialog is initially 2 pixels too tall.
        // why this code below appears to work is unknown.
        newNetworkJDialogComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                NewNetworkFromFileJDialog.classInstance.setBounds(
                        (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2),
                        (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                        - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                        - getInsets().top - getInsets().bottom) / 2),
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + getInsets().top + getInsets().bottom);

                // fileParserDisplayNames is declared outside the following try
                // block so that the fileParserJComboBox can use it outside the try
                // block later on
                ArrayList<String> fileParserDisplayNames = new ArrayList<String>();
                fileParserDisplayNames.add("");

                try {

                    // this code populates the combo box contained in the
                    // NewNetworkFromFileJDialog.fileParserJComboBox field with the display
                    // names of file parser plug-ins.
                    // each file in the file path of the file parser plug-ins is
                    // checked to see if it is a jar file.
                    // each jar file found is then search for class files which
                    // implement the FileParserPluginInterface interface.
                    // these class files are then loaded and there getName() methods
                    // are called.
                    // the display names are returned by the getName() methods.

                    File file = new File(Data.parsingPluginsPath);
                    String[] list = file.list();
                    Data.fileParserJarNames = new ArrayList<String>();
                    Data.fileParserClassNames = new ArrayList<String>();
                    for (String filename : list) {

                        if (filename.endsWith(".jar")) {

                            File jarFile = new File(Data.parsingPluginsPath + '/' + filename);

                            Enumeration<JarEntry> enumeration = new JarFile(jarFile).entries();

                            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});

                            while (enumeration.hasMoreElements()) {
                                JarEntry jarEntry = enumeration.nextElement();
                                if (jarEntry.getName().endsWith(".class")) {

                                    Class<?> loadedClass = urlClassLoader.loadClass(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    if (FileParserPluginInterface.class.isAssignableFrom(loadedClass)) {

                                        Data.fileParserJarNames.add(filename);
                                        Data.fileParserClassNames.add(
                                                jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                        Object classInstance = loadedClass.newInstance();
                                        Method method = loadedClass.getMethod("getFileType", new Class[]{});
                                        fileParserDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                    }

                                }
                            }

                        }

                    }

                } catch (Exception exception) {

                    // the code after the try block which populates the
                    // fileParserJComboBox and adds the plug-in controls should not
                    // be executed when an error occurs, so the method is returned
                    return;

                }

                NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                        .setModel(
                        new DefaultComboBoxModel(
                        fileParserDisplayNames
                        .toArray(new String[fileParserDisplayNames
                        .size()])));

                NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();

//                // the plug-in description is set to contain no characters here so that,
//                // if there is an error in loading the description, this description
//                // will be used to avoid issues
//                String pluginDescription = "";
//
//                try {
//
//                    // call the getDescription method of the plug-in to get
//                    // its description
//                    File file = new File(Data.parsingPluginsPath
//                            + '/'
//                            + Data.fileParserJarNames
//                            .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
//                            .getSelectedIndex()));
//                    URLClassLoader urlClassLoader = URLClassLoader
//                            .newInstance(new URL[]{file.toURI().toURL()});
//                    pluginClass = urlClassLoader.loadClass(Data.fileParserClassNames
//                            .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
//                            .getSelectedIndex()));
//                    pluginClassInstance = pluginClass.newInstance();
//                    Method method = pluginClass.getMethod("getDescription",
//                            new Class[]{});
//                    pluginDescription = (String) method.invoke(pluginClassInstance, new Object[]{});
//
//                } catch (Exception exception) {
//
//                    exception.printStackTrace();
//
//                    JOptionPane.showMessageDialog(
//                            NewNetworkFromFileJDialog.classInstance,
//                            "Plug-in description could not be loaded due to error");
//
//                }
//
//                // add the pluginDetailsJLabel, pluginDescriptionJLabel, and
//                // pluginDescriptionJScrollPane of the NetworkJDialog to the
//                // newNetworkDialogJPanel so that they can display the details of the
//                // plug-in.
//                // this should only happen if they haven't been added to the
//                // newNetworkDialogJPanel already.
//                if (NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
//                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel);
//                }
//                if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
//                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel);
//                }
//                if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
//                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane);
//                }
//
//                // update the pluginDescriptionJTextArea to contain the contents of
//                // pluginDescription which was assigned earlier
//                NewNetworkFromFileJDialog.classInstance.pluginDescriptionJTextArea.setText(pluginDescription);
//                // this makes the scrollbar of the pluginDescriptionJScrollPane start
//                // at the top
//                NewNetworkFromFileJDialog.classInstance.pluginDescriptionJTextArea.setCaretPosition(0);
//
//                try {
//
//                    // call the getConfigurationControls method of the plug-in to get
//                    // its configuration controls
//                    File file = new File(Data.parsingPluginsPath
//                            + '/'
//                            + Data.fileParserJarNames
//                            .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
//                            .getSelectedIndex()));
//                    URLClassLoader urlClassLoader = URLClassLoader
//                            .newInstance(new URL[]{file.toURI().toURL()});
//                    pluginClass = urlClassLoader.loadClass(Data.fileParserClassNames
//                            .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
//                            .getSelectedIndex()));
//                    pluginClassInstance = pluginClass.newInstance();
//                    Method method = pluginClass.getMethod("getConfigurationControls",
//                            new Class[]{});
//                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = (ArrayList<PluginConfigurationControl>) method.invoke(pluginClassInstance, new Object[]{});
//
//                } catch (Exception exception) {
//
//                    exception.printStackTrace();
//
//                    NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();
//
//                    JOptionPane.showMessageDialog(
//                            NewNetworkFromFileJDialog.classInstance,
//                            "Plug-in configuration controls could not be loaded due to error");
//
//                }
//
//                // components which correspond to the pluginConfigurationControls
//                // are created and added to the NewNetworkFromFileJDialog
//                NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents = new ArrayList<ArrayList<JComponent>>();
//                for (PluginConfigurationControl control : NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls) {
//                    if (control instanceof CheckBoxControl) {
//                        ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
//                        JLabel jLabel = new JLabel(((CheckBoxControl) control).label);
//                        Font font = new Font("Dialog", Font.PLAIN, 12);
//                        jLabel.setFont(font);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
//                        arrayList.add(jLabel);
//                        JCheckBox jCheckBox = new JCheckBox();
//                        jCheckBox.setSelected(((CheckBoxControl) control).checked);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jCheckBox);
//                        arrayList.add(jCheckBox);
//                        NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
//                    }
//                    if (control instanceof ComboBoxControl) {
//                        ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
//                        JLabel jLabel = new JLabel(((ComboBoxControl) control).label);
//                        Font font = new Font("Dialog", Font.PLAIN, 12);
//                        jLabel.setFont(font);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
//                        arrayList.add(jLabel);
//                        JComboBox jComboBox = new JComboBox();
//                        jComboBox
//                                .setModel(
//                                new DefaultComboBoxModel(
//                                ((ComboBoxControl) control).options
//                                .toArray(new String[((ComboBoxControl) control).options
//                                .size()])));
//                        jComboBox.setSelectedIndex(((ComboBoxControl) control).selectedOptionIndex);
//                        jComboBox.setFont(font);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jComboBox);
//                        arrayList.add(jComboBox);
//                        NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
//                    }
//                    if (control instanceof FileSystemPathControl) {
//                        ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
//                        JLabel jLabel = new JLabel(((FileSystemPathControl) control).label);
//                        Font font = new Font("Dialog", Font.PLAIN, 12);
//                        jLabel.setFont(font);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
//                        arrayList.add(jLabel);
//                        JTextField jTextField = new JTextField(((FileSystemPathControl) control).text);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jTextField);
//                        arrayList.add(jTextField);
//                        BrowseButton browseButton = new BrowseButton();
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(browseButton);
//                        browseButton.addMouseListener(newNetworkMouseListener);
//                        arrayList.add(browseButton);
//                        NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
//                    }
//                    if (control instanceof TextFieldControl) {
//                        ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
//                        JLabel jLabel = new JLabel(((TextFieldControl) control).label);
//                        Font font = new Font("Dialog", Font.PLAIN, 12);
//                        jLabel.setFont(font);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
//                        arrayList.add(jLabel);
//                        JTextField jTextField = new JTextField(((TextFieldControl) control).text);
//                        NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jTextField);
//                        arrayList.add(jTextField);
//                        NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
//                    }
//                }
//
//                NewNetworkFromFileJDialog.classInstance.setBounds(
//                        (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2),
//                        (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
//                        - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10
//                        + NewNetworkFromFileJDialog.classInstance.getInsets().top + NewNetworkFromFileJDialog.classInstance.getInsets().bottom)) / 2),
//                        500,
//                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10
//                        + NewNetworkFromFileJDialog.classInstance.getInsets().top + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);

            }
        });
        // the setVisible method is called to make the NewNetworkFromFileJDialog
        // appear
        setVisible(true);

    }
}
