package justclust.plugins.parsing.sequence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import justclust.Main;

import justclust.datastructures.Data;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import justclust.plugins.configurationcontrols.PluginConfigurationControlInterface;
import justclust.plugins.configurationcontrols.TextFieldControl;
import justclust.plugins.parsing.FileParserPluginInterface;

/**
 * This class has instances which act as a file parser for sequence data files.
 */
public class SequenceFileParser implements FileParserPluginInterface {

    // fileSystemPathControl allows the getConfigurationControls and parseFile
    // methods to share the text field of this FileSystemPathControl
    public FileSystemPathControl fileSystemPathControl;

    public String getFileType() throws Exception {
        return "Sequence (.fasta) (uses BLAST)";
    }

    public String getDescription() throws Exception {

        String description = "This file parser plug-in parses FASTA (.fasta) files with BLAST."
                + '\n'
                + "You need to have BLAST installed on your computer to use this plug-in.";

        try {
            Runtime.getRuntime().exec(new String[]{"makeblastdb"});
            Runtime.getRuntime().exec(new String[]{"blastp"});
        } catch (IOException ioException) {
            description = "This file parser plug-in parses FASTA (.fasta) files with BLAST."
                    + '\n'
                    + "You need to have BLAST installed on your computer to use this plug-in."
                    + '\n'
                    + "If the file path of the binary files from BLAST is included in your PATH environment variable, you may leave the 'BLAST Binary Files Path' field blank.";
        }

        return description;

    }

    public ArrayList<PluginConfigurationControlInterface> getConfigurationControls() throws Exception {

        ArrayList<PluginConfigurationControlInterface> controls = new ArrayList<PluginConfigurationControlInterface>();

        fileSystemPathControl = new FileSystemPathControl();
        fileSystemPathControl.label = "BLAST Binary Files Path:";
        fileSystemPathControl.text = "";
        fileSystemPathControl.directoriesOnly = true;

        try {
            Runtime.getRuntime().exec(new String[]{"makeblastdb"});
            Runtime.getRuntime().exec(new String[]{"blastp"});
        } catch (IOException ioException) {
            controls.add(fileSystemPathControl);
        }

        return controls;

    }

    /**
     * This method parses a sequence data file.
     */
    public void parseFile(File file, ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges) throws Exception {

        Runtime runtime = Runtime.getRuntime();

        // This code creates a copy of a file. The copy is called input.
        Scanner scanner = new Scanner(file);
        File copyOfFile = new File("input");
        FileWriter fileWriter = new FileWriter(copyOfFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        while (scanner.hasNextLine()) {
            bufferedWriter.write(scanner.nextLine());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        fileWriter.close();
        scanner.close();

        // This code executes a makeblastdb program to create a BLAST database
        // with
        // the contents of a file.
        String[] parameters = new String[]{
            fileSystemPathControl.text + "makeblastdb",
            "-in", copyOfFile.getName(), "-out", "database", "-dbtype",
            "prot"};
        Process process = runtime.exec(parameters);
        process.waitFor();

        // This code executes a blastp program to perform a BLAST sequence
        // alignment algorithm with the BLAST database.
        parameters = new String[9];
        parameters[0] = fileSystemPathControl.text + "blastp";
        parameters[1] = "-query";
        parameters[2] = copyOfFile.getName();
        parameters[3] = "-db";
        parameters[4] = "database";
        parameters[5] = "-out";
        parameters[6] = "output";
        parameters[7] = "-outfmt";
        parameters[8] = "6 std qlen";
        process = runtime.exec(parameters);
        process.waitFor();

        // This code creates the networkEdges and networkNodes data
        // structures with the results of the execution of the blastp program.
        scanner = new Scanner(new File("output"));
        Hashtable<String, Node> hashTable = new Hashtable<String, Node>();
        while (scanner.hasNextLine()) {

            Scanner lineScanner = new Scanner(scanner.nextLine());
            lineScanner.useDelimiter("\t");

            Edge edge = new Edge();
            networkEdges.add(edge);

            String identifier = lineScanner.next();
            if (identifier.contains("|") && identifier.split("\\|").length >= 2) {
                identifier = ((String[]) identifier.split("\\|"))[1];
            }
            if (hashTable.containsKey(identifier)) {
                edge.node1 = hashTable.get(identifier);
            } else {
                edge.node1 = new Node();
                edge.node1.label = identifier;
                hashTable.put(identifier, edge.node1);
            }

            identifier = lineScanner.next();
            if (identifier.contains("|") && identifier.split("\\|").length >= 2) {
                identifier = ((String[]) identifier.split("\\|"))[1];
            }
            if (hashTable.containsKey(identifier)) {
                edge.node2 = hashTable.get(identifier);
            } else {
                edge.node2 = new Node();
                edge.node2.label = identifier;
                hashTable.put(identifier, edge.node2);
            }

            for (int i = 0; i < 8; i++) {
                lineScanner.next();
            }

            Double weight = Double.valueOf(lineScanner.next());
            if (weight == 0.0) {
                edge.weight = 1.0;
            }
            edge.weight = 1.0 / (1 + Math.pow(weight, 6.1302 / Math.log(10)) * Math.exp(1.2112));

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
            for (j = i - 1; j >= 0 && edge.weight > networkEdges.get(j).weight; j--) {
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

        // This code deletes the files which were used during the execution of
        // this method, and are no longer necessary.
        copyOfFile.delete();
        new File("database.phr").delete();
        new File("database.pin").delete();
        new File("database.psq").delete();
        new File("output").delete();

    }

    public boolean isMicroarrayData() throws Exception {
        return false;
    }

    public ArrayList<String> getMicroarrayHeaders() throws Exception {
        return null;
    }
}
