/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.visualisation.cytoscapeedgeweightedspringembedded;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

/**
 *
 * @author wuaz008
 */
public class CytoscapeEdgeWeightedSpringEmbeddedVisualisationLayout implements VisualisationLayoutPluginInterface {

    // textFieldControl allows the getConfigurationControls and applyLayout
    // methods to share the text field of this TextFieldControl
    public TextFieldControl textFieldControl;

    public String getName() throws Exception {
        return "Cytoscape edge-weighted spring embedded visualisation layout";
    }

    public String getDescription() throws Exception {
        return "This visualisation layout plug-in lays-out the graphical representation of the current network with the edge-weighted spring embedded layout from Cytoscape.";
    }

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception {

        textFieldControl = new TextFieldControl();
        textFieldControl.label = "Number of Layout Passes:";
        textFieldControl.text = "2";

        ArrayList<PluginConfigurationControl> controls = new ArrayList<PluginConfigurationControl>();
        controls.add(textFieldControl);

        return controls;

    }
    public ArrayList<Node> networkNodes;
    public ArrayList<Edge> networkEdges;
    public ArrayList<Cluster> networkClusters;
    // these arrays contain coordinates for every node.
    // the point of using these arrays is to speed up the layout by saving
    // the coordinates in them rather than setting the coordinates of the nodes.
    static double[] nodeXCoordinates;
    static double[] nodeYCoordinates;
    // this array contains the weight of every edge.
    // this is neccessary because this layout normalizes the weights and the
    // changes should not been made directly to the network edges.
    static double[] edgeWeights;
    /**
     * Spring strength
     */
    private static double m_nodeDistanceStrengthConstant = 15.0;
    /**
     * Spring rest length"
     */
    private static double m_nodeDistanceRestLengthConstant = 45.0;
    private static double[] m_nodeDistanceSpringScalars;
    /**
     * Strength of a 'disconnected' spring
     */
    private static double m_disconnectedNodeDistanceSpringStrength = 0.05;
    /**
     * Rest length of a 'disconnected' spring"
     */
    private static double m_disconnectedNodeDistanceSpringRestLength = 2000.0;
    /**
     * Strength to apply to avoid collisions
     */
    private static double m_anticollisionSpringStrength;
    private static double[] m_anticollisionSpringScalars;
    /**
     * Data arrays
     */
    private static double[][] m_nodeDistanceSpringRestLengths;
    private static double[][] m_nodeDistanceSpringStrengths;
    /**
     * Current layout pass
     */
    private static int m_layoutPass = 2;
    public static int m_nodeCount;
    public static ArrayList<Node> component;
    public static ArrayList<Edge> edgesWithinComponent;

