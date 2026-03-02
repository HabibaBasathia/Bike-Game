package Example2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Object2 {
	protected BufferedImage image;
	protected int x;
	protected int y;
	
	
	 public Object2(String path, int startX, int startY) throws IOException {
	          setImage(ImageLoader2.loadImage(path));
	        x = startX;
	        y = startY;
	    }

	    public BufferedImage getImage() {
			return image;
	    }
	    
	    
	    public int getX() {
	        return x;
	    }

	    public void setX(int x) {
	        this.x = x;
	    }

	    public int getY() {
	        return y;
	    }

	    public void setY(int y) {
	        this.y = y;
	    }
	    
	    public BufferedImage resizeImage(double d) {
	        int newWidth = (int) (getImage().getWidth() * d);
	        int newHeight = (int) (getImage().getHeight() * d);
	        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, getImage().getType());
	        Graphics2D g = resizedImage.createGraphics();
	        g.drawImage(getImage(), 0, 0, newWidth, newHeight, null);
	        g.dispose();
	        return resizedImage;
	    }

		public void setImage(BufferedImage image) {
			this.image = image;
		}

}
