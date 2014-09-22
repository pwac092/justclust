/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.affinitypropagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
 * This is basically a tranlation of
 * https://github.com/nojima/affinity-propagation-sparse/blob/master/ap.cpp
 *
 * @author aeromero
 */
public class AffinityPropagationClusteringAlgorithm implements ClusteringAlgorithmPluginInterface {

    public IntegerFieldControl maxitIntegerFieldControl;
    public IntegerFieldControl convitIntegerFieldControl;
    public IntegerFieldControl prefTypeIntegerFieldControl;
    public DoubleFieldControl dampingDoubleFieldControl;
    /**
     * number of nodes
     */
    private int n;
    /**
     * number of edges
     */
    private int m;
    /**
     * Similarity for each edge
     */
    private Map<Edge, Double> similarity;
    /**
     * Responsibility for each edge
     */
    private Map<Edge, Double> responsibility;
    /**
     * Availability for each edge
     */
    private Map<Edge, Double> availability;
    /**
     * Exemplar for each node (cluster representative)
     */
    private Map<Node, Node> exemplar;
    /**
     * Incident edges for a certain node
     */
    private Map<Node, Set<Edge>> inedges;
    /**
     * Outgoing edges for a certain node
     */
    private Map<Node, Set<Edge>> outedges;

    @Override
    public String getName() throws Exception {
        return "Affinity propagation clustering algorithm";
    }

    @Override
    public String getDescription() throws Exception {
        return "Based on Frey's and Dueck's Affinity Propagation clustering method (Science, 2007).";
    }

    @Override
    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

        maxitIntegerFieldControl = new IntegerFieldControl();
        maxitIntegerFieldControl.label = "Maxit:";
        maxitIntegerFieldControl.value = 1000;
        controls.add(maxitIntegerFieldControl);

        convitIntegerFieldControl = new IntegerFieldControl();
        convitIntegerFieldControl.label = "Convit:";
        convitIntegerFieldControl.value = 50;
        controls.add(convitIntegerFieldControl);

        prefTypeIntegerFieldControl = new IntegerFieldControl();
        prefTypeIntegerFieldControl.label = "Pref Type (1, 2 or 3):";
        prefTypeIntegerFieldControl.value = 1;
        controls.add(prefTypeIntegerFieldControl);

        dampingDoubleFieldControl = new DoubleFieldControl();
        dampingDoubleFieldControl.label = "Damping (range valid in [0.5, 1.0]):";
        dampingDoubleFieldControl.value = 0.9;
        controls.add(dampingDoubleFieldControl);

