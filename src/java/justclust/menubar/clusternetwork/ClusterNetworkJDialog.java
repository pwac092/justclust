package justclust.menubar.clusternetwork;

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
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;

/**
 * This class has an instance which represent a dialog for clustering the
 * current network.
 */
public class ClusterNetworkJDialog extends JDialog {

    /**
     * This field contains the current instance of this class.
     */
    public static ClusterNetworkJDialog classInstance;
    /**
     * This field contains a panel for a ClusterNetworkJDialog.
     */
    public ClusterNetworkJPanel clusterNetworkDialogJPanel;
    public HelpButton clusterNetworkHelpButton;
    /**
     * This field contains a label for clustering algorithm plug-ins.
     */
    public JLabel clusteringAlgorithmJLabel;
    /**
     * This field contains a combo box for clustering algorithm plug-ins.
     */
    public JComboBox clusteringAlgorithmJComboBox;
    public JLabel pluginDetailsJLabel;
    public JLabel pluginDescriptionJLabel;
    public JTextArea pluginDescriptionJTextArea;
    public JScrollPane pluginDescriptionJScrollPane;
    // pluginConfigurationControls contains PluginConfigurationControls which
    // are updated by the ClusterNetworkJDialogActionListener when the
    // clusterNetworkJButton is clicked.
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
     * This field contains a button for clustering the current network.
     */
    public JButton clusterNetworkJButton;
    /**
     * This field contains a progress bar for clustering the current network.
     */
    public JProgressBar clusterNetworkJProgressBar;
    // clusterNetworkJDialogComponentListener is a field so that the
    // ClusterNetworkJDialogActionListener can use it to reposition the components
    // of the ClusterNetworkJDialog when components for configuring plug-ins are
    // changed
    ClusterNetworkComponentListener clusterNetworkJDialogComponentListener;
    // pluginClass and pluginClassInstance contain the plug-in class and class
    // instance which has been used to get the configuration controls.
    // they are fields so that, when a layout is applied, the code
    // which handles that will have access to them and will be able to update
    // its fields with the configuration information input by the user.
    public Class<?> pluginClass;
    public Object pluginClassInstance;

    /**
     * This constructor initialises the fields of this class.
     */
    public ClusterNetworkJDialog() {

        classInstance = this;

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Cluster Network");
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        clusterNetworkDialogJPanel = new ClusterNetworkJPanel();
        add(clusterNetworkDialogJPanel);
        clusterNetworkDialogJPanel.setLayout(null);
        clusterNetworkDialogJPanel.setBackground(new Color(239, 239, 239));

        final ClusterNetworkMouseListener clusterNetworkMouseListener = new ClusterNetworkMouseListener();

        clusterNetworkHelpButton = new HelpButton();
        clusterNetworkHelpButton.addMouseListener(clusterNetworkMouseListener);
        clusterNetworkDialogJPanel.add(clusterNetworkHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        clusteringAlgorithmJLabel = new JLabel("Clustering Algorithm:");
        clusteringAlgorithmJLabel.setFont(font);
        clusterNetworkDialogJPanel.add(clusteringAlgorithmJLabel);

        ClusterNetworkActionListener clusterNetworkActionListener = new ClusterNetworkActionListener();

        clusteringAlgorithmJComboBox = new JComboBox();
        clusteringAlgorithmJComboBox.setFont(font);
        clusterNetworkDialogJPanel.add(clusteringAlgorithmJComboBox);
        clusteringAlgorithmJComboBox.addActionListener(clusterNetworkActionListener);

        // the pluginDetailsJLabel is added to the clusterNetworkDialogJPanel when
        // a plug-in is loaded
        pluginDetailsJLabel = new JLabel("Plug-in Details");
        pluginDetailsJLabel.setFont(font);
        pluginDetailsJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // the pluginDescriptionJLabel is added to the clusterNetworkDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJLabel = new JLabel("Plug-in Description:");
        pluginDescriptionJLabel.setFont(font);

        // the pluginDescriptionJScrollPane is added to the clusterNetworkDialogJPanel
        // when a plug-in is loaded
        pluginDescriptionJTextArea = new JTextArea("");
        pluginDescriptionJTextArea.setEditable(false);
        pluginDescriptionJTextArea.setLineWrap(true);
        // this makes words start on a new line instead of spanning multiple
        // lines
        pluginDescriptionJTextArea.setWrapStyleWord(true);
        pluginDescriptionJScrollPane = new JScrollPane(pluginDescriptionJTextArea);

        clusterNetworkJButton = new JButton("Cluster Network");
        clusterNetworkJButton.setFont(font);
        clusterNetworkDialogJPanel.add(clusterNetworkJButton);
        clusterNetworkJButton.addActionListener(clusterNetworkActionListener);
        // when the user presses the enter key, the clusterNetworkJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Cluster Network");
        getRootPane().getActionMap().put("Cluster Network", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clusterNetworkJButton.doClick();
            }
        });

