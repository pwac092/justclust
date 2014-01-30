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
public class Edge implements Serializable {

    // the graphical representation of the edge
    public PPath graphicalEdge;
    // the graphical representation of the edge's label
    public PText graphicalLabel;
    // the text of graphicalLabel
    public String label;
    // whether graphicalEdge is visible
    public boolean visible;
    // the colour of graphicalEdge
    public Color colour;
    /**
     * This field contains a node of the current edge.
     */
    public Node node1;
    /**
     * This field contains a node of the current edge.
     */
    public Node node2;
    /**
     * This field contains the weight of the current edge.
     */
    public double weight;
    // this field contains other Edges which represent this same Edge.
    // these include Edges in different graphs which represent the same
    // Edge.
    public ArrayList<Edge> otherVersions;
    // the instance of the Data class which contains this Edge.
    // if the Edge is saved as part of a session, which graph it belongs to can
    // be discovered easily from this field (each Data instance corresponds to
    // exactly one graph).
    public Data data;
}
