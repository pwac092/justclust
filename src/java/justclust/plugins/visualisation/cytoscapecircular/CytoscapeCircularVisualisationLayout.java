/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.visualisation.cytoscapecircular;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.visualisation.VisualisationLayoutPluginInterface;

/**
 *
 * @author wuaz008
 */
public class CytoscapeCircularVisualisationLayout implements VisualisationLayoutPluginInterface {

    public String getName() throws Exception {
        return "Cytoscape circular visualisation layout";
    }

    public String getDescription() throws Exception {
        return "This visualisation layout plug-in lays-out the graphical representation of the current network with the circular layout from Cytoscape.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {
        return new ArrayList<PluginConfigurationControlInterface>();
    }
    public ArrayList<Node> networkNodes;
    public ArrayList<Edge> networkEdges;
    public ArrayList<Cluster> networkClusters;
    // this field is from
    // https://github.com/cytoscape/cytoscape-impl/blob/develop/layout-cytoscape-impl/src/main/java/csapps/layout/algorithms/circularLayout/CircularLayoutAlgorithmTask.java
    private static int[][] bc;
    private static boolean[] posSet;
    private static boolean[] depthPosSet;
    private static Map<Integer, Integer> nodeHeights;
    static LinkedList[] edgesFrom;
    private static Map<Integer, Node> nodeViews;
    private static Map<Integer, Integer> node2BiComp;
    private static boolean[] drawnBiComps;
    // the following fields are from
    // https://github.com/cytoscape/cytoscape-impl/blob/develop/layout-cytoscape-impl/src/main/java/csapps/layout/algorithms/hierarchicalLayout/Graph.java
    private static byte[] status;
    private static int[] d;
    private static int[] low;
    private static int[] pred;
    private static int time;
    private static HashMap<Integer, LinkedList<Integer>> neighbours;
    private static Stack<Edge> edgesStack;
    private static LinkedList<LinkedList<Integer>> biComponents;
    // this field exists so that the various methods are able to work with the
    // same component without passing it between each other
    private static ArrayList<Node> component;

