package tile;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.Maze;
import main.Maze.Cell;
/**
 * Class for managing the tiles in the maze game. The class maintains an array of tiles, and a 2D array
 * representing the layout of the tiles in the game world. The class provides methods for drawing the
 * tiles and for loading the map.
 *
 * @author BOUGRAIN Nathan, HENRIQUES Valentin
 */
public class TileManager {
	    /** The game panel the tiles belong to. */
	    GamePanel gp;
	    /** The array of tiles. */
	    public Tile[] tile;
	    /** The 2D array representing the layout of the tiles in the game world. */
	    public int mapTileNum[][];
	    /** The maze the tiles belong to. */
	    Maze m;
	    /** A flag for whether to draw the image for each tile. */
	    private boolean drawImage;
	    /** A flag for whether to draw the way through the maze. */
	    public boolean drawWay;

	    /**
	     * Constructs a new tile manager for the given game panel.
	     *
	     * @param gp the game panel the tiles belong to
	     */
	public TileManager(GamePanel gp) {
		this.gp = gp;
		m = gp.m;
		tile = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol*2+1][gp.maxWorldRow*2+1];

		//m.PrintMaze();
		getTileImage();
		loadMap();
	}
    /**
     * Gets the images for the tiles from the resources folder.
     */
	public void getTileImage() {
		try{
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/texture_2_buie.png"));
			tile[0].collision = true;			

			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/texture_2_buie_face.png"));
			tile[1].collision = true;			

			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/texture_sol_brique.png"));
			tile[2].collision = false;

			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/texture_solution.png"));
			tile[3].collision = false;
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/black.png"));
			tile[4].collision = true;
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	  /**
     * Loads the map of tiles from the maze.
     */
	public void loadMap(){
		for (int i = 0; i < mapTileNum.length; i++) {
			for (int j = 0; j < mapTileNum[i].length; j++) {
				mapTileNum[i][j] = 2;
			}
		}

		int worldCol = 0;
		int worldRow = 0;
		ArrayList<Cell> c=m.c;

		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) { // parcour l'ecran
			if(m.columns > worldCol && m.rows > worldRow){ // si le laby a plus de colone/row que l'ecran 

				int xtab=worldRow+1+worldRow%gp.maxWorldRow;
				int ytab=worldCol+1+worldCol%gp.maxWorldCol;
				mapTileNum[xtab][ytab] = 2; // entre le type de tuile (0/1/2) dans un tableau

				if(m.Maze_tab[worldRow][worldCol].Walls[0]){//haut 
					mapTileNum[xtab-1][ytab] = 1;
					mapTileNum[xtab-1][ytab-1] = 1;
					mapTileNum[xtab-1][ytab+1] = 1;

				}
				else{
					mapTileNum[xtab-1][ytab] = 2;
				}

				if(m.Maze_tab[worldRow][worldCol].Walls[1]){//droite
					mapTileNum[xtab][ytab+1] = 0;
					mapTileNum[xtab+1][ytab+1] = 1;
					mapTileNum[xtab-1][ytab+1] = 0;
				}else{
					mapTileNum[xtab][ytab+1] = 2;
				}

				if(m.Maze_tab[worldRow][worldCol].Walls[2]){//bas
					mapTileNum[xtab+1][ytab] = 1;
					mapTileNum[xtab+1][ytab-1] = 1;
					mapTileNum[xtab+1][ytab+1] = 1;
				}else{
					mapTileNum[xtab+1][ytab] = 2;
				}

				if(m.Maze_tab[worldRow][worldCol].Walls[3]){//gauche
					mapTileNum[xtab-1][ytab-1] = 0;
					mapTileNum[xtab+1][ytab-1] = 1;
					mapTileNum[xtab][ytab-1] = 0;
				}else{
					mapTileNum[xtab][ytab-1] = 2;
				}
				
			}
			worldCol++;

			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
		for(int i=0;i<c.size();++i) {
			Cell a=c.get(i);
			mapTileNum[a.x*2+1][a.y*2+1]=3;
			if (i!=0) {
				Cell b=c.get(i-1);
				mapTileNum[a.x*2+1+(b.x-a.x)][a.y*2+1+(b.y-a.y)]=3;
			}
			mapTileNum[0][1]=3;    //mur du bas
			mapTileNum[m.exit_x*2+1+(1)][m.exit_y*2+1]=3;
		}
		
		int rows = mapTileNum.length;
		int cols = mapTileNum[0].length;

		int[][] copy = new int[rows][cols];

		for (int i = 0; i < rows; i++) {
		    for (int j = 0; j < cols; j++) {
		        copy[i][j] = mapTileNum[i][j];
		    }
		}

		int[][] result = new int[rows + 2][cols + 2];

		for (int i = 0; i < rows; i++) {
		    for (int j = 0; j < cols; j++) {
		        result[i + 1][j + 1] = copy[i][j];
		    }
		}
		
		for (int i = 0; i < result.length; i++) {
		    result[i][0] = 4;
		    result[i][result[0].length - 1] = 4;
		}
		for (int i = 0; i < result[0].length; i++) {
		    result[0][i] = 4;
		    result[result.length - 1][i] = 4;
		}
		mapTileNum=result;
		
		
	}
    /**
     * Draws the tiles that only are in the player vision and the way through the maze (if enabled).
     *
     * @param g2 the graphics object to draw with
     */
	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		while(worldCol < gp.maxWorldCol * 2 + 1+2 && worldRow < gp.maxWorldRow * 2 + 1+2){
			int tileNum = mapTileNum[worldRow][worldCol];
			if(tileNum==3 && drawWay==false){
				tileNum=2;
			}
			int worldX = worldCol*gp.tileSize;
			int worldY = worldRow*gp.tileSize;
			int screenX = (int) (worldX-gp.player.worldX+gp.player.screenX);
			int screenY = (int) (worldY-gp.player.worldY+gp.player.screenY);
				if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
						g2.drawImage(tile[tileNum].image,screenX,screenY,gp.tileSize,gp.tileSize,null);
				}
			worldCol++;

			if(worldCol == gp.maxWorldCol * 2 + 1+2){
				worldCol = 0;
				worldRow ++;
			}
		}

	}	
	public void printArray(int[][] array) {
	    for (int[] row : array) {
	        for (int element : row) {
	            System.out.print(element + " ");
	        }
	        System.out.println();
	    }
	}
}
