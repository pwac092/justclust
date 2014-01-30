package justclust;

import justclust.menubar.SaveJMenuActionListener;
import justclust.menubar.LoadJMenuActionListener;
import edu.umd.cs.piccolo.PLayer;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import justclust.menubar.clusternetwork.ClusterNetworkJDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SplashScreen;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.graphdrawing.CustomGraphEditorMouseListener;
import justclust.menubar.ColouriseClustersJMenuActionListener;
import justclust.menubar.ExportJMenuActionListener;
import justclust.toolbar.manageplugins.ManagePluginsJPanel;

/**
 * This class has an instance which acts as a frame.
 */
public class JustclustJFrame extends JFrame {

    /**
     * This field contains the instance of this class.
     */
    public static JustclustJFrame classInstance;
    /**
     * This field contains a panel for a JustclustJFrame.
     */
    public JustclustJPanel justclustJPanel;
    /**
     * This field contains a menu bar for a JustclustJFrame.
     */
    public JMenuBar justclustJMenuBar;
    /**
     * This field contains menu for clustering the current network or exiting
     * JustClust.
     */
    public JMenu justclustJMenu;
    public JMenuItem newNetworkJMenuItem;
    /**
     * This field contains menu for loading a network or a clustering.
     */
    public JMenu loadJMenu;
    public JMenuItem loadSessionJMenuItem;
    /**
     * This field contains menu for saving a network, a clustering, or a graph
     * image.
     */
    public JMenu saveJMenu;
    public JMenuItem saveSessionJMenuItem;
    public JMenuItem exportGraphJMenuItem;
    public JMenu exportJMenu;
    public JMenuItem exportNetworkJMenuItem;
    public JMenuItem exportClusteringJMenuItem;
    public JMenuItem exitJMenuItem;
    public JMenuItem clusterNetworkJMenuItem;
    public JMenuItem applyLayoutJMenuItem;
    public JMenu colouriseClustersJMenu;
    public JMenuItem applyColourToNodesJMenuItem;
    public JMenuItem applyColourToEdgesJMenuItem;
    public JMenuItem applyColourToNodesAndEdgesJMenuItem;
    public JMenuItem aboutJMenuItem;
    public JustclustChangeListener justclustChangeListener;
    public JTabbedPane justclustJTabbedPane;
    public ArrayList<String> tabTitles;
    public ArrayList<CustomGraphEditor> customGraphEditors;
    /**
     * This field contains a label for information about the status of
     * JustClust.
     */
    public JLabel statusBarJLabel;

    /**
     * This constructor initialises the fields of this class.
     */
    public JustclustJFrame() {

        classInstance = this;

        // wait while the splash screen is displayed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JustClust");
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();
        setBounds((int) Math.round((double) devices[0].getDisplayMode().getWidth() / 8),
                (int) Math.round((double) devices[0].getDisplayMode().getHeight() / 8),
                (int) Math.round((double) devices[0].getDisplayMode().getWidth() * 3 / 4),
                (int) Math.round((double) devices[0].getDisplayMode().getHeight() * 3 / 4));
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        JustclustMouseListener justclustMouseListener = new JustclustMouseListener();

        justclustJPanel = new JustclustJPanel();
        add(justclustJPanel);
        justclustJPanel.setLayout(null);
        // register the JustclustJPanel with a
        // JustclustActionListener so that the
        // JustclustActionListener can respond to buttons on the
        // information toolbar
        justclustJPanel.addMouseListener(justclustMouseListener);
        // passing an empty String to the setToolTipText method enables tooltips
        // for justclustJPanel
        justclustJPanel.setToolTipText("");

        Font font = new Font("Dialog", Font.PLAIN, 12);

        // set the font of the menu bar
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);

        justclustJMenuBar = new JMenuBar();
        setJMenuBar(justclustJMenuBar);

