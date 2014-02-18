package justclust.plugins.clustering.mcl;

import java.util.ArrayList;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This class contains a method which performs a complete linkage clustering
 * algorithm.
 */
public class MCLClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {

    public TextFieldControl maximumResidualTextFieldControl;
    public TextFieldControl pGammaTextFieldControl;
    public TextFieldControl loopGainTextFieldControl;
    public TextFieldControl maximumZeroTextFieldControl;

    /**
     * This method returns a display name for the clustering algorithm which a
     * method of this class performs.
     */
    public String getName() throws Exception {
        return "MCL clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with an MCL clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

        maximumResidualTextFieldControl = new TextFieldControl();
        maximumResidualTextFieldControl.label = "Maximum Difference Between Row Elements and Row Square Sum:";
        maximumResidualTextFieldControl.text = "0.001";
        controls.add(maximumResidualTextFieldControl);

        pGammaTextFieldControl = new TextFieldControl();
        pGammaTextFieldControl.label = "Inflation Exponent for Gamma Operator:";
        pGammaTextFieldControl.text = "2.0";
        controls.add(pGammaTextFieldControl);

        loopGainTextFieldControl = new TextFieldControl();
        loopGainTextFieldControl.label = "Loop Gain Values for Cycles:";
        loopGainTextFieldControl.text = "0";
        controls.add(loopGainTextFieldControl);

        maximumZeroTextFieldControl = new TextFieldControl();
        maximumZeroTextFieldControl.label = "Maximum Value Considered Zero for Pruning Operations:";
        maximumZeroTextFieldControl.text = "0.001";
        controls.add(maximumZeroTextFieldControl);

        return controls;

    }

    /**
     * This method performs a complete linkage clustering algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        MCL mcl = new MCL();
        ArrayList<Cluster> output = mcl.cluster(networkNodes);
        for (Cluster cluster : output) {
            networkClusters.add(cluster);
        }

    }

    public boolean isHierarchicalClustering() {
        return false;
    }

    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return null;
    }
}
