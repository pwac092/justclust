package justclust.toolbar.manageplugins;

import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.DialogSizesAndPositions;
import justclust.customcomponents.BrowseButton;
import justclust.customcomponents.HelpButton;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

public class ManagePluginsJDialog extends JDialog {

    public static ManagePluginsJDialog classInstance;
    public JPanel managePluginsJPanel;
    public HelpButton managePluginsHelpButton;
    public JTabbedPane managePluginsJTabbedPane;
    public ManagePluginsJPanel parsingPluginsJPanel;
    public JLabel parsingPluginsPathJLabel;
    public JTextField parsingPluginsPathJTextField;
    public BrowseButton parsingPluginsPathBrowseButton;
    public JButton loadParsingPluginsJButton;
    public JLabel loadedParsingPluginsJLabel;
    public JComboBox loadedParsingPluginsJComboBox;
    public ManagePluginsJPanel clusteringPluginsJPanel;
    public JLabel clusteringPluginsPathJLabel;
    public JTextField clusteringPluginsPathJTextField;
    public BrowseButton clusteringPluginsPathBrowseButton;
    public JButton loadClusteringPluginsJButton;
    public JLabel loadedClusteringPluginsJLabel;
    public JComboBox loadedClusteringPluginsJComboBox;
    public ManagePluginsJPanel visualisationPluginsJPanel;
    public JLabel visualisationPluginsPathJLabel;
    public JTextField visualisationPluginsPathJTextField;
    public BrowseButton visualisationPluginsPathBrowseButton;
    public JButton loadVisualisationPluginsJButton;
    public JLabel loadedVisualisationPluginsJLabel;
    public JComboBox loadedVisualisationPluginsJComboBox;
    ManagePluginsComponentListener managePluginsComponentListener;

