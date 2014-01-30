package justclust.toolbar.networkedges;

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

public class NetworkEdgesJDialog extends JDialog {

    public static NetworkEdgesJDialog classInstance;
    public JPanel networkEdgesDialogJPanel;
    public HelpButton networkEdgesHelpButton;
    public JTable networkEdgesRowHeaderJTable;
    public JTable networkEdgesDialogJTable;
    NetworkEdgesListSelectionModel networkEdgesListSelectionModel;
    public JScrollPane networkEdgesDialogJScrollPane;
    NetworkEdgesComponentListener networkEdgesComponentListener;

    public NetworkEdgesJDialog(JFrame parent, int rowToScrollTo, ArrayList<Integer> rowsToHighlight) {
        
        super(parent, "Network Edges");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        networkEdgesDialogJPanel = new JPanel();
        add(networkEdgesDialogJPanel);
        networkEdgesDialogJPanel.setLayout(null);

        networkEdgesHelpButton = new HelpButton();
        NetworkEdgesMouseListener networkEdgesMouseListener = new NetworkEdgesMouseListener();
        networkEdgesHelpButton.addMouseListener(networkEdgesMouseListener);
        networkEdgesDialogJPanel.add(networkEdgesHelpButton);

        networkEdgesRowHeaderJTable = new JTable(new NetworkEdgesRowHeaderTableModel());
        networkEdgesRowHeaderJTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        networkEdgesRowHeaderJTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        networkEdgesRowHeaderJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        networkEdgesRowHeaderJTable.setRowHeight(20);
        networkEdgesRowHeaderJTable.setBackground(new Color(238, 238, 238));
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        networkEdgesRowHeaderJTable.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        networkEdgesRowHeaderJTable.setEnabled(false);
        networkEdgesDialogJTable = new JTable();
        networkEdgesDialogJTable.setModel(new NetworkEdgesTableModel());
        //Set up renderer and editor
        NetworkEdgesTableCellEditor networkEdgesTableCellEditor = new NetworkEdgesTableCellEditor();
        NetworkEdgesTableCellRenderer networkEdgesTableCellRenderer = new NetworkEdgesTableCellRenderer(true);
        networkEdgesDialogJTable.getColumnModel().getColumn(2).setCellEditor(networkEdgesTableCellEditor);
        networkEdgesDialogJTable.getColumnModel().getColumn(2).setCellRenderer(networkEdgesTableCellRenderer);
        networkEdgesListSelectionModel = new NetworkEdgesListSelectionModel();
        networkEdgesDialogJTable.setSelectionModel(networkEdgesListSelectionModel);
        networkEdgesDialogJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < networkEdgesDialogJTable.getColumnCount(); i++) {
            networkEdgesDialogJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        networkEdgesDialogJTable.getColumnModel().getColumn(1).setMaxWidth(50);
        networkEdgesDialogJTable.getColumnModel().getColumn(2).setMaxWidth(50);
        networkEdgesDialogJTable.setRowHeight(20);
        networkEdgesDialogJScrollPane = new JScrollPane(networkEdgesDialogJTable);
        networkEdgesDialogJScrollPane.setRowHeaderView(networkEdgesRowHeaderJTable);
        NetworkEdgesChangeListener networkEdgesChangeListener = new NetworkEdgesChangeListener();
        networkEdgesDialogJScrollPane.getViewport().addChangeListener(networkEdgesChangeListener);
        networkEdgesDialogJPanel.add(networkEdgesDialogJScrollPane);

        scrollToRowAndHighlightRows(rowToScrollTo, rowsToHighlight);

        networkEdgesComponentListener = new NetworkEdgesComponentListener();
        addComponentListener(networkEdgesComponentListener);

        // the setBounds method must be called after the
        // NetworkEdgesComponentListener is registered so that the
        // networkEdgesDialogJTable is always visible within the
        // networkEdgesDialogJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkEdgesJDialog.classInstance.setBounds(
                DialogSizesAndPositions.networkEdgesXCoordinate,
                DialogSizesAndPositions.networkEdgesYCoordinate,
                DialogSizesAndPositions.networkEdgesWidth,
                DialogSizesAndPositions.networkEdgesHeight);

        // the setVisible method is called to make the NetworkEdgesJDialog
        // appear
        setVisible(true);

    }

    public void scrollToRowAndHighlightRows(int rowToScrollTo, ArrayList<Integer> rowsToHighlight) {

        // scroll to the row with index rowToScrollTo, except if rowToScrollTo
        // is 0 which indicates that the row which was last scrolled to when the
        // NetworkEdgesJDialog was closed, should be scrolled to
        if (rowToScrollTo > 0) {
            // first the bottom-most row is scrolled to so that, when the row to
            // scroll to is scrolled to, it will appear at the top of the table
            networkEdgesDialogJTable.scrollRectToVisible(new Rectangle(networkEdgesDialogJTable.getCellRect(networkEdgesDialogJTable.getRowCount() - 1, 0, true)));
            networkEdgesDialogJTable.scrollRectToVisible(new Rectangle(networkEdgesDialogJTable.getCellRect(rowToScrollTo, 0, true)));
            // the scrollRectToVisible method scrolls to the row immediately
            // below the one passed to it when the NetworkEdgesJDialog is not
            // already showing for some unkown reason
            if (!this.isShowing()) {
                networkEdgesDialogJTable.scrollRectToVisible(new Rectangle(networkEdgesDialogJTable.getCellRect(rowToScrollTo - 1, 0, true)));
            } else {
                networkEdgesDialogJTable.scrollRectToVisible(new Rectangle(networkEdgesDialogJTable.getCellRect(rowToScrollTo, 0, true)));
            }
        } else {
            networkEdgesDialogJTable.scrollRectToVisible(
                    new Rectangle(networkEdgesDialogJTable.getCellRect(DialogSizesAndPositions.networkEdgesRowToScrollTo, 0, true)));
        }

        // highlight the rows with indexes in rowsToHighlight
        networkEdgesDialogJTable.getSelectionModel().clearSelection();
        for (Integer integer : rowsToHighlight) {
            networkEdgesDialogJTable.getSelectionModel().addSelectionInterval(integer, integer);
        }

    }
}
