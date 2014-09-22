/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.overrepresentationanalysis;

import GOtree.AnnotationFile;
import GOtree.Assignment;
import GOtree.GOTerm;
import GOtree.GeneOntology;
import GOtree.GeneOntologyException;
import GOtree.GeneOntologyParser;
import GOtree.Propagation;
import GOtree.PropagationStrategies.PropagationMaxWithinTrees;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.JustclustJFrame;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkclusters.NetworkClustersTableModel;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networkedges.NetworkEdgesTableModel;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.networknodes.NetworkNodesTableModel;

/**
 *
 * @author wuaz008
 */
public class OverrepresentationAnalysisActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        OverrepresentationAnalysisJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        ArrayList<String> functions;

        try {

            AnnotationFile.useUniProtIds(false);

            GeneOntologyParser parser = new GeneOntologyParser();
            GOTerm.setRelations(new String[]{"is_a", "part_of"});
            GeneOntology ontology;
            ontology = parser.readFromOBOFile(
                    OverrepresentationAnalysisJDialog.classInstance.geneOntologyJTextField.getText(),
                    false);

            // The variable evidenceCodes can have three values (you can switch the
            // values by changing the "AnnotationFile.USE_ALL" to
            // "AnnotationFile.USE_ALL_BUT_IEA" or "AnnotationFile.USE_EXP". You can ask
            // the user whether to use all the evidence codes (and in that case you would
            // use "AnnotationFile.USE_ALL"), to use all but IEA codes (and you'd use
            // "AnnotationFile.USE_ALL_BUT_IEA") or just experimental codes
            // (AnnotationFile.USE_EXP). By default I would leave the experimental option
            // which produces more reliable annotations.
            AnnotationFile annoFile = new AnnotationFile();
            ArrayList<String> evidenceCodes = new ArrayList<String>();
            for (int i = 0; i < OverrepresentationAnalysisJDialog.classInstance.evidenceCodesJTable.getRowCount(); i++) {
                if (OverrepresentationAnalysisJDialog.classInstance.evidenceCodesJTable.getValueAt(i, 1) == true) {
                    evidenceCodes.add((String) OverrepresentationAnalysisJDialog.classInstance.evidenceCodesJTable.getValueAt(i, 0));
                }
            }
            Assignment annotations = annoFile.readAnnotationFile(
                    OverrepresentationAnalysisJDialog.classInstance.geneOntologyAnnotationsJTextField.getText(),
                    evidenceCodes.toArray(new String[0]));

            Propagation propagator = new Propagation();
            PropagationMaxWithinTrees strategy = new PropagationMaxWithinTrees();
//          Assignment propagated_annotations = propagator.propagateAssignment(annotations, GO, strategy);
            Assignment propagated_annotations = propagator.propagateAssignment(annotations, ontology, strategy);

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            functions = new ArrayList<String>();
            ArrayList<Double> pValues = new ArrayList<Double>();
            Cluster cluster = data.networkClusters.get(OverrepresentationAnalysisJDialog.classInstance.clusterToAnalyseJTable.getSelectedRow());
            for (String goTerm : propagated_annotations.getColumnIdentifiers()) {

                int networkNodesWithFunction = 0;
                int clusterNodesWithFunction = 0;
                for (Node node : data.networkNodes) {
                    if (propagated_annotations.getScoreForProteinAndGOterm(node.label, goTerm) >= 1) {
                        networkNodesWithFunction++;
                        if (cluster.nodes.contains(node)) {
                            clusterNodesWithFunction++;
                        }
                    }
                }

                double pValue = 0;
                for (int i = clusterNodesWithFunction; i <= cluster.nodes.size(); i++) {
                    pValue += getCombination(BigInteger.valueOf(data.networkNodes.size() - networkNodesWithFunction), BigInteger.valueOf(cluster.nodes.size() - i)).doubleValue()
                            * getCombination(BigInteger.valueOf(networkNodesWithFunction), BigInteger.valueOf(i)).doubleValue()
                            / getCombination(BigInteger.valueOf(data.networkNodes.size()), BigInteger.valueOf(cluster.nodes.size())).doubleValue();
                }

                functions.add(ontology.getTermById(goTerm).getFunction());
                pValues.add(pValue);

            }

            // this code sorts the functions in order of smallest p-value to
            // largest.
            // both of the arrays, functions and pValues, are sorted together.
            for (int i = 1; i < functions.size(); i++) {
                String function = functions.get(i);
                double pValue = pValues.get(i);
                int j;
                for (j = i - 1; j >= 0 && pValue < pValues.get(j); j--) {
                    functions.set(j + 1, functions.get(j));
                    pValues.set(j + 1, pValues.get(j));
                }
                functions.set(j + 1, function);
                pValues.set(j + 1, pValue);
            }

            for (int i = 0; i < functions.size(); i++) {
                if (OverrepresentationAnalysisJDialog.classInstance.multipleHypothesisTestingCorrectionJComboBox.getSelectedItem().equals("No correction")) {
                    if (pValues.get(i) >= Double.parseDouble(OverrepresentationAnalysisJDialog.classInstance.significanceValueJTextField.getText())) {
                        functions.remove(i);
                        pValues.remove(i);
                        i--;
                    }
                } else if (OverrepresentationAnalysisJDialog.classInstance.multipleHypothesisTestingCorrectionJComboBox.getSelectedItem().equals("Bonferroni correction")) {
                    if (pValues.get(i)
                            > Double.parseDouble(OverrepresentationAnalysisJDialog.classInstance.significanceValueJTextField.getText())
                            / propagated_annotations.getColumnIdentifiers().size()) {
                        functions.remove(i);
                        pValues.remove(i);
                        i--;
                    }
                } else if (OverrepresentationAnalysisJDialog.classInstance.multipleHypothesisTestingCorrectionJComboBox.getSelectedItem().equals("Hochberg's step-up procedure")) {
                    if (pValues.get(i)
                            > Double.parseDouble(OverrepresentationAnalysisJDialog.classInstance.significanceValueJTextField.getText())
                            / (propagated_annotations.getColumnIdentifiers().size() + 1 - (i + 1))) {
                        functions.remove(i);
                        pValues.remove(i);
                        i--;
                    }
                }
            }

            Object[] columnNames = new String[]{"Function", "P-value"};
            Object[][] tableData = new String[functions.size()][columnNames.length];
            for (int i = 0; i < functions.size(); i++) {
                tableData[i][0] = functions.get(i);
                tableData[i][1] = String.valueOf(pValues.get(i));
            }
            OverrepresentationAnalysisJDialog.classInstance.functionsForClusterJTable.setModel(new DefaultTableModel(tableData, columnNames));

        } catch (Exception ex) {

            OverrepresentationAnalysisJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            JOptionPane.showMessageDialog(OverrepresentationAnalysisJDialog.classInstance, "Analysis could not be completed due to error");

            return;

        }

        OverrepresentationAnalysisJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        if (functions.size() == 1) {
            JOptionPane.showMessageDialog(OverrepresentationAnalysisJDialog.classInstance, "1 function found");
        } else {
            JOptionPane.showMessageDialog(OverrepresentationAnalysisJDialog.classInstance, functions.size() + " functions found");
        }

    }

    static BigInteger getCombination(BigInteger n, BigInteger k) {
        return getFactorial(n).divide(getFactorial(k).multiply(getFactorial(n.subtract(k))));
    }

    static BigInteger getFactorial(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(1)) <= 0) {
            return BigInteger.valueOf(1);
        } else {
            return getFactorial(n.subtract(BigInteger.valueOf(1))).multiply(n);
        }
    }
}