    public void applyLayout(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        // these variables are made available here to the other methods in this
        // plug-in
        this.networkNodes = networkNodes;
        this.networkEdges = networkEdges;
        this.networkClusters = networkClusters;

        // create an array.
        // one element for each node.
        // the element contains whether the node's connected component has been
        // visited yet by the below algorithm.
        // the purpose of the array is to prevent the algorithm from finding the
        // connected component of each node even when it has already been found.
        boolean[] componentFound = new boolean[networkNodes.size()];
        // this ArrayList will contain all the connected components
        ArrayList<ArrayList<Node>> connectedComponents = new ArrayList<ArrayList<Node>>();
        // iterate through each node
        for (int i = 0; i < networkNodes.size(); i++) {
            // if the node's component has not been found
            if (!componentFound[i]) {

                // get the node's connected component
                ArrayList<Node> component = new ArrayList<Node>();
                component = getConnectedComponent(component, networkNodes.get(i));

                // the componentFound array is updated for all nodes in the
                // component
                for (int j = 0; j < component.size(); j++) {
                    componentFound[networkNodes.indexOf(component.get(j))] = true;
                }

                connectedComponents.add(component);

            }
        }

        // iterate through all the connected components, and lay them out with
        // the edge-weighted spring embedded layout
        for (ArrayList<Node> element : connectedComponents) {
            component = element;

            // these arrays contain coordinates for every node.
            // the point of using these arrays is to speed up the layout by changing
            // the coordinates in them rather than setting the coordinates of the nodes,
            // which takes longer, and changing them later.
            nodeXCoordinates = new double[component.size()];
            nodeYCoordinates = new double[component.size()];

            // the nodeXCoordinates and nodeYCoordinates arrays are populated with
            // the nodes' coordinates.
            for (int i = 0; i < networkNodes.size(); i++) {
                if (!component.contains(networkNodes.get(i))) {
                    continue;
                }
                int nodeIndex = component.indexOf(networkNodes.get(i));
                Node node = networkNodes.get(i);
                nodeXCoordinates[nodeIndex] = node.getGraphicalNodeXCoordinate();
                nodeYCoordinates[nodeIndex] = node.getGraphicalNodeYCoordinate();
            }

            // the edgeWeights array contains the weight of every edge.
            // this is neccessary because this layout normalizes the weights and the
            // changes should not been made directly to the network edges.
            edgesWithinComponent = new ArrayList<Edge>();
            OUTER_LOOP:
            for (Edge edge : networkEdges) {
                if (component.contains(edge.node1) && component.contains(edge.node2)) {
                    edgesWithinComponent.add(edge);
                }
            }
            edgeWeights = new double[edgesWithinComponent.size()];
            for (int i = 0; i < edgesWithinComponent.size(); i++) {
                edgeWeights[i] = edgesWithinComponent.get(i).weight;
            }

//        LayoutPoint initialLocation = null;
            double initialLocationX;
            double initialLocationY;

//        this.partition = partition;

            // Initialize all of our values.  This will create
            // our internal objects and initialize them
            // local_initialize();

//        m_nodeCount = partition.nodeCount();
            m_nodeCount = component.size();

            // this comment is not applicable here
////         Set defaults -- this is done here insted of in the constructor
////         to allow users to change m_numLayoutPasses
            int m_numLayoutPasses = Integer.parseInt(textFieldControl.text);
            m_nodeDistanceSpringScalars = new double[m_numLayoutPasses];
            for (int i = 0; i < m_numLayoutPasses; i++) {
                m_nodeDistanceSpringScalars[i] = 1.0;
            }
            m_anticollisionSpringScalars = new double[m_numLayoutPasses];
            m_anticollisionSpringScalars[0] = 0.0;

            for (int i = 1; i < m_numLayoutPasses; i++) {
                m_anticollisionSpringScalars[i] = 1.0;
            }
//        System.out.println("BioLayoutKK Algorithm.  Laying out " + m_nodeCount + " nodes and "
//                + partition.edgeCount() + " edges: ");

            /*
             for (Iterator diter = partition.nodeIterator(); diter.hasNext(); ) {
             System.out.println("\t"+(LayoutNode)diter.next());
             }
             for (Iterator diter = partition.edgeIterator(); diter.hasNext(); ) {
             System.out.println("\t"+(LayoutEdge)diter.next());
             }
             */

            // Calculate a distance threshold
//        double euclideanDistanceThreshold = (m_nodeCount + partition.edgeCount()) / 10;
            double euclideanDistanceThreshold = (m_nodeCount + edgesWithinComponent.size()) / 10;
            double m_averageIterationsPerNode = 40;
//        int numIterations = (int) ((m_nodeCount * m_averageIterationsPerNode) / m_numLayoutPasses);
            int numIterations = (int) ((m_nodeCount * m_averageIterationsPerNode) / 10);
            List<PartialDerivatives> partialsList = new ArrayList<PartialDerivatives>();
            double[] potentialEnergy = new double[1];
            if (potentialEnergy[0] != 0.0) {
                throw new RuntimeException();
            }
            PartialDerivatives partials;
            PartialDerivatives furthestNodePartials = null;
            m_nodeDistanceSpringRestLengths = new double[m_nodeCount][m_nodeCount];
            m_nodeDistanceSpringStrengths = new double[m_nodeCount][m_nodeCount];

            // Figure out our starting point
//        initialLocation = partition.getAverageLocation();
            // the average coordinates are computed by iterating through all of the
            // nodes and adding their coordinates divided by the amount of nodes.
            double averageX = 0;
            double averageY = 0;
            for (int i = 0; i < component.size(); i++) {
                averageX += nodeXCoordinates[i] / component.size();
                averageY += nodeYCoordinates[i] / component.size();
            }
            initialLocationX = averageX;
            initialLocationY = averageY;

            // this comment is not applicable here
////         Randomize our points, if any points lie
////         outside of our bounds
//        if (context.randomize) {
//            partition.randomizeLocations();
//        }
            // the nodes are randomly positioned by iterating through them all.
            // the index i is increase by 2 and initialized to 1
            // to get the indexes of the graphical node representations (the PPaths).
            // the increase by 2 is because the node labels are
            // interleaved with the nodes themselves and these have indexes
            // also.
            // the initialization to one skips the first index which is for the
            // nodeLayer itself (the nodeLayer is a node of itself).
            // Get a seeded pseudo random-number generator
            Date today = new Date();
            Random random = new Random(today.getTime());
            for (int i = 0; i < component.size(); i++) {
                // 60 and 40 are the width and height, respectively, which Cytoscape
                // gives to nodes
                double x = random.nextDouble() * component.size() * 60;
                double y = random.nextDouble() * component.size() * 40;
                nodeXCoordinates[i] = x;
                nodeYCoordinates[i] = y;
            }

            // Calculate our edge weights
//        partition.calculateEdgeWeights();
            double lowerBounds = .1f;
            double upperBounds = .9f;
            double maxWeight = -1000000;
            double minWeight = 1000000;
            for (int i = 0; i < edgeWeights.length; i++) {
                maxWeight = Math.max(maxWeight, edgeWeights[i]);
                minWeight = Math.min(minWeight, edgeWeights[i]);
            }
            double normalFactor = (upperBounds - lowerBounds) / (maxWeight - minWeight);
            for (int i = 0; i < edgeWeights.length; i++) {
                edgeWeights[i] = (edgeWeights[i] - minWeight) * normalFactor + lowerBounds;
            }

            // Compute our distances
            //        if (cancelled) {
            //            return;
            //        }
            //        taskMonitor.setProgress(0.02);
            //        taskMonitor.setStatusMessage("Calculating node distances");

            int[][] nodeDistances = calculateNodeDistances();

//        if (cancelled) {
//            return;
//        }
//        taskMonitor.setProgress(0.04);
//        taskMonitor.setStatusMessage("Calculating spring constants");

            calculateSpringData(nodeDistances);

            final double percentCompletedBeforePasses = 5.0d;
            final double percentCompletedAfterPass1 = 60.0d;
            final double percentCompletedAfterFinalPass = 95.0d;
            double currentProgress = percentCompletedBeforePasses;
            // Profile partialProfile = new Profile();
            // Profile springProfile = new Profile();

            // Compute our optimal lengths
            for (m_layoutPass = 0; m_layoutPass < m_numLayoutPasses; m_layoutPass++) {

                final double percentProgressPerIter;
//            Profile passTimer = new Profile();
//            passTimer.start();
                long startTime = System.currentTimeMillis();

                if (m_layoutPass == 0) {
                    percentProgressPerIter = (percentCompletedAfterPass1 - percentCompletedBeforePasses) / (double) (m_nodeCount
                            + numIterations);
                } else {
                    percentProgressPerIter = (percentCompletedAfterFinalPass
                            - percentCompletedAfterPass1) / (double) ((m_nodeCount
                            + numIterations) * (m_numLayoutPasses
                            - 1));
                }

                // Initialize this layout pass.
                potentialEnergy[0] = 0.0;
                partialsList.clear();
                furthestNodePartials = null;

//            taskMonitor.setStatusMessage("Calculating partial derivatives -- pass " + (m_layoutPass + 1)
//                    + " of " + m_numLayoutPasses);

                // partialProfile.start();

                // Calculate all node distances.  Keep track of the furthest.
//            for (LayoutNode v : partition.getNodeList()) {
                for (Node v : component) {

//                if (cancelled) {
//                    return;
//                }

//                taskMonitor.setProgress(currentProgress / 100.0);

//                if (v.isLocked()) {
//                    continue;
//                }

                    partials = new CytoscapeEdgeWeightedSpringEmbeddedVisualisationLayout().new PartialDerivatives(v);
                    calculatePartials(partials, null, potentialEnergy, false);
                    // System.out.println(partials.printPartial()+" potentialEnergy = "+potentialEnergy[0]);
                    partialsList.add(partials);

                    if ((furthestNodePartials == null)
                            || (partials.euclideanDistance > furthestNodePartials.euclideanDistance)) {
                        furthestNodePartials = partials;
                    }

                    currentProgress += percentProgressPerIter;
                }

//            // partialProfile.done("Partial time for pass "+(m_layoutPass+1)+" is ");
//            taskMonitor.setStatusMessage("Executing spring logic -- pass " + (m_layoutPass + 1) + " of "
//                    + m_numLayoutPasses);

                // springProfile.start();
                for (int iterations_i = 0; (iterations_i < numIterations) && (furthestNodePartials.euclideanDistance >= euclideanDistanceThreshold); iterations_i++) {
//                if (cancelled) {
//                    return;
//                }

//                taskMonitor.setPrmoveogress(currentProgress / 100.0);

                    furthestNodePartials = moveNode(furthestNodePartials, partialsList, potentialEnergy);
                    //    		System.out.println(furthestNodePartials.printPartial()+" (furthest) potentialEnergy = "+potentialEnergy[0]);
                    currentProgress += percentProgressPerIter;
                }

                // springProfile.done("Spring time for pass "+(m_layoutPass+1)+" is ");

            }

//        taskMonitor.setProgress(percentCompletedAfterFinalPass / 100.0);
//        taskMonitor.setStatusMessage("Updating display");

            // the pieces have already been actually moved around during the
            // algorithm so far
//        // Actually move the pieces around
//        // Note that we reset our min/max values before we start this
//        // so we can get an accurate min/max for paritioning
//        partition.resetNodes();
//        for (LayoutNode v : partition.getNodeList()) {
//            partition.moveNodeToLocation(v);
//        }

            // all nodes are being laid-out so this isn't necessary

            // Not quite done, yet.  If we're only laying out selected nodes, we need
            // to migrate the selected nodes back to their starting position
            double xDelta = 0.0;
            double yDelta = 0.0;
//        final LayoutPoint finalLocation = partition.getAverageLocation();

            // the average coordinates are computed by iterating through all of the
            // nodes and adding their coordinates divided by the amount of nodes.
            averageX = 0;
            averageY = 0;
            for (int i = 0; i < component.size(); i++) {
                averageX += nodeXCoordinates[i] / component.size();
                averageY += nodeYCoordinates[i] / component.size();
            }
            double finalLocationX = averageX;
            double finalLocationY = averageY;

//        xDelta = finalLocation.getX() - initialLocation.getX();
            xDelta = finalLocationX - initialLocationX;
//        yDelta = finalLocation.getY() - initialLocation.getY();
            yDelta = finalLocationY - initialLocationY;
//        for (LayoutNode v : partition.getNodeList()) {
            for (Node v : component) {
//            if (!v.isLocked()) {
//                v.decrement(xDelta, yDelta);
//                partition.moveNodeToLocation(v);
                nodeXCoordinates[component.indexOf(v)] -= xDelta;
                nodeYCoordinates[component.indexOf(v)] -= yDelta;
//            }
            }

            // the nodes are positioned by iterating through them all.
            for (int i = 0; i < networkNodes.size(); i++) {
                if (!component.contains(networkNodes.get(i))) {
                    continue;
                }
                int nodeIndex = component.indexOf(networkNodes.get(i));
                Node node = networkNodes.get(i);
                double x = nodeXCoordinates[nodeIndex];
                double y = nodeYCoordinates[nodeIndex];
                node.setGraphicalNodeXCoordinate(x);
                node.setGraphicalNodeYCoordinate(y);
            }

        }

        // the connected components are spaced-out in rows in order of width with
        // the widest component having its own row on top, and all subsequent rows
        // being roughly the width of the top row
        layoutConnectedComponents();

    }

