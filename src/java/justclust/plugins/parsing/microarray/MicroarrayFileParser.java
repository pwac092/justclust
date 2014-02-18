package justclust.plugins.parsing.microarray;

import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.parsing.FileParserPluginInterface;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * This class has instances which act as a file parser for microarray data
 * files.
 */
public class MicroarrayFileParser implements FileParserPluginInterface {

    ArrayList<String> microarrayHeaders;

    public String getFileType() throws Exception {
        return "Microarray file (uses Pearson's product-moment correlation)";
    }

    public String getDescription() throws Exception {
        return "This file parser plug-in parses microarray (.tsv, .tab) files."
                + '\n'
                + "Pearson's product-moment correlation is used to calculate the correlation between microarray entries.";
    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {
        return new ArrayList<PluginConfigurationControlInterface>();
    }

    /**
     * This method parses a microarray data file.
     */
    public void parseFile(File file, ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges) throws Exception {

        Scanner scanner = new Scanner(file);

        microarrayHeaders = new ArrayList<String>();
        Scanner lineScanner = new Scanner(scanner.nextLine());
        lineScanner.useDelimiter("\t");
        while (lineScanner.hasNext()) {
            microarrayHeaders.add(lineScanner.next());
        }

        // This code creates a matrix data structure to represent the contents
        // of a file. Each row of the matrix data structure contains the tab
        // separated tokens of one line of the file with one token per entry.
        // The networkNodes data structure is also created with one Node
        // per row of the matrix data structure.
        ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
        int lineIndex = 0;
        WHILE_LOOP:
        while (scanner.hasNextLine()) {
            lineScanner = new Scanner(scanner.nextLine());
            lineScanner.useDelimiter("\t");
            Node node = new Node();
            node.edges = new ArrayList<Edge>();
            node.label = lineScanner.next();
            node.microarrayValues = new ArrayList<Double>();
            ArrayList<Double> arrayList = new ArrayList<Double>();
            for (int i = 0; i < microarrayHeaders.size(); i++) {
                try {
                    double value = Double.valueOf(lineScanner.next());
                    arrayList.add(value);
                    node.microarrayValues.add(value);
                } catch (NumberFormatException | NoSuchElementException exception) {
                    continue WHILE_LOOP;
                }
            }
            matrix.add(arrayList);
            networkNodes.add(node);
            lineIndex++;
            lineScanner.close();
        }
        Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(
                microarrayHeaders.size(), networkNodes.size());
        for (int i = 0; i < networkNodes.size(); i++) {
            for (int j = 0; j < microarrayHeaders.size(); j++) {
                array2DRowRealMatrix.setEntry(j, i, matrix.get(i).get(j));
            }
        }

        scanner.close();

        // This code computes the Pearson product-moment correlation coefficient
        // between each pair of different rows in the matrix data structure. The
        // networkEdges data structure is created as well. For each Pearson
        // product-moment correlation coefficient computed, an Edge is added to
        // the networkEdges data structure. The Edge is between the Nodes
        // which correspond to the rows of the matrix involved in the
        // computation.
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonsCorrelation
                .computeCorrelationMatrix(array2DRowRealMatrix);
        double minWeight = 0;
        for (int i = 0; i < networkNodes.size(); i++) {
            for (int j = 0; j < i; j++) {
                minWeight = Math.min(minWeight, correlationMatrix.getEntry(i, j));
            }
        }
        for (int i = 0; i < networkNodes.size(); i++) {
            for (int j = 0; j < i; j++) {
                Edge edge = new Edge();
                networkEdges.add(edge);
                edge.node1 = networkNodes.get(i);
                networkNodes.get(i).edges.add(edge);
                edge.node2 = networkNodes.get(j);
                networkNodes.get(j).edges.add(edge);
                edge.weight = correlationMatrix.getEntry(i, j) - minWeight;
            }
        }

    }

    public boolean isMicroarrayData() throws Exception {
        return true;
    }

    public ArrayList<String> getMicroarrayHeaders() throws Exception {
        return microarrayHeaders;
    }
}
