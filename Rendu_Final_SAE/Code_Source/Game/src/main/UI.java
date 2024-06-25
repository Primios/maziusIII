package main;

import java.awt.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JTextField;

import com.google.gson.Gson;

/**
 * The UI class is responsible for handling the visual aspects of the game, such as displaying the title screen and 
 * the win/lose screens. It also handles displaying messages to the player.
 * 
 * @author BOUGRAIN Nathan, HENRIQUES Valentin
 *
 */
public class UI {
	/**
     * A reference to the GamePanel object that this UI belongs to
     */
    GamePanel gp;
    /**
     * A Graphics2D object for drawing on the screen
     */
    Graphics2D g2;
    /**
     * A Font object for displaying text in Arial with a size of 40
     */
    Font arial_40;
    /**
     * A Font object for displaying pixelated text
     */
    Font pixelFont;
    /**
     * A boolean indicating whether a message is currently being displayed
     */
    public boolean messageOn = false;
    /**
     * The message currently being displayed
     */
    public String message = "";
    /**
     * A counter for the duration of the displayed message
     */
    int messageCounter = 0;
    /**
     * The current command number used to place the cursor and know on what it's placed
     */
    public int commandNum=0;
    /**
     * The size of the game list
     */
    public int list_size=0;
    /**
     * The substate of option menu
     */
    int subState = 0;

    /**
     * Constructs a new UI object with a reference to the given GamePanel
     * @param gp the GamePanel object that this UI belongs to
     */

