package justclust.toolbar.networknodes;

import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JColorChooser;
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
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
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
import justclust.graphdrawing.CustomGraphEditorKeyListener;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class NetworkNodesJDialog extends JDialog {

    public static NetworkNodesJDialog classInstance;
    public JPanel networkNodesDialogJPanel;
    public HelpButton networkNodesHelpButton;
    public JTable networkNodesRowHeaderJTable;
    public JTable networkNodesDialogJTable;
    NetworkNodesListSelectionModel networkNodesListSelectionModel;
    public JScrollPane networkNodesDialogJScrollPane;
    NetworkNodesComponentListener networkNodesComponentListener;

    public NetworkNodesJDialog(JFrame parent, int rowToScrollTo, ArrayList<Integer> rowsToHighlight) {

        super(parent, "Network Nodes");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());

        networkNodesDialogJPanel = new JPanel();
        add(networkNodesDialogJPanel);
        networkNodesDialogJPanel.setLayout(null);

        networkNodesHelpButton = new HelpButton();
        NetworkNodesMouseListener networkNodesMouseListener = new NetworkNodesMouseListener();
        networkNodesHelpButton.addMouseListener(networkNodesMouseListener);
        networkNodesDialogJPanel.add(networkNodesHelpButton);

        networkNodesRowHeaderJTable = new JTable(new NetworkNodesRowHeaderTableModel());
        networkNodesRowHeaderJTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        networkNodesRowHeaderJTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        networkNodesRowHeaderJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        networkNodesRowHeaderJTable.setRowHeight(20);
        networkNodesRowHeaderJTable.setBackground(new Color(238, 238, 238));
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        networkNodesRowHeaderJTable.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        networkNodesDialogJTable = new JTable();
        NetworkNodesTableModel networkNodesTableModel = new NetworkNodesTableModel();
        networkNodesDialogJTable.setModel(networkNodesTableModel);
        //Set up renderer and editor
        NetworkNodesTableCellEditor networkNodesTableCellEditor = new NetworkNodesTableCellEditor();
        NetworkNodesTableCellRenderer networkNodesTableCellRenderer = new NetworkNodesTableCellRenderer(true);
        networkNodesDialogJTable.getColumnModel().getColumn(2).setCellEditor(networkNodesTableCellEditor);
        networkNodesDialogJTable.getColumnModel().getColumn(2).setCellRenderer(networkNodesTableCellRenderer);
        networkNodesDialogJTable.getColumnModel().getColumn(3).setCellEditor(networkNodesTableCellEditor);
        networkNodesDialogJTable.getColumnModel().getColumn(3).setCellRenderer(networkNodesTableCellRenderer);
        networkNodesListSelectionModel = new NetworkNodesListSelectionModel();
        networkNodesDialogJTable.setSelectionModel(networkNodesListSelectionModel);
        networkNodesDialogJTable.getColumnModel().getColumn(1).setMaxWidth(50);
        networkNodesDialogJTable.getColumnModel().getColumn(2).setMaxWidth(50);
        networkNodesDialogJTable.setRowHeight(20);
        networkNodesDialogJScrollPane = new JScrollPane(networkNodesDialogJTable);
        networkNodesDialogJScrollPane.setRowHeaderView(networkNodesRowHeaderJTable);
//        networkNodesDialogJTable.setPreferredScrollableViewportSize(networkNodesDialogJTable.getPreferredSize());
        NetworkNodesChangeListener networkNodesChangeListener = new NetworkNodesChangeListener();
        networkNodesDialogJScrollPane.getViewport().addChangeListener(networkNodesChangeListener);
        networkNodesDialogJPanel.add(networkNodesDialogJScrollPane);

        scrollToRowAndHighlightRows(rowToScrollTo, rowsToHighlight);

        networkNodesComponentListener = new NetworkNodesComponentListener();
        addComponentListener(networkNodesComponentListener);

        // the setBounds method must be called after the
        // NetworkNodesComponentListener is registered so that the
        // networkNodesDialogJTable is always visible within the
        // networkNodesDialogJScrollPane.
        // this is for unkown reasons.
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        NetworkNodesJDialog.classInstance.setBounds(
                DialogSizesAndPositions.networkNodesXCoordinate,
                DialogSizesAndPositions.networkNodesYCoordinate,
                DialogSizesAndPositions.networkNodesWidth,
                DialogSizesAndPositions.networkNodesHeight);

        // the setVisible method is called to make the NetworkNodesJDialog
        // appear
        setVisible(true);

    }

    public void scrollToRowAndHighlightRows(int rowToScrollTo, ArrayList<Integer> rowsToHighlight) {

        // scroll to the row with index rowToScrollTo, except if rowToScrollTo
        // is 0 which indicates that the row which was last scrolled to when the
        // NetworkNodesJDialog was closed, should be scrolled to
        if (rowToScrollTo > 0) {
            // first the bottom-most row is scrolled to so that, when the row to
            // scroll to is scrolled to, it will appear at the top of the table
            networkNodesDialogJTable.scrollRectToVisible(new Rectangle(networkNodesDialogJTable.getCellRect(networkNodesDialogJTable.getRowCount() - 1, 0, true)));
            // the scrollRectToVisible method scrolls to the row immediately
            // below the one passed to it when the NetworkNodesJDialog is not
            // already showing for some unkown reason
            if (!this.isShowing()) {
                networkNodesDialogJTable.scrollRectToVisible(new Rectangle(networkNodesDialogJTable.getCellRect(rowToScrollTo - 1, 0, true)));
            } else {
                networkNodesDialogJTable.scrollRectToVisible(new Rectangle(networkNodesDialogJTable.getCellRect(rowToScrollTo, 0, true)));
            }
        } else {
            networkNodesDialogJTable.scrollRectToVisible(
                    new Rectangle(networkNodesDialogJTable.getCellRect(DialogSizesAndPositions.networkNodesRowToScrollTo, 0, true)));
        }

        // highlight the rows with indexes in rowsToHighlight
        networkNodesDialogJTable.getSelectionModel().clearSelection();
        for (Integer integer : rowsToHighlight) {
            networkNodesDialogJTable.getSelectionModel().addSelectionInterval(integer, integer);
        }

    }
}
