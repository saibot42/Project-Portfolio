package Main;

import Object.*;

public class ObjectSpawner extends Object {
    GamePanel gamePanel;

    public ObjectSpawner (GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void spawnObject() {

        gamePanel.obj[0] = new door();
        gamePanel.obj[0].worldX = 8 * gamePanel.tileSize;
        gamePanel.obj[0].worldY = 20 * gamePanel.tileSize;
        
        gamePanel.obj[1] = new door();
        gamePanel.obj[1].worldX = 17 * gamePanel.tileSize;
        gamePanel.obj[1].worldY = 20 * gamePanel.tileSize;
        
        gamePanel.obj[2] = new customerDoor();
        gamePanel.obj[2].worldX = 23 * gamePanel.tileSize;
        gamePanel.obj[2].worldY = 7 * gamePanel.tileSize;
    }
}
