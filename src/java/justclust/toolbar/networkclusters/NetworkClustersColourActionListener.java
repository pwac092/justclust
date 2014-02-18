package justclust.toolbar.networkclusters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NetworkClustersColourActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("OK")) {
            NetworkClustersTableCellEditor.classInstance.okButtonClicked = true;
            NetworkClustersColourJDialog.classInstance.dispose();
        }

        if (actionEvent.getActionCommand().equals("Cancel")) {
            NetworkClustersTableCellEditor.classInstance.okButtonClicked = false;
            NetworkClustersColourJDialog.classInstance.dispose();
        }

        if (actionEvent.getActionCommand().equals("Random")) {
            Random random = new Random();
            NetworkClustersColourJDialog.classInstance.jColorChooser.setColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

    }
}
