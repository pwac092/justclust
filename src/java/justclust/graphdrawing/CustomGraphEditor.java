package justclust.graphdrawing;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.JustclustActionListener;
import justclust.datastructures.Cluster;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;

public class CustomGraphEditor extends PCanvas {

    // this variable contains the Nodes which are displayed by this
    // Graph
    public ArrayList<Node> networkNodes;
    // this variable contains the Edges which are displayed by this
    // Graph
    public ArrayList<Edge> networkEdges;
    // this variable contains the Clusters which are displayed by this
    // Graph
    public ArrayList<Cluster> networkClusters;
    // this PLayer contains the labels to be drawn on the Graph
    public PLayer labelLayer;
    // this PLayer contains the nodes to be drawn on the Graph
    public PLayer nodeLayer;
    // this PLayer contains the edges to be drawn on the Graph
    public PLayer edgeLayer;
    // these listeners and handlers deal with user interaction
    public CustomPPanEventHandler customPPanEventHandler;
    public CustomPSelectionEventHandler customPSelectionEventHandler;
    public CustomPZoomEventHandler customPZoomEventHandler;
    public CustomGraphEditorKeyListener customKeyListener;
    public PanningAndZoomingThread panningAndZoomingThread;
    public CustomGraphEditorMouseListener customGraphEditorMouseListener;
    // the images which are used for controls for panning, zooming, and resizing
    // nodes
    BufferedImage panUpButton;
    Point panUpButtonCoordinates;
    BufferedImage panRightButton;
    Point panRightButtonCoordinates;
    BufferedImage panDownButton;
    Point panDownButtonCoordinates;
    BufferedImage panLeftButton;
    Point panLeftButtonCoordinates;
    BufferedImage zoomInButton;
    Point zoomInButtonCoordinates;
    BufferedImage zoomOutButton;
    Point zoomOutButtonCoordinates;
    BufferedImage growNodeSizeButton;
    Point growNodeSizeButtonCoordinates;
    BufferedImage shrinkNodeSizeButton;
    BufferedImage shrinkNodeSizeButtonDisabled;
    Point shrinkNodeSizeButtonCoordinates;
    BufferedImage panningToolButton;
    BufferedImage panningToolButtonPressed;
    Point panningToolButtonCoordinates;
    BufferedImage selectionToolButton;
    BufferedImage selectionToolButtonPressed;
    Point selectionToolButtonCoordinates;
    // shouldRepaint is set to false when the Graph is in the
    // process of being updated and shouldn't be repainted
    public boolean shouldRepaint = true;
    // paintButtons is set to false when the Graph is being painted
    // to an image file and the buttons should not be included
    public boolean paintButtons = true;

    public CustomGraphEditor(ArrayList<Node> networkNodes, ArrayList<Edge> networkEdges, ArrayList<Cluster> networkClusters) {

        this.networkNodes = networkNodes;
        this.networkEdges = networkEdges;
        this.networkClusters = networkClusters;

        // Initialize, and create a layer for the labels, nodes, and edges
        // (always underneath the nodes)
        labelLayer = new PLayer();
        nodeLayer = new PLayer();
        edgeLayer = new PLayer();
        getRoot().addChild(labelLayer);
        getRoot().addChild(nodeLayer);
        getRoot().addChild(edgeLayer);
        getCamera().addLayer(0, labelLayer);
        getCamera().addLayer(0, nodeLayer);
        getCamera().addLayer(0, edgeLayer);

        // When nodes are moving or zooming is occurring the quality will remain
        // low so that these actions are smooth
        setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        setAnimatingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        setInteractingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);

        PInputEventFilter pInputEventFilter;

        removeInputEventListener(getPanEventHandler());
        // to select nodes, the user must hold the control button and click the
        // left-mouse button.
        // the left-mouse button is used for panning when the control button is
        // not held, so, when the control button is held, panning must not
        // happen.
        customPPanEventHandler = new CustomPPanEventHandler(this);
        addInputEventListener(customPPanEventHandler);
        pInputEventFilter = new PInputEventFilter();
//        pInputEventFilter.setNotMask(InputEvent.CTRL_MASK + InputEvent.BUTTON3_MASK);
        pInputEventFilter.setNotMask(InputEvent.BUTTON3_MASK);
        customPPanEventHandler.setEventFilter(pInputEventFilter);

        removeInputEventListener(getZoomEventHandler());
        // the CustomPZoomEventHandler zooms the main graphical view with
        // vertical mouse movement instead of horizontal, to make it more
        // intuitive
        customPZoomEventHandler = new CustomPZoomEventHandler(this);
        addInputEventListener(customPZoomEventHandler);

        // the customGraphEditorMouseWheelListener listens fot mouse wheel
        // movements and zooms the graphical representation of the current
        // network accordingly
        CustomGraphEditorMouseWheelListener customGraphEditorMouseWheelListener = new CustomGraphEditorMouseWheelListener(this);
        this.addMouseWheelListener(customGraphEditorMouseWheelListener);

        // customPSelectionEventHandler provides a way to select nodes.
        // the selection marquee box is painted on to the camera so that it
        // doesn't scale when zooming occurs.
        // this is specified by passing the camera into the constructor of
        // CustomPSelectionEventHandler.
        customPSelectionEventHandler = new CustomPSelectionEventHandler(getCamera(), nodeLayer, this);
        pInputEventFilter = new PInputEventFilter();
        // selecting nodes cannot be done by right-clicking because
        // right-clicking is for zooming.
        // this must be specified.
        // InputEvent.BUTTON3_MASK represents only right-clicking
        pInputEventFilter.setNotMask(InputEvent.BUTTON3_MASK);
//        // to select nodes, the user must hold the control button and click the
//        // left-mouse button
//        pInputEventFilter.setAndMask(InputEvent.BUTTON1_MASK + InputEvent.CTRL_MASK);
        customPSelectionEventHandler.setEventFilter(pInputEventFilter);
        addInputEventListener(customPSelectionEventHandler);

        // customGraphEditorMouseListener listens for the cursor moving over the
        // Graph and changes the cursor image to indicate that the
        // Graph can be dragged when the cursor is over it
        customGraphEditorMouseListener = new CustomGraphEditorMouseListener(this);
        addMouseListener(customGraphEditorMouseListener);
        // customGraphEditorMouseListener is a MouseMotionListener so that the
        // cursor's icon can change when it is over the panning and zooming
        // buttons
        addMouseMotionListener(customGraphEditorMouseListener);

        // customKeyListener listens for keyboard arrow keys being pressed and
        // pans the graphical views accordingly
        customKeyListener = new CustomGraphEditorKeyListener(this);
        this.addKeyListener(customKeyListener);

        panningAndZoomingThread = new PanningAndZoomingThread(this);

