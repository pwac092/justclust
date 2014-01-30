package justclust.toolbar.networkclusters;

import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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

public class NetworkClustersJDialog extends JDialog {

    public static NetworkClustersJDialog classInstance;
    public JPanel networkClustersDialogJPanel;
    public HelpButton networkClustersHelpButton;
    public JTable networkClustersRowHeaderJTable;
    public JTable networkClustersDialogJTable;
    NetworkClustersListSelectionModel networkClustersListSelectionModel;
    public JScrollPane networkClustersDialogJScrollPane;
    NetworkClustersComponentListener networkClustersComponentListener;

    public NetworkClustersJDialog(JFrame parent, int rowToScrollTo, ArrayList<Integer> rowsToHighlight) {

        super(parent, "Network Clusters");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        networkClustersDialogJPanel = new JPanel();
        add(networkClustersDialogJPanel);
        networkClustersDialogJPanel.setLayout(null);

        networkClustersHelpButton = new HelpButton();
        NetworkClustersMouseListener networkClustersMouseListener = new NetworkClustersMouseListener();
        networkClustersHelpButton.addMouseListener(networkClustersMouseListener);
        networkClustersDialogJPanel.add(networkClustersHelpButton);

        networkClustersRowHeaderJTable = new JTable(new NetworkClustersRowHeaderTableModel());
        networkClustersRowHeaderJTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        networkClustersRowHeaderJTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        networkClustersRowHeaderJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        networkClustersRowHeaderJTable.setRowHeight(20);
        networkClustersRowHeaderJTable.setBackground(new Color(238, 238, 238));
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        networkClustersRowHeaderJTable.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        networkClustersRowHeaderJTable.setEnabled(false);
        networkClustersDialogJTable = new JTable();
        networkClustersDialogJTable.setModel(new NetworkClustersTableModel());
        //Set up renderer and editor
        NetworkClustersTableCellEditor networkClustersTableCellEditor = new NetworkClustersTableCellEditor();
        NetworkClustersTableCellRenderer networkClustersTableCellRenderer = new NetworkClustersTableCellRenderer(true);
        networkClustersDialogJTable.getColumnModel().getColumn(3).setCellEditor(networkClustersTableCellEditor);
        networkClustersDialogJTable.getColumnModel().getColumn(3).setCellRenderer(networkClustersTableCellRenderer);
        networkClustersDialogJTable.getColumnModel().getColumn(4).setCellEditor(networkClustersTableCellEditor);
        networkClustersDialogJTable.getColumnModel().getColumn(4).setCellRenderer(networkClustersTableCellRenderer);
        networkClustersListSelectionModel = new NetworkClustersListSelectionModel();
        networkClustersDialogJTable.setSelectionModel(networkClustersListSelectionModel);
        networkClustersDialogJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < networkClustersDialogJTable.getColumnCount(); i++) {
            networkClustersDialogJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        networkClustersDialogJTable.setRowHeight(20);
        networkClustersDialogJScrollPane = new JScrollPane(networkClustersDialogJTable);
        networkClustersDialogJScrollPane.setRowHeaderView(networkClustersRowHeaderJTable);
        NetworkClustersChangeListener networkClustersChangeListener = new NetworkClustersChangeListener();
        networkClustersDialogJScrollPane.getViewport().addChangeListener(networkClustersChangeListener);
        networkClustersDialogJPanel.add(networkClustersDialogJScrollPane);

        // scroll to the row with index rowToScrollTo, except if rowToScrollTo
        // is 0 which indicates that the row which was last scrolled to when the
        // NetworkClustersJDialog was closed, should be scrolled to
        if (rowToScrollTo > 0) {
            networkClustersDialogJTable.scrollRectToVisible(new Rectangle(networkClustersDialogJTable.getCellRect(rowToScrollTo, 0, true)));
        } else {
            networkClustersDialogJTable.scrollRectToVisible(
                    new Rectangle(networkClustersDialogJTable.getCellRect(DialogSizesAndPositions.networkClustersRowToScrollTo, 0, true)));
        }

        // highlight the rows with indexes in rowsToHighlight
        for (Integer integer : rowsToHighlight) {
            networkClustersDialogJTable.getSelectionModel().addSelectionInterval(integer, integer);
        }

        networkClustersComponentListener = new NetworkClustersComponentListener();
        addComponentListener(networkClustersComponentListener);

        // the setBounds method must be called after the
        // NetworkClustersComponentListener is registered so that the
        // networkClustersDialogJTable is always visible within the
        // networkClustersDialogJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkClustersJDialog.classInstance.setBounds(
                DialogSizesAndPositions.networkClustersXCoordinate,
                DialogSizesAndPositions.networkClustersYCoordinate,
                DialogSizesAndPositions.networkClustersWidth,
                DialogSizesAndPositions.networkClustersHeight);

        // the setVisible method is called to make the NetworkClustersJDialog
        // appear
        setVisible(true);

    }
}
