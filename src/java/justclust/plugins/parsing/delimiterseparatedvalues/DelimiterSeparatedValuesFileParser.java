package justclust.plugins.parsing.delimiterseparatedvalues;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.ComboBoxControl;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;

public class DelimiterSeparatedValuesFileParser implements FileParserPluginInterface {

    // comboBoxControl allows the getConfigurationControls and parseFile
    // methods to share the selectedOptionIndex field of this comboBoxControl
    public ComboBoxControl comboBoxControl;

    public String getFileType() throws Exception {
        return "Delimiter-separated values file (.csv, .tab, .tsv)";
    }

    public String getDescription() throws Exception {
        return "This file parser plug-in parses files containing values seperated by any delimiter, such as comma-separated values (.csv) files and tab-separated values (.tsv, .tab) files.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        comboBoxControl = new ComboBoxControl();
        comboBoxControl.label = "Delimiter Between Values:";
        comboBoxControl.options = new ArrayList<String>();
        comboBoxControl.options.add("Commas");
        comboBoxControl.options.add("Tabs");
        comboBoxControl.selectedOptionIndex = 0;

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();
        controls.add(comboBoxControl);

        return controls;

    }

    public void parseFile(File file, ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges) throws Exception {

        // This code creates the networkEdges and networkNodes data
        // structures with the contents of a file.
        Scanner scanner = new Scanner(file);
        Hashtable<String, Node> hashTable = new Hashtable<String, Node>();
        while (scanner.hasNextLine()) {

            Scanner lineScanner = new Scanner(scanner.nextLine().trim());
            // the delimiter chosen by the user was commas
            if (comboBoxControl.options.get(comboBoxControl.selectedOptionIndex).equals("Commas")) {
                lineScanner.useDelimiter(",");
            }
            // the delimiter chosen by the user was tabs
            if (comboBoxControl.options.get(comboBoxControl.selectedOptionIndex).equals("Tabs")) {
                lineScanner.useDelimiter("\t");
            }

            Edge edge = new Edge();
            networkEdges.add(edge);

            String identifier = lineScanner.next().trim();
            if (hashTable.containsKey(identifier)) {
                edge.node1 = hashTable.get(identifier);
            } else {
                edge.node1 = new Node();
                edge.node1.label = identifier;
                hashTable.put(identifier, edge.node1);
            }

            identifier = lineScanner.next().trim();
            if (hashTable.containsKey(identifier)) {
                edge.node2 = hashTable.get(identifier);
            } else {
                edge.node2 = new Node();
                edge.node2.label = identifier;
                hashTable.put(identifier, edge.node2);
            }

            edge.weight = Double.valueOf(lineScanner.next().trim());

            lineScanner.close();

        }
        ArrayList<Node> arrayList = new ArrayList<Node>(hashTable.values());
        for (Node node : arrayList) {
            networkNodes.add(node);
        }
        scanner.close();

        // This code sorts the Edges in the networkEdges data
        // structure from largest weight field to smallest weight field.
        for (int i = 1; i < networkEdges.size(); i++) {
            Edge edge = networkEdges.get(i);
            int j;
            for (j = i - 1; j >= 0
                    && edge.weight > networkEdges.get(j).weight; j--) {
                networkEdges.set(j + 1, networkEdges.get(j));
            }
            networkEdges.set(j + 1, edge);
        }

        // This code creates, for each Edge, the Node.edges data structure of
        // each of its two Nodes.
        for (Node node : networkNodes) {
            node.edges = new ArrayList<Edge>();
        }
        for (Edge edge : networkEdges) {
            if (!edge.node1.edges.contains(edge)) {
                edge.node1.edges.add(edge);
            }
            if (!edge.node2.edges.contains(edge)) {
                edge.node2.edges.add(edge);
            }
        }

    }

    public boolean isMicroarrayData() throws Exception {
        return false;
    }

    public ArrayList<String> getMicroarrayHeaders() throws Exception {
        return null;
    }
}