        return controls;

    }

    private void initialize(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, int preftype) {
        this.n = networkNodes.size();
        this.m = networkEdges.size();

        this.similarity = new HashMap<Edge, Double>();
        responsibility = new HashMap<Edge, Double>();
        availability = new HashMap<Edge, Double>();
        ArrayList<Edge> edges = new ArrayList<Edge>(networkEdges);

        double pref = computePref(edges, preftype);
        // we add autolinks
        this.outedges = new HashMap<Node, Set<Edge>>();
        this.inedges = new HashMap<Node, Set<Edge>>();
        this.exemplar = new HashMap<Node, Node>();

        for (Node node : networkNodes) {
            edges.add(new Edge(node, node, pref));
            this.outedges.put(node, new HashSet<Edge>());
            this.inedges.put(node, new HashSet<Edge>());
            this.exemplar.put(node, null);
        }

        for (Edge e : edges) {
            this.similarity.put(e, e.weight + (1e-16 * e.weight + 1e-300) * Math.random());
            this.availability.put(e, 0.0);
            this.responsibility.put(e, 0.0);
            Node n1 = e.node1;
            Node n2 = e.node2;
            this.outedges.get(n1).add(e);
            this.outedges.get(n2).add(e);
            this.inedges.get(n1).add(e);
            this.inedges.get(n2).add(e);
        }
    }

    private boolean updateExemplars(final ArrayList<Node> networkNodes) {
        boolean changed = false;
        for (Node node : networkNodes) {
            Set<Edge> edges = this.outedges.get(node);
            double maxValue = Double.NEGATIVE_INFINITY;
            Node argmax = null;
            for (Edge e : edges) {

                double value = this.responsibility.get(e) + this.availability.get(e);

                if (value > maxValue) {
                    maxValue = value;
                    argmax = e.node2;
                }
            }
            if (this.exemplar.get(node) != argmax) {
                exemplar.put(node, argmax);
                changed = true;
            }
        }
        return changed;
    }

    private void updateResponsibilities(final ArrayList<Node> networkNodes, double damping) {
        for (Node node : networkNodes) {
            Set<Edge> edges = this.outedges.get(node);
            double max1 = Double.NEGATIVE_INFINITY, max2 = Double.NEGATIVE_INFINITY;
            Edge bestEdge = null;
            for (Edge edge : edges) {
                double value = this.similarity.get(edge) + this.availability.get(edge);
                if (value > max1) {
                    double tmp = max1;
                    max1 = value;
                    value = tmp;
                    bestEdge = edge;
                }
                if (value > max2) {
                    max2 = value;
                }
            }
            for (Edge edge : edges) {
                double resp = this.responsibility.get(edge);
                double sim = this.similarity.get(edge);
                if (edge != bestEdge) {
                    this.responsibility.put(edge, this.update(resp, sim - max1, damping));
                } else {
                    this.responsibility.put(edge, this.update(resp, sim - max2, damping));
                }
            }
        }
    }

    private void updateAvailabilities(final ArrayList<Node> networkNodes, double damping) {

        for (Node node : networkNodes) {
            Set<Edge> edges = this.inedges.get(node);
            int m = edges.size();
            // calculate sum of positive responsibilities
            double sum = 0.0;
            int i = 0;
            double rkk = 0.0;
            for (Edge e : edges) {
                i++;
                if (i != edges.size()) {
                    sum += Math.max(0.0, this.responsibility.get(e));
                } else {
                    rkk = this.responsibility.get(e);
                }
            }

            // calculate availabilities
            i = 0;
            for (Edge e : edges) {
                i++;
                double a = this.availability.get(e);
                if (i != edges.size()) {

                    this.availability.put(e, this.update(a, Math.min(0.0, rkk + sum - Math.max(0.0, this.responsibility.get(e))), damping));
                } else {
                    this.availability.put(e, this.update(a, sum, damping));
                }
            }
        }
    }

    double update(double v, double newValue, double damping) {
        return damping * v + (1.0 - damping) * newValue;
    }

    @Override
    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        int maxit = maxitIntegerFieldControl.value;
        int convit = convitIntegerFieldControl.value;
        int prefType = prefTypeIntegerFieldControl.value;
        double damping = dampingDoubleFieldControl.value;

        this.initialize(networkNodes, networkEdges, prefType);

        for (int i = 0, nochange = 0; i < maxit && nochange < convit; ++i, ++nochange) {
            this.updateResponsibilities(networkNodes, damping);
            this.updateAvailabilities(networkNodes, damping);

            if (updateExemplars(networkNodes)) {
                nochange = 0;
            }
        }

        // we write the clusters
        Map<Node, Cluster> cluster = new HashMap<Node, Cluster>();
        for (Map.Entry<Node, Node> entry : this.exemplar.entrySet()) {
            Node target = entry.getKey();
            Node clusterId = entry.getValue();
            if (!cluster.containsKey(clusterId)) {
                Cluster clu = new Cluster();
                cluster.put(clusterId, clu);
                clu.nodes = new ArrayList<Node>();
                networkClusters.add(clu);
            }
            Cluster cluster_i = cluster.get(clusterId);
            cluster_i.nodes.add(target);
//            cluster_i.label = "Cluster around node " + clusterId.label;
            target.cluster = cluster_i;
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

    private double computePref(ArrayList<Edge> edges, int prefType) {
        double pref = 0.0;
        if (prefType == 1) {
            Collections.sort(edges);
            int mm = edges.size();
            pref = (mm % 2 != 0) ? edges.get(mm / 2).weight : (edges.get(mm / 2 - 1).weight + edges.get(mm / 2).weight) / 2.0;
        } else if (prefType == 2) {
            pref = Collections.min(edges).weight;
        } else if (prefType == 3) {
            double minValue = Collections.min(edges).weight;
            double maxValue = Collections.max(edges).weight;
            pref = 2 * minValue - maxValue;
        }
        return pref;
    }
}