package justclust.menubar.newnetworkfromfile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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
import java.util.Collections;
import java.util.Comparator;
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
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.ButtonTabComponent;
import justclust.JustclustMouseListener;
import justclust.customcomponents.BrowseButton;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.toolbar.dendrogram.DendrogramJDialog;
import justclust.toolbar.filterclusters.FilterClustersJDialog;
import justclust.toolbar.heatmap.HeatMapJDialog;
import justclust.toolbar.microarrayheatmap.MicroarrayHeatMapJDialog;
import justclust.toolbar.networkclusters.NetworkClustersJDialog;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;
import justclust.toolbar.networkedges.NetworkEdgesJDialog;
import justclust.toolbar.networknodes.NetworkNodesJDialog;
import justclust.toolbar.overrepresentationanalysis.OverrepresentationAnalysisJDialog;
import justclust.toolbar.searchnetwork.SearchNetworkJDialog;

public class NewNetworkFromFileActionListener implements ActionListener {

    /**
     * This method responds to user input which is made using a
     * NewNetworkFromFileJDialog.
     */
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("comboBoxChanged")) {

            // this method adds the controls for the currently selected
            // plugin into the NewNetworkFromFileJDialog
            addPluginControls();

        }

        if (actionEvent.getActionCommand().equals("Create Network")) {

            NewNetworkFromFileJDialog.classInstance.createNetworkJButton.setEnabled(false);

            // this code starts a new thread to create a network.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // network is being created in the new thread.
            NewNetworkThread createNetworkThread = new NewNetworkThread();
            createNetworkThread.start();

        }

    }

    // this method adds the controls for the currently selected
    // plugin into the NewNetworkFromFileJDialog
    void addPluginControls() {

        // if NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0,
        // then a plug-in has been selected
        if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {

            // the plug-in description is set to contain no characters here so that,
            // if there is an error in loading the description, this description
            // will be used to avoid issues
            String pluginDescription = "";

            try {

                // call the getDescription method of the plug-in to get
                // its description
                File file = new File(Data.parsingPluginsPath
                        + '/'
                        + Data.fileParserJarNames
                        .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                        .getSelectedIndex() - 1));
                URLClassLoader urlClassLoader = URLClassLoader
                        .newInstance(new URL[]{file.toURI().toURL()});
                NewNetworkFromFileJDialog.classInstance.pluginClass = urlClassLoader.loadClass(Data.fileParserClassNames
                        .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                        .getSelectedIndex() - 1));
                NewNetworkFromFileJDialog.classInstance.pluginClassInstance = NewNetworkFromFileJDialog.classInstance.pluginClass.newInstance();
                Method method = NewNetworkFromFileJDialog.classInstance.pluginClass.getMethod("getDescription",
                        new Class[]{});
                pluginDescription = (String) method.invoke(NewNetworkFromFileJDialog.classInstance.pluginClassInstance, new Object[]{});

            } catch (Exception exception) {

                exception.printStackTrace();

                JOptionPane.showMessageDialog(
                        NewNetworkFromFileJDialog.classInstance,
                        "Plug-in description could not be loaded due to error");

                return;

            }

            try {

                // call the getConfigurationControls method of the plug-in to get
                // its configuration controls
                File file = new File(Data.parsingPluginsPath
                        + '/'
                        + Data.fileParserJarNames
                        .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                        .getSelectedIndex() - 1));
                URLClassLoader urlClassLoader = URLClassLoader
                        .newInstance(new URL[]{file.toURI().toURL()});
                NewNetworkFromFileJDialog.classInstance.pluginClass = urlClassLoader.loadClass(Data.fileParserClassNames
                        .get(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox
                        .getSelectedIndex() - 1));
                NewNetworkFromFileJDialog.classInstance.pluginClassInstance = NewNetworkFromFileJDialog.classInstance.pluginClass.newInstance();
                Method method = NewNetworkFromFileJDialog.classInstance.pluginClass.getMethod("getConfigurationControls",
                        new Class[]{});
                NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = (ArrayList<PluginConfigurationControl>) method.invoke(NewNetworkFromFileJDialog.classInstance.pluginClassInstance, new Object[]{});

            } catch (Exception exception) {

                NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();

                JOptionPane.showMessageDialog(
                        NewNetworkFromFileJDialog.classInstance,
                        "Plug-in configuration controls could not be loaded due to error");

                return;

            }

            // add the pluginDetailsJLabel, pluginDescriptionJLabel, and
            // pluginDescriptionJScrollPane of the NetworkJDialog to the
            // newNetworkDialogJPanel so that they can display the details of the
            // plug-in.
            // this should only happen if they haven't been added to the
            // newNetworkDialogJPanel already.
            if (NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel);
            }
            if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel);
            }
            if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane.getParent() != NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane);
            }

            // update the pluginDescriptionJTextArea to contain the contents of
            // pluginDescription which was assigned earlier
            NewNetworkFromFileJDialog.classInstance.pluginDescriptionJTextArea.setText(pluginDescription);
            // this makes the scrollbar of the pluginDescriptionJScrollPane start
            // at the top
            NewNetworkFromFileJDialog.classInstance.pluginDescriptionJTextArea.setCaretPosition(0);

        } else {

            // no plug-in is selected so remove the components which describe
            // plug-ins
            if (NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel.getParent() == NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.remove(NewNetworkFromFileJDialog.classInstance.pluginDetailsJLabel);
            }
            if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel.getParent() == NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.remove(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJLabel);
            }
            if (NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane.getParent() == NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel) {
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.remove(NewNetworkFromFileJDialog.classInstance.pluginDescriptionJScrollPane);
            }

            // there are no pluginConfigurationControls
            NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();

        }

        // remove any jcomponents which have been added to the
        // NewNetworkDialogJPanel by previous plug-ins
        if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents != null) {
            for (ArrayList<JComponent> arrayList : NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents) {
                for (JComponent jComponent : arrayList) {
                    NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.remove(jComponent);
                    // if the JComponent is a BrowseButton, it will have had the
                    // NewNetworkJDialogMouseActionListener added as a mouse
                    // listener.
                    // the NewNetworkJDialogMouseActionListener should be removed.
                    if (jComponent instanceof BrowseButton) {
                        jComponent.removeMouseListener(NewNetworkFromFileMouseListener.classInstance);
                    }
                }
            }
        }

        // components which correspond to the pluginConfigurationControls
        // are created and added to the NewNetworkFromFileJDialog
        NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents = new ArrayList<ArrayList<JComponent>>();
        for (PluginConfigurationControl control : NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls) {
            if (control instanceof CheckBoxControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((CheckBoxControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JCheckBox jCheckBox = new JCheckBox();
                jCheckBox.setSelected(((CheckBoxControl) control).checked);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jCheckBox);
                arrayList.add(jCheckBox);
                NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof ComboBoxControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((ComboBoxControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JComboBox jComboBox = new JComboBox();
                jComboBox
                        .setModel(
                        new DefaultComboBoxModel(
                        ((ComboBoxControl) control).options
                        .toArray(new String[((ComboBoxControl) control).options
                        .size()])));
                jComboBox.setSelectedIndex(((ComboBoxControl) control).selectedOptionIndex);
                jComboBox.setFont(font);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jComboBox);
                arrayList.add(jComboBox);
                NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof FileSystemPathControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((FileSystemPathControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JTextField jTextField = new JTextField(((FileSystemPathControl) control).text);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jTextField);
                arrayList.add(jTextField);
                BrowseButton browseButton = new BrowseButton();
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(browseButton);
                browseButton.addMouseListener(NewNetworkFromFileMouseListener.classInstance);
                arrayList.add(browseButton);
                NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof TextFieldControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((TextFieldControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JTextField jTextField = new JTextField(((TextFieldControl) control).text);
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(jTextField);
                arrayList.add(jTextField);
                NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
        }

        // pluginDetailsVerticalDisplacement is based on how many components are added to the
        // NewNetworkFromFileJDialog by plug-ins to configure and describe them.
        // if components have been added, this affects the vertical positioning
        // of all the components in the NewNetworkFromFileJDialog.
        int pluginDetailsVerticalDisplacement = 0;
        // a positive selected index for the fileParserJComboBox indicates that a
        // plug-in has been selected and therefore some components have been
        // added to the NewNetworkFromFileJDialog to discribe the plug-in
        if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {
            pluginDetailsVerticalDisplacement = 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10;
        }

        // set the y coordinate of the NewNetworkFromFileJDialog so that it remains
        // centered around the point it currently is around when its height
        // is increased.
        // the difference in height between the new height and old height
        // is halved and taken away from the current y coordinate of the
        // NewNetworkFromFileJDialog.
        // also, increase the height of the NewNetworkFromFileJDialog.
        NewNetworkFromFileJDialog.classInstance.setBounds(
                NewNetworkFromFileJDialog.classInstance.getLocation().x,
                NewNetworkFromFileJDialog.classInstance.getLocation().y
                - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + pluginDetailsVerticalDisplacement + 25 + 10
                - NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight()) / 2),
                NewNetworkFromFileJDialog.classInstance.getWidth(),
                NewNetworkFromFileJDialog.classInstance.getInsets().top
                + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + pluginDetailsVerticalDisplacement + 25 + 10
                + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);

        // the position of the new components is set.
        // this is necessary if the NewNetworkFromFileJDialog was not resized above.
        NewNetworkFromFileJDialog.classInstance.newNetworkJDialogComponentListener.componentResized(null);

    }

    /**
     * This inner class has a method which creates a network with a new thread.
     */
    class NewNetworkThread extends Thread {

        /**
         * This method creates a network with a new thread.
         */
        public void run() {

            // the createNetworkJProgressBar is added to the
            // newNetworkDialogJPanel
            NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.add(NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar);

            // if NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0,
            // then a plug-in has been selected
            if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {
                // set the y coordinate of the NewNetworkFromFileJDialog so that it remains
                // centered around the point it currently is around when its height
                // is increased.
                // the difference in height between the new height and old height
                // is halved and taken away from the current y coordinate of the
                // NewNetworkFromFileJDialog.
                // also, increase the height of the NewNetworkFromFileJDialog.
                NewNetworkFromFileJDialog.classInstance.setBounds(
                        NewNetworkFromFileJDialog.classInstance.getLocation().x,
                        NewNetworkFromFileJDialog.classInstance.getLocation().y
                        - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10 + 25 + 10
                        - NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight()) / 2),
                        NewNetworkFromFileJDialog.classInstance.getWidth(),
                        NewNetworkFromFileJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10 + 25 + 10
                        + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);
            } else {
                // set the y coordinate of the NewNetworkFromFileJDialog so that it remains
                // centered around the point it currently is around when its height
                // is increased.
                // the difference in height between the new height and old height
                // is halved and taken away from the current y coordinate of the
                // NewNetworkFromFileJDialog.
                // also, increase the height of the NewNetworkFromFileJDialog.
                NewNetworkFromFileJDialog.classInstance.setBounds(
                        NewNetworkFromFileJDialog.classInstance.getLocation().x,
                        NewNetworkFromFileJDialog.classInstance.getLocation().y
                        - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                        - NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.getHeight()) / 2),
                        NewNetworkFromFileJDialog.classInstance.getWidth(),
                        NewNetworkFromFileJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                        + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);
            }

            // activate the createNetworkJProgressBar to indicate that some
            // process is running
            NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar.setIndeterminate(true);

            NewNetworkFromFileJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Creating network...");

            // a new instance of the Data class is created
            Data data = new Data();

            try {

                // pass the user specified information for configuring the
                // plug-in to the plug-in by updating fields of the
                // PluginConfigurationControls based on the contents of the
                // corresponding JComponents
                for (int i = 0; i < NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size(); i++) {
                    if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof CheckBoxControl) {
                        ((CheckBoxControl) NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i)).checked = ((JCheckBox) NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).isSelected();
                    }
                    if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof ComboBoxControl) {
                        ((ComboBoxControl) NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i)).selectedOptionIndex = ((JComboBox) NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getSelectedIndex();
                    }
                    if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl) {
                        ((FileSystemPathControl) NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i)).text = ((JTextField) NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getText();
                    }
                    if (NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i) instanceof TextFieldControl) {
                        ((TextFieldControl) NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.get(i)).text = ((JTextField) NewNetworkFromFileJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getText();
                    }
                }

                // This code calls the parseFile method of the class file
                // for the current file parser.
                Method method = NewNetworkFromFileJDialog.classInstance.pluginClass.getMethod(
                        "parseFile", new Class[]{File.class, ArrayList.class, ArrayList.class});
                data.networkNodes = new ArrayList<Node>();
                data.networkEdges = new ArrayList<Edge>();
                long fileParserStartTime = new Date().getTime();
                method.invoke(NewNetworkFromFileJDialog.classInstance.pluginClassInstance, new Object[]{
                    new File(NewNetworkFromFileJDialog.classInstance.inputFileJTextField.getText()),
                    data.networkNodes,
                    data.networkEdges});
                data.timeTakenToCreateNetwork = new Date().getTime() - fileParserStartTime;

                method = NewNetworkFromFileJDialog.classInstance.pluginClass.getMethod(
                        "microarrayData", new Class[]{});
                data.microarrayData = (boolean) method.invoke(
                        NewNetworkFromFileJDialog.classInstance.pluginClassInstance, new Object[]{});

                method = NewNetworkFromFileJDialog.classInstance.pluginClass.getMethod(
                        "microarrayHeaders", new Class[]{});
                data.microarrayHeaders = (ArrayList<String>) method.invoke(
                        NewNetworkFromFileJDialog.classInstance.pluginClassInstance, new Object[]{});

            } catch (Exception exception) {

                exception.printStackTrace();

                NewNetworkFromFileJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the createNetworkJProgressBar is removed from the
                // newNetworkDialogJPanel
                NewNetworkFromFileJDialog.classInstance.newNetworkDialogJPanel.remove(NewNetworkFromFileJDialog.classInstance.createNetworkJProgressBar);

                // if NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0,
                // then a plug-in has been selected
                if (NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedIndex() > 0) {
                    // set the y coordinate of the NewNetworkFromFileJDialog so that it remains
                    // centered around the point it currently is around when its height
                    // is decreased.
                    // the difference in height between the new height and old height
                    // is halved and added to the current y coordinate of the
                    // NewNetworkFromFileJDialog.
                    // also, decrease the height of the NewNetworkFromFileJDialog.
                    NewNetworkFromFileJDialog.classInstance.setBounds(
                            NewNetworkFromFileJDialog.classInstance.getLocation().x,
                            NewNetworkFromFileJDialog.classInstance.getLocation().y
                            + Math.round((10 + 25) / 2),
                            NewNetworkFromFileJDialog.classInstance.getWidth(),
                            NewNetworkFromFileJDialog.classInstance.getInsets().top
                            + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * NewNetworkFromFileJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10
                            + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);
                } else {
                    // set the y coordinate of the NewNetworkFromFileJDialog so that it remains
                    // centered around the point it currently is around when its height
                    // is decreased.
                    // the difference in height between the new height and old height
                    // is halved and added to the current y coordinate of the
                    // NewNetworkFromFileJDialog.
                    // also, decrease the height of the NewNetworkFromFileJDialog.
                    NewNetworkFromFileJDialog.classInstance.setBounds(
                            NewNetworkFromFileJDialog.classInstance.getLocation().x,
                            NewNetworkFromFileJDialog.classInstance.getLocation().y
                            + Math.round((10 + 25) / 2),
                            NewNetworkFromFileJDialog.classInstance.getWidth(),
                            NewNetworkFromFileJDialog.classInstance.getInsets().top
                            + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                            + NewNetworkFromFileJDialog.classInstance.getInsets().bottom);
                }

                NewNetworkFromFileJDialog.classInstance.createNetworkJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Parsing could not be completed due to error");

                return;

            }

            // remove any open dialogs
            if (NetworkDetailsJDialog.classInstance != null) {
                NetworkDetailsJDialog.classInstance.dispose();
            }
            if (NetworkNodesJDialog.classInstance != null) {
                NetworkNodesJDialog.classInstance.dispose();
            }
            if (NetworkEdgesJDialog.classInstance != null) {
                NetworkEdgesJDialog.classInstance.dispose();
            }
            if (NetworkClustersJDialog.classInstance != null) {
                NetworkClustersJDialog.classInstance.dispose();
            }
            if (SearchNetworkJDialog.classInstance != null) {
                SearchNetworkJDialog.classInstance.dispose();
            }
            if (FilterClustersJDialog.classInstance != null) {
                FilterClustersJDialog.classInstance.dispose();
            }
            if (OverrepresentationAnalysisJDialog.classInstance != null) {
                OverrepresentationAnalysisJDialog.classInstance.dispose();
            }
            if (HeatMapJDialog.classInstance != null) {
                HeatMapJDialog.classInstance.dispose();
            }
            if (MicroarrayHeatMapJDialog.classInstance != null) {
                MicroarrayHeatMapJDialog.classInstance.dispose();
            }
            if (DendrogramJDialog.classInstance != null) {
                DendrogramJDialog.classInstance.dispose();
            }

            Data.data.add(data);

            // networkClusters is set to null so that Graph will not treat a new network
            // as though it has clusters because the last network did
            data.networkClusters = null;

            // iterate through each Node and create an ArrayList of Edges
            // for the Node
            for (Node node : data.networkNodes) {
                node.edges = new ArrayList<Edge>();
            }
            // iterate through each Edge and, for each Edge, iterate
            // through each Node.
            // add the Edge to the Node's list of Edges.
            for (Edge edge : data.networkEdges) {
                edge.node1.edges.add(edge);
                // check that the edge isn't a loop and therefore already added
                if (edge.node1 != edge.node2) {
                    edge.node2.edges.add(edge);
                }
            }

            // iterate through each Node
            for (Node node : data.networkNodes) {
                // set the visible field to true for the Node because all nodes
                // are visible initially
                node.visible = true;
                // set the colour field to Color.WHITE for the Node because all
                // nodes are white initially
                node.colour = Color.WHITE;
                // initialize the otherVersions field for the Node so that an
                // error does not occur when some code tries to update the other
                // versions of this Node (when this Node is updated)
                node.otherVersions = new ArrayList<Node>();
                // initialize the data field for the Node so that, if the Node
                // is saved as part of a session, which graph it belongs to can
                // be discovered easily (each Data instance corresponds to
                // exactly one graph)
                node.data = data;
            }

            // iterate through each Edge
            for (Edge edge : data.networkEdges) {
                // set the visible field to true for the Edge because all edges
                // are visible initially
                edge.visible = true;
                // set the colour field to Color.BLACK for the Edge because all
                // edges are black initially
                edge.colour = Color.BLACK;
                // initialize the otherVersions field for the Edge so that an
                // error does not occur when some code tries to update the other
                // versions of this Edge (when this Edge is updated)
                edge.otherVersions = new ArrayList<Edge>();
                // initialize the data field for the Edge so that, if the Edge
                // is saved as part of a session, which graph it belongs to can
                // be discovered easily (each Data instance corresponds to
                // exactly one graph)
                edge.data = data;
            }

            // this code sorts the Nodes in the networkNodes data
            // structure in alphabetical order.
            // this is done before the createGraph method is called so that the
            // graphical representations of the Nodes are created in this order.
            // this is important because indices of the network Nodes are used
            // to find the indices of graphical nodes later in layouts.
            // the ordering is done so that the Nodes are displayed in order
            // in the NetworkNodesJDialog.
            for (int i = 1; i < data.networkNodes.size(); i++) {
                Node node = data.networkNodes.get(i);
                int j;
                for (j = i - 1; j >= 0
                        && node.label.compareTo(data.networkNodes.get(j).label) < 0; j--) {
                    data.networkNodes.set(j + 1, data.networkNodes.get(j));
                }
                data.networkNodes.set(j + 1, node);
            }

            // this code sorts the Edges in the networkEdges data
            // structure from largest weight field to smallest weight field.
            // this is done before the createGraph method is called so that the
            // graphical representations of the Edges are created in this order.
            // this is important because indexes of the network Edges are used
            // to find the indexes of graphical edges later in layouts.
            // the ordering is done so that the Edges are displayed in order
            // in the NetworkEdgesJDialog.
