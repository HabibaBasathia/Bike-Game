//package Example2;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader2 {
    public static BufferedImage loadImage(String path) throws IOException {
        //System.out.println("Loading image from path: " + path);
        try (InputStream inputStream = ImageLoader2.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + path);
            }

            BufferedImage img = ImageIO.read(inputStream);
            if (img == null) {
                throw new IOException("Unable to load image: " + path);
            }

            return img;
        }
    }
}
