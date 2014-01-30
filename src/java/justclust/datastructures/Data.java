package justclust.datastructures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.toolbar.dendrogram.DendrogramCluster;

// this class contains data structures which are used by other classes.
// each instance of the Data class corresponds with a graph in a tab.
// this class implements Serializable because instances of the class are saved
// to a file when the user saves their session.
public class Data implements Serializable {

    // //////////////////////////////////////////////////
    // the following fields are specific to one graphs in one tab
    // //////////////////////////////////////////////////
    // the nodes in the network
    public ArrayList<Node> networkNodes;
    // the edges in the network
    public ArrayList<Edge> networkEdges;
    // the clusters in the network
    public ArrayList<Cluster> networkClusters;
    // this field contains the file name of the data file which was parsed to
    // create the current network
    public String fileName;
    // this field contains the display name of the current file parser plug-in
    public String fileParser;
    // this field contains the display name of the current file parser plug-in
    public String clusteringAlgorithm;
    // this field contains the time which was taken to create the current
    // network
    public long timeTakenToCreateNetwork;
    // this field contains the time which was taken to cluster the current
    // network
    public long timeTakenToClusterNetwork;
    // this field contains the time which was taken to layout the current
    // network
    public long timeTakenToApplyLayout;
    public boolean microarrayData;
    public ArrayList<String> microarrayHeaders;
    public boolean hierarchicalClustering;
    public ArrayList<DendrogramCluster> rootDendrogramClusters;
    
    // //////////////////////////////////////////////////
    // the following fields are global to all graphs in all tabs
    // //////////////////////////////////////////////////
    // this variable contains all of the Data instances for all graphs in order
    // of tabs (eg. the first element of the ArrayList corresponds to the graph
    // in the first tab)
    public static ArrayList<Data> data;
    // this field contains the jar file names of the current file parser
    // plug-ins
    public static ArrayList<String> fileParserJarNames;
    // this field contains the class file names of the current file parser
    // plug-ins
    public static ArrayList<String> fileParserClassNames;
    // this field contains the jar file names of the current clustering
    // algorithm plug-ins
    public static ArrayList<String> clusteringAlgorithmJarNames;
    // this field contains the class file names of the current clustering
    // algorithm plug-ins
    public static ArrayList<String> clusteringAlgorithmClassNames;
    // this field contains the jar file names of the current visualisation
    // layout plug-ins
    public static ArrayList<String> visualisationLayoutJarNames;
    // this field contains the class file names of the current visualisation
    // layout plug-ins
    public static ArrayList<String> visualisationLayoutClassNames;
    // this field contains the file path of the current file parser plug-ins
    public static String parsingPluginsPath;
    // this field contains the file path of the current clustering algorithm
    // plug-ins
    public static String clusteringPluginsPath;
    // this field contains the file path of the current visualisation layout
    // plug-ins
    public static String visualisationPluginsPath;
    // this Properties object contains the user preferences to be saved in a
    // file called config.properties in the user's home directory
    public static Properties prop;
    // lastAccessedFile contains the file on the user's hard drive which was
    // last accessed.
    // whenever the user browses their hard drive from within JustClust, this
    // lastAccessedFile will determine the directory which the file chooser
    // opens initially.
    // the user will be taken straight to the directory they last browsed to.
    public static File lastAccessedFile;

