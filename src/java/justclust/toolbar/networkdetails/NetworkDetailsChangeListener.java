/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.networkdetails;

import java.awt.FontMetrics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import justclust.DialogSizesAndPositions;

/**
 *
 * @author wuaz008
 */
public class NetworkDetailsChangeListener implements ChangeListener {

    public void stateChanged(ChangeEvent changeEvent) {
        
        // this code finds the caret position which is at the top of the
        // currently visible part of the networkDetailsDialogJTextArea, every
        // time the networkDetailsDialogJScrollPane is adjusted.
        // when the NetworkDetailsJDialog is closed and opened, this value will
        // be used to scroll the networkDetailsDialogJScrollPane so that it
        // shows the same part of the networkDetailsDialogJTextArea as before.
        int caretPosition = 0;
        FontMetrics fontMetrics = NetworkDetailsJDialog.classInstance.networkDetailsDialogJTextArea.getGraphics().getFontMetrics();
        int pixelsFromTop = fontMetrics.getHeight() / 2;
        while (NetworkDetailsJDialog.classInstance.networkDetailsDialogJScrollPane.getVerticalScrollBar().getValue() > pixelsFromTop) {
            if (NetworkDetailsJDialog.classInstance.networkDetailsDialogJTextArea.getText().charAt(caretPosition) == '\n') {
                pixelsFromTop += fontMetrics.getHeight();
            }
            caretPosition++;
        };
        DialogSizesAndPositions.networkDetailsCaretPositionToScrollTo = caretPosition;
        
    }
    
}
