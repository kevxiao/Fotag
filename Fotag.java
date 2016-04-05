import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Fotag extends JFrame implements Observer{

    // PUBLIC

    public static final double ratioDPI = Toolkit.getDefaultToolkit().getScreenResolution() / Constants.DEFAULT_DPI;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Fotag();
            }
        });
    }

    public Fotag() {
        this.setTitle("Fotag");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setResizable(true);

        this.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width / Constants.DEFAULT_DIMENSIONS_RATIO), (int) (Toolkit.getDefaultToolkit().getScreenSize().height / Constants.DEFAULT_DIMENSIONS_RATIO)));
        this.setMinimumSize(new Dimension((int) (Fotag.ratioDPI * Constants.MIN_WIDTH), (int) (Fotag.ratioDPI * Constants.MIN_HEIGHT)));

        Font menuFont = (Font)UIManager.get("Menu.font");
        Font f = new Font(menuFont.getFontName(), menuFont.getStyle(), (int)(menuFont.getSize() * Fotag.ratioDPI));
        Color color = new Color(240, 240, 240);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while(keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if(value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
            if(value != null && value instanceof javax.swing.plaf.ColorUIResource && key.toString().contains(".background")) {
                UIManager.put(key, color);
            }
        }

        this.fotagModel = new FotagModel();
        this.initUI();
        try {
            File persist = new File("fotag_persist");
            if(persist.exists()) {
                Scanner scanner = new Scanner(persist);
                int n = scanner.nextInt();
                for (int i = 0; i < n; ++i) {
                    scanner.nextLine();
                    File file = new File(scanner.nextLine());
                    this.fotagModel.addImage(new ImageModel(ImageIO.read(file), file.getName(), new Date(file.lastModified()), scanner.nextInt(), file.getPath()));
                }
                this.fotagModel.changeDisplayMode(scanner.nextBoolean());
                this.fotagModel.changeFilterMode(scanner.nextInt());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.fotagModel.addObserver(this);

        this.pack();
        this.setVisible(true);
    }

    public void update(Observable o, Object arg) {
        try {
            FileWriter fileWriter = new FileWriter("fotag_persist");
            String output = "";
            output += Integer.toString(this.fotagModel.getImages().size());
            for(ImageModel img : this.fotagModel.getImages()) {
                output += "\n" + img.getPath() + "\n" + Integer.toString(img.getRating());
            }
            output += "\n" + Boolean.toString(this.fotagModel.getDisplayMode());
            output += "\n" + Integer.toString(this.fotagModel.getFilterMode());
            output += "\n";
            fileWriter.write(output);
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // PRIVATE

    private FotagModel fotagModel;
    private Toolbar toolbar;
    private ImagePanel imagePanel;

    private void initUI() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.toolbar = new Toolbar(this.fotagModel);
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        this.add(this.toolbar, c);
        this.imagePanel = new ImagePanel(this.fotagModel);
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        this.add(this.imagePanel, c);
    }
}