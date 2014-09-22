package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class has instances which represent nodes.
 */
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Node implements Serializable {

    // the text of graphicalLabel
    public String label;
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
    public Node equivalentNodeInOriginalNetwork;
    public ArrayList<Double> microarrayValues;
    public NodeGraphicalAttributes nodeGraphicalAttributes;

    public Node() {
    }

    public double getGraphicalNodeXCoordinate() {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getX.
        return nodeGraphicalAttributes.graphicalNode.getX() + nodeGraphicalAttributes.graphicalNode.getOffset().getX();
    }

    public void setGraphicalNodeXCoordinate(double xCoordinate) {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getX.
        // the x coordinate of the offset is set to 0 so that the new x
        // coordinate of the node is not affected by the x coordinate of the
        // offset.
        nodeGraphicalAttributes.graphicalNode.setOffset(0, nodeGraphicalAttributes.graphicalNode.getOffset().getY());
        nodeGraphicalAttributes.graphicalNode.setX(xCoordinate);
        // set the x coordinate of the node.s label
        nodeGraphicalAttributes.graphicalLabel.setX(nodeGraphicalAttributes.graphicalNode.getFullBoundsReference().getCenter2D().getX() - nodeGraphicalAttributes.graphicalLabel.getWidth() / 2);
    }

    public double getGraphicalNodeYCoordinate() {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getY.
        return nodeGraphicalAttributes.graphicalNode.getY() + nodeGraphicalAttributes.graphicalNode.getOffset().getY();
    }

    public void setGraphicalNodeYCoordinate(double yCoordinate) {
        // the offset describes how the user has moved the node from its
        // initial position which is described by graphNode.getY.
        // the y coordinate of the offset is set to 0 so that the new y
        // coordinate of the node is not affected by the y coordinate of the
        // offset.
        nodeGraphicalAttributes.graphicalNode.setOffset(nodeGraphicalAttributes.graphicalNode.getOffset().getX(), 0);
        nodeGraphicalAttributes.graphicalNode.setY(yCoordinate);
        // set the y coordinate of the node's label
        nodeGraphicalAttributes.graphicalLabel.setY(nodeGraphicalAttributes.graphicalNode.getFullBoundsReference().getCenter2D().getY() - nodeGraphicalAttributes.graphicalLabel.getHeight() / 2);
    }
}
