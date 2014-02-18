/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.chinesewhispers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.toolbar.dendrogram.DendrogramCluster;

/**
 * Based on Biemann, 2006
 * (http://wortschatz.uni-leipzig.de/~cbiemann/pub/2006/BiemannTextGraph06.pdf)
 *
 * @author aeromero
 */
public class ChineseWhispersClustering implements
        ClusteringAlgorithmPluginInterface {
    
    public TextFieldControl maximumIterationsTextFieldControl;
    List<Integer> l;
    
    public ChineseWhispersClustering() {
    }
    
    @Override
    public String getName() throws Exception {
        return "Chinese whisperers clustering algorithm";
    }
    
    @Override
    public String getDescription() throws Exception {
        return "Based on the algorithm by Biemann \"Chinese Whispers - an Efficient Graph Clustering Algorithm \n"
                + "and its Application to Natural Language Processing Problems\"";
    }
    
    @Override
    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {
        
        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();
        
        maximumIterationsTextFieldControl = new TextFieldControl();
        maximumIterationsTextFieldControl.label = "Maximum Number of Iterations:";
        maximumIterationsTextFieldControl.text = "1000";
        controls.add(maximumIterationsTextFieldControl);

        return controls;
    }
    
    private List<Integer> randomPermutationOfNodeIndices(int n) {
        List<Integer> ll = new ArrayList<Integer>(l);
        java.util.Collections.shuffle(ll);
        return ll;
    }
    
    private Map<Node, Double> getNeighbours(Node n) {
        Map<Node, Double> neighbors = new HashMap<Node, Double>();
        for (Edge e : n.edges) {
            if (e.node1 == e.node2) {
                // ignore self-links
                continue;
            }
            double weight = e.weight;
            if (e.node1 != n) {
                neighbors.put(e.node1, weight);
            } else {
                neighbors.put(e.node2, weight);
            }
        }
        return neighbors;
    }
    
    @Override
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {
        int m = networkNodes.size();
        l = new ArrayList<Integer>();
        for (int i = 0; i < m; ++i) {
            l.add(i);
        }

        // 1.- initialize each node to its own class
        Map<Node, Integer> nodeClass = new HashMap<Node, Integer>();
        int i = 0;
        for (Node n : networkNodes) {
            nodeClass.put(n, i);
            i++;
        }

        // 2.- repeat while changes
        boolean changes;
        int numNodes = networkNodes.size();
        final int NUM_ITERATIONS = Integer.parseInt(maximumIterationsTextFieldControl.text); // this should be enough...
        i = 0;
        do {
            changes = false;
            int num_changes = 0;
            for (int j : this.randomPermutationOfNodeIndices(numNodes)) {
                Node node_j = networkNodes.get(j);
                
                Map<Integer, Double> weightPerClass = new HashMap<Integer, Double>();
                int num_neighbours = 0;
                for (Map.Entry<Node, Double> e : this.getNeighbours(node_j).entrySet()) {
                    Node neighbour_k = e.getKey();
                    double weight_node_k = e.getValue();
                    int class_for_neighbour_k = nodeClass.get(neighbour_k);
                    
                    if (!weightPerClass.containsKey(class_for_neighbour_k)) {
                        weightPerClass.put(class_for_neighbour_k, weight_node_k);
                    } else {
                        weightPerClass.put(class_for_neighbour_k, weight_node_k + weightPerClass.get(class_for_neighbour_k));
                    }
                    num_neighbours++;
                }
                
                if (num_neighbours > 0) {
                    int newLabel = getMax(weightPerClass);
                    int oldLabel = nodeClass.get(node_j);
                                        
                    if (newLabel != oldLabel) {
//                        System.out.println("   -> Node " + node_j.label + ", new class " + newLabel + ", old class " + nodeClass.get(node_j));
                        changes = true;
                        nodeClass.put(node_j, newLabel);
                        num_changes++;
                    }
                }                 
            }
            
//            System.out.println("Iteration " + i + ", num. of clusters=" + (new HashSet<Integer>(nodeClass.values())).size() + ", num. changes=" + num_changes);
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            i++;
        } while (changes && i < NUM_ITERATIONS);

        // 3.- build clusters with respect to the assigned node class
        Map<Integer, Set<Node>> clusterIdToNodes = new HashMap<Integer, Set<Node>>();
        for (Map.Entry<Node, Integer> e : nodeClass.entrySet()) {
            int label = e.getValue();
            Node id = e.getKey();
            if (!clusterIdToNodes.containsKey(label)) {
                clusterIdToNodes.put(label, new HashSet<Node>());
            }
            clusterIdToNodes.get(label).add(id);
        }
        
        for (Map.Entry<Integer, Set<Node>> e : clusterIdToNodes.entrySet()) {
            Cluster cluster = new Cluster();
            Set<Node> nodesOfTheCluster = e.getValue();
            cluster.nodes = new ArrayList<Node>(nodesOfTheCluster);
            
            for (Node element : nodesOfTheCluster) {
                element.cluster = cluster;
            }
            networkClusters.add(cluster);
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
    
    private int getMax(Map<Integer, Double> weightPerClass) {
        double maximum = Double.NEGATIVE_INFINITY;
        int id = -1;
        for (Map.Entry<Integer, Double> e : weightPerClass.entrySet()) {
            if (e.getValue() > maximum) {
                id = e.getKey();
                maximum = e.getValue();
            }
        }
        return id;
    }
}
