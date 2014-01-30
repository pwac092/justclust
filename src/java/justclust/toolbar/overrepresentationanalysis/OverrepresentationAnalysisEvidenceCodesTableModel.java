package justclust.toolbar.overrepresentationanalysis;

import java.awt.Color;
import java.awt.Component;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
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
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.toolbar.networkclusters.NetworkClustersChangeListener;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersListSelectionModel;
import justclust.toolbar.networkclusters.NetworkClustersTableCellEditor;
import justclust.toolbar.networkclusters.NetworkClustersTableCellRenderer;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkedges.NetworkEdgesChangeListener;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesListSelectionModel;
import justclust.toolbar.networkedges.NetworkEdgesTableCellEditor;
import justclust.toolbar.networkedges.NetworkEdgesTableCellRenderer;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkTableCellEditor;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class OverrepresentationAnalysisEvidenceCodesTableModel extends AbstractTableModel {

    String[] columnNames;
    Object[][] tableData;

    public OverrepresentationAnalysisEvidenceCodesTableModel() {
        updateTable();
    }

    public void updateTable() {

        // this code populates the OverrepresentationAnalysisEvidenceCodesTableModel
        columnNames = new String[]{"", "Use Annotations"};
        tableData = new Object[][]{
            {"EXP", true},
            {"IDA", true},
            {"IPI", true},
            {"IMP", true},
            {"IGI", true},
            {"IEP", true},
            {"ISS", false},
            {"ISO", false},
            {"ISA", false},
            {"ISM", false},
            {"IGC", false},
            {"IBA", false},
            {"IBD", false},
            {"IKR", false},
            {"IRD", false},
            {"RCA", false},
            {"TAS", true},
            {"NAS", false},
            {"IC", true},
            {"ND", false},
            {"IEA", false},
            {"NR", false}};

    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return tableData.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        
        //Note that the tableData/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 0) {
            return false;
        } else {
            return true;
        }
        
    }

    // this method is called when the user inputs tableData into the table
    public void setValueAt(Object value, int row, int col) {

        // update the cell which was directly input into
        tableData[row][col] = value;
        // fireTableCellUpdated updates the appearance of the cell
        fireTableCellUpdated(row, col);

    }
}