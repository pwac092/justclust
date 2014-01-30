package justclust.plugins.clustering;

import java.util.ArrayList;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This interface contains a method signature which defines a method which
 * performs a clustering algorithm.
 */
public interface ClusteringAlgorithmPluginInterface {

    /**
     * This method signature defines a method which returns a display name for
     * the clustering algorithm which the method defined by a method signature
     * of this interface performs.
     */
    public String getName() throws Exception;

    public String getDescription() throws Exception;

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception;

    /**
     * This method signature defines a method which performs a clustering
     * algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception;

    public boolean hierarchicalClustering();
    
    public ArrayList<DendrogramCluster> rootDendrogramClusters();
}