    // this layout only affects one connected commponent of the graphical
    // representation of the current network.
    // the other connected components (if they exist) are moved to the bottom
    // of the graph and spaced out by code which can be found at the bottom of
    // this method.
    public void applyLayout(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {

        // these variables are made available here to the other methods in this
        // plug-in
        this.networkNodes = networkNodes;
        this.networkEdges = networkEdges;
        this.networkClusters = networkClusters;

        // create an array.
        // one element for each node.
        // each element contains whether the node's connected component has been
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

        for (ArrayList<Node> element : connectedComponents) {
            component = element;

//		if (cancelled)
//			return;

//		final int numNodes = partition.nodeCount();
            final int numNodes = component.size();

            if (numNodes == 1) {
                // We were asked to do a circular layout of a single node -- done!
//                return;
                continue;
            }

            nodeViews = new HashMap<Integer, Node>(numNodes);

//		Map<CyNode, Integer> nodeIdexMap = new HashMap<CyNode, Integer>();
            Map<Node, Integer> nodeIdexMap = new HashMap<Node, Integer>();
            int nodeIndex = 0;

            // populate the nodeViews and nodeIdexMap HashMaps with the
            // indexes of the nodes
//		Iterator<LayoutNode> nodeIter = partition.getNodeList().iterator();
            Iterator<Node> nodeIter = component.iterator();
//		while (nodeIter.hasNext() && !cancelled) {
            while (nodeIter.hasNext()) {
//			final View<CyNode> nv = nodeIter.next().getNodeView();
                final Node nv = nodeIter.next();
                nodeViews.put(nodeIndex, nv);
//			nodeIdexMap.put(nv.getModel(), nodeIndex);
                nodeIdexMap.put(nv, nodeIndex);
                nodeIndex++;
            }

//		if (cancelled)
//			return;

            /* create edge list from edges between selected nodes */
            final List<Edge> edges = new LinkedList<Edge>();
//		final Iterator<LayoutEdge> edgeIter = partition.edgeIterator();
            ArrayList<Edge> edgesWithinComponent = new ArrayList<Edge>();
            for (Edge edge : networkEdges) {
                if (component.contains(edge.node1) && component.contains(edge.node2)) {
                    edgesWithinComponent.add(edge);
                }
            }
            final Iterator<Edge> edgeIter = edgesWithinComponent.iterator();
//		while (edgeIter.hasNext() && !cancelled) {
            while (edgeIter.hasNext()) {
//			final LayoutEdge ev = edgeIter.next();
                final Edge ev = edgeIter.next();
                if (!component.contains(ev.node1) && !component.contains(ev.node2)) {
                    continue;
                }
//			final Integer edgeFrom = nodeIdexMap.get(ev.getEdge().getSource());
                final Integer edgeFrom = nodeIdexMap.get(ev.node1);
//			final Integer edgeTo = nodeIdexMap.get(ev.getEdge().getTarget());
                final Integer edgeTo = nodeIdexMap.get(ev.node2);

                // if the network has been clustered, edges between nodes which are
                // not in the same cluster should be ignored because clusters are
                // represented by edges connecting their nodes.
                // this code checks networkClusters != null, which means that the
                // network has been clustered.
                if (networkClusters != null) {
                    if (ev.node1.cluster != ev.node2.cluster) {
                        continue;
                    }
                }

                // this code is not needed because all of the nodes are
                // to be laid-out, and this check is for nodes which
                // will not be laid-out.
//			if ((edgeFrom == null) || (edgeTo == null))
//				continue;

//			edges.add(new Edge(edgeFrom, edgeTo));
                Edge edge = new Edge();
                edge.node1 = ev.node1;
                edge.node2 = ev.node2;
                edges.add(edge);
//			edges.add(new Edge(edgeTo, edgeFrom));
                edge = new Edge();
                edge.node1 = ev.node2;
                edge.node2 = ev.node1;
                edges.add(edge);
            }
            nodeIdexMap.clear();
            nodeIdexMap = null;
//		if (cancelled)
//			return;

            /* find horizontal and vertical coordinates of each node */
            final Edge[] edge = new Edge[edges.size()];
            edges.toArray(edge);

//		final Graph graph = new Graph(numNodes, edge);
            // the following code came from the first constructor of
            // https://github.com/cytoscape/cytoscape-impl/blob/develop/layout-cytoscape-impl/src/main/java/csapps/layout/algorithms/hierarchicalLayout/Graph.java .
            // it initializes some of the global fields which are used later in
            // this method and other methods called from this method.
            status = new byte[numNodes];
            d = new int[numNodes];
            low = new int[numNodes];
            pred = new int[numNodes];
            time = 0;
            neighbours = new HashMap<Integer, LinkedList<Integer>>();
            edgesStack = new Stack<Edge>();
            biComponents = new LinkedList<LinkedList<Integer>>();

//		if (cancelled)
//			return;

//		posSet = new boolean[nodeViews.size()]; // all false
            posSet = new boolean[component.size()]; // all false
//		depthPosSet = new boolean[nodeViews.size()]; // all false
            depthPosSet = new boolean[component.size()]; // all false

            bc = biconnectedComponents();

            int maxSize = -1;
            int maxIndex = -1;

            for (int i = 0; i < bc.length; i++) {
                if (bc[i].length > maxSize) {
                    maxSize = bc[i].length;
                    maxIndex = i;
                }
            }

            if (maxIndex == -1) {
//                return;
                continue;
            }

//            if (cancelled) {
//                return;
//            }

            drawnBiComps = new boolean[bc.length];
            node2BiComp = new HashMap<Integer, Integer>();

            for (int i = 0; i < bc.length; i++) {
                if (bc[i].length > 3) {
                    for (int j = 0; j < bc[i].length; j++) {
                        node2BiComp.put(bc[i][j], i);
                    }
                }
            }

            final double radius = (48 * maxSize) / (2 * Math.PI);
            final double deltaAngle = (2 * Math.PI) / maxSize;
            double angle = 0;

            int startX = (int) radius;
            int startY = (int) radius;

//            edgesFrom = graph.GetEdgesFrom();
            // the following code came from the first constructor of
            // https://github.com/cytoscape/cytoscape-impl/blob/develop/layout-cytoscape-impl/src/main/java/csapps/layout/algorithms/hierarchicalLayout/Graph.java .
            // it creates the edgesFrom LinkedList which is an array of
            // LinkedLists.
            // each element (a LinkedList) of the array corresponds to a node.
            // each element of that element (the LinkedList) corresponds to a
            // a node which the original node is connect to by an edge.
            edgesFrom = new LinkedList[component.size()];
            int x;
            for (x = 0; x < component.size(); x++) {
                edgesFrom[x] = new LinkedList();
            }
            for (x = 0; x < edges.size(); x++) {

                int edgeFrom = component.indexOf(edges.get(x).node1);
                int edgeTo = component.indexOf(edges.get(x).node2);

                // if the network has been clustered, edges between nodes which are
                // not in the same cluster should be ignored because clusters are
                // represented by edges connecting their nodes.
                // this code checks networkClusters != null, which means that the
                // network has been clustered.
                if (networkClusters != null) {
                    if (edges.get(x).node1.cluster != edges.get(x).node2.cluster) {
                        continue;
                    }
                }

                edgesFrom[edgeFrom].add(Integer.valueOf(edgeTo));
                // the edges are considered to be bi-directional here so that the code
                // after this point works
                edgesFrom[edgeTo].add(Integer.valueOf(edgeFrom));

            }

            // sorting nodes on inner circle
            bc[maxIndex] = SortInnerCircle(bc[maxIndex]);

            // setting nodes on inner circle
            for (int i = 0; i < bc[maxIndex].length; i++) {
                setOffset(nodeViews.get(bc[maxIndex][i]),
                        startX + (Math.cos(angle) * radius),
                        startY - (Math.sin(angle) * radius));
                posSet[bc[maxIndex][i]] = true;

                angle += deltaAngle;
            }

            drawnBiComps[maxIndex] = true;

            nodeHeights = new HashMap<Integer, Integer>();

            SetOuterCircle(maxIndex, radius, startX, startY, -1);

//            if (cancelled) {
//                return;
//            }

////            nodeIter = partition.nodeIterator();
//            nodeIter = networkNodes.iterator();
//
////            while (nodeIter.hasNext() && !cancelled) {
//            while (nodeIter.hasNext()) {
//                final Node ln = nodeIter.next();
//                final View<CyNode> nv = ln.getNodeView();
//                ln.setX(nv.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION));
//                ln.setY(nv.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION));
//                partition.moveNodeToLocation(ln);
//            }

        }

        // the connected components are spaced-out in rows in order of area with
        // from largest to smallest.
        // the layout is approximately as square as possible.
        layoutConnectedComponents();

    }

    /**
     * Function which calculates the biconnected components of the given graph
     *
     * @return
     */
//    public static int[][] biconnectedComponents() {
    public int[][] biconnectedComponents() {

        int nodecount = component.size();

        for (int i = 0; i < nodecount; i++) {
            neighbours.put(Integer.valueOf(i), new LinkedList<Integer>());
        }

        ArrayList<Edge> edgesWithinComponent = new ArrayList<Edge>();
        for (Edge edge : networkEdges) {
            if (component.contains(edge.node1) && component.contains(edge.node2)) {
                edgesWithinComponent.add(edge);
            }
        }
        Edge[] edge = new Edge[edgesWithinComponent.size()];
        edge = edgesWithinComponent.toArray(edge);

        for (int i = 0; i < edge.length; i++) {

            // if the network has been clustered, edges between nodes which are
            // not in the same cluster should be ignored because clusters are
            // represented by edges connecting their nodes.
            // this code checks networkClusters != null, which means that the
            // network has been clustered.
            if (networkClusters != null) {
                if (edge[i].node1.cluster != edge[i].node2.cluster) {
                    continue;
                }
            }

//            neighbours.get(Integer.valueOf(edge[i].getFrom())).add(Integer.valueOf(edge[i].getTo()));
            neighbours.get(Integer.valueOf(component.indexOf(edge[i].node1))).add(Integer.valueOf(component.indexOf(edge[i].node2)));
            // the edges are made to be bi-directional here so that the code
            // after this point works
            neighbours.get(Integer.valueOf(component.indexOf(edge[i].node2))).add(Integer.valueOf(component.indexOf(edge[i].node1)));
        }

        for (int i = 0; i < nodecount; i++) {
            status[i] = 0;
        }

        //time = 0;
        pred[0] = -1;

        ArtPoints(0);

        int[][] bc = new int[biComponents.size()][];

        for (int i = 0; i < biComponents.size(); i++) {
            bc[i] = new int[biComponents.get(i).size()];

            for (int j = 0; j < biComponents.get(i).size(); j++) {
                bc[i][j] = biComponents.get(i).get(j).intValue();
            }
        }

        return bc;
    }

    private static void ArtPoints(int current) {

        status[current] = 1;
        low[current] = d[current] = ++time;

        int neigh;
        Iterator iter = neighbours.get(current).iterator();

        while (iter.hasNext()) {
            neigh = ((Integer) (iter.next())).intValue();

            if (status[neigh] == 0) {
                pred[neigh] = current;
//                edgesStack.push(new Edge(current, neigh));
                Edge edge = new Edge();
                edge.node1 = component.get(current);
                edge.node2 = component.get(neigh);
                edgesStack.push(edge);
                ArtPoints(neigh);
                low[current] = Math.min(low[current], low[neigh]);

                if (pred[current] == -1) {
                    Iterator rootChildren = neighbours.get(Integer.valueOf(current)).iterator();
                    int noChildren = 0;

                    while (rootChildren.hasNext()) {
                        //if (pred[((Integer)rootChildren.next()).intValue()] == current)
                        noChildren++;
                        rootChildren.next();
                    }

                    if (noChildren >= 2) {
                        LinkedList<Integer> singleComponent = new LinkedList<Integer>();
//                        singleComponent.add(Integer.valueOf(edgesStack.peek().getTo()));
                        singleComponent.add(Integer.valueOf(component.indexOf(edgesStack.peek().node2)));

//                        while (edgesStack.peek().getFrom() != current) {
                        while (component.indexOf(edgesStack.peek().node1) != current) {
                            Edge currEdge = edgesStack.pop();

//                            if (!singleComponent.contains(Integer.valueOf(currEdge.getFrom()))) {
                            if (!singleComponent.contains(Integer.valueOf(component.indexOf(currEdge.node1)))) {
//                                singleComponent.add(Integer.valueOf(currEdge.getFrom()));
                                singleComponent.add(Integer.valueOf(component.indexOf(currEdge.node1)));
                            }

//                            if (!singleComponent.contains(Integer.valueOf(currEdge.getTo()))) {
                            if (!singleComponent.contains(Integer.valueOf(component.indexOf(currEdge.node2)))) {
//                                singleComponent.add(Integer.valueOf(currEdge.getTo()));
                                singleComponent.add(Integer.valueOf(component.indexOf(currEdge.node2)));
                            }
                        }

                        edgesStack.pop();

                        if (!singleComponent.contains(Integer.valueOf(current))) {
                            singleComponent.add(Integer.valueOf(current));
                        }

                        biComponents.add(singleComponent);

                        // skini sa steka sve do art tacke to je jedna bi komp
                    }
                } else if (low[neigh] >= d[current]) {
                    LinkedList<Integer> singleComponent = new LinkedList<Integer>();
//                    singleComponent.add(Integer.valueOf(edgesStack.peek().getTo()));
                    singleComponent.add(Integer.valueOf(component.indexOf(edgesStack.peek().node2)));

//                    while (edgesStack.peek().getFrom() != current) {
                    while (component.indexOf(edgesStack.peek().node1) != current) {
                        Edge currEdge = edgesStack.pop();

//                        if (!singleComponent.contains(Integer.valueOf(currEdge.getFrom()))) {
                        if (!singleComponent.contains(Integer.valueOf(component.indexOf(currEdge.node1)))) {
//                            singleComponent.add(Integer.valueOf(currEdge.getFrom()));
                            singleComponent.add(Integer.valueOf(component.indexOf(currEdge.node1)));
                        }

//                        if (!singleComponent.contains(Integer.valueOf(currEdge.getTo()))) {
                        if (!singleComponent.contains(Integer.valueOf(component.indexOf(currEdge.node2)))) {
//                            singleComponent.add(Integer.valueOf(currEdge.getTo()));
                            singleComponent.add(Integer.valueOf(component.indexOf(currEdge.node2)));
                        }
                    }

                    edgesStack.pop();

                    if (!singleComponent.contains(Integer.valueOf(current))) {
                        singleComponent.add(Integer.valueOf(current));
                    }

                    biComponents.add(singleComponent);

                    // skini sa steka sve do art tacke to je jedna bi komp
                }

                status[neigh] = 1;
                low[neigh] = d[neigh] = ++time;
            } else if (neigh != pred[current]) {
                low[current] = Math.min(low[current], d[neigh]);
            }
        }
    }

    /**
     * Sort the nodes from biconnected component to get the best ordering in
     * terms of tree-like neighbouring patterns
     *
     * @param icNodes - nodes from biconnected component
     * @return
     */
    private static int[] SortInnerCircle(int[] icNodes) {
        LinkedList<Integer> greedyNodes = new LinkedList<Integer>();
        LinkedList<Integer> modestNodes = new LinkedList<Integer>();

        HashMap<Integer, Integer> forFunct = new HashMap<Integer, Integer>();

        for (int i = 0; i < icNodes.length; i++) {
            forFunct.put(Integer.valueOf(icNodes[i]), Integer.valueOf(0));
        }

        for (int i = 0; i < icNodes.length; i++) {
            int tmp = NoOfChildren(icNodes[i], forFunct);

            if (tmp > 4) {
                greedyNodes.add(Integer.valueOf(icNodes[i]));
            } else {
                modestNodes.add(Integer.valueOf(icNodes[i]));
            }
        }

        int[] toReturn = new int[icNodes.length];
        int gNo = greedyNodes.size();
        int mNo = modestNodes.size();
        int deltaM;
        int deltaG;

        if (gNo == 0) {
            deltaM = mNo;
            deltaG = 0;
        } else if (mNo == 0) {
            deltaG = gNo;
            deltaM = 0;
        } else if (gNo > mNo) {
            deltaM = 1;
            deltaG = gNo / mNo;
        } else {
            deltaG = 1;
            deltaM = mNo / gNo;
        }

        int x = 0;
        Iterator iterM = modestNodes.iterator();
        Iterator iterG = greedyNodes.iterator();

        while (iterM.hasNext() && iterG.hasNext()) {
            for (int i = 0; i < deltaG; i++) {
                toReturn[x++] = ((Integer) iterG.next()).intValue();
            }

            for (int i = 0; i < deltaM; i++) {
                toReturn[x++] = ((Integer) iterM.next()).intValue();
            }
        }

        while (iterG.hasNext()) {
            toReturn[x++] = ((Integer) iterG.next()).intValue();
        }

        while (iterM.hasNext()) {
            toReturn[x++] = ((Integer) iterM.next()).intValue();
        }

        return toReturn;
    }

    /**
     * Returns number of children of the specified node from outer circle. If
     * number of children larger than 7 return 7.
     *
     * @param nodeID
     * @param outerCircle
     * @return
     */
    private static int NoOfChildren(int nodeID, Map<Integer, Integer> outerCircle) {
        int toReturn = 0;
        Iterator iter = edgesFrom[nodeID].iterator();

        while (iter.hasNext()) {
            int currNeigh = ((Integer) iter.next()).intValue();

            if (!posSet[currNeigh] && !outerCircle.containsKey(currNeigh)) {
                toReturn++;
            }
        }

        if (toReturn > 7) {
            return 7;
        }

        return toReturn;
    }

    private static void setOffset(Node nv, double x, double y) {
//        nv.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
//        nv.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);
        nv.setGraphicalNodeXCoordinate(x);
        nv.setGraphicalNodeYCoordinate(y);
    }

    /**
     * Function which sets the first neighbours of nodes from circle
     * (biconnected component) on the concentric circle (larger then the first
     * circle).
     *
     * @param compIndex - index of that biconnected component in array bc
     * @param innerCircleRadius - radius of the inner cicrle
     * @param startX - start X position for drawing
     * @param startY - start Y position for drawing
     * @param firstTouched - node from that component which is found first
     */
    private static void SetOuterCircle(int compIndex, double innerCircleRadius, double startX,
            double startY, int firstTouched) {
        int outerNodesCount = 0;
        int rnc = 0;
        Iterator<Integer> iter;
        Map<Integer, Integer> outerCircle = new HashMap<Integer, Integer>();

        for (int i = 0; i < bc[compIndex].length; i++) {
            iter = edgesFrom[bc[compIndex][i]].iterator();

            while (iter.hasNext()) {
                int currNeighbour = iter.next();

                if (!posSet[currNeighbour]) {
                    outerNodesCount += (NoOfChildren(currNeighbour, outerCircle) + 1);
                    outerCircle.put(Integer.valueOf(currNeighbour), Integer.valueOf(0));
                    rnc++;
                }
            }
        }

        double outerRadius = 1.5 * innerCircleRadius;

        // + 5 * nodeHorizontalSpacing;
        int tryCount = (int) ((2 * Math.PI * outerRadius) / 32);
        double outerDeltaAngle = (2 * Math.PI) / tryCount;

        if (tryCount < (1.2 * outerNodesCount)) {
            outerRadius = (1.2 * 32 * outerNodesCount) / (2 * Math.PI);
            outerDeltaAngle = (2 * Math.PI) / (1.2 * outerNodesCount);
            outerNodesCount *= 1.2;
        } else {
            outerNodesCount = tryCount;
        }

        if ((outerNodesCount > 10) && (firstTouched != -1)) {
            outerNodesCount += 5;
        }

        // 5 places on outer circle for connection with other biconn. comp.
        //System.out.println("tryCount = " + tryCount);

        // setting nodes on outer circle
        int[] outerPositionsTaken = new int[outerNodesCount];
        int[] outerPositionsOwners = new int[outerNodesCount];

        for (int i = 0; i < outerPositionsTaken.length; i++) {
            outerPositionsTaken[i] = -1;
            outerPositionsOwners[i] = -1;
        }

        double pointX;
        double pointY;
        double theAngle;
        double theAngleHlp;
        double innerDeltaAngle;
        innerDeltaAngle = (2 * Math.PI) / bc[compIndex].length;

        if (firstTouched != -1) {
            Node view = nodeViews.get(firstTouched);
//            pointX = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
            pointX = 0;
//            pointY = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            pointY = 0;
            theAngle = Math.asin((startY - pointY) / Math.sqrt(((pointX - startX) * (pointX
                    - startX))
                    + ((pointY - startY) * (pointY
                    - startY))));
            theAngleHlp = Math.acos((pointX - startX) / Math.sqrt(((pointX - startX) * (pointX
                    - startX))
                    + ((pointY - startY) * (pointY
                    - startY))));

            if (theAngleHlp > (Math.PI / 2)) {
                theAngle = Math.PI - theAngle;
            }

            if (theAngle < 0) {
                theAngle += (2 * Math.PI);
            }

            int idPos = ((int) (theAngle / outerDeltaAngle)) % outerPositionsTaken.length;
            outerPositionsTaken[idPos] = (int) (theAngle / innerDeltaAngle);
            outerPositionsOwners[idPos] = -2; // must not be even moved because that node is coming from another bicomp.

            if (outerPositionsTaken.length > 10) {
                outerPositionsTaken[(idPos + 1) % outerPositionsTaken.length] = (int) (theAngle / innerDeltaAngle);
                outerPositionsTaken[(idPos + 2) % outerPositionsTaken.length] = (int) (theAngle / innerDeltaAngle);
                outerPositionsTaken[(idPos - 1 + outerPositionsTaken.length) % outerPositionsTaken.length] = (int) (theAngle / innerDeltaAngle);
                outerPositionsTaken[(idPos - 2 + outerPositionsTaken.length) % outerPositionsTaken.length] = (int) (theAngle / innerDeltaAngle);

                outerPositionsOwners[(idPos + 1) % outerPositionsOwners.length] = -2;
                outerPositionsOwners[(idPos + 2) % outerPositionsOwners.length] = -2;
                outerPositionsOwners[(idPos - 1 + outerPositionsOwners.length) % outerPositionsOwners.length] = -2;
                outerPositionsOwners[(idPos - 2 + outerPositionsOwners.length) % outerPositionsOwners.length] = -2;
            }
        }

        HashMap<Integer, Integer> addedNeighbours = new HashMap<Integer, Integer>();

        for (int i = 0; i < bc[compIndex].length; i++) {
            iter = edgesFrom[bc[compIndex][i]].iterator();

            int currentNeighbour;
            int noOfNeighbours = 0;

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (!posSet[currentNeighbour]) {
                    noOfNeighbours += (NoOfChildren(currentNeighbour, addedNeighbours) + 1);
                    addedNeighbours.put(Integer.valueOf(currentNeighbour), Integer.valueOf(0));
                }
            }

            if (noOfNeighbours == 0) {
                continue;
            }

//            pointX = nodeViews.get(bc[compIndex][i]).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//            pointY = nodeViews.get(bc[compIndex][i]).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            pointX = nodeViews.get(bc[compIndex][i]).getGraphicalNodeXCoordinate();
            pointY = nodeViews.get(bc[compIndex][i]).getGraphicalNodeYCoordinate();

            theAngle = Math.asin((startY - pointY) / Math.sqrt(((pointX - startX) * (pointX
                    - startX))
                    + ((pointY - startY) * (pointY
                    - startY))));
            theAngleHlp = Math.acos((pointX - startX) / Math.sqrt(((pointX - startX) * (pointX
                    - startX))
                    + ((pointY - startY) * (pointY
                    - startY))));

            if (theAngleHlp > (Math.PI / 2)) {
                theAngle = Math.PI - theAngle;
            }

            if (theAngle < 0) {
                theAngle += (2 * Math.PI);
            }

            iter = edgesFrom[bc[compIndex][i]].iterator();

            int startPos = BestFreePositionsForAll((int) ((theAngle / outerDeltaAngle)
                    - (noOfNeighbours / 2.0)), outerPositionsTaken,
                    outerPositionsOwners, noOfNeighbours,
                    (int) (theAngle / innerDeltaAngle), startX,
                    startY, outerDeltaAngle, outerRadius,
                    bc[compIndex].length);
            double startAngle = startPos * outerDeltaAngle;

            if (startAngle < 0) {
                continue;
            }

            iter = edgesFrom[bc[compIndex][i]].iterator();

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (!posSet[currentNeighbour]) {
                    posSet[currentNeighbour] = true;

                    int holeDepth = NoOfChildren(currentNeighbour, addedNeighbours);

//                    for (int j = 0; j < (holeDepth / 2); j++) {
                    for (int k = 0; k < (holeDepth / 2); k++) {
                        outerPositionsOwners[(startPos) % outerPositionsOwners.length] = -3;
                        // free but it must not be used (add. space for tree-like struct.)
                        outerPositionsTaken[(startPos) % outerPositionsOwners.length] = (int) (theAngle / innerDeltaAngle);
                        startPos++;
                        startAngle += outerDeltaAngle;

                        if (startAngle > (2 * Math.PI)) {
                            startAngle -= (2 * Math.PI);
                        }
                    }

                    setOffset(nodeViews.get(currentNeighbour), startX + (Math.cos(startAngle) * outerRadius),
                            startY - (Math.sin(startAngle) * outerRadius));
                    outerPositionsOwners[(startPos) % outerPositionsOwners.length] = currentNeighbour;
                    outerPositionsTaken[(startPos) % outerPositionsOwners.length] = (int) (theAngle / innerDeltaAngle);
                    startPos++;
                    startAngle += outerDeltaAngle;

                    if (startAngle > (2 * Math.PI)) {
                        startAngle -= (2 * Math.PI);
                    }

//                    for (int j = 0; j < (holeDepth / 2); j++) {
                    for (int k = 0; k < (holeDepth / 2); k++) {
                        outerPositionsOwners[(startPos) % outerPositionsOwners.length] = -3;
                        outerPositionsTaken[(startPos) % outerPositionsOwners.length] = (int) (theAngle / innerDeltaAngle);
                        startPos++;
                        startAngle += outerDeltaAngle;

                        if (startAngle > (2 * Math.PI)) {
                            startAngle -= (2 * Math.PI);
                        }
                    }
                }
            }
        }