        // this InputEventListener has been replaced by
        // customPSelectionEventHandler because there should be a selection
        // handler for the main graphical view rather than a drag handler
////         Create event handler to move nodes and update edges
//        nodeLayer.addInputEventListener(new PDragEventHandler() {
//            {
//                PInputEventFilter filter = new PInputEventFilter();
//                filter.setOrMask(InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK);
//                setEventFilter(filter);
//            }
//
//            // Highlight the nodes when the cursor hovers over them
////            public void mouseEntered(PInputEvent e) {
////                super.mouseEntered(e);
////                if (e.getButton() == MouseEvent.NOBUTTON) {
////                    e.getPickedNode().setPaint(Color.RED);
////                }
////            }
////            public void mouseExited(PInputEvent e) {
////                super.mouseExited(e);
////                if (e.getButton() == MouseEvent.NOBUTTON) {
////                    e.getPickedNode().setPaint(Color.WHITE);
////                }
////            }
//            protected void startDrag(PInputEvent e) {
//                super.startDrag(e);
//                e.setHandled(true);
//                e.getPickedNode().moveToFront();
//            }
//
//            protected void drag(PInputEvent e) {
//                super.drag(e);
//
//                ArrayList edges = (ArrayList) e.getPickedNode().getAttribute("edges");
//                for (int i = 0; i < edges.size(); i++) {
//                    GraphEditor.this.updateEdge((PPath) edges.get(i));
//                }
//            }
//        });

