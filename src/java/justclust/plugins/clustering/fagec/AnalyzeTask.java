/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.fagec;

import java.awt.Image;
import java.util.List;
import justclust.datastructures.Cluster;

/**
 *
 * @author wuaz008
 */
public class AnalyzeTask {

//    private final Algorithm alg;
    private final Fagec alg;
//    private final ClusterUtil mcodeUtil;
    private final ClusterUtil clusterUtil;
    private final int analyze;
    private final int resultId;
//    private final AnalysisCompletedListener listener;
    private boolean interrupted;
//    private CyNetwork network;
//    private static final Logger logger = LoggerFactory
//            .getLogger(AnalyzeTask.class);
    final static int FIRST_TIME = 0;
    final static int RESCORE = 1;
    final static int REFIND = 2;
    final static int FIND = 3;
    final static int INTERRUPTED = 4;
    final static int FINDCLIQUE = 5;
    final static int CLIQUEBASED = 6;
    final static int EXISTS = 7;

//    public AnalyzeTask(CyNetwork network, int analyze, int resultId,
//            Algorithm alg, ClusterUtil mcodeUtil,
//            AnalysisCompletedListener listener) {
    public AnalyzeTask(int analyze, int resultId,
            Fagec alg, ClusterUtil clusterUtil) {
//        this.network = network;
        this.analyze = analyze;
        this.resultId = resultId;
        this.alg = alg;
//        this.mcodeUtil = mcodeUtil;
        this.clusterUtil = clusterUtil;
//        this.listener = listener;
    }

//    @Override
//    public void run(TaskMonitor taskMonitor) throws Exception {
    public void run() throws Exception {
//        if (taskMonitor == null) {
//            throw new IllegalStateException("Task Monitor is not set.");
//        }

        boolean success = false;
//        List<Cluster> clusters = null;
        List<Cluster> clusters = null;
//        mcodeUtil.resetLoading();
        clusterUtil.resetLoading();

        try {
//            if (alg instanceof MCODE) {
//                // Run MCODE scoring algorithm - node scores are saved in the alg object
//                alg.setTaskMonitor(taskMonitor, network.getSUID());
//
//                // Only (re)score the graph if the scoring parameters have been changed
//                if (analyze == AnalyzeAction.RESCORE) {
//                    taskMonitor.setProgress(0.001);
//                    taskMonitor.setTitle("MCODE Analysis");
//                    taskMonitor.setStatusMessage("Scoring Network (Step 1 of 3)");
//                    alg.scoreGraph(network, resultId);
//
//                    if (interrupted) {
//                        return;
//                    }
//
//                    logger.info("Network was scored in " + alg.getLastScoreTime() + " ms.");
//                }
//
//                taskMonitor.setProgress(0.001);
//                taskMonitor.setStatusMessage("Finding Clusters (Step 2 of 3)");
//
//                clusters = alg.findClusters(network, resultId);
//
//                if (interrupted) {
//                    return;
//                }
//
//                taskMonitor.setProgress(0.001);
//                taskMonitor.setStatusMessage("Drawing Results (Step 3 of 3)");
//
//                // Also create all the images here for the clusters, since it can be a time consuming operation
//                mcodeUtil.sortClusters(clusters);
//                int imageSize = mcodeUtil.getCurrentParameters().getResultParams(resultId).getDefaultRowHeight();
//                int count = 0;
//
//                for (final Cluster c : clusters) {
//                    if (interrupted) {
//                        return;
//                    }
//
//                    final Image img = mcodeUtil.createClusterImage(c, imageSize, imageSize, null, true, null);
//                    c.setImage(img);
//                    taskMonitor.setProgress((++count) / (double) clusters.size());
//                }
//
//                success = true;
//            } else if (alg instanceof EAGLE)//
//            {
//                EAGLE objEagle = (EAGLE) alg;
//                objEagle.setTaskMonitor(taskMonitor, network.getSUID());
//                taskMonitor.setProgress(0);
//                taskMonitor
//                        .setStatusMessage("Step 1 of 3:Calculate all the maximal Clique...");
//                clusters = objEagle.run(network, this.resultId);
//                objEagle.getMaximalCliques(network, this.resultId);
//                System.err.println("Finding clique: Time spent "
//                        + objEagle.getLastFindTime() + " ms.");
//                if (interrupted) {
//                    return;
//                }
//                taskMonitor.setProgress(0);
//                taskMonitor
//                        .setStatusMessage("Step 2 of 3:Generating Complexes...");
//                clusters = objEagle.EAGLEFinder(network, this.resultId);
//                if (interrupted) {
//                    return;
//                }
//                taskMonitor.setProgress(0);
//                taskMonitor
//                        .setStatusMessage("Step 3 of 3: Drawing the Result Network...");
//                // create all the images here for the clusters, it can be a time
//                // consuming operation
//                //clusters = ClusterUtil.sortClusters2(clusters);
//                mcodeUtil.sortClusters2(clusters);
//
//                //for (int i = 0; i < (double) clusters.size(); i++)
//                //              clusterL.add(clusters[i]);
//
//                int imageSize = this.mcodeUtil.getCurrentParameters()
//                        .getResultParams(this.resultId).getDefaultRowHeight();
//                int count = 0;
//
//                for (Cluster c : clusters) {
//                    if (this.interrupted) {
//                        return;
//                    }
//
//
//                    final Image img = mcodeUtil.createClusterImage(c, imageSize, imageSize, null, true, null);
//                    /*      Image img = this.mcodeUtil.convertClusterToImage(null, c,
//                     imageSize, imageSize, null, true);*/
//                    c.setImage(img);
//                    taskMonitor.setProgress(++count / (double) clusters.size());
//                }
//
//                success = true;
//
//
//
//            } else if (alg instanceof FAGEC)//
//            {

//                FAGEC algFagec = (FAGEC) alg;
                Fagec algFagec = (Fagec) alg;

//                algFagec.setTaskMonitor(taskMonitor, network.getSUID());

//                taskMonitor.setProgress(0);
//                taskMonitor
//                        .setStatusMessage("Step 2 of 3:Generating Complexes...");
                clusters = algFagec.run(network, this.resultId);
//                System.err.println("After FAG-EC.Time used:"
//                        + algFagec.getLastFindTime());
                if (interrupted) {
                    return;
                }
//                taskMonitor.setProgress(0);
//                taskMonitor
//                        .setStatusMessage("Step 3 of 3: Drawing the Result Network...");
                // create all the images here for the clusters, it can be a
                // time consuming operation
//                if (this.mcodeUtil.getCurrentParameters()
                if (this.clusterUtil.getCurrentParameters()
                        .getResultParams(this.resultId).isWeak()) //      clusters = ClusterUtil.sortClusters3(clusters);
                {
//                    mcodeUtil.sortClusters3(clusters);
                    clusterUtil.sortClusters3(clusters);
                } else {
//                    mcodeUtil.sortClusters2(clusters);
                    clusterUtil.sortClusters2(clusters);
                }

                //clusters = ClusterUtil.sortClusters2(clusters);

//                int imageSize = this.mcodeUtil.getCurrentParameters()
//                        .getResultParams(this.resultId)
//                        .getDefaultRowHeight();
                int imageSize = this.clusterUtil.getCurrentParameters()
                        .getResultParams(this.resultId)
                        .getDefaultRowHeight();


                /*for (int i = 0; i < clusters.length; i++)
                 clusterL.add(clusters[i]);*/



                int count = 0;

                for (Cluster c : clusters) {
                    if (this.interrupted) {
                        return;
                    }


//                    final Image img = mcodeUtil.createClusterImage(c, imageSize, imageSize, null, true, null);
                    final Image img = clusterUtil.createClusterImage(c, imageSize, imageSize, null, true, null);

                    /*Image img = this.mcodeUtil.convertClusterToImage(null,
                     c, imageSize, imageSize, null, true);*/
//                    c.setImage(img);
//                    taskMonitor.setProgress(++count / (double) clusters.size());
                }

                success = true;

                /*
                 * imageList = new Image[clusters.length]; for (int i = 0; i
                 * < clusters.length; i++) { if (interrupted) { return; }
                 * imageList[i] = clusterUtil.convertClusterToImage(null,
                 * clusters[i], imageSize, imageSize, null, true);
                 * taskMonitor.setProgress((i * 100) / clusters.length); }
                 * success = true;
                 */
                // }
                if (interrupted) {
                    return;
                }
//            }


        } catch (Exception e) {
            throw new Exception("Error while executing the MCODE analysis", e);
        } finally {
//                      mcodeUtil.destroyUnusedNetworks(network, clusters);//±£ÁôÃ¿Ò»´ÎµÄ½á¹û£¬Ê¹ÏÂ´Î¿ÉÒÔ¼ÌÐø·ÃÎÊ

//            if (listener != null) {
//                listener.handleEvent(new AnalysisCompletedEvent(success, clusters));
//            }
        }
    }

    public void cancel() {
        this.interrupted = true;
        this.alg.setCancelled(true);
//        this.mcodeUtil.removeNetworkResult(this.resultId);
        this.clusterUtil.removeNetworkResult(this.resultId);
//        this.mcodeUtil.removeNetworkAlgorithm(this.network.getSUID()
//                .longValue());
        this.clusterUtil.removeNetworkAlgorithm(this.network.getSUID()
                .longValue());
    }

    public String getTitle() {
        return "MCODE Network Cluster Detection";
    }
}
