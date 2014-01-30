/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author computer
 */
public class HeatMapJPanel extends JPanel {

    HeatMapMatrix matrix = null;
    String[] labels = null;
    int width = 0;
    int height = 0;
    int fontSize = 12;
//    int squareSize;
    int squareWidth;
    int squareHeight;
    double scale = 1.0;
    int offsetX = 0;
    int offsetY = 0;
    boolean showValue = false;
    int border = 2;
    ArrayList<Integer> nodeAmountPerCluster;

//    public HeatMapJPanel(HeatMapMatrix m, String[] l) {
    public HeatMapJPanel(HeatMapMatrix m, String[] l, ArrayList<Integer> nodeAmountPerCluster) {

        this.setBackground(Color.white);
        this.setFocusable(false);

        this.matrix = m;

        if (matrix == null) {
            return;
        }

        labels = l;

        this.width = matrix.getNumCols();
        this.height = matrix.getNumRows();

        this.nodeAmountPerCluster = nodeAmountPerCluster;

    }

    public void setShowValue(boolean val) {
        showValue = val;
    }

    public void setScale(double s) {
        scale = s;
        this.repaint();
    }

    public String getValueAt(int x, int y) {

        if (matrix == null) {
            return null;
        }


//        x = (int) ((double) ((x - ((double) offsetX * scale)) / ((double) squareSize * scale)));
        x = (int) ((double) ((x - ((double) offsetX * scale)) / ((double) squareWidth * scale)));
//        y = (int) ((double) ((y - ((double) offsetY * scale)) / ((double) squareSize * scale)));
        y = (int) ((double) ((y - ((double) offsetY * scale)) / ((double) squareHeight * scale)));

        //System.out.println(x + " " + y);

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }

        NumberFormat nf = NumberFormat.getInstance();

        nf.setMaximumFractionDigits(4);

        return labels[y] + ", " + labels[x] + " = " + nf.format(matrix.get(x, y));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (matrix == null) {
            return;
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.PLAIN, fontSize));

        g2d.scale(scale, scale);

        g2d.setColor(Color.black);

        FontMetrics fm = g.getFontMetrics();

