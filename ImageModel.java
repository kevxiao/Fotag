import java.awt.image.BufferedImage;
import java.util.Date;

public class ImageModel {

    //PUBLIC

    public ImageModel(BufferedImage img, String nm, Date dt, int rt, String pt) {
        this.image = img;
        this.name = nm;
        this.creationDate = dt;
        this.rating = rt;
        this.path = pt;
    }

    public void changeRating(int newRating) {
        if(newRating < 0) {
            newRating = 0;
        } else if(newRating > 5) {
            newRating = 5;
        }
        rating = newRating;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getFileName() {
        return name;
    }

    public Date getDate() {
        return creationDate;
    }

    public int getRating() {
        return rating;
    }

    public String getPath() {
        return path;
    }


    //PRIVATE

    private BufferedImage image;
    private String name;
    private Date creationDate;
    private int rating;
    private String path;

}
