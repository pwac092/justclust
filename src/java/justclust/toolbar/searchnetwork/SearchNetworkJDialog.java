package justclust.toolbar.searchnetwork;

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
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
import justclust.customcomponents.HelpButton;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class SearchNetworkJDialog extends JDialog {

    public static SearchNetworkJDialog classInstance;
    public SearchNetworkJPanel searchNetworkJPanel;
    public HelpButton searchNetworkHelpButton;
    public JLabel searchJLabel;
    public JTextField searchJTextField;
    public JButton searchJButton;
    public JLabel nodesJLabel;
    public JTable nodesJTable;
    public JScrollPane nodesJScrollPane;
    public JButton nodeOptionsJButton;
    public JLabel edgesJLabel;
    public JTable edgesJTable;
    public JScrollPane edgesJScrollPane;
    public JButton edgeOptionsJButton;
    public JLabel clustersJLabel;
    public JTable clustersJTable;
    public JScrollPane clustersJScrollPane;
    public JButton clusterOptionsJButton;
    SearchNetworkComponentListener searchNetworkComponentListener;

    public SearchNetworkJDialog(JFrame parent) {

        super(parent, "Search Network");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        searchNetworkJPanel = new SearchNetworkJPanel();
        add(searchNetworkJPanel);
        searchNetworkJPanel.setLayout(null);

        SearchNetworkMouseListener searchNetworkMouseListener = new SearchNetworkMouseListener();

        searchNetworkHelpButton = new HelpButton();
        searchNetworkHelpButton.addMouseListener(searchNetworkMouseListener);
        searchNetworkJPanel.add(searchNetworkHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        searchJLabel = new JLabel("Search:");
        searchJLabel.setFont(font);
        searchNetworkJPanel.add(searchJLabel);

        searchJTextField = new JTextField();
        searchNetworkJPanel.add(searchJTextField);

        SearchNetworkActionListener searchNetworkActionListener = new SearchNetworkActionListener();

        searchJButton = new JButton("Search");
        searchJButton.setFont(font);
        searchJButton.addActionListener(searchNetworkActionListener);
        searchNetworkJPanel.add(searchJButton);
        // when the user presses the enter key, the searchJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Search");
        getRootPane().getActionMap().put("Search", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                searchJButton.doClick();
            }
        });

        nodesJLabel = new JLabel("Nodes:");
        nodesJLabel.setFont(font);
        searchNetworkJPanel.add(nodesJLabel);

        String[] columnNames;
        String[][] data;

        columnNames = new String[]{"Label"};
        data = new String[0][columnNames.length];
        nodesJTable = new JTable(data, columnNames);
        nodesJTable.addMouseListener(searchNetworkMouseListener);
        nodesJScrollPane = new JScrollPane(nodesJTable);
        searchNetworkJPanel.add(nodesJScrollPane);

        nodeOptionsJButton = new JButton("Node Options...");
        nodeOptionsJButton.addActionListener(searchNetworkActionListener);
        nodeOptionsJButton.setEnabled(false);
        nodeOptionsJButton.setFont(font);
        searchNetworkJPanel.add(nodeOptionsJButton);

        edgesJLabel = new JLabel("Edges:");
        edgesJLabel.setFont(font);
        searchNetworkJPanel.add(edgesJLabel);

        columnNames = new String[]{"Label", "Node 1", "Node 2", "Weight"};
        data = new String[0][columnNames.length];
        edgesJTable = new JTable(data, columnNames);
        edgesJTable.addMouseListener(searchNetworkMouseListener);
        edgesJScrollPane = new JScrollPane(edgesJTable);
        searchNetworkJPanel.add(edgesJScrollPane);

        edgeOptionsJButton = new JButton("Edge Options...");
        edgeOptionsJButton.addActionListener(searchNetworkActionListener);
        edgeOptionsJButton.setEnabled(false);
        edgeOptionsJButton.setFont(font);
        searchNetworkJPanel.add(edgeOptionsJButton);

        clustersJLabel = new JLabel("Clusters:");
        clustersJLabel.setFont(font);
        searchNetworkJPanel.add(clustersJLabel);

        columnNames = new String[]{"Label"};
        data = new String[0][columnNames.length];
        clustersJTable = new JTable(data, columnNames);
        clustersJTable.addMouseListener(searchNetworkMouseListener);
        clustersJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < clustersJTable.getColumnCount(); i++) {
            clustersJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        clustersJScrollPane = new JScrollPane(clustersJTable);
        searchNetworkJPanel.add(clustersJScrollPane);

        clusterOptionsJButton = new JButton("Cluster Options...");
        clusterOptionsJButton.addActionListener(searchNetworkActionListener);
        clusterOptionsJButton.setEnabled(false);
        clusterOptionsJButton.setFont(font);
        searchNetworkJPanel.add(clusterOptionsJButton);

        searchNetworkComponentListener = new SearchNetworkComponentListener();
        addComponentListener(searchNetworkComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // SearchNetworkJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the SearchNetworkJDialog correctly.
        // without this specific order and combination, the
        // SearchNetworkJDialog is initially too short.
        // why this code below appears to work is unknown.
        searchNetworkComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                SearchNetworkJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.searchNetworkXCoordinate,
                        DialogSizesAndPositions.searchNetworkYCoordinate,
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 1 + 10
                        + getInsets().top + getInsets().bottom);
            }
        });
        // the setVisible method is called to make the SearchNetworkJDialog
        // appear
        setVisible(true);

    }
}