    public ManagePluginsJDialog(JFrame parent) {
        
        super(parent, "Manage Plug-ins");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        managePluginsJPanel = new JPanel();
        managePluginsJPanel.setLayout(null);
        add(managePluginsJPanel);

        ManagePluginsMouseListener managePluginsMouseListener = new ManagePluginsMouseListener();

        managePluginsHelpButton = new HelpButton();
        managePluginsHelpButton.addMouseListener(managePluginsMouseListener);
        managePluginsJPanel.add(managePluginsHelpButton);
        
        managePluginsJTabbedPane = new JTabbedPane();
        managePluginsJPanel.add(managePluginsJTabbedPane);

        parsingPluginsJPanel = new ManagePluginsJPanel();
        parsingPluginsJPanel.setLayout(null);
        managePluginsJTabbedPane.add("File Parsers", parsingPluginsJPanel);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        parsingPluginsPathJLabel = new JLabel("Parsing Plug-ins Path:");
        parsingPluginsPathJLabel.setFont(font);
        parsingPluginsJPanel.add(parsingPluginsPathJLabel);

        parsingPluginsPathJTextField = new JTextField(Data.parsingPluginsPath);
        parsingPluginsJPanel.add(parsingPluginsPathJTextField);

        parsingPluginsPathBrowseButton = new BrowseButton();
        parsingPluginsPathBrowseButton.addMouseListener(managePluginsMouseListener);
        parsingPluginsJPanel.add(parsingPluginsPathBrowseButton);

        ManagePluginsActionListener managePluginsActionListener = new ManagePluginsActionListener();

        loadParsingPluginsJButton = new JButton("Load Parsing Plug-ins");
        loadParsingPluginsJButton.addActionListener(managePluginsActionListener);
        loadParsingPluginsJButton.setFont(font);
        parsingPluginsJPanel.add(loadParsingPluginsJButton);

        loadedParsingPluginsJLabel = new JLabel("Loaded Parsing Plug-ins:");
        loadedParsingPluginsJLabel.setFont(font);
        parsingPluginsJPanel.add(loadedParsingPluginsJLabel);

        loadedParsingPluginsJComboBox = new JComboBox();
        loadedParsingPluginsJComboBox.setFont(font);
        parsingPluginsJPanel.add(loadedParsingPluginsJComboBox);

        clusteringPluginsJPanel = new ManagePluginsJPanel();
        clusteringPluginsJPanel.setLayout(null);
        managePluginsJTabbedPane.add("Clustering Algorithms", clusteringPluginsJPanel);

        clusteringPluginsPathJLabel = new JLabel("Clustering Plug-ins Path:");
        clusteringPluginsPathJLabel.setFont(font);
        clusteringPluginsJPanel.add(clusteringPluginsPathJLabel);

        clusteringPluginsPathJTextField = new JTextField(Data.clusteringPluginsPath);
        clusteringPluginsJPanel.add(clusteringPluginsPathJTextField);

        clusteringPluginsPathBrowseButton = new BrowseButton();
        clusteringPluginsPathBrowseButton.addMouseListener(managePluginsMouseListener);
        clusteringPluginsJPanel.add(clusteringPluginsPathBrowseButton);

        loadClusteringPluginsJButton = new JButton("Load Clustering Plug-ins");
        loadClusteringPluginsJButton.addActionListener(managePluginsActionListener);
        loadClusteringPluginsJButton.setFont(font);
        clusteringPluginsJPanel.add(loadClusteringPluginsJButton);

        loadedClusteringPluginsJLabel = new JLabel("Loaded Clustering Plug-ins:");
        loadedClusteringPluginsJLabel.setFont(font);
        clusteringPluginsJPanel.add(loadedClusteringPluginsJLabel);

        loadedClusteringPluginsJComboBox = new JComboBox();
        loadedClusteringPluginsJComboBox.setFont(font);
        clusteringPluginsJPanel.add(loadedClusteringPluginsJComboBox);

        visualisationPluginsJPanel = new ManagePluginsJPanel();
        visualisationPluginsJPanel.setLayout(null);
        managePluginsJTabbedPane.add("Visualisation Layouts", visualisationPluginsJPanel);

        visualisationPluginsPathJLabel = new JLabel("Visualisation Plug-ins Path:");
        visualisationPluginsPathJLabel.setFont(font);
        visualisationPluginsJPanel.add(visualisationPluginsPathJLabel);

        visualisationPluginsPathJTextField = new JTextField(Data.visualisationPluginsPath);
        visualisationPluginsJPanel.add(visualisationPluginsPathJTextField);

        visualisationPluginsPathBrowseButton = new BrowseButton();
        visualisationPluginsPathBrowseButton.addMouseListener(managePluginsMouseListener);
        visualisationPluginsJPanel.add(visualisationPluginsPathBrowseButton);

        loadVisualisationPluginsJButton = new JButton("Load Visualisation Plug-ins");
        loadVisualisationPluginsJButton.addActionListener(managePluginsActionListener);
        loadVisualisationPluginsJButton.setFont(font);
        visualisationPluginsJPanel.add(loadVisualisationPluginsJButton);

        loadedVisualisationPluginsJLabel = new JLabel("Loaded Visualisation Plug-ins:");
        loadedVisualisationPluginsJLabel.setFont(font);
        visualisationPluginsJPanel.add(loadedVisualisationPluginsJLabel);

        loadedVisualisationPluginsJComboBox = new JComboBox();
        loadedVisualisationPluginsJComboBox.setFont(font);
        visualisationPluginsJPanel.add(loadedVisualisationPluginsJComboBox);

        managePluginsComponentListener = new ManagePluginsComponentListener();
        addComponentListener(managePluginsComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // ManagePluginsJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the ManagePluginsJDialog correctly.
        // without this specific order and combination, the
        // ManagePluginsJDialog is initially too short.
        // why this code below appears to work is unknown.
        managePluginsComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                ManagePluginsJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.managePluginsXCoordinate,
                        DialogSizesAndPositions.managePluginsYCoordinate,
                        500,
                        10 + 16 + 10 + managePluginsJTabbedPane.getInsets().top + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + managePluginsJTabbedPane.getInsets().bottom + 10
                        + ManagePluginsJDialog.classInstance.getInsets().top + ManagePluginsJDialog.classInstance.getInsets().bottom);

                // fileParserDisplayNames is declared outside the following try
                // block so that the loadedParsingPluginsJComboBox can use it outside the try
                // block later on
                ArrayList<String> fileParserDisplayNames = new ArrayList<String>();

                try {

                    // this code populates the combo box contained in the
                    // ManagePluginsJDialog.loadedParsingPluginsJComboBox field with the display
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

                            Enumeration<JarEntry> enumeration = new JarFile(
                                    jarFile).entries();

                            URLClassLoader urlClassLoader = URLClassLoader
                                    .newInstance(new URL[]{jarFile.toURI()
                                .toURL()});

                            while (enumeration.hasMoreElements()) {
                                JarEntry jarEntry = enumeration.nextElement();
                                if (jarEntry.getName().endsWith(".class")) {

                                    Class<?> loadedClass = urlClassLoader.loadClass(jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

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

                    exception.printStackTrace();

                    // fileParserDisplayNames is emptied so that the list of
                    // plug-ins in the loadedParsingPluginsJComboBox is empty when an error
                    // occurs
                    fileParserDisplayNames = new ArrayList<String>();

                }

                ManagePluginsJDialog.classInstance.loadedParsingPluginsJComboBox.setModel(new DefaultComboBoxModel(
                        fileParserDisplayNames.toArray(new String[fileParserDisplayNames.size()])));

                // clusteringAlgorithmDisplayNames is declared outside the following try
                // block so that the loadedClusteringPluginsJComboBox can use it outside the try
                // block later on
                ArrayList<String> clusteringAlgorithmDisplayNames = new ArrayList<String>();

                try {

                    // this code populates the combo box contained in the
                    // ManagePluginsJDialog.loadedClusteringPluginsJComboBox field with the display
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

                            File jarFile = new File(Data.clusteringPluginsPath + '/' + filename);

                            Enumeration<JarEntry> enumeration = new JarFile(jarFile).entries();

                            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI()
                                .toURL()});

                            while (enumeration.hasMoreElements()) {
                                JarEntry jarEntry = enumeration.nextElement();
                                if (jarEntry.getName().endsWith(".class")) {

                                    Class<?> loadedClass = urlClassLoader.loadClass(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    if (ClusteringAlgorithmPluginInterface.class.isAssignableFrom(loadedClass)) {

                                        Data.clusteringAlgorithmJarNames.add(filename);
                                        Data.clusteringAlgorithmClassNames.add(
                                                jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                        Object classInstance = loadedClass.newInstance();
                                        Method method = loadedClass.getMethod("getName", new Class[]{});
                                        clusteringAlgorithmDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                    }

                                }
                            }

                        }

                    }

                } catch (Exception exception) {

                    exception.printStackTrace();

                    // clusteringAlgorithmDisplayNames is emptied so that the list of
                    // plug-ins in the loadedClusteringPluginsJComboBox is empty when an error
                    // occurs
                    clusteringAlgorithmDisplayNames = new ArrayList<String>();

                }

                ManagePluginsJDialog.classInstance.loadedClusteringPluginsJComboBox.setModel(new DefaultComboBoxModel(
                        clusteringAlgorithmDisplayNames.toArray(new String[clusteringAlgorithmDisplayNames.size()])));

                // visualisationLayoutDisplayNames is declared outside the following try
                // block so that the loadedVisualisationPluginsJComboBox can use it outside the try
                // block later on
                ArrayList<String> visualisationLayoutDisplayNames = new ArrayList<String>();

                try {

                    // this code populates the combo box contained in the
                    // ManagePluginsJDialog.loadedVisualisationPluginsJComboBox field with the display
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

                            File jarFile = new File(Data.visualisationPluginsPath + '/' + filename);

                            Enumeration<JarEntry> enumeration = new JarFile(jarFile).entries();

                            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});

                            while (enumeration.hasMoreElements()) {
                                JarEntry jarEntry = enumeration.nextElement();
                                if (jarEntry.getName().endsWith(".class")) {

                                    Class<?> loadedClass = urlClassLoader.loadClass(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    if (VisualisationLayoutPluginInterface.class.isAssignableFrom(loadedClass)) {

                                        Data.visualisationLayoutJarNames.add(filename);
                                        Data.visualisationLayoutClassNames.add(
                                                jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                        Object classInstance = loadedClass.newInstance();
                                        Method method = loadedClass.getMethod("getName", new Class[]{});
                                        visualisationLayoutDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                    }

                                }
                            }

                        }

                    }

                } catch (Exception exception) {

                    exception.printStackTrace();

                    // visualisationLayoutDisplayNames is emptied so that the list of
                    // plug-ins in the loadedVisualisationPluginsJComboBox is empty when an error
                    // occurs
                    visualisationLayoutDisplayNames = new ArrayList<String>();

                }

                ManagePluginsJDialog.classInstance.loadedVisualisationPluginsJComboBox.setModel(new DefaultComboBoxModel(
                        visualisationLayoutDisplayNames.toArray(new String[visualisationLayoutDisplayNames.size()])));

            }
        });
        // the setVisible method is called to make the ManagePluginsJDialog
        // appear
        setVisible(true);

    }
}
