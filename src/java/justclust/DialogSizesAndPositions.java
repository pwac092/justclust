/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JDialog;
import justclust.toolbar.networkdetails.NetworkDetailsJDialog;

public class DialogSizesAndPositions {

    public static int networkDetailsXCoordinate;
    public static int networkDetailsYCoordinate;
    public static int networkDetailsWidth;
    public static int networkDetailsHeight;
    public static int networkDetailsCaretPositionToScrollTo;
    public static int networkNodesXCoordinate;
    public static int networkNodesYCoordinate;
    public static int networkNodesWidth;
    public static int networkNodesHeight;
    public static int networkNodesRowToScrollTo;
    public static int networkEdgesXCoordinate;
    public static int networkEdgesYCoordinate;
    public static int networkEdgesWidth;
    public static int networkEdgesHeight;
    public static int networkEdgesRowToScrollTo;
    public static int networkClustersXCoordinate;
    public static int networkClustersYCoordinate;
    public static int networkClustersWidth;
    public static int networkClustersHeight;
    public static int networkClustersRowToScrollTo;
    public static int searchNetworkXCoordinate;
    public static int searchNetworkYCoordinate;
    public static int filterClustersXCoordinate;
    public static int filterClustersYCoordinate;
    public static int overrepresentationAnalysisXCoordinate;
    public static int overrepresentationAnalysisYCoordinate;
    public static int heatMapXCoordinate;
    public static int heatMapYCoordinate;
    public static int heatMapWidth;
    public static int heatMapHeight;
    public static boolean heatMapIncludeLabels;
    public static int microarrayHeatMapXCoordinate;
    public static int microarrayHeatMapYCoordinate;
    public static int microarrayHeatMapWidth;
    public static int microarrayHeatMapHeight;
    public static boolean microarrayHeatMapIncludeLabels;
    public static int dendrogramXCoordinate;
    public static int dendrogramYCoordinate;
    public static int dendrogramWidth;
    public static int dendrogramHeight;
    public static int managePluginsXCoordinate;
    public static int managePluginsYCoordinate;

    public DialogSizesAndPositions() {

        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();

        networkDetailsXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        networkDetailsYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        networkDetailsWidth = 500;
        networkDetailsHeight = 800;
        networkDetailsCaretPositionToScrollTo = 0;

        networkNodesXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        networkNodesYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        networkNodesWidth = 500;
        networkNodesHeight = 800;
        networkClustersRowToScrollTo = 0;

        networkEdgesXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        networkEdgesYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        networkEdgesWidth = 500;
        networkEdgesHeight = 800;

        networkClustersXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        networkClustersYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        networkClustersWidth = 500;
        networkClustersHeight = 800;

        JDialog jDialog = new JDialog();
        jDialog.setVisible(true);

        searchNetworkXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        searchNetworkYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 25 + 10 + 130 + 10 + 25 + 10 + 1 + 10
                + jDialog.getInsets().top + jDialog.getInsets().bottom)) / 2);

        filterClustersXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        filterClustersYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                + jDialog.getInsets().top + jDialog.getInsets().bottom)) / 2);

        overrepresentationAnalysisXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        overrepresentationAnalysisYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                - (10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 25 + 10 + 25 + 10 + 95 + 10 + 1 + 10
                + jDialog.getInsets().top + jDialog.getInsets().bottom)) / 2);

        heatMapXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        heatMapYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        heatMapWidth = 500;
        heatMapHeight = 800;
        heatMapIncludeLabels = true;

        microarrayHeatMapXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        microarrayHeatMapYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        microarrayHeatMapWidth = 500;
        microarrayHeatMapHeight = 800;
        microarrayHeatMapIncludeLabels = true;

        dendrogramXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        dendrogramYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight() - 800) / 2);
        dendrogramWidth = 500;
        dendrogramHeight = 800;

        managePluginsXCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getWidth() - 500) / 2);
        managePluginsYCoordinate = (int) Math.round((double) (devices[0].getDisplayMode().getHeight()
                - (10 + 16 + 10 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 10
                + jDialog.getInsets().top + jDialog.getInsets().bottom)) / 2);

        jDialog.dispose();

    }
}
