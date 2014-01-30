package justclust.menubar.newnetworkfromfile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.customcomponents.BrowseButton;
import justclust.menubar.applylayout.ApplyLayoutMouseListener;
import static justclust.menubar.applylayout.ApplyLayoutMouseListener.classInstance;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;

public class NewNetworkFromFileMouseListener implements MouseListener {

    public static NewNetworkFromFileMouseListener classInstance;

    NewNetworkFromFileMouseListener() {
        classInstance = this;
    }

    public void mouseClicked(MouseEvent me) {

        if (me.getComponent() == NewNetworkFromFileJDialog.classInstance.newNetworkFromFileHelpButton) {
            new NewNetworkFromFileHelpJDialog();
        }

        if (me.getComponent() == NewNetworkFromFileJDialog.classInstance.inputFileBrowseButton) {

            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a file, assigns the file to the file field of
            // this class and populates the text field contained in the
            // NewNetworkFromFileJDialog.inputFileJTextField field with the path of the
            // file

            JFileChooser jFileChooser = new JFileChooser();
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showOpenDialog(NewNetworkFromFileJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
//                NewNetworkFromFileJDialog.classInstance.file = jFileChooser.getSelectedFile();
                NewNetworkFromFileJDialog.classInstance.inputFileJTextField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }

        }

        // iterate through the PluginConfigurationControls.
        // if one is a FileSystemPathControl, the third of its corresponding
        // JComponents (with index 2) will be a BrowseButton.
        // the outer if statement checks whether this BrowseButton has been
        // clicked, and the body of the if statement presents a JFileChooser to
        // the user if it has.
        if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls != null) {
            for (int i = 0; i < NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size(); i++) {
                if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl
                        && me.getComponent() == NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(2)) {

                    // this code presents a JFileChooser to the user and then,
                    // if the user has chosen a path, populates the
                    // JTextField which corresponds to the BrowseButton which
                    // was clicked, with the path

                    JFileChooser jFileChooser = new JFileChooser();

                    // if the directoriesOnly field of the corresponding
                    // FileSystemPathControl is true, the jFileChooser is configured
                    // to accept directories instead of files
                    if (((FileSystemPathControl) NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i)).directoriesOnly) {
                        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    }

                    // lastAccessedFile contains the file on the user's hard drive which was
                    // last accessed.
                    // whenever the user browses their hard drive from within JustClust, this
                    // lastAccessedFile will determine the directory which the file chooser
                    // opens initially.
                    // the user will be taken straight to the directory they last browsed to.
                    jFileChooser.setSelectedFile(Data.lastAccessedFile);
                    int jFileChooserState = jFileChooser.showOpenDialog(NewNetworkFromFileJDialog.classInstance);
                    if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                        Data.lastAccessedFile = jFileChooser.getSelectedFile();
                        ((JTextField) NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1))
                                .setText(jFileChooser.getSelectedFile().getAbsolutePath());
                    }

                }
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