    /**
     * The PartialDerivatives class maintains the values for the partial
     * derivatives as they are computed.
     */
    private class PartialDerivatives {

//        final LayoutNode node;
        final Node node;
        double x;
        double y;
        double xx;
        double yy;
        double xy;
        double euclideanDistance;

//        PartialDerivatives(LayoutNode node) {
        PartialDerivatives(Node node) {
            this.node = node;
        }

        PartialDerivatives(PartialDerivatives copyFrom) {
            this.node = copyFrom.node;
            copyFrom(copyFrom);
        }

        String printPartial() {
//            String retVal = "Partials for node " + node.getIndex() + " are: " + x + "," + y + ","
//                    + xx + "," + yy + "," + xy + " dist = " + euclideanDistance;
            String retVal = "Partials for node " + component.indexOf(node) + " are: " + x + "," + y + ","
                    + xx + "," + yy + "," + xy + " dist = " + euclideanDistance;

            return retVal;
        }

        void reset() {
            x = 0.0;
            y = 0.0;
            xx = 0.0;
            yy = 0.0;
            xy = 0.0;
            euclideanDistance = 0.0;
        }

        void copyFrom(PartialDerivatives otherPartialDerivatives) {
            x = otherPartialDerivatives.x;
            y = otherPartialDerivatives.y;
            xx = otherPartialDerivatives.xx;
            yy = otherPartialDerivatives.yy;
            xy = otherPartialDerivatives.xy;
            euclideanDistance = otherPartialDerivatives.euclideanDistance;
        }
    }

