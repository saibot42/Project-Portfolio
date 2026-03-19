package Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

// GameObject is an abastract class so that the classes extending GameObject are responsible
// for implementing the methods from Iobject
public abstract class GameObject implements IObject{

    public BufferedImage image;
    public String name;
    public boolean interacted = false; // Has the object been interacted with or not - Basically collision
    public boolean needsInteraction = false; // Does the object need player action for something to happen
    public int worldX, worldY;
    public Rectangle hitBox = new Rectangle(0, 0, 48, 48); //Set specific x by using hitbox.x for example
    public int defaultHitBoxX = 0;
    public int defaultHitBoxY = 0;

    /**
     * Draws objects on screen that are in frame based on character position
     * @param g2
     * @param gamePanel
     */
    public void draw(Graphics2D g2, GamePanel gamePanel) {
        // Player is always at center, so we must offset that
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX; //World position -( players position in the world + player postion on scren)
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        int edgeBuffer = gamePanel.tileSize * 2; //Used to load beyond the screen to avoid flickering at the edges
        
        //Only draw the tiles that should be visible by the camera
        if (worldX > gamePanel.player.worldX - gamePanel.player.screenX - edgeBuffer &&
                worldX < gamePanel.player.worldX + gamePanel.player.screenX + edgeBuffer &&
                worldY > gamePanel.player.worldY - gamePanel.player.screenY - edgeBuffer&&
                worldY < gamePanel.player.worldY + gamePanel.player.screenY + edgeBuffer) {
                   
                    g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }

    /**
     * Helper method to set image based on filepath
     * Used by classes extending gameObject to safely load pictures
     * 
     * @param filepath
     */
    public void getObjectImage(String filepath) {
        try {
            image = ImageIO.read(getClass().getResource(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
