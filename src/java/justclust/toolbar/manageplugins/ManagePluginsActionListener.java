/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.manageplugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.newnetworkfromfile.NewNetworkFromFileJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

/**
 *
 * @author wuaz008
 */
public class ManagePluginsActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Load Parsing Plug-ins")) {

            // the parsing plugins path entered by the user is stored and
            // the configuration file is updated (the
            // saveParsingPluginsPath method does this) so that the
            // information is saved when the user restarts JustClust
            Data.saveParsingPluginsPath(ManagePluginsJDialog.classInstance.parsingPluginsPathJTextField.getText());

            // fileParserDisplayNames is declared outside the following try
            // block so that the loadedParsingPluginsJComboBox can use it outside the try
            // block later on
            ArrayList<String> fileParserDisplayNames = new ArrayList<String>();

            try {

                // this code populates the combo box contained in the
                // ManagePluginsJDialog.loadedParsingPluginsJComboBox field with the display
                // names of file parser plug-ins.
                // each file in the file path of the file parser plug-ins is
                // checked to see if it is a jar file.
                // each jar file found is then search for class files which
                // implement the FileParserPluginInterface interface.
                // these class files are then loaded and there getName() methods
                // are called.
                // the display names are returned by the getName() methods.

                File file = new File(Data.parsingPluginsPath);
                String[] list = file.list();
                Data.fileParserJarNames = new ArrayList<String>();
                Data.fileParserClassNames = new ArrayList<String>();
                for (String filename : list) {

                    if (filename.endsWith(".jar")) {

                        File jarFile = new File(Data.parsingPluginsPath + '/' + filename);

                        Enumeration<JarEntry> enumeration = new JarFile(
                                jarFile).entries();

                        URLClassLoader urlClassLoader = URLClassLoader
                                .newInstance(new URL[]{jarFile.toURI()
                            .toURL()});

                        while (enumeration.hasMoreElements()) {
                            JarEntry jarEntry = enumeration.nextElement();
                            if (jarEntry.getName().endsWith(".class")) {

                                Class<?> loadedClass = urlClassLoader.loadClass(
                                        jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                if (FileParserPluginInterface.class.isAssignableFrom(loadedClass)) {

                                    Data.fileParserJarNames.add(filename);
                                    Data.fileParserClassNames.add(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    Object classInstance = loadedClass.newInstance();
                                    Method method = loadedClass.getMethod("getFileType", new Class[]{});
                                    fileParserDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                }

                            }
                        }

                    }

                }

            } catch (Exception exception) {

                // fileParserDisplayNames is emptied so that the list of
                // plug-ins in the loadedParsingPluginsJComboBox is empty when an error
                // occurs
                fileParserDisplayNames = new ArrayList<String>();

                JOptionPane.showMessageDialog(ManagePluginsJDialog.classInstance, "Plug-ins could not be loaded due to error");

            }

            ManagePluginsJDialog.classInstance.loadedParsingPluginsJComboBox.setModel(new DefaultComboBoxModel(
                    fileParserDisplayNames.toArray(new String[fileParserDisplayNames.size()])));

        }

        if (actionEvent.getActionCommand().equals("Load Clustering Plug-ins")) {

            // the clustering plugins path entered by the user is stored and
            // the configuration file is updated (the
            // saveClusteringPluginsPath method does this) so that the
            // information is saved when the user restarts JustClust
            Data.saveClusteringPluginsPath(ManagePluginsJDialog.classInstance.clusteringPluginsPathJTextField.getText());

            // clusteringAlgorithmDisplayNames is declared outside the following try
            // block so that the loadedClusteringPluginsJComboBox can use it outside the try
            // block later on
            ArrayList<String> clusteringAlgorithmDisplayNames = new ArrayList<String>();

            try {

                // this code populates the combo box contained in the
                // ManagePluginsJDialog.loadedClusteringPluginsJComboBox field with the display
                // names of clustering algorithm plug-ins.
                // each file in the file path of the clustering algorithm plug-ins is
                // checked to see if it is a jar file.
                // each jar file found is then search for class files which
                // implement the ClusteringAlgorithmPluginInterface interface.
                // these class files are then loaded and there getName() methods
                // are called.
                // the display names are returned by the getName() methods.

                File file = new File(Data.clusteringPluginsPath);
                String[] list = file.list();
                Data.clusteringAlgorithmJarNames = new ArrayList<String>();
                Data.clusteringAlgorithmClassNames = new ArrayList<String>();
                for (String filename : list) {

                    if (filename.endsWith(".jar")) {

                        File jarFile = new File(Data.clusteringPluginsPath + '/' + filename);

                        Enumeration<JarEntry> enumeration = new JarFile(
                                jarFile).entries();

                        URLClassLoader urlClassLoader = URLClassLoader
                                .newInstance(new URL[]{jarFile.toURI()
                            .toURL()});

                        while (enumeration.hasMoreElements()) {
                            JarEntry jarEntry = enumeration.nextElement();
                            if (jarEntry.getName().endsWith(".class")) {

                                Class<?> loadedClass = urlClassLoader.loadClass(
                                        jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                if (ClusteringAlgorithmPluginInterface.class.isAssignableFrom(loadedClass)) {

                                    Data.clusteringAlgorithmJarNames.add(filename);
                                    Data.clusteringAlgorithmClassNames.add(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    Object classInstance = loadedClass.newInstance();
                                    Method method = loadedClass.getMethod("getName", new Class[]{});
                                    clusteringAlgorithmDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                }

                            }
                        }

                    }

                }

            } catch (Exception exception) {

                exception.printStackTrace();

                // clusteringAlgorithmDisplayNames is emptied so that the list of
                // plug-ins in the loadedClusteringPluginsJComboBox is empty when an error
                // occurs
                clusteringAlgorithmDisplayNames = new ArrayList<String>();

                JOptionPane.showMessageDialog(ManagePluginsJDialog.classInstance, "Plug-ins could not be loaded due to error");

            }

            ManagePluginsJDialog.classInstance.loadedClusteringPluginsJComboBox.setModel(new DefaultComboBoxModel(
                    clusteringAlgorithmDisplayNames.toArray(new String[clusteringAlgorithmDisplayNames.size()])));

        }

        if (actionEvent.getActionCommand().equals("Load Visualisation Plug-ins")) {

            // the visualisation plugins path entered by the user is stored and
            // the configuration file is updated (the
            // saveVisualisationPluginsPath method does this) so that the
            // information is saved when the user restarts JustClust
            Data.saveVisualisationPluginsPath(ManagePluginsJDialog.classInstance.visualisationPluginsPathJTextField.getText());

            // visualisationLayoutDisplayNames is declared outside the following try
            // block so that the loadedVisualisationPluginsJComboBox can use it outside the try
            // block later on
            ArrayList<String> visualisationLayoutDisplayNames = new ArrayList<String>();

            try {

                // this code populates the combo box contained in the
                // ManagePluginsJDialog.loadedVisualisationPluginsJComboBox field with the display
                // names of visualisation layout plug-ins.
                // each file in the file path of the visualisation layout plug-ins is
                // checked to see if it is a jar file.
                // each jar file found is then search for class files which
                // implement the VisualisationLayoutPluginInterface interface.
                // these class files are then loaded and there getName() methods
                // are called.
                // the display names are returned by the getName() methods.

                File file = new File(Data.visualisationPluginsPath);
                String[] list = file.list();
                Data.visualisationLayoutJarNames = new ArrayList<String>();
                Data.visualisationLayoutClassNames = new ArrayList<String>();
                for (String filename : list) {

                    if (filename.endsWith(".jar")) {

                        File jarFile = new File(Data.visualisationPluginsPath + '/' + filename);

                        Enumeration<JarEntry> enumeration = new JarFile(
                                jarFile).entries();

                        URLClassLoader urlClassLoader = URLClassLoader
                                .newInstance(new URL[]{jarFile.toURI()
                            .toURL()});

                        while (enumeration.hasMoreElements()) {
                            JarEntry jarEntry = enumeration.nextElement();
                            if (jarEntry.getName().endsWith(".class")) {

                                Class<?> loadedClass = urlClassLoader.loadClass(
                                        jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                if (VisualisationLayoutPluginInterface.class.isAssignableFrom(loadedClass)) {

                                    Data.visualisationLayoutJarNames.add(filename);
                                    Data.visualisationLayoutClassNames.add(
                                            jarEntry.getName().replaceAll("/", ".").replaceAll(".class", ""));

                                    Object classInstance = loadedClass.newInstance();
                                    Method method = loadedClass.getMethod("getName", new Class[]{});
                                    visualisationLayoutDisplayNames.add((String) method.invoke(classInstance, new Object[]{}));

                                }

                            }
                        }

                    }

                }

            } catch (Exception exception) {

                exception.printStackTrace();

                // visualisationLayoutDisplayNames is emptied so that the list of
                // plug-ins in the loadedVisualisationPluginsJComboBox is empty when an error
                // occurs
                visualisationLayoutDisplayNames = new ArrayList<String>();

                JOptionPane.showMessageDialog(ManagePluginsJDialog.classInstance, "Plug-ins could not be loaded due to error");

            }

            ManagePluginsJDialog.classInstance.loadedVisualisationPluginsJComboBox.setModel(new DefaultComboBoxModel(
                    visualisationLayoutDisplayNames.toArray(new String[visualisationLayoutDisplayNames.size()])));

        }

    }
}
