package justclust.toolbar.networknodes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class NetworkNodesTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    static NetworkNodesTableCellEditor classInstance;
    Color currentColor;
    JButton button;
    int currentRow;
    boolean okButtonClicked;

    public NetworkNodesTableCellEditor() {
        classInstance = this;
        //Set up the editor (from the table's point of view),
        //which is a button.
        //This button brings up the color chooser dialog,
        //which is the editor from the user's point of view.
        button = new JButton();
        button.addActionListener(this);
        button.setBorderPainted(false);
    }

    /**
     * Handles events from the editor button and from the dialog's OK button.
     */
    public void actionPerformed(ActionEvent e) {

        if (NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getEditingColumn() == 2) {

            //The user has clicked the cell, so
            //bring up the dialog.
            NetworkNodesColourJDialog networkNodesColourJDialog = new NetworkNodesColourJDialog();

            if (okButtonClicked) {
                currentColor = networkNodesColourJDialog.jColorChooser.getColor();
            }

            button.setBackground(currentColor);

        }

        //Make the renderer reappear.
        fireEditingStopped();

    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentColor;
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        if (column == 2) {
            currentColor = (Color) value;
            button.setBackground(currentColor);
            currentRow = row;
            return button;
        }
        if (column == 3) {
            JComboBox jComboBox = new JComboBox();
            jComboBox.addItem("Link to...");
            jComboBox.addItem("UniProtKB...");
            jComboBox.addItem("NCBI...");
            jComboBox.addActionListener(new NetworkNodesActionListener());
            return jComboBox;
        }
        return null;

    }
}
