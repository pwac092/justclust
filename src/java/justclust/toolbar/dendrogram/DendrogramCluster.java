/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.dendrogram;

import java.awt.Point;
import java.io.Serializable;

// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session
public class DendrogramCluster implements Serializable {
    
    public double distance;
    public DendrogramCluster left;
    public int size;
    public int[] elements;
    public Point pos;
    public int level;
    public DendrogramCluster right;
    public int nodeIndex;

    public double getDistance() {
        return distance;
    }

    public DendrogramCluster getLeft() {
        return left;
    }

    public int size() {
        return size;
    }

    public int elementAt(int a) {
        return elements[a];
    }

    public void setPos(Point a) {
        pos = a;
    }

    public int getLevel() {
        return level;
    }

    public DendrogramCluster getRight() {
        return right;
    }

    public Point getPos() {
        return pos;
    }
}
