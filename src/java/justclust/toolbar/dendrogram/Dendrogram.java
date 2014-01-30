/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.dendrogram;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author Dimitrios Zervas 26/05/2008
 */
public final class Dendrogram {

    private static final int FONT_SIZE = 12;
    public static final int SHOW_REAL_SCALE = 0;
    public static final int SHOW_LOG10_SCALE = 1;
    public static final int SHOW_LEVELS = 2;
    private static final int LEFT_BORDER_WIDTH = 8;
    private static final int HLABEL_OFFSET = 5;
    private static final int TICK_HEIGHT = 3;
//    private DendrogramCluster root = null;
    private ArrayList<DendrogramCluster> roots = null;
    private String labels[] = null;
    private int labelOffsetY = 0;
    private int fontHeight = 0;
    private int left = 0;
    private int top;
    private int numElements = 0;
    private int width = 0;
    private int height = 0;
    private int[] log10Scale;
    private double log10Step;
    private double log10InnerStep;
    private int scaleType = SHOW_REAL_SCALE;
    public double maxDistance;

//    public Dendrogram(DendrogramCluster cluster, String[] lbls, int num) {
    public Dendrogram(ArrayList<DendrogramCluster> clusters, String[] lbls, int num) {

//        root = cluster;
        roots = (ArrayList<DendrogramCluster>) clusters.clone();
        numElements = num;
        labels = lbls;

        clusters = (ArrayList<DendrogramCluster>) clusters.clone();
        maxDistance = 0;
        while (!clusters.isEmpty()) {
            maxDistance = Math.max(maxDistance, clusters.get(0).distance);
            if (clusters.get(0).left != null) {
                clusters.add(clusters.get(0).left);
            }
            if (clusters.get(0).right != null) {
                clusters.add(clusters.get(0).right);
            }
            clusters.remove(0);
        }

        setSizes();

    }

    private void setSizes() {
        BufferedImage bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        FontMetrics fm = g.getFontMetrics();

        fontHeight = fm.getHeight();

        int maxLen = 0;
        for (int i = 0; i < labels.length; i++) {
            int len = fm.stringWidth(labels[i]);
            if (len > maxLen) {
                maxLen = len;
            }
        }


        left = maxLen + 2 * LEFT_BORDER_WIDTH;
        top = fontHeight * 2;

        labelOffsetY = top + fontHeight;

        width = 500 + left;
        height = numElements * fontHeight + top + fontHeight;

        g.dispose();
    }

    public void setScaleType(int type) {
        scaleType = type;
    }

    public int getScaleType() {
        return scaleType;
    }

    public int getNumElements() {
        return numElements;
    }

    public int getWidth() {
        return width + 40;
    }

    public int getHeight() {
        return height + 40;
    }

    public void draw(Graphics2D g) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        setSizes();

        FontMetrics fm = g.getFontMetrics();

        NumberFormat nf = NumberFormat.getInstance();

        if (scaleType == SHOW_REAL_SCALE) {
//            int numTicks = 20;
            double numTicks = (int) Math.round((double) (width - left) / 50);
//            double numStep = root.getDistance() / (double) numTicks;
            double numStep = maxDistance / (double) numTicks;
            double stepX = (double) (width - left) / (double) numTicks;


            nf.setMaximumFractionDigits(4);

//            double num = 0;
            double num = maxDistance;
            double offsetX = (double) left;
            g.setColor(Color.black);
            for (int i = 0; i <= numTicks; i++) {
                String str = nf.format(num);
                int x = (int) offsetX;
                g.drawString(str, x - fm.stringWidth(str) / 2, top - HLABEL_OFFSET);
                g.drawLine(x, top, x, top - TICK_HEIGHT);
//                num += numStep;
                num -= numStep;
                if (num < 0) {
                    num = 0;
                }
                offsetX += stepX;
            }
        } else if (scaleType == SHOW_LEVELS) {

            double stepX = (double) (width - left) / (double) numElements;

            double offsetX = (double) left + stepX;
            g.setColor(Color.black);
            for (int i = 1; i <= numElements; i++) {

                int x = (int) offsetX;
                if ((i % 4 == 0) || (i == 1) || (i == numElements)) {
                    String str = Integer.toString(i);
                    g.drawString(str, x - fm.stringWidth(str) / 2, top - HLABEL_OFFSET);
                }

                g.drawLine(x, top, x, top - TICK_HEIGHT);

                offsetX += stepX;

            }
        } else if (scaleType == SHOW_LOG10_SCALE) {

//            double maxDist = root.getDistance();

            int count = 0;
            double step = 1.0;
            do {
                step *= 10.0;
                count++;
//            } while (step <= maxDist);
            } while (step <= maxDistance);

            int numElem = count + 1;
            log10Scale = new int[numElem];
            log10Scale[0] = 0;
            step = 1.0;
            for (int i = 1; i < log10Scale.length; i++) {
                step *= 10.0;
                log10Scale[i] = (int) step;
            }


            log10Step = (double) (width - left) / (double) count;

            double offsetX = (double) left;
            g.setColor(Color.black);
            for (int i = 0; i < log10Scale.length; i++) {
                String str = Integer.toString(log10Scale[i]);
                int x = (int) offsetX;
                g.drawString(str, x - fm.stringWidth(str) / 2, top - HLABEL_OFFSET);
                g.drawLine(x, top, x, top - TICK_HEIGHT);

                int maxSteps = 9;
                int startOffset = 2;
                int endOffset = maxSteps;
                if (i == 0) {
                    maxSteps = 10;
                    startOffset = 1;
                }

                if (i == (log10Scale.length - 1)) {
                    break;
                }

                double log10Step2 = log10Step / (double) maxSteps;
                double offsetX2 = offsetX + log10Step2;
                int scale = (int) Math.pow(10.0, (double) i);
                log10InnerStep = log10Step2;
                for (int j = startOffset; j <= endOffset; j++) {
                    str = Integer.toString(j * scale);
                    x = (int) offsetX2;
                    g.drawString(str, x - fm.stringWidth(str) / 2, top - HLABEL_OFFSET);
                    g.drawLine(x, top, x, top - TICK_HEIGHT);

                    offsetX2 += log10Step2;
                }

                offsetX += log10Step;
            }

        }

