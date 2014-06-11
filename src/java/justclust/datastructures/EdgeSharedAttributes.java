/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.datastructures;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author wuaz008
 */
public class EdgeSharedAttributes implements Serializable {

    // the text of graphicalLabel
    public String label;
    // whether graphicalEdge is visible
    public boolean visible;
    // the colour of graphicalEdge
    public Color colour;
    /**
     * This field contains the weight of the current edge.
     */
    public double weight;
    // this field contains other Edges which represent this same Edge.
    // these include Edges in different graphs which represent the same
    // Edge.
    public ArrayList<Edge> otherVersions;
}