    // this constructor initialises variables with user preferences from a file
    // called config.properties in the user's home directory.
    /**
     * This constructor initialises the fields of this class.
     */
    public Data() {

        // get the settings directory for JustClust which is called
        // .JustClust and exists in the user's home directory.
        // this settings directory is hidden on unix-like operating systems
        // because of the period prefix.
        // the user's home directory is a good place for configuration files
        // because it will be writable by the user and if there are multiple
        // users on the same computer, they will all have their own
        // configuration file.
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".JustClust");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }

        // create a reference to the configuration file in the settings
        // directory
        File file = new File(settingsDirectory.getAbsoluteFile() + "/config.properties");

        prop = new Properties();

        // WRITE DEFAULT CONFIGURATION
        // if no user preferences are recorded in a configuration file, default
        // preferences are recorded.
        // this must be done before the 'READ EXISTING CONFIGURATION' code below
        // so that the default data is read into the system.
        if (!file.exists()) {

            try {
                //set the properties value.
                // these values are default values.
                prop.setProperty("parsing_plugins_path", "plugins/parsing");
                prop.setProperty("clustering_plugins_path", "plugins/clustering");
                prop.setProperty("visualisation_plugins_path", "plugins/visualisation");
                prop.setProperty("blast_binary_files_path", "");

                //save properties to project root folder
                prop.store(new FileOutputStream(file), null);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        // READ EXISTING CONFIGURATION
        // this consists of global variables being initialised with the recorded
        // user preferences in the configuration file
        try {

            //load a properties file
            prop.load(new FileInputStream(file));

            parsingPluginsPath = prop.getProperty("parsing_plugins_path");
            clusteringPluginsPath = prop.getProperty("clustering_plugins_path");
            visualisationPluginsPath = prop.getProperty("visualisation_plugins_path");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    // store user entered preference in the NewNetworkJDialog for the file path
    // of parsing plug-ins.
    // also, save this preference in the user preference configuration file.
    public static void saveParsingPluginsPath(String parsingPluginsPath) {

        Data.parsingPluginsPath = parsingPluginsPath;

        // get the settings directory for JustClust which is called
        // .JustClust and exists in the user's home directory.
        // this settings directory is hidden on unix-like operating systems
        // because of the period prefix.
        // the user's home directory is a good place for configuration files
        // because it will be writable by the user and if there are multiple
        // users on the same computer, they will all have their own
        // configuration file.
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".JustClust");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }

        // create a reference to the configuration file in the settings
        // directory
        File file = new File(settingsDirectory.getAbsoluteFile() + "/config.properties");

        // save the user specified path for parsing plug-ins into the
        // configuration file
        try {

            //set the properties value.
            prop.setProperty("parsing_plugins_path", parsingPluginsPath);

            //save properties to project root folder
            prop.store(new FileOutputStream(file), null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    // store user entered preference in the ClusterNetworkJDialog for the file path
    // of clustering plug-ins.
    // also, save this preference in the user preference configuration file.
    public static void saveClusteringPluginsPath(String clusteringPluginsPath) {

        Data.clusteringPluginsPath = clusteringPluginsPath;

        // get the settings directory for JustClust which is called
        // .JustClust and exists in the user's home directory.
        // this settings directory is hidden on unix-like operating systems
        // because of the period prefix.
        // the user's home directory is a good place for configuration files
        // because it will be writable by the user and if there are multiple
        // users on the same computer, they will all have their own
        // configuration file.
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".JustClust");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }

        // create a reference to the configuration file in the settings
        // directory
        File file = new File(settingsDirectory.getAbsoluteFile() + "/config.properties");

        // save the user specified path for clustering plug-ins into the
        // configuration file
        try {

            //set the properties value.
            prop.setProperty("clustering_plugins_path", clusteringPluginsPath);

            //save properties to project root folder
            prop.store(new FileOutputStream(file), null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    // store user entered preference in the LayoutNetworkJDialog for the file path
    // of visualisation plug-ins.
    // also, save this preference in the user preference configuration file.
    public static void saveVisualisationPluginsPath(String visualisationPluginsPath) {

        Data.visualisationPluginsPath = visualisationPluginsPath;

        // get the settings directory for JustClust which is called
        // .JustClust and exists in the user's home directory.
        // this settings directory is hidden on unix-like operating systems
        // because of the period prefix.
        // the user's home directory is a good place for configuration files
        // because it will be writable by the user and if there are multiple
        // users on the same computer, they will all have their own
        // configuration file.
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".JustClust");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }

        // create a reference to the configuration file in the settings
        // directory
        File file = new File(settingsDirectory.getAbsoluteFile() + "/config.properties");

        // save the user specified path for clustering plug-ins into the
        // configuration file
        try {

            //set the properties value.
            prop.setProperty("visualisation_plugins_path", visualisationPluginsPath);

            //save properties to project root folder
            prop.store(new FileOutputStream(file), null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
