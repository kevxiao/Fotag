import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImagePanel extends JPanel implements Observer{

    // PUBLIC

    public ImagePanel(FotagModel model) {
        this.fotagModel = model;
        this.curRatingFilter = 0;
        this.curLayout = false;
        this.fotagModel.addObserver(this);
        this.initUI();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                imagesPnl.setPreferredSize(new Dimension(ImagePanel.this.getSize().width, imagesPnl.getComponentCount() > 0 ? imagesPnl.getComponents()[0].getHeight() * imagesPnl.getComponentCount() : 0));
            }
        });
    }

    public void update(Observable o, Object arg) {
        ArrayList<ImageModel> images = this.fotagModel.getImages();
        if(this.fotagModel.getDisplayMode() != this.curLayout) {
            this.curLayout = this.fotagModel.getDisplayMode();
            imagesPnl.setLayout(this.curLayout ? new BoxLayout(imagesPnl, BoxLayout.PAGE_AXIS) : new FlowLayout());
            for(Component imgItm : imagesPnl.getComponents()) {
                ((ImageItem)imgItm).changeLayout(this.curLayout);
            }
        } else if(this.curRatingFilter != this.fotagModel.getFilterMode()) {
            this.curRatingFilter = this.fotagModel.getFilterMode();
            imagesPnl.removeAll();
            for (ImageModel img : images) {
                if(img.getRating() >= this.curRatingFilter) {
                    imagesPnl.add(new ImageItem(img, this.curLayout));
                }
            }
        } else if(images.size() > imagesPnl.getComponentCount()) {
            for (ImageModel img : images.subList(imagesPnl.getComponentCount(), images.size())) {
                if(img.getRating() >= this.curRatingFilter) {
                    imagesPnl.add(new ImageItem(img, this.curLayout));
                }
            }
        } else if(images.size() == 0) {
            imagesPnl.removeAll();
        }
        imagesPnl.repaint();
        imagesPnl.revalidate();
    }

    // PRIVATE

    private FotagModel fotagModel;
    private JPanel imagesPnl;
    private int curRatingFilter;
    private boolean curLayout;

    private void initUI() {
        this.setLayout(new BorderLayout());

        imagesPnl = new JPanel();
        imagesPnl.setLayout(this.fotagModel.getDisplayMode() ? new BoxLayout(imagesPnl, BoxLayout.PAGE_AXIS) : new FlowLayout());

        JScrollPane sc = new JScrollPane(imagesPnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(sc, BorderLayout.CENTER);
    }

    private class ImageItem extends JPanel {

        // PUBLIC

        public ImageItem(ImageModel img, boolean lt) {
            this.setPreferredSize(new Dimension((int)(200 * Fotag.ratioDPI), (int)(300 * Fotag.ratioDPI)));
            this.setLayout(new BoxLayout(this, lt ? BoxLayout.LINE_AXIS : BoxLayout.PAGE_AXIS));
            Border paddingBorder = BorderFactory.createEmptyBorder(0, (int)(20 * Fotag.ratioDPI), 0, (int)(20 * Fotag.ratioDPI));
            this.imgLbl = new JLabel();
            this.imgLbl.setPreferredSize(new Dimension((int)(200 * Fotag.ratioDPI), (int)(150 * Fotag.ratioDPI)));
            this.imgLbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    new ImageFrame(img.getImage());
                }
            });
            this.add(this.imgLbl);
            this.imgLbl.setIcon(new ImageIcon(img.getImage().getScaledInstance(this.imgLbl.getPreferredSize().width, this.imgLbl.getPreferredSize().height, Image.SCALE_SMOOTH)));
            this.path = img.getPath();
            this.nameLbl = new JLabel(img.getFileName());
            this.nameLbl.setBorder(paddingBorder);
            this.nameLbl.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(this.nameLbl);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String fileDate = df.format(img.getDate());
            this.dateLbl = new JLabel(fileDate);
            this.dateLbl.setBorder(paddingBorder);
            this.dateLbl.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(this.dateLbl);
            this.ratingPnl = new JPanel();
            this.ratingPnl.setLayout(new FlowLayout());
            this.ratingPnl.setPreferredSize(new Dimension((int)(200 * Fotag.ratioDPI), (int)(50 * Fotag.ratioDPI)));
            this.ratingPnl.setMaximumSize(this.ratingPnl.getPreferredSize());
            this.ratingBtns = new ArrayList<>();
            this.ratingFilter = new ButtonGroup();
            for(int i = 0; i <= 5; ++i) {
                this.ratingBtns.add(new JRadioButton(i == 0 ? "None" : Integer.toString(i), i == img.getRating()));
                this.ratingBtns.get(i).setVerticalTextPosition(JRadioButton.BOTTOM);
                this.ratingBtns.get(i).setHorizontalTextPosition(JRadioButton.CENTER);
                this.ratingFilter.add(ratingBtns.get(i));
                this.ratingBtns.get(i).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(int i = 0; i < ImageItem.this.ratingBtns.size(); ++i) {
                            if(ImageItem.this.ratingBtns.get(i).isSelected()) {
                                ImagePanel.this.fotagModel.changeImageRating(ImageItem.this.path, i);
                            }
                        }
                    }
                });
                this.ratingPnl.add(ratingBtns.get(i));
            }
            this.ratingPnl.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(ratingPnl);
            this.layout = false;
        }

        public void changeLayout(boolean newLayout) {
            if(newLayout != this.layout) {
                this.layout = newLayout;
                if(this.layout) {
                    this.setPreferredSize(new Dimension(0, (int)(150 * Fotag.ratioDPI)));
                    this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
                } else {
                    this.setPreferredSize(new Dimension((int)(200 * Fotag.ratioDPI), (int)(300 * Fotag.ratioDPI)));
                    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                }
                this.repaint();
                this.revalidate();
            }
        }

        // PRIVATE

        private JLabel imgLbl;
        private JLabel nameLbl;
        private JLabel dateLbl;
        private JPanel ratingPnl;
        private boolean layout;
        private String path;
        private ArrayList<JRadioButton> ratingBtns;
        private ButtonGroup ratingFilter;

    }

}
