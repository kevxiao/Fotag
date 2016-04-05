import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Toolbar extends JPanel implements Observer {

    // PUBLIC

    public Toolbar(FotagModel model) {
        this.fotagModel = model;
        this.fotagModel.addObserver(this);

        this.initUI();

        this.gridBtn.doClick();
    }

    public void update(Observable o, Object arg) {
        if(this.fotagModel.getDisplayMode()) {
            this.gridBtn.getModel().setPressed(false);
            this.gridBtn.setBackground(new Color(240, 240, 240));
            this.listBtn.getModel().setPressed(true);
            this.listBtn.setBackground(new Color(220, 220, 220));
        } else {
            this.listBtn.getModel().setPressed(false);
            this.listBtn.setBackground(new Color(240, 240, 240));
            this.gridBtn.getModel().setPressed(true);
            this.gridBtn.setBackground(new Color(220, 220, 220));
        }
        if(!ratingBtns.get(this.fotagModel.getFilterMode()).isSelected()) {
            ratingBtns.get(this.fotagModel.getFilterMode()).setSelected(true);
        }
    }

    // PRIVATE

    private FotagModel fotagModel;
    private JButton loadBtn;
    private JLabel titleLbl;
    private JButton gridBtn;
    private JButton listBtn;
    private ArrayList<JRadioButton> ratingBtns;
    private ButtonGroup ratingFilter;

    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(0, (int)((Constants.BUTTON_SIZE + 10) * Fotag.ratioDPI)));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        this.loadBtn = new JButton("Load");
        this.loadBtn.setMargin(new Insets(0,0,0,0));
        this.loadBtn.setMinimumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.loadBtn.setPreferredSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.loadBtn.setMaximumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));
                fileChooser.setMultiSelectionEnabled(true);
                int returnVal = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(Toolbar.this));
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    if(files.length > 0) {
                        fotagModel.clearImages();
                        for (File file : files) {
                            try {
                                fotagModel.addImage(new ImageModel(ImageIO.read(file), file.getName(), new Date(file.lastModified()), 0, file.getPath()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        this.add(this.loadBtn, c);

        c.gridx = 2;
        c.insets = new Insets(0, 20, 0, 20);
        c.weightx = 1;
        this.titleLbl = new JLabel("Fotag!");
        Font menuFont = (Font)UIManager.get("Menu.font");
        this.titleLbl.setFont(new Font(menuFont.getFontName(), menuFont.getStyle(), (int)(menuFont.getSize() * Fotag.ratioDPI * 2)));
        this.add(this.titleLbl, c);

        ratingFilter = new ButtonGroup();
        ratingBtns = new ArrayList<>();
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        for(int i = 0; i <= 5; ++i) {
            c.gridx = 5 + i;
            ratingBtns.add(new JRadioButton(i == 0 ? "None" : Integer.toString(i), i == 0));
            ratingBtns.get(i).setVerticalTextPosition(JRadioButton.BOTTOM);
            ratingBtns.get(i).setHorizontalTextPosition(JRadioButton.CENTER);
            ratingFilter.add(ratingBtns.get(i));
            ratingBtns.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(int i = 0; i < ratingBtns.size(); ++i) {
                        if(ratingBtns.get(i).isSelected()) {
                            Toolbar.this.fotagModel.changeFilterMode(i);
                        }
                    }
                }
            });
            this.add(ratingBtns.get(i), c);
        }

        c.gridx = 15;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.LINE_END;
        this.gridBtn = new JButton("Grid");
        this.gridBtn.setMargin(new Insets(0,0,0,0));
        this.gridBtn.setMinimumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.gridBtn.setPreferredSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.gridBtn.setMaximumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.gridBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolbar.this.fotagModel.changeDisplayMode(false);
            }
        });
        this.add(this.gridBtn, c);

        c.gridx = 16;
        c.weightx = 0;
        this.listBtn = new JButton("List");
        this.listBtn.setMargin(new Insets(0,0,0,0));
        this.listBtn.setMinimumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.listBtn.setPreferredSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.listBtn.setMaximumSize(new Dimension((int)(Constants.BUTTON_SIZE * Fotag.ratioDPI), (int)(Constants.BUTTON_SIZE * Fotag.ratioDPI)));
        this.listBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolbar.this.fotagModel.changeDisplayMode(true);
            }
        });
        this.add(this.listBtn, c);
    }

}
