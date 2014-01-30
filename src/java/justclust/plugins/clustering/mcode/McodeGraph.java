/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.mcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;

/**
 *
 * @author wuaz008
 */
public class McodeGraph {

//    private final CyRootNetwork rootNetwork;
    private final ArrayList<Node> rootNetworkNodes;
    private final ArrayList<Edge> rootNetworkEdges;
//    private final Set<CyNode> nodes;
    private final Set<Node> nodes;
//    private final Set<CyEdge> edges;
    private final Set<Edge> edges;
//    private final Map<Long, CyNode> nodeMap;
    private final Map<Long, Node> nodeMap;
//    private final Map<Long, CyEdge> edgeMap;
    private final Map<Long, Edge> edgeMap;
//    private CySubNetwork subNetwork;
//    private MCODEUtil mcodeUtil;
    private McodeUtil mcodeUtil;
    private boolean disposed;

//    public MCODEGraph(final CyRootNetwork rootNetwork,
//            final Collection<CyNode> nodes,
//            final Collection<CyEdge> edges,
//            final MCODEUtil mcodeUtil) {
    public McodeGraph(ArrayList<Node> rootNetworkNodes,
            ArrayList<Edge> rootNetworkEdges,
            final Collection<Node> nodes,
            final Collection<Edge> edges,
            final McodeUtil mcodeUtil) {
//        if (rootNetwork == null) {
//            throw new NullPointerException("rootNetwork is null!");
//        }
        if (nodes == null) {
            throw new NullPointerException("nodes is null!");
        }
        if (edges == null) {
            throw new NullPointerException("edges is null!");
        }

        this.mcodeUtil = mcodeUtil;
//        this.rootNetwork = rootNetwork;
        this.rootNetworkNodes = rootNetworkNodes;
        this.rootNetworkEdges = rootNetworkEdges;
//        this.nodes = Collections.synchronizedSet(new HashSet<CyNode>(nodes.size()));
        this.nodes = Collections.synchronizedSet(new HashSet<Node>(nodes.size()));
//        this.edges = Collections.synchronizedSet(new HashSet<CyEdge>(edges.size()));
        this.edges = Collections.synchronizedSet(new HashSet<Edge>(edges.size()));
//        this.nodeMap = Collections.synchronizedMap(new HashMap<Long, CyNode>(nodes.size()));
        this.nodeMap = Collections.synchronizedMap(new HashMap<Long, Node>(nodes.size()));
//        this.edgeMap = Collections.synchronizedMap(new HashMap<Long, CyEdge>(edges.size()));
        this.edgeMap = Collections.synchronizedMap(new HashMap<Long, Edge>(edges.size()));

//        for (CyNode n : nodes) {
        for (Node n : nodes) {
            addNode(n);
        }
//        for (CyEdge e : edges) {
        for (Edge e : edges) {
            addEdge(e);
        }
    }

//    public boolean addNode(CyNode node) {
    public boolean addNode(Node node) {
        if (nodes.contains(node)) {
            return false;
        }

//        node = rootNetwork.getNode(node.getSUID());

        if (nodes.add(node)) {
//            nodeMap.put(node.getSUID(), node);
            nodeMap.put((long) McodeClusteringAlgorithm.classInstance.networkNodes.indexOf(node), node);
            return true;
        }

        return false;
    }

//    public boolean addEdge(CyEdge edge) {
    public boolean addEdge(Edge edge) {
        if (edges.contains(edge)) {
            return false;
        }

//        if (nodes.contains(edge.getSource()) && nodes.contains(edge.getTarget())) {
        if (nodes.contains(edge.node1) && nodes.contains(edge.node2)) {
//            edge = rootNetwork.getEdge(edge.getSUID());

            if (edges.add(edge)) {
//                edgeMap.put(edge.getSUID(), edge);
                edgeMap.put((long) McodeClusteringAlgorithm.classInstance.networkEdges.indexOf(edge), edge);
                return true;
            }
        }

        return false;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

//    public List<CyNode> getNodeList() {
    public List<Node> getNodeList() {
//        return new ArrayList<CyNode>(nodes);{
        return new ArrayList<Node>(nodes);
    }

//    public List<CyEdge> getEdgeList() {
    public List<Edge> getEdgeList() {
//        return new ArrayList<CyEdge>(edges);
        return new ArrayList<Edge>(edges);
    }

//    public boolean containsNode(final CyNode node) {
    public boolean containsNode(final Node node) {
        return nodes.contains(node);
    }

//    public boolean containsEdge(final CyEdge edge) {
    public boolean containsEdge(final Edge edge) {
        return edges.contains(edge);
    }

//    public CyNode getNode(final long index) {
    public Node getNode(final long index) {
        return nodeMap.get(index);
    }

//    public CyEdge getEdge(final long index) {
    public Edge getEdge(final long index) {
        return edgeMap.get(index);
    }

//    public List<CyEdge> getAdjacentEdgeList(final CyNode node, final Type edgeType) {
    public List<Edge> getAdjacentEdgeList(final Node node) {
//        List<CyEdge> rootList = rootNetwork.getAdjacentEdgeList(node, edgeType);
        Node n = node;
        final ArrayList<Edge> adjacentEdgesArrayList = new ArrayList<Edge>();
        for (Edge edge : n.edges) {
            if (edge.node1 == n && McodeClusteringAlgorithm.classInstance.networkNodes.contains(n) && McodeClusteringAlgorithm.classInstance.networkEdges.contains(edge) && edge.node1 != edge.node2) {
                adjacentEdgesArrayList.add(edge);
            }
            if (edge.node2 == n && McodeClusteringAlgorithm.classInstance.networkNodes.contains(n) && McodeClusteringAlgorithm.classInstance.networkEdges.contains(edge) && edge.node1 != edge.node2) {
                adjacentEdgesArrayList.add(edge);
            }
        }
        List<Edge> rootList = adjacentEdgesArrayList;

//        List<CyEdge> list = new ArrayList<CyEdge>(rootList.size());
        List<Edge> list = new ArrayList<Edge>(rootList.size());

//        for (CyEdge e : rootList) {
        for (Edge e : rootList) {
            if (containsEdge(e)) {
                list.add(e);
            }
        }

        return list;
    }

//    public List<CyEdge> getConnectingEdgeList(final CyNode source, final CyNode target, final Type edgeType) {
    public List<Edge> getConnectingEdgeList(final Node source, final Node target) {
//        List<CyEdge> rootList = rootNetwork.getConnectingEdgeList(source, target, edgeType);
        List<Edge> rootList = new ArrayList<Edge>();
        for (Edge edge : source.edges) {
            if (edge.node2 == target) {
                rootList.add(edge);
            }
        }
//        List<CyEdge> list = new ArrayList<CyEdge>(rootList.size());
        List<Edge> list = new ArrayList<Edge>(rootList.size());

//        for (CyEdge e : rootList) {
        for (Edge e : rootList) {
            if (containsEdge(e)) {
                list.add(e);
            }
        }

        return list;
    }

//    public CyRootNetwork getRootNetwork() {
    public void getRootNetwork(ArrayList<Node> nodes, ArrayList<Edge> edges) {
//        return rootNetwork;
        nodes.clear();
        nodes.addAll(rootNetworkNodes);
        edges.clear();
        edges.addAll(rootNetworkEdges);
    }
//    public synchronized CySubNetwork getSubNetwork() {
//        if (!disposed && subNetwork == null) {
//            subNetwork = mcodeUtil.createSubNetwork(rootNetwork, nodes, SavePolicy.DO_NOT_SAVE);
//        }
//
//        return subNetwork;
//    }

    public synchronized boolean isDisposed() {
        return disposed;
    }

    public synchronized void dispose() {
        if (disposed) {
            return;
        }

//        if (subNetwork != null) {
//            mcodeUtil.destroy(subNetwork);
//            subNetwork = null;
//        }

        nodes.clear();
        edges.clear();
        nodeMap.clear();
        edgeMap.clear();

        disposed = true;
    }
}
