/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

// this class implements Serializable because an instance of the class is saved
// to a file when the user saves their session
public class Session implements Serializable {
    
    public ArrayList<Data> data;
    public ArrayList<ArrayList<Double>> nodeXCoordinates;
    public ArrayList<ArrayList<Double>> nodeYCoordinates;
    public ArrayList<String> tabTitles;
    
}
