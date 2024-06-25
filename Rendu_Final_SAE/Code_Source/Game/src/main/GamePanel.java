package main;

import java.util.*;
import javax.swing.*;

import entity.Player;
import object.SuperObject;
import tile.TileManager;
import main.KeyHandler;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * A panel for the game that handles all of the game logic, drawing, and input.
 *
 * @author HENRIQUES Valentin,BOUGRAIN Nathan
 */

public class GamePanel extends JPanel implements Runnable {
	/**
	 * The size of a tile in its original form (before scaling).
	 */
	final int originalTileSize = 16; // 16*16 tile
	/**
	 * The scale to apply to the tile size.
	 */
    final int scale = 3;
    /**
     * The size of a tile after scaling.
     */
    public int tileSize = originalTileSize * scale; // 48*48
    /**
     * The maximum number of columns of tiles that can be displayed on the screen at once.
     */
    public final int maxScreenCol = 16;
    /**
     * The maximum number of rows of tiles that can be displayed on the screen at once.
     */
    public final int maxScreenRow = 12;
    /**
     * The width of the screen in pixels.
     */
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    /**
     * The height of the screen in pixels.
     */
    public final int screenHeight = tileSize * maxScreenRow; // 576 px
    
    //WORLD SETTINGS
    /**
     * The maximum number of columns of tiles in the world.
     */
    public int maxWorldCol;// = 7;
    /**
     * The maximum number of rows of tiles in the world.
     */
    public int maxWorldRow;// = 7;
    /**
     * The width of the world in pixels.
     */
    public int worldWidth;// = tileSize * maxWorldCol;
    /**
     * The height of the world in pixels.
     */
    public int worldHeight;// = tileSize * maxWorldRow;
    /**
     * The maze for the game.
     */
    public Maze m ;//= new Maze(maxWorldRow, maxWorldCol,0,3,maxWorldRow-1,maxWorldCol-1,1);
    //Maze m = new Maze(maxWorldRow, maxWorldCol,0,5, maxWorldRow-1, maxWorldCol-1,1);
    /**
     * How many time the game is draw by seconds;
     */
    int FPS = 60;
    // SYSYTEM
    /**
     * The object responsible for managing the tiles in the game.
     */
    public TileManager tileM;// = new TileManager(this);
    /**
     * The object responsible for handling key input.
     */
    public KeyHandler keyH;// = new KeyHandler();
    /**
     * The main game thread.
     */
    Thread gameThread;
    /**
     * The object responsible for checking collisions in the game.
     */
    public CollisionCheck ColliCheck;//  = new CollisionCheck(this);
    /**
     * The player character in the game.
     */
    public Player player;// = new Player(this,keyH);
    /**
     * The object responsible for managing the user interface of the game.
     */
    public UI ui = new UI(this);
    
    //Entity and object
    /**
     * An array of objects in the game.
     */
    public SuperObject obj[] = new SuperObject[10]; // 10 slot of abject, ne veut pas dire que l'on a 10 abject dans le jeu mais que  lon poura avior 10 object en meme temps in game a l'ecran
    /**
     * The object responsible for setting up game assets.
     */
    public AssetSetter aSetter = new AssetSetter(this);
    
    //GAME STATE
    /**
     * The state of the game when the load menu is displayed.
     */
    public final int loadState=0;
    /**
     * The state of the game when it is being played.
     */
    public final int playState=1;
    /**
     * The state of the game when the options menu is being displayed.
     */
    public final int optionState = 2;
    /**
     * The state of the game when the player has won.
     */
    public final int winState = 3;
    /**
     * The state of the game when the title screen is being displayed.
     */
    public final int titleState = 4;
    /**
     * The state of the game when the new game menu is displayed.
     */
    public final int setGameState = 6;
    /**
     * The state of the game when the player has lost.
     */
    public final int looseState=7;
    /**
     * The current state of the game.
     */
    public final int scoreState=8;
    /**
     * The state when the player choose to send his score to the website
     */
    public final int connexionErrState=9;
    /**
     * The state when a connection error occurs with the database
     * **/
    public final int SuccessfullScoreState=10;
    
