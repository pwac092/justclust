package justclust.menubar.exportgraph;

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
import justclust.menubar.filefilters.JPGFileFilter;
import justclust.menubar.filefilters.PDFFileFilter;
import justclust.menubar.filefilters.PNGFileFilter;
import justclust.menubar.filefilters.SVGFileFilter;
import justclust.plugins.configurationcontrols.FileSystemPathControl;

public class ExportGraphMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent me) {

        if (me.getComponent() == ExportGraphJDialog.classInstance.exportGraphHelpButton) {
            new ExportGraphHelpJDialog();
        }

        if (me.getComponent() == ExportGraphJDialog.classInstance.browseButton) {
            
            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a file, adds a file extension to the file if
            // there isn't one, assigns the file to the file field of the
            // ExportGraphJDialog class, and populates the text field contained
            // in the ExportGraphJDialog.fileNameJTextField field with the path
            // of the file
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            PNGFileFilter pngFileFilter = new PNGFileFilter();
            jFileChooser.addChoosableFileFilter(pngFileFilter);
            JPGFileFilter jpgFileFilter = new JPGFileFilter();
            jFileChooser.addChoosableFileFilter(jpgFileFilter);
            PDFFileFilter pdfFileFilter = new PDFFileFilter();
            jFileChooser.addChoosableFileFilter(pdfFileFilter);
            SVGFileFilter svgFileFilter = new SVGFileFilter();
            jFileChooser.addChoosableFileFilter(svgFileFilter);
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showSaveDialog(ExportGraphJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
//                ExportGraphJDialog.classInstance.file = jFileChooser.getSelectedFile();
//                if (jFileChooser.getFileFilter() == pngFileFilter
//                        && !ExportGraphJDialog.classInstance.file.getAbsolutePath().toLowerCase().endsWith(".png")) {
//                    ExportGraphJDialog.classInstance.file = new File(ExportGraphJDialog.classInstance.file.getAbsolutePath() + ".png");
//                }
//                if (jFileChooser.getFileFilter() == jpgFileFilter
//                        && !ExportGraphJDialog.classInstance.file.getAbsolutePath().toLowerCase().endsWith(".jpg")) {
//                    ExportGraphJDialog.classInstance.file = new File(ExportGraphJDialog.classInstance.file.getAbsolutePath() + ".jpg");
//                }
//                if (jFileChooser.getFileFilter() == pdfFileFilter
//                        && !ExportGraphJDialog.classInstance.file.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
//                    ExportGraphJDialog.classInstance.file = new File(ExportGraphJDialog.classInstance.file.getAbsolutePath() + ".pdf");
//                }
//                if (jFileChooser.getFileFilter() == svgFileFilter
//                        && !ExportGraphJDialog.classInstance.file.getAbsolutePath().toLowerCase().endsWith(".svg")) {
//                    ExportGraphJDialog.classInstance.file = new File(ExportGraphJDialog.classInstance.file.getAbsolutePath() + ".svg");
//                }
                ExportGraphJDialog.classInstance.fileNameJTextField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
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
