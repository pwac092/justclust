package justclust.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import justclust.menubar.loadsession.LoadSessionJDialog;

/**
 * This class has a method which responds to user input which is made using a
 * JMenu.
 */
public class LoadJMenuActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a JMenu.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Session...")) {
            new LoadSessionJDialog();
        }

    }
}