//        squareSize = fm.getHeight();
        if (HeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected()) {
            squareWidth = fm.getHeight();
            squareHeight = fm.getHeight();
        } else {
            squareWidth = 3;
            squareHeight = 3;
        }

        AffineTransform transform = g2d.getTransform();

        int maxLen = 0;
        for (int i = 0; i < labels.length; i++) {
            int len = fm.stringWidth(labels[i]);
            if (len > maxLen) {
                maxLen = len;
            }
        }

        if (HeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected()) {
            offsetX = maxLen + 4;
            offsetY = maxLen + 4;
        } else {
            offsetX = 4;
            offsetY = 4;
        }

        if (HeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected()) {
            for (int i = 0; i < labels.length; i++) {
//            g2d.drawString(labels[i], offsetX - fm.stringWidth(labels[i]), offsetY + squareSize + i * squareSize + border);
                g2d.drawString(labels[i], offsetX - fm.stringWidth(labels[i]), offsetY + squareHeight + i * squareHeight + border);
            }
        }

        if (HeatMapJDialog.classInstance.includeLabelsJCheckBox.isSelected()) {
            for (int i = 0; i < labels.length; i++) {
//            g2d.translate(offsetX + squareSize + i * squareSize, offsetY);
                g2d.translate(offsetX + squareWidth + i * squareWidth, offsetY);
                g2d.rotate(-Math.toRadians(90.0f));
                g2d.drawString(labels[i], 0, 0);
                g2d.setTransform(transform);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                double val = matrix.get(x, y);
                double val = matrix.get(y, x);

                double correlThreshold = 0;
                Color posCorrelColorMax = Color.GREEN;
                Color posCorrelColorMin = Color.RED;
                Color negCorrelColorMax = Color.RED;
                Color negCorrelColorMin = Color.GREEN;

//            if (val >= Diagram.correlThreshold) {
                if (val >= correlThreshold) {

//                float dr = Diagram.posCorrelColorMax.getRed() - Diagram.posCorrelColorMin.getRed();
                    float dr = posCorrelColorMax.getRed() - posCorrelColorMin.getRed();
//                 float dg = Diagram.posCorrelColorMax.getGreen() - Diagram.posCorrelColorMin.getGreen();
                    float dg = posCorrelColorMax.getGreen() - posCorrelColorMin.getGreen();
//                 float db = Diagram.posCorrelColorMax.getBlue() - Diagram.posCorrelColorMin.getBlue();
                    float db = posCorrelColorMax.getBlue() - posCorrelColorMin.getBlue();

                    float ir = dr;
                    float ig = dg;
                    float ib = db;

//                int red = Diagram.posCorrelColorMin.getRed() + (int)(ir * val);
                    int red = posCorrelColorMin.getRed() + (int) (ir * val);
//                int green = Diagram.posCorrelColorMin.getGreen() + (int)(ig * val);
                    int green = posCorrelColorMin.getGreen() + (int) (ig * val);
//                int blue = Diagram.posCorrelColorMin.getBlue() + (int)(ib * val);    
                    int blue = posCorrelColorMin.getBlue() + (int) (ib * val);

                    g2d.setColor(new Color(red, green, blue));
                } else //        if (val <= -Diagram.correlThreshold) {
                if (val <= -correlThreshold) {

//                float dr = Diagram.negCorrelColorMax.getRed() - Diagram.negCorrelColorMin.getRed();
                    float dr = negCorrelColorMax.getRed() - negCorrelColorMin.getRed();
//                float dg = Diagram.negCorrelColorMax.getGreen() - Diagram.negCorrelColorMin.getGreen();
                    float dg = negCorrelColorMax.getGreen() - negCorrelColorMin.getGreen();
//                float db = Diagram.negCorrelColorMax.getBlue() - Diagram.negCorrelColorMin.getBlue();
                    float db = negCorrelColorMax.getBlue() - negCorrelColorMin.getBlue();

                    float ir = dr;
                    float ig = dg;
                    float ib = db;

//                int red = Diagram.negCorrelColorMin.getRed() + (int)(ir * Math.abs(val));
                    int red = negCorrelColorMin.getRed() + (int) (ir * Math.abs(val));
//                int green = Diagram.negCorrelColorMin.getGreen() + (int)(ig * Math.abs(val));
                    int green = negCorrelColorMin.getGreen() + (int) (ig * Math.abs(val));
//                int blue = Diagram.negCorrelColorMin.getBlue() + (int)(ib * Math.abs(val));    
                    int blue = negCorrelColorMin.getBlue() + (int) (ib * Math.abs(val));

                    g2d.setColor(new Color(red, green, blue));
                } else {
                    g2d.setColor(Color.white);
                }

//                g2d.fillRect(offsetX + x * squareSize + border, offsetY + y * squareSize + border, squareSize, squareSize);
                g2d.fillRect(offsetX + x * squareWidth + border, offsetY + y * squareHeight + border, squareWidth, squareHeight);
            }
        }

        //  if (showVal){
        //    PopupBox popup = new PopupBox(ptext, pos, g2d);
        //  }

        g2d.setColor(Color.black);
//        g2d.drawRect(
//                offsetX + border,
//                offsetY + border,
//                width * squareSize,
//                height * squareSize);
        g2d.drawRect(
                offsetX + border,
                offsetY + border,
                width * squareWidth,
                height * squareHeight);

        if (nodeAmountPerCluster != null) {
            int sum = 0;
            for (int i = 0; i < nodeAmountPerCluster.size(); i++) {
                sum += nodeAmountPerCluster.get(i);
                g2d.drawLine(
                        offsetX + border,
                        offsetY + border + sum * squareHeight,
                        offsetX + border + width * squareWidth,
                        offsetY + border + sum * squareHeight);
                g2d.drawLine(
                        offsetX + border + sum * squareWidth,
                        offsetY + border,
                        offsetX + border + sum * squareWidth,
                        offsetY + border + height * squareHeight);
            }
        }

        Dimension area = new Dimension();
//        area.width = (int) ((double) (2 * offsetX + squareSize * width) * scale);
        area.width = (int) ((double) (2 * offsetX + squareWidth * width) * scale);
//        area.height = (int) ((double) (2 * offsetY + squareSize * height) * scale);
        area.height = (int) ((double) (2 * offsetY + squareHeight * height) * scale);

        setPreferredSize(area);

        revalidate();
    }

    public void draw(Graphics g) {
        paintComponent(g);
    }
}
