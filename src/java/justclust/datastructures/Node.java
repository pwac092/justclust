package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class has instances which represent nodes.
 */
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Node implements Serializable {

    // the graphical representation of the node
    public PPath graphicalNode;
    // the graphical representation of the node's label
    public PText graphicalLabel;
    /**
     * This field contains the edges of the current node.
     */
    public ArrayList<Edge> edges;
    /**
     * This field contains the cluster of the current node.
     */
    public Cluster cluster;
    // the instance of the Data class which contains this Node.
    // if the Node is saved as part of a session, which graph it belongs to can
    // be discovered easily from this field (each Data instance corresponds to
    // exactly one graph).
    public Data data;
    public NodeSharedAttributes nodeSharedAttributes;
    
    public Node() {
        nodeSharedAttributes = new NodeSharedAttributes();
    }

    public double getGraphicalNodeXCoordinate() {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getX.
        return graphicalNode.getX() + graphicalNode.getOffset().getX();
    }

    public void setGraphicalNodeXCoordinate(double xCoordinate) {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getX.
        // the x coordinate of the offset is set to 0 so that the new x
        // coordinate of the node is not affected by the x coordinate of the
        // offset.
        graphicalNode.setOffset(0, graphicalNode.getOffset().getY());
        graphicalNode.setX(xCoordinate);
        // set the x coordinate of the node.s label
        graphicalLabel.setX(graphicalNode.getFullBoundsReference().getCenter2D().getX() - graphicalLabel.getWidth() / 2);
    }

    public double getGraphicalNodeYCoordinate() {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getY.
        return graphicalNode.getY() + graphicalNode.getOffset().getY();
    }

    public void setGraphicalNodeYCoordinate(double yCoordinate) {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getY.
        // the y coordinate of the offset is set to 0 so that the new y
        // coordinate of the node is not affected by the y coordinate of the
        // offset.
        graphicalNode.setOffset(graphicalNode.getOffset().getX(), 0);
        graphicalNode.setY(yCoordinate);
        // set the y coordinate of the node's label
        graphicalLabel.setY(graphicalNode.getFullBoundsReference().getCenter2D().getY() - graphicalLabel.getHeight() / 2);
    }
}