        justclustJMenu = new JMenu("File");
        justclustJMenuBar.add(justclustJMenu);
        justclustJMenu.setMnemonic(KeyEvent.VK_F);

        JustclustActionListener justclustActionListener = new JustclustActionListener();

        newNetworkJMenuItem = new JMenuItem("New Network from File...", KeyEvent.VK_N);
        justclustJMenu.add(newNetworkJMenuItem);
        newNetworkJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newNetworkJMenuItem.addActionListener(justclustActionListener);

        justclustJMenu.addSeparator();

        loadJMenu = new JMenu("Load");
        justclustJMenu.add(loadJMenu);
        loadJMenu.setMnemonic(KeyEvent.VK_L);

        LoadJMenuActionListener loadJMenuActionListener = new LoadJMenuActionListener();

        loadSessionJMenuItem = new JMenuItem("Session...", KeyEvent.VK_S);
        loadJMenu.add(loadSessionJMenuItem);
        loadSessionJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        loadSessionJMenuItem.addActionListener(loadJMenuActionListener);

        saveJMenu = new JMenu("Save");
        justclustJMenu.add(saveJMenu);
        saveJMenu.setMnemonic(KeyEvent.VK_S);

        SaveJMenuActionListener saveJMenuActionListener = new SaveJMenuActionListener();

        saveSessionJMenuItem = new JMenuItem("Session...", KeyEvent.VK_S);
        saveJMenu.add(saveSessionJMenuItem);
        saveSessionJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK));
        saveSessionJMenuItem.addActionListener(saveJMenuActionListener);

        exportJMenu = new JMenu("Export");
        justclustJMenu.add(exportJMenu);
        exportJMenu.setMnemonic(KeyEvent.VK_E);

        ExportJMenuActionListener exportJMenuActionListener = new ExportJMenuActionListener();

        exportNetworkJMenuItem = new JMenuItem("Network as Text File...", KeyEvent.VK_N);
        exportJMenu.add(exportNetworkJMenuItem);
        exportNetworkJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        exportNetworkJMenuItem.addActionListener(exportJMenuActionListener);
        exportNetworkJMenuItem.setEnabled(false);

        exportClusteringJMenuItem = new JMenuItem("Clustering as Text File...", KeyEvent.VK_C);
        exportJMenu.add(exportClusteringJMenuItem);
        exportClusteringJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        exportClusteringJMenuItem.addActionListener(exportJMenuActionListener);
        exportClusteringJMenuItem.setEnabled(false);

        exportGraphJMenuItem = new JMenuItem("Graph as Image File...", KeyEvent.VK_G);
        exportJMenu.add(exportGraphJMenuItem);
        exportGraphJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        exportGraphJMenuItem.addActionListener(exportJMenuActionListener);
        exportGraphJMenuItem.setEnabled(false);

        justclustJMenu.addSeparator();

        exitJMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        justclustJMenu.add(exitJMenuItem);
        exitJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exitJMenuItem.addActionListener(justclustActionListener);

        justclustJMenu = new JMenu("Cluster Analysis");
        justclustJMenuBar.add(justclustJMenu);
        justclustJMenu.setMnemonic(KeyEvent.VK_C);

        clusterNetworkJMenuItem = new JMenuItem("Cluster Network...", KeyEvent.VK_C);
        justclustJMenu.add(clusterNetworkJMenuItem);
        clusterNetworkJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        clusterNetworkJMenuItem.addActionListener(justclustActionListener);
        clusterNetworkJMenuItem.setEnabled(false);

        justclustJMenu = new JMenu("Visualisation");
        justclustJMenuBar.add(justclustJMenu);
        justclustJMenu.setMnemonic(KeyEvent.VK_V);

        applyLayoutJMenuItem = new JMenuItem("Apply Layout...");
        justclustJMenu.add(applyLayoutJMenuItem);
        applyLayoutJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        applyLayoutJMenuItem.addActionListener(justclustActionListener);
        applyLayoutJMenuItem.setEnabled(false);

        justclustJMenu.addSeparator();

        ColouriseClustersJMenuActionListener colouriseClustersJMenuActionListener = new ColouriseClustersJMenuActionListener();

        colouriseClustersJMenu = new JMenu("Colourise Clusters");
        justclustJMenu.add(colouriseClustersJMenu);
        colouriseClustersJMenu.setMnemonic(KeyEvent.VK_C);
        colouriseClustersJMenu.setEnabled(false);

        applyColourToNodesJMenuItem = new JMenuItem("Apply Colour to Nodes");
        colouriseClustersJMenu.add(applyColourToNodesJMenuItem);
        applyColourToNodesJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
        applyColourToNodesJMenuItem.addActionListener(colouriseClustersJMenuActionListener);

        applyColourToEdgesJMenuItem = new JMenuItem("Apply Colour to Edges");
        colouriseClustersJMenu.add(applyColourToEdgesJMenuItem);
        applyColourToEdgesJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
        applyColourToEdgesJMenuItem.addActionListener(colouriseClustersJMenuActionListener);

        applyColourToNodesAndEdgesJMenuItem = new JMenuItem("Apply Colour to Nodes and Edges");
        colouriseClustersJMenu.add(applyColourToNodesAndEdgesJMenuItem);
        applyColourToNodesAndEdgesJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.SHIFT_MASK));
        applyColourToNodesAndEdgesJMenuItem.addActionListener(colouriseClustersJMenuActionListener);

        justclustJMenu = new JMenu("Help");
        justclustJMenuBar.add(justclustJMenu);
        justclustJMenu.setMnemonic(KeyEvent.VK_H);

        aboutJMenuItem = new JMenuItem("About...");
        justclustJMenu.add(aboutJMenuItem);
        aboutJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        aboutJMenuItem.addActionListener(justclustActionListener);

        justclustChangeListener = new JustclustChangeListener();
        
        justclustJTabbedPane = new JTabbedPane();
        justclustJTabbedPane.setFont(font);
        justclustJTabbedPane.addChangeListener(justclustChangeListener);
        TabTitleEditListener tabTitleEditListener = new TabTitleEditListener(justclustJTabbedPane);
        justclustJTabbedPane.addChangeListener(tabTitleEditListener);
        justclustJTabbedPane.addMouseListener(tabTitleEditListener);
        justclustJPanel.add(justclustJTabbedPane);

        tabTitles = new ArrayList<String>();

        customGraphEditors = new ArrayList<CustomGraphEditor>();
