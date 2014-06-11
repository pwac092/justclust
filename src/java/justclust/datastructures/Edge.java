package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.io.Serializable;

/**
 * This class has instances which represent edges.
 */
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Edge implements Serializable, Comparable<Edge> {

    // the graphical representation of the edge
    public PPath graphicalEdge;
    // the graphical representation of the edge's label
    public PText graphicalLabel;
    /**
     * This field contains a node of the current edge.
     */
    public Node node1;
    /**
     * This field contains a node of the current edge.
     */
    public Node node2;
    // the instance of the Data class which contains this Edge.
    // if the Edge is saved as part of a session, which graph it belongs to can
    // be discovered easily from this field (each Data instance corresponds to
    // exactly one graph).
    public Data data;
    public EdgeSharedAttributes edgeSharedAttributes;

    public Edge() {
        edgeSharedAttributes = new EdgeSharedAttributes();
    }

    public Edge(Node node1, Node node2, double weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.edgeSharedAttributes.weight = weight;
    }

    @Override
    public int compareTo(Edge t) {
        if (this.edgeSharedAttributes.weight < t.edgeSharedAttributes.weight) {
            return -1;
        } else if (this.edgeSharedAttributes.weight == t.edgeSharedAttributes.weight) {
            return 0;
        } else {
            return 1;
        }
    }
}
