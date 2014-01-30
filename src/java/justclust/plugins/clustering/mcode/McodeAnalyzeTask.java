/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.mcode;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import justclust.datastructures.Data;
import justclust.datastructures.Cluster;
import justclust.datastructures.Node;

/**
 * MCODE Score network and find cluster task.
 */
public class McodeAnalyzeTask {

//    private final MCODEAlgorithm alg;
    private final McodeAlgorithm alg;
//    private final MCODEUtil mcodeUtil;
    private final McodeUtil mcodeUtil;
    private final int analyze;
    private final int resultId;
//    private final AnalysisCompletedListener listener;
    private boolean interrupted;
//    private CyNetwork network;
//    private static final Logger logger = LoggerFactory.getLogger(MCODEAnalyzeTask.class);

    /**
     * Scores and finds clusters in a given network
     *
     * @param network The network to cluster
     * @param analyze Tells the task if we need to rescore and/or refind
     * @param resultId Identifier of the current result set
     * @param alg reference to the algorithm for this network
     */
//    public MCODEAnalyzeTask(final CyNetwork network,
//            final int analyze,
//            final int resultId,
//            final MCODEAlgorithm alg,
//            final MCODEUtil mcodeUtil,
//            final AnalysisCompletedListener listener) {
    public McodeAnalyzeTask(final int analyze,
            final int resultId,
            final McodeAlgorithm alg,
            final McodeUtil mcodeUtil) {
//        this.network = network;
        this.analyze = analyze;
        this.resultId = resultId;
        this.alg = alg;
        this.mcodeUtil = mcodeUtil;
//        this.listener = listener;
    }

    /**
     * Run MCODE (Both score and find steps).
     */
//    @Override
//    public void run(TaskMonitor taskMonitor) throws Exception {
    public void run() throws Exception {
//        if (taskMonitor == null) {
//            throw new IllegalStateException("Task Monitor is not set.");
//        }

        boolean success = false;
//        List<MCODECluster> clusters = null;
        List<McodeCluster> clusters = null;
        mcodeUtil.resetLoading();

        try {
            // Run MCODE scoring algorithm - node scores are saved in the alg object
//            alg.setTaskMonitor(taskMonitor, network.getSUID());

//            // Only (re)score the graph if the scoring parameters have been changed
//            if (analyze == MCODEAnalyzeAction.RESCORE) {
//                taskMonitor.setProgress(0.001);
//                taskMonitor.setTitle("MCODE Analysis");
//                taskMonitor.setStatusMessage("Scoring Network (Step 1 of 3)");
//                alg.scoreGraph(network, resultId);
            alg.scoreGraph(resultId);

//                if (interrupted) {
//                    return;
//                }

//                logger.info("Network was scored in " + alg.getLastScoreTime() + " ms.");
//            }

//            taskMonitor.setProgress(0.001);
//            taskMonitor.setStatusMessage("Finding Clusters (Step 2 of 3)");

//            clusters = alg.findClusters(network, resultId);
            clusters = alg.findClusters(resultId);
//            for (McodeCluster mcodeCluster : clusters)
//            {
//                System.out.println(mcodeCluster.getScore());
//            }

            // populate the networkClusters field of the McodeClusteringAlgorithm class with the
            // clusters which have been found by the MCODE algorithm
            for (McodeCluster mcodeCluster : clusters) {
                Cluster cluster = new Cluster();
                cluster.nodes = new ArrayList<Node>();
                for (Node node : mcodeCluster.getGraph().getNodeList()) {
                    cluster.nodes.add(node);
                }
                McodeClusteringAlgorithm.classInstance.networkClusters.add(cluster);
            }

//            if (interrupted) {
//                return;
//            }

//            taskMonitor.setProgress(0.001);
//            taskMonitor.setStatusMessage("Drawing Results (Step 3 of 3)");

            // Also create all the images here for the clusters, since it can be a time consuming operation
//            mcodeUtil.sortClusters(clusters);
//            int imageSize = mcodeUtil.getCurrentParameters().getResultParams(resultId).getDefaultRowHeight();
//            int count = 0;

//            for (final MCODECluster c : clusters) {
//                if (interrupted) {
//                    return;
//                }
//
//                final Image img = mcodeUtil.createClusterImage(c, imageSize, imageSize, null, true, null);
//                c.setImage(img);
//                taskMonitor.setProgress((++count) / (double) clusters.size());
//            }

            success = true;
        } catch (Exception e) {
            throw new Exception("Error while executing the MCODE analysis", e);
        } finally {
            mcodeUtil.destroyUnusedNetworks(McodeClusteringAlgorithm.classInstance.networkNodes, McodeClusteringAlgorithm.classInstance.networkEdges, clusters);
//            if (listener != null) {
//                listener.handleEvent(new AnalysisCompletedEvent(success, clusters));
//            }
        }
    }

//    @Override
    public void cancel() {
        this.interrupted = true;
        alg.setCancelled(true);
        mcodeUtil.removeNetworkResult(resultId);
//        mcodeUtil.removeNetworkAlgorithm(network.getSUID());
    }

    /**
     * Gets the Task Title.
     *
     * @return human readable task title.
     */
    public String getTitle() {
        return "MCODE Network Cluster Detection";
    }
}
