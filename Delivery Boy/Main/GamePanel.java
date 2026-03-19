package Main;

import java.awt.*;

import javax.swing.*;

import Entity.Player;
import Tile.TileManager;
import Object.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS - Maybe make another class just for settings?
    public final int originalTileSize = 32; //32x32 tile
    public final int gameScale = 2; //So we can make the tiles bigger and easier to see

    public final int tileSize = originalTileSize * gameScale;

    //Make screen grid to make 16x12 in size
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 1000;
    public final int maxWorldRow = 1853;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    int FPS = 60;
    
    //TILES
    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //Game Loop
    public CollisionTracker ct = new CollisionTracker(this);

    //OBJECT
    public ObjectSpawner oSpawner = new ObjectSpawner(this);
    public GameObject obj[] = new GameObject[10]; //Possible to display 10 objects at a time (can be increased)

    //PLAYER
    public Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true); // All drawing from this component will be done in an offscreen painting buffer
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void gameSetUp() {
        oSpawner.spawnObject();
    }

    /**s
     * Initializes the game loop thread 
     * 
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // Limit system performance so that the game is actually runnable
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                //Moved update() to paintcomponent
                repaint();
                delta--;
                drawCount++;
                // Optionally, track FPS:
                if (timer >= 1000000000) {
                    // Reset the timer and draw count every second
                    System.out.println("FPS: " + drawCount);
                    drawCount = 0;
                    timer = 0;
                }
            }
        }
    }

    /**
     * Updates information of whats going on in the game
     * Example: Character position
     * 
     */
    public void update() {
        player.update();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        update();

        Graphics2D g2 = (Graphics2D) g;
        //TILE
        tileManager.draw(g2); // Draws tiles
        
        //OBJECT
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        
        //PLAYER
        player.draw(g2);

        g2.dispose();
    }
}   
