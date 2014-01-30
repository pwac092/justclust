package justclust.menubar.applylayout;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
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
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

public class ApplyLayoutMouseListener implements MouseListener {

    public static ApplyLayoutMouseListener classInstance;

    ApplyLayoutMouseListener() {
        classInstance = this;
    }

    public void mouseClicked(MouseEvent me) {

        if (me.getComponent() == ApplyLayoutJDialog.classInstance.applyLayoutHelpButton) {
            new ApplyLayoutHelpJDialog();
        }

        // iterate through the PluginConfigurationControls.
        // if one is a FileSystemPathControl, the third of its corresponding
        // JComponents (with index 2) will be a BrowseButton.
        // the outer if statement checks whether this BrowseButton has been
        // clicked, and the body of the if statement presents a JFileChooser to
        // the user if it has.
        if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls != null) {
            for (int i = 0; i < ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size(); i++) {
                if (me.getComponent() == ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(2)) {

                    // this code presents a JFileChooser to the user and then,
                    // if the user has chosen a path, populates the
                    // JTextField which corresponds to the BrowseButton which
                    // was clicked, with the path

                    JFileChooser jFileChooser = new JFileChooser();

                    // if the directoriesOnly field of the corresponding
                    // FileSystemPathControl is true, the jFileChooser is configured
                    // to accept directories instead of files
                    if (((FileSystemPathControl) ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i)).directoriesOnly) {
                        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    }

                    int jFileChooserState = jFileChooser
                            .showOpenDialog(ApplyLayoutJDialog.classInstance);
                    if (jFileChooserState == JFileChooser.APPROVE_OPTION) {
                        ((JTextField) ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1))
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