        // create the images for the buttons for panning, zooming, and resizing
        // nodes
        try {
            panUpButton = ImageIO.read(new File("img/pan_up_button.png"));
            panRightButton = ImageIO.read(new File("img/pan_right_button.png"));
            panDownButton = ImageIO.read(new File("img/pan_down_button.png"));
            panLeftButton = ImageIO.read(new File("img/pan_left_button.png"));
            zoomInButton = ImageIO.read(new File("img/zoom_in_button.png"));
            zoomOutButton = ImageIO.read(new File("img/zoom_out_button.png"));
            growNodeSizeButton = ImageIO.read(new File("img/grow_node_size_button.png"));
            shrinkNodeSizeButton = ImageIO.read(new File("img/shrink_node_size_button.png"));
            shrinkNodeSizeButtonDisabled = ImageIO.read(new File("img/shrink_node_size_button_disabled.png"));
            panningToolButton = ImageIO.read(new File("img/panning_tool_button.png"));
            panningToolButtonPressed = ImageIO.read(new File("img/panning_tool_button_pressed.png"));
            selectionToolButton = ImageIO.read(new File("img/selection_tool_button.png"));
            selectionToolButtonPressed = ImageIO.read(new File("img/selection_tool_button_pressed.png"));
        } catch (IOException ex) {
        }
        Image transparentPanUpButton = TransformColorToTransparency(panUpButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentPanRightButton = TransformColorToTransparency(panRightButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentPanDownButton = TransformColorToTransparency(panDownButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentPanLeftButton = TransformColorToTransparency(panLeftButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentZoomInButton = TransformColorToTransparency(zoomInButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentZoomOutButton = TransformColorToTransparency(zoomOutButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentGrowNodeSizeButton = TransformColorToTransparency(growNodeSizeButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentShrinkNodeSizeButton = TransformColorToTransparency(shrinkNodeSizeButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentShrinkNodeSizeButtonDisabled = TransformColorToTransparency(shrinkNodeSizeButtonDisabled, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentPanningToolButton = TransformColorToTransparency(panningToolButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentPanningToolButtonPressed = TransformColorToTransparency(panningToolButtonPressed, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentSelectionToolButton = TransformColorToTransparency(selectionToolButton, new Color(255, 0, 255), new Color(255, 0, 255));
        Image transparentSelectionToolButtonPressed = TransformColorToTransparency(selectionToolButtonPressed, new Color(255, 0, 255), new Color(255, 0, 255));
        panUpButton = ImageToBufferedImage(transparentPanUpButton, panUpButton.getWidth(), panUpButton.getHeight());
        panRightButton = ImageToBufferedImage(transparentPanRightButton, panRightButton.getWidth(), panRightButton.getHeight());
        panDownButton = ImageToBufferedImage(transparentPanDownButton, panDownButton.getWidth(), panDownButton.getHeight());
        panLeftButton = ImageToBufferedImage(transparentPanLeftButton, panLeftButton.getWidth(), panLeftButton.getHeight());
        zoomInButton = ImageToBufferedImage(transparentZoomInButton, zoomInButton.getWidth(), zoomInButton.getHeight());
        zoomOutButton = ImageToBufferedImage(transparentZoomOutButton, zoomOutButton.getWidth(), zoomOutButton.getHeight());
        growNodeSizeButton = ImageToBufferedImage(transparentGrowNodeSizeButton, growNodeSizeButton.getWidth(), growNodeSizeButton.getHeight());
        shrinkNodeSizeButton = ImageToBufferedImage(transparentShrinkNodeSizeButton, shrinkNodeSizeButton.getWidth(), shrinkNodeSizeButton.getHeight());
        shrinkNodeSizeButtonDisabled = ImageToBufferedImage(transparentShrinkNodeSizeButtonDisabled, shrinkNodeSizeButtonDisabled.getWidth(), shrinkNodeSizeButtonDisabled.getHeight());
        panningToolButton = ImageToBufferedImage(transparentPanningToolButton, panningToolButton.getWidth(), panningToolButton.getHeight());
        panningToolButtonPressed = ImageToBufferedImage(transparentPanningToolButtonPressed, panningToolButtonPressed.getWidth(), panningToolButtonPressed.getHeight());
        selectionToolButton = ImageToBufferedImage(transparentSelectionToolButton, selectionToolButton.getWidth(), selectionToolButton.getHeight());
        selectionToolButtonPressed = ImageToBufferedImage(transparentSelectionToolButtonPressed, selectionToolButtonPressed.getWidth(), selectionToolButtonPressed.getHeight());

        // set the coordinates of the buttons
        panUpButtonCoordinates = new Point(-43, 89);
        panRightButtonCoordinates = new Point(-43, 175);
        panDownButtonCoordinates = new Point(-43, 132);
        panLeftButtonCoordinates = new Point(-86, 175);
        zoomInButtonCoordinates = new Point(-86, 89);
        zoomOutButtonCoordinates = new Point(-86, 132);
        growNodeSizeButtonCoordinates = new Point(-86, 3);
        shrinkNodeSizeButtonCoordinates = new Point(-43, 3);
        panningToolButtonCoordinates = new Point(-86, 46);
        selectionToolButtonCoordinates = new Point(-43, 46);

    }

    // this method extends the super class' paint method with additional
    // graphics to display in the Graph
    public void paint(Graphics g) {

        if (!shouldRepaint) {
            return;
        }

        // the paint method of the super class is called so that the parts of
        // the Graph which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        if (paintButtons) {

//            // draw the frames for the buttons
//            g.setColor(new Color(238, 238, 238));
//            g.fillRect((int) Math.round(getWidth() - 67.5),
//                    0,
//                    46,
//                    46);
//            g.fillRect(getWidth() - 89,
//                    43,
//                    92,
//                    46);
//            g.fillRect((int) Math.round(getWidth() - 67.5),
//                    86,
//                    46,
//                    132);
//            g.fillRect(getWidth() - 89,
//                    215,
//                    92,
//                    89);

            // drawing the controls
            g.drawImage(panUpButton, getWidth() + panUpButtonCoordinates.x, panUpButtonCoordinates.y, null);
            g.drawImage(panRightButton, getWidth() + panRightButtonCoordinates.x, panRightButtonCoordinates.y, null);
            g.drawImage(panDownButton, getWidth() + panDownButtonCoordinates.x, panDownButtonCoordinates.y, null);
            g.drawImage(panLeftButton, getWidth() + panLeftButtonCoordinates.x, panLeftButtonCoordinates.y, null);
            g.drawImage(zoomInButton, getWidth() + zoomInButtonCoordinates.x, zoomInButtonCoordinates.y, null);
            g.drawImage(zoomOutButton, getWidth() + zoomOutButtonCoordinates.x, zoomOutButtonCoordinates.y, null);
            g.drawImage(growNodeSizeButton, getWidth() + growNodeSizeButtonCoordinates.x, growNodeSizeButtonCoordinates.y, null);
            if (nodeLayer.getAllNodes().size() == 1) {
                g.drawImage(shrinkNodeSizeButtonDisabled, getWidth() + shrinkNodeSizeButtonCoordinates.x, shrinkNodeSizeButtonCoordinates.y, null);
            } else {
                PPath node = ((ArrayList<PPath>) nodeLayer.getAllNodes()).get(1);
                if (node.getWidth() < 40) {
                    g.drawImage(shrinkNodeSizeButtonDisabled, getWidth() + shrinkNodeSizeButtonCoordinates.x, shrinkNodeSizeButtonCoordinates.y, null);
                } else {
                    g.drawImage(shrinkNodeSizeButton, getWidth() + shrinkNodeSizeButtonCoordinates.x, shrinkNodeSizeButtonCoordinates.y, null);
                }
            }
            if (customGraphEditorMouseListener.panningToolMode && !customGraphEditorMouseListener.controlKeyPressed
                    || !customGraphEditorMouseListener.panningToolMode && customGraphEditorMouseListener.controlKeyPressed) {
                g.drawImage(panningToolButtonPressed, getWidth() + panningToolButtonCoordinates.x, panningToolButtonCoordinates.y, null);
                g.drawImage(selectionToolButton, getWidth() + selectionToolButtonCoordinates.x, selectionToolButtonCoordinates.y, null);
            } else {
                g.drawImage(panningToolButton, getWidth() + panningToolButtonCoordinates.x, panningToolButtonCoordinates.y, null);
                g.drawImage(selectionToolButtonPressed, getWidth() + selectionToolButtonCoordinates.x, selectionToolButtonCoordinates.y, null);
            }

        }

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

    // this method provides the tooltips for the panning and zooming buttons on
    // this view
    public String getToolTipText(MouseEvent event) {

        int x = event.getX();
        int y = event.getY();
        // the coordinates are those for the buttons for panning, zooming, and
        // resizing nodes in the main graphical view.
        // subtracting 2 from y is done because the coordinates are off by
        // 2 for some unkown reason.
        if (x >= getWidth() + panUpButtonCoordinates.x
                && x <= getWidth() + panUpButtonCoordinates.x + 39
                && y - 2 >= panUpButtonCoordinates.y
                && y - 2 <= panUpButtonCoordinates.y + 39) {
            return "Pan Up";
        }
        if (x >= getWidth() + panRightButtonCoordinates.x
                && x <= getWidth() + panRightButtonCoordinates.x + 39
                && y - 2 >= panRightButtonCoordinates.y
                && y - 2 <= panRightButtonCoordinates.y + 39) {
            return "Pan Right";
        }
        if (x >= getWidth() + panDownButtonCoordinates.x
                && x <= getWidth() + panDownButtonCoordinates.x + 39
                && y - 2 >= panDownButtonCoordinates.y
                && y - 2 <= panDownButtonCoordinates.y + 39) {
            return "Pan Down";
        }
        if (x >= getWidth() + panLeftButtonCoordinates.x
                && x <= getWidth() + panLeftButtonCoordinates.x + 39
                && y - 2 >= panLeftButtonCoordinates.y
                && y - 2 <= panLeftButtonCoordinates.y + 39) {
            return "Pan Left";
        }
        if (x >= getWidth() + zoomInButtonCoordinates.x
                && x <= getWidth() + zoomInButtonCoordinates.x + 39
                && y - 2 >= zoomInButtonCoordinates.y
                && y - 2 <= zoomInButtonCoordinates.y + 39) {
            return "Zoom In";
        }
        if (x >= getWidth() + zoomOutButtonCoordinates.x
                && x <= getWidth() + zoomOutButtonCoordinates.x + 39
                && y - 2 >= zoomOutButtonCoordinates.y
                && y - 2 <= zoomOutButtonCoordinates.y + 39) {
            return "Zoom Out";
        }
        if (x >= getWidth() + growNodeSizeButtonCoordinates.x
                && x <= getWidth() + growNodeSizeButtonCoordinates.x + 39
                && y - 2 >= growNodeSizeButtonCoordinates.y
                && y - 2 <= growNodeSizeButtonCoordinates.y + 39) {
            return "Grow Node Size";
        }
        if (x >= getWidth() + shrinkNodeSizeButtonCoordinates.x
                && x <= getWidth() + shrinkNodeSizeButtonCoordinates.x + 39
                && y - 2 >= shrinkNodeSizeButtonCoordinates.y
                && y - 2 <= shrinkNodeSizeButtonCoordinates.y + 39) {
            return "Shrink Node Size";
        }
        if (x >= getWidth() + panningToolButtonCoordinates.x
                && x <= getWidth() + panningToolButtonCoordinates.x + 39
                && y - 2 >= panningToolButtonCoordinates.y
                && y - 2 <= panningToolButtonCoordinates.y + 39) {
            return "Panning Tool";
        }
        if (x >= getWidth() + selectionToolButtonCoordinates.x
                && x <= getWidth() + selectionToolButtonCoordinates.x + 39
                && y - 2 >= selectionToolButtonCoordinates.y
                && y - 2 <= selectionToolButtonCoordinates.y + 39) {
            return "Selection Tool";
        }

        return null;

    }

    // this method is called when a network is loaded from a file.
    // nodes and edges which represent the network are displayed in a grid
    // layout in the main graphical view.
    public void createGraph() {

        // all the labels, nodes, and edges are removed so that new ones can be placed
        labelLayer.removeAllChildren();
        nodeLayer.removeAllChildren();
        edgeLayer.removeAllChildren();

        // the cameras position and scale (zoom) is reset so that when the nodes
        // are positioned in a grid later in this method, the grid is centered
        // and fits the view nicely
        getCamera().setViewOffset(0, 0);
        getCamera().setViewScale(1);

        // this code laid out the graphical representation of the new network in
        // a grid, but it has been replaced by the call to
        // JustclustJMenuActionListener.applyGridLayout() below
//        int columnAmount = (int) Math.round(Math.sqrt(networkNodes.size()));
//        int rowAmount = (int) Math.ceil((double) networkNodes.size() / columnAmount);

        // the nodes and lables are added to the view
        for (int i = 0; i < networkNodes.size(); i++) {

            PPath node = PPath.createEllipse(0, 0, 20, 20);
            // add attributes to node so that, given the node, it is
            // easy to find its edges and labels.
            // this useful when user interaction is processed and edges need
            // to be moved with nodes, along with other examples.
            node.addAttribute("labels", new ArrayList<PText>());
            node.addAttribute("edges", new ArrayList<PPath>());

            nodeLayer.addChild(node);

            // provide a way to access the corresponding network node from the
            // graphical node and vice versa
            node.addAttribute("networkNode", networkNodes.get(i));
            networkNodes.get(i).graphicalNode = node;

            PText label = new PText();
            label.setText(networkNodes.get(i).label);
            label.setPickable(false);
            label.setFont(new Font("SansSerif", Font.PLAIN, 8));
            // add attributes to the label and its node so that, given a label,
            // it is easy to find its nodes, and, given a node, it is easy to
            // find its labels.
            // this useful when user interaction is processed and labels need
            // to be moved with nodes, along with other examples.
            label.addAttribute("nodes", new ArrayList<PPath>());
            ((ArrayList<PPath>) label.getAttribute("nodes")).add(node);
            ((ArrayList<PText>) node.getAttribute("labels")).add(label);

            labelLayer.addChild(label);

            // provide a way to access the corresponding network node from the
            // label and vice versa
            label.addAttribute("networkNode", networkNodes.get(i));
            networkNodes.get(i).graphicalLabel = label;

        }

        // the edges are added to the view
        for (int i = 0; i < networkEdges.size(); i++) {

            // this code creates an edge which is an object affected by zoom.
            // this is not used because the edges become invisible when the user
            // zooms out and this looks bad.
            // also, they take longer to render and slow down the program.
//            PPath edge = PPath.createRectangle(0, 0, 1, 1);
//            edge.animateToColor(Color.BLACK, 0);
//            edge.setStroke(null);

            PPath edge = new PPath();

            // add attributes to the edge and the nodes it connects so that,
            // given a node, it is easy to find its edges, and, given an edge,
            // it is easy to find its nodes.
            // this useful when user interaction is processed and edges need
            // to be moved with nodes, along with other examples.
            PPath node1 = networkEdges.get(i).node1.graphicalNode;
            PPath node2 = networkEdges.get(i).node2.graphicalNode;
            edge.addAttribute("node1", node1);
            edge.addAttribute("node2", node2);
            ((ArrayList<PPath>) node1.getAttribute("edges")).add(edge);
            ((ArrayList<PPath>) node2.getAttribute("edges")).add(edge);

            // allow selection box to appear when cursor is over an edge
//            edge.setPickable(false);

            edgeLayer.addChild(edge);

            // provide a way to access the corresponding network edge from the
            // graphical edge and vice versa
            edge.addAttribute("networkEdge", networkEdges.get(i));
            networkEdges.get(i).graphicalEdge = edge;

            PText label = new PText();
            label.setPickable(false);
            label.setFont(new Font("SansSerif", Font.PLAIN, 8));
            // add attributes to the label and the nodes which are connected by
            // the current edge so that, given the label, it is easy to find the
            // nodes, and, given a node, it is easy to find the label.
            // this useful when user interaction is processed and labels need
            // to be moved with nodes, along with other examples.
            label.addAttribute("nodes", new ArrayList<PPath>());
            ((ArrayList<PPath>) label.getAttribute("nodes")).add(node1);
            ((ArrayList<PPath>) label.getAttribute("nodes")).add(node2);
            ((ArrayList<PText>) node1.getAttribute("labels")).add(label);
            ((ArrayList<PText>) node2.getAttribute("labels")).add(label);

            labelLayer.addChild(label);

            // provide a way to access the corresponding network edge from the
            // label and vice versa
            label.addAttribute("networkEdge", networkEdges.get(i));
            networkEdges.get(i).graphicalLabel = label;

        }

        // the CustomBirdsEyeView is no longer used
//        // the customBirdsEyeView is given the customGraphEditor so that it can
//        // pan for the customGraphEditor.
//        // the edgeLayer is given before the nodeLayer, which is given before
//        // the labelLayer, so that the labelLayer is displayed on top, then the
//        // nodeLayer, then the edgeLayer.
//        // the customBirdsEyeView is not connected to the Graph when
//        // they are both constructed because this causes an error for some
//        // unkown reason.
//        JustclustJFrame.classInstance.customBirdsEyeView.connect(this, new PLayer[]{edgeLayer, nodeLayer, labelLayer});
//        // the view for panning is updated so that the window will contain any
//        // nodes which are moved outside of its boundaries
//        JustclustJFrame.classInstance.customBirdsEyeView.updateFromViewed();

        // panningAndZoomingThread acts as a Thread which responds for arrow keyboard
        // key presses (among other things) in a seperate thread (which is
        // started here) by panning the main graphical view.
        // panningAndZoomingThread is started here because it causes an error when it
        // is started after the Graph and CustomBirdsEyeView are
        // created for some unknown reason.
        // this method (updateGraph) is called often and the panningAndZoomingThread
        // thread should only be started once, so whether it has already been
        // started is checked before.
        if (!panningAndZoomingThread.isAlive()) {
            panningAndZoomingThread.start();
        }

    }

    public void applyCytoscapeGridLayout() {

        double nodeHorizontalSpacing = 50;
        double nodeVerticalSpacing = 40;

        double currX = 0.0d;
        double currY = 0.0d;
        double initialX = 0.0d;
        double initialY = 0.0d;

        // Yes, our size and starting points need to be different

        // the nodeCount is set to 0 so that, if there are no nodes, the rest
        // of this method will not try to access nodes which don't exist,
        // causing an error.
        // networkNodes != null is checked so that
        // networkNodes.size() is not called when
        // networkNodes == null which will cause an
        // error.
        int nodeCount = 0;
        if (networkNodes != null) {
            nodeCount = networkNodes.size();
        }
        final int columns = (int) Math.sqrt(nodeCount);

        // Calculate our starting point as the geographical center of the
        // selected nodes.
        for (int i = 1; i < nodeLayer.getAllNodes().size(); i++) {
            PPath node = (PPath) ((ArrayList<PNode>) nodeLayer.getAllNodes()).get(i);
            initialX += (node.getX() / nodeCount);
            initialY += (node.getY() / nodeCount);
        }

        // initialX and initialY reflect the center of our grid, so we
        // need to offset by distance*columns/2 in each direction
        initialX = initialX - ((nodeHorizontalSpacing * (columns - 1)) / 2);
        initialY = initialY - ((nodeVerticalSpacing * (columns - 1)) / 2);
        currX = initialX;
        currY = initialY;

        int count = 0;

        // the nodes are laid-out first because the positions of the labels and
        // edges depend on the positions of the nodes.
        // Set visual property.
        // TODO: We need batch apply method for Visual Property values for
        // performance.
        for (int i = 1; i < nodeLayer.getAllNodes().size(); i++) {

            PPath node = (PPath) ((ArrayList<PNode>) nodeLayer.getAllNodes()).get(i);
            node.setOffset(0, 0);
            node.setX(currX);
            node.setY(currY);

            Node networkNode = (Node) node.getAttribute("networkNode");
            PText label = networkNode.graphicalLabel;
            label.setOffset(0, 0);
            label.setX(currX + 10 - label.getWidth() / 2);
            label.setY(currY + 10 - label.getHeight() / 2);

            count++;

            if (count == columns) {
                count = 0;
                currX = initialX;
                currY += nodeVerticalSpacing;
            } else {
                currX += nodeHorizontalSpacing;
            }

        }

//        ArrayList<PNode> labels = (ArrayList<PNode>) labelLayer.getAllNodes();
//        for (int i = 1; i < labelLayer.getAllNodes().size(); i++) {
//
//            PText label = (PText) labels.get(i);
//            PPath node = (PPath) ((ArrayList<PPath>) label.getAttribute("nodes")).get(0);
////            Point2D point2D = node.getFullBoundsReference().getCenter2D();
////            label.setOffset(0, 0);
////            label.setX(point2D.getX() - label.getWidth() / 2);
////            label.setY(point2D.getY() - label.getHeight() / 2);
//            label.setOffset(0, 0);
//            label.setX(node.getX() + 10 - label.getWidth() / 2);
//            label.setY(node.getY() + 10 - label.getHeight() / 2);
//
//        }

        // position the edges in the new layout
        ArrayList<PNode> edges = (ArrayList<PNode>) edgeLayer.getAllNodes();
        for (int i = 1; i < edgeLayer.getAllNodes().size(); i++) {
            
            // this code repositioned and rotated the current edge if it was
            // a rectangle affected by zoom.
            // this is not used because the edges become invisible when the user
            // zooms out and this looks bad.
            // also, they take longer to render and slow down the program.
//            PNode edge = (PNode) ((ArrayList<PNode>) Graph.classInstance.edgeLayer.getAllNodes()).get(i);
//            
//            Edge networkEdge = Data.classInstance.networkEdges.get(i - 1);
//            int n1 = Data.classInstance.networkNodes.indexOf(networkEdge.node1) * 2 + 1;
//            int n2 = Data.classInstance.networkNodes.indexOf(networkEdge.node2) * 2 + 1;
//            PNode node1 = ((ArrayList<PNode>) Graph.classInstance.nodeLayer.getAllNodes()).get(n1);
//            PNode node2 = ((ArrayList<PNode>) Graph.classInstance.nodeLayer.getAllNodes()).get(n2);
//            Point2D point1 = node1.getFullBoundsReference().getCenter2D();
//            Point2D point2 = node2.getFullBoundsReference().getCenter2D();
//            
//            edge.setWidth(point1.distance(point2));
//            edge.setHeight(1);
//            
//            if (point1.getY() > point2.getY()) {
//                Point2D temporaryPoint = point1;
//                point1 = point2;
//                point2 = temporaryPoint;
//            }
//            
//            edge.setX(point1.getX());
//            edge.setY(point1.getY());
//            
//            double point3X = point1.getX() + 1;
//            double point3Y = point1.getY();
//            double lengthOppositePoint1 = Math.sqrt(Math.pow(point2.getX() - point3X, 2) + Math.pow(point2.getY() - point3Y, 2));
//            double lengthOppositePoint2 = Math.sqrt(Math.pow(point1.getX() - point3X, 2) + Math.pow(point1.getY() - point3Y, 2));
//            double lengthOppositePoint3 = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
//            double angle = Math.acos((Math.pow(lengthOppositePoint2, 2) + Math.pow(lengthOppositePoint3, 2) - Math.pow(lengthOppositePoint1, 2)) / (2 * lengthOppositePoint2 * lengthOppositePoint3));
//            edge.rotateAboutPoint(angle, point1);

            PPath edge = (PPath) edges.get(i);
//            PNode node1 = (PNode) edge.getAttribute("node1");
//            PNode node2 = (PNode) edge.getAttribute("node2");
//            Point2D start = node1.getFullBoundsReference().getCenter2D();
//            Point2D end = node2.getFullBoundsReference().getCenter2D();
//            edge.reset();
//            edge.moveTo((float) start.getX(), (float) start.getY());
//            edge.lineTo((float) end.getX(), (float) end.getY());
            edge.reset();
            PNode node1 = networkEdges.get(i - 1).node1.graphicalNode;
            PNode node2 = networkEdges.get(i - 1).node2.graphicalNode;
            edge.moveTo((float) node1.getX() + 10, (float) node1.getY() + 10);
            edge.lineTo((float) node2.getX() + 10, (float) node2.getY() + 10);

        }

        // the entire graph is fitted into the main graphical view
        final Rectangle2D drag_bounds = getCamera().getUnionOfLayerFullBounds();
        getCamera().animateViewToCenterBounds(drag_bounds, true, 0);

        // due to zooming the graph cannot be more than 2 times smaller or more
        // than 50 times bigger than its initial size
        customPZoomEventHandler.setMinScale(getCamera().getViewScale() / 2);
        customPZoomEventHandler.setMaxScale(getCamera().getViewScale() * 50);

    }

    // this method shows the current clustering by making all the Nodes and
    // Edges visible except for Edges which connect Nodes which do not belong to
    // the same Cluster
    public void showClusteringWithNodeAndEdgeVisibility() {

        // all Nodes should be visible
        for (Node node : networkNodes) {
            node.visible = true;
            for (Node otherVersion : node.otherVersions) {
                otherVersion.visible = true;
            }
        }

        // the Edges which connect Nodes which are in the same Cluster are made
        // visible, and the other Edges are made invisible
        for (Edge edge : networkEdges) {
            if (edge.node1.cluster == edge.node2.cluster) {
                edge.visible = true;
                for (Edge otherVersion : edge.otherVersions) {
                    if (otherVersion.data != edge.data) {
                        otherVersion.visible = true;
                    }
                }
            } else {
                edge.visible = false;
                for (Edge otherVersion : edge.otherVersions) {
                    if (otherVersion.data != edge.data) {
                        otherVersion.visible = false;
                    }
                }
            }
        }

        // update the appearance of all customGraphEditors.
        // all customGraphEditors are included incase a change in a graph, other than the
        // current graph, has been made.
        for (CustomGraphEditor customGraphEditor : JustclustJFrame.classInstance.customGraphEditors) {
            customGraphEditor.updateGraphVisibility();
        }

    }

    // this method creates a label for each Cluster in the current network
    public void createLabelsForClusters() {

        for (Cluster cluster : networkClusters) {

            PText label = new PText();
            label.setPickable(false);
            label.setFont(new Font("SansSerif", Font.PLAIN, 24));
            // add attributes to the label and the Nodes which belong to the
            // current Cluster so that, given the label, it is easy to find the
            // Nodes, and, given a Node, it is easy to find the label.
            // this useful when user interaction is processed and labels need
            // to be moved with Nodes, along with other examples.
            label.addAttribute("nodes", new ArrayList<PPath>());
            for (Node node : cluster.nodes) {
                ((ArrayList<PPath>) label.getAttribute("nodes")).add(node.graphicalNode);
                ((ArrayList<PText>) node.graphicalNode.getAttribute("labels")).add(label);
            }

            labelLayer.addChild(label);

            // provide a way to access the corresponding network Cluster from
            // the label and vice versa
            label.addAttribute("networkCluster", cluster);
            cluster.graphicalLabel = label;

        }

    }

    // this method updates the labels of the graphical nodes, edges, and
    // clusters based on the label field of each Node, Edge, and Cluster in the
    // current network
    public void updateGraphLabels() {

        // update Node labels
        for (Node node : networkNodes) {
            PText label = (PText) node.graphicalLabel;
            label.setText(node.label);
            // set the coordinates of the Node's label
            PPath graphicaNode = (PPath) node.graphicalNode;
            label.setX(graphicaNode.getFullBoundsReference().getCenter2D().getX() - label.getWidth() / 2);
            label.setY(graphicaNode.getFullBoundsReference().getCenter2D().getY() - label.getHeight() / 2);
        }

        // update Edge labels
        for (Edge edge : networkEdges) {
            PText label = (PText) edge.graphicalLabel;
            label.setText(edge.label);
            // set the coordinates of the Edge's label.
            // these are the average coordinates of Nodes which the Edge
            // connects.
            double x = 0;
            double y = 0;
            ArrayList<PPath> labelNodes = (ArrayList<PPath>) label.getAttribute("nodes");
            for (int j = 0; j < labelNodes.size(); j++) {
                x += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getX() / labelNodes.size();
                y += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getY() / labelNodes.size();
            }
            label.setX(x - label.getWidth() / 2);
            label.setY(y - label.getHeight() / 2);
        }

        // update Cluster labels.
        // this code should only be executed if networkClusters is not null
        // which means that the current network has been clustered.
        if (networkClusters != null) {
            for (Cluster cluster : networkClusters) {
                PText label = (PText) cluster.graphicalLabel;
                label.setText(cluster.label);
                // set the coordinates of the Cluster's label.
                // these are the average coordinates of Nodes contained in the
                // Cluster.
                double x = 0;
                double y = 0;
                ArrayList<PPath> labelNodes = (ArrayList<PPath>) label.getAttribute("nodes");
                for (int j = 0; j < labelNodes.size(); j++) {
                    x += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getX() / labelNodes.size();
                    y += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getY() / labelNodes.size();
                }
                label.setX(x - label.getWidth() / 2);
                label.setY(y - label.getHeight() / 2);
            }
        }

    }

    // this method shows and hides the graphical nodes and edges based on the
    // visible field of each Node and Edge in the current network
    public void updateGraphVisibility() {

        // update the visibilty of each graphical node in accordance with the
        // visible field of its corresponding network Node
        for (Node node : networkNodes) {
            PPath graphicalNode = (PPath) node.graphicalNode;
            graphicalNode.setVisible(node.visible);
            PText label = (PText) node.graphicalLabel;
            label.setVisible(node.visible);
        }

        // update the visibilty of each graphical edge in accordance with the
        // visible field of its corresponding network Edge
        for (Edge edge : networkEdges) {
            PPath graphicalEdge = (PPath) edge.graphicalEdge;
            PText label = (PText) edge.graphicalLabel;
            // if either of the Nodes which the Edge connects are invisible, the
            // Edge is made invisible so that it is not floating with no Node
            if (!edge.node1.visible || !edge.node2.visible) {
                graphicalEdge.setVisible(false);
                label.setVisible(false);
            } else {
                graphicalEdge.setVisible(edge.visible);
                label.setVisible(edge.visible);
            }
        }

    }

    // this method updates the colour of the graphical nodes and edges based on
    // the colour field of each Node and Edge in the current network
    public void updateGraphColour() {

        // update the colour of each graphical node in accordance with the
        // colour field of its corresponding network Node
        for (Node node : networkNodes) {
            PPath graphicalNode = (PPath) node.graphicalNode;
            if (customPSelectionEventHandler.isSelected(graphicalNode)) {

                // the node is selected, therefore, its colour should be changed
                // slightly to indicate this

                Color networkNodeColour = node.colour;
                int red = networkNodeColour.getRed();
                int green = networkNodeColour.getGreen();
                int blue = networkNodeColour.getBlue();
                if (red + green + blue <= 465) {
                    red = Math.min(red + 100, 255);
                    green = Math.min(green + 100, 255);
                    blue = Math.min(blue + 100, 255);
                } else {
                    red = Math.max(red - 100, 0);
                    green = Math.max(green - 100, 0);
                    blue = Math.max(blue - 100, 0);
                }
                graphicalNode.animateToColor(new Color(red, green, blue), 1000);

            } else {
                graphicalNode.animateToColor(node.colour, 1000);
            }
        }

        // update the colour of each graphical edge in accordance with the
        // colour field of its corresponding network Edge
        for (Edge edge : networkEdges) {
            PPath graphicalEdge = (PPath) edge.graphicalEdge;
            graphicalEdge.setStrokePaint(edge.colour);
        }

    }

    public void applyPrefuseForceLayout() {

        // create an array.
        // one element for each node.
        // the element contains whether the node's connected component has been
        // visited yet by the below algorithm.
        // the purpose of the array is to prevent the algorithm from finding the
        // connected component of each node even when it has already been found.
        boolean[] componentFound = new boolean[networkNodes.size()];
        // this ArrayList will contain all the connected components
        ArrayList<ArrayList<Node>> connectedComponents = new ArrayList<ArrayList<Node>>();
        // iterate through each node
        for (int i = 0; i < networkNodes.size(); i++) {
            // if the node's component has not been found
            if (!componentFound[i]) {

                // get the node's connected component
                ArrayList<Node> component = new ArrayList<Node>();
                component = getConnectedComponent(component, networkNodes.get(i));

                // the componentFound array is updated for all nodes in the
                // component
                for (int j = 0; j < component.size(); j++) {
                    componentFound[networkNodes.indexOf(component.get(j))] = true;
                }

                connectedComponents.add(component);

            }
        }

        // iterate through all the connected components, and lay them out with
        // the prefuse force visualisation layout
        for (ArrayList<Node> component : connectedComponents) {

            ForceSimulator m_fsim = new ForceSimulator();
            m_fsim.addForce(new NBodyForce());
            m_fsim.addForce(new SpringForce());
            m_fsim.addForce(new DragForce());

            Map<Node, ForceItem> forceItems = new HashMap<Node, ForceItem>();

            // initialize nodes
            for (int i = 0; i < networkNodes.size(); i++) {

                if (!component.contains(networkNodes.get(i))) {
                    continue;
                }

                ForceItem fitem = new ForceItem();
                forceItems.put(networkNodes.get(i), fitem);
                fitem.mass = (float) 3.0;
                fitem.location[0] = 0f;
                fitem.location[1] = 0f;
                m_fsim.addItem(fitem);

            }

            // initialize edges
            for (int i = 0; i < networkEdges.size(); i++) {

                if (!component.contains(networkEdges.get(i).node1) || !component.contains(networkEdges.get(i).node2)) {
                    continue;
                }

                Edge edge = networkEdges.get(i);
                Node n1 = edge.node1;
                ForceItem f1 = forceItems.get(n1);
                Node n2 = edge.node2;
                ForceItem f2 = forceItems.get(n2);
                // System.out.println("Adding edge "+e+" with spring coeffficient = "+getSpringCoefficient(e)+" and length "+getSpringLength(e));
                double defaultSpringCoefficient = 1e-4;
                double defaultSpringLength = 50.0;
                m_fsim.addSpring(f1, f2, (float) defaultSpringCoefficient, (float) defaultSpringLength);

            }

            // perform layout
            int numIterations = 100;
            long timestep = 1000L;
            for (int i = 0; i < numIterations; i++) {
                timestep *= (1.0 - i / (double) numIterations);
                long step = timestep + 50;
                m_fsim.runSimulator(step);
            }

            // position the nodes in the new layout
            for (int i = 0; i < networkNodes.size(); i++) {

                if (!component.contains(networkNodes.get(i))) {
                    continue;
                }

                // find x and y coordinates which position the nodes in the computed layout
                ForceItem fitem = forceItems.get(networkNodes.get(i));
                float x = fitem.location[0];
                float y = fitem.location[1];

                Node node = networkNodes.get(i);
                node.setGraphicalNodeXCoordinate(x);
                node.setGraphicalNodeYCoordinate(y);

            }

        }

        // the connected components are spaced-out in rows in order of width with
        // the widest component having its own row on top, and all subsequent rows
        // being roughly the width of the top row
        layoutConnectedComponents();

        // position the labels in the new layout
        for (int i = 1; i < labelLayer.getAllNodes().size(); i++) {
            PText label = (PText) ((ArrayList<PText>) labelLayer.getAllNodes()).get(i);
            double x = 0;
            double y = 0;
            ArrayList<PPath> labelNodes = (ArrayList<PPath>) label.getAttribute("nodes");
            for (int j = 0; j < labelNodes.size(); j++) {
                x += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getX() / labelNodes.size();
                y += ((PPath) labelNodes.get(j)).getFullBoundsReference().getCenter2D().getY() / labelNodes.size();
            }
            label.setX(x - label.getWidth() / 2);
            label.setY(y - label.getHeight() / 2);
        }

        // position the edges in the new layout
        for (int i = 1; i < edgeLayer.getAllNodes().size(); i++) {
            PPath edge = (PPath) ((ArrayList<PPath>) edgeLayer.getAllNodes()).get(i);
            PPath node1 = (PPath) edge.getAttribute("node1");
            PPath node2 = (PPath) edge.getAttribute("node2");
            Point2D start = node1.getFullBoundsReference().getCenter2D();
            Point2D end = node2.getFullBoundsReference().getCenter2D();
            edge.reset();
            edge.moveTo((float) start.getX(), (float) start.getY());
            edge.lineTo((float) end.getX(), (float) end.getY());
        }

        // the entire graph is fitted into the main graphical view
        final Rectangle2D drag_bounds = getCamera().getUnionOfLayerFullBounds();
        getCamera().animateViewToCenterBounds(drag_bounds, true, 0);

        // due to zooming the graph cannot be more than 2 times smaller or more
        // than 50 times bigger than its initial size
        customPZoomEventHandler.setMinScale(getCamera().getViewScale() / 2);
        customPZoomEventHandler.setMaxScale(getCamera().getViewScale() * 50);

    }

    // the connected components are spaced-out in rows in order of area with
    // from largest to smallest.
    // the layout is approximately as square as possible.
    public void layoutConnectedComponents() {

        // this ArrayList will contain all the connected components in order of
        // area from largest to smallest
        ArrayList<ArrayList<Node>> connectedComponents = new ArrayList<ArrayList<Node>>();

        // create an array.
        // one element for each node.
        // each element contains whether the corresponding node's connected
        // component has been added to connectedComponents.
        // the purpose of the array is to avoid computing the components more
        // than once for different nodes in the same component.
        // the elements are initialized to false by default.
        boolean[] componentFound = new boolean[networkNodes.size()];

        double[] componentTopMostYs = new double[networkNodes.size()];
        double[] componentRightMostXs = new double[networkNodes.size()];
        double[] componentBottomMostYs = new double[networkNodes.size()];
        double[] componentLeftMostXs = new double[networkNodes.size()];

        double componentsTotalWidth = 0;
        double componentsTotalArea = 0;
        // iterate through each node
        for (int i = 0; i < networkNodes.size(); i++) {
            // if the node's component has not been found
            if (!componentFound[i]) {

                // get the node's connected component
                ArrayList<Node> component = new ArrayList<Node>();
                component = getConnectedComponent(component, networkNodes.get(i));

                // find the nodes coordinates and initialize currentComponentTopMostY,
                // currentComponentRightMostX, currentComponentBottomMostY, and
                // currentComponentLeftMostX with them.
                // these variables will hold the top-most y, right-most x,
                // bottom-most y, and left-most x coordinates of the connected
                // component.
                Node node = networkNodes.get(i);
                double currentComponentTopMostY = node.getGraphicalNodeYCoordinate();
                double currentComponentRightMostX = node.getGraphicalNodeXCoordinate();
                double currentComponentBottomMostY = node.getGraphicalNodeYCoordinate();
                double currentComponentLeftMostX = node.getGraphicalNodeXCoordinate();
                // currentComponentTopMostY, currentComponentRightMostX,
                // currentComponentBottomMostY, and currentComponentLeftMostX
                // are updated by iterating through the nodes of the component.
                // the index, j, starts at one because the first node has
                // been considered.
                for (int j = 1; j < component.size(); j++) {
                    node = component.get(j);
                    currentComponentTopMostY = Math.min(currentComponentTopMostY, node.getGraphicalNodeYCoordinate());
                    currentComponentRightMostX = Math.max(currentComponentRightMostX, node.getGraphicalNodeXCoordinate());
                    currentComponentBottomMostY = Math.max(currentComponentBottomMostY, node.getGraphicalNodeYCoordinate());
                    currentComponentLeftMostX = Math.min(currentComponentLeftMostX, node.getGraphicalNodeXCoordinate());
                }

                // the componentFound array and the coordinates arrays are
                // updated for all nodes in the component
                for (int j = 0; j < component.size(); j++) {
                    componentFound[networkNodes.indexOf(component.get(j))] = true;
                    componentTopMostYs[networkNodes.indexOf(component.get(j))] = currentComponentTopMostY;
                    componentRightMostXs[networkNodes.indexOf(component.get(j))] = currentComponentRightMostX;
                    componentBottomMostYs[networkNodes.indexOf(component.get(j))] = currentComponentBottomMostY;
                    componentLeftMostXs[networkNodes.indexOf(component.get(j))] = currentComponentLeftMostX;
                }

                // the width of the component is added to componentsTotalWidth.
                // the additional 100 is the space between components and is
                // also included to make calculations more accurate.
                componentsTotalWidth += currentComponentRightMostX - currentComponentLeftMostX + 100;
                // the area of the component is added to componentsTotalArea.
                // the additional 100 is the space between components and is
                // included to make calculations more accurate.
                componentsTotalArea += (currentComponentBottomMostY - currentComponentTopMostY + 100) * (currentComponentRightMostX - currentComponentLeftMostX + 100);

                // the component is added to the ArrayList of components in
                // order of amount of nodes from largest to smallest amount of
                // nodes
                int j = 0;
                while (j < connectedComponents.size()) {
                    // if the amount of nodes of the current component is less
                    // than the amount of nodes of the component at index j in
                    // connectedComponents, j is increased and the loop is
                    // repeated, otherwise, the loop is broken out of.
                    // this is because the current component should be inserted
                    // in connectedComponents in order of largest amount of
                    // nodes to smallest amount of nodes.
                    if (component.size() < connectedComponents.get(j).size()) {
                        j++;
                    } else {
                        break;
                    }
                }
                connectedComponents.add(j, component);

            }
        }

        // if the current network has been clustered
        if (networkClusters != null) {
            // connectedComponents is populated with the clusters.
            // this is done so that the clusters are laid-out in the
            // same order as they are displayed in the Network Clusters
            // dialog and elsewhere.
            connectedComponents.clear();
            for (Cluster cluster : networkClusters) {
                connectedComponents.add((ArrayList<Node>) cluster.nodes.clone());
            }
        }

        // the average width which each row of components should have is
        // calculated by finding a number which gives the same result when
        // multiplying the average height of the components by it, and when
        // dividing the total width of the components by it.
        // this number represents the amount of rows of components in the layout
        // and is contained in approximateRowAmount.
        // the reason this way of calculating the approximate width is useful,
        // is that the approximate width of each row of components should be
        // similar to the approximate height of all the rows (so the components
        // are laid-out as square as possible).
        double averageComponentWidth = componentsTotalWidth / connectedComponents.size();
        // the value of componentsTotalHeightAdjusted is not exactly the total
        // height of all the components.
        // if there are many components with smaller widths than
        // averageComponentWidth, componentsTotalHeightAdjusted will be larger
        // than the real total height and vice versa.
        double componentsTotalHeightAdjusted = componentsTotalArea / averageComponentWidth;
        double averageComponentHeightAdjusted = componentsTotalHeightAdjusted / connectedComponents.size();
        double approximateRowAmount = Math.sqrt(componentsTotalWidth / averageComponentHeightAdjusted);
        double averageComponentRowWidth = componentsTotalWidth / approximateRowAmount;

        // all the components are laid out in rows.
        // currentComponentLeftMostX contains the left-most x coordinate which
        // the current component should have, its left edge should be inline
        // with this coordinate.
        double currentComponentLeftMostX = 0;
        // currentRowBottomMostY contains the bottom-most y coordinate of all the
        // connected components on the current row.
        // this indicates where the next row of components should be placed
        // (100 pixels below).
        double currentRowBottomMostY = 0;
        // currentRowTopMostY is a variable which keeps track of where the
        // components in the current row should be placed.
        // each component should have its top edge inline with this coordinate.
        double currentRowTopMostY = 0;

        // iterate through all the connected components, and lay them out.
        for (int i = 0; i < connectedComponents.size(); i++) {

            // the index of the first node in the component
            int firstNodeIndex = networkNodes.indexOf(connectedComponents.get(i).get(0));

            // if, when the current component is laid-out in the current row,
            // the row will be too wide, a new row of components is begun
            double currentComponentWidth = componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex];
            // rowWidth + currentComponentWidth / 2 is used instead of just
            // rowWidth, so that the average width of the rows will tend more
            // towards rowWidth rather than all the rows being less than
            // rowWidth
            if (currentComponentLeftMostX + currentComponentWidth > averageComponentRowWidth + currentComponentWidth / 2) {
                currentComponentLeftMostX = 0;
                currentRowTopMostY = currentRowBottomMostY + 100;
            }

            // if the current component has an equal or smaller area to 1 tenth
            // of the previous component's area, a new row of components is
            // begun.
            // this simply separates the components into groups based on their
            // area.
            if (i > 0) {
                // 100 is added to the width and height in computing the area.
                // this is mainly to stop components with one node and no area
                // starting a new line every time.
                double currentComponentArea = (componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex] + 100) * (componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex] + 100);
                int firstNodePreviousComponentIndex = networkNodes.indexOf(connectedComponents.get(i - 1).get(0));
                double previousComponentArea = (componentRightMostXs[firstNodePreviousComponentIndex] - componentLeftMostXs[firstNodePreviousComponentIndex] + 100) * (componentBottomMostYs[firstNodePreviousComponentIndex] - componentTopMostYs[firstNodePreviousComponentIndex] + 100);
                if (currentComponentArea <= previousComponentArea / 10) {
                    currentComponentLeftMostX = 0;
                    currentRowTopMostY = currentRowBottomMostY + 100;
                }
            }

            // iterate through the nodes of the component and lay them out
            // in their new positions
            for (int j = 0; j < connectedComponents.get(i).size(); j++) {

                Node node = connectedComponents.get(i).get(j);
                // currentComponentLeftMostX represents the
                // left-most x coordinate of the current component.
                // node.getGraphicalNodeXCoordinate() - componentLeftMostXs[firstNodeIndex]
                // represents the x coordinate of the current node relative to
                // the left-most x coordinate of the current component.
                // the next expression for y coordinates is analagous.
                node.setGraphicalNodeXCoordinate(currentComponentLeftMostX + node.getGraphicalNodeXCoordinate() - componentLeftMostXs[firstNodeIndex]);
                node.setGraphicalNodeYCoordinate(currentRowTopMostY + node.getGraphicalNodeYCoordinate() - componentTopMostYs[firstNodeIndex]);

            }

            // these variables are updated accordingly, ready for the next
            // component.
            // componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex]
            // gives the width of the current component.
            // the additional 100 is the gap between components.
            currentComponentLeftMostX += componentRightMostXs[firstNodeIndex] - componentLeftMostXs[firstNodeIndex] + 100;
            // componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex]
            // gives the height of the current component
            currentRowBottomMostY = Math.max(currentRowBottomMostY, currentRowTopMostY + componentBottomMostYs[firstNodeIndex] - componentTopMostYs[firstNodeIndex]);

        }
    }

    // create an ArrayList of all the nodes in the same connected
    // component as the current node.
    // this method is recursive.
    public ArrayList<Node> getConnectedComponent(ArrayList<Node> connectedComponent, Node currentNode) {

        // add the currentNode to the component
        connectedComponent.add(currentNode);

        // for each neighbour of the current node, if the neighbour has not been
        // added to the component, add it, then call this method again to check
        // all of its neighbours
        for (Edge edge : currentNode.edges) {

            // if the network has been clustered, edges between nodes which are
            // not in the same cluster should be ignored because clusters are
            // represented by edges connecting their nodes.
            // this code checks networkClusters != null, which means that the
            // network has been clustered.
            if (networkClusters != null) {
                if (edge.node1.cluster != edge.node2.cluster) {
                    continue;
                }
            }

            // the two outer if statements are to check which node is the
            // current node and which is the neighbouring node for the edge
            if (edge.node1.equals(currentNode)) {
                if (!connectedComponent.contains(edge.node2)) {
                    getConnectedComponent(connectedComponent, edge.node2);
                }
            } else if (edge.node2.equals(currentNode)) {
                if (!connectedComponent.contains(edge.node1)) {
                    getConnectedComponent(connectedComponent, edge.node1);
                }
            }
        }

        return connectedComponent;

    }
}