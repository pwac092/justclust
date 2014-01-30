/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.fagec;

/**
 *
 * @author wuaz008
 */
public class ParameterSet {

    public static String NETWORK = "network";
    public static String SELECTION = "selection";
    private String scope;
    private Long[] selectedNodes;
    private boolean includeLoops;
    private int degreeCutoff;
    private int kCore;
    private boolean optimize;
    private int maxDepthFromStart;
    private double nodeScoreCutoff;
    private boolean fluff;
    private boolean haircut;
    private double fluffNodeDensityCutoff;
    private int defaultRowHeight;
    //used in cluster finding stage
//  private double nodeDensityThreshold;   
    //parameter used when clustering using EAGLE
    private int cliqueSizeThreshold1;
    private int complexSizeThreshold1;
    //used in clustering using FAG-EC
    private boolean overlapped;
    private double fThreshold;
    private int cliqueSizeThreshold;
    private int complexSizeThreshold;
    private boolean isWeak;
    //result viewing parameters (only used for dialog box of results)
    public static String MCODE = "MCODE";
    public static String EAGLE = "EAGLE";
    public static String FAGEC = "FAG-EC";
    private String algorithm;

    public ParameterSet() {
        setDefaultParams();

        this.defaultRowHeight = 80;
    }

//    setAllAlgorithmParams(Cytoscape.getCurrentNetwork().getIdentifier(), NETWORK, "", new Integer[0], 
//              false, 2, 2, false, 100, 0.2, false, true, 0.1, 3, 2, 1.0, 3, 2, true,false);
    //      NETWORK,  new Integer[0],       false, 2, 2, false, 100, 0.2, false,true,0.1,  3, 2, false,1.0, 3, 2,true
    public ParameterSet(String scope, Long[] selectedNodes, boolean includeLoops, int degreeCutoff,
            int kCore, boolean optimize, int maxDepthFromStart, double nodeScoreCutoff, boolean fluff,
            boolean haircut, double fluffNodeDensityCutoff,
            /*  double nodeDensityThreshold,*/ int cliqueSizeThreshold1,
            int complexSizeThreshold1, boolean overlapped,
            double fThreshold, int cliqueSizeThreshold,
            int complexSizeThreshold, boolean isWeak,
            String algorithm) {
        setAllAlgorithmParams(scope,
                selectedNodes,
                includeLoops,
                degreeCutoff,
                kCore,
                optimize,
                maxDepthFromStart,
                nodeScoreCutoff,
                fluff,
                haircut,
                fluffNodeDensityCutoff,
                /*nodeDensityThreshold, */ cliqueSizeThreshold1,
                complexSizeThreshold1, overlapped,
                fThreshold, cliqueSizeThreshold,
                complexSizeThreshold, isWeak,
                algorithm);


        this.defaultRowHeight = 80;
    }

    public void setDefaultParams() {
        setAllAlgorithmParams(NETWORK, new Long[0],
                false, 2, 2, false, 100, 0.2, false, true,
                0.1, 3, 2, false, 1.0, 3, 2, true, "");





    }

    public void setAllAlgorithmParams(String scope, Long[] selectedNodes, boolean includeLoops,
            int degreeCutoff, int kCore, boolean optimize, int maxDepthFromStart,
            double nodeScoreCutoff, boolean fluff, boolean haircut,
            double fluffNodeDensityCutoff,
            /* double nodeDensityThreshold, */ int cliqueSizeThreshold1,
            int complexSizeThreshold1, boolean overlapped,
            double fThreshold, int cliqueSizeThreshold,
            int complexSizeThreshold, boolean isWeak,
            String algorithm) {
        this.scope = scope;
        this.selectedNodes = selectedNodes;
        this.includeLoops = includeLoops;
        this.degreeCutoff = degreeCutoff;
        this.kCore = kCore;
        this.optimize = optimize;
        this.maxDepthFromStart = maxDepthFromStart;
        this.nodeScoreCutoff = nodeScoreCutoff;
        this.fluff = fluff;
        this.haircut = haircut;
        this.fluffNodeDensityCutoff = fluffNodeDensityCutoff;

//    this.nodeDensityThreshold=nodeDensityThreshold;
        this.cliqueSizeThreshold1 = cliqueSizeThreshold1;
        this.complexSizeThreshold1 = complexSizeThreshold1;
        this.overlapped = overlapped;
        this.fThreshold = fThreshold;
        this.cliqueSizeThreshold = cliqueSizeThreshold;
        this.complexSizeThreshold = complexSizeThreshold;

        this.isWeak = isWeak;


        this.algorithm = algorithm;
    }

