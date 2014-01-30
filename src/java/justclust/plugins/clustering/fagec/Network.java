/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.fagec;

import java.util.ArrayList;
import java.util.List;
import justclust.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;

/**
 *
 * @author wuaz008
 */
public class Network {

    public ArrayList<Node> nodes;
    public ArrayList<Edge> edges;

    public List<Edge> getAdjacentEdgeList(final Node node) {

        final List<Edge> adjacentEdgesList = new ArrayList<Edge>();

        if (!nodes.contains(node)) {
            return adjacentEdgesList;
        }

        for (Edge edge : node.edges) {
            if (edges.contains(edge) && edge.node1 != edge.node2) {
                if (edge.node1 == node && nodes.contains(edge.node2)) {
                    adjacentEdgesList.add(edge);
                }
                if (edge.node2 == node && nodes.contains(edge.node1)) {
                    adjacentEdgesList.add(edge);
                }
            }
        }

        return adjacentEdgesList;

    }

    public List<Edge> getConnectingEdgeList(final Node node1, final Node node2) {
        List<Edge> connectingEdgeList = new ArrayList<Edge>();
        for (Edge edge : node1.edges) {
            if (edges.contains(edge) && edge.node2 == node2) {
                connectingEdgeList.add(edge);
            }
        }
        return connectingEdgeList;
    }
    
    
}