    public final int CguState=11;


    

    public int gameState;
  
    /**
     * Creates a new game panel with the specified maze.
     *
     * @param _m The maze for the game.
     */
    
    public GamePanel(Maze _m){
    	this.m =_m;
    	
    	//this.m = new Maze(10,10,0,0,9,9,1);
    	this.maxWorldCol=m.columns;
    	this.maxWorldRow=m.rows;
    	this.worldWidth=tileSize * maxWorldCol;
    	this.worldHeight=tileSize * maxWorldRow;
        
        this.tileM = new TileManager(this);
        this.keyH = new KeyHandler(this);
        this.ColliCheck = new CollisionCheck(this);
        this.player = new Player(this,keyH);
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true); // with this, this GamePanel can be "focused" ti recive key input    
    }
    
    /**
     * Zooms out the view of the maze to see the entire maze.
     */
    
    public void zoomOut() {
    	int oldWorldWidth=tileSize*maxWorldCol;
		System.out.print(m.Difficulty);

    	if(m.Difficulty.equals("Easy") || m.Difficulty.equals("Normal")) {
        	tileSize+=-maxWorldCol*2;
    	}
    	if(m.Difficulty.equals("Hard")) {
        	tileSize+=-maxWorldCol*1.30;
    	}
    	int newWorldWidth=tileSize*maxWorldCol;

    	double multiplier=(double)newWorldWidth/oldWorldWidth;
    	
    	double newPlayerWorldX=player.worldX*multiplier;
    	double newPlayerWorldY=player.worldY*multiplier;
    	
    	player.worldX=newPlayerWorldX+tileSize;
    	player.worldY=newPlayerWorldY+tileSize;
    }
    /**
     * Set the game base state
     */
    public void setupGame() {
    	aSetter.setObject();

    	gameState = CguState;

    	//gameState=loadState;

    }
    
    /**
     * Start the thread
     */
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // appel run

    }
    
    /**
     * The main game loop that updates and repaints the game panel.
     */
    public void run() {
    	double drawInterval = 1000000000/FPS;
    	double delta = 0;
    	long lastTime = System.nanoTime();
    	long currentTime;
    	long timer = 0;
    	int drawCount = 0;
    	
    	
    	while(gameThread !=null) {
    		currentTime = System.nanoTime();
    		delta += (currentTime-lastTime)/drawInterval;
    		timer += (currentTime-lastTime);
    		lastTime=currentTime;
    		if(delta >= 1) {
    			update(); // 1 UPDATE : modifie les informations en rapport avec la position du personnage
    			repaint(); // 2 DRAW : dessine l'ecran avec les informations modifier
    			delta--;
    			drawCount++;
    		}
    		if(timer>=1000000000) {
    			//System.out.println("FPS: "+drawCount);
    			drawCount=0;
    			timer=0;
    		}
    	}
    }

    
    
    /**
     * Updates the game state and repaints the game panel.
     */
    public void update(){
    	if(gameState == playState) {
        	player.update();
    	}
    	//if(gameState == pauseState) {
    		//nothing
    	//}
    } 
    

    /**
     * Draws the game onto the game panel.
     *
     * @param g The graphics context to draw with.
     */
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // convert en 2D
        
        // System.out.println("gameState = "+gameState+ " setGameState  ="+ setGameState);
        
        // TITLE SCREEN
        if(gameState == titleState) {
        	ui.draw(g2);
        }
        
        // SETTING GAME SCREE 
        if(gameState == setGameState){
        	//System.out.println("147 test gp");
        	ui.draw(g2);
        }
        
        //LOAD SCREEN
        if(gameState==loadState) {
        	ui.draw(g2);
        }
        
        
        // OTHERS ( GAME SCREEN )
        else {
            // TILE
            tileM.draw(g2);
            
            //OBJECT
            for(int i = 0; i < obj.length; i++){ // parcour tout les objects et les affiche
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }
            // PLAYER
            player.draw(g2);
            
            // UI
            ui.draw(g2);
        }    
        g2.dispose(); 
    }
}