        // laying out the rest of nodes
        for (int i = 0; i < bc[compIndex].length; i++) {
            iter = edgesFrom[bc[compIndex][i]].iterator();

            int currentNeighbour;

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (!addedNeighbours.containsKey(Integer.valueOf(currentNeighbour))) {
                    continue;
                }

//                View<CyNode> view = nodeViews.get(currentNeighbour);
//                pointX = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//                pointY = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
                pointX = nodeViews.get(currentNeighbour).getGraphicalNodeXCoordinate();
                pointY = nodeViews.get(currentNeighbour).getGraphicalNodeYCoordinate();

                theAngle = Math.asin((startY - pointY) / Math.sqrt(((pointX - startX) * (pointX
                        - startX))
                        + ((pointY - startY) * (pointY
                        - startY))));
                theAngleHlp = Math.acos((pointX - startX) / Math.sqrt(((pointX - startX) * (pointX
                        - startX))
                        + ((pointY - startY) * (pointY
                        - startY))));

                if (theAngleHlp > (Math.PI / 2)) {
                    theAngle = Math.PI - theAngle;
                }

                if (theAngle < 0) {
                    theAngle += (2 * Math.PI);
                }

//                for (int j = 0; j < posSet.length; j++) {
                for (int k = 0; k < posSet.length; k++) {
                    depthPosSet[k] = posSet[k];
                }

                EachNodeHeight(currentNeighbour);

                DFSSetPos(currentNeighbour, theAngle, outerRadius - innerCircleRadius);
            }
        }
    }

    /**
     * Founds best positions for nodes from outer cicrle, according to inner
     * circle. We avoid crossings of edges between inner and outer circle, and
     * we want to minimize the length of that edges.
     *
     * @param idealPosition - according to position of neighbour node from inner
     * circle
     * @param outerPositionsTaken - array of availability of positions on second
     * circle
     * @param outerPositionsOwners - array of owners (from inner cicrle) of
     * positions on second circle
     * @param noOfPos - number of positions that we need
     * @param innerCirclePos - owner (parent, neighbour from inner cicrle) of
     * given node
     * @param startX
     * @param startY
     * @param outerDeltaAngle
     * @param outerRadius
     * @param innerCSize
     * @return
     */
    private static int BestFreePositionsForAll(int idealPosition, int[] outerPositionsTaken,
            int[] outerPositionsOwners, int noOfPos,
            int innerCirclePos, double startX, double startY,
            double outerDeltaAngle, double outerRadius, int innerCSize) {
//		for (int j = 0; j < outerPositionsTaken.length; j++)
//			System.out.print(outerPositionsTaken[j] + " ");

//		System.out.println("innerCircPos: " + innerCirclePos + ", noOfPos: " + noOfPos
//		                   + ", idealPos: " + idealPosition);

        int startPos = idealPosition;

        if (idealPosition < 0) {
            startPos += outerPositionsTaken.length;
        }

        int i = 0;
        int alreadyFound = 0;
        int startOfAlFound = -1;
        boolean found = false;
        boolean goDown = false;
        boolean goUp = false;

        while (!found && !(goUp && goDown)) {
            //System.out.print(startPos + " ");
            for (i = startPos;
                    (i < (startPos + noOfPos))
                    && (outerPositionsTaken[i % outerPositionsTaken.length] == -1); i++) {
            }

            if (i < (startPos + noOfPos)) {
                if (((outerPositionsTaken[i % outerPositionsTaken.length] > innerCirclePos)
                        && ((outerPositionsTaken[i % outerPositionsTaken.length] - innerCirclePos) < (0.7 * innerCSize)))
                        || ((innerCirclePos - outerPositionsTaken[i % outerPositionsTaken.length]) > (0.7 * innerCSize))) {
                    alreadyFound = (i - startPos + outerPositionsTaken.length) % outerPositionsTaken.length;
                    startOfAlFound = startPos;
                    startPos -= (noOfPos - alreadyFound);

                    if (startPos < 0) {
                        startPos += outerPositionsTaken.length;
                    }

                    goDown = true;
                } else {
                    startPos = (i + 1) % outerPositionsTaken.length;
                    goUp = true;
                }
            } else {
                found = true;
            }
        }

        if (goUp && goDown) {
            i = startOfAlFound - 1;

            int j = i - 1;
            int count = 0;
            //System.out.print(j + " ");

            int index = (i % outerPositionsTaken.length + outerPositionsTaken.length) % outerPositionsTaken.length;
            if (((outerPositionsTaken[index] > innerCirclePos)
                    && ((outerPositionsTaken[index] - innerCirclePos) < (0.7 * innerCSize)))
                    || ((innerCirclePos - outerPositionsTaken[index]) > (0.7 * innerCSize))) {
                j--;
                i--;
            }

            while (count < (noOfPos - alreadyFound)) {
                //System.out.print(j + " ");

                if (outerPositionsTaken[(j + outerPositionsTaken.length) % outerPositionsTaken.length] == -1) {
                    // move all for one place left
                    //	System.out.print(" moving ");
                    if (outerPositionsOwners[(j + outerPositionsTaken.length) % outerPositionsTaken.length] == -2) {
                        //System.out.println("BUUUUUUUUUUUUUUUUUUU");

                        return -1;
                    }

                    for (int k = j; k < (i - count); k++) {
                        if (outerPositionsOwners[(k + 1 + outerPositionsTaken.length) % outerPositionsTaken.length] > 0) {
                            setOffset(nodeViews.get(outerPositionsOwners[(k + 1 + outerPositionsTaken.length) % outerPositionsTaken.length]),
                                    startX + (Math.cos(outerDeltaAngle * k) * outerRadius),
                                    startY - (Math.sin(outerDeltaAngle * k) * outerRadius));
                        }

                        outerPositionsOwners[(k + outerPositionsTaken.length) % outerPositionsTaken.length] = outerPositionsOwners[(k + 1 + outerPositionsTaken.length) % outerPositionsTaken.length];
                        outerPositionsTaken[(k + outerPositionsTaken.length) % outerPositionsTaken.length] = outerPositionsTaken[(k + 1 + outerPositionsTaken.length) % outerPositionsTaken.length];
                    }

                    count++;
                }

                j--;
            }

            startPos = (i - count + 1 + outerPositionsOwners.length) % outerPositionsOwners.length;
        }

        /*    for (i = startPos; i < startPos + noOfPos; i++)
         {
         outerPositionsTaken[i % outerPositionsTaken.length] = innerCirclePos;
         }*/
        return startPos;
    }

    /**
     * Heuristic function which estimates the number of nodes "after" the given
     * node. Using it we can estimate the distance from this node to his
     * children.
     *
     * @param nodeID - ID of given node
     * @return
     */
    private static int EachNodeHeight(int nodeID) {
        Iterator iter = edgesFrom[nodeID].iterator();
        int currentNeighbour;
        int noOfChildren = 0;
        HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();

        while (iter.hasNext()) {
            currentNeighbour = ((Integer) iter.next()).intValue();

            if (!depthPosSet[currentNeighbour] && !tmp.containsKey(Integer.valueOf(currentNeighbour))) {
                depthPosSet[currentNeighbour] = true;
                tmp.put(Integer.valueOf(currentNeighbour), Integer.valueOf(0));
            }
        }

        iter = edgesFrom[nodeID].iterator();

        while (iter.hasNext()) {
            currentNeighbour = ((Integer) iter.next()).intValue();

            if (tmp.containsKey(Integer.valueOf(currentNeighbour))) {
                noOfChildren += EachNodeHeight(currentNeighbour);
            }
        }

        if (nodeHeights.containsKey(Integer.valueOf(nodeID))) {
            nodeHeights.remove(Integer.valueOf(nodeID));
        }

        nodeHeights.put(Integer.valueOf(nodeID), Integer.valueOf(noOfChildren));

        return (noOfChildren + 1);
    }

    /**
     * Function traverses graph starting from the node from outer circle until
     * it traverse all the nodes. When it comes along another biconnected
     * component it sets it out on circle and calls SetOuterCircle() again. The
     * main purpose of the function is setting the node positions of tree-like
     * parts of graph.
     *
     * @param nodeID - ID of the node from which we start DFS
     * @param theAngle - the angle at which we "enter" the node, using it we can
     * calculate at which position to set the node
     * @param theRadius - this will represent the distance between the parent of
     * the node and the child in tree-like parts
     */
    private static void DFSSetPos(int nodeID, double theAngle, double theRadius) {
        Integer component = node2BiComp.get(Integer.valueOf(nodeID));
        if (component != null && !drawnBiComps[component]) {
            int comp = node2BiComp.get(Integer.valueOf(nodeID)).intValue();
            Node view = nodeViews.get(nodeID);
//            double centerX = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//            double centerY = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            double centerX = view.getGraphicalNodeXCoordinate();
            double centerY = view.getGraphicalNodeYCoordinate();

            double radius = (48 * bc[comp].length) / (2 * Math.PI);
            double deltaAngle = (2 * Math.PI) / bc[comp].length;
            double currAngle = theAngle - Math.PI - deltaAngle;

            if (currAngle < 0) {
                currAngle += (2 * Math.PI);
            }

            centerX += (Math.cos(theAngle) * radius * 4.0);
            centerY -= (Math.sin(theAngle) * radius * 4.0);

            drawnBiComps[comp] = true;

            // sorting nodes on inner circle
            bc[comp] = SortInnerCircle(bc[comp]);

            /*if (bc[comp].length > 20)
             bc[comp] = ReduceInnerCircleCrossings(bc[comp]);*/
            boolean oneAtLeast = false;

//            for (int i = 0; i < bc[comp].length; i++) {
            for (int j = 0; j < bc[comp].length; j++) {
                if (posSet[bc[comp][j]]) {
                    continue;
                }

                setOffset(nodeViews.get(bc[comp][j]), centerX + (Math.cos(currAngle) * radius),
                        centerY - (Math.sin(currAngle) * radius));
                posSet[bc[comp][j]] = true;

                oneAtLeast = true;
                currAngle -= deltaAngle;

                if (currAngle < 0) {
                    currAngle += (2 * Math.PI);
                }
            }

            if (oneAtLeast) {
//                setOffset(nodeViews.get(nodeID),
//                        nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION) + (Math.cos(theAngle) * 3 * radius),
//                        nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION) - (Math.sin(theAngle) * 3 * radius));
                double x = view.getGraphicalNodeXCoordinate();
                double y = view.getGraphicalNodeYCoordinate();
                setOffset(nodeViews.get(nodeID),
                        x + (Math.cos(theAngle) * 3 * radius),
                        y - (Math.sin(theAngle) * 3 * radius));


                SetOuterCircle(comp, radius, centerX, centerY, nodeID);
            }
        } else {
            Iterator iter = edgesFrom[nodeID].iterator();
            int currentNeighbour;
            double startAngle = theAngle + (Math.PI / 2);

            if (startAngle > (2 * Math.PI)) {
                startAngle -= (2 * Math.PI);
            }

            int neighboursCount = 0;
            int min1 = 1000;
            int min2 = 1000;
            int max = -1;
            int min1Id = -1;
            int min2Id = -2;
            int maxId = -3;
            HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (!posSet[currentNeighbour] && !tmp.containsKey(Integer.valueOf(currentNeighbour))) {
                    neighboursCount++;
                    tmp.put(Integer.valueOf(currentNeighbour), Integer.valueOf(0));

                    if (nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue() < min1) {
                        min2 = min1;
                        min2Id = min1Id;
                        min1 = nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue();
                        min1Id = currentNeighbour;
                    } else if (nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue() < min2) {
                        min2 = nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue();
                        min2Id = currentNeighbour;
                    }

                    if (nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue() >= max)//&& currentNeighbour != min2Id && currentNeighbour != min1Id)
                    {
                        max = nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue();
                        maxId = currentNeighbour;
                    }
                }
            }

            if (neighboursCount == 0) {
                return;
            }

            double deltaAngle = Math.PI / (neighboursCount + 1);

            startAngle -= deltaAngle;

            if (startAngle < 0) {
                startAngle += (2 * Math.PI);
            }

            double remStartAngle = startAngle;

            if (neighboursCount > 2) {
                deltaAngle = (2 * Math.PI) / neighboursCount;
                startAngle = (theAngle + Math.PI) - ((3 * deltaAngle) / 2);

                if (startAngle > (2 * Math.PI)) {
                    startAngle -= (2 * Math.PI);
                }

                remStartAngle = (theAngle + Math.PI) - (deltaAngle / 2);

                if (remStartAngle > (2 * Math.PI)) {
                    remStartAngle -= (2 * Math.PI);
                }
            }

            iter = edgesFrom[nodeID].iterator();

            double r = 72;
            double rTry;

            if (((48 * neighboursCount) / (2 * Math.PI)) > r) {
                r = (48 * neighboursCount) / (2 * Math.PI);
            }

            rTry = r;

            double hlp = 100.0;
//            double startX = nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//            double startY = nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            double startX = nodeViews.get(nodeID).getGraphicalNodeXCoordinate();
            double startY = nodeViews.get(nodeID).getGraphicalNodeYCoordinate();

            if (neighboursCount > 2) {
                setOffset(nodeViews.get(nodeID), startX + (Math.cos(theAngle) * r * ((min2 + 1) % 100)),
                        startY - (Math.sin(theAngle) * r * ((min2 + 1) % 100)));
//                startX = nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//                startY = nodeViews.get(nodeID).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
                startX = nodeViews.get(nodeID).getGraphicalNodeXCoordinate();
                startY = nodeViews.get(nodeID).getGraphicalNodeYCoordinate();

                //System.out.println("theAngle = " + theAngle + ", startAngle = " + startAngle + ", remStartAngle = " + remStartAngle + ", deltaAngle = " + deltaAngle);
                //System.out.println("min1Id = " + min1Id + ", min2Id" + min2Id + ", maxId" + maxId);
                setOffset(nodeViews.get(min1Id), startX + (Math.cos(remStartAngle) * r),
                        startY - (Math.sin(remStartAngle) * r));
                setOffset(nodeViews.get(min2Id), startX + (Math.cos(remStartAngle + deltaAngle) * r),
                        startY - (Math.sin(remStartAngle + deltaAngle) * r));

                if (nodeHeights.get(Integer.valueOf(maxId)).intValue() > 8) {
                    r = 256;
                }

                setOffset(nodeViews.get(maxId),
                        startX + (Math.cos(remStartAngle - ((neighboursCount / 2) * deltaAngle)) * r),
                        startY - (Math.sin(remStartAngle - ((neighboursCount / 2) * deltaAngle)) * r));
                //System.out.println("Ugao za maxID "
                //                  + (remStartAngle - ((neighboursCount / 2) * deltaAngle)));
            }

            tmp = new HashMap<Integer, Integer>();

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (!posSet[currentNeighbour] && !tmp.containsKey(Integer.valueOf(currentNeighbour))) {
                    if (nodeHeights.get(Integer.valueOf(currentNeighbour)).intValue() > 8) {
                        r = 256;
                    } else {
                        r = rTry;
                    }

                    posSet[currentNeighbour] = true;
                    tmp.put(Integer.valueOf(currentNeighbour), Integer.valueOf(0));

                    if (((currentNeighbour != min1Id) && (currentNeighbour != min2Id)
                            && (currentNeighbour != maxId)) || (neighboursCount <= 2)) {
                        setOffset(nodeViews.get(currentNeighbour), startX + (Math.cos(startAngle) * r),
                                startY - (Math.sin(startAngle) * r));

                        startAngle -= deltaAngle;

                        if (startAngle < 0) {
                            startAngle += (2 * Math.PI);
                        }

                        if (((Math.abs(startAngle
                                - (remStartAngle - ((neighboursCount / 2) * deltaAngle))) < 0.0001)
                                || (Math.abs(startAngle
                                - (remStartAngle - ((neighboursCount / 2) * deltaAngle)
                                + (2 * Math.PI))) < 0.0001)) && (neighboursCount > 2)) {
                            startAngle -= deltaAngle;

                            if (startAngle < 0) {
                                startAngle += (2 * Math.PI);
                            }
                        }
                    }
                }
            }

            iter = edgesFrom[nodeID].iterator();

            if (neighboursCount > 2) {
                DFSSetPos(min1Id, remStartAngle, theRadius * Math.sin(deltaAngle / 2));
                DFSSetPos(min2Id, remStartAngle + deltaAngle, theRadius * Math.sin(deltaAngle / 2));
                DFSSetPos(maxId, remStartAngle - ((neighboursCount / 2) * deltaAngle),
                        theRadius * Math.sin(deltaAngle / 2));
                hlp = remStartAngle;
                remStartAngle -= deltaAngle;
            }

            while (iter.hasNext()) {
                currentNeighbour = ((Integer) iter.next()).intValue();

                if (tmp.containsKey(Integer.valueOf(currentNeighbour))) {
                    if (((currentNeighbour != min1Id) && (currentNeighbour != min2Id)
                            && (currentNeighbour != maxId)) || (neighboursCount <= 2)) {
                        DFSSetPos(currentNeighbour, remStartAngle,
                                theRadius * Math.sin(deltaAngle / 2));

                        remStartAngle -= deltaAngle;

                        if (((remStartAngle == (hlp - ((neighboursCount / 2) * deltaAngle)))
                                || (remStartAngle == (hlp - ((neighboursCount / 2) * deltaAngle)
                                + (2 * Math.PI)))) && (neighboursCount > 2)) {
                            startAngle -= deltaAngle;
                        }

                        if (remStartAngle < 0) {
                            remStartAngle += (2 * Math.PI);
                        }
                    }
                }
            }
        }
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