//        Graph customGraphEditor = new Graph();
//        customGraphEditors.add(customGraphEditor);
        // the CustomBirdsEyeView is no longer used
//        customBirdsEyeView = new CustomBirdsEyeView();
//        // the customBirdsEyeView is added to the justclustJPanel before the
//        // customGraphEditor so that the customBirdsEyeView covers the
//        // customGraphEditor
//        justclustJPanel.add(customBirdsEyeView);
//        justclustJPanel.add(customGraphEditor);
//        // register the customGraphEditor with a
//        // JustclustActionListener so that the
//        // JustclustActionListener can respond to buttons on the main
//        // graphical view being clicked
//        customGraphEditor.addMouseListener(justclustMouseListener);
//        // passing an empty String to the setToolTipText method enables tooltips
//        // for customGraphEditor
//        customGraphEditor.setToolTipText("");
//        justclustJTabbedPane.add("Network", customGraphEditor);

        statusBarJLabel = new JLabel();
        statusBarJLabel.setFont(font);
        justclustJPanel.add(statusBarJLabel);
        statusBarJLabel.setHorizontalAlignment(JLabel.CENTER);
        statusBarJLabel.setBorder(new EtchedBorder());

        JustclustComponentListener justclustJFrameComponentListener = new JustclustComponentListener();
        addComponentListener(justclustJFrameComponentListener);

    }
}
