package justclust.menubar.applylayout;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.customcomponents.BrowseButton;
import justclust.graphdrawing.CustomGraphEditor;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

public class ApplyLayoutActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("comboBoxChanged")) {

            // this method adds the controls for the currently selected
            // plugin into the ApplyLayoutJDialog
            addPluginControls();

        }

        if (actionEvent.getActionCommand().equals("Apply Layout")) {

            ApplyLayoutJDialog.classInstance.applyLayoutJButton.setEnabled(false);

            // this code starts a new thread to apply a layout.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // layout is being applied in the new thread.
            ApplyLayoutThread applyLayoutThread = new ApplyLayoutThread();
            applyLayoutThread.start();

        }

    }

    // this method adds the controls for the currently selected
    // plugin into the ApplyLayoutJDialog
    void addPluginControls() {

        // if ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0,
        // then a plug-in has been selected
        if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0) {

            // the plug-in description is set to contain no characters here so that,
            // if there is an error in loading the description, this description
            // will be used to avoid issues
            String pluginDescription = "";

            try {

                // call the getDescription method of the plug-in to get
                // its description
                File file = new File(Data.visualisationPluginsPath
                        + '/'
                        + Data.visualisationLayoutJarNames
                        .get(ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                        .getSelectedIndex() - 1));
                URLClassLoader urlClassLoader = URLClassLoader
                        .newInstance(new URL[]{file.toURI().toURL()});
                ApplyLayoutJDialog.classInstance.pluginClass = urlClassLoader.loadClass(Data.visualisationLayoutClassNames
                        .get(ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                        .getSelectedIndex() - 1));
                ApplyLayoutJDialog.classInstance.pluginClassInstance = ApplyLayoutJDialog.classInstance.pluginClass.newInstance();
                Method method = ApplyLayoutJDialog.classInstance.pluginClass.getMethod("getDescription",
                        new Class[]{});
                pluginDescription = (String) method.invoke(ApplyLayoutJDialog.classInstance.pluginClassInstance, new Object[]{});

            } catch (Exception exception) {

                exception.printStackTrace();

                JOptionPane.showMessageDialog(
                        ApplyLayoutJDialog.classInstance,
                        "Plug-in description could not be loaded due to error");

                return;

            }

            try {

                // call the getConfigurationControls method of the plug-in to get
                // its configuration controls
                File file = new File(Data.visualisationPluginsPath
                        + '/'
                        + Data.visualisationLayoutJarNames
                        .get(ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                        .getSelectedIndex() - 1));
                URLClassLoader urlClassLoader = URLClassLoader
                        .newInstance(new URL[]{file.toURI().toURL()});
                ApplyLayoutJDialog.classInstance.pluginClass = urlClassLoader.loadClass(Data.visualisationLayoutClassNames
                        .get(ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox
                        .getSelectedIndex() - 1));
                ApplyLayoutJDialog.classInstance.pluginClassInstance = ApplyLayoutJDialog.classInstance.pluginClass.newInstance();
                Method method = ApplyLayoutJDialog.classInstance.pluginClass.getMethod("getConfigurationControls",
                        new Class[]{});
                ApplyLayoutJDialog.classInstance.pluginConfigurationControls = (ArrayList<PluginConfigurationControl>) method.invoke(ApplyLayoutJDialog.classInstance.pluginClassInstance, new Object[]{});

            } catch (Exception exception) {

                exception.printStackTrace();

                ApplyLayoutJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();

                JOptionPane.showMessageDialog(
                        ApplyLayoutJDialog.classInstance,
                        "Plug-in configuration controls could not be loaded due to error");

                return;

            }

            // add the pluginDetailsJLabel, pluginDescriptionJLabel, and
            // pluginDescriptionJScrollPane of the ApplyLayoutJDialog to the
            // applyLayoutDialogJPanel so that they can display the details of the
            // plug-in.
            // this should only happen if they haven't been added to the
            // applyLayoutDialogJPanel already.
            if (ApplyLayoutJDialog.classInstance.pluginDetailsJLabel.getParent() != ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(ApplyLayoutJDialog.classInstance.pluginDetailsJLabel);
            }
            if (ApplyLayoutJDialog.classInstance.pluginDescriptionJLabel.getParent() != ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(ApplyLayoutJDialog.classInstance.pluginDescriptionJLabel);
            }
            if (ApplyLayoutJDialog.classInstance.pluginDescriptionJScrollPane.getParent() != ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(ApplyLayoutJDialog.classInstance.pluginDescriptionJScrollPane);
            }

            // update the pluginDescriptionJTextArea to contain the contents of
            // pluginDescription which was assigned earlier
            ApplyLayoutJDialog.classInstance.pluginDescriptionJTextArea.setText(pluginDescription);
            // this makes the scrollbar of the pluginDescriptionJScrollPane start
            // at the top
            ApplyLayoutJDialog.classInstance.pluginDescriptionJTextArea.setCaretPosition(0);

        } else {

            // no plug-in is selected so remove the components which describe
            // plug-ins
            if (ApplyLayoutJDialog.classInstance.pluginDetailsJLabel.getParent() == ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.remove(ApplyLayoutJDialog.classInstance.pluginDetailsJLabel);
            }
            if (ApplyLayoutJDialog.classInstance.pluginDescriptionJLabel.getParent() == ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.remove(ApplyLayoutJDialog.classInstance.pluginDescriptionJLabel);
            }
            if (ApplyLayoutJDialog.classInstance.pluginDescriptionJScrollPane.getParent() == ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel) {
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.remove(ApplyLayoutJDialog.classInstance.pluginDescriptionJScrollPane);
            }

            // there are no pluginConfigurationControls
            ApplyLayoutJDialog.classInstance.pluginConfigurationControls = new ArrayList<PluginConfigurationControl>();

        }

        // remove any jcomponents which have been added to the
        // ApplyLayoutJDialog by previous plug-ins
        if (ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents != null) {
            for (ArrayList<JComponent> arrayList : ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents) {
                for (JComponent jComponent : arrayList) {
                    ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.remove(jComponent);
                    // if the JComponent is a BrowseButton, it will have had the
                    // ApplyLayoutJDialogMouseListener added as a mouse
                    // listener.
                    // the ApplyLayoutJDialogMouseListener should be removed.
                    if (jComponent instanceof BrowseButton) {
                        jComponent.removeMouseListener(ApplyLayoutMouseListener.classInstance);
                    }
                }
            }
        }

        // components which correspond to the pluginConfigurationControls
        // are created and added to the ApplyLayoutJDialog
        ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents = new ArrayList<ArrayList<JComponent>>();
        for (PluginConfigurationControl control : ApplyLayoutJDialog.classInstance.pluginConfigurationControls) {
            if (control instanceof CheckBoxControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((CheckBoxControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JCheckBox jCheckBox = new JCheckBox();
                jCheckBox.setSelected(((CheckBoxControl) control).checked);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jCheckBox);
                arrayList.add(jCheckBox);
                ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof ComboBoxControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((ComboBoxControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jLabel);
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
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jComboBox);
                arrayList.add(jComboBox);
                ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof FileSystemPathControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((FileSystemPathControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JTextField jTextField = new JTextField(((FileSystemPathControl) control).text);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jTextField);
                arrayList.add(jTextField);
                BrowseButton browseButton = new BrowseButton();
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(browseButton);
                browseButton.addMouseListener(ApplyLayoutMouseListener.classInstance);
                arrayList.add(browseButton);
                ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
            if (control instanceof TextFieldControl) {
                ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
                JLabel jLabel = new JLabel(((TextFieldControl) control).label);
                Font font = new Font("Dialog", Font.PLAIN, 12);
                jLabel.setFont(font);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jLabel);
                arrayList.add(jLabel);
                JTextField jTextField = new JTextField(((TextFieldControl) control).text);
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(jTextField);
                arrayList.add(jTextField);
                ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.add(arrayList);
            }
        }

        // pluginDetailsVerticalDisplacement is based on how many components are added to the
        // ApplyLayoutJDialog by plug-ins to configure and describe them.
        // if components have been added, this affects the vertical positioning
        // of all the components in the ApplyLayoutJDialog.
        int pluginDetailsVerticalDisplacement = 0;
        // a positive selected index for the visualisationLayoutJComboBox indicates that a
        // plug-in has been selected and therefore some components have been
        // added to the ApplyLayoutJDialog to discribe the plug-in
        if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0) {
            pluginDetailsVerticalDisplacement = 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10;
        }

        // set the y coordinate of the ApplyLayoutJDialog so that it remains
        // centered around the point it currently is around when its height
        // is increased.
        // the difference in height between the new height and old height
        // is halved and taken away from the current y coordinate of the
        // ApplyLayoutJDialog.
        // also, increase the height of the ApplyLayoutJDialog.
        ApplyLayoutJDialog.classInstance.setBounds(
                ApplyLayoutJDialog.classInstance.getLocation().x,
                ApplyLayoutJDialog.classInstance.getLocation().y
                - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + pluginDetailsVerticalDisplacement + 25 + 10
                - ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight()) / 2),
                ApplyLayoutJDialog.classInstance.getWidth(),
                ApplyLayoutJDialog.classInstance.getInsets().top
                + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + pluginDetailsVerticalDisplacement + 25 + 10
                + ApplyLayoutJDialog.classInstance.getInsets().bottom);

        // the position of the new components is set.
        // this is necessary if the ApplyLayoutJDialog was not resized above.
        ApplyLayoutJDialog.classInstance.applyLayoutJDialogComponentListener.componentResized(null);

    }

    /**
     * This inner class has a method which lays-out the current network with a
     * new thread.
     */
    class ApplyLayoutThread extends Thread {

        /**
         * This method lays-out the current network with a new thread.
         */
        public void run() {

            // the applyLayoutJProgressBar is added to the
            // applyLayoutDialogJPanel
            ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.add(ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar);

            // if ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0,
            // then a plug-in has been selected
            if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0) {
                // set the y coordinate of the ApplyLayoutJDialog so that it remains
                // centered around the point it currently is around when its height
                // is increased.
                // the difference in height between the new height and old height
                // is halved and taken away from the current y coordinate of the
                // ApplyLayoutJDialog.
                // also, increase the height of the ApplyLayoutJDialog.
                ApplyLayoutJDialog.classInstance.setBounds(
                        ApplyLayoutJDialog.classInstance.getLocation().x,
                        ApplyLayoutJDialog.classInstance.getLocation().y
                        - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10 + 25 + 10
                        - ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight()) / 2),
                        ApplyLayoutJDialog.classInstance.getWidth(),
                        ApplyLayoutJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10 + 25 + 10
                        + ApplyLayoutJDialog.classInstance.getInsets().bottom);
            } else {
                // set the y coordinate of the ApplyLayoutJDialog so that it remains
                // centered around the point it currently is around when its height
                // is increased.
                // the difference in height between the new height and old height
                // is halved and taken away from the current y coordinate of the
                // ApplyLayoutJDialog.
                // also, increase the height of the ApplyLayoutJDialog.
                ApplyLayoutJDialog.classInstance.setBounds(
                        ApplyLayoutJDialog.classInstance.getLocation().x,
                        ApplyLayoutJDialog.classInstance.getLocation().y
                        - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                        - ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.getHeight()) / 2),
                        ApplyLayoutJDialog.classInstance.getWidth(),
                        ApplyLayoutJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                        + ApplyLayoutJDialog.classInstance.getInsets().bottom);
            }

            // activate the applyLayoutJProgressBar to indicate that some
            // process is running
            ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar.setIndeterminate(true);

            ApplyLayoutJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Applying layout...");

            try {

                // pass the user specified information for configuring the
                // plug-in to the plug-in by updating fields of the
                // PluginConfigurationControls based on the contents of the
                // corresponding JComponents
                for (int i = 0; i < ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size(); i++) {
                    if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof CheckBoxControl) {
                        ((CheckBoxControl) ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i)).checked = ((JCheckBox) ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).isSelected();
                    }
                    if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof ComboBoxControl) {
                        ((ComboBoxControl) ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i)).selectedOptionIndex = ((JComboBox) ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getSelectedIndex();
                    }
                    if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof FileSystemPathControl) {
                        ((FileSystemPathControl) ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i)).text = ((JTextField) ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getText();
                    }
                    if (ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i) instanceof TextFieldControl) {
                        ((TextFieldControl) ApplyLayoutJDialog.classInstance.pluginConfigurationControls.get(i)).text = ((JTextField) ApplyLayoutJDialog.classInstance.pluginConfigurationJComponents.get(i).get(1)).getText();
                    }
                }

                // get the current Data instance for the following code to use
                int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
                Data data = Data.data.get(currentCustomGraphEditorIndex);

                // This code calls the applyLayout method of the class file
                // for the current visualisation layout.
                Method method = ApplyLayoutJDialog.classInstance.pluginClass.getMethod("applyLayout",
                        new Class[]{ArrayList.class, ArrayList.class, ArrayList.class});
                long visualisationLayoutStartTime = new Date().getTime();
                method.invoke(ApplyLayoutJDialog.classInstance.pluginClassInstance,
                        new Object[]{data.networkNodes, data.networkEdges, data.networkClusters});
                data.timeTakenToApplyLayout = new Date().getTime() - visualisationLayoutStartTime;

            } catch (Exception exception) {

                ApplyLayoutJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the applyLayoutJProgressBar is remove from the
                // applyLayoutDialogJPanel
                ApplyLayoutJDialog.classInstance.applyLayoutDialogJPanel.remove(ApplyLayoutJDialog.classInstance.applyLayoutJProgressBar);

                // if ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0,
                // then a plug-in has been selected
                if (ApplyLayoutJDialog.classInstance.visualisationLayoutJComboBox.getSelectedIndex() > 0) {
                    // set the y coordinate of the ApplyLayoutJDialog so that it remains
                    // centered around the point it currently is around when its height
                    // is decreased.
                    // the difference in height between the new height and old height
                    // is halved and added to the current y coordinate of the
                    // ApplyLayoutJDialog.
                    // also, decrease the height of the ApplyLayoutJDialog.
                    ApplyLayoutJDialog.classInstance.setBounds(
                            ApplyLayoutJDialog.classInstance.getLocation().x,
                            ApplyLayoutJDialog.classInstance.getLocation().y
                            + Math.round((10 + 25) / 2),
                            ApplyLayoutJDialog.classInstance.getWidth(),
                            ApplyLayoutJDialog.classInstance.getInsets().top
                            + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 60 + 10 + (25 + 10 + 25 + 10) * ApplyLayoutJDialog.classInstance.pluginConfigurationControls.size() + 1 + 10 + 25 + 10
                            + ApplyLayoutJDialog.classInstance.getInsets().bottom);
                } else {
                    // set the y coordinate of the ApplyLayoutJDialog so that it remains
                    // centered around the point it currently is around when its height
                    // is decreased.
                    // the difference in height between the new height and old height
                    // is halved and added to the current y coordinate of the
                    // ApplyLayoutJDialog.
                    // also, decrease the height of the ApplyLayoutJDialog.
                    ApplyLayoutJDialog.classInstance.setBounds(
                            ApplyLayoutJDialog.classInstance.getLocation().x,
                            ApplyLayoutJDialog.classInstance.getLocation().y
                            + Math.round((10 + 25) / 2),
                            ApplyLayoutJDialog.classInstance.getWidth(),
                            ApplyLayoutJDialog.classInstance.getInsets().top
                            + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                            + ApplyLayoutJDialog.classInstance.getInsets().bottom);
                }

                ApplyLayoutJDialog.classInstance.applyLayoutJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Layout could not be completed due to error");

                return;

            }

            // get the current Graph for the following code to use
            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
            CustomGraphEditor currentCustomGraphEditor = JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex);

            // position the labels in the new layout
            ArrayList<PText> labels = (ArrayList<PText>) currentCustomGraphEditor.labelLayer.getAllNodes();
            for (int i = 1; i < currentCustomGraphEditor.labelLayer.getAllNodes().size(); i++) {
                PText label = (PText) labels.get(i);
                double x = 0;
                double y = 0;
                ArrayList<PPath> labelNodes = (ArrayList<PPath>) label.getAttribute("nodes");
                for (int j = 0; j < labelNodes.size(); j++) {
                    PPath node = (PPath) labelNodes.get(j);
                    x += (node.getX() + 10) / labelNodes.size();
                    y += (node.getY() + 10) / labelNodes.size();
                }
                label.setX(x - label.getWidth() / 2);
                label.setY(y - label.getHeight() / 2);
            }

            // position the edges in the new layout
            ArrayList<PPath> edges = (ArrayList<PPath>) currentCustomGraphEditor.edgeLayer.getAllNodes();
            for (int i = 1; i < currentCustomGraphEditor.edgeLayer.getAllNodes().size(); i++) {
                PPath edge = (PPath) edges.get(i);
                PPath node1 = (PPath) edge.getAttribute("node1");
                PPath node2 = (PPath) edge.getAttribute("node2");
//                Point2D start = node1.getFullBoundsReference().getCenter2D();
//                Point2D end = node2.getFullBoundsReference().getCenter2D();
//                edge.reset();
//                edge.moveTo((float) start.getX(), (float) start.getY());
//                edge.lineTo((float) end.getX(), (float) end.getY());
                edge.reset();
                edge.moveTo((float) node1.getX() + 10, (float) node1.getY() + 10);
                edge.lineTo((float) node2.getX() + 10, (float) node2.getY() + 10);
            }

            // the entire graph is fitted into the main graphical view
            final Rectangle2D drag_bounds = currentCustomGraphEditor.getCamera().getUnionOfLayerFullBounds();
            currentCustomGraphEditor.getCamera().animateViewToCenterBounds(drag_bounds, true, 0);

            // due to zooming the graph cannot be more than 2 times smaller or more
            // than 50 times bigger than its initial size
            currentCustomGraphEditor.customPZoomEventHandler.setMinScale(currentCustomGraphEditor.getCamera().getViewScale() / 2);
            currentCustomGraphEditor.customPZoomEventHandler.setMaxScale(currentCustomGraphEditor.getCamera().getViewScale() * 50);

            ApplyLayoutJDialog.classInstance.setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            ApplyLayoutJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel
                    .setText("Layout applied");

        }
    }
}
