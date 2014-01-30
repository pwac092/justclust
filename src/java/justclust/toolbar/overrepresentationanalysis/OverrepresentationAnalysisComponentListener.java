package justclust.toolbar.overrepresentationanalysis;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import justclust.DialogSizesAndPositions;

public class OverrepresentationAnalysisComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {

        DialogSizesAndPositions.overrepresentationAnalysisXCoordinate = OverrepresentationAnalysisJDialog.classInstance.getX();
        DialogSizesAndPositions.overrepresentationAnalysisYCoordinate = OverrepresentationAnalysisJDialog.classInstance.getY();

    }

    public void componentResized(ComponentEvent componentEvent) {

        OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisHelpButton
                .setBounds(
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 16),
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10)),
                16,
                16);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyBrowseButton
                .setBounds(
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10)),
                25,
                25);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyAnnotationsJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyAnnotationsJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.geneOntologyAnnotationsBrowseButton
                .setBounds(
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                25,
                25);

        OverrepresentationAnalysisJDialog.classInstance.evidenceCodesJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.evidenceCodesJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                95);

        OverrepresentationAnalysisJDialog.classInstance.significanceValueJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.significanceValueJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.multipleHypothesisTestingCorrectionJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.multipleHypothesisTestingCorrectionJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.clusterToAnalyseJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.clusterToAnalyseJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                95);

        OverrepresentationAnalysisJDialog.classInstance.analyseOverrepresentationJButton
                .setBounds(
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10)),
                200,
                25);

        OverrepresentationAnalysisJDialog.classInstance.functionsForClusterJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        OverrepresentationAnalysisJDialog.classInstance.functionsForClusterJScrollPane
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getHeight() * 1 / 2
                - 396.5 + (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10)),
                OverrepresentationAnalysisJDialog.classInstance.overrepresentationAnalysisJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                95);

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
