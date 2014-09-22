package justclust.plugins.clustering.completelinkage;

import java.util.ArrayList;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.DoubleFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.IntegerFieldControl;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This class contains a method which performs a complete linkage clustering
 * algorithm.
 */
public class CompleteLinkageClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {

    public IntegerFieldControl clusterAmountIntegerFieldControl;
    public DoubleFieldControl minimumEdgeWeightIntegerFieldControl;
    public ArrayList<DendrogramCluster> rootDendrogramClusters;

    /**
     * This method returns a display name for the clustering algorithm which a
     * method of this class performs.
     */
    public String getName() throws Exception {
        return "Complete-linkage clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with a complete-linkage clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();
        
        clusterAmountIntegerFieldControl = new IntegerFieldControl();
        clusterAmountIntegerFieldControl.label = "Number of Clusters:";
        clusterAmountIntegerFieldControl.value = 50;
        controls.add(clusterAmountIntegerFieldControl);

        minimumEdgeWeightIntegerFieldControl = new DoubleFieldControl();
        minimumEdgeWeightIntegerFieldControl.label = "Minimum Edge Weight for Combining Clusters:";
        minimumEdgeWeightIntegerFieldControl.value = 0;
        controls.add(minimumEdgeWeightIntegerFieldControl);

        return controls;

    }