        g.setColor(Color.black);
        g.drawRect(left, top, width - left, height - top);

        for (DendrogramCluster dendrogramCluster : roots) {
            drawLabels(g, dendrogramCluster);
            drawLines(g, dendrogramCluster);
        }

    }

    private void drawLabels(Graphics2D g, DendrogramCluster p) {
        if (p != null) {
            drawLabels(g, p.getLeft());

//            if (p.size() == 1) {
            if (p.left == null && p.right == null) {

                FontMetrics fm = g.getFontMetrics();

//                int i = p.elementAt(0);
                int i = p.nodeIndex;
                g.setColor(Color.black);
                g.drawString(labels[i], left - (fm.stringWidth(labels[i]) + LEFT_BORDER_WIDTH), labelOffsetY);
                labelOffsetY += fontHeight;

//                p.setPos(new Point(left, labelOffsetY - (fontHeight + fontHeight / 3)));
                p.setPos(new Point(left, labelOffsetY - (fontHeight + fontHeight / 3)));

            } else {

                if (scaleType == SHOW_REAL_SCALE) {
//                    int offsetX = left + (int) (p.getDistance() * (double) (width - left) / root.getDistance());
                    int offsetX = left + (int) ((maxDistance - p.getDistance()) * (double) (width - left) / maxDistance);

                    p.setPos(new Point(offsetX, labelOffsetY - (fontHeight + fontHeight / 3)));
                } else if (scaleType == SHOW_LEVELS) {

//                    int offsetX = left + (int) (p.getLevel() * (double) (width - left) / (double) root.getLevel());

//                    p.setPos(new Point(offsetX, labelOffsetY - (fontHeight + fontHeight / 3)));
                    p.setPos(new Point(0, labelOffsetY - (fontHeight + fontHeight / 3)));
                    /*
                     int rem = (int)p.getLevel() % 4;
          
                     if (rem == 0){
                     NumberFormat nf = NumberFormat.getInstance();
                     nf.setMaximumFractionDigits(6);

                     FontMetrics fm = g.getFontMetrics();

                     g.setColor(Color.black);
                     String str = nf.format(p.getDistance());
                     g.drawString(str, offsetX-fm.stringWidth(str) / 2, top - HLABEL_OFFSET);
                     g.drawLine(offsetX, top, offsetX, top - TICK_HEIGHT);
                     }
                     */

                } else if (scaleType == SHOW_LOG10_SCALE) {

                    int offsetX = left;
                    int index = 0;
                    for (int i = 0; i < log10Scale.length - 1; i++) {
                        if (p.getDistance() >= log10Scale[i] && p.getDistance() < log10Scale[i + 1]) {
                            offsetX = left + (int) ((double) i * log10Step);
                            index = i + 1;
                            break;
                        } // end if
                    } // end for i

                    double scale = log10Scale[index];
                    double max = log10Step;
                    int offset = 0;
                    if (p.getDistance() > 10) {
                        max *= (10.0 / 9.0);
                        offset = (int) log10InnerStep;
                    }

                    offsetX += (int) (p.getDistance() * max / scale);

                    p.setPos(new Point(offsetX - offset, labelOffsetY - (fontHeight + fontHeight / 3)));

                } // end else if

            }

            drawLabels(g, p.getRight());
        }
    }

    private void drawLines(Graphics2D g, DendrogramCluster p) {

        if (p != null) {

            drawLines(g, p.getLeft());

            g.setColor(Color.red);
//            if (p.size() > 1) {
            if (p.left != null && p.right != null) {

                int x1 = p.getPos().x;
                int y1 = p.getPos().y;
                int x2 = p.getLeft().getPos().x;
                int y2 = p.getLeft().getPos().y;
                int x3 = p.getRight().getPos().x;
                int y3 = p.getRight().getPos().y;

                g.drawLine(x1, y2, x1, y3);

                g.drawLine(x2, y2, x1, y2);

                g.drawLine(x3, y3, x1, y3);
            }

            drawLines(g, p.getRight());
        }
    }
}
