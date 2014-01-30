/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import justclust.datastructures.Data;

/**
 *
 * @author wuaz008
 */
public class JustclustJPanel extends JPanel {

    // the images which are used for buttons which bring up dialogs with network
    // information in
    BufferedImage detailsButton;
    BufferedImage detailsButtonDisabled;
    Point detailsButtonCoordinates;
    BufferedImage nodesButton;
    BufferedImage nodesButtonDisabled;
    Point nodesButtonCoordinates;
    BufferedImage edgesButton;
    BufferedImage edgesButtonDisabled;
    Point edgesButtonCoordinates;
    BufferedImage clustersButton;
    BufferedImage clustersButtonDisabled;
    Point clustersButtonCoordinates;
    BufferedImage buttonSeparator;
    BufferedImage searchButton;
    BufferedImage searchButtonDisabled;
    Point searchButtonCoordinates;
    BufferedImage filterButton;
    BufferedImage filterButtonDisabled;
    Point filterButtonCoordinates;
    BufferedImage overrepresentationAnalysisButton;
    BufferedImage overrepresentationAnalysisButtonDisabled;
    Point overrepresentationAnalysisButtonCoordinates;
    BufferedImage heatMapButton;
    BufferedImage heatMapButtonDisabled;
    Point heatMapButtonCoordinates;
    BufferedImage microarrayHeatMapButton;
    BufferedImage microarrayHeatMapButtonDisabled;
    Point microarrayHeatMapButtonCoordinates;
    BufferedImage dendrogramButton;
    BufferedImage dendrogramButtonDisabled;
    Point dendrogramButtonCoordinates;
    BufferedImage managePluginsButton;
    Point managePluginsButtonCoordinates;

