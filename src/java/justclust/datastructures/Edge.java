package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class has instances which represent edges.
 */
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Edge implements Serializable, Comparable<Edge> {

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
    /**
     * This field contains the weight of the current edge.
     */
    public double weight;
    public EdgeGraphicalAttributes edgeGraphicalAttributes;

    public Edge() {
    }

    public Edge(Node node1, Node node2, double weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge t) {
        if (this.weight < t.weight) {
            return -1;
        } else if (this.weight == t.weight) {
            return 0;
        } else {
            return 1;
        }
    }
}
