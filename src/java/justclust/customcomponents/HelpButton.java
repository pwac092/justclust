/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.customcomponents;

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
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import justclust.datastructures.Data;

/**
 *
 * @author wuaz008
 */
public class HelpButton extends JComponent {

    BufferedImage helpButton;

    public HelpButton() {

        // create the image for the HelpButton
        try {
            helpButton = ImageIO.read(new File("img/help_button.png"));
        } catch (IOException ex) {
        }

    }

    // this method displays a custom image on the top of the HelpButton
    public void paint(Graphics g) {

        // the paint method of the super class is called so that the parts of
        // the HelpButton which are not painted in this method are
        // painted in the paint method of the super class
        super.paint(g);

        g.drawImage(helpButton, 0, 0, null);

    }
}
