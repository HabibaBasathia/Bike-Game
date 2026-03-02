//package Example2;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovingObject extends Object2 {

    public MovingObject(String path, int startX, int startY) throws IOException {
        super(path, startX, startY);
    }

    public void moveDown(int speed) {
        this.y += speed;
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

}
