/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networknodes;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JComboBox;
import justclust.datastructures.Data;
import justclust.datastructures.Node;
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class NetworkNodesActionListener implements ActionListener {

    public void actionPerformed(ActionEvent ae) {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            // get the current Data instance for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            Data data = Data.data.get(currentCustomGraphEditorIndex);
            Node node = data.networkNodes.get(NetworkNodesJDialog.classInstance.networkNodesDialogJTable.getEditingRow() - 1);
            try {
                switch (((JComboBox) ae.getSource()).getSelectedIndex()) {
                    case 1:
                        desktop.browse(new URI("http://www.uniprot.org/uniprot/?query=" + node.label.replace(' ', '+') + "&sort=score"));
                        break;
                    case 2:
                        desktop.browse(new URI("http://www.ncbi.nlm.nih.gov/gquery/?term=" + node.label.replace(' ', '+')));
                        break;
                }
            } catch (IOException ex) {
            } catch (URISyntaxException ex) {
            }
        }

    }
}
