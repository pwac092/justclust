package justclust.menubar.applylayout;

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
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

public class ApplyLayoutJDialog extends JDialog {

    public static ApplyLayoutJDialog classInstance;
    public ApplyLayoutJPanel applyLayoutDialogJPanel;
    public HelpButton applyLayoutHelpButton;
    public JLabel visualisationLayoutJLabel;
    public JComboBox visualisationLayoutJComboBox;
    public JLabel pluginDetailsJLabel;
    public JLabel pluginDescriptionJLabel;
    public JTextArea pluginDescriptionJTextArea;
    public JScrollPane pluginDescriptionJScrollPane;
    // pluginConfigurationControls contains PluginConfigurationControls which
    // are updated by the ApplyLayoutJDialogActionListener when the
    // applyLayoutJButton is clicked.
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
    public JButton applyLayoutJButton;
    public JProgressBar applyLayoutJProgressBar;
    // applyLayoutJDialogComponentListener is a field so that the
    // ApplyLayoutJDialogActionListener can use it to reposition the components
    // of the ApplyLayoutJDialog when components for configuring plug-ins are
    // changed
    ApplyLayoutComponentListener applyLayoutJDialogComponentListener;
    // pluginClass and pluginClassInstance contain the plug-in class and class
    // instance which has been used to get the configuration controls.
    // they are fields so that, when a layout is applied, the code
    // which handles that will have access to them and will be able to update
    // its fields with the configuration information input by the user.
    public Class<?> pluginClass;
    public Object pluginClassInstance;

    public ApplyLayoutJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Layout Network");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        applyLayoutDialogJPanel = new ApplyLayoutJPanel();
        add(applyLayoutDialogJPanel);
        applyLayoutDialogJPanel.setLayout(null);
        applyLayoutDialogJPanel.setBackground(new Color(239, 239, 239));

        final ApplyLayoutMouseListener applyLayoutMouseListener = new ApplyLayoutMouseListener();

