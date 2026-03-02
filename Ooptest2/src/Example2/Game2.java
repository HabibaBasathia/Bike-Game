//package Example2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game2 extends JFrame {
    private static final int TREE_AREA_X1 = 50;
    private static final int TREE_AREA_Y1 = 150;
    private static final int TREE_AREA_X2 = 100;
    private static final int TREE_AREA_Y2 = 300;

    private String playerName;
    private JLabel highScoreLabel;
    private int highScore = 0;
    private boolean isGameOver = false;
    private JButton playAgainButton;
    private long startTime;


    
    private Timer treeGenerationTimer;
    private Background2 background;
    private Bike2 bike;
    private List<MovingObject> lightsR;
    private List<MovingObject> lightsL;
    private List<MovingObject> cars;
    private BufferedImage combinedImage;
    private List<MovingObject> trees;
    private Timer timer;
    private int roadMove = 0;
    private Random random;

    private static final String[] CAR_IMAGES = {
            "car1.png",
            "car2.png",
            "car3.png",
            "car5.png"
    };

    private BufferedImage[] carImages;

    public Game2(String playerName) {
    	this.playerName = playerName;
        try {
            loadImages();
            initializeGameObjects();
            initializeUI();
            startGameLoop();
        } catch (IOException e) {
            showError(e.getMessage());
        }
        treeGenerationTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTreeCluster();
            }
        });
        treeGenerationTimer.start();
    }

    private void loadImages() throws IOException {
        carImages = new BufferedImage[CAR_IMAGES.length];
        for (int i = 0; i < CAR_IMAGES.length; i++) {
            carImages[i] = ImageIO.read(getClass().getResource(CAR_IMAGES[i]));
        }
    }

    private List<MovingObject> generateTreeCluster() {
        List<MovingObject> treeObjects = new ArrayList<>();
        Random random = new Random();
        int clusterSize = random.nextInt(5) + 1; // Random cluster size between 1 and 5

        for (int i = 0; i < clusterSize; i++) {
            int x = random.nextInt(TREE_AREA_X2 - TREE_AREA_X1) + TREE_AREA_X1;
            int y = random.nextInt(TREE_AREA_Y2 - TREE_AREA_Y1) + TREE_AREA_Y1;
            try {
                treeObjects.add(new MovingObject("tree3.png", x, y));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return treeObjects;
    }

    private List<MovingObject> generateOppositeTreeCluster() {
        List<MovingObject> treeObjects = new ArrayList<>();
        Random random = new Random();
        int clusterSize = random.nextInt(5) + 1; // Random cluster size between 1 and 5

        for (int i = 0; i < clusterSize; i++) {
            int x = background.getImage().getWidth() - (random.nextInt(TREE_AREA_X2 - TREE_AREA_X1) + TREE_AREA_X1);
            int y = random.nextInt(TREE_AREA_Y2 - TREE_AREA_Y1) + TREE_AREA_Y1;
            try {
                treeObjects.add(new MovingObject("tree3.png", x, y));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return treeObjects;
    }

    private void initializeGameObjects() throws IOException {
        random = new Random();
        background = new Background2("roadF.jpeg");
        bike = new Bike2("bbb.png", 0, 0);
        cars = new ArrayList<>();

        lightsR = new ArrayList<>();
        lightsL = new ArrayList<>();

        // Initialize right lights
        int baseXRight = 360;
        int baseYRight = background.getImage().getHeight() / 14;
        for (int i = 0; i < 7; i++) {
            lightsR.add(new MovingObject("lightright.png", baseXRight + (i * 48), baseYRight * (i + 3) - 50));
        }

        // Initialize left lights
        int baseXLeft = background.getImage().getWidth() - 460;
        int baseYLeft = background.getImage().getHeight() / 14;
        for (int i = 0; i < 7; i++) {
            lightsL.add(new MovingObject("lightleft.png", baseXLeft - (i * 48), baseYLeft * (i + 3) - 50));
        }

        bike.setX((background.getImage().getWidth() - bike.resizeImage(0.5).getWidth()) / 2);
        bike.setY((background.getImage().getHeight() - bike.resizeImage(0.5).getHeight()) + 50);
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game");

        GamePanel gamePanel = new GamePanel();
        setContentPane(gamePanel);
        
        
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Player: " + playerName);
        highScoreLabel = new JLabel("High Score: 0");

        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(highScoreLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        
        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        playAgainButton.setVisible(false);  // Hide the button initially
        add(playAgainButton, BorderLayout.SOUTH);



        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLightsPosition();
                updateCarsPosition();
                try {
                    generateRandomCar();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                checkCollision();
                gamePanel.repaint();
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleBikeMovement(e.getKeyCode());
                gamePanel.repaint();
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }
    private void checkCollision() {
        // Resize bike image for accurate collision detection
        BufferedImage bikeImage = bike.resizeImage(0.5);
        Rectangle bikeRect = new Rectangle(bike.getX(), bike.getY(), bikeImage.getWidth(), bikeImage.getHeight());
        
        System.out.println("Bike Rect: " + bikeRect); // Debugging

        for (MovingObject car : cars) {
            // Resize car image for accurate collision detection
            BufferedImage carImage = car.resizeImage(0.25);
            Rectangle carRect = new Rectangle(car.getX(), car.getY(), carImage.getWidth(), carImage.getHeight());
            
            System.out.println("Car Rect: " + carRect); // Debugging

            if (bikeRect.intersects(carRect)) {
                System.out.println("Collision Detected");
                // Debugging
                isGameOver = true;
                playAgainButton.setVisible(true);
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!\nPlayer: " + playerName + "\nHigh Score: " + highScore, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void updateHighScore() {
        if (!isGameOver) { // Check if the game is still running
            long currentTime = System.currentTimeMillis();
            int currentScore = (int) ((currentTime - startTime) / 1000); // Calculate elapsed time in seconds
            if (currentScore > highScore) { // Update high score if the current score is higher
                highScore = currentScore;
            }
            highScoreLabel.setText("High Score: " + highScore); // Update high score label
        }
    }



    private void handleBikeMovement(int key) {
        if (key == KeyEvent.VK_RIGHT) {
            bike.setAlternateImageRight();
            bike.moveRight();
            new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bike.setOriginalImage();
                }
            }).start();
        } else if (key == KeyEvent.VK_LEFT) {
            bike.setAlternateImageLeft();
            bike.moveLeft();
            new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bike.setOriginalImage();
                }
            }).start();
        }

        int maxX = background.getImage().getWidth() - bike.resizeImage(0.5).getWidth();
        bike.setX(Math.max(0, Math.min(bike.getX(), maxX)));
    }

    private void startGameLoop() {
        startTime = System.currentTimeMillis(); // Record the start time when the game starts
        
        Timer highScoreTimer = new Timer(1000, new ActionListener() { // Timer to update high score every second
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHighScore();
            }
        });
        highScoreTimer.start();
        
        // Additional game loop logic if needed
    }


    private void showError(String message) {
        System.out.println("Error loading images: " + message);
        JOptionPane.showMessageDialog(this, "Error loading images: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void updateLightsPosition() {
        int width = background.getImage().getWidth();
        int initialYForLights = (background.getImage().getHeight() / 14) * 3 - 50;

        for (MovingObject lightR : lightsR) {
            lightR.setY(lightR.getY() + 10);
            lightR.setX(lightR.getX() + 10);
            if (lightR.getX() > width) {
                lightR.setY(initialYForLights);
                lightR.setX(360);
            }
        }

        for (MovingObject lightL : lightsL) {
            lightL.setY(lightL.getY() + 10);
            lightL.setX(lightL.getX() - 10);
            if (lightL.getX() < 0) {
                lightL.setY(initialYForLights);
                lightL.setX(background.getImage().getWidth() - 450);
            }
        }
        roadMove = (roadMove == 0) ? 1 : 0;
    }

    public void updateCarsPosition() {
        int height = background.getImage().getHeight();
        cars.removeIf(car -> car.getY() > height);
        cars.forEach(car -> car.moveDown(9));
    }

    public void generateRandomCar() throws IOException {
        if (random.nextInt(100) > 1) {
            // Find the position of the rightmost lamplight on the right side
            int rightmostX = Integer.MIN_VALUE;
            for (MovingObject light : lightsR) {
                rightmostX = Math.max(rightmostX, light.getX());
            }

            // Find the position of the leftmost lamplight on the left side
            int leftmostX = Integer.MAX_VALUE;
            for (MovingObject light : lightsL) {
                leftmostX = Math.min(leftmostX, light.getX());
            }

            // Adjust the positions to ensure cars are generated within the specified range
            int carSpawnOffsetRight = rightmostX - 300;
            int carSpawnOffsetLeft = leftmostX + 300;

            // Generate a random x-coordinate for car generation within the specified range
            int x = random.nextInt(carSpawnOffsetRight - carSpawnOffsetLeft) + carSpawnOffsetLeft;

            // Generate the car if the position is valid
            if (!isPositionOccupied(80, 100) && isDistanceSufficient(200)) {
                String carImage = CAR_IMAGES[random.nextInt(CAR_IMAGES.length)];
                cars.add(new MovingObject(carImage, x, 210));
            }
        }
    }

    private boolean isPositionOccupied(int x, int yThreshold) {
        return cars.stream().anyMatch(car -> car.getX() == x && car.getY() <= yThreshold);
    }

    private boolean isDistanceSufficient(int minDistance) {
        return cars.stream().allMatch(car -> Math.abs(car.getY() - 200) >= minDistance);
    }
    private void restartGame() {
        isGameOver = false;
        playAgainButton.setVisible(false);

        // Reset game objects and variables
        bike.setX((background.getImage().getWidth() - bike.resizeImage(0.5).getWidth()) / 2);
        bike.setY((background.getImage().getHeight() - bike.resizeImage(0.5).getHeight()) + 50);
        cars.clear();

        // Restart the game loop timer
        timer.start();
    }

   

    private class GamePanel extends JPanel {
        public GamePanel() {
            setDoubleBuffered(true);
            setPreferredSize(new Dimension(background.getImage().getWidth(), background.getImage().getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage combinedImage = combineImages();
            g.drawImage(combinedImage, 0, 0, null);
            
            if (isGameOver) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("Game Over", getWidth() / 2 - 150, getHeight() / 2);
            }
        }

        private BufferedImage combineImages() {
            BufferedImage largeImage = background.getImage();

            combinedImage = new BufferedImage(
                    largeImage.getWidth(),
                    largeImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(largeImage, 0, 0, null);
          

            drawObjects(g2d, lightsR, 0.3);
            drawObjects(g2d, lightsL, 0.3);
            drawWhitePatches(g2d);
            drawObjects(g2d, cars, 0.25);

            // Initialize treeObjects list with trees generated by generateTreeCluster()
            List<MovingObject> treeObjects = generateTreeCluster();
            drawObjects(g2d, treeObjects, 0.2); // Draw trees with full scale
            List<MovingObject> treeObjects1 = generateOppositeTreeCluster();
            drawObjects(g2d, treeObjects1, 0.2);
            
            //Drawing bike
          BufferedImage smallImage = bike.resizeImage(0.3);
          g2d.drawImage(smallImage, bike.getX(), bike.getY(), null);

            g2d.dispose();
            return combinedImage;
        }

        private void drawObjects(Graphics2D g2d, List<MovingObject> objects, double scale) {
            if (objects == null) {
                System.err.println("Warning: objects list is null in drawObjects method.");
                return;
            }

            for (MovingObject obj : objects) {
                BufferedImage objImage = obj.resizeImage(scale);
                g2d.drawImage(objImage, obj.getX(), obj.getY(), null);
            }
        }

        private void drawWhitePatches(Graphics g) {
            int start = (roadMove == 0) ? 0 : 50;
            for (int i = start; i <= 700; i += 100) {
                g.setColor(Color.white);
                g.fillRect(360, i, 10, 50);
            }
        }
        
    }
    
    
}
    
    //public static void main(String[] args) {
       // SwingUtilities.invokeLater(Game2::new);
  //  }

