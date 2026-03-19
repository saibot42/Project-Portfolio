package Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import Main.GamePanel;

public class TileManager {
    GamePanel gamePanel;
    public Tile[] tile;
    BufferedImage grassTiles; // All grasstiles are in this image
    BufferedImage waterTiles; // All watertiles are in this image
    BufferedImage treeTiles; // All treetiles are in this image
    
    public int mapTileNum[][];

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        
        tile = new Tile[40];
        mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

        this.grassTiles = loadTile("/Assets/Tiles/GRASS+.png");
        this.waterTiles = loadTile("/Assets/Tiles/Water+.png");
        this.treeTiles = loadTile("/Assets/Tiles/Trees+.png");

        populateTileArray();
        loadMap("/Assets/Maps/world1.txt");

    }

    /**
     * This method will add the different tiles that are to be used in the game in to an array.
     * It will use a specific part of a larger image as each tile
     * 
     */
    public void populateTileArray() {
        // Normal grass
        tile[0] = new Tile();
        tile[0].image = grassTiles.getSubimage(0, 0, 16, 16);

        // Normal Waterss
        tile[1] = new Tile();
        tile[1].image = waterTiles.getSubimage(80, 0, 16, 16);
        tile[1].collision = true;

        // Simple tree
        tile[2] = new Tile();
        tile[2].image = loadTile("/Assets/Tiles/Ryisnow/tree.png");
        tile[2].collision = true;
        
        // Yellow-ish dirt
        tile[3] = new Tile();
        tile[3].image = grassTiles.getSubimage(0, 32, 16, 16);
        
        // Simple bush
        tile[4] = new Tile();
        tile[4].image = grassTiles.getSubimage(176, 176, 16, 16);
        
        // Grass with flowers
        tile[5] = new Tile();
        tile[5].image = grassTiles.getSubimage(16, 160, 16, 16);
        
        // Wall
        tile[6] = new Tile();
        tile[6].image = loadTile("/Assets/Tiles/Ryisnow/wall.png");
        tile[6].collision = true;

        // Floor
        tile[7] = new Tile();
        tile[7].image = loadTile("/Assets/Tiles/Ryisnow/floor01.png");

    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Map file not found!");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
    
            int col = 0;
            int row = 0;
    
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
    
                // Loop through each character in the line
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
    
                    // Check if the character is a digit
                    if (Character.isDigit(c)) {
                        int num = Character.getNumericValue(c); // Get the numeric value of the character
                        mapTileNum[col][row] = num; // Store the digit in the map
                        col++; // Move to the next column
    
                        // If the number of columns exceeds maxWorldCol, stop loading
                        if (col >= gamePanel.maxWorldCol) {
                            break;
                        }
                    }
                }
    
                // Reset column index after processing a row
                col = 0;
                row++;
    
                // If the number of rows exceeds maxWorldRow, stop loading
                if (row >= gamePanel.maxWorldRow) {
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tilenum = mapTileNum[worldCol][worldRow];

            //Adjust the frame of view of the player
            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;

            // Player is always at center, so we must offset that
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX; //World position -( players position in the world + player postion on scren)
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
            int edgeBuffer = gamePanel.tileSize * 2; //Used to load beyond the screen to avoid flickering at the edges
            
            //Only draw the tiles that should be visible by the camera
            if (worldX > gamePanel.player.worldX - gamePanel.player.screenX - edgeBuffer &&
                    worldX < gamePanel.player.worldX + gamePanel.player.screenX + edgeBuffer &&
                    worldY > gamePanel.player.worldY - gamePanel.player.screenY - edgeBuffer&&
                    worldY < gamePanel.player.worldY + gamePanel.player.screenY + edgeBuffer) {
                        g2.drawImage(tile[tilenum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            }
            
            worldCol++; //Continue the while loop to draw next tile

            //After we reached most right, then go to next column
            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }

        }

    }

    //Helper function: Retrivies the tile sprites for grass which are all on one image
    private BufferedImage loadTile(String filepath) {
        BufferedImage tileImage = null;
        try {
            tileImage = ImageIO.read(getClass().getResource(filepath));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return tileImage;
    }

}