    /**
     * This method performs a complete linkage clustering algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        ArrayList<DendrogramCluster> dendrogramClusters = new ArrayList<DendrogramCluster>();

        // This code creates a Cluster for each of the Nodes in the
        // networkNodes data structure. These clusters will be merged
        // during the complete linkage clustering algorithm.
        for (Node node : networkNodes) {

            Cluster cluster = new Cluster();
            networkClusters.add(cluster);
            cluster.nodes = new ArrayList<Node>();
            cluster.nodes.add(node);
            node.cluster = cluster;

            DendrogramCluster dendrogramCluster = new DendrogramCluster();
            dendrogramCluster.distance = 0;
            dendrogramCluster.left = null;
            dendrogramCluster.right = null;
            dendrogramCluster.nodeIndex = networkNodes.indexOf(node);
            dendrogramClusters.add(dendrogramCluster);

        }

        // This code creates a data structure of Edges called edges which is a
        // shallow copy of the networkEdges data structure. The Edges in
        // the edges data structure are sorted from largest weight field to
        // smallest weight field.
        ArrayList<Edge> edges = (ArrayList<Edge>) networkEdges.clone();
        for (int i = 1; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            int j;
            for (j = i - 1; j >= 0
                    && edge.weight > edges.get(j).weight; j--) {
                edges.set(j + 1, edges.get(j));
            }
            edges.set(j + 1, edge);
        }

        // This code removes Edges from the edges data structure which link a
        // Node to itself.
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).node1 == edges.get(i).node2) {
                edges.remove(i);
                i--;
            }
        }

        // This code removes Edges from the edges data structure which link
        // Nodes which are already linked by another Edge in the edges data
        // structure.
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                if (edges.get(i).node1 == edges.get(j).node1
                        && edges.get(i).node2 == edges.get(j).node2
                        || edges.get(i).node1 == edges.get(j).node2
                        && edges.get(i).node2 == edges.get(j).node1) {
                    edges.remove(j);
                    j--;
                }
            }
        }

        // This code clusters the Nodes in the networkNodes data structure.
        // At each stage in the complete linkage clustering algorithm, the
        // closest two Clusters are merged and, for every Edge between the
        // single Cluster they form and each other Cluster, all Edges between
        // the two Clusters are removed except for the Edge with the smallest
        // weight field.
        int i = 0;
        while (networkClusters.size() > clusterAmountIntegerFieldControl.value
                && edges.get(i).weight >= minimumEdgeWeightIntegerFieldControl.value) {

            DendrogramCluster dendrogramCluster = new DendrogramCluster();
            dendrogramCluster.distance = edges.get(i).weight;
            dendrogramCluster.left =
                    dendrogramClusters.get(networkClusters.indexOf(edges.get(i).node1.cluster));
            dendrogramCluster.right =
                    dendrogramClusters.get(networkClusters.indexOf(edges.get(i).node2.cluster));
            dendrogramClusters.remove(networkClusters.indexOf(edges.get(i).node1.cluster));
            dendrogramClusters.add(networkClusters.indexOf(edges.get(i).node1.cluster), dendrogramCluster);
            dendrogramClusters.remove(networkClusters.indexOf(edges.get(i).node2.cluster));

            edges.get(i).node1.cluster.nodes
                    .addAll(edges.get(i).node2.cluster.nodes);
            networkClusters
                    .remove(edges.get(i).node2.cluster);
            for (Node node : edges.get(i).node2.cluster.nodes) {
                node.cluster = edges.get(i).node1.cluster;
            }
            for (int j = i + 1; j < edges.size(); j++) {
                if (edges.get(j).node1.cluster == edges
                        .get(i).node1.cluster
                        || edges.get(j).node2.cluster == edges
                        .get(i).node1.cluster) {
                    for (int k = j + 1; k < edges.size(); k++) {
                        if (edges.get(j).node1.cluster == edges
                                .get(k).node1.cluster
                                && edges.get(j).node2.cluster == edges
                                .get(k).node2.cluster
                                || edges.get(j).node1.cluster == edges
                                .get(k).node2.cluster
                                && edges.get(j).node2.cluster == edges
                                .get(k).node1.cluster) {
                            if (edges.get(j).weight <= edges.get(k).weight) {
                                edges.remove(k);
                            } else {
                                edges.remove(j);
                                j--;
                            }
                            break;
                        }
                    }
                }
            }
            i++;
        }
        
        ArrayList<Node> networkNodesCopy = new ArrayList<Node>();
        for (Node node : networkNodes) {
            Node nodeCopy = new Node();
            networkNodesCopy.add(nodeCopy);
        }
        ArrayList<Edge> networkEdgesCopy = new ArrayList<Edge>();
        for (Edge edge : edges) {
            Edge edgeCopy = new Edge();
            edgeCopy.node1 = networkNodesCopy.get(networkNodes.indexOf(edge.node1));
            edgeCopy.node2 = networkNodesCopy.get(networkNodes.indexOf(edge.node2));
            edgeCopy.weight = edge.weight;
            networkEdgesCopy.add(edgeCopy);
        }
        ArrayList<Cluster> networkClustersCopy = new ArrayList<Cluster>();
        for (Cluster cluster : networkClusters) {
            Cluster clusterCopy = new Cluster();
            clusterCopy.nodes = new ArrayList<Node>();
            for (Node node : cluster.nodes) {
                Node nodeCopy = networkNodesCopy.get(networkNodes.indexOf(node));
                nodeCopy.cluster = clusterCopy;
                clusterCopy.nodes.add(nodeCopy);
            }
            networkClustersCopy.add(clusterCopy);
        }
        
        while (networkClustersCopy.size() > 1 && i < networkEdgesCopy.size()) {

            DendrogramCluster dendrogramCluster = new DendrogramCluster();
            dendrogramCluster.distance = networkEdgesCopy.get(i).weight;
            dendrogramCluster.left =
                    dendrogramClusters.get(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster));
            dendrogramCluster.right =
                    dendrogramClusters.get(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node2.cluster));
            dendrogramClusters.remove(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster));
            dendrogramClusters.add(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster), dendrogramCluster);
            dendrogramClusters.remove(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node2.cluster));

            networkEdgesCopy.get(i).node1.cluster.nodes
                    .addAll(networkEdgesCopy.get(i).node2.cluster.nodes);
            networkClustersCopy
                    .remove(networkEdgesCopy.get(i).node2.cluster);
            for (Node node : networkEdgesCopy.get(i).node2.cluster.nodes) {
                node.cluster = networkEdgesCopy.get(i).node1.cluster;
            }
            for (int j = i + 1; j < networkEdgesCopy.size(); j++) {
                if (networkEdgesCopy.get(j).node1.cluster == networkEdgesCopy
                        .get(i).node1.cluster
                        || networkEdgesCopy.get(j).node2.cluster == networkEdgesCopy
                        .get(i).node1.cluster) {
                    for (int k = j + 1; k < networkEdgesCopy.size(); k++) {
                        if (networkEdgesCopy.get(j).node1.cluster == networkEdgesCopy
                                .get(k).node1.cluster
                                && networkEdgesCopy.get(j).node2.cluster == networkEdgesCopy
                                .get(k).node2.cluster
                                || networkEdgesCopy.get(j).node1.cluster == networkEdgesCopy
                                .get(k).node2.cluster
                                && networkEdgesCopy.get(j).node2.cluster == networkEdgesCopy
                                .get(k).node1.cluster) {
                            if (networkEdgesCopy.get(j).weight <= networkEdgesCopy.get(k).weight) {
                                networkEdgesCopy.remove(k);
                            } else {
                                networkEdgesCopy.remove(j);
                                j--;
                            }
                            break;
                        }
                    }
                }
            }
            i++;
        }

        rootDendrogramClusters = dendrogramClusters;

    }

    public boolean isHierarchicalClustering() {
        return true;
    }

    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return rootDendrogramClusters;
    }
}
