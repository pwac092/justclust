/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.datastructures;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author wuaz008
 */
public class NodeGraphicalAttributes implements Serializable {
    
    // the graphical representation of the node
    public PPath graphicalNode;
    // the graphical representation of the node's label
    public PText graphicalLabel;
    // whether graphicalNode is visible
    public boolean visible;
    // the colour of graphicalNode
    public Color colour;
    // this field contains other Nodes which represent the same Node.
    // these include Nodes in the same graph which represent the same Node in
    // different overlapping Clusters (such Nodes need to be duplicated so that
    // the overlapping Clusters can be represented separately).
    // these also include Nodes in different graphs which represent the same
    // Node.
    public ArrayList<Node> otherVersions;
}
