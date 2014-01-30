package justclust.plugins.clustering.singlelinkage;

import java.util.ArrayList;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * This class contains a method which performs a single linkage clustering
 * algorithm.
 */
public class SingleLinkageClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {

    // clusterAmountTextFieldControl and minimumEdgeWeightTextFieldControl allow
    // the getConfigurationControls and clusterNetwork methods to share the text
    // fields of these TextFieldControls
    public TextFieldControl clusterAmountTextFieldControl;
    public TextFieldControl minimumEdgeWeightTextFieldControl;
    public ArrayList<DendrogramCluster> rootDendrogramClusters;

    /**
     * This method returns a display name for the clustering algorithm which a
     * method of this class performs.
     */
    public String getName() throws Exception {
        return "Single-linkage clustering algorithm";
    }

    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with a single-linkage clustering algorithm.";
    }

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControl> controls = new ArrayList<PluginConfigurationControl>();
        
        clusterAmountTextFieldControl = new TextFieldControl();
        clusterAmountTextFieldControl.label = "Number of Clusters:";
        clusterAmountTextFieldControl.text = "50";
        controls.add(clusterAmountTextFieldControl);

        minimumEdgeWeightTextFieldControl = new TextFieldControl();
        minimumEdgeWeightTextFieldControl.label = "Minimum Edge Weight for Combining Clusters:";
        minimumEdgeWeightTextFieldControl.text = "0";
        controls.add(minimumEdgeWeightTextFieldControl);

        return controls;

    }

    /**
     * This method performs a single linkage clustering algorithm.
     */
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        ArrayList<DendrogramCluster> dendrogramClusters = new ArrayList<DendrogramCluster>();

        // This code creates a Cluster for each of the Nodes in the
        // networkNodes data structure. These clusters will be merged
        // during the single linkage clustering algorithm.
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
        // At each stage in the single linkage clustering algorithm, the closest
        // two Clusters are merged.
        int i = 0;
        while (networkClusters.size() > Integer.parseInt(clusterAmountTextFieldControl.text)
                && edges.get(i).weight >= Integer.parseInt(minimumEdgeWeightTextFieldControl.text)) {
            if (edges.get(i).node1.cluster != edges.get(i).node2.cluster) {

                DendrogramCluster dendrogramCluster = new DendrogramCluster();
                dendrogramCluster.distance = edges.get(i).weight;
                dendrogramCluster.left =
                        dendrogramClusters.get(networkClusters.indexOf(edges.get(i).node1.cluster));;
                dendrogramCluster.right =
                        dendrogramClusters.get(networkClusters.indexOf(edges.get(i).node2.cluster));;
                dendrogramClusters.remove(networkClusters.indexOf(edges.get(i).node1.cluster));
                dendrogramClusters.add(networkClusters.indexOf(edges.get(i).node1.cluster), dendrogramCluster);
                dendrogramClusters.remove(networkClusters.indexOf(edges.get(i).node2.cluster));

                edges.get(i).node1.cluster.nodes.addAll(edges.get(i).node2.cluster.nodes);
                networkClusters.remove(edges.get(i).node2.cluster);
                for (Node node : edges.get(i).node2.cluster.nodes) {
                    node.cluster = edges.get(i).node1.cluster;
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
            if (networkEdgesCopy.get(i).node1.cluster != networkEdgesCopy.get(i).node2.cluster) {

                DendrogramCluster dendrogramCluster = new DendrogramCluster();
                dendrogramCluster.distance = networkEdgesCopy.get(i).weight;
                dendrogramCluster.left =
                        dendrogramClusters.get(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster));
                dendrogramCluster.right =
                        dendrogramClusters.get(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node2.cluster));
                dendrogramClusters.remove(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster));
                dendrogramClusters.add(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node1.cluster), dendrogramCluster);
                dendrogramClusters.remove(networkClustersCopy.indexOf(networkEdgesCopy.get(i).node2.cluster));

                networkEdgesCopy.get(i).node1.cluster.nodes.addAll(networkEdgesCopy.get(i).node2.cluster.nodes);
                networkClustersCopy.remove(networkEdgesCopy.get(i).node2.cluster);
                for (Node node : networkEdgesCopy.get(i).node2.cluster.nodes) {
                    node.cluster = networkEdgesCopy.get(i).node1.cluster;
                }

            }
            i++;
        }

        rootDendrogramClusters = dendrogramClusters;

    }

    public boolean hierarchicalClustering() {
        return true;
    }

    public ArrayList<DendrogramCluster> rootDendrogramClusters() {
        return rootDendrogramClusters;
    }
}
