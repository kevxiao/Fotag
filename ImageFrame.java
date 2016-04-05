import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFrame extends JFrame {

    // PUBLIC

    public ImageFrame(BufferedImage img) {
        this.add(new JLabel(new ImageIcon(img.getScaledInstance((int)(800 * Fotag.ratioDPI), (int)(600 * Fotag.ratioDPI), Image.SCALE_SMOOTH))));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationByPlatform(true);
        this.setPreferredSize(new Dimension((int)(800 * Fotag.ratioDPI), (int)(600 * Fotag.ratioDPI)));
        this.setMinimumSize(this.getPreferredSize());
        this.setMaximumSize(this.getPreferredSize());
        this.setVisible(true);
    }
}
