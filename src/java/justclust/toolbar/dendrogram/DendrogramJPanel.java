/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.dendrogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author computer
 */
public class DendrogramJPanel extends JPanel {

    Dendrogram dendrogram = null;
    double scale = 1.0f;

    public DendrogramJPanel() {
        super();
        this.setBackground(Color.white);
    }

    public DendrogramJPanel(Dendrogram dendro) {
        this();
        set(dendro);
    }

    public Dendrogram getDendrogram() {
        return dendrogram;
    }

    public void set(Dendrogram dendro) {
        dendrogram = dendro;
        this.repaint();
    }

    public void setScale(double s) {
        scale = s;
        this.repaint();
    }

    public void setScaleType(int type) {
        dendrogram.setScaleType(type);
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dendrogram == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.scale(scale, scale);

        dendrogram.draw(g2d);

        int w = (int) ((double) dendrogram.getWidth() * scale + 0.5);
        int h = (int) ((double) dendrogram.getHeight() * scale + 0.5);

        Dimension area = new Dimension();
        area.width = w;
        area.height = h;

        setPreferredSize(area);

        revalidate();

    }

    public void draw(Graphics g) {
        paintComponent(g);
    }
}
