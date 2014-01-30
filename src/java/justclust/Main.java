package justclust;

import justclust.datastructures.Data;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * This class has a method which is the entry point of execution for JustClust.
 */
public class Main {

    /**
     * This method is the entry point of execution for JustClust.
     */
    public static void main(String[] args) {

//        // handle an incorrect version of Java running JustClust
//        if (Integer.parseInt(System.getProperty("java.version").split("\\.")[1]) < 7)
//        {
//            System.out.println("To run JustClust, you will need to update Java on your computer to the latest version at www.java.com.");
//            return;
//        }

        // the constructor creates the class instance for the Data
        // class and initialises some variables which contain user preferences
        new Data();
        Data.data = new ArrayList<Data>();

        new DialogSizesAndPositions();

        // This code constructs a JustclustJFrame within the event dispatch thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JustclustJFrame();
            }
        });

    }
}
