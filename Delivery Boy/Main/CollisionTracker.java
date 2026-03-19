package Main;

import Entity.Entity;

public class CollisionTracker {

    GamePanel gamePanel;
    int ts; // Tilesize

    public CollisionTracker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.ts = gamePanel.tileSize;
    }

    /**
     * Checks if entity and tile collides and weither or not that tile has collidable
     * 
     * @param entity
     */
    public void checkTile(Entity entity) {

        //Find the coordinates of the players hitbox based on world postion and hitbox stats
        int entityLeftX = entity.worldX + entity.hitBox.x; 
        int entityRightX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopY = entity.worldY + entity.hitBox.y;
        int entityBottomY = entity.worldY + entity.hitBox.y + entity.hitBox.height;

        //Find Column and row number from the coordinates above
        int entityLeftCol = entityLeftX / ts;
        int entityRightCol = entityRightX / ts;

        int entityTopRow = entityTopY / ts;
        int entityBottomRow = 
        entityBottomY / ts;

        int leftTile, rightTile;

        //Checks if it will walk into 
        if (entity.direction == "up") {
            entityTopRow = (entityTopY - entity.speed) / ts;
            leftTile = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
            rightTile = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
            if (gamePanel.tileManager.tile[leftTile].collision == true || gamePanel.tileManager.tile[rightTile].collision == true) {
                entity.collisionOn = true;
            }
        } else if (entity.direction == "down") {
            entityBottomRow = (entityBottomY + entity.speed) / ts;
            leftTile = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            rightTile = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if (gamePanel.tileManager.tile[leftTile].collision == true || gamePanel.tileManager.tile[rightTile].collision == true) {
                entity.collisionOn = true;
            }
        } else if (entity.direction == "left") {
            entityLeftCol = (entityLeftX - entity.speed) / ts;
            leftTile = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
            rightTile = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
            if (gamePanel.tileManager.tile[leftTile].collision == true || gamePanel.tileManager.tile[rightTile].collision == true) {
                entity.collisionOn = true;
            }
        } else if (entity.direction == "right") {
            entityRightCol = (entityRightX + entity.speed) / ts;
            leftTile = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
            rightTile = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
            if (gamePanel.tileManager.tile[leftTile].collision == true || gamePanel.tileManager.tile[rightTile].collision == true) {
                entity.collisionOn = true;
            }
        }
    }

    /**
     * Checks if entity and objects collides and what object the player collides with
     * 
     * @param entity 
     * @param player is a boolean that tells us if the entity is a player
     * @return
     */
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] != null) {
                //Get entitys hitbox postion on the map
                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;

                //Get objects hitbox on the on the map
                gamePanel.obj[i].hitBox.x = gamePanel.obj[i].worldX + gamePanel.obj[i].hitBox.x;
                gamePanel.obj[i].hitBox.y = gamePanel.obj[i].worldY + gamePanel.obj[i].hitBox.y;

                switch (entity.direction) {
                case "up":
                    entity.hitBox.y -= entity.speed;
                    //Use interesects to check if entities are tocuhing or not
                    if(entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                        if (gamePanel.obj[i].interacted == true) {
                            entity.collisionOn = true;
                        }
                        if (player == true) {
                            index = i;
                        }
                    }
                    break;
                case "down":
                    entity.hitBox.y += entity.speed;
                    if(entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                        if (gamePanel.obj[i].interacted == true) {
                            entity.collisionOn = true;
                        }
                        if (player == true) {
                            index = i;
                        }
                    }
                    break;
                case "left":
                    entity.hitBox.x -= entity.speed;
                    if(entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                        if (gamePanel.obj[i].interacted == true) {
                            entity.collisionOn = true;
                        }
                        if (player == true) {
                            index = i;
                        }
                    }
                    break;
                case "right":
                    entity.hitBox.x += entity.speed;
                    if(entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                        if (gamePanel.obj[i].interacted == true) {
                            entity.collisionOn = true;
                        }
                        if (player == true) {
                            index = i;
                        }
                    }
                    break;
                }
                entity.hitBox.x = entity.defaultHitBoxX;
                entity.hitBox.y = entity.defaultHitBoxY;
                gamePanel.obj[i].hitBox.x = gamePanel.obj[i].defaultHitBoxX;
                gamePanel.obj[i].hitBox.y = gamePanel.obj[i].defaultHitBoxY;

            }
        }
        
        return index;
    }
}