        // the clusterNetworkJProgressBar is added to the clusterNetworkDialogJPanel
        // when the current network is being clustered
        clusterNetworkJProgressBar = new JProgressBar();

        clusterNetworkJDialogComponentListener = new ClusterNetworkComponentListener();
        addComponentListener(clusterNetworkJDialogComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // ClusterNetworkJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the ClusterNetworkJDialog correctly.
        // without this specific order and combination, the
        // ClusterNetworkJDialog is initially 2 pixels too tall.
        // why this code below appears to work is unknown.
        clusterNetworkJDialogComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                ClusterNetworkJDialog.classInstance.setBounds(
                        (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2),
                        (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                        - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10)
                        - getInsets().top - getInsets().bottom) / 2),
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + getInsets().top + getInsets().bottom);

                // clusteringAlgorithmDisplayNames is declared outside the following try
                // block so that the clusteringAlgorithmJComboBox can use it outside the try
                // block later on
                ArrayList<String> clusteringAlgorithmDisplayNames = new ArrayList<String>();
                clusteringAlgorithmDisplayNames.add("");

                try {

                    // this code populates the combo box contained in the
                    // ClusterNetworkJDialog.clusteringAlgorithmJComboBox field with the display
                    // names of clustering algorithm plug-ins.
                    // each file in the file path of the clustering algorithm plug-ins is
                    // checked to see if it is a jar file.
                    // each jar file found is then search for class files which
                    // implement the ClusteringAlgorithmPluginInterface interface.
                    // these class files are then loaded and there getName() methods
                    // are called.
                    // the display names are returned by the getName() methods.

                    File file = new File(Data.clusteringPluginsPath);
                    String[] list = file.list();
                    Data.clusteringAlgorithmJarNames = new ArrayList<String>();
                    Data.clusteringAlgorithmClassNames = new ArrayList<String>();
                    for (String filename : list) {

                        if (filename.endsWith(".jar")) {

                            File jarFile = new File(Data.clusteringPluginsPath
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

                                    if (ClusteringAlgorithmPluginInterface.class
                                            .isAssignableFrom(loadedClass)) {

                                        Data.clusteringAlgorithmJarNames.add(filename);
                                        Data.clusteringAlgorithmClassNames.add(jarEntry
                                                .getName()
                                                .replaceAll("/", ".")
                                                .replaceAll(".class",
                                                ""));

                                        Object classInstance = loadedClass
                                                .newInstance();
                                        Method method = loadedClass.getMethod(
                                                "getName", new Class[]{});
                                        clusteringAlgorithmDisplayNames
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
                    // clusteringAlgorithmJComboBox and adds the plug-in controls should not
                    // be executed when an error occurs, so the method is returned
                    return;

                }

                ClusterNetworkJDialog.classInstance.clusteringAlgorithmJComboBox
                        .setModel(
                        new DefaultComboBoxModel(
                        clusteringAlgorithmDisplayNames
                        .toArray(new String[clusteringAlgorithmDisplayNames
                        .size()])));

                ClusterNetworkJDialog.classInstance.pluginConfigurationJComponents = new ArrayList<ArrayList<JComponent>>();

            }
        });
        // the setVisible method is called to make the ClusterNetworkJDialog
        // appear
        setVisible(true);

    }
}
