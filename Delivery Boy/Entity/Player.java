package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.KeyHandler;
import Object.GameObject;

public class Player extends Entity {

    GamePanel gamePanel;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int gameScale;

    public Player(GamePanel gamePanel, KeyHandler keyH) {
        this.gamePanel = gamePanel;
        this.keyH = keyH;
        this.gameScale = gamePanel.gameScale;

        // Set player position at the center of the screen
        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        hitBox = new Rectangle();
        hitBox.x =  4 * gameScale; 
        hitBox.y = 8 * gameScale; 
        hitBox.width = 20 * gameScale;
        hitBox.height = 13 * gameScale;
        defaultHitBoxX = hitBox.x;
        defaultHitBoxY = hitBox.y;  
        
        setDefaultValue();
        getPlayerImage();
    }

    public void setDefaultValue() {
        worldX = gamePanel.tileSize * 23; // Starting point - X
        worldY = gamePanel.tileSize * 21; // Starting point - y
        speed = 4;
        direction = "idle";
    }

    public void getPlayerImage() {
        try {
            //TODO: Get images and fix the read imageio, because its not working
            idle = ImageIO.read(getClass().getResource("/Assets/Player/idle.png"));
            up1 = ImageIO.read(getClass().getResource("/Assets/Player/up1.png"));
            up2 = ImageIO.read(getClass().getResource("/Assets/Player/up2.png"));
            down1 = ImageIO.read(getClass().getResource("/Assets/Player/down1.png"));
            down2 = ImageIO.read(getClass().getResource("/Assets/Player/down2.png"));
            left1 = ImageIO.read(getClass().getResource("/Assets/Player/left1.png"));
            left2 = ImageIO.read(getClass().getResource("/Assets/Player/left2.png"));
            right1 = ImageIO.read(getClass().getResource("/Assets/Player/right1.png"));
            right2 = ImageIO.read(getClass().getResource("/Assets/Player/right2.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update() {
        if(keyH.upPressed == true) {
            direction = "up";
        } 
        else if (keyH.downPressed == true) {
            direction = "down";
        }
        else if (keyH.leftPressed == true) {
            direction = "left";
        }
        else if (keyH.rightPressed == true) {
            direction = "right";
            
        }

        //Check Tile Collison
        collisionOn = false;
        gamePanel.ct.checkTile(this);

        //Check object collison
        checkTouchedObjects();

        //Make sure player is not colliding with a collidable tile
        if (collisionOn == false) {
            if (direction == "up") {
                worldY -= speed;
            } else if (direction == "down") {
                worldY += speed;
            } else if (direction == "left") {
                worldX -= speed;
            } else if (direction == "right") {
                worldX += speed;
            }
        } else {
            //TODO: Set direction to stunned
        }   

        //Change which sprite connected to each direction that should be shown randomly
        spriteCounter++;
        if(spriteCounter > 16) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }

            spriteCounter = 0;
        }
    }

    public void checkTouchedObjects() {
        //TODO: Something wrong. Cant move player
        // Check if any objects is being intercations
        int objIndex = gamePanel.ct.checkObject(this, true);
        for (int i = 0; i < gamePanel.obj.length; i++)    {
            GameObject object = gamePanel.obj[i]; // Object in this index
            if (object != null && objIndex != 999) {
                if (object.needsInteraction == true) {
                    if (isFacingObject(object) && keyH.interactPressed) {
                            object.objectInteraction();
                    }
                } else {
                    object.objectInteraction(); //Tells us what it means to interact with that object
                }            
            }
        }
    }

    private boolean isFacingObject(GameObject object) {
        int objX = object.worldX; // Objects X position
        int objY = object.worldY; // Objects Y position
    
        int playerX = this.worldX; // Players X position
        int playerY = this.worldY; // Players Y position
    
        switch (this.direction) {
            case "up":
                return playerX == objX && playerY - gamePanel.tileSize == objY;
            case "down":
                return playerX == objX && playerY + gamePanel.tileSize == objY;
            case "left":
                return playerX - gamePanel.tileSize == objX && playerY == objY;
            case "right":
                return playerX + gamePanel.tileSize == objX && playerY == objY;
            default:
                return false;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = idle; //Character is not movien
        // Check if no keys are pressed
        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {
            direction = "idle";
        }

        switch(direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
            case "idle":
                image = idle;
                break;

            //TODO: Another case: "stunned", Displays a stunned charachter thats bounces backwards
        }

        //TODO: Use a different x and y

        g2.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null );
    }

}