    JustclustJPanel() {

        // create the images for the buttons which bring up dialogs with network
        // information in
        try {
            detailsButton = ImageIO.read(new File("img/details_button.png"));
            detailsButtonDisabled = ImageIO.read(new File("img/details_button_disabled.png"));
            nodesButton = ImageIO.read(new File("img/nodes_button.png"));
            nodesButtonDisabled = ImageIO.read(new File("img/nodes_button_disabled.png"));
            edgesButton = ImageIO.read(new File("img/edges_button.png"));
            edgesButtonDisabled = ImageIO.read(new File("img/edges_button_disabled.png"));
            clustersButton = ImageIO.read(new File("img/clusters_button.png"));
            clustersButtonDisabled = ImageIO.read(new File("img/clusters_button_disabled.png"));
            buttonSeparator = ImageIO.read(new File("img/button_separator.png"));
            searchButton = ImageIO.read(new File("img/search_button.png"));
            searchButtonDisabled = ImageIO.read(new File("img/search_button_disabled.png"));
            filterButton = ImageIO.read(new File("img/filter_button.png"));
            filterButtonDisabled = ImageIO.read(new File("img/filter_button_disabled.png"));
            overrepresentationAnalysisButton = ImageIO.read(new File("img/overrepresentation_analysis_button.png"));
            overrepresentationAnalysisButtonDisabled = ImageIO.read(new File("img/overrepresentation_analysis_button_disabled.png"));
            heatMapButton = ImageIO.read(new File("img/heat_map_button.png"));
            heatMapButtonDisabled = ImageIO.read(new File("img/heat_map_button_disabled.png"));
            microarrayHeatMapButton = ImageIO.read(new File("img/microarray_heat_map_button.png"));
            microarrayHeatMapButtonDisabled = ImageIO.read(new File("img/microarray_heat_map_button_disabled.png"));
            dendrogramButton = ImageIO.read(new File("img/dendrogram_button.png"));
            dendrogramButtonDisabled = ImageIO.read(new File("img/dendrogram_button_disabled.png"));
            managePluginsButton = ImageIO.read(new File("img/manage_plugins_button.png"));
        } catch (IOException ex) {
        }
        Image transparentDetailsButton = TransformColorToTransparency(detailsButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentDetailsButtonDisabled = TransformColorToTransparency(detailsButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentNodesButton = TransformColorToTransparency(nodesButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentNodesButtonDisabled = TransformColorToTransparency(nodesButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentEdgesButton = TransformColorToTransparency(edgesButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentEdgesButtonDisabled = TransformColorToTransparency(edgesButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentClustersButton = TransformColorToTransparency(clustersButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentClustersButtonDisabled = TransformColorToTransparency(clustersButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentButtonSeparator = TransformColorToTransparency(buttonSeparator, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentSearchButton = TransformColorToTransparency(searchButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentSearchButtonDisabled = TransformColorToTransparency(searchButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentFilterButton = TransformColorToTransparency(filterButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentFilterButtonDisabled = TransformColorToTransparency(filterButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentOverrepresentationAnalysisButton = TransformColorToTransparency(overrepresentationAnalysisButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentOverrepresentationAnalysisButtonDisabled = TransformColorToTransparency(overrepresentationAnalysisButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentHeatMapButton = TransformColorToTransparency(heatMapButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentHeatMapButtonDisabled = TransformColorToTransparency(heatMapButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentMicroarrayHeatMapButton = TransformColorToTransparency(microarrayHeatMapButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentMicroarrayHeatMapButtonDisabled = TransformColorToTransparency(microarrayHeatMapButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentDendrogramButton = TransformColorToTransparency(dendrogramButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentDendrogramButtonDisabled = TransformColorToTransparency(dendrogramButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentManagePluginsButton = TransformColorToTransparency(managePluginsButton, new Color(255, 0, 255), new Color(255, 0, 255));
        detailsButton = ImageToBufferedImage(transparentDetailsButton, detailsButton.getWidth(), detailsButton.getHeight());
        detailsButtonDisabled = ImageToBufferedImage(transparentDetailsButtonDisabled, detailsButtonDisabled.getWidth(), detailsButtonDisabled.getHeight());
        nodesButton = ImageToBufferedImage(transparentNodesButton, nodesButton.getWidth(), nodesButton.getHeight());
        nodesButtonDisabled = ImageToBufferedImage(transparentNodesButtonDisabled, nodesButtonDisabled.getWidth(), nodesButtonDisabled.getHeight());
        edgesButton = ImageToBufferedImage(transparentEdgesButton, edgesButton.getWidth(), edgesButton.getHeight());
        edgesButtonDisabled = ImageToBufferedImage(transparentEdgesButtonDisabled, edgesButtonDisabled.getWidth(), edgesButtonDisabled.getHeight());
        clustersButton = ImageToBufferedImage(transparentClustersButton, clustersButton.getWidth(), clustersButton.getHeight());
        clustersButtonDisabled = ImageToBufferedImage(transparentClustersButtonDisabled, clustersButtonDisabled.getWidth(), clustersButtonDisabled.getHeight());
        buttonSeparator = ImageToBufferedImage(transparentButtonSeparator, buttonSeparator.getWidth(), buttonSeparator.getHeight());
        searchButton = ImageToBufferedImage(transparentSearchButton, searchButton.getWidth(), searchButton.getHeight());
        searchButtonDisabled = ImageToBufferedImage(transparentSearchButtonDisabled, searchButtonDisabled.getWidth(), searchButtonDisabled.getHeight());
        filterButton = ImageToBufferedImage(transparentFilterButton, filterButton.getWidth(), filterButton.getHeight());
        filterButtonDisabled = ImageToBufferedImage(transparentFilterButtonDisabled, filterButtonDisabled.getWidth(), filterButtonDisabled.getHeight());
        overrepresentationAnalysisButton = ImageToBufferedImage(transparentOverrepresentationAnalysisButton, overrepresentationAnalysisButton.getWidth(), overrepresentationAnalysisButton.getHeight());
        overrepresentationAnalysisButtonDisabled = ImageToBufferedImage(transparentOverrepresentationAnalysisButtonDisabled, overrepresentationAnalysisButtonDisabled.getWidth(), overrepresentationAnalysisButtonDisabled.getHeight());
        heatMapButton = ImageToBufferedImage(transparentHeatMapButton, heatMapButton.getWidth(), heatMapButton.getHeight());
        heatMapButtonDisabled = ImageToBufferedImage(transparentHeatMapButtonDisabled, heatMapButtonDisabled.getWidth(), heatMapButtonDisabled.getHeight());
        microarrayHeatMapButton = ImageToBufferedImage(transparentMicroarrayHeatMapButton, microarrayHeatMapButton.getWidth(), microarrayHeatMapButton.getHeight());
        microarrayHeatMapButtonDisabled = ImageToBufferedImage(transparentMicroarrayHeatMapButtonDisabled, microarrayHeatMapButtonDisabled.getWidth(), microarrayHeatMapButtonDisabled.getHeight());
        dendrogramButton = ImageToBufferedImage(transparentDendrogramButton, dendrogramButton.getWidth(), dendrogramButton.getHeight());
        dendrogramButtonDisabled = ImageToBufferedImage(transparentDendrogramButtonDisabled, dendrogramButtonDisabled.getWidth(), dendrogramButtonDisabled.getHeight());
        managePluginsButton = ImageToBufferedImage(transparentManagePluginsButton, managePluginsButton.getWidth(), managePluginsButton.getHeight());

        // set the coordinates of the buttons
        detailsButtonCoordinates = new Point(3, 3);
        nodesButtonCoordinates = new Point(3, 46);
        edgesButtonCoordinates = new Point(3, 89);
        clustersButtonCoordinates = new Point(3, 132);
        searchButtonCoordinates = new Point(3, 188);
        filterButtonCoordinates = new Point(3, 231);
        overrepresentationAnalysisButtonCoordinates = new Point(3, 274);
        heatMapButtonCoordinates = new Point(3, 317);
        microarrayHeatMapButtonCoordinates = new Point(3, 360);
        dendrogramButtonCoordinates = new Point(3, 403);
        managePluginsButtonCoordinates = new Point(3, 459);

    }

    // this method extends the super class' paint method with additional
    // graphics to display in the JustclustJPanel
    public void paint(Graphics g) {

        // the paint method of the super class is called so that the parts of
        // the JustclustJPanel which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        // get the current Data instance for the following code to use
        int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
        Data data;
        if (currentCustomGraphEditorIndex >= 0) {
            data = Data.data.get(currentCustomGraphEditorIndex);
        } else {
            data = new Data();
        }

        // drawing the buttons for the information toolbar
        Image detailsButton = null;
        Image nodesButton = null;
        Image edgesButton = null;
        Image clustersButton = null;
        Image searchButton = null;
        Image filterButton = null;
        Image overrepresentationAnalysisButton = null;
        Image heatMapButton = null;
        Image microarrayHeatMapButton = null;
        Image dendrogramButton = null;
        if (data.networkNodes == null) {
            detailsButton = this.detailsButtonDisabled;
            nodesButton = this.nodesButtonDisabled;
            edgesButton = this.edgesButtonDisabled;
            clustersButton = this.clustersButtonDisabled;
            searchButton = this.searchButtonDisabled;
            filterButton = this.filterButtonDisabled;
            overrepresentationAnalysisButton = this.overrepresentationAnalysisButtonDisabled;
            heatMapButton = this.heatMapButtonDisabled;
            microarrayHeatMapButton = this.microarrayHeatMapButtonDisabled;
            dendrogramButton = this.dendrogramButtonDisabled;
        } else if (data.networkClusters == null) {
            detailsButton = this.detailsButton;
            nodesButton = this.nodesButton;
            edgesButton = this.edgesButton;
            clustersButton = this.clustersButtonDisabled;
            searchButton = this.searchButton;
            filterButton = this.filterButtonDisabled;
            overrepresentationAnalysisButton = this.overrepresentationAnalysisButtonDisabled;
            heatMapButton = this.heatMapButton;
            if (data.microarrayData) {
                microarrayHeatMapButton = this.microarrayHeatMapButton;
            } else {
                microarrayHeatMapButton = this.microarrayHeatMapButtonDisabled;
            }
            dendrogramButton = this.dendrogramButtonDisabled;
        } else {
            detailsButton = this.detailsButton;
            nodesButton = this.nodesButton;
            edgesButton = this.edgesButton;
            clustersButton = this.clustersButton;
            searchButton = this.searchButton;
            filterButton = this.filterButton;
            overrepresentationAnalysisButton = this.overrepresentationAnalysisButton;
            heatMapButton = this.heatMapButton;
            if (data.microarrayData) {
                microarrayHeatMapButton = this.microarrayHeatMapButton;
            } else {
                microarrayHeatMapButton = this.microarrayHeatMapButtonDisabled;
            }
            if (data.hierarchicalClustering) {
                dendrogramButton = this.dendrogramButton;
            } else {
                dendrogramButton = this.dendrogramButtonDisabled;
            }
        }
        g.drawImage(detailsButton, detailsButtonCoordinates.x, detailsButtonCoordinates.y, null);
        g.drawImage(nodesButton, nodesButtonCoordinates.x, nodesButtonCoordinates.y, null);
        g.drawImage(edgesButton, edgesButtonCoordinates.x, edgesButtonCoordinates.y, null);
        g.drawImage(clustersButton, clustersButtonCoordinates.x, clustersButtonCoordinates.y, null);
        g.drawImage(buttonSeparator, 0, 175, null);
        g.drawImage(searchButton, searchButtonCoordinates.x, searchButtonCoordinates.y, null);
        g.drawImage(filterButton, filterButtonCoordinates.x, filterButtonCoordinates.y, null);
        g.drawImage(overrepresentationAnalysisButton, overrepresentationAnalysisButtonCoordinates.x, overrepresentationAnalysisButtonCoordinates.y, null);
        g.drawImage(heatMapButton, heatMapButtonCoordinates.x, heatMapButtonCoordinates.y, null);
        g.drawImage(microarrayHeatMapButton, microarrayHeatMapButtonCoordinates.x, microarrayHeatMapButtonCoordinates.y, null);
        g.drawImage(dendrogramButton, dendrogramButtonCoordinates.x, dendrogramButtonCoordinates.y, null);
        g.drawImage(buttonSeparator, 0, 446, null);
        g.drawImage(managePluginsButton, managePluginsButtonCoordinates.x, managePluginsButtonCoordinates.y, null);

        // the CustomBirdsEyeView is no longer used
//        // drawing the borders for the CustomBirdsEyeView
//        g.setColor(new Color(238, 238, 238));
//        g.fillRect(0,
//                getHeight() - 25 - 200,
//                200,
//                10);
//        g.fillRect(190,
//                getHeight() - 25 - 200,
//                10,
//                200);
//        g.fillRect(0,
//                getHeight() - 25 - 10,
//                200,
//                10);
//        g.fillRect(0,
//                getHeight() - 25 - 200,
//                10,
//                200);

        // this is now done in each Graph
//        // drawing the arrows for panning and the plus and minus for zooming
//        if (Data.classInstance.networkNodes != null) {
//            int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();
//            Graph currentCustomGraphEditor = JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex);
//            g.drawImage(panUpButton, (int) Math.round(JustclustJFrame.classInstance.justclustJPanel.getWidth() - 64.5), 3, null);
//            g.drawImage(panRightButton, JustclustJFrame.classInstance.justclustJPanel.getWidth() - 43, 46, null);
//            g.drawImage(panDownButton, (int) Math.round(JustclustJFrame.classInstance.justclustJPanel.getWidth() - 64.5), 89, null);
//            g.drawImage(panLeftButton, JustclustJFrame.classInstance.justclustJPanel.getWidth() - 86, 46, null);
//            g.drawImage(zoomInButton, (int) Math.round(JustclustJFrame.classInstance.justclustJPanel.getWidth() - 64.5), 132, null);
//            g.drawImage(zoomOutButton, (int) Math.round(JustclustJFrame.classInstance.justclustJPanel.getWidth() - 64.5), 175, null);
//        }

    }

    // this method makes a colour transparent in an image
    private Image TransformColorToTransparency(BufferedImage image, Color c1, Color c2) {

        // Primitive test, just an example
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = rgb & 0xFF;
                if (r == r1 && g == g1 && b == b1) {
                    // Set fully transparent but keep color
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);

    }

    // this method transforms an Image into a BufferedImage
    private BufferedImage ImageToBufferedImage(Image image, int width, int height) {
        BufferedImage dest = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public String getToolTipText(MouseEvent event) {

        int x = event.getX();
        int y = event.getY();
        // the coordinates are those for the buttons in the toolbar
        if (x >= detailsButtonCoordinates.x && x <= detailsButtonCoordinates.x + 39
                && y - 2 >= detailsButtonCoordinates.y && y - 2 <= detailsButtonCoordinates.y + 39) {
            return "Network Details";
        }
        if (x >= nodesButtonCoordinates.x && x <= nodesButtonCoordinates.x + 39
                && y - 2 >= nodesButtonCoordinates.y && y - 2 <= nodesButtonCoordinates.y + 39) {
            return "Network Nodes";
        }
        if (x >= edgesButtonCoordinates.x && x <= edgesButtonCoordinates.x + 39
                && y - 2 >= edgesButtonCoordinates.y && y - 2 <= edgesButtonCoordinates.y + 39) {
            return "Network Edges";
        }
        if (x >= clustersButtonCoordinates.x && x <= clustersButtonCoordinates.x + 39
                && y - 2 >= clustersButtonCoordinates.y && y - 2 <= clustersButtonCoordinates.y + 39) {
            return "Network Clusters";
        }
        if (x >= searchButtonCoordinates.x && x <= searchButtonCoordinates.x + 39
                && y - 2 >= searchButtonCoordinates.y && y - 2 <= searchButtonCoordinates.y + 39) {
            return "Search Network";
        }
        if (x >= filterButtonCoordinates.x && x <= filterButtonCoordinates.x + 39
                && y - 2 >= filterButtonCoordinates.y && y - 2 <= filterButtonCoordinates.y + 39) {
            return "Filter Clusters";
        }
        if (x >= overrepresentationAnalysisButtonCoordinates.x && x <= overrepresentationAnalysisButtonCoordinates.x + 39
                && y - 2 >= overrepresentationAnalysisButtonCoordinates.y && y - 2 <= overrepresentationAnalysisButtonCoordinates.y + 39) {
            return "Over-representation Analysis";
        }
        if (x >= heatMapButtonCoordinates.x && x <= heatMapButtonCoordinates.x + 39
                && y - 2 >= heatMapButtonCoordinates.y && y - 2 <= heatMapButtonCoordinates.y + 39) {
            return "Heat Map";
        }
        if (x >= microarrayHeatMapButtonCoordinates.x && x <= microarrayHeatMapButtonCoordinates.x + 39
                && y - 2 >= microarrayHeatMapButtonCoordinates.y && y - 2 <= microarrayHeatMapButtonCoordinates.y + 39) {
            return "Microarray Heat Map";
        }
        if (x >= dendrogramButtonCoordinates.x && x <= dendrogramButtonCoordinates.x + 39
                && y - 2 >= dendrogramButtonCoordinates.y && y - 2 <= dendrogramButtonCoordinates.y + 39) {
            return "Dendrogram";
        }
        if (x >= managePluginsButtonCoordinates.x && x <= managePluginsButtonCoordinates.x + 39
                && y - 2 >= managePluginsButtonCoordinates.y && y - 2 <= managePluginsButtonCoordinates.y + 39) {
            return "Manage Plug-ins";
        }

        return null;

    }
}
