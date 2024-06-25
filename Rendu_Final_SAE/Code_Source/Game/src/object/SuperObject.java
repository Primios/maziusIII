package object;
import java.awt.image.*;
import java.awt.*;
import main.GamePanel;
/**
 * Super object representating an item such as the banana
 * 
 * @author BOUGRAIN Nathan, HENRIQUES Valentin
 */
public class SuperObject {
	
	/**
	 * The item image
	 */
    public BufferedImage image;
	
	/**
	 * The item name
	 */
    public String name;
	
	/**
	 * Does this item have collision
	 */
    public boolean collision = false;
	/**
	 * Position of the item
	 */
    public int worldX, worldY; //position
	/**
	 * Rectangle representating the hitbox of the item
	 */
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    /**
     * Draws the object on the given graphics object.
     *
     * @param g2 the graphics object to draw with
     * @param gp the game panel the object belongs to
     */
    public void draw(Graphics2D g2, GamePanel gp){
        int screenX=(int) (worldX-gp.player.worldX+gp.player.screenX+gp.tileSize);
        int screenY=(int) (worldY-gp.player.worldY+gp.player.screenY+gp.tileSize);

            if(worldX+gp.tileSize>gp.player.worldX-gp.player.screenX &&
                worldX-gp.tileSize<gp.player.worldX+gp.player.screenX&&
                worldY+gp.tileSize>gp.player.worldY-gp.player.screenY &&
                worldY-gp.tileSize<gp.player.worldY+gp.player.screenY){

                    g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize,null);
            }
    }

}
