package justclust.toolbar.overrepresentationanalysis;

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
import java.io.File;
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
import justclust.customcomponents.BrowseButton;
import justclust.customcomponents.HelpButton;
import justclust.menubar.clusternetwork.ClusterNetworkJDialog;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public class OverrepresentationAnalysisJDialog extends JDialog {

    public static OverrepresentationAnalysisJDialog classInstance;
    public OverrepresentationAnalysisJPanel overrepresentationAnalysisJPanel;
    public HelpButton overrepresentationAnalysisHelpButton;
    public JLabel geneOntologyJLabel;
    public JTextField geneOntologyJTextField;
    public BrowseButton geneOntologyBrowseButton;
    public JLabel geneOntologyAnnotationsJLabel;
    public JTextField geneOntologyAnnotationsJTextField;
    public BrowseButton geneOntologyAnnotationsBrowseButton;
    public JLabel evidenceCodesJLabel;
    public JTable evidenceCodesJTable;
    public JScrollPane evidenceCodesJScrollPane;
    public JLabel significanceValueJLabel;
    public JTextField significanceValueJTextField;
    public JLabel multipleHypothesisTestingCorrectionJLabel;
    public JComboBox multipleHypothesisTestingCorrectionJComboBox;
    public JLabel clusterToAnalyseJLabel;
    public JTable clusterToAnalyseJTable;
    public JScrollPane clusterToAnalyseJScrollPane;
    public JButton analyseOverrepresentationJButton;
    public JLabel functionsForClusterJLabel;
    public JTable functionsForClusterJTable;
    public JScrollPane functionsForClusterJScrollPane;
//    // this field contains the current gene ontology file
//    public File geneOntologyFile;
//    // this field contains the current gene ontology annotations file
//    public File geneOntologyAnnotationsFile;

    public OverrepresentationAnalysisJDialog(JFrame parent) {

        super(parent, "Over-representation Analysis");

        classInstance = this;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("img/justclust_icon.png");
        setIconImage(img.getImage());
        setResizable(false);

        overrepresentationAnalysisJPanel = new OverrepresentationAnalysisJPanel();
        add(overrepresentationAnalysisJPanel);
        overrepresentationAnalysisJPanel.setLayout(null);

        OverrepresentationAnalysisMouseListener overrepresentationAnalysisMouseListener = new OverrepresentationAnalysisMouseListener();

        overrepresentationAnalysisHelpButton = new HelpButton();
        overrepresentationAnalysisHelpButton.addMouseListener(overrepresentationAnalysisMouseListener);
        overrepresentationAnalysisJPanel.add(overrepresentationAnalysisHelpButton);

        Font font = new Font("Dialog", Font.PLAIN, 12);

        geneOntologyJLabel = new JLabel("Gene Ontology:");
        geneOntologyJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(geneOntologyJLabel);

        geneOntologyJTextField = new JTextField();
        overrepresentationAnalysisJPanel.add(geneOntologyJTextField);

        geneOntologyBrowseButton = new BrowseButton();
        geneOntologyBrowseButton.addMouseListener(overrepresentationAnalysisMouseListener);
        overrepresentationAnalysisJPanel.add(geneOntologyBrowseButton);

        geneOntologyAnnotationsJLabel = new JLabel("Gene Ontology Annotations:");
        geneOntologyAnnotationsJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(geneOntologyAnnotationsJLabel);

        geneOntologyAnnotationsJTextField = new JTextField();
        overrepresentationAnalysisJPanel.add(geneOntologyAnnotationsJTextField);

        geneOntologyAnnotationsBrowseButton = new BrowseButton();
        geneOntologyAnnotationsBrowseButton.addMouseListener(overrepresentationAnalysisMouseListener);
        overrepresentationAnalysisJPanel.add(geneOntologyAnnotationsBrowseButton);

        evidenceCodesJLabel = new JLabel("Evidence Codes:");
        evidenceCodesJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(evidenceCodesJLabel);

        evidenceCodesJTable = new JTable();
        OverrepresentationAnalysisEvidenceCodesTableModel overrepresentationAnalysisEvidenceCodesTableModel = new OverrepresentationAnalysisEvidenceCodesTableModel();
        evidenceCodesJTable.setModel(overrepresentationAnalysisEvidenceCodesTableModel);
        evidenceCodesJScrollPane = new JScrollPane(evidenceCodesJTable);
        overrepresentationAnalysisJPanel.add(evidenceCodesJScrollPane);

        significanceValueJLabel = new JLabel("Significance Value:");
        significanceValueJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(significanceValueJLabel);

        significanceValueJTextField = new JTextField("0.05");
        overrepresentationAnalysisJPanel.add(significanceValueJTextField);

        multipleHypothesisTestingCorrectionJLabel = new JLabel("Multiple Hypothesis Testing Correction:");
        multipleHypothesisTestingCorrectionJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(multipleHypothesisTestingCorrectionJLabel);

        String[] items = new String[]{"No correction", "Bonferroni correction", "Hochberg's step-up procedure"};
        multipleHypothesisTestingCorrectionJComboBox = new JComboBox(items);
        overrepresentationAnalysisJPanel.add(multipleHypothesisTestingCorrectionJComboBox);

        clusterToAnalyseJLabel = new JLabel("Cluster to Analyse:");
        clusterToAnalyseJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(clusterToAnalyseJLabel);

        clusterToAnalyseJTable = new JTable();
        OverrepresentationAnalysisClusterToAnalyseTableModel overrepresentationAnalysisClusterToAnalyseTableModel = new OverrepresentationAnalysisClusterToAnalyseTableModel();
        clusterToAnalyseJTable.setModel(overrepresentationAnalysisClusterToAnalyseTableModel);
        clusterToAnalyseJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        clusterToAnalyseJTable.setEnabled(false);
        for (int i = 0; i < clusterToAnalyseJTable.getColumnCount(); i++) {
            clusterToAnalyseJTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        clusterToAnalyseJScrollPane = new JScrollPane(clusterToAnalyseJTable);
        overrepresentationAnalysisJPanel.add(clusterToAnalyseJScrollPane);

        analyseOverrepresentationJButton = new JButton("Analyse Over-representation");
        analyseOverrepresentationJButton.setFont(font);
        OverrepresentationAnalysisActionListener overrepresentationAnalysisActionListener = new OverrepresentationAnalysisActionListener();
        analyseOverrepresentationJButton.addActionListener(overrepresentationAnalysisActionListener);
        overrepresentationAnalysisJPanel.add(analyseOverrepresentationJButton);
        // when the user presses the enter key, the analyseOverrepresentationJButton will be
        // triggered
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Analyse Over-representation");
        getRootPane().getActionMap().put("Analyse Over-representation", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                analyseOverrepresentationJButton.doClick();
            }
        });

        functionsForClusterJLabel = new JLabel("Functions for Cluster:");
        functionsForClusterJLabel.setFont(font);
        overrepresentationAnalysisJPanel.add(functionsForClusterJLabel);

        String[] columnNames = new String[]{"Function", "P-value"};
        String[][] tableData = new String[0][columnNames.length];
        functionsForClusterJTable = new JTable(tableData, columnNames);
        functionsForClusterJScrollPane = new JScrollPane(functionsForClusterJTable);
        overrepresentationAnalysisJPanel.add(functionsForClusterJScrollPane);

        OverrepresentationAnalysisComponentListener overrepresentationAnalysisComponentListener = new OverrepresentationAnalysisComponentListener();
        addComponentListener(overrepresentationAnalysisComponentListener);

        // this combination and order of method calls, with setBounds being run
        // in the event dispatch thread, appears to give the insets of the
        // OverrepresentationAnalysisJDialog the correct dimensions so that the setBounds
        // method sets the bounds of the OverrepresentationAnalysisJDialog correctly.
        // without this specific order and combination, the
        // OverrepresentationAnalysisJDialog is initially too short.
        // why this code below appears to work is unknown.
        overrepresentationAnalysisComponentListener.componentResized(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = g.getScreenDevices();
                OverrepresentationAnalysisJDialog.classInstance.setBounds(
                        DialogSizesAndPositions.overrepresentationAnalysisXCoordinate,
                        DialogSizesAndPositions.overrepresentationAnalysisYCoordinate,
                        500,
                        10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 1 + 10
                        + getInsets().top + getInsets().bottom);
            }
        });
        // the setVisible method is called to make the OverrepresentationAnalysisJDialog
        // appear
        setVisible(true);

    }
}