    private static int[][] calculateNodeDistances() {

        int[][] distances = new int[m_nodeCount][];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        boolean[] completedNodes = new boolean[m_nodeCount];
        int toNode;
        int fromNode;
        int neighbor;
        int toNodeDistance;
        int neighborDistance;

//        for (LayoutNode v : partition.getNodeList()) {
        for (Node v : component) {
//            fromNode = v.getIndex();
            fromNode = component.indexOf(v);

            if (distances[fromNode] == null) {
                distances[fromNode] = new int[m_nodeCount];
            }

            Arrays.fill(distances[fromNode], Integer.MAX_VALUE);
            distances[fromNode][fromNode] = 0;
            Arrays.fill(completedNodes, false);
            queue.add(Integer.valueOf(fromNode));

            while (!(queue.isEmpty())) {

                int index = ((Integer) queue.removeFirst()).intValue();

                if (completedNodes[index]) {
                    continue;
                }

                completedNodes[index] = true;
                toNode = index;
                toNodeDistance = distances[fromNode][index];

                if (index < fromNode) {
                    // Oh boy.  We've already got every distance from/to this node.
                    int distanceThroughToNode;

                    for (int i = 0; i < m_nodeCount; i++) {
                        if (distances[index][i] == Integer.MAX_VALUE) {
                            continue;
                        }

                        distanceThroughToNode = toNodeDistance + distances[index][i];

                        if (distanceThroughToNode <= distances[fromNode][i]) {
                            // Any immediate neighbor of a node that's already been
                            // calculated for that does not already have a shorter path
                            // calculated from fromNode never will, and is thus complete.
                            if (distances[index][i] == 1) {
                                completedNodes[i] = true;
                            }

                            distances[fromNode][i] = distanceThroughToNode;
                        }
                    }

                    // End for every node, update the distance using the distance
                    // from toNode.  So now we don't need to put any neighbors on the
                    // queue or anything, since they've already been taken care of by
                    // the previous calculation.
                    continue;
                } // End if toNode has already had all of its distances calculated.

//                List<LayoutNode> neighborList = v.getNeighbors();
                // create an ArrayList of the neighbours of v (a node) by
                // iterating through the edges of v and adding the node
                // connected to v by each edge
                List<Node> neighborList = new ArrayList<Node>();
                for (Edge edge : v.edges) {

                    if (!component.contains(edge.node1) || !component.contains(edge.node2)) {
                        continue;
                    }

                    // the if statements check which node connected by the edge
                    // is v and add the other node to the list
                    if (edge.node1 == v) {
                        neighborList.add(edge.node2);
                    } else if (edge.node2 == v) {
                        neighborList.add(edge.node1);
                    }

                }

//                for (LayoutNode neighbor_v : neighborList) {
                for (Node neighbor_v : neighborList) {
//                    neighbor = neighbor_v.getIndex();
                    neighbor = component.indexOf(neighbor_v);

                    // We've already done everything we can here.
                    if (completedNodes[neighbor]) {
                        continue;
                    }

                    neighborDistance = distances[fromNode][neighbor];

                    if ((toNodeDistance != Integer.MAX_VALUE)
                            && (neighborDistance > (toNodeDistance + 1))) {
                        distances[fromNode][neighbor] = toNodeDistance + 1;
                        queue.addLast(Integer.valueOf(neighbor));
                    }
                }
            }
        }

        return distances;
    }

//    private static void calculateSpringData(int[][] nodeDistances) {
    private void calculateSpringData(int[][] nodeDistances) {

        // Set all springs to the default
        for (int node_i = 0; node_i < m_nodeCount; node_i++) {
            Arrays.fill(m_nodeDistanceSpringRestLengths[node_i],
                    m_disconnectedNodeDistanceSpringRestLength);
            Arrays.fill(m_nodeDistanceSpringStrengths[node_i],
                    m_disconnectedNodeDistanceSpringStrength);
        }

        // Calculate rest lengths and strengths based on node distance data.
//        for (LayoutEdge edge : partition.getEdgeList()) {
        for (Edge edge : networkEdges) {

            if (!component.contains(edge.node1) || !component.contains(edge.node2)) {
                continue;
            }

//            int node_i = edge.getSource().getIndex();
            int node_i = component.indexOf(edge.node1);
//            int node_j = edge.getTarget().getIndex();
            int node_j = component.indexOf(edge.node2);
//            double weight = context.unweighted ? edgeWeighter.defaultEdgeWeight : edge.getWeight();
            double weight = edgeWeights[edgesWithinComponent.indexOf(edge)];

            // System.out.println(edge);
            if (nodeDistances[node_i][node_j] != Integer.MAX_VALUE) {
                // Compute spring rest lengths.
                m_nodeDistanceSpringRestLengths[node_i][node_j] = (m_nodeDistanceRestLengthConstant * nodeDistances[node_i][node_j]) / (weight);
                m_nodeDistanceSpringRestLengths[node_j][node_i] = m_nodeDistanceSpringRestLengths[node_i][node_j];
                // System.out.println("Setting spring ("+node_i+","+node_j+") ["+weight+"] length to "+m_nodeDistanceSpringRestLengths[node_j][node_i]);
                // Compute spring strengths.
                m_nodeDistanceSpringStrengths[node_i][node_j] = m_nodeDistanceStrengthConstant / (nodeDistances[node_i][node_j] * nodeDistances[node_i][node_j]);
                m_nodeDistanceSpringStrengths[node_j][node_i] = m_nodeDistanceSpringStrengths[node_i][node_j];

                // System.out.println("Setting spring ("+node_i+","+node_j+") strength to "+m_nodeDistanceSpringStrengths[node_j][node_i]);
            }
        }
    }

