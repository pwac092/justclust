package justclust.menubar.loadsession;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.filefilters.CSVFileFilter;
import justclust.menubar.filefilters.SERFileFilter;

public class LoadSessionMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent me) {

        if (me.getComponent() == LoadSessionJDialog.classInstance.loadSessionHelpButton) {
            new LoadSessionHelpJDialog();
        }

        if (me.getComponent() == LoadSessionJDialog.classInstance.browseButton) {

            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a file, assigns the file to the file field of
            // this class and populates the text field contained in the
            // LoadSessionJDialog.fileNameJTextField field with the path of the
            // file
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(new SERFileFilter());
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showOpenDialog(LoadSessionJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
//                LoadSessionJDialog.classInstance.file = jFileChooser.getSelectedFile();
                LoadSessionJDialog.classInstance.fileNameJTextField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }

        }

    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }
}