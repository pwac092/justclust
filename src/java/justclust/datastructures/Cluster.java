package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PText;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class has instances which represent clusters.
 */
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Cluster implements Serializable {

    // the graphical representation of the cluster's label
    public PText graphicalLabel;
    // the text of graphicalLabel
    public String label;
    /**
     * This field contains the nodes of the current cluster.
     */
    public ArrayList<Node> nodes;
}
