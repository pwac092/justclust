package justclust.toolbar.manageplugins;

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

public class ManagePluginsMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent me) {

        if (me.getComponent() == ManagePluginsJDialog.classInstance.managePluginsHelpButton) {
            new ManagePluginsHelpJDialog();
        }

        if (me.getComponent() == ManagePluginsJDialog.classInstance.parsingPluginsPathBrowseButton) {

            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a directory, populates the
            // parsingPluginsPathJTextField with the path of the directory
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setSelectedFile(new File(ManagePluginsJDialog.classInstance.parsingPluginsPathJTextField.getText()));
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showOpenDialog(ManagePluginsJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
                ManagePluginsJDialog.classInstance.parsingPluginsPathJTextField.setText(
                        jFileChooser.getSelectedFile().getAbsolutePath());
            }

        }

        if (me.getComponent() == ManagePluginsJDialog.classInstance.clusteringPluginsPathBrowseButton) {

            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a directory, populates the
            // clusteringPluginsPathJTextField with the path of the directory
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showOpenDialog(ManagePluginsJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
                ManagePluginsJDialog.classInstance.clusteringPluginsPathJTextField.setText(
                        jFileChooser.getSelectedFile().getAbsolutePath());
            }

        }

        if (me.getComponent() == ManagePluginsJDialog.classInstance.visualisationPluginsPathBrowseButton) {

            // this code presents a JFileChooser to the user and then, if the
            // user has chosen a directory, populates the
            // visualisationPluginsPathJTextField with the path of the directory
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // lastAccessedFile contains the file on the user's hard drive which was
            // last accessed.
            // whenever the user browses their hard drive from within JustClust, this
            // lastAccessedFile will determine the directory which the file chooser
            // opens initially.
            // the user will be taken straight to the directory they last browsed to.
            jFileChooser.setSelectedFile(Data.lastAccessedFile);
            int jFileChooserState = jFileChooser.showOpenDialog(ManagePluginsJDialog.classInstance);
            if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                Data.lastAccessedFile = jFileChooser.getSelectedFile();
                ManagePluginsJDialog.classInstance.visualisationPluginsPathJTextField.setText(
                        jFileChooser.getSelectedFile().getAbsolutePath());
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
