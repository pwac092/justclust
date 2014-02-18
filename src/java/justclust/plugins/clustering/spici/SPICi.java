/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.spici;

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
 *
 * @author aeromero
 */
public class SPICi implements
        ClusteringAlgorithmPluginInterface {

    double maxWeight;

    double buckets[] = {0.0, 0.2, 0.4, 0.6, 0.8, 1.0};

    Map<Node, Double> weightedDegreePerNode;
    Set<Node> alreadyUsed;
    private TextFieldControl densityThreshold;
    private TextFieldControl supportThreshold;
    double Ts, Td;

    public SPICi() {
        maxWeight = 0;
        weightedDegreePerNode = new HashMap<Node, Double>();
        alreadyUsed = new HashSet<Node>();
    }

    @Override
    public String getName() throws Exception {
        return "SPICi";
    }

    @Override
    public String getDescription() throws Exception {
        return "Peng Jiang and Mona Singh, SPICi: a fast clustering algorithm for large biological networks, Bioinformatics, Vol. 26 no. 8 2010";
    }

    @Override
    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {
        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();
        densityThreshold = new TextFieldControl();
        densityThreshold.label = "Density threshold Td:";
        densityThreshold.text = "0.5";
        controls.add(densityThreshold);

        supportThreshold = new TextFieldControl();
        supportThreshold.label = "Support threshold Ts:";
        supportThreshold.text = "0.5";
        controls.add(supportThreshold);

        return controls;
    }

    private Set<Node> expandCluster(Node u, Node v) {
        Map<Node, Edge> neighbors_u = this.neighbors(u);
        Map<Node, Edge> neighbors_v = this.neighbors(v);

        // CandidateQ is the union of the neighbors of both u and v
        Set<Node> CandidateQ = new HashSet<Node>(neighbors_u.keySet());
        CandidateQ.addAll(neighbors_v.keySet());
        // ... which have not been used before
        CandidateQ.removeAll(this.alreadyUsed);

        // S is the produced cluster (at least will contain u and v)
        Set<Node> S = new HashSet<Node>();
        S.add(u);
        S.add(v);

        // we compute the support of the nodes 
        Map<Node, Double> supportS = new HashMap<Node, Double>();
        for (Node n : CandidateQ) {
            supportS.put(n, this.support(n, S));
        }

        boolean terminate = false;

        while (!CandidateQ.isEmpty() && !terminate) {
            Pair<Node, Double> largest = this.extractLargestElement(supportS);

            double sup = largest.getSecond();
            Node t = largest.getFirst();

            Set<Node> S_and_t = new HashSet<Node>(S);
            S_and_t.add(t);

            if (sup >= Ts * ((double) S.size()) * this.density(S)
                    && this.density(S_and_t) > Td) {
                S = S_and_t;

                for (Node n_t : this.neighbors(t).keySet()) {
                    if (!this.alreadyUsed.contains(n_t) && !S.contains(n_t)) {
                        supportS.put(n_t, this.support(n_t, S));
                        CandidateQ.add(n_t);
                    }
                }

            } else {
                terminate = true;
            }
            supportS.remove(t);
        }
        return S;
    }

    private int getBucketId(double val) {
        for (int i = 0; i < 5; ++i) {
            if (val > this.buckets[i] && val <= this.buckets[i + 1]) {
                return i;
            }
        }
        return 4;
    }

    private Node getBestNeighbor(Node u, Map<Node, Edge> neighbors) {
        Map<Integer, Set<Node>> nodesPerBucket = new HashMap<Integer, Set<Node>>();
        for (int i = 0; i < 5; i++) {
            nodesPerBucket.put(i, new HashSet<Node>());
        }

        for (Node neighbor : neighbors.keySet()) {
            double wnorm = neighbors.get(neighbor).weight / this.maxWeight;
            int bucket_id = this.getBucketId(wnorm);
            nodesPerBucket.get(bucket_id).add(neighbor);
        }

        int bucket = 4;
        boolean found = false;
        Node v = null;
        while (!found && bucket >= 0) {
            Set<Node> nodes = nodesPerBucket.get(bucket);
            if (!nodes.isEmpty()) {
                found = true;
                Pair<Node, Double> p = this.extractLargestElementPresentInSet(this.weightedDegreePerNode, nodes);
                v = p.getFirst();
            } else {
                bucket--;
            }
        }
        return v;
    }

    @Override
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {
        Map<Node, Double> DegreeQ = initializeQueue(networkNodes);
        this.Ts = Double.parseDouble(this.supportThreshold.text);
        this.Td = Double.parseDouble(this.densityThreshold.text);

        for (Edge e : networkEdges) {
            this.maxWeight = Math.max(this.maxWeight, e.weight);
        }

        while (!DegreeQ.isEmpty()) {
            Pair<Node, Double> p = extractLargestElement(DegreeQ);

            Node u = p.getFirst();
            Map<Node, Edge> neighbors = this.neighbors(u);
            for (Node used : alreadyUsed) {
                if (neighbors.containsKey(used)) {
                    neighbors.remove(used);
                }
            }

            Set<Node> S;
            if (!neighbors.isEmpty()) {
                Node v = getBestNeighbor(u, neighbors);
                S = this.expandCluster(u, v);
            } else {
                S = new HashSet<Node>();
                S.add(u);
            }

            // update queue and S
            for (Node n : S) {
                DegreeQ.remove(n);
            }
            alreadyUsed.addAll(S);

            Map<Node, Double> DegreeQ2 = new HashMap<Node, Double>();
            for (Node n : DegreeQ.keySet()) {
                Set<Node> intersection = new HashSet<Node>(this.neighbors(n).keySet());
                intersection.retainAll(S);
                intersection.removeAll(this.alreadyUsed);
                if (!intersection.isEmpty()) {
                    DegreeQ2.put(n, this.weighted_degree(n) - this.support(n, S));
                }
            }
            DegreeQ.putAll(DegreeQ2);

            // create a new cluster based on S
            Cluster cluster = new Cluster();
            cluster.nodes = new ArrayList<Node>(S);

            for (Node element : S) {
                element.cluster = cluster;
            }
            networkClusters.add(cluster);
            // end of iteration!
        }

    }

    private Map<Node, Edge> neighbors(Node n) {
        Map<Node, Edge> neighbors = new HashMap<Node, Edge>();
        for (Edge e : n.edges) {
            if (e.node1 == e.node2) {
                continue;
            }
            if (e.node1 != n) {
                neighbors.put(e.node1, e);
            } else {
                neighbors.put(e.node2, e);
            }
        }
        return neighbors;
    }

    private double support(Node n, Set<Node> subgraph) {
        double support = 0.0;
        for (Map.Entry<Node, Edge> neighs : this.neighbors(n).entrySet()) {
            Node neighbor = neighs.getKey();
            Edge edge = neighs.getValue();
            if (subgraph.contains(neighbor)) {
                support += edge.weight;
            }
        }
        return support;
    }

    private double weighted_degree(Node n) {
        double deg = 0.0;
        for (Edge e : n.edges) {
            if (e.node1 != e.node2) {
                deg += e.weight;
            }
        }
        return deg;
    }

    /**
     * Computes the density of a given subgraph (consider only the internal
     * edges)
     */
    private double density(Set<Node> subgraph) {
        double density = 0.0;
        int size = subgraph.size();
        Set<Edge> visitedEdges = new HashSet<Edge>();
        for (Node n_i : subgraph) {
            for (Map.Entry<Node, Edge> neighbor : this.neighbors(n_i).entrySet()) {
                Edge e = neighbor.getValue();
                Node nei = neighbor.getKey();
                if (subgraph.contains(nei) && !visitedEdges.contains(e)) {
                    density += e.weight;
                    visitedEdges.add(e);
                }
            }
        }
        double denominator = (double) (size * (size - 1)) * 0.5;
        return density / denominator;
    }

    @Override
    public boolean isHierarchicalClustering() {
        return false;
    }

    @Override
    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return null;
    }

    private Map<Node, Double> initializeQueue(List<Node> nodes) {
        Map<Node, Double> q = new HashMap<Node, Double>();
        for (Node n : nodes) {
            q.put(n, this.weighted_degree(n));
        }
        this.weightedDegreePerNode.putAll(q);
        return q;
    }

    private Pair<Node, Double> extractLargestElementPresentInSet(Map<Node, Double> DegreeQ, Set<Node> s) {
        Pair<Node, Double> p = new Pair<Node, Double>(null, Double.NEGATIVE_INFINITY);
        for (Map.Entry<Node, Double> e : DegreeQ.entrySet()) {
            if (p.getSecond() < e.getValue() && s.contains(e.getKey())) {
                p.setSecond(e.getValue());
                p.setFirst(e.getKey());
            }
        }
        if (p.getFirst() != null) {
            DegreeQ.remove(p.getFirst());
        }
        return p;
    }

    private Pair<Node, Double> extractLargestElement(Map<Node, Double> DegreeQ) {
        Pair<Node, Double> p = new Pair<Node, Double>(null, Double.NEGATIVE_INFINITY);
        for (Map.Entry<Node, Double> e : DegreeQ.entrySet()) {
            if (p.getSecond() < e.getValue()) {
                p.setSecond(e.getValue());
                p.setFirst(e.getKey());
            }
        }
        DegreeQ.remove(p.getFirst());
        return p;
    }

}
