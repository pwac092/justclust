/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.connectedcomponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.DoubleFieldControl;
import justclust.plugins.configurationcontrols.IntegerFieldControl;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 *
 * @author aeromero
 */
public class ConnectedComponentsClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {

    /**
     * Minimum similarity considered
     */
    private DoubleFieldControl minSimilarity;
    /**
     * Minimum number of elements for a cluster to be considered
     */
    private IntegerFieldControl minNumElements;

    @Override
    public String getName() throws Exception {
        return "Connected components clustering";
    }

    @Override
    public String getDescription() throws Exception {
        return "Only edges with at least having at least a certain simiarity are kept, and the connected components are computed for the resulting graph. If a connected component has very few elements (user-configurable) it is not considered";
    }

    @Override
    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

        this.minSimilarity = new DoubleFieldControl();
        this.minSimilarity.label = "Minimum similarity to consider and edge for computing connected components";
        this.minSimilarity.value = 0;
        controls.add(this.minSimilarity);

        this.minNumElements = new IntegerFieldControl();
        this.minNumElements.label = "Minimum number of points required to form a cluster:";
        this.minNumElements.value = 2;
        controls.add(this.minNumElements);

        return controls;
    }

    /**
     *
     * Given a certain node "center" and a minimum similarity "epsilon", returns
     * the set of nodes which have at least that similarity with the given one.
     *
     */
    private List<Node> connectedComponent(Node seed, double minSimilarity, Map<Node, Boolean> visited) {
        List<Node> neighbourNodes = new ArrayList<Node>();
        Set<Node> added = new HashSet<Node>();

        Queue<Node> toProcess = new LinkedList<Node>();
        toProcess.add(seed);
        added.add(seed);

        while (!toProcess.isEmpty()) {
            Node center = toProcess.poll();
            for (Edge incidentEdge : center.edges) {
                if (incidentEdge.weight >= minSimilarity) {
                    Node neighbour;
                    if (incidentEdge.node1 != center) {
                        neighbour = incidentEdge.node1;
                    } else {
                        neighbour = incidentEdge.node2;
                    }
                    if (!added.contains(neighbour)) {
                        neighbourNodes.add(neighbour);
                        added.add(neighbour);
                        toProcess.add(neighbour);
                    }
                }
            }
            visited.put(center, Boolean.TRUE);
        }
        return neighbourNodes;
    }

    @Override
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {
        double epsilon = this.minSimilarity.value;
        int minPoints = this.minNumElements.value;

        Map<Node, Boolean> visited = new HashMap<Node, Boolean>();
        // 1.- initialize all points as unvisited
        // and not in any clusters
        for (Node n : networkNodes) {
            visited.put(n, Boolean.FALSE);
        }

        for (Node n : networkNodes) {
            
            if (visited.get(n)) {
                continue;
            }
            visited.put(n, Boolean.TRUE);

            List<Node> component = this.connectedComponent(n, epsilon, visited);

            if (component.size() >= minPoints) {
                // we create a cluster
                Cluster cluster = new Cluster();
                cluster.nodes = new ArrayList<Node>(component);
                for (Node com : component) {
                    com.cluster = cluster;
                }
                networkClusters.add(cluster);
            }
        }

    }

    @Override
    public boolean isHierarchicalClustering() {
        return false;
    }

    @Override
    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return null;
    }

}