        applyLayoutHelpButton = new HelpButton();
        applyLayoutHelpButton.addMouseListener(applyLayoutMouseListener);
        applyLayoutDialogJPanel.add(applyLayoutHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        visualisationLayoutJLabel = new JLabel("Visualisation Layout:");
        visualisationLayoutJLabel.setFont(font);
        applyLayoutDialogJPanel.add(visualisationLayoutJLabel);

        ApplyLayoutActionListener applyLayoutActionListener = new ApplyLayoutActionListener();

        visualisationLayoutJComboBox = new JComboBox();
        visualisationLayoutJComboBox.setFont(font);
        applyLayoutDialogJPanel.add(visualisationLayoutJComboBox);
        visualisationLayoutJComboBox.addActionListener(applyLayoutActionListener);

        // the pluginDetailsJLabel is added to the applyLayoutDialogJPanel when
        // a plug-in is loaded
        pluginDetailsJLabel = new JLabel("Plug-in Details");
        pluginDetailsJLabel.setFont(font);
        pluginDetailsJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // the pluginDescriptionJLabel is added to the applyLayoutDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJLabel = new JLabel("Plug-in Description:");
        pluginDescriptionJLabel.setFont(font);

        // the pluginDescriptionJScrollPane is added to the applyLayoutDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJTextArea = new JTextArea("");
        pluginDescriptionJTextArea.setEditable(false);
        pluginDescriptionJTextArea.setLineWrap(true);
        // this makes words start on a new line instead of spanning multiple
        // lines
        pluginDescriptionJTextArea.setWrapStyleWord(true);
        pluginDescriptionJScrollPane = new JScrollPane(pluginDescriptionJTextArea);

        applyLayoutJButton = new JButton("Apply Layout");
        applyLayoutJButton.setFont(font);
        applyLayoutDialogJPanel.add(applyLayoutJButton);
        applyLayoutJButton.addActionListener(applyLayoutActionListener);
        // when the user presses the enter key, the applyLayoutJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Apply Layout");
        getRootPane().getActionMap().put("Apply Layout", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                applyLayoutJButton.doClick();
            }
        });

        // the applyLayoutJProgressBar is added to the applyLayoutDialogJPanel
        // when a layout is being applied
        applyLayoutJProgressBar = new JProgressBar();

        applyLayoutJDialogComponentListener = new ApplyLayoutComponentListener();
        addComponentListener(applyLayoutJDialogComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // ApplyLayoutJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the ApplyLayoutJDialog correctly.
        // without this specific order and combination, the
        // ApplyLayoutJDialog is initially 2 pixels too tall.
        // why this code below appears to work is unknown.
        applyLayoutJDialogComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                ApplyLayoutJDialog.classInstance.setBounds(
                        (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2),
                        (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                        - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                        - getInsets().top - getInsets().bottom) / 2),
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + getInsets().top + getInsets().bottom);

                // visualisationLayoutDisplayNames is declared outside the following try
                // block so that the visualisationLayoutJComboBox can use it outside the try
                // block later on
                ArrayList<String> visualisationLayoutDisplayNames = new ArrayList<String>();
                visualisationLayoutDisplayNames.add("");

                try {

                    // this code populates the combo box contained in the
                    // ApplyLayoutJDialog.visualisationLayoutJComboBox field with the display
                    // names of visualisation layout plug-ins.
                    // each file in the file path of the visualisation layout plug-ins is
                    // checked to see if it is a jar file.
                    // each jar file found is then search for class files which
                    // implement the VisualisationLayoutPluginInterface interface.
                    // these class files are then loaded and there getName() methods
                    // are called.
                    // the display names are returned by the getName() methods.

                    File file = new File(Data.visualisationPluginsPath);
                    String[] list = file.list();
                    Data.visualisationLayoutJarNames = new ArrayList<String>();
                    Data.visualisationLayoutClassNames = new ArrayList<String>();
                    for (String filename : list) {

                        if (filename.endsWith(".jar")) {

                            File jarFile = new File(Data.visualisationPluginsPath
                                    + '/'
                                    + filename);

                            Enumeration<JarEntry> enumeration = new JarFile(
                                    jarFile).entries();

                            URLClassLoader urlClassLoader = URLClassLoader
                                    .newInstance(new URL[]{jarFile.toURI()
                                .toURL()});

                            while (enumeration.hasMoreElements()) {
                                JarEntry jarEntry = enumeration.nextElement();
                                if (jarEntry.getName().endsWith(".class")) {

                                    Class<?> loadedClass = urlClassLoader
                                            .loadClass(jarEntry.getName()
                                            .replaceAll("/", ".")
                                            .replaceAll(".class", ""));

                                    if (VisualisationLayoutPluginInterface.class
                                            .isAssignableFrom(loadedClass)) {

                                        Data.visualisationLayoutJarNames
                                                .add(filename);
                                        Data.visualisationLayoutClassNames
                                                .add(jarEntry
                                                .getName()
                                                .replaceAll("/", ".")
                                                .replaceAll(".class",
                                                ""));

                                        Object classInstance = loadedClass
                                                .newInstance();
                                        Method method = loadedClass.getMethod(
                                                "getName", new Class[]{});
                                        visualisationLayoutDisplayNames
                                                .add((String) method.invoke(
                                                classInstance,
                                                new Object[]{}));

                                    }

                                }
                            }

                        }

                    }

                } catch (Exception exception) {

                    // the code after the try block which populates the
                    // visualisationLayoutJComboBox and adds the plug-in controls should not
                    // be executed when an error occurs, so the method is returned
                    return;

                }

                ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                        .setModel(
                        new DefaultComboBoxModel(
                        visualisationLayoutDisplayNames
                        .toArray(new String[visualisationLayoutDisplayNames
                        .size()])));

                ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents = new ArrayList<ArrayList<JComponent>>();

            }
        });
        // the setVisible method is called to make the ApplyLayoutJDialog
        // appear
        setVisible(true);

    }
}
