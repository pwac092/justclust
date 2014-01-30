package justclust.menubar.exportnetwork;

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
import javax.swing.JTextField;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Edge;
import justclust.menubar.filefilters.CSVFileFilter;
import justclust.plugins.configurationcontrols.FileSystemPathControl;

public class ExportNetworkActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a
     * ExportNetworkJDialog.
     */
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Export Network")) {

            ExportNetworkJDialog.classInstance.exportNetworkJButton.setEnabled(false);

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            if (data.networkNodes == null) {

                ExportNetworkJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                ExportNetworkJDialog.classInstance.exportNetworkJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance,
                        "Exporting could not be completed due to network not existing");

                return;

            }

            // this code starts a new thread to export the network.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // network is being exported in the new thread.
            ExportNetworkThread exportNetworkThread = new ExportNetworkThread();
            exportNetworkThread.start();

        }

    }

    /**
     * This inner class has a method which exports the current network with a
     * new thread.
     */
    class ExportNetworkThread extends Thread {

        /**
         * This method exports the current network with a new thread.
         */
        public void run() {

            // the createNetworkJProgressBar is added to the
            // newNetworkDialogJPanel
            ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.add(ExportNetworkJDialog.classInstance.exportNetworkJProgressBar);

            // set the y coordinate of the ExportNetworkJDialog so that it remains
            // centered around the point it currently is around when its height
            // is increased.
            // the difference in height between the new height and old height
            // is halved and taken away from the current y coordinate of the
            // ExportNetworkJDialog.
            // also, increase the height of the ExportNetworkJDialog.
            ExportNetworkJDialog.classInstance.setBounds(
                    ExportNetworkJDialog.classInstance.getLocation().x,
                    ExportNetworkJDialog.classInstance.getLocation().y
                    - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    - ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.getHeight()) / 2),
                    ExportNetworkJDialog.classInstance.getWidth(),
                    ExportNetworkJDialog.classInstance.getInsets().top
                    + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    + ExportNetworkJDialog.classInstance.getInsets().bottom);

            ExportNetworkJDialog.classInstance.exportNetworkJProgressBar.setIndeterminate(true);

            ExportNetworkJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Exporting network...");

            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);

            try {

                FileWriter fileWriter = new FileWriter(new File(ExportNetworkJDialog.classInstance.fileNameJTextField.getText()));
//                ExportNetworkJDialog.classInstance.file = null;
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                // This code populates the contents of a file with the
                // Data.networkEdges data structure.
                bufferedWriter.write(", Node 1, Node 2, Weight");
                for (int i = 0; i < data.networkEdges.size(); i++) {
                    bufferedWriter.newLine();
                    bufferedWriter.write("Edge "
                            + (i + 1)
                            + ", "
                            + data.networkEdges.get(i).node1.label
                            + ", "
                            + data.networkEdges.get(i).node2.label
                            + ", "
                            + new DecimalFormat("#.#####").format(data.networkEdges.get(i).weight));
                }

                bufferedWriter.close();
                fileWriter.close();

            } catch (IOException | NullPointerException exception) {

                ExportNetworkJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the createNetworkJProgressBar is removed from the
                // newNetworkDialogJPanel
                ExportNetworkJDialog.classInstance.exportNetworkDialogJPanel.remove(ExportNetworkJDialog.classInstance.exportNetworkJProgressBar);

                // set the y coordinate of the ExportNetworkJDialog so that it remains
                // centered around the point it currently is around when its height
                // is decreased.
                // the difference in height between the new height and old height
                // is halved and added to the current y coordinate of the
                // ExportNetworkJDialog.
                // also, decrease the height of the ExportNetworkJDialog.
                ExportNetworkJDialog.classInstance.setBounds(
                        ExportNetworkJDialog.classInstance.getLocation().x,
                        ExportNetworkJDialog.classInstance.getLocation().y
                        + Math.round((10 + 25) / 2),
                        ExportNetworkJDialog.classInstance.getWidth(),
                        ExportNetworkJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + ExportNetworkJDialog.classInstance.getInsets().bottom);

                ExportNetworkJDialog.classInstance.exportNetworkJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Exporting could not be completed due to error");

                return;

            }

            ExportNetworkJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            ExportNetworkJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Network exported");

        }
    }
}
