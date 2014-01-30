/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.plugins.clustering.mcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the current parameters for MCODE.  Parameters are entered in the MCODEMainPanel and
 * stored in a hash map for the particular network being analyzed by the MCODEAnalyzeAction
 * if the analysis produced a result.
 */
public class McodeCurrentParameters {

//	private Map<Long, MCODEParameterSet> currentParams = new HashMap<Long, MCODEParameterSet>();
	private Map<Long, McodeParameterSet> currentParams = new HashMap<Long, McodeParameterSet>();
//	private Map<Integer, MCODEParameterSet> resultParams = new HashMap<Integer, MCODEParameterSet>();
	private Map<Integer, McodeParameterSet> resultParams = new HashMap<Integer, McodeParameterSet>();

	/**
	 * Get a copy of the current parameters for a particular network. Only a copy of the current param object is
	 * returned to avoid side effects.  The user should use the following code to get their
	 * own copy of the current parameters:
	 * MCODECurrentParameters.getInstance().getParamsCopy();
	 * <p/>
	 * Note: parameters can be changed by the user after you have your own copy,
	 * so if you always need the latest, you should get the updated parameters again.                                                    
	 *
	 * @param networkID Id of the network
	 * @return A copy of the parameters
	 */
//	public MCODEParameterSet getParamsCopy(Long networkID) {
	public McodeParameterSet getParamsCopy(Long networkID) {
		if (networkID != null) {
			return currentParams.get(networkID).copy();
		} else {
//			MCODEParameterSet newParams = new MCODEParameterSet();
			McodeParameterSet newParams = new McodeParameterSet();
			return newParams.copy();
		}
	}

	/**
	 * Current parameters can only be updated using this method.
	 * This method is called by MCODEAnalyzeAction after comparisons have been conducted
	 * between the last saved version of the parameters and the current user's version.
	 *
	 * @param newParams The new current parameters to set
	 * @param resultId Id of the result set
	 * @param networkID Id of the network
	 */
//	public void setParams(MCODEParameterSet newParams, int resultId, Long networkID) {
	public void setParams(McodeParameterSet newParams, int resultId, Long networkID) {
		//cannot simply equate the params and newParams classes since that creates a permanent reference
		//and prevents us from keeping 2 sets of the class such that the saved version is not altered
		//until this method is called
//		MCODEParameterSet currentParamSet = new MCODEParameterSet(newParams.getScope(), newParams.getSelectedNodes(),
//																  newParams.isIncludeLoops(), newParams
//																		  .getDegreeCutoff(), newParams.getKCore(),
//																  newParams.isOptimize(), newParams
//																		  .getMaxDepthFromStart(), newParams
//																		  .getNodeScoreCutoff(), newParams.isFluff(),
//																  newParams.isHaircut(), newParams
//																		  .getFluffNodeDensityCutoff());
		McodeParameterSet currentParamSet = new McodeParameterSet(newParams.getScope(), newParams.getSelectedNodes(),
																  newParams.isIncludeLoops(), newParams
																		  .getDegreeCutoff(), newParams.getKCore(),
																  newParams.isOptimize(), newParams
																		  .getMaxDepthFromStart(), newParams
																		  .getNodeScoreCutoff(), newParams.isFluff(),
																  newParams.isHaircut(), newParams
																		  .getFluffNodeDensityCutoff());

		currentParams.put(networkID, currentParamSet);

//		MCODEParameterSet resultParamSet = new MCODEParameterSet(newParams.getScope(), newParams.getSelectedNodes(),
//																 newParams.isIncludeLoops(), newParams
//																		 .getDegreeCutoff(), newParams.getKCore(),
//																 newParams.isOptimize(), newParams
//																		 .getMaxDepthFromStart(), newParams
//																		 .getNodeScoreCutoff(), newParams.isFluff(),
//																 newParams.isHaircut(), newParams
//																		 .getFluffNodeDensityCutoff());
		McodeParameterSet resultParamSet = new McodeParameterSet(newParams.getScope(), newParams.getSelectedNodes(),
																 newParams.isIncludeLoops(), newParams
																		 .getDegreeCutoff(), newParams.getKCore(),
																 newParams.isOptimize(), newParams
																		 .getMaxDepthFromStart(), newParams
																		 .getNodeScoreCutoff(), newParams.isFluff(),
																 newParams.isHaircut(), newParams
																		 .getFluffNodeDensityCutoff());

		resultParams.put(resultId, resultParamSet);
	}

//	public MCODEParameterSet getResultParams(int resultId) {
	public McodeParameterSet getResultParams(int resultId) {
		return resultParams.get(resultId).copy();
	}

	public void removeResultParams(int resultId) {
		resultParams.remove(resultId);
	}
}
