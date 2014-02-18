package justclust.plugins.parsing;

import java.io.File;
import java.util.ArrayList;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;

/**
 * This interface defines classes which have instances which act as file
 * parsers.
 */
public interface FileParserPluginInterface {

    public String getFileType() throws Exception;

    public String getDescription() throws Exception;

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception;

    /**
     * This method signature defines a method which parses a file.
     */
    public void parseFile(File file, ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges) throws Exception;
    
    public boolean isMicroarrayData() throws Exception;
    
    public ArrayList<String> getMicroarrayHeaders() throws Exception;
    
}
