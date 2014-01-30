package justclust.toolbar.manageplugins;

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.UIManager;
import justclust.DialogSizesAndPositions;
import justclust.toolbar.filterclusters.FilterClustersJDialog;

public class ManagePluginsComponentListener implements ComponentListener {

    public void componentHidden(ComponentEvent componentEvent) {
    }

    public void componentMoved(ComponentEvent componentEvent) {
        
        DialogSizesAndPositions.managePluginsXCoordinate = ManagePluginsJDialog.classInstance.getX();
        DialogSizesAndPositions.managePluginsYCoordinate = ManagePluginsJDialog.classInstance.getY();
        
    }

    public void componentResized(ComponentEvent componentEvent) {
        
        ManagePluginsJDialog.classInstance.managePluginsHelpButton
                .setBounds(
                ManagePluginsJDialog.classInstance.managePluginsJPanel.getWidth() - (10 + 16),
                10,
                16,
                16);

        ManagePluginsJDialog.classInstance.managePluginsJTabbedPane
                .setBounds(
                10,
                10 + 16 + 10,
                ManagePluginsJDialog.classInstance.managePluginsJPanel.getWidth() - (10 + 10),
                ManagePluginsJDialog.classInstance.managePluginsJPanel.getHeight() - (10 + 16 + 10 + 10));

        ManagePluginsJDialog.classInstance.parsingPluginsPathJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10)),
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.parsingPluginsPathJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.parsingPluginsPathBrowseButton
                .setBounds(
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                25,
                25);

        ManagePluginsJDialog.classInstance.loadParsingPluginsJButton
                .setBounds(
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10)),
                200,
                25);

        ManagePluginsJDialog.classInstance.loadedParsingPluginsJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.loadedParsingPluginsJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.parsingPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.clusteringPluginsPathJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10)),
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.clusteringPluginsPathJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.clusteringPluginsPathBrowseButton
                .setBounds(
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                25,
                25);

        ManagePluginsJDialog.classInstance.loadClusteringPluginsJButton
                .setBounds(
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10)),
                200,
                25);

        ManagePluginsJDialog.classInstance.loadedClusteringPluginsJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.loadedClusteringPluginsJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.clusteringPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.visualisationPluginsPathJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10)),
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.visualisationPluginsPathJTextField
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 25 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.visualisationPluginsPathBrowseButton
                .setBounds(
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 25),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10)),
                25,
                25);

        ManagePluginsJDialog.classInstance.loadVisualisationPluginsJButton
                .setBounds(
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 200),
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10)),
                200,
                25);

        ManagePluginsJDialog.classInstance.loadedVisualisationPluginsJLabel
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

        ManagePluginsJDialog.classInstance.loadedVisualisationPluginsJComboBox
                .setBounds(
                10 + 1 + 10,
                (int) Math.round((double) ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getHeight() * 1 / 2
                - 103.5 + (10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10)),
                ManagePluginsJDialog.classInstance.visualisationPluginsJPanel.getWidth() - (10 + 1 + 10 + 10 + 1 + 10),
                25);

    }

    public void componentShown(ComponentEvent componentEvent) {
    }
}
