package main;

import object.OBJ_Banana;

/**
 *A class used to fill the obj array and set the objects position 
 * @author BOUGRAIN Nathan, HENRIQUES Valentin
 */
public class AssetSetter {
    GamePanel gp;
    
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Banana();
        gp.obj[0].worldX = (gp.worldWidth*2)-gp.tileSize; // a changer pour les valeur de la sortie 
        gp.obj[0].worldY = gp.worldWidth*2 ; // gp.m.exit

        
    }
}
