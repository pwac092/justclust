package justclust.plugins.clustering.kmeans;

import justclust.plugins.clustering.singlelinkage.*;
import java.util.ArrayList;
import java.util.Random;

import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.DoubleFieldControl;
import justclust.plugins.clustering.ClusteringAlgorithmPluginInterface;
import justclust.plugins.configurationcontrols.IntegerFieldControl;
import justclust.toolbar.dendrogram.DendrogramCluster;

public class KMeansClusteringAlgorithm implements
        ClusteringAlgorithmPluginInterface {
    
    public IntegerFieldControl integerFieldControl;
    /** number of nodes (genes/rows in the microarray) */
    private int networkNodeAmount;
    /** number of coordinates (columns with time points) */
    private int coordinates;
    /** value of k */
    private int k;
    
    public String getName() throws Exception {
        return "k-means clustering algorithm";
    }
    
    public String getDescription() throws Exception {
        return "This clustering algorithm plug-in clusters the current network with a k-means clustering algorithm.";
    }
    
    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {
        
        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();
        
        integerFieldControl = new IntegerFieldControl();
        integerFieldControl.label = "Number of Clusters:";
        integerFieldControl.value = 10;
        controls.add(integerFieldControl);
        
        return controls;
        
    }

    private ArrayList<Node> initializeCentroids(ArrayList<Node> networkNodes)
    {
        ArrayList<Node> centroidNodes = new ArrayList<Node>();
        this.k = integerFieldControl.value;
        Random random = new Random();
        while (centroidNodes.size() < k) {
            Node centroidNode = networkNodes.get(random.nextInt(networkNodeAmount));
            if (!centroidNodes.contains(centroidNode)) {
                centroidNodes.add(centroidNode);
            }
        }
        return centroidNodes;
    }

    private double[][] getInitialCentroidCoordinates(ArrayList<Node> centroidNodes)
    {
        double[][] centroidValues = new double[k][coordinates];
        for (int i = 0; i < k; i++) {
            Node centroidNode = centroidNodes.get(i);
            for (int j = 0; j < coordinates; j++) {
                centroidValues[i][j] = centroidNode.nodeSharedAttributes.microarrayValues.get(j);
            }
        }
        return centroidValues; 
    }

    private double euclideanDistance(ArrayList<Double> n, double[] centroid)
    {
        double dist = 0.0;
        for (int l = 0; l < this.coordinates; l++)
        {
            dist += Math.pow(centroid[l] - n.get(l), 2);                                                       
        }
        return Math.sqrt(dist);
    }

    private void average(double[] profile, int n)
    {
        double inv_n = 1.0/(double) n;
        for (int i=0; i<profile.length; ++i)
        {
            profile[i] *= inv_n;    
        }
    }
    
    private void add(ArrayList<Double> n, double[] centroid)
    {
        for (int l = 0; l < this.coordinates; l++) 
            centroid[l] += n.get(l);
    }

    private boolean equalCentroids(double v[], double v2[])
    {
        final double tol = 1e-5;
        boolean areEqual = true;
        for (int i=0; i<v.length && areEqual; ++i)
        {
            areEqual = Math.abs(v[i] - v2[i]) <= tol;
        }
        return areEqual;
    }

    public void clusterNetwork(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception {
        // 0.- number of genes and number of columns are set
        this.networkNodeAmount = networkNodes.size();
        this.coordinates = networkNodes.get(0).nodeSharedAttributes.microarrayValues.size();

        // 1.- centroids are initialized (k different elements are taken at random)
        ArrayList<Node> centroidNodes = initializeCentroids(networkNodes);
        
        // 2.- centroid coordinate values are written to a double array
        double[][] centroidValues = this.getInitialCentroidCoordinates(centroidNodes);
       
        // 3.- main k-means loop 
        int[] nodeCentroidIndexes;
        boolean centroidValuesChanged;
        do {
            nodeCentroidIndexes = new int[networkNodeAmount];
            
            // 3.1.- for each node in the network, we compute its distance to each one of the centroids
            // and we assign the index of the closest centroid to each node
            for (int i = 0; i < networkNodeAmount; i++) {
                double minimumDistanceFromCentriod = Double.MAX_VALUE;
                ArrayList<Double> values = networkNodes.get(i).nodeSharedAttributes.microarrayValues;
                for (int j = 0; j < k; j++) {
                    double distanceFromCentroid = this.euclideanDistance(values, centroidValues[j]);
                    
                    if (distanceFromCentroid < minimumDistanceFromCentriod) {
                        minimumDistanceFromCentriod = distanceFromCentroid;
                        nodeCentroidIndexes[i] = j;
                    }
                }
            }
           
            // 3.2.- we now recompute centroid values
            double[][] nextCentroidValues = new double[k][coordinates]; // initialized to 0 by default
            int [] numNodesPerCentroid = new int[k];
            
            // ...for each node, we sum its values to its profile
            for (int j = 0; j < networkNodeAmount; j++) {
                ArrayList<Double> values = networkNodes.get(j).nodeSharedAttributes.microarrayValues;
                int centroid_id = nodeCentroidIndexes[j];
                this.add(values, nextCentroidValues[centroid_id]);
                numNodesPerCentroid[centroid_id]++;
            }

            // ...average the profiles
            for (int i=0; i<k; ++i)
            {
                this.average(nextCentroidValues[i], numNodesPerCentroid[i]);
            }

            // 3.3.- if the recalculated centroids are equal than the previous ones we stop,
            // otherwise we keep iterating
            centroidValuesChanged = false;
            for (int i=0; i<k && !centroidValuesChanged; ++i)
            {
                centroidValuesChanged = !equalCentroids(centroidValues[i], nextCentroidValues[i]);
            }

            // 3.4.- centroids are updated, anyway 
            centroidValues = nextCentroidValues;
        } while (centroidValuesChanged); // repeat while there are node changes
       
        // 4.- set the clustering into JustClust's data structures 
        for (int i = 0; i < k; i++) {
            Cluster cluster = new Cluster();
            networkClusters.add(cluster);
            cluster.nodes = new ArrayList<Node>();
        }
        for (int i = 0; i < networkNodeAmount; i++) {
            networkClusters.get(nodeCentroidIndexes[i]).nodes.add(networkNodes.get(i));
        }
        
    }
    
    public boolean isHierarchicalClustering() {
        return false;
    }
    
    public ArrayList<DendrogramCluster> getRootDendrogramClusters() {
        return null;
    }
}

