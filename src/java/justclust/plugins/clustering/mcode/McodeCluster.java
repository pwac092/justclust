/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.mcode;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import justclust.datastructures.Node;

/**
 * Stores various cluster information for simple get/set purposes.
 */
public class McodeCluster {

    private int resultId;
    private Long seedNode;
//    private MCODEGraph graph;
    private McodeGraph graph;
//    private List<Long> alCluster;
    private ArrayList<Long> alCluster;
//    private CyNetworkView view; // keeps track of layout so that layout process doesn't have to be repeated unnecessarily
    private Map<Long, Boolean> nodeSeenHashMap; // stores the nodes that have already been included in higher ranking clusters
    private double score;
    private String name; // pretty much unused so far, but could store name by user's input
    private int rank;
    private Image image;
    private boolean disposed;

//    public MCODECluster(final int resultId,
//            final Long seedNode,
//            final MCODEGraph graph,
//            final double score,
//            final List<Long> alCluster,
//            final Map<Long, Boolean> nodeSeenHashMap) {
    public McodeCluster(final int resultId,
            final Long seedNode,
            final McodeGraph graph,
            final double score,
            final ArrayList<Long> alCluster,
            final Map<Long, Boolean> nodeSeenHashMap) {
        assert seedNode != null;
        assert graph != null;
        assert alCluster != null;
        assert nodeSeenHashMap != null;

        this.resultId = resultId;
        this.seedNode = seedNode;
        this.graph = graph;
        this.score = score;
        this.alCluster = alCluster;
        this.nodeSeenHashMap = nodeSeenHashMap;
    }

    public int getResultId() {
        return resultId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        throwExceptionIfDisposed();
        this.name = name;
    }

//    public MCODEGraph getGraph() {
    public McodeGraph getGraph() {
        return graph;
    }

//    public synchronized CyNetworkView getView() {
//        return view;
//    }

//    public synchronized void setView(final CyNetworkView view) {
//        throwExceptionIfDisposed();
//
//        if (this.view != null) {
//            this.view.dispose();
//        }
//
//        this.view = view;
//    }

//    public synchronized CySubNetwork getNetwork() {
    public synchronized ArrayList<Node> getNetwork() {
//        return graph.getSubNetwork();
        return (ArrayList<Node>) graph.getNodeList();
    }

    public double getScore() {
        return score;
    }

    public List<Long> getALCluster() {
        return alCluster;
    }

    public Long getSeedNode() {
        return seedNode;
    }

    public Map<Long, Boolean> getNodeSeenHashMap() {
        return nodeSeenHashMap;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
        this.name = "Cluster " + (rank + 1);
    }

    public synchronized Image getImage() {
        return image;
    }

    public synchronized void setImage(Image image) {
        this.image = image;
    }

    public synchronized boolean isDisposed() {
        return disposed;
    }

    public synchronized void dispose() {
        if (isDisposed()) {
            return;
        }

//        if (view != null) {
//            view.dispose();
//        }

        graph.dispose();

        disposed = true;
    }

    @Override
    public String toString() {
        return "MCODECluster [clusterName=" + name + ", clusterScore=" + score
                + ", rank=" + rank + ", resultId=" + resultId + ", disposed=" + disposed + "]";
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    private void throwExceptionIfDisposed() {
        if (isDisposed()) {
            throw new RuntimeException("MCODECluster has been disposed and cannot be used anymore: ");
        }
    }
}
