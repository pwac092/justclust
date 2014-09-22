package justclust.menubar.exportclustering;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.menubar.filefilters.CSVFileFilter;

public class ExportClusteringActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a
     * ExportClusteringJDialog.
     */
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Export Clustering")) {

            ExportClusteringJDialog.classInstance.exportClusteringJButton.setEnabled(false);

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            if (data.networkClusters == null) {

                ExportClusteringJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                ExportClusteringJDialog.classInstance.exportClusteringJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Exporting could not be completed due to clustering not existing");

                return;

            }

            // this code starts a new thread to export the clustering.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // clustering is being exported in the new thread.
            ExportClusteringThread exportClusteringThread = new ExportClusteringThread();
            exportClusteringThread.start();

        }

    }

    /**
     * This inner class has a method which exports the current clustering with a
     * new thread.
     */
    class ExportClusteringThread extends Thread {

        /**
         * This method exports the current clustering with a new thread.
         */
        public void run() {

            // the exportClusteringJProgressBar is added to the
            // exportClusteringDialogJPanel
            ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.add(ExportClusteringJDialog.classInstance.exportClusteringJProgressBar);

            // set the y coordinate of the ExportClusteringJDialog so that it remains
            // centered around the point it currently is around when its height
            // is increased.
            // the difference in height between the new height and old height
            // is halved and taken away from the current y coordinate of the
            // ExportClusteringJDialog.
            // also, increase the height of the ExportClusteringJDialog.
            ExportClusteringJDialog.classInstance.setBounds(
                    ExportClusteringJDialog.classInstance.getLocation().x,
                    ExportClusteringJDialog.classInstance.getLocation().y
                    - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    - ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.getHeight()) / 2),
                    ExportClusteringJDialog.classInstance.getWidth(),
                    ExportClusteringJDialog.classInstance.getInsets().top
                    + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    + ExportClusteringJDialog.classInstance.getInsets().bottom);

            ExportClusteringJDialog.classInstance.exportClusteringJProgressBar.setIndeterminate(true);

            ExportClusteringJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Exporting clustering...");

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            try {

                FileWriter fileWriter = new FileWriter(new File(ExportClusteringJDialog.classInstance.fileNameJTextField.getText()));
//                ExportClusteringJDialog.classInstance.file = null;
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                // This code populates the contents of a file with the
                // Data.networkClusters data structure.
                int maxClusterSize = 0;
                for (Cluster cluster : data.networkClusters) {
                    if (cluster.nodes.size() > maxClusterSize) {
                        maxClusterSize = cluster.nodes.size();
                    }
                }
                for (int i = 0; i < maxClusterSize; i++) {
                    bufferedWriter.write(", Node " + (i + 1));
                }
                for (int i = 0; i < data.networkClusters.size(); i++) {
                    bufferedWriter.newLine();
                    bufferedWriter.write("Cluster " + (i + 1));
                    for (int j = 0; j < data.networkClusters.get(i).nodes.size(); j++) {
                        bufferedWriter.write(", "
                                + data.networkClusters.get(i).nodes.get(j).label);
                    }
                }

                bufferedWriter.close();
                fileWriter.close();

            } catch (IOException | NullPointerException exception) {

                ExportClusteringJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the exportClusteringJProgressBar is removed from the
                // exportClusteringDialogJPanel
                ExportClusteringJDialog.classInstance.exportClusteringDialogJPanel.remove(ExportClusteringJDialog.classInstance.exportClusteringJProgressBar);

                // set the y coordinate of the ExportClusteringJDialog so that it remains
                // centered around the point it currently is around when its height
                // is decreased.
                // the difference in height between the new height and old height
                // is halved and added to the current y coordinate of the
                // ExportClusteringJDialog.
                // also, decrease the height of the ExportClusteringJDialog.
                ExportClusteringJDialog.classInstance.setBounds(
                        ExportClusteringJDialog.classInstance.getLocation().x,
                        ExportClusteringJDialog.classInstance.getLocation().y
                        + Math.round((10 + 25) / 2),
                        ExportClusteringJDialog.classInstance.getWidth(),
                        ExportClusteringJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + ExportClusteringJDialog.classInstance.getInsets().bottom);

                ExportClusteringJDialog.classInstance.exportClusteringJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Exporting could not be completed due to error");

                return;

            }

            ExportClusteringJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            ExportClusteringJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Clustering exported");

        }
    }
}
