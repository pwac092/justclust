/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.mcode;

import java.util.ArrayList;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import static justclust.plugins.clustering.mcode.McodeParameterSet.NETWORK;
import justclust.plugins.configurationcontrols.CheckBoxControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 *
 * @author wuaz008
 */
public class McodeClusteringAlgorithm implements ClusteringAlgorithmPluginInterface {

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
    public static McodeClusteringAlgorithm classInstance;
    public ArrayList<Node> networkNodes;
    public ArrayList<Edge> networkEdges;
    public ArrayList<Cluster> networkClusters;

    public McodeClusteringAlgorithm() {
        classInstance = this;
    }

    public String getName() throws Exception {
        return "MCODE clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with the MCODE clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

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

    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        // these variables are made available here to the other classes in this
        // plug-in
        this.networkNodes = networkNodes;
        this.networkEdges = networkEdges;
        this.networkClusters = networkClusters;

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

    public boolean isHierarchicalClustering() {
        return false;
    }

    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return null;
    }
}
