package justclust.toolbar.networknodes;

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

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class NetworkNodesTableCellRenderer implements TableCellRenderer {

    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;

    public NetworkNodesTableCellRenderer(boolean isBordered) {
        this.isBordered = isBordered;
    }

    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (column == 2) {

            JLabel jlabel = new JLabel();
            jlabel.setOpaque(true);
            Color newColor = (Color) color;
            jlabel.setBackground(newColor);
            if (isBordered) {
                if (isSelected) {
                    if (selectedBorder == null) {
                        selectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                table.getSelectionBackground());
                    }
                    jlabel.setBorder(selectedBorder);
                } else {
                    if (unselectedBorder == null) {
                        unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                table.getBackground());
                    }
                    jlabel.setBorder(unselectedBorder);
                }
            }

            if (color != null) {
                jlabel.setToolTipText("RGB value: " + newColor.getRed() + ", "
                        + newColor.getGreen() + ", "
                        + newColor.getBlue());
            }

            return jlabel;

        }

        if (column == 3) {
            if (row == 0) {

                JLabel jlabel = new JLabel();
                if (isBordered) {
                    if (isSelected) {
                        if (selectedBorder == null) {
                            selectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                    table.getSelectionBackground());
                        }
                        jlabel.setBorder(selectedBorder);
                        jlabel.setOpaque(true);
                        jlabel.setBackground(table.getSelectionBackground());
                    } else {
                        if (unselectedBorder == null) {
                            unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                    table.getBackground());
                        }
                        jlabel.setBorder(unselectedBorder);
                        jlabel.setOpaque(true);
                        jlabel.setBackground(table.getBackground());
                    }
                }
                return jlabel;

            } else {

                JComboBox jComboBox = new JComboBox();
                jComboBox.addItem("Link to...");
                jComboBox.addItem("UniProtKB...");
                jComboBox.addItem("NCBI...");
                if (isSelected) {
                    if (selectedBorder == null) {
                        selectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                table.getSelectionBackground());
                    }
                    jComboBox.setBorder(selectedBorder);
                } else {
                    if (unselectedBorder == null) {
                        unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1,
                                table.getBackground());
                    }
                    jComboBox.setBorder(unselectedBorder);
                }
                return jComboBox;

            }
        }

        return null;

    }
}
