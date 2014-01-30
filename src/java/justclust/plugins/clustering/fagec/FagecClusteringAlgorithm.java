/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.fagec;

import justclust.plugins.clustering.mcode.*;
import java.util.ArrayList;
import justclust.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import static justclust.plugins.clustering.mcode.McodeParameterSet.NETWORK;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.interfaces.ClusteringAlgorithmPlugin;

/**
 *
 * @author wuaz008
 */
public class FagecClusteringAlgorithm implements
        ClusteringAlgorithmPlugin {

    // includeLoopsControl, degreeCutoffControl, haircutControl, fluffControl,
    // nodeScoreCutoffControl, fluffNodeDensityCutoffControl, KCoreControl, and
    // maxDepthControl are fields so that the getConfigurationControls and
    // clusterNetwork methods can share their text and checked fields
    public CheckBoxControl includeLoopsControl;
    public TextFieldControl degreeCutoffControl;
    public CheckBoxControl haircutControl;
    public CheckBoxControl fluffControl;
    public TextFieldControl nodeScoreCutoffControl;
    public TextFieldControl fluffNodeDensityCutoffControl;
    public TextFieldControl KCoreControl;
    public TextFieldControl maxDepthControl;

    public String getName() throws Exception {
        return "MCODE clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with the MCODE clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControl> controls = new ArrayList<PluginConfigurationControl>();

        includeLoopsControl = new CheckBoxControl();
        includeLoopsControl.label = "Include Loops:";
        includeLoopsControl.checked = false;
        controls.add(includeLoopsControl);

        degreeCutoffControl = new TextFieldControl();
        degreeCutoffControl.label = "Degree Cutoff:";
        degreeCutoffControl.text = "2";
        controls.add(degreeCutoffControl);

        haircutControl = new CheckBoxControl();
        haircutControl.label = "Haircut:";
        haircutControl.checked = true;
        controls.add(haircutControl);

        fluffControl = new CheckBoxControl();
        fluffControl.label = "Fluff:";
        fluffControl.checked = false;
        controls.add(fluffControl);

        fluffNodeDensityCutoffControl = new TextFieldControl();
        fluffNodeDensityCutoffControl.label = "Fluff Node Density Cutoff:";
        fluffNodeDensityCutoffControl.text = "0.1";
        controls.add(fluffNodeDensityCutoffControl);

        nodeScoreCutoffControl = new TextFieldControl();
        nodeScoreCutoffControl.label = "Node Score Cutoff:";
        nodeScoreCutoffControl.text = "0.2";
        controls.add(nodeScoreCutoffControl);

        KCoreControl = new TextFieldControl();
        KCoreControl.label = "K-Core:";
        KCoreControl.text = "2";
        controls.add(KCoreControl);

        maxDepthControl = new TextFieldControl();
        maxDepthControl.label = "Max. Depth:";
        maxDepthControl.text = "100";
        controls.add(maxDepthControl);

        return controls;

    }

    public void clusterNetwork() throws Exception {

        McodeUtil mcodeUtil = new McodeUtil();
        // setting the parameters must happen before the new McodeAlgorithm is
        // constructed in the line bellow this one because the McodeAlgorithm
        // constructor uses the parameters
        mcodeUtil.getCurrentParameters().setParams(
                new McodeParameterSet(
                NETWORK,
                new Long[0],
                includeLoopsControl.checked,
                Integer.parseInt(degreeCutoffControl.text),
                Integer.parseInt(KCoreControl.text),
                false,
                Integer.parseInt(maxDepthControl.text),
                Double.parseDouble(nodeScoreCutoffControl.text),
                fluffControl.checked,
                haircutControl.checked,
                Double.parseDouble(fluffNodeDensityCutoffControl.text)),
                mcodeUtil.getCurrentResultId(), (long) 0);
        McodeAlgorithm mcodeAlgorithm = new McodeAlgorithm((long) 0, mcodeUtil);
        McodeAnalyzeTask mcodeAnalyzeTask = new McodeAnalyzeTask(0, mcodeUtil.getCurrentResultId(), mcodeAlgorithm, mcodeUtil);
        mcodeAnalyzeTask.run();

    }
}
