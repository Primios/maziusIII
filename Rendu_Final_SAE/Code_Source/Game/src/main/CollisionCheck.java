package main;

import entity.Entity;
/**
 * A base class to check if the player hit a wall or an object.
 *
 * @author BOUGRAIN Nathan,HENRIQUES Valentin
 */
public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp){
        this.gp = gp;
    }
    /**
     * Check if the tile encoutered have collision or not
     *
     * @param entity 
     * the entity that encounter the tile
     */
    public void checkTile(Entity entity){
        int entityLeftWorldX = (int) (entity.worldX + entity.solidArea.x); // position X de l'entité dans la map + la position X de la hit boxe de cette entite. => donne la coordonne du coin ifferieur gauche de la hitboxe dans la map
        int entityRightWorldX = (int) (entity.worldX + entity.solidArea.x + entity.solidArea.width); // donne la coordonne du cote droit (coin ifferieur droit) de la hitboxe de l'netite dans la map
        int entityTopWorldY = (int) (entity.worldY + entity.solidArea.y); // donne la coordonne du haut (coin superieur gauche) de la hitboxe dans la map
        int entityBottomWorldY = (int) (entity.worldY + entity.solidArea.y + entity.solidArea.height); // donne la coordonne du bas (coin superieur gauche) de la hitboxe de l'netite dans la map
    
        int entityLeftCol = entityLeftWorldX/gp.tileSize; // nombre de tile a gauche de la hitboxe
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2; // idex de la tile

        switch(entity.direction){
            case "up":
            entityTopRow = (entityTopWorldY-entity.speed)/gp.tileSize;
            tileNum1=gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
            tileNum2=gp.tileM.mapTileNum[entityTopRow][entityRightCol];
            if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true){
                entity.collisionOn=true;
            }
                break;
            case "down":
            entityBottomRow = (entityBottomWorldY+entity.speed)/gp.tileSize;
            tileNum1=gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
            tileNum2=gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
            if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true){
                entity.collisionOn=true;
            }
                break;
            case "left":
            entityLeftCol = (entityLeftWorldX-entity.speed)/gp.tileSize;
            tileNum1=gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
            tileNum2=gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
            if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true){
                entity.collisionOn=true;
            }
                break;
            case "right":
            entityRightCol = (entityRightWorldX+entity.speed)/gp.tileSize;
            tileNum1=gp.tileM.mapTileNum[entityTopRow][entityRightCol];
            tileNum2=gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
            if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true){
                entity.collisionOn=true;
            }
                break;
        }
    }
    //bool player permet de savoir si cest un joueur ou une autre entite
    /**
     * A base class to check if the player hit a wall or an object.
     *
     * @param entity
     * The entity encoutering the object
     *@param player
     *check if the entity is a player or another entity
     */
    public int checkObject(Entity entity, boolean player){
        int index = 999;
        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] != null){
                // get entity's solid area position
                entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x)-gp.tileSize;
                entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y)-gp.tileSize;

                // get the object's solid area posision
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction){
                case "up":
                    entity.solidArea.y -= entity.speed;
                    if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                        //System.out.println("up collision");
                        if(gp.obj[i].collision == true){
                            entity.collisionOn = true;
                        }
                        if(player == true){
                            index =i;
                        }
                    }
                    break;

                case "down":
                    entity.solidArea.y += entity.speed;
                    if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                        System.out.println("down collision");
                        if(gp.obj[i].collision == true){
                            entity.collisionOn = true;
                        }
                        if(player == true){
                            index =i;
                        }
                    }
                    break;

                case "left":
                    entity.solidArea.x -= entity.speed;
                    if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                        //System.out.println("left collision");
                        if(gp.obj[i].collision == true){
                            entity.collisionOn = true;
                        }
                        if(player == true){
                            index = i;
                        }
                    }
                    break;

                case "right":
                    entity.solidArea.x += entity.speed;
                    if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                        //System.out.println("right collision");
                        if(gp.obj[i].collision == true){
                            entity.collisionOn = true;
                        }
                        if(player == true){
                            index =i;
                        }
                    }
                    break;
                }
                // reset entity/object solid area
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;

                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }
        return index; // retourne l'index de l objet dans obj[]
    }
}


