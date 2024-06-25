package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A base class for entities in the game (such as player).
 *
 * @author BOUGRAIN Nathan,HENRIQUES Valentin
 */

public class Entity {
	/**
	 * The x-coordinate of the entity's position in the world.
	 */
	public double worldX;
	/**
	 * The y-coordinate of the entity's position in the world.
	 */
	public double worldY; // ? taille de l'ecran 
	/**
	 * The speed of the entity.
	 */
	public int speed;
	/**
	 * The entity's sprite.
	 */
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	/**
	 * The direction that the entity is facing.
	 */
	public String direction;
	/**
	 * A counter for the entity's sprite animation.
	 */
	public int spriteCounter = 0;
	/**
	 * The number of sprites in the entity's sprite animation.
	 */
	public int spriteNum = 1;
	/**
	 * The solid area (hit box) of the entity.
	 */
	public Rectangle solidArea; // hit boxe
	/**
	 * The default of the entity's solid area.
	 */
	public int solidAreaDefaultX,  solidAreaDefaultY;
	/**
	 * Does the entity is solid.
	 */
	public boolean collisionOn = false;

}