    public UI(GamePanel gp){
        this.gp = gp;
        //arial_40 = new Font("Arial", Font.PLAIN, 40);
        
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");

			pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * Shows the given message on the screen
     * @param msg the message to be displayed
     */
    public void showMessage(String msg){
        message = msg;
        messageOn = true;
    }
    /**
     * Draws the various screens and elements of the game, depending on the current game state.
     * @param g2 the Graphics2D object to use for drawing
     */
    public void draw(Graphics2D g2){
    	this.g2 = g2;
    	
    	g2.setFont(pixelFont);
    	g2.setColor(Color.white);

    	
    	// LOAD STATE
    	if(gp.gameState == gp.loadState) {
    		drawLoadScreen();
    	}
    	
    	// SETTING GAME STATE
    	if(gp.gameState == gp.setGameState) {
    		drawSetGameScreen();
    	}
    	
    	// TITLE STATE
    	if(gp.gameState == gp.titleState) {  		
    		drawTitleScreen();
    	}
    	
        if(gp.gameState==gp.looseState) {
        	drawLoseScreen();
        }
    	
    	// PLAY STATE
//    	if(gp.gameState == gp.playState) {
//    		drawLoseScreen();
//    	}
    	
    	// WIN STATE
    	if(gp.gameState == gp.winState) {
    		drawWinScreen();
    	}
    	
    	// SCORE STATE
    	
    	if(gp.gameState == gp.scoreState) {
    		drawScoreScreen();
    	}
    	
    	// OPTION STATE
    	if(gp.gameState == gp.optionState) {
    		drawOptionScreen();
    	}
    	
    	// CONNEXION ERROR STATE
    	if(gp.gameState==gp.connexionErrState) {
    		drawConnexionErrorScreen();
    	}
    	
    	if(gp.gameState==gp.SuccessfullScoreState) {
    		drawSuccessfullySendedScore();
    	}
    	
    	if(gp.gameState==gp.CguState){
    		drawCguScreen();    		
    	}
    }
    
    public void drawCguScreen() {
    	// BACKGROUND
    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	// HEADER
    	g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
    	
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
        String text="CGU";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*2;
        
        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);
    	
    	// MAIN COLOR
    	g2.setColor(Color.white);
    	g2.drawString(text, x, y);
    	
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,20F));    	
    	
    	text = "En acceptant ces conditions, vous reconnaissez que le jeu peut entraîner";
    	x = getXforCenteredText(text);
    	y += gp.tileSize*2;
    	g2.drawString(text, x, y);
    	
    	text = "une addiction et vous vous engagez à jouer de manière responsable.";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
    	text = "Nous nous réservons le droit de restreindre l'accès au jeu si nous avons des";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
    	text = "raisons de croire que vous ne respectez pas les termes et conditions d'utilisation.";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);

    	text = "Vous acceptez de ne pas utiliser de logiciels tiers pour obtenir un avantage dans le jeu,";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
    	text = "ni de pirater le jeu ou d'utiliser des astuces pour obtenir des avantages injustes.";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
    	text = "Nous ne sommes pas responsables des dommages ou pertes subis en raison de l'utilisation";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
    	text = "du jeu ou de l'incapacité à y accéder. Vous utilisez le jeu à vos propres risques et périls.";
    	x = getXforCenteredText(text);
    	y += gp.tileSize/2;
    	g2.drawString(text, x, y);
    	
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));    	
    	text ="J'ai pris connaissance et j'accepte les CGU";
    	x = getXforCenteredText(text);
    	y += gp.tileSize;
    	if(commandNum == 0) {
    		g2.drawString(">",x-25,y);
    	}
    	g2.drawString(text, x, y);
    	
    	text ="Je refuse les CGU";
    	x = getXforCenteredText(text);
    	y += gp.tileSize*2;
    	if(commandNum == 1) {
    		g2.drawString(">",x-25,y);
    	}
    	g2.drawString(text, x, y);
    }
    
    public void drawLoseScreen() {
    	gp.m.winState=2;
    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
    	g2.setColor(Color.red);
    	String text="Loose...";
    	int x=getXforCenteredText(text);
    	int y=gp.screenHeight/2;
    	g2.drawString(text, x, y);  
    	
    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,40F));
    	g2.setColor(Color.white);
    	text="Press ESC to go back to title screen";
    	x=getXforCenteredText(text);
    	y=gp.screenHeight/2+gp.tileSize*3;
    	if(gp.m.Difficulty.equals("Hard")) {
        	y=gp.screenHeight/2+gp.tileSize*5;
    	}
    	g2.drawString(text, x, y);
    }
    
    public void drawOptionScreen() {
    	// modification text
    	g2.setColor(Color.white);
    	g2.setFont(g2.getFont().deriveFont(35F));
    	
    	// sub window
    	int frameX = gp.tileSize*4;
    	int frameY = gp.tileSize;
    	int frameWidth = gp.tileSize*8;
    	int frameHeight = gp.tileSize*8;
    	drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    	
    	switch(subState) {
       	case 0: option_main( frameX,  frameY); break;
    	case 1: option_quit(frameX, frameY);break;
    	case 2: break;
    	}
    	
    	
    }
    
    public void option_main(int frameX, int frameY){
    	//System.out.print("option_top test");
    	int textX;
    	int textY;
    	
    	// TILE
    	String text = "Option";
    	textX = getXforCenteredText(text);
    	textY = frameY + gp.tileSize+20;
    	g2.drawString(text, textX, textY);
    	
    	// give up
    	textX = frameX + gp.tileSize;
    	textY += gp.tileSize+40;
    	g2.drawString("Give up", textX, textY);
    	if(commandNum == 0) {
    		g2.drawString(">",textX-25,textY);
    	}
    	
    	// Save
    	textY += gp.tileSize;
    	g2.drawString("Save", textX, textY);
    	if(commandNum == 1) {
    		g2.drawString(">",textX-25,textY);
    	}
    	
    	// quit
    	textY += gp.tileSize;
    	g2.drawString("Quit", textX, textY);
    	if(commandNum == 2) {
    		g2.drawString(">",textX-25,textY);
    	}
    	
    	// resume game
    	textY += gp.tileSize*2;
    	g2.drawString("Resume game", textX, textY);
    	if(commandNum == 3) {
    		g2.drawString(">",textX-25,textY);
    	}
    	
    	
    }
    
    public void option_quit(int frameX, int frameY) {
    	g2.setFont(g2.getFont().deriveFont(30F));
    	int textX;
    	int textY;
    	// TILE
    	String text = "If you have not saved all your"; 
    	textX = getXforCenteredText(text);
    	textY = frameY + gp.tileSize*2;
    	g2.drawString(text, textX, textY);
    	text = "progress will be deleted !"; 
    	textX = getXforCenteredText(text);
    	textY += gp.tileSize;
    	g2.drawString(text, textX, textY);
    	// OK
    	text = "OK";
    	textX = getXforCenteredText(text)-17;
    	textY += gp.tileSize*2;
    	g2.drawString(text, textX, textY);
    	if(commandNum == 0) {
    		g2.drawString(">",textX-25,textY);
    	}
    	
    	// Cancel
    	text = "Cancel";
    	textX = getXforCenteredText(text);
    	textY += gp.tileSize;
    	g2.drawString(text, textX, textY);
    	if(commandNum == 1) {
    		g2.drawString(">",textX-25,textY);

  
    	}


    }
    
    public void drawSetGameScreen() {	
    	// BACKGROUND
    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	// HEADER
    	g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
    	
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
        String text="New Game";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*2;
        
        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);
    	
    	// MAIN COLOR
    	g2.setColor(Color.white);
    	g2.drawString(text, x, y);
    	
    	// MENU
    	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
    	
    	text = "Name :";
    	x = getXforCenteredText(text)-250;
    	y += gp.tileSize*3;
    	g2.drawString(text, x, y);
    	if(commandNum == 0) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	text = gp.keyH.nameText;
    	x = getXforCenteredText(text);
    	g2.drawString(text, x, y);
    	
    	//seed input
    	text = "Seed :";
    	x = getXforCenteredText(text)-250;
    	y += gp.tileSize*1.5;
    	g2.drawString(text, x, y);
    	if(commandNum == 1) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	text = gp.keyH.seedText;
    	x = getXforCenteredText(text);
    	g2.drawString(text, x, y);
    	
    	// EZ
    	text = "Easy  ";
    	x = getXforCenteredText(text)-255;
    	y += gp.tileSize*2;
    	g2.drawString(text, x, y);
    	if(commandNum == 2) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	//  box
    	g2.setStroke(new BasicStroke(3));
    	g2.drawRect(x+100, y-25, 24, 24);
    	
    	
    	// MEDIUM
    	text = "Normal";
    	x = getXforCenteredText(text);
    	g2.drawString(text, x, y);
    	if(commandNum == 3) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	 //  box
    	g2.setStroke(new BasicStroke(3));
    	g2.drawRect(x+140, y-25, 24, 24);

    	
    	// HARD
    	text = "Hard";
    	x = getXforCenteredText(text)+ 250;
    	g2.drawString(text, x , y);   	
    	if(commandNum == 4) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	// box
    	g2.setStroke(new BasicStroke(3));
    	g2.drawRect(x+100, y-25, 24, 24);

    	// Check box
    	switch(gp.keyH.level) {
			case 1: g2.fillRect(184, y-25, 24, 24); break;
			case 2: g2.fillRect(184+287, y-25, 24, 24); break;
			case 3: g2.fillRect(x+100, y-25, 24, 24); break;	
    	}
    	
    	
    	text = "OK";
    	x = getXforCenteredText(text)+300;
    	y += gp.tileSize*2;
    	g2.drawString(text, x, y);
    	if(commandNum == 5) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}

    	text = "Cancel";
    	x = getXforCenteredText(text)-250;
    	g2.drawString(text, x, y);
    	if(commandNum == 6) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	gp.keyH.enterPressed = false;

    }
    
    public void drawTitleScreen() {
    	
    	// background
    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	// TITLE NAME
    	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
    	String text = "MAZIUS III ";
    	int x = getXforCenteredText(text);
    	int y = gp.tileSize*3;
    	
    	//SHADOW
    	g2.setColor(Color.gray);
    	g2.drawString(text, x+5, y+5);
    	
    	// MAIN COLOR
    	g2.setColor(Color.white);
    	g2.drawString(text, x, y);
    	
    	// IMAGE
    	x = gp.screenWidth/2 - (gp.tileSize+30);
    	y += gp.tileSize;
    	
    	g2.drawImage(gp.player.down1, x, y, gp.tileSize*3, gp.tileSize*3,null);
    	
    	
    	
    	// MENU
    	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
    	
    	text = "NEW GAME";
    	x = getXforCenteredText(text);
    	y += gp.tileSize*5;
    	g2.drawString(text, x, y);
    	if(commandNum == 0) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	
    	text = "LOAD GAME";
    	x = getXforCenteredText(text);
    	y += gp.tileSize;
    	g2.drawString(text, x, y);
    	if(commandNum == 1) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	
    	text = "QUIT";
    	x = getXforCenteredText(text);
    	y += gp.tileSize;
    	g2.drawString(text, x, y);
    	if(commandNum == 2) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	
    }
    
    public void drawWinScreen() {
    	gp.m.winState=2;
    	gp.m.save_maze(gp.m.maze_name);

    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
    	g2.setColor(Color.yellow);
    	String text="You win !";
    	int x=getXforCenteredText(text);
    	int y=gp.screenHeight/2;
    	g2.drawString(text, x, y);    
    	
    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,30F));
    	g2.setColor(Color.white);
    	text="Press ESC to go back to title screen";
    	x=getXforCenteredText(text);
    	y=gp.screenHeight/2+gp.tileSize*2;
    	g2.drawString(text, x, y);
    	
    	g2.setFont(g2.getFont().deriveFont(Font.PLAIN,30F));
    	g2.setColor(Color.white);
    	text="or ENTER to share your maze !";
    	x=getXforCenteredText(text);
    	y=gp.screenHeight/2+gp.tileSize*3;
    	g2.drawString(text, x, y);
    }
    
    public void drawScoreScreen() {
    	// BACKGROUND
    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	// HEADER
    	g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
    	
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
        String text="Send your score !";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*2;
        
        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);
    	
    	// MAIN COLOR
    	g2.setColor(Color.white);
    	g2.drawString(text, x, y);
    	
    	// MENU
    	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
    	
    	text = "Pseudo :";
    	x = getXforCenteredText(text)-250;
    	y += gp.tileSize*3;
    	g2.drawString(text, x, y);
    	if(commandNum == 0) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	text = gp.keyH.pseudoText;
    	x = getXforCenteredText(text);
    	g2.drawString(text, x, y);
    	
    	text = "Nombres de pas : " +gp.m.step_nb;
    	x = getXforCenteredText(text)-150;
    	y += gp.tileSize*1.5;
    	g2.drawString(text, x, y);
    	
    	text = "Seed de la partie : " +gp.m.seed;
    	x = getXforCenteredText(text)-150;
    	y += gp.tileSize*1.25;
    	g2.drawString(text, x, y);
    	
    	text = "OK";
    	x = getXforCenteredText(text)+300;
    	y += gp.tileSize*4;
    	g2.drawString(text, x, y);
    	if(commandNum == 1) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}

    	text = "Cancel";
    	x = getXforCenteredText(text)-250;
    	g2.drawString(text, x, y);
    	if(commandNum == 2) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	gp.keyH.enterPressed = false;
    }
    
    public void drawLoadScreen() {
        

    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
    	
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
        String text="Load a Maze !";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*2;
        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);
        //MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text,x,y);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
        
        text = "Cancel";
        x=gp.tileSize;
        y=gp.screenHeight/2;
        g2.drawString(text,x,y);
        if(commandNum==-1) {
            g2.drawString(">",x-gp.tileSize+15,y);
        }
        y=gp.tileSize*2;
        File directory = new File("res/maps");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File folder = new File("res/maps/");
        File[] listOfFiles = folder.listFiles();
        gp.keyH.listOfFiles=listOfFiles;
        list_size=listOfFiles.length;
        String[] listOfGames=new String[listOfFiles.length];
        for(int i=0;i<listOfFiles.length;i++){
            String filename = listOfFiles[i].getName();
            Maze m;
			try {
				m = new Maze(filename);
	            text=listOfFiles[i].getName();
	            int index = filename.lastIndexOf(".");
	            String state="";
	            if(m.winState==0) {
	            	state="";
	            }
	            if(m.winState==1) {
	            	state="L";
	            }
	            if(m.winState==2) {
	            	state="W";
	            }
	            listOfGames[i] = state+"  "+filename.substring(0, index)+"  "+m.Difficulty+"  "+m.Creation_Date;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        int a;
        if (listOfGames.length>=8){
        	a=8;
        }else {
        	a=listOfGames.length;
        }
		String[] currentGames=new String[a];
		for(int k=0;k+commandNum<currentGames.length+commandNum;k++) {
			if(commandNum>=8) {
				currentGames[k]=listOfGames[k+commandNum-7];
			}else {
				currentGames[k]=listOfGames[k];
			}
		}
        for(int i=0;i<currentGames.length;i++) {
                    x=getXforCenteredText(currentGames[i]);
                    if(i==0) {
                        y+=gp.tileSize*2;
                    }
                    else {
                        y+=gp.tileSize;    
                    }
                    g2.drawString(currentGames[i], x, y);
                    if(commandNum>=8 && i==0) {
                        g2.drawString(">",x-gp.tileSize,y+gp.tileSize*7);
                    }else {
                        for(int j=0;j<currentGames.length;j++) {
                        	if(commandNum==i && j==0) {
                                g2.drawString(">",x-gp.tileSize,y);
                            }
                        }
                    }

 
                }
    }
      
    public void drawSubWindow(int x, int y, int width, int height) {
    	Color c = new Color(0,0,0,215);
    	g2.setColor(c);
    	g2.fillRoundRect(x, y, width, height, 35, 35);
    	
    	c = new Color(255,255,255);
    	g2.setColor(c);
    	g2.setStroke(new BasicStroke(5));
    	g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    public void drawConnexionErrorScreen() {
    	// BACKGROUND
    	g2.setColor( new Color(0,0,0)); // color black
    	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
    	
    	// HEADER
    	g2.setColor(new Color(70,120,80));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
    	
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
        String text="Send your score !";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*2;
        
        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);
    	
    	// MAIN COLOR
    	g2.setColor(Color.white);
    	g2.drawString(text, x, y);
    	
    	// MENU
    	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
    	
    	text = "Pseudo :";
    	x = getXforCenteredText(text)-250;
    	y += gp.tileSize*3;
    	g2.drawString(text, x, y);
    	if(commandNum == 0) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	text = gp.keyH.pseudoText;
    	x = getXforCenteredText(text);
    	g2.drawString(text, x, y);
    	
    	
    	text = "OK";
    	x = getXforCenteredText(text)+300;
    	y += gp.tileSize*4;
    	g2.drawString(text, x, y);
    	if(commandNum == 1) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}

    	text = "Cancel";
    	x = getXforCenteredText(text)-250;
    	g2.drawString(text, x, y);
    	if(commandNum == 2) {
    		g2.drawString(">", x-gp.tileSize, y);
    	}
    	gp.keyH.enterPressed = false;
    	// modification text
    	g2.setColor(Color.white);
    	g2.setFont(g2.getFont().deriveFont(35F));
    	
    	// sub window
    	int frameX = gp.tileSize*4;
    	int frameY = gp.tileSize;
    	int frameWidth = gp.tileSize*8;
    	int frameHeight = gp.tileSize*8;
    	drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    	
    	//System.out.print("option_top test");
    	int textX;
    	int textY;
    	
    	// TILE
    	text = "Error";
    	textX = getXforCenteredText(text);
    	textY = frameY + gp.tileSize+20;
    	g2.drawString(text, textX, textY);
    	
    	// give up
    	textX = frameX + gp.tileSize;
    	textY += gp.tileSize+40;
    	g2.drawString("An error occured", textX, textY);
    	
    	// Save
    	textY += gp.tileSize/1.5;
    	g2.drawString("please check your", textX, textY);
    	
    	textY += gp.tileSize/1.5;
    	g2.drawString("connexion", textX, textY);
    	
    	textY += gp.tileSize*1.25;
    	g2.drawString("Retry", textX, textY);
    	if(commandNum == -1) {
    		g2.drawString(">",textX-25,textY);
    	}
    	textY += gp.tileSize*1.5;
    	g2.drawString("Back to title screen", textX, textY);
    	if(commandNum == -2) {
    		g2.drawString(">",textX-25,textY);
    	}
    }
    
    public void drawSuccessfullySendedScore() {
        	// BACKGROUND
        	g2.setColor( new Color(0,0,0)); // color black
        	g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
        	
        	// HEADER
        	g2.setColor(new Color(70,120,80));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight-gp.tileSize*9);
        	
            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,85F));
            String text="Send your score !";
            int x=getXforCenteredText(text);
            int y=gp.tileSize*2;
            
            //SHADOW
            g2.setColor(Color.black);
            g2.drawString(text,x+5,y+5);
        	
        	// MAIN COLOR
        	g2.setColor(Color.white);
        	g2.drawString(text, x, y);
        	
        	// MENU
        	g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        	
        	text = "Pseudo :";
        	x = getXforCenteredText(text)-250;
        	y += gp.tileSize*3;
        	g2.drawString(text, x, y);
        	if(commandNum == 0) {
        		g2.drawString(">", x-gp.tileSize, y);
        	}
        	text = gp.keyH.pseudoText;
        	x = getXforCenteredText(text);
        	g2.drawString(text, x, y);
        	
        	
        	text = "OK";
        	x = getXforCenteredText(text)+300;
        	y += gp.tileSize*4;
        	g2.drawString(text, x, y);
        	if(commandNum == 1) {
        		g2.drawString(">", x-gp.tileSize, y);
        	}

        	text = "Cancel";
        	x = getXforCenteredText(text)-250;
        	g2.drawString(text, x, y);
        	if(commandNum == 2) {
        		g2.drawString(">", x-gp.tileSize, y);
        	}
        	gp.keyH.enterPressed = false;
        	// modification text
        	g2.setColor(Color.white);
        	g2.setFont(g2.getFont().deriveFont(35F));
        	
        	// sub window
        	int frameX = gp.tileSize*4;
        	int frameY = gp.tileSize;
        	int frameWidth = gp.tileSize*8;
        	int frameHeight = gp.tileSize*8;
        	drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        	
        	//System.out.print("option_top test");
        	int textX;
        	int textY;
        	
        	// TILE
        	text = "Success !";
        	textX = getXforCenteredText(text);
        	textY = frameY + gp.tileSize+20;
        	g2.drawString(text, textX, textY);
        	
        	// give up
        	textX = frameX + gp.tileSize;
        	textY += gp.tileSize+40;
        	g2.drawString("Your score was ", textX, textY);
        	
        	textY += gp.tileSize/1.5;
        	g2.drawString("successfully", textX, textY);
        	// Save
        	textY += gp.tileSize/1.5;
        	g2.drawString("added to our website", textX, textY);
        	
        	textY += gp.tileSize*1.25;
        	g2.drawString("Check it out !", textX, textY);
        	if(commandNum == -1) {
        		g2.drawString(">",textX-25,textY);
        	}
        	textY += gp.tileSize*1.5;
        	g2.drawString("Back to title screen", textX, textY);
        	if(commandNum == -2) {
        		g2.drawString(">",textX-25,textY);
        	}
        }
    
    
    /**
     * Calculate the where should be placed a text to be in the middle of the window
     * @param text that is going to be draw
     * @return the x-coordinate
     */
    public int getXforCenteredText(String text) {
    	int length=(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
    	int x=gp.screenWidth/2-length/2;
    	return x;
    }

}