//            for (int i = 1; i < data.networkEdges.size(); i++) {
//                System.out.println("sorting edges " + i);
//                Edge edge = data.networkEdges.get(i);
//                int j;
//                for (j = i - 1; j >= 0
//                        && edge.weight > data.networkEdges.get(j).weight; j--) {
//                    data.networkEdges.set(j + 1, data.networkEdges.get(j));
//                }
//                data.networkEdges.set(j + 1, edge);
//            }
            Collections.sort(data.networkEdges, new Comparator<Edge>() {
                public int compare(Edge o1, Edge o2) {
                    if (o1.weight == o2.weight) {
                        return 0;
                    }
                    return o1.weight < o2.weight ? -1 : 1;
                }
            });

            // create a new graph to display a graphical representation of the
            // network
            final CustomGraphEditor customGraphEditor = new CustomGraphEditor(data.networkNodes, data.networkEdges, data.networkClusters);
            JustclustJFrame.classInstance.customGraphEditors.add(customGraphEditor);
            // register the customGraphEditor with a
            // JustclustActionListener so that the
            // JustclustActionListener can respond to buttons on the main
            // graphical view being clicked
            JustclustMouseListener justclustMouseListener = new JustclustMouseListener();
            customGraphEditor.addMouseListener(justclustMouseListener);
            // passing an empty String to the setToolTipText method enables tooltips
            // for customGraphEditor
            customGraphEditor.setToolTipText("");
            String title = new File(NewNetworkFromFileJDialog.classInstance.inputFileJTextField.getText()).getName();
            if (title.length() > 15) {
                title = title.substring(0, 12) + "...";
            }
            JustclustJFrame.classInstance.tabTitles.add(new File(NewNetworkFromFileJDialog.classInstance.inputFileJTextField.getText()).getName());
            JustclustJFrame.classInstance.justclustJTabbedPane.addTab(title, customGraphEditor);
            int graphAmount = JustclustJFrame.classInstance.customGraphEditors.size();
            JustclustJFrame.classInstance.justclustJTabbedPane.setSelectedIndex(graphAmount - 1);
            ButtonTabComponent buttonTabComponent = new ButtonTabComponent(JustclustJFrame.classInstance.justclustJTabbedPane);
            JustclustJFrame.classInstance.justclustJTabbedPane.setTabComponentAt(graphAmount - 1, buttonTabComponent);
            JustclustJFrame.classInstance.justclustJTabbedPane.setToolTipTextAt(graphAmount - 1, new File(NewNetworkFromFileJDialog.classInstance.inputFileJTextField.getText()).getName());

            // the createGraph method produces a graphical representation of the
            // current network for the Graph in the first tab
            customGraphEditor.createGraph();

            // lay out the nodes in a grid.
            // the shouldRepaint field is false while the layout is being
            // applied so that the customGraphEditor is not repainted during the
            // layout.
            // this would cause an error for some unkown reason.
            customGraphEditor.shouldRepaint = false;
            customGraphEditor.applyCytoscapeGridLayout();
            customGraphEditor.shouldRepaint = true;

            // the repaint method updates the buttons on the toolbar so that
            // they are no longer disabled now that there is information about
            // the current network
            JustclustJFrame.classInstance.repaint();

//            // enable menu items which were disabled but should now be
//            // accessible
//            JustclustJFrame.classInstance.saveNetworkJMenuItem.setEnabled(true);
//            JustclustJFrame.classInstance.clusterNetworkJMenuItem.setEnabled(true);
//            JustclustJFrame.classInstance.applyLayoutJMenuItem.setEnabled(true);

            // the fileName and fileParser fields of the Data class are
            // populated so that the NetworkDetailsJDialog can display their
            // information
            data.fileName = NewNetworkFromFileJDialog.classInstance.inputFileJTextField.getText();
//            NewNetworkFromFileJDialog.classInstance.file = null;
            data.fileParser = String.valueOf(NewNetworkFromFileJDialog.classInstance.fileParserJComboBox.getSelectedItem());

            NewNetworkFromFileJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            NewNetworkFromFileJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Network created");

        }
    }
}
