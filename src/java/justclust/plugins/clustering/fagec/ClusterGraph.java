/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.fagec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import justclust.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;

/**
 *
 * @author wuaz008
 */
public class ClusterGraph {

//    private final CyRootNetwork rootNetwork;
    private final Network rootNetwork;
//    private final Set<CyNode> nodes;
    private final Set<Node> nodes;
//    private final Set<CyEdge> edges;
    private final Set<Edge> edges;
//    private final Map<Long, CyNode> nodeMap;
    private final Map<Long, Node> nodeMap;
//    private final Map<Long, CyEdge> edgeMap;
    private final Map<Long, Edge> edgeMap;
//    private CySubNetwork subNetwork;
    private Network subNetwork;
    private ClusterUtil clusterUtil;
    private boolean disposed;

//    public ClusterGraph(CyRootNetwork rootNetwork, Collection<CyNode> nodes, Collection<CyEdge> edges, ClusterUtil mcodeUtil) {
    public ClusterGraph(Network rootNetwork, Collection<Node> nodes, Collection<Edge> edges, ClusterUtil mcodeUtil) {
        if (rootNetwork == null) {
            throw new NullPointerException("rootNetwork is null!");
        }
        if (nodes == null) {
            throw new NullPointerException("nodes is null!");
        }
        if (edges == null) {
            throw new NullPointerException("edges is null!");
        }
        this.clusterUtil = mcodeUtil;
        this.rootNetwork = rootNetwork;


        this.nodes = Collections.synchronizedSet(new HashSet(nodes.size()));
        this.edges = Collections.synchronizedSet(new HashSet(edges.size()));
        this.nodeMap = Collections.synchronizedMap(new HashMap(nodes.size()));
        this.edgeMap = Collections.synchronizedMap(new HashMap(edges.size()));

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
        if (this.nodes.contains(node)) {
            return false;
        }
//        node = this.rootNetwork.getNode(node.getSUID().longValue());

        if (this.nodes.add(node)) {
//            this.nodeMap.put(node.getSUID(), node);
            this.nodeMap.put((long) Data.classInstance.networkNodes.indexOf(node), node);
            return true;
        }

        return false;
    }

//    public boolean addEdge(CyEdge edge) {
    public boolean addEdge(Edge edge) {
        if (this.edges.contains(edge)) {
            return false;
        }
//        if ((this.nodes.contains(edge.getSource())) && (this.nodes.contains(edge.getTarget()))) {
        if ((this.nodes.contains(edge.node1)) && (this.nodes.contains(edge.node2))) {
//            edge = this.rootNetwork.getEdge(edge.getSUID().longValue());

            if (this.edges.add(edge)) {
//                this.edgeMap.put(edge.getSUID(), edge);
                this.edgeMap.put((long) Data.classInstance.networkEdges.indexOf(edge), edge);
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
        return new ArrayList(this.nodes);
    }

//    public List<CyEdge> getEdgeList() {
    public List<Edge> getEdgeList() {
        return new ArrayList(this.edges);
    }

//    public boolean containsNode(CyNode node) {
    public boolean containsNode(Node node) {
        return nodes.contains(node);
    }

//    public boolean containsEdge(CyEdge edge) {
    public boolean containsEdge(Edge edge) {
        return edges.contains(edge);
    }

//    public CyNode getNode(long index) {
    public Node getNode(long index) {
//        return (CyNode) nodeMap.get(Long.valueOf(index));
        return (Node) nodeMap.get(Long.valueOf(index));
    }

//    public CyEdge getEdge(long index) {
    public Edge getEdge(long index) {
//        return (CyEdge) edgeMap.get(Long.valueOf(index));
        return (Edge) edgeMap.get(Long.valueOf(index));
    }

//    public List getAdjacentEdgeList(CyNode node, org.cytoscape.model.CyEdge.Type edgeType) {
    public List getAdjacentEdgeList(Node node) {
//        List rootList = rootNetwork.getAdjacentEdgeList(node, edgeType);
        List rootList = rootNetwork.getAdjacentEdgeList(node);
        List list = new ArrayList(rootList.size());
        for (Iterator iterator = rootList.iterator(); iterator.hasNext();) {
//            CyEdge e = (CyEdge) iterator.next();
            Edge e = (Edge) iterator.next();
            if (containsEdge(e)) {
                list.add(e);
            }
        }

        return list;
    }

//    public List getConnectingEdgeList(CyNode source, CyNode target, org.cytoscape.model.CyEdge.Type edgeType) {
    public List getConnectingEdgeList(Node source, Node target) {
//        List rootList = rootNetwork.getConnectingEdgeList(source, target, edgeType);
        List rootList = rootNetwork.getConnectingEdgeList(source, target);
        List list = new ArrayList(rootList.size());
        for (Iterator iterator = rootList.iterator(); iterator.hasNext();) {
//            CyEdge e = (CyEdge) iterator.next();
            Edge e = (Edge) iterator.next();
            if (containsEdge(e)) {
                list.add(e);
            }
        }

        return list;
    }

//    public CyRootNetwork getRootNetwork() {
    public Network getRootNetwork() {
        return rootNetwork;
    }

//    public synchronized CySubNetwork getSubNetwork() {
    public synchronized Network getSubNetwork() {
        if (!disposed && subNetwork == null) {
//            subNetwork = clusterUtil.createSubNetwork(rootNetwork, nodes, SavePolicy.DO_NOT_SAVE);
            subNetwork = clusterUtil.createSubNetwork(rootNetwork, nodes);
        }
        return subNetwork;
    }

    public synchronized boolean isDisposed() {
        return disposed;
    }

    public synchronized void dispose() {
        if (disposed) {
            return;
        }
        if (subNetwork != null) {
            clusterUtil.destroy(subNetwork);
            subNetwork = null;
        }
        nodes.clear();
        edges.clear();
        nodeMap.clear();
        edgeMap.clear();
        disposed = true;
    }
}
