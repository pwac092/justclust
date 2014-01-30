package justclust.plugins.visualisation;

import java.util.ArrayList;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControl;

public interface VisualisationLayoutPluginInterface {

    public String getName() throws Exception;

    public String getDescription() throws Exception;

    public ArrayList<PluginConfigurationControl> getConfigurationControls() throws Exception;

    public void applyLayout(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) throws Exception;
}