    private static PartialDerivatives calculatePartials(PartialDerivatives partials, List partialsList,
            double[] potentialEnergy, boolean reversed) {

        partials.reset();

//        LayoutNode node = partials.node;
        Node node = partials.node;

        // How does this ever get to be > 0?
        // Get the node size from the nodeView?
//        double nodeRadius = node.getWidth() / 2;
        // 60 is the width which Cytoscape gives to its nodes
        double nodeRadius = 60 / 2;

//        double nodeX = node.getX();
//        double nodeY = node.getY();
        // intialize nodeX and nodeY with the coordinates of the node.
        int i = component.indexOf(node);
        double nodeX = nodeXCoordinates[i];
        double nodeY = nodeYCoordinates[i];

        PartialDerivatives otherPartials = null;
//        LayoutNode otherNode;
        Node otherNode;
        double otherNodeRadius;
        PartialDerivatives furthestPartials = null;
        Iterator iterator;

        if (partialsList == null) {
//            iterator = partition.nodeIterator();
            iterator = component.iterator();
        } else {
            iterator = partialsList.iterator();
        }

        double deltaX;
        double deltaY;
        double otherNodeX;
        double otherNodeY;
        double euclideanDistance;
        double euclideanDistanceCubed;
        double distanceFromRest;
        double distanceFromTouching;
        double incrementalChange;
        double[] xTable = {.01, .01, -.01, -.01};
        double[] yTable = {.01, -.01, .01, -.01};
        int offsetTable = 0;
//        int nodeIndex = node.getIndex();
        int nodeIndex = component.indexOf(node);

        while (iterator.hasNext()) {

            if (partialsList == null) {
//                otherNode = (LayoutNode) iterator.next();
                otherNode = (Node) iterator.next();
            } else {
                otherPartials = (PartialDerivatives) iterator.next();
                otherNode = otherPartials.node;
            }

            if (node == otherNode) {
                continue;
            }

            // How does this every get to be > 0?
            // Get the node size from the nodeView?
//            otherNodeRadius = otherNode.getWidth() / 2;
            // 60 is the width which Cytoscape gives to its nodes
            otherNodeRadius = 60 / 2;

//            otherNodeX = otherNode.getX();
//            otherNodeY = otherNode.getY();
            // intialize nodeX and nodeY with the coordinates of the node.
            i = component.indexOf(otherNode);
            otherNodeX = nodeXCoordinates[i];
            otherNodeY = nodeYCoordinates[i];

            deltaX = nodeX - otherNodeX;
            deltaY = nodeY - otherNodeY;
            euclideanDistance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

            if (((float) euclideanDistance) < 0.0001) {
                otherNodeX = otherNodeX + xTable[offsetTable];
                otherNodeY = otherNodeY + yTable[offsetTable++];

                if (offsetTable > 3) {
                    offsetTable = 0;
                }

//                otherNode.setX(otherNodeX);
//                otherNode.setY(otherNodeY);
                // the position of otherNode is set.
                i = component.indexOf(otherNode);
                nodeXCoordinates[i] = otherNodeX;
                nodeYCoordinates[i] = otherNodeY;

                euclideanDistance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

            }

            /*
             System.out.println("nodeX = "+nodeX);
             System.out.println("nodeY = "+nodeY);
             System.out.println("otherNodeX = "+otherNode.getX());
             System.out.println("otherNodeY = "+otherNode.getY());
             */
//            int otherNodeIndex = otherNode.getIndex();
            int otherNodeIndex = component.indexOf(otherNode);
            double radius = nodeRadius + otherNodeRadius;

            euclideanDistanceCubed = euclideanDistance * euclideanDistance * euclideanDistance;
            distanceFromTouching = euclideanDistance - (nodeRadius + otherNodeRadius);
            distanceFromRest = (euclideanDistance
                    - m_nodeDistanceSpringRestLengths[nodeIndex][otherNodeIndex]);

            // calculationProfile.start();
            if (!reversed) {
                partials.x += calculateSpringPartial(m_layoutPass, distanceFromTouching, nodeIndex,
                        otherNodeIndex, euclideanDistance, deltaX,
                        radius);
                partials.y += calculateSpringPartial(m_layoutPass, distanceFromTouching, nodeIndex,
                        otherNodeIndex, euclideanDistance, deltaY,
                        radius);
                partials.xx += calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                        nodeIndex, otherNodeIndex,
                        euclideanDistanceCubed, deltaY * deltaY,
                        radius);
                partials.yy += calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                        nodeIndex, otherNodeIndex,
                        euclideanDistanceCubed, deltaX * deltaX,
                        radius);
                partials.xy += calculateSpringPartialCross(m_layoutPass, distanceFromTouching,
                        nodeIndex, otherNodeIndex,
                        euclideanDistanceCubed, deltaX * deltaY,
                        radius);
                potentialEnergy[0] += calculatePE(m_layoutPass, distanceFromRest,
                        distanceFromTouching, nodeIndex, otherNodeIndex);
            }

