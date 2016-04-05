import java.awt.*;

public class Constants {

    // PUBLIC

    public static final int DEFAULT_DPI = 96;
    public static final double MIN_WIDTH = 700;
    public static final double MIN_HEIGHT = 300;
    public static final double DEFAULT_DIMENSIONS_RATIO = 1.5;
    public static final double BUTTON_SIZE = 50;

    // PRIVATE

    // prevent creating an instance of this class
    private Constants(){
        throw new AssertionError();
    }
}