    public ParameterSet copy() {
        ParameterSet newParam = new ParameterSet();
        newParam.setScope(this.scope);
        newParam.setSelectedNodes(this.selectedNodes);
        newParam.setIncludeLoops(this.includeLoops);
        newParam.setDegreeCutoff(this.degreeCutoff);
        newParam.setKCore(this.kCore);
        newParam.setOptimize(this.optimize);
        newParam.setMaxDepthFromStart(this.maxDepthFromStart);
        newParam.setNodeScoreCutoff(this.nodeScoreCutoff);
        newParam.setFluff(this.fluff);
        newParam.setHaircut(this.haircut);
        newParam.setFluffNodeDensityCutoff(this.fluffNodeDensityCutoff);

        newParam.setDefaultRowHeight(this.defaultRowHeight);
        newParam.setAlgorithm(this.algorithm);
//    return newParam;


        newParam.setCliqueSizeThreshold1(this.cliqueSizeThreshold1);
        newParam.setComplexSizeThreshold1(this.complexSizeThreshold1);
        newParam.setCliqueSizeThreshold(this.cliqueSizeThreshold);
        newParam.setComplexSizeThreshold(this.complexSizeThreshold);

        newParam.setfThreshold(this.fThreshold);
        newParam.setWeak(this.isWeak);
        newParam.setOverlapped(this.overlapped);
        //results dialog box
        newParam.setDefaultRowHeight(this.defaultRowHeight);
        return newParam;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long[] getSelectedNodes() {
        return this.selectedNodes;
    }

    public void setSelectedNodes(Long[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public boolean isIncludeLoops() {
        return this.includeLoops;
    }

    public void setIncludeLoops(boolean includeLoops) {
        this.includeLoops = includeLoops;
    }

    public int getDegreeCutoff() {
        return this.degreeCutoff;
    }

    public void setDegreeCutoff(int degreeCutoff) {
        this.degreeCutoff = degreeCutoff;
    }

    public int getKCore() {
        return this.kCore;
    }

    public void setKCore(int kCore) {
        this.kCore = kCore;
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isOptimize() {
        return this.optimize;
    }

    public int getMaxDepthFromStart() {
        return this.maxDepthFromStart;
    }

    public void setMaxDepthFromStart(int maxDepthFromStart) {
        this.maxDepthFromStart = maxDepthFromStart;
    }

    public double getNodeScoreCutoff() {
        return this.nodeScoreCutoff;
    }

    public void setNodeScoreCutoff(double nodeScoreCutoff) {
        this.nodeScoreCutoff = nodeScoreCutoff;
    }

    public boolean isFluff() {
        return this.fluff;
    }

    public void setFluff(boolean fluff) {
        this.fluff = fluff;
    }

    public boolean isHaircut() {
        return this.haircut;
    }

    public void setHaircut(boolean haircut) {
        this.haircut = haircut;
    }

    public double getFluffNodeDensityCutoff() {
        return this.fluffNodeDensityCutoff;
    }

    public void setFluffNodeDensityCutoff(double fluffNodeDensityCutoff) {
        this.fluffNodeDensityCutoff = fluffNodeDensityCutoff;
    }

    public int getDefaultRowHeight() {
        return this.defaultRowHeight;
    }

    public void setDefaultRowHeight(int defaultRowHeight) {
        this.defaultRowHeight = defaultRowHeight;
    }

    public String toString() {
        /* String lineSep = System.getProperty("line.separator");
         StringBuffer sb = new StringBuffer();
         sb.append("   Network Scoring:" + lineSep + "      Include Loops: " + this.includeLoops + "  Degree Cutoff: " + 
         this.degreeCutoff + lineSep);
         sb.append("   Cluster Finding:" + lineSep + "      Node Score Cutoff: " + this.nodeScoreCutoff + "  Haircut: " + 
         this.haircut + "  Fluff: " + this.fluff + (
         this.fluff ? "  Fluff Density Cutoff " + this.fluffNodeDensityCutoff : "") + "  K-Core: " + this.kCore + 
         "  Max. Depth from Seed: " + this.maxDepthFromStart + lineSep);
         return sb.toString();*/

        String lineSep = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
//      sb.append("   Network: "+networkID);
        if (algorithm.equals(MCODE)) {
            sb.append("   Algorithm:  MCODE" + lineSep);
            sb.append("   Scoring:" + lineSep
                    + "      IncludeLoop: " + includeLoops + "  DegreeThreshold: " + degreeCutoff + lineSep);
            sb.append("   Clustering:" + lineSep
                    + "      NodeScoreThreshold: " + nodeScoreCutoff + "  Haircut: " + haircut + lineSep
                    + "      Fluff: " + fluff + ((fluff) ? ("  FluffNodeDensityThreshold " + fluffNodeDensityCutoff) : "") + lineSep
                    + "      K-Core: " + kCore + "  Max.DepthFromSeed: " + maxDepthFromStart + lineSep);
        } else if (algorithm.equals(FAGEC)) {
            sb.append("   Algorithm:  FAG-EC" + lineSep);
            sb.append("   Clustering:" + lineSep
                    + "      DefinitionWay: " + ((isWeak) ? ("Weak  In/OutThreshold: " + fThreshold) : "Strong") + lineSep
                    + "      Overlapped: " + overlapped + ((overlapped) ? (" CliqueSizeThreshold: " + cliqueSizeThreshold) : "") + lineSep
                    + "      OutputThreshold: " + complexSizeThreshold + lineSep);
        } else if (algorithm.equals(EAGLE)) {
            sb.append("   Algorithm:  EAGLE" + lineSep);
            sb.append("   Clustering:" + lineSep
                    + "      CliqueSizeThrshold: " + cliqueSizeThreshold1
                    + "  OutputThreshold: " + complexSizeThreshold1 + lineSep);
        }
        return sb.toString();
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {


        this.algorithm = algorithm;
    }

    /*public double getNodeDensityThreshold() {
     return nodeDensityThreshold;
     }

     public void setNodeDensityThreshold(double nodeDensityThreshold) {
     this.nodeDensityThreshold = nodeDensityThreshold;
     }
     */
    public int getCliqueSizeThreshold1() {
        return cliqueSizeThreshold1;
    }

    public void setCliqueSizeThreshold1(int cliqueSizeThreshold1) {
        this.cliqueSizeThreshold1 = cliqueSizeThreshold1;
    }

    public int getComplexSizeThreshold1() {
        return complexSizeThreshold1;
    }

    public void setComplexSizeThreshold1(int complexSizeThreshold1) {
        this.complexSizeThreshold1 = complexSizeThreshold1;
    }

    public boolean isOverlapped() {
        return overlapped;
    }

    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    public double getfThreshold() {
        return fThreshold;
    }

    public void setfThreshold(double fThreshold) {
        this.fThreshold = fThreshold;
    }

    public int getCliqueSizeThreshold() {
        return cliqueSizeThreshold;
    }

    public void setCliqueSizeThreshold(int cliqueSizeThreshold) {
        this.cliqueSizeThreshold = cliqueSizeThreshold;
    }

    public int getComplexSizeThreshold() {
        return complexSizeThreshold;
    }

    public void setComplexSizeThreshold(int complexSizeThreshold) {
        this.complexSizeThreshold = complexSizeThreshold;
    }

    public boolean isWeak() {
        return isWeak;
    }

    public void setWeak(boolean isWeak) {
        this.isWeak = isWeak;
    }
}
