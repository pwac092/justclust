/**
 * This file is part of the Java Machine Learning Library
 *
 * The Java Machine Learning Library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * The Java Machine Learning Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * Copyright (c) 2006-2009, Thomas Abeel
 *
 * Project: http://java-ml.sourceforge.net/
 *
 */
//package net.sf.javaml.clustering.mcl;
package justclust.plugins.clustering.mcl;

import java.util.ArrayList;
import java.util.Vector;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;

//import net.sf.javaml.clustering.Clusterer;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.DefaultDataset;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.distance.AbstractSimilarity;
//import net.sf.javaml.distance.DistanceMeasure;
//public class MCL implements Clusterer {
public class MCL {

    /**
     * XXX doc
     *
     * @param dm
     */
//    public MCL(DistanceMeasure dm) {
    public MCL() {
//        this(dm, 0.001, 2.0, 0, 0.001);
        this(0.001, 2.0, 0, 0.001);

    }

    /**
     * XXX doc
     *
     * @param dm
     * @param maxResidual
     * @param gamma
     * @param loopGain
     * @param maxZero
     */
//    public MCL(DistanceMeasure dm, double maxResidual, double pGamma, double loopGain, double maxZero) {
    public MCL(double maxResidual, double pGamma, double loopGain, double maxZero) {
//        if (!(dm instanceof AbstractSimilarity))
//            throw new RuntimeException("MCL requires the distance measure to be a Similarity measure");


//        this.dm = dm;
        this.maxResidual = maxResidual;
        this.pGamma = pGamma;
        this.loopGain = loopGain;
        this.maxZero = maxZero;
    }
//    private DistanceMeasure dm;
    // Maximum difference between row elements and row square sum (measure of
    // idempotence)
    private double maxResidual = 0.001;
    // inflation exponent for Gamma operator
    private double pGamma = 2.0;
    // loopGain values for cycles
    private double loopGain = 0.;
    // maximum value considered zero for pruning operations
    private double maxZero = 0.001;

//    public Dataset[] cluster(Dataset data) {
    public ArrayList<Cluster> cluster(ArrayList<Node> data) {
        SparseMatrix dataSparseMatrix = new SparseMatrix();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j <= i; j++) {
//                Instance x = data.instance(i);
                Node x = data.get(i);
//                Instance y = data.instance(j);
                Node y = data.get(j);
//                double dist = dm.measure(x, y);
                double dist = 0;
                for (Edge edge : x.edges) {
                    if (edge.node1 == y || edge.node2 == y) {
                        dist = edge.edgeSharedAttributes.weight;
                    }
                }
                if (dist > maxZero) {
//                    dataSparseMatrix.add(i, j, dm.measure(x, y));
                    dataSparseMatrix.add(i, j, dist);
                }
            }
        }

        MarkovClustering mcl = new MarkovClustering();
        SparseMatrix matrix = mcl.run(dataSparseMatrix, maxResidual, pGamma, loopGain, maxZero);

        // convert matrix to output dataset:
        int[] sparseMatrixSize = matrix.getSize();
        // find number of attractors (non zero values) in diagonal
        int attractors = 0;
        for (int i = 0; i < sparseMatrixSize[0]; i++) {
            double val = matrix.get(i, i);
            if (val != 0) {
                attractors++;
            }
        }
        // create cluster for each attractor with value close to 1
//        Vector<Vector<Instance>> finalClusters = new Vector<Vector<Instance>>();

        for (int i = 0; i < sparseMatrixSize[0]; i++) {
//            Vector<Instance> cluster = new Vector<Instance>();
            Cluster cluster = new Cluster();
            cluster.nodes = new ArrayList<Node>();
            double val = matrix.get(i, i);
            if (val >= 0.98) {
                for (int j = 0; j < sparseMatrixSize[0]; j++) {
                    double value = matrix.get(j, i);
                    if (value != 0) {
//                        cluster.add(data.instance(j));
                        cluster.nodes.add(data.get(j));
                        data.get(j).cluster = cluster;
                    }
                }
//                finalClusters.add(cluster);
            }
        }

        // create an array.
        // one element for each node.
        // each element contains whether the node's connected component has been
        // visited yet by the below algorithm.
        // the purpose of the array is to prevent the algorithm from finding the
        // connected component of each node even when it has already been found.
        boolean[] componentFound = new boolean[data.size()];
        // this ArrayList will contain all the connected components
        ArrayList<ArrayList<Node>> connectedComponents = new ArrayList<ArrayList<Node>>();
        // iterate through each node
        for (int i = 0; i < data.size(); i++) {
            // if the node's component has not been found
            if (!componentFound[i]) {

                // get the node's connected component
                ArrayList<Node> component = new ArrayList<Node>();
                component = getConnectedComponent(component, data.get(i));

                // the componentFound array and connectedComponent ArrayList are
                // updated for all nodes in the component
                for (int j = 0; j < component.size(); j++) {
                    componentFound[data.indexOf(component.get(j))] = true;
                }

                connectedComponents.add(component);

            }
        }

//        Dataset[] output = new Dataset[finalClusters.size()];
//        for (int i = 0; i < finalClusters.size(); i++) {
//            output[i] = new DefaultDataset();
//        }
//        for (int i = 0; i < finalClusters.size(); i++) {
//            Vector<Instance> getCluster = new Vector<Instance>();
//            getCluster = finalClusters.get(i);
//            for (int j = 0; j < getCluster.size(); j++) {
//                output[i].add(getCluster.get(j));
//            }
//        }
        ArrayList<Cluster> output = new ArrayList<Cluster>();
        for (ArrayList<Node> connectedComponent : connectedComponents) {
            Cluster cluster = new Cluster();
            cluster.nodes = new ArrayList<Node>();
            for (Node node : connectedComponent) {
                cluster.nodes.add(node);
            }
            output.add(cluster);
        }

        return output;

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

            // edges between nodes which are not in the same cluster should be
            // ignored because clusters are represented by edges connecting
            // their nodes.
            if (edge.node1.cluster != edge.node2.cluster) {
                continue;
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
