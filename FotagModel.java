import java.util.ArrayList;
import java.util.Observable;

public class FotagModel extends Observable {

    // PUBLIC

    public FotagModel() {
        this.displayMode = false;
        this.filterMode = 0;
        this.images = new ArrayList<>();
    }

    public ArrayList<ImageModel> getImages() {
        return this.images;
    }

    public boolean getDisplayMode() {
        return this.displayMode;
    }

    public int getFilterMode() {
        return filterMode;
    }

    public ImageModel getImage(String path) {
        for(ImageModel img : images) {
            if(img.getPath().equals(path)) {
                return img;
            }
        }
        return null;
    }

    public void addImage(ImageModel img) {
        this.images.add(img);
        this.setChanged();
        this.notifyObservers();
    }

    public void changeDisplayMode(boolean mode) {
        this.displayMode = mode;
        this.setChanged();
        this.notifyObservers();
    }

    public void changeFilterMode(int mode) {
        this.filterMode = mode;
        this.setChanged();
        this.notifyObservers();
    }

    public void changeImageRating(String path, int rt) {
        for(ImageModel img : images) {
            if(img.getPath().equals(path)) {
                img.changeRating(rt);
                this.setChanged();
                this.notifyObservers();
            }
        }
    }

    public void clearImages() {
        this.images.clear();
        this.setChanged();
        this.notifyObservers();
    }

    // PRIVATE

    private ArrayList<ImageModel> images;
    private boolean displayMode; // false is grid, true is list
    private int filterMode;

}
