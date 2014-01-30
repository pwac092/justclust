/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.menubar.about;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
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
import justclust.JustclustJFrame;

/**
 *
 * @author wuaz008
 */
public class AboutJPanel extends JPanel {
    
    // the image which is used for the JustClust icon
    BufferedImage justclustIcon;

    AboutJPanel() {

        // create the image for the JustClust icon
        try {
            justclustIcon = ImageIO.read(new File("img/justclust_icon.png"));
        } catch (IOException ex) {
        }
        Image transparentJustclustIcon = TransformColorToTransparency(justclustIcon, new Color(255, 0, 255), new Color(255, 0, 255));
        justclustIcon = ImageToBufferedImage(transparentJustclustIcon, justclustIcon.getWidth(), justclustIcon.getHeight());

    }

    // this method extends the super class' paint method with additional
    // graphics to display in the AboutJPanel
    public void paint(Graphics g) {

        // the paint method of the super class is called so that the parts of
        // the AboutJPanel which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        // drawing the JustClust icon
        g.drawImage(justclustIcon, 150, 10, 200, 200, null);

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
    
}
