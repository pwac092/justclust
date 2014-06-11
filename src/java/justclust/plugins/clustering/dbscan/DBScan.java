/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.dbscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
public class DBScan implements
        ClusteringAlgorithmPluginInterface {

    /**
     * This is the value used to compute the neighborhood
     */
    private DoubleFieldControl epsilon;
    /**
     * Minimum number of elements for a cluster to be considered
     */
    private IntegerFieldControl min_size;

    public DBScan() {
    }

    @Override
    public String getName() throws Exception {
        return "DBScan clustering algorithm";
    }

    @Override
    public String getDescription() throws Exception {
        return "A data clustering algorithm proposed by Martin Ester, Hans-Peter Kriegel, Jörg Sander and Xiaowei Xu in 1996";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

        this.epsilon = new DoubleFieldControl();
        this.epsilon.label = "Minimum similarity to consider a point a neighbour of another given:";
        this.epsilon.value = 0.1;
        controls.add(this.epsilon);

        this.min_size = new IntegerFieldControl();
        this.min_size.label = "Minimum number of points required to form a cluster:";
        this.min_size.value = 2;
        controls.add(this.min_size);

        return controls;

    }

    /**
     * Given a certain node "center" and a minimum similarity "epsilon", returns
     * the set of nodes which have at least that similarity with the given one.
     *
     */
    private List<Node> regionQuery(double epsilon, Node center) {
        List<Node> neighbourNodes = new ArrayList<Node>();
        for (Edge incidentEdge : center.edges) {
            if (incidentEdge.edgeSharedAttributes.weight >= epsilon) {
                Node neighbour;
                if (incidentEdge.node1 != center) {
                    neighbour = incidentEdge.node1;
                } else {
                    neighbour = incidentEdge.node2;
                }
                neighbourNodes.add(neighbour);
            }
        }
        return neighbourNodes;
    }

    @Override
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {
        double eps = this.epsilon.value;
        int minPoints = this.min_size.value;

        Map<Node, Boolean> isInACluster = new HashMap<Node, Boolean>();
        Map<Node, Boolean> visited = new HashMap<Node, Boolean>();
        // 1.- initialize all points as unvisited
        // and not in any clusters
        for (Node n : networkNodes) {
            visited.put(n, Boolean.FALSE);
            isInACluster.put(n, Boolean.FALSE);
        }

        // 2.- for each unvisited node...
        for (Node n : networkNodes) {
            if (visited.get(n)) {
                continue;
            }
            visited.put(n, Boolean.TRUE);
            // we get its epsilon-neighbours
            List<Node> neighbours = this.regionQuery(eps, n);
            if (neighbours.size() >= minPoints) {
                // if there are very few neighbours, the point is noise
                // otherwise the point is not noise and it is a cluster
                Cluster cluster = this.expandCluster(n, neighbours, eps, minPoints, visited, isInACluster);
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

    private Cluster expandCluster(Node n, List<Node> neighbours, double epsilon, int minPoints, Map<Node, Boolean> visited, Map<Node, Boolean> isInACluster) {

        Cluster cluster = new Cluster();
        cluster.nodes = new ArrayList<Node>();

        // 1.- we add the proper seed node to the cluster
        cluster.nodes.add(n);
        n.cluster = cluster;

        Queue<Node> toVisit = new LinkedList<Node>(neighbours);

        // 2.- for each neighbor, if it is not visited..
        while (!toVisit.isEmpty()) {
            Node neighbour = toVisit.poll();
            if (!visited.get(neighbour)) {
                // we "visit" it
                visited.put(neighbour, Boolean.TRUE);

            }
            if (!isInACluster.get(neighbour)) {
                // we add it to a cluster if it was not added before
                cluster.nodes.add(neighbour);
                isInACluster.put(neighbour, Boolean.TRUE);

                List<Node> neighboursPrime = this.regionQuery(epsilon, neighbour);

                if (neighboursPrime.size() >= minPoints) {
                    toVisit.addAll(neighboursPrime);
                }
            }
        }

        return cluster;
    }
}