            if (otherPartials != null) {
                if (!reversed) {
                    otherPartials.x += calculateSpringPartial(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistance, -deltaX, radius);
                    otherPartials.y += calculateSpringPartial(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistance, -deltaY, radius);
                    otherPartials.xx += calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistanceCubed,
                            deltaY * deltaY, radius);
                    otherPartials.yy += calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistanceCubed,
                            deltaX * deltaX, radius);
                    otherPartials.xy += calculateSpringPartialCross(m_layoutPass,
                            distanceFromTouching,
                            nodeIndex, otherNodeIndex,
                            euclideanDistanceCubed,
                            deltaX * deltaY, radius);
                    potentialEnergy[0] += calculatePE(m_layoutPass, distanceFromRest,
                            distanceFromTouching, nodeIndex,
                            otherNodeIndex);
                } else {
                    otherPartials.x -= calculateSpringPartial(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistance, -deltaX, radius);
                    otherPartials.y -= calculateSpringPartial(m_layoutPass, distanceFromTouching,
                            otherNodeIndex, nodeIndex,
                            euclideanDistance, -deltaY, radius);
                    otherPartials.xx -= calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                            nodeIndex, otherNodeIndex,
                            euclideanDistanceCubed,
                            deltaY * deltaY, radius);
                    otherPartials.yy -= calculateSpringPartial3(m_layoutPass, distanceFromTouching,
                            nodeIndex, otherNodeIndex,
                            euclideanDistanceCubed,
                            deltaX * deltaX, radius);
                    otherPartials.xy -= calculateSpringPartialCross(m_layoutPass,
                            distanceFromTouching,
                            nodeIndex, otherNodeIndex,
                            euclideanDistanceCubed,
                            deltaX * deltaY, radius);
                    potentialEnergy[0] -= calculatePE(m_layoutPass, distanceFromRest,
                            distanceFromTouching, nodeIndex,
                            otherNodeIndex);
                }

                // Update the euclidean distance
                otherPartials.euclideanDistance = Math.sqrt((otherPartials.x * otherPartials.x)
                        + (otherPartials.y * otherPartials.y));

                if ((furthestPartials == null)
                        || (otherPartials.euclideanDistance > furthestPartials.euclideanDistance)) {
                    furthestPartials = otherPartials;
                }
            }

            // calculationProfile.checkpoint();
        } // end of while loop

        if (!reversed) {
            partials.euclideanDistance = Math.sqrt((partials.x * partials.x)
                    + (partials.y * partials.y));
        }

        if ((furthestPartials == null)
                || (partials.euclideanDistance > furthestPartials.euclideanDistance)) {
            furthestPartials = partials;
        }

        return furthestPartials;
    }

    // this comment is from Cytoscape and may not be entirely applicable here
    /**
     * Here is the code for the partial derivative solver. Note that for
     * clarity, it has been devided into four parts: calculatePartials -- main
     * algorithm, calls the other three parts calculateSpringPartial -- computes
     * the first part of the spring partial (partial.x, partial.y)
     * calculateSpringPartial3 -- computes the second part of the partial
     * (partial.xx, partial.yy) calculateSpringPartialCross -- computes the
     * final part of the partial (partial.xy) calculatePE -- computes the
     * potential energy
     */
    // used to calculate the x and y portions of the partial
    private static double calculateSpringPartial(int pass, double distToTouch, int nodeIndex,
            int otherNodeIndex, double eucDist, double value,
            double radius) {
        double incrementalChange = (m_nodeDistanceSpringScalars[pass] * (m_nodeDistanceSpringStrengths[nodeIndex][otherNodeIndex] * (value
                - ((m_nodeDistanceSpringRestLengths[nodeIndex][otherNodeIndex] * value) / eucDist))));

        if (distToTouch < 0.0) {
            incrementalChange += (m_anticollisionSpringScalars[pass] * (m_anticollisionSpringStrength * (value
                    - ((radius * value) / eucDist))));
        }

        return incrementalChange;
    }

    // used to calculate the xx and yy portions of the partial
    private static double calculateSpringPartial3(int pass, double distToTouch, int nodeIndex,
            int otherNodeIndex, double eucDist3, double value,
            double radius) {
        double incrementalChange = (m_nodeDistanceSpringScalars[pass] * (m_nodeDistanceSpringStrengths[nodeIndex][otherNodeIndex] * (1.0
                - ((m_nodeDistanceSpringRestLengths[nodeIndex][otherNodeIndex] * value) / eucDist3))));

        if (distToTouch < 0.0) {
            incrementalChange += (m_anticollisionSpringScalars[m_layoutPass] * (m_anticollisionSpringStrength * (1.0
                    - ((radius * value) / eucDist3))));
        }

        return incrementalChange;
    }

    // used to calculate the xy portion of the partial
    private static double calculateSpringPartialCross(int pass, double distToTouch, int nodeIndex,
            int otherNodeIndex, double eucDist3, double value,
            double radius) {
        double incrementalChange = (m_nodeDistanceSpringScalars[pass] * (m_nodeDistanceSpringStrengths[nodeIndex][otherNodeIndex] * ((m_nodeDistanceSpringRestLengths[nodeIndex][otherNodeIndex] * value) / eucDist3)));

        if (distToTouch < 0.0) {
            incrementalChange += ((m_anticollisionSpringScalars[m_layoutPass] * (m_anticollisionSpringStrength * radius * value)) / eucDist3);
        }

        return incrementalChange;
    }

    // Calculate the potential energy
    private static double calculatePE(int pass, double distToRest, double distToTouch, int nodeIndex,
            int otherNodeIndex) {
        double incrementalChange = (m_nodeDistanceSpringScalars[pass] * ((m_nodeDistanceSpringStrengths[nodeIndex][otherNodeIndex] * (distToRest * distToRest)) / 2));

        if (distToTouch < 0.0) {
            incrementalChange += (m_anticollisionSpringScalars[pass] * ((m_anticollisionSpringStrength * (distToTouch * distToTouch)) / 2));
        }

        return incrementalChange;
    }

    private static PartialDerivatives moveNode(PartialDerivatives partials, List partialsList,
            double[] potentialEnergy) {
        PartialDerivatives startingPartials = new CytoscapeEdgeWeightedSpringEmbeddedVisualisationLayout().new PartialDerivatives(partials);
        calculatePartials(partials, partialsList, potentialEnergy, true);

        // System.out.println(partials.printPartial()+" potentialEnergy = "+potentialEnergy[0]);
        try {
            simpleMoveNode(startingPartials);
        } catch (Exception e) {
            System.out.println(e);
        }

        return calculatePartials(partials, partialsList, potentialEnergy, false);
    }

    private static void simpleMoveNode(PartialDerivatives partials) {
//        LayoutNode node = partials.node;
        Node node = partials.node;

//        if (node.isLocked()) {
//            return;
//        }

        double denominator = ((partials.xx * partials.yy) - (partials.xy * partials.xy));

        if (((float) denominator) == 0.0) {
            return;

            // throw new RuntimeException("denominator too close to 0 for node "+node);
        }

        // System.out.println(partials.printPartial());
        double deltaX = (((-partials.x * partials.yy) - (-partials.y * partials.xy)) / denominator);
        double deltaY = (((-partials.y * partials.xx) - (-partials.x * partials.xy)) / denominator);
        /* System.out.println("Moving node "+node.getIdentifier()+" from "+node.getX()+", "+node.getY()+
         " to "+(node.getX()+deltaX)+", "+(node.getY()+deltaY)); */
//        node.setLocation(node.getX() + deltaX, node.getY() + deltaY);
        // set the position of node, increasing its coordinates by deltaX and deltaY.
        int i = component.indexOf(node);
        nodeXCoordinates[i] = nodeXCoordinates[i] + deltaX;
        nodeYCoordinates[i] = nodeYCoordinates[i] + deltaY;

    }

    // the connected components are spaced-out in rows in order of area with
    // from largest to smallest.
    // the layout is approximately as square as possible.
    public void layoutConnectedComponents() {

        // this ArrayList will contain all the connected components in order of
        // area from largest to smallest
        ArrayList<ArrayList<Node>> connectedComponents = new ArrayList<ArrayList<Node>>();

        // create an array.
        // one element for each node.
        // each element contains whether the corresponding node's connected
        // component has been added to connectedComponents.
        // the purpose of the array is to avoid computing the components more
        // than once for different nodes in the same component.
        // the elements are initialized to false by default.
        boolean[] componentFound = new boolean[networkNodes.size()];

        double[] componentTopMostYs = new double[networkNodes.size()];
        double[] componentRightMostXs = new double[networkNodes.size()];
        double[] componentBottomMostYs = new double[networkNodes.size()];
        double[] componentLeftMostXs = new double[networkNodes.size()];

        double componentsTotalWidth = 0;
        double componentsTotalArea = 0;
        // iterate through each node
        for (int i = 0; i < networkNodes.size(); i++) {
            // if the node's component has not been found
            if (!componentFound[i]) {

                // get the node's connected component
                ArrayList<Node> component = new ArrayList<Node>();
                component = getConnectedComponent(component, networkNodes.get(i));

                // find the nodes coordinates and initialize currentComponentTopMostY,
                // currentComponentRightMostX, currentComponentBottomMostY, and
                // currentComponentLeftMostX with them.
                // these variables will hold the top-most y, right-most x,
                // bottom-most y, and left-most x coordinates of the connected
                // component.
                Node node = networkNodes.get(i);
                double currentComponentTopMostY = node.getGraphicalNodeYCoordinate();
                double currentComponentRightMostX = node.getGraphicalNodeXCoordinate();
                double currentComponentBottomMostY = node.getGraphicalNodeYCoordinate();
                double currentComponentLeftMostX = node.getGraphicalNodeXCoordinate();
                // currentComponentTopMostY, currentComponentRightMostX,
                // currentComponentBottomMostY, and currentComponentLeftMostX
                // are updated by iterating through the nodes of the component.
                // the index, j, starts at one because the first node has
                // been considered.
                for (int j = 1; j < component.size(); j++) {
                    node = component.get(j);
                    currentComponentTopMostY = Math.min(currentComponentTopMostY, node.getGraphicalNodeYCoordinate());
                    currentComponentRightMostX = Math.max(currentComponentRightMostX, node.getGraphicalNodeXCoordinate());
                    currentComponentBottomMostY = Math.max(currentComponentBottomMostY, node.getGraphicalNodeYCoordinate());
                    currentComponentLeftMostX = Math.min(currentComponentLeftMostX, node.getGraphicalNodeXCoordinate());
                }

                // the componentFound array and the coordinates arrays are
                // updated for all nodes in the component
                for (int j = 0; j < component.size(); j++) {
                    componentFound[networkNodes.indexOf(component.get(j))] = true;
                    componentTopMostYs[networkNodes.indexOf(component.get(j))] = currentComponentTopMostY;
                    componentRightMostXs[networkNodes.indexOf(component.get(j))] = currentComponentRightMostX;
                    componentBottomMostYs[networkNodes.indexOf(component.get(j))] = currentComponentBottomMostY;
                    componentLeftMostXs[networkNodes.indexOf(component.get(j))] = currentComponentLeftMostX;
                }

                // the width of the component is added to componentsTotalWidth.
                // the additional 100 is the space between components and is
                // also included to make calculations more accurate.
                componentsTotalWidth += currentComponentRightMostX - currentComponentLeftMostX + 100;
                // the area of the component is added to componentsTotalArea.
                // the additional 100 is the space between components and is
                // included to make calculations more accurate.
                componentsTotalArea += (currentComponentBottomMostY - currentComponentTopMostY + 100) * (currentComponentRightMostX - currentComponentLeftMostX + 100);

                // the component is added to the ArrayList of components in
                // order of amount of nodes from largest to smallest amount of
                // nodes
                int j = 0;
                while (j < connectedComponents.size()) {
                    // if the amount of nodes of the current component is less
                    // than the amount of nodes of the component at index j in
                    // connectedComponents, j is increased and the loop is
                    // repeated, otherwise, the loop is broken out of.
                    // this is because the current component should be inserted
                    // in connectedComponents in order of largest amount of
                    // nodes to smallest amount of nodes.
                    if (component.size() < connectedComponents.get(j).size()) {
                        j++;
                    } else {
                        break;
                    }
                }
                connectedComponents.add(j, component);

            }
        }

        // if the current network has been clustered
        if (networkClusters != null) {
            // connectedComponents is populated with the clusters.
            // this is done so that the clusters are laid-out in the
            // same order as they are displayed in the Network Clusters
            // dialog and elsewhere.
            connectedComponents.clear();
            for (Cluster cluster : networkClusters) {
                connectedComponents.add((ArrayList<Node>) cluster.nodes.clone());
            }
        }

        // the average width which each row of components should have is
        // calculated by finding a number which gives the same result when
        // multiplying the average height of the components by it, and when
        // dividing the total width of the components by it.
        // this number represents the amount of rows of components in the layout
        // and is contained in approximateRowAmount.
        // the reason this way of calculating the approximate width is useful,
        // is that the approximate width of each row of components should be
        // similar to the approximate height of all the rows (so the components
        // are laid-out as square as possible).
        double averageComponentWidth = componentsTotalWidth / connectedComponents.size();
        // the value of componentsTotalHeightAdjusted is not exactly the total
        // height of all the components.
        // if there are many components with smaller widths than
        // averageComponentWidth, componentsTotalHeightAdjusted will be larger
        // than the real total height and vice versa.
        double componentsTotalHeightAdjusted = componentsTotalArea / averageComponentWidth;
        double averageComponentHeightAdjusted = componentsTotalHeightAdjusted / connectedComponents.size();
        double approximateRowAmount = Math.sqrt(componentsTotalWidth / averageComponentHeightAdjusted);
        double averageComponentRowWidth = componentsTotalWidth / approximateRowAmount;

        // all the components are laid out in rows.
        // currentComponentLeftMostX contains the left-most x coordinate which
        // the current component should have, its left edge should be inline
        // with this coordinate.
        double currentComponentLeftMostX = 0;
        // currentRowBottomMostY contains the bottom-most y coordinate of all the
        // connected components on the current row.
        // this indicates where the next row of components should be placed
        // (100 pixels below).
        double currentRowBottomMostY = 0;
        // currentRowTopMostY is a variable which keeps track of where the
        // components in the current row should be placed.
        // each component should have its top edge inline with this coordinate.
        double currentRowTopMostY = 0;

        // iterate through all the connected components, and lay them out.
        for (int i = 0; i < connectedComponents.size(); i++) {

            // the index of the first node in the component
            int firstNodeIndex = networkNodes.indexOf(connectedComponents.get(i).get(0));

            // if, when the current component is laid-out in the current row,
            // the row will be too wide, a new row of components is begun
            double currentComponentWidth = componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex];
            // rowWidth + currentComponentWidth / 2 is used instead of just
            // rowWidth, so that the average width of the rows will tend more
            // towards rowWidth rather than all the rows being less than
            // rowWidth
            if (currentComponentLeftMostX + currentComponentWidth > averageComponentRowWidth + currentComponentWidth / 2) {
                currentComponentLeftMostX = 0;
                currentRowTopMostY = currentRowBottomMostY + 100;
            }

            // if the current component has an equal or smaller area to 1 tenth
            // of the previous component's area, a new row of components is
            // begun.
            // this simply separates the components into groups based on their
            // area.
            if (i > 0) {
                // 100 is added to the width and height in computing the area.
                // this is mainly to stop components with one node and no area
                // starting a new line every time.
                double currentComponentArea = (componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex] + 100) * (componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex] + 100);
                int firstNodePreviousComponentIndex = networkNodes.indexOf(connectedComponents.get(i - 1).get(0));
                double previousComponentArea = (componentRightMostXs[firstNodePreviousComponentIndex] - componentLeftMostXs[firstNodePreviousComponentIndex] + 100) * (componentBottomMostYs[firstNodePreviousComponentIndex] - componentTopMostYs[firstNodePreviousComponentIndex] + 100);
                if (currentComponentArea <= previousComponentArea / 10) {
                    currentComponentLeftMostX = 0;
                    currentRowTopMostY = currentRowBottomMostY + 100;
                }
            }

            // iterate through the nodes of the component and lay them out
            // in their new positions
            for (int j = 0; j < connectedComponents.get(i).size(); j++) {

                Node node = connectedComponents.get(i).get(j);
                // currentComponentLeftMostX represents the
                // left-most x coordinate of the current component.
                // node.getGraphicalNodeXCoordinate() - componentLeftMostXs[firstNodeIndex]
                // represents the x coordinate of the current node relative to
                // the left-most x coordinate of the current component.
                // the next expression for y coordinates is analagous.
                node.setGraphicalNodeXCoordinate(currentComponentLeftMostX + node.getGraphicalNodeXCoordinate() - componentLeftMostXs[firstNodeIndex]);
                node.setGraphicalNodeYCoordinate(currentRowTopMostY + node.getGraphicalNodeYCoordinate() - componentTopMostYs[firstNodeIndex]);

            }

            // these variables are updated accordingly, ready for the next
            // component.
            // componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex]
            // gives the width of the current component.
            // the additional 100 is the gap between components.
            currentComponentLeftMostX += componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex] + 100;
            // componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex]
            // gives the height of the current component
            currentRowBottomMostY = Math.max(currentRowBottomMostY, currentRowTopMostY + componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex]);

        }
    }

    // create an ArrayList of all the nodes in the same connected
    // component as the current node.
    // this method is recursive.
    public ArrayList<Node> getConnectedComponent(ArrayList<Node> connectedComponent, Node currentNode) {

        // add the currentNode to the component
        connectedComponent.add(currentNode);

        // for each neighbour of the current node, if the neighbour has not been
        // added to the component, add it, then call this method again to check
        // all of its neighbours
        for (Edge edge : currentNode.edges) {

            // if the network has been clustered, edges between nodes which are
            // not in the same cluster should be ignored because clusters are
            // represented by edges connecting their nodes.
            // this code checks networkClusters != null, which means that the
            // network has been clustered.
            if (networkClusters != null) {
                if (edge.node1.cluster != edge.node2.cluster) {
                    continue;
                }
            }

            // the two outer if statements are to check which node is the
            // current node and which is the neighbouring node for the edge
            if (edge.node1.equals(currentNode)) {
                if (!connectedComponent.contains(edge.node2)) {
                    getConnectedComponent(connectedComponent, edge.node2);
                }
            } else if (edge.node2.equals(currentNode)) {
                if (!connectedComponent.contains(edge.node1)) {
                    getConnectedComponent(connectedComponent, edge.node1);
                }
            }
        }

        return connectedComponent;

    }
}
