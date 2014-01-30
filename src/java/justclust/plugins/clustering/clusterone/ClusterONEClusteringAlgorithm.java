package justclust.plugins.clustering.clusterone;

import java.util.ArrayList;

import uk.ac.rhul.cs.cl1.ClusterONE;
import uk.ac.rhul.cs.cl1.ClusterONEAlgorithmParameters;
import uk.ac.rhul.cs.cl1.ValuedNodeSet;
import uk.ac.rhul.cs.graph.Graph;
import uk.ac.rhul.cs.utils.UniqueIDGenerator;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This class contains a method which performs a ClusterONE clustering
 * algorithm.
 */
public class ClusterONEClusteringAlgorithm implements ClusteringAlgorithmPluginInterface {

    /**
     * This method returns a display name for the clustering algorithm which a
     * method of this class performs.
     */
    public String getName() throws Exception {
        return "ClusterONE clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with the ClusterONE clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception {
        return new ArrayList<PluginConfigurationControl>();
    }

    /**
     * This method performs a ClusterONE clustering algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        Graph graph = new Graph();

        // this code populates the Graph contained in the graph variable by
        // adding edges between nodes
        UniqueIDGenerator<String> nodeGen = new UniqueIDGenerator<String>(graph);
        for (Edge edge : networkEdges) {
            int node1 = nodeGen.get(edge.node1.label);
            int node2 = nodeGen.get(edge.node2.label);
            graph.createEdge(node1, node2, edge.weight);
        }

        // this code performs the ClusterONE clustering algorithm on the Graph
        // contained in the graph variable
        ClusterONEAlgorithmParameters params = new ClusterONEAlgorithmParameters();
        ClusterONE algorithm = new ClusterONE(params);
        algorithm.runOnGraph(graph);

        // this code populates the networkClusters field with results from the
        // ClusterONE clustering algorithm
        for (ValuedNodeSet valuedNodeSet : algorithm.getResults()) {
            Cluster cluster = new Cluster();
            networkClusters.add(cluster);
            cluster.nodes = new ArrayList<Node>();
            for (String memberName : valuedNodeSet.getMemberNames()) {
                for (Node node : networkNodes) {
                    if (node.label.equals(memberName)) {
                        cluster.nodes.add(node);
                    }
                }
            }
        }

    }

    public boolean hierarchicalClustering() {
        return false;
    }

    public ArrayList<DendrogramCluster> rootDendrogramClusters() {
        return null;
    }
}
