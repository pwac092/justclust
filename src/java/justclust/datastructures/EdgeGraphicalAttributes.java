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
public class EdgeGraphicalAttributes implements Serializable {
    
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
    // this field contains other Edges which represent this same Edge.
    // these include Edges in different graphs which represent the same
    // Edge.
    public ArrayList<Edge> otherVersions;
}
