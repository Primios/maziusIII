package entity;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
/**
 * A player character in the game.
 *
 * @author HENRIQUES Valentin, BOUGRAIN Nathan
 */

public class Player extends Entity {
	/**
	 * The game panel for the game.
	 */
	GamePanel gp;
	/**
	 * The key handler for the game.
	 */
	KeyHandler keyH;
	/**
	 * The x-coordinate of the player's position on the screen.
	 */
	public final int screenX;
	/**
	 * The y-coordinate of the player's position on the screen.
	 */
	public final int screenY;
	/**
	 * Creates a new player with the specified game panel and key handler.
	 *
	 * @param gp The game panel for the game.
	 * @param keyH The key handler for the game.
	 */
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2; // position x du joueur sur l'écran
		screenY = gp.screenHeight/2; // position y du joueur sur l'écran


		solidArea = new Rectangle(); // hit boxe. Rect(x, y, width, height)
		solidArea.x =15;
		solidArea.y = 37;
		
		solidAreaDefaultX = solidArea.x;
 		solidAreaDefaultY = solidArea.y;
		
		solidArea.width = 18;
		solidArea.height = 10;

		
		setDefaultValues();
		getPlayerImage();
	}
	/**
	 * Sets the default values for the player.
	 */
	public void setDefaultValues() {
		worldX = gp.m.worldX;//gp.m.worldX+48*(gp.m.start_x*2); //gp.tileSize*gp.maxScreenRow; ??? gp.screenWidth 
		worldY = gp.m.worldY;//gp.m.worldY+48*(gp.m.start_y*2+1); //gp.tileSize*gp.maxScreenCol;
		speed = 4;
		direction = "down";
	}
	/**
	 * Loads the player's sprites.
	 */
	public void getPlayerImage() {
		try {
			up1=ImageIO.read(getClass().getResourceAsStream("/player/sinj_up_1.png"));
			up2=ImageIO.read(getClass().getResourceAsStream("/player/sinj_up_2.png"));
			down1=ImageIO.read(getClass().getResourceAsStream("/player/sinj_down_1.png"));
			down2=ImageIO.read(getClass().getResourceAsStream("/player/sinj_down_2.png"));
			left1=ImageIO.read(getClass().getResourceAsStream("/player/sinj_left_1.png"));
			left2=ImageIO.read(getClass().getResourceAsStream("/player/sinj_left_2.png"));
			right1=ImageIO.read(getClass().getResourceAsStream("/player/sinj_right_1.png"));
			right2=ImageIO.read(getClass().getResourceAsStream("/player/sinj_right_2.png"));

		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Updates the player's sprite and position also check the collision.
	 */
	public void update() {
		if(keyH.upPressed == true 
		|| keyH.downPressed == true 
		|| keyH.leftPressed == true 
		|| keyH.rightPressed == true){

	        if(keyH.upPressed == true){
	        	direction = "up";
	        }
	        else if(keyH.downPressed == true) {
	        	direction = "down";
	        }
	        else if(keyH.leftPressed == true) {
	        	direction = "left";
	        }
	        else if(keyH.rightPressed == true) {
	        	direction = "right";
	        }
			
			//CHECK TILE COLLISION
			collisionOn = false;
			gp.ColliCheck.checkTile(this);

			//CHECK OBJECT COLLISION
			int objIndex = gp.ColliCheck.checkObject(this, true);
			pickupObject(objIndex);

			//IF COLLISION IS FALSE PLAYER CAN MOVE
			if(collisionOn==false){
				switch(direction){
					case "up":
						// System.out.print("X: ");
						// System.out.print("Y: ");

						// gp.m.Maze_tab[((worldX/48)/2-1)][((worldY/48)/2-1)].isPlayer = false;
						// gp.m.Maze_tab[(worldX/48 - 1)][((worldY/48)/2-1)].isPlayer = true;
						worldY -= speed;
						break;
					case "down":


						// gp.m.Maze_tab[((worldX/48)/2-1)][((worldY/48)/2-1)].isPlayer = false;
						// gp.m.Maze_tab[((worldX/48)/2-1)][(worldY/48 - 1)].isPlayer = true;
						worldY += speed;
						break;
					case "left":
						 //System.out.println("Player X: "+worldY);
						 //System.out.println("Player Y: "+worldX);

						// gp.m.Maze_tab[((worldX/48)/2-1)][((worldY/48)/2-1)].isPlayer = false;
						// gp.m.Maze_tab[(worldX/48 + 1)][((worldY/48)/2-1)].isPlayer = true;
						worldX -= speed;
						break;
					case "right":
						// System.out.println("X: "+worldX/48);
						// System.out.println("Y: "+worldY/48);
						// gp.m.Maze_tab[(worldX)][((worldY/48)/2-1)].isPlayer = false;
						// gp.m.Maze_tab[(worldX)][(worldY/48 + 1)].isPlayer = true;
						//gp.m.PrintMaze5();
						worldX += speed;
						break;
					case "forfeit":
						gp.tileM.drawWay=true;
						break;
				}
			}

	        spriteCounter++;

	        if(spriteCounter > 12) {
	        	if(spriteNum == 1) {
	        		spriteNum = 2;
	        	}
	        	else if(spriteNum == 2) {
	        		spriteNum = 1;
	        	}
	        	spriteCounter = 0;
	        }
			
		}

	}
	/**
	 * Picks up an object if the player is colliding with one.
	 *
	 * @param i Index The index of the object that the player is colliding with.
	 */
	public void pickupObject(int i){
		// si l'index est a 999 cela veut dire que l on touche aucun object
		if(i != 999){
			
			// pour comportement specifique voir tuto 8, 20min
			String objectName = gp.obj[i].name;

			switch(objectName){
				case "banana":
					//gp.obj[i] = null; // permet de supr l'object quand on la toucher
					gp.gameState=3;
					break;
			}

		}

	}
	/**
	 * Draws the player onto the game panel.
	 *
	 * @param g2 The graphics context to draw with.
	 */
	public void draw(Graphics2D g2) {
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		//System.out.println("x : " + worldX/gp.tileSize + " Y : "+ worldY/gp.tileSize);

		BufferedImage image = null;
		switch(direction){
			case "up":
				if(spriteNum == 1) {
					image = up1;
				}
				if(spriteNum == 2) {
					image = up2;
				}
				break;
			case "down":
				if(spriteNum == 1) {
					image = down1;
				}
				if(spriteNum == 2) {
					image = down2;
				}				
				break;
			case "left":
				if(spriteNum == 1) {
					image = left1;
				}
				if(spriteNum == 2) {
					image = left2;
				}				
				break;
			case "right":
				if(spriteNum == 1) {
					image = right1;
				}
				if(spriteNum == 2) {
					image = right2;
				}		
				break;
			case "forfeit":
				image = down2;
				break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); 
		// set le joueur. 
		// image direction
		// position sur l'ecran
		// 
	}
}
