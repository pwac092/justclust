package justclust.menubar.exportgraph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import justclust.Main;
import justclust.datastructures.Cluster;

import justclust.datastructures.Data;
import justclust.JustclustJFrame;
import justclust.datastructures.Edge;
import justclust.datastructures.Node;
import justclust.menubar.filefilters.CSVFileFilter;
import justclust.plugins.configurationcontrols.FileSystemPathControl;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class ExportGraphActionListener implements ActionListener {

    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getActionCommand().equals("Export Graph")) {

            ExportGraphJDialog.classInstance.exportGraphJButton.setEnabled(false);

            // this code starts a new thread to export the graph.
            // the reason for the new thread is that it allows the progress bar
            // to be animated in the event dispatch thread (this one) while the
            // graph is being exported in the new thread.
            ExportGraphThread exportGraphThread = new ExportGraphThread();
            exportGraphThread.start();

        }

    }

    // this inner class has a method which exports the current graph with a new
    // thread
    class ExportGraphThread extends Thread {

        // this method exports the current graph with a new thread
        public void run() {

            // the exportGraphJProgressBar is added to the exportGraphJPanel
            ExportGraphJDialog.classInstance.exportGraphJPanel.add(ExportGraphJDialog.classInstance.exportGraphJProgressBar);

            // set the y coordinate of the ExportGraphJDialog so that it remains
            // centered around the point it currently is around when its height
            // is increased.
            // the difference in height between the new height and old height
            // is halved and taken away from the current y coordinate of the
            // ExportGraphJDialog.
            // also, increase the height of the ExportGraphJDialog.
            ExportGraphJDialog.classInstance.setBounds(
                    ExportGraphJDialog.classInstance.getLocation().x,
                    ExportGraphJDialog.classInstance.getLocation().y
                    - Math.round((10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    - ExportGraphJDialog.classInstance.exportGraphJPanel.getHeight()) / 2),
                    ExportGraphJDialog.classInstance.getWidth(),
                    ExportGraphJDialog.classInstance.getInsets().top
                    + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10 + 25 + 10
                    + ExportGraphJDialog.classInstance.getInsets().bottom);

            ExportGraphJDialog.classInstance.exportGraphJProgressBar.setIndeterminate(true);

            ExportGraphJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            JustclustJFrame.classInstance.statusBarJLabel.setText("Exporting graph...");

            try {

                // get the current Data instance for the following code to use
                int currentCustomGraphEditorIndex = JustclustJFrame.classInstance.justclustJTabbedPane.getSelectedIndex();

                BufferedImage bufferedImage = new BufferedImage(
                        JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).getWidth(),
                        JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                // the paintButtons field is set to false while the
                // Graph is being painted to the image file so that
                // the buttons are not included in the image
                JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).paintButtons = false;
                JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).paint(bufferedImage.getGraphics());
                JustclustJFrame.classInstance.customGraphEditors.get(currentCustomGraphEditorIndex).paintButtons = true;

                // the graph is to be saved into a PNG file
                if (ExportGraphJDialog.classInstance.fileNameJTextField.getText().toLowerCase().endsWith(".png")) {
                    ImageIO.write(bufferedImage, "png", new File(ExportGraphJDialog.classInstance.fileNameJTextField.getText()));
                }
                // the graph is to be saved into a JPG file
                if (ExportGraphJDialog.classInstance.fileNameJTextField.getText().toLowerCase().endsWith(".jpg")) {
                    ImageIO.write(bufferedImage, "jpg", new File(ExportGraphJDialog.classInstance.fileNameJTextField.getText()));
                }
                // the graph is to be saved into a PDF file
                if (ExportGraphJDialog.classInstance.fileNameJTextField.getText().toLowerCase().endsWith(".pdf")) {

                    // the document
                    PDDocument doc = new PDDocument();

//                            PDPage page = new PDPage();
                    PDPage page = new PDPage();
                    doc.addPage(page);

//                            PDXObjectImage ximage = new PDJpeg(doc, new FileInputStream("/home/local/wuaz008/Useful Files/New Folder/graph_image_1.jpg"));
                    File graphFile = new File("graph.png");
                    ImageIO.write(bufferedImage, "png", graphFile);
                    bufferedImage = ImageIO.read(graphFile);
                    graphFile.delete();
                    double resizeFactor = Math.min((double) page.getMediaBox().getWidth() / bufferedImage.getWidth(), (double) page.getMediaBox().getHeight() / bufferedImage.getHeight());
                    BufferedImage resized = new BufferedImage(
                            (int) Math.round(bufferedImage.getWidth() * resizeFactor),
                            (int) Math.round(bufferedImage.getHeight() * resizeFactor),
                            bufferedImage.getType());
                    Graphics2D g = resized.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(bufferedImage, 0, 0, resized.getWidth(), resized.getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                    g.dispose();
                    PDJpeg ximage = new PDJpeg(doc, resized);
                    PDPageContentStream contentStream = new PDPageContentStream(doc, page);

                    contentStream.drawImage(
                            ximage,
                            page.getMediaBox().getWidth() - resized.getWidth(),
                            page.getMediaBox().getHeight() - resized.getHeight());

                    contentStream.close();
                    doc.save(new File(ExportGraphJDialog.classInstance.fileNameJTextField.getText()));

                }
                // the graph is to be saved into a SVG file
                if (ExportGraphJDialog.classInstance.fileNameJTextField.getText().toLowerCase().endsWith(".svg")) {

                    // Create an SVG document.
                    DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
                    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
                    SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

                    // Create a converter for this document.
                    SVGGraphics2D g = new SVGGraphics2D(doc);

                    if (Data.data.get(currentCustomGraphEditorIndex).networkNodes.size() >= 1) {

                        // Do some drawing.
                        double minXCoordinate = Data.data.get(currentCustomGraphEditorIndex).networkNodes.get(0).graphicalNode.getFullBoundsReference().getCenter2D().getX();
                        double maxXCoordinate = Data.data.get(currentCustomGraphEditorIndex).networkNodes.get(0).graphicalNode.getFullBoundsReference().getCenter2D().getX();
                        double minYCoordinate = Data.data.get(currentCustomGraphEditorIndex).networkNodes.get(0).graphicalNode.getFullBoundsReference().getCenter2D().getY();
                        double maxYCoordinate = Data.data.get(currentCustomGraphEditorIndex).networkNodes.get(0).graphicalNode.getFullBoundsReference().getCenter2D().getY();
                        for (Node node : Data.data.get(currentCustomGraphEditorIndex).networkNodes) {
                            minXCoordinate = Math.min(minXCoordinate, node.graphicalNode.getFullBoundsReference().getCenter2D().getX());
                            maxXCoordinate = Math.max(maxXCoordinate, node.graphicalNode.getFullBoundsReference().getCenter2D().getX());
                            minYCoordinate = Math.min(minYCoordinate, node.graphicalNode.getFullBoundsReference().getCenter2D().getY());
                            maxYCoordinate = Math.max(maxYCoordinate, node.graphicalNode.getFullBoundsReference().getCenter2D().getY());
                        }
                        double width = maxXCoordinate - minXCoordinate;
                        double height = maxYCoordinate - minYCoordinate;
                        g.setSVGCanvasSize(new Dimension((int) Math.round(10 + 10 + width + 10 + 10), (int) Math.round(10 + 10 + height + 10 + 10)));
                        for (Edge edge : Data.data.get(currentCustomGraphEditorIndex).networkEdges) {
                            if (edge.visible) {
                                g.setColor(edge.colour);
                                g.drawLine(
                                        (int) Math.round(10 + 10 + (edge.node1.graphicalNode.getFullBoundsReference().getCenter2D().getX() - minXCoordinate)),
                                        (int) Math.round(10 + 10 + (edge.node1.graphicalNode.getFullBoundsReference().getCenter2D().getY() - minYCoordinate)),
                                        (int) Math.round(10 + 10 + (edge.node2.graphicalNode.getFullBoundsReference().getCenter2D().getX() - minXCoordinate)),
                                        (int) Math.round(10 + 10 + (edge.node2.graphicalNode.getFullBoundsReference().getCenter2D().getY() - minYCoordinate)));
                            }
                        }
                        for (Node node : Data.data.get(currentCustomGraphEditorIndex).networkNodes) {
                            if (node.visible) {
                                g.setColor(node.colour);
                                g.fillOval(
                                        (int) Math.round(10 + 10 + (node.graphicalNode.getFullBoundsReference().getCenter2D().getX() - minXCoordinate) - 10),
                                        (int) Math.round(10 + 10 + (node.graphicalNode.getFullBoundsReference().getCenter2D().getY() - minYCoordinate) - 10),
                                        20,
                                        20);
                                g.setColor(Color.BLACK);
                                g.drawOval(
                                        (int) Math.round(10 + 10 + (node.graphicalNode.getFullBoundsReference().getCenter2D().getX() - minXCoordinate) - 10),
                                        (int) Math.round(10 + 10 + (node.graphicalNode.getFullBoundsReference().getCenter2D().getY() - minYCoordinate) - 10),
                                        20,
                                        20);
                            }
                        }
                        g.setColor(Color.BLACK);
                        for (Node node : Data.data.get(currentCustomGraphEditorIndex).networkNodes) {
                            if (node.visible) {
                                g.setFont(node.graphicalLabel.getFont());
                                FontMetrics fontMetrics = g.getFontMetrics();
                                g.drawString(
                                        node.label,
                                        (int) Math.round(10 + 10 + ((node.graphicalLabel.getFullBoundsReference().getCenter2D().getX() - fontMetrics.stringWidth(node.label) / 2) - minXCoordinate)),
                                        (int) Math.round(10 + 10 + ((node.graphicalLabel.getFullBoundsReference().getCenter2D().getY() + (fontMetrics.getAscent() - 2) / 2) - minYCoordinate)));
                            }
                        }
                        for (Edge edge : Data.data.get(currentCustomGraphEditorIndex).networkEdges) {
                            if (edge.visible && edge.label != null) {
                                g.setFont(edge.graphicalLabel.getFont());
                                FontMetrics fontMetrics = g.getFontMetrics();
                                g.drawString(
                                        edge.label,
                                        (int) Math.round(10 + 10 + ((edge.graphicalLabel.getFullBoundsReference().getCenter2D().getX() - fontMetrics.stringWidth(edge.label) / 2) - minXCoordinate)),
                                        (int) Math.round(10 + 10 + ((edge.graphicalLabel.getFullBoundsReference().getCenter2D().getY() + (fontMetrics.getAscent() - 2) / 2) - minYCoordinate)));
                            }
                        }
                        if (Data.data.get(currentCustomGraphEditorIndex).networkClusters != null) {
                            for (Cluster cluster : Data.data.get(currentCustomGraphEditorIndex).networkClusters) {
                                if (cluster.label != null) {
                                    g.setFont(cluster.graphicalLabel.getFont());
                                    FontMetrics fontMetrics = g.getFontMetrics();
                                    g.drawString(
                                            cluster.label,
                                            (int) Math.round(10 + 10 + ((cluster.graphicalLabel.getFullBoundsReference().getCenter2D().getX() - fontMetrics.stringWidth(cluster.label) / 2) - minXCoordinate)),
                                            (int) Math.round(10 + 10 + ((cluster.graphicalLabel.getFullBoundsReference().getCenter2D().getY() + (fontMetrics.getAscent() - 2) / 2) - minYCoordinate)));
                                }
                            }
                        }

                    }

                    Element svgRoot = g.getRoot();
                    OutputStream os = new FileOutputStream(new File(ExportGraphJDialog.classInstance.fileNameJTextField.getText()));
                    Writer w = new OutputStreamWriter(os, "iso-8859-1");
                    g.stream(svgRoot, w);

                }

            } catch (IOException | NullPointerException | COSVisitorException exception) {

                ExportGraphJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JustclustJFrame.classInstance.statusBarJLabel.setText("");

                // the exportGraphJProgressBar is removed from the exportGraphJPanel
                ExportGraphJDialog.classInstance.exportGraphJPanel.remove(ExportGraphJDialog.classInstance.exportGraphJProgressBar);

                // set the y coordinate of the ExportGraphJDialog so that it remains
                // centered around the point it currently is around when its height
                // is decreased.
                // the difference in height between the new height and old height
                // is halved and added to the current y coordinate of the
                // ExportGraphJDialog.
                // also, decrease the height of the ExportGraphJDialog.
                ExportGraphJDialog.classInstance.setBounds(
                        ExportGraphJDialog.classInstance.getLocation().x,
                        ExportGraphJDialog.classInstance.getLocation().y
                        + Math.round((10 + 25) / 2),
                        ExportGraphJDialog.classInstance.getWidth(),
                        ExportGraphJDialog.classInstance.getInsets().top
                        + 10 + 16 + 10 + 1 + 10 + 25 + 10 + 25 + 10 + 1 + 10 + 25 + 10
                        + ExportGraphJDialog.classInstance.getInsets().bottom);

                ExportGraphJDialog.classInstance.exportGraphJButton.setEnabled(true);

                JOptionPane.showMessageDialog(JustclustJFrame.classInstance, "Exporting could not be completed due to error");

                return;

            }

            ExportGraphJDialog.classInstance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            ExportGraphJDialog.classInstance.dispose();

            JustclustJFrame.classInstance.statusBarJLabel.setText("Graph exported");

        }
    }
}
