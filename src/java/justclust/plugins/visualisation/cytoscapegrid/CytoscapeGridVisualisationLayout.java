package justclust.plugins.visualisation.cytoscapegrid;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import justclust.datastructures.Cluster;

import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

public class CytoscapeGridVisualisationLayout implements VisualisationLayoutPluginInterface {

    // textFieldControl allows the getConfigurationControls and applyLayout
    // methods to share the text field of this TextFieldControl
    public TextFieldControl horizontalSpacingControl;
    public TextFieldControl verticalSpacingControl;

    public String getName() throws Exception {
        return "Cytoscape grid visualisation layout";
    }
    
    public String getDescription() throws Exception {
        return "This visualisation layout plug-in lays-out the graphical representation of the current network with the grid layout from Cytoscape.";
    }

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception {

        horizontalSpacingControl = new TextFieldControl();
        horizontalSpacingControl.label = "Horizontal Node Spacing:";
        horizontalSpacingControl.text = "50";

        verticalSpacingControl = new TextFieldControl();
        verticalSpacingControl.label = "Vertical Node Spacing:";
        verticalSpacingControl.text = "40";

        ArrayList<PluginConfigurationControl> controls = new ArrayList<PluginConfigurationControl>();
        controls.add(horizontalSpacingControl);
        controls.add(verticalSpacingControl);

        return controls;

    }

    public void applyLayout(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        double nodeHorizontalSpacing = Integer.parseInt(horizontalSpacingControl.text);
        double nodeVerticalSpacing = Integer.parseInt(verticalSpacingControl.text);

        double currX = 0.0d;
        double currY = 0.0d;
        double initialX = 0.0d;
        double initialY = 0.0d;

        // Yes, our size and starting points need to be different

        // the nodeCount is set to 0 so that, if there are no nodes, the rest
        // of this method will not try to access nodes which don't exist,
        // causing an error.
        // networkNodes != null is checked so that
        // networkNodes.size() is not called when
        // networkNodes == null which will cause an
        // error.
        int nodeCount = 0;
        if (networkNodes != null) {
            nodeCount = networkNodes.size();
        }
        final int columns = (int) Math.sqrt(nodeCount);

        // Calculate our starting point as the geographical center of the
        // selected nodes.
        for (int i = 1; i < networkNodes.size(); i++) {
            initialX += (networkNodes.get(i).getGraphicalNodeXCoordinate() / nodeCount);
            initialY += (networkNodes.get(i).getGraphicalNodeYCoordinate() / nodeCount);
        }

        // initialX and initialY reflect the center of our grid, so we
        // need to offset by distance*columns/2 in each direction
        initialX = initialX - ((nodeHorizontalSpacing * (columns - 1)) / 2);
        initialY = initialY - ((nodeVerticalSpacing * (columns - 1)) / 2);
        currX = initialX;
        currY = initialY;

        int count = 0;

        // Set visual property.
        // TODO: We need batch apply method for Visual Property values for
        // performance.
        for (int i = 0; i < networkNodes.size(); i++) {
            Node node = networkNodes.get(i);
            node.setGraphicalNodeXCoordinate(currX);
            node.setGraphicalNodeYCoordinate(currY);

            count++;

            if (count == columns) {
                count = 0;
                currX = initialX;
                currY += nodeVerticalSpacing;
            } else {
                currX += nodeHorizontalSpacing;
            }
        }

    }
}
