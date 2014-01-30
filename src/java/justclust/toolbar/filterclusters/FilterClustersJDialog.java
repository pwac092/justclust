package justclust.toolbar.filterclusters;

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

public class FilterClustersJDialog extends JDialog {

    public static FilterClustersJDialog classInstance;
    public FilterClustersJPanel filterClustersJPanel;
    public HelpButton filterClustersHelpButton;
    public JLabel showLargestClustersJLabel;
    public JTextField showLargestClustersJTextField;
    public JLabel hideSmallestClustersJLabel;
    public JTextField hideSmallestClustersJTextField;
    public JLabel hideClustersAboveNodeAmountJLabel;
    public JTextField hideClustersAboveNodeAmountJTextField;
    public JLabel hideClustersBelowNodeAmountJLabel;
    public JTextField hideClustersBelowNodeAmountJTextField;
    public JLabel hideClustersBelowDensityThresholdJLabel;
    public JTextField hideClustersBelowDensityThresholdJTextField;
    public JButton filterClustersJButton;

    public FilterClustersJDialog(JFrame parent) {

        super(parent, "Filter Clusters");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        filterClustersJPanel = new FilterClustersJPanel();
        add(filterClustersJPanel);
        filterClustersJPanel.setLayout(null);

        filterClustersHelpButton = new HelpButton();
        FilterClustersMouseListener filterClustersMouseListener = new FilterClustersMouseListener();
        filterClustersHelpButton.addMouseListener(filterClustersMouseListener);
        filterClustersJPanel.add(filterClustersHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        showLargestClustersJLabel = new JLabel("Show Largest Clusters:");
        showLargestClustersJLabel.setFont(font);
        filterClustersJPanel.add(showLargestClustersJLabel);

        FilterClustersKeyListener filterClustersKeyListener = new FilterClustersKeyListener();

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data = Data.data.get(currentCustomGraphEditorIndex);

        showLargestClustersJTextField = new JTextField(String.valueOf(data.networkClusters.size()));
        showLargestClustersJTextField.addKeyListener(filterClustersKeyListener);
        filterClustersJPanel.add(showLargestClustersJTextField);

        hideSmallestClustersJLabel = new JLabel("Hide Smallest Clusters:");
        hideSmallestClustersJLabel.setFont(font);
        filterClustersJPanel.add(hideSmallestClustersJLabel);

        hideSmallestClustersJTextField = new JTextField("0");
        hideSmallestClustersJTextField.addKeyListener(filterClustersKeyListener);
        filterClustersJPanel.add(hideSmallestClustersJTextField);

        hideClustersAboveNodeAmountJLabel = new JLabel("Hide Clusters Above Node Amount:");
        hideClustersAboveNodeAmountJLabel.setFont(font);
        filterClustersJPanel.add(hideClustersAboveNodeAmountJLabel);

        hideClustersAboveNodeAmountJTextField = new JTextField();
        // the Clusters are sorted by size so the first cluster has the most
        // nodes
        if (data.networkClusters.size() >= 1) {
            hideClustersAboveNodeAmountJTextField.setText(String.valueOf(
                    data.networkClusters.get(0).nodes.size()));
        }
        filterClustersJPanel.add(hideClustersAboveNodeAmountJTextField);

        hideClustersBelowNodeAmountJLabel = new JLabel("Hide Clusters Below Node Amount:");
        hideClustersBelowNodeAmountJLabel.setFont(font);
        filterClustersJPanel.add(hideClustersBelowNodeAmountJLabel);

        hideClustersBelowNodeAmountJTextField = new JTextField("1");
        filterClustersJPanel.add(hideClustersBelowNodeAmountJTextField);

        hideClustersBelowDensityThresholdJLabel = new JLabel("Hide Clusters Below Density Threshold:");
        hideClustersBelowDensityThresholdJLabel.setFont(font);
        filterClustersJPanel.add(hideClustersBelowDensityThresholdJLabel);

        hideClustersBelowDensityThresholdJTextField = new JTextField("0");
        filterClustersJPanel.add(hideClustersBelowDensityThresholdJTextField);

        filterClustersJButton = new JButton("Filter Clusters");
        filterClustersJButton.setFont(font);
        FilterClustersActionListener filterClustersActionListener = new FilterClustersActionListener();
        filterClustersJButton.addActionListener(filterClustersActionListener);
        filterClustersJPanel.add(filterClustersJButton);
        // when the user presses the enter key, the filterClustersJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Filter Clusters");
        getRootPane().getActionMap().put("Filter Clusters", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                filterClustersJButton.doClick();
            }
        });

        FilterClustersComponentListener filterClustersComponentListener = new FilterClustersComponentListener();
        addComponentListener(filterClustersComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // FilterClustersJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the FilterClustersJDialog correctly.
        // without this specific order and combination, the
        // FilterClustersJDialog is initially too short.
        // why this code below appears to work is unknown.
        filterClustersComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                FilterClustersJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.filterClustersXCoordinate,
                        DialogSizesAndPositions.filterClustersYCoordinate,
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + getInsets().top + getInsets().bottom);
            }
        });
        // the setVisible method is called to make the FilterClustersJDialog
        // appear
        setVisible(true);

    }
}
