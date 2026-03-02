//package Example2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Bike2 extends Object2 {
    private BufferedImage originalImage;
    private BufferedImage alternateImageRight;
    private BufferedImage alternateImageLeft;

    public Bike2(String path, int startX, int startY) throws IOException {
        super(path, startX, startY);
        originalImage = image; // Assuming 'image' is already loaded in Object2 constructor
        // Load alternate images
        alternateImageRight = ImageIO.read(getClass().getResourceAsStream("bike_right.png"));
        alternateImageLeft = ImageIO.read(getClass().getResourceAsStream("bike_left.png"));
    }

    public void moveLeft() {
        this.x -= 10;
    }

    public void moveRight() {
        this.x += 10;
    }

    public void setAlternateImageRight() {
        setImage(alternateImageRight);
    }

    public void setAlternateImageLeft() {
        setImage(alternateImageLeft);
    }

    public void setOriginalImage() {
        setImage(originalImage);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    

}
