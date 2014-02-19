package justclust.plugins.clustering.mcl;

import java.util.ArrayList;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.DoubleFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This class contains a method which performs a complete linkage clustering
 * algorithm.
 */
public class MCLClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {

    public DoubleFieldControl maximumResidualDoubleFieldControl;
    public DoubleFieldControl pGammaDoubleFieldControl;
    public DoubleFieldControl loopGainDoubleFieldControl;
    public DoubleFieldControl maximumZeroDoubleFieldControl;

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

        maximumResidualDoubleFieldControl = new DoubleFieldControl();
        maximumResidualDoubleFieldControl.label = "Maximum Difference Between Row Elements and Row Square Sum:";
        maximumResidualDoubleFieldControl.value = 0.001;
        controls.add(maximumResidualDoubleFieldControl);

        pGammaDoubleFieldControl = new DoubleFieldControl();
        pGammaDoubleFieldControl.label = "Inflation Exponent for Gamma Operator:";
        pGammaDoubleFieldControl.value = 2.0;
        controls.add(pGammaDoubleFieldControl);

        loopGainDoubleFieldControl = new DoubleFieldControl();
        loopGainDoubleFieldControl.label = "Loop Gain Values for Cycles:";
        loopGainDoubleFieldControl.value = 0;
        controls.add(loopGainDoubleFieldControl);

        maximumZeroDoubleFieldControl = new DoubleFieldControl();
        maximumZeroDoubleFieldControl.label = "Maximum Value Considered Zero for Pruning Operations:";
        maximumZeroDoubleFieldControl.value = 0.001;
        controls.add(maximumZeroDoubleFieldControl);

        return controls;

    }

    /**
     * This method performs a complete linkage clustering algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        MCL mcl = new MCL(maximumResidualDoubleFieldControl.value,
                pGammaDoubleFieldControl.value,
                loopGainDoubleFieldControl.value,
                maximumZeroDoubleFieldControl.value);
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
