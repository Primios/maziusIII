package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.awt.event.KeyListener;
import tile.TileManager;
/**
 * A class for handling keyboard input in the game.
 *
 * @author HENRIQUES Valentin, BOUGRAIN Nathan
 */
public class KeyHandler implements KeyListener{ // gestionnaire touche
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, nameChecked=false, pseudoChecked=false, seedChecked=false;
    double x,y;
    File[] listOfFiles;
    String nameText="";
    String pseudoText="";
    String seedText="";
    int level =0;
    GamePanel gp;
    /**
     * Constructor for the KeyHandler class.
     *
     * @param gp The game panel.
     */
    public KeyHandler(GamePanel gp) {
    	this.gp=gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    @Override
    /**
     * Handles key pressed events to manage menu & player moves.
     *
     * @param e The key event.
     */
    public void keyPressed(KeyEvent e) {
    	int code = e.getKeyCode();
    	
    	// PLAY STATE
    	if(gp.gameState == gp.playState) {
    	     nameText="";
    	     pseudoText="";
    	     seedText="";    		
    	     level=0;
    		if(code == KeyEvent.VK_Z){
    			gp.m.step_nb++;
                upPressed = true;
            }
    		
            if(code == KeyEvent.VK_S){
    			gp.m.step_nb++;

                downPressed = true;
            }
            
            if(code == KeyEvent.VK_Q){
    			gp.m.step_nb++;

                leftPressed = true;    
            }
            
            if(code == KeyEvent.VK_D){
    			gp.m.step_nb++;

                rightPressed = true;
            }  
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
                System.out.println("enter");
            }
            
            if(code == KeyEvent.VK_ESCAPE) { // option
            	gp.gameState = gp.optionState;
            	gp.ui.commandNum = 0;
            	gp.ui.subState=0;
            }
    	}
    	
    	//LOOSE STATE
    	else if(gp.gameState==gp.looseState) {
    		if(code == KeyEvent.VK_ESCAPE){
            	gp.tileSize=48;
            	gp.gameState = gp.titleState;
        		gp.player.worldX=x;
        		gp.player.worldY=y;
            }
    	}
    	
    	//WIN STATE
    	else if(gp.gameState==gp.winState) {
    		if(code == KeyEvent.VK_ESCAPE){
            	gp.gameState=gp.titleState;
            }else if(code==KeyEvent.VK_ENTER) {
    			gp.gameState=gp.scoreState;
            }
    	}
    	
    	//SCORE STATE 
    	
    	else if(gp.gameState==gp.scoreState) {
    		if(pseudoChecked==true) {
                if(pseudoText.length()<=10) {
    	            for (char c = 'a'; c <= 'z'; c++) {
    	            	  if (code == KeyEvent.getExtendedKeyCodeForChar(c)) {
    	            		 pseudoText += c;
    	            	  }
    	            	}
                }
    	            if(code == KeyEvent.VK_BACK_SPACE && pseudoText.length()>0){
    	            	pseudoText = pseudoText.substring(0, pseudoText.length() - 1);
                }
    	            if(code == KeyEvent.VK_ESCAPE) {
    	            	System.out.println("nnnnnnn");
    	            	pseudoChecked=false;
    	            }
            }else {
            	if(code == KeyEvent.VK_Z || code==KeyEvent.VK_Q){
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) {
                    	gp.ui.commandNum = 2;
                    }
                }
                if(code == KeyEvent.VK_S || code==KeyEvent.VK_D){
                	gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2) {
                    	gp.ui.commandNum = 0;
                    }
                }
            }
    		
            if(code == KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum == 0) { // Name
       
            		pseudoChecked=true;
            	}
            }
            
            if(code==KeyEvent.VK_ENTER ) {
            	if(gp.ui.commandNum==1 && pseudoText.length()>0	) {
                	// URL de connexion à la base de données
                	String url = "jdbc:mysql://mysql-serveurmazius.alwaysdata.net:3306/serveurmazius_bdd";

                	// Identifiants de connexion
                	String username = "305082";
                	String password = "mazius3";

                	// Données à insérer
                	String name = pseudoText;
                	String level = gp.m.Difficulty;
                    String currentDate = LocalDate.now().toString();
                	int steps_nb = gp.m.step_nb;
                	int seed=gp.m.seed;
                	String GameName=gp.m.maze_name;
                	// Connexion à la base de données
                	try {
                	    Connection connection = DriverManager.getConnection(url, username, password);
                	    System.out.println("Connexion réussie !");

                	    // Préparation de la requête d'insertion
                	    String query = "INSERT INTO mazius (name, level, date, step, seed,GameName ) VALUES (?, ?, ?, ?, ?, ?)";
                	    try (PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                	        statement.setString(1, name);
                	        statement.setString(2, level);
                	        statement.setString(3, currentDate);
                	        statement.setInt(4, steps_nb);
                	        statement.setInt(5, seed);
                	        statement.setString(6, GameName);

                	        // Exécution de la requête d'insertion
                	        int rowsAffected = statement.executeUpdate();

                	        if (rowsAffected == 1) {
                	            System.out.println("Ligne insérée avec succès !");
                        	    gp.gameState = gp.SuccessfullScoreState;
                	            // Récupération de l'id généré automatiquement
                	            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                	                if (resultSet.next()) {
                	                    int id = resultSet.getInt(1);
                	                    System.out.println("Id de la nouvelle ligne : " + id);
                	                }
                	            }
                	        } else {
                	            System.err.println("Aucune ligne insérée !");
                        	    gp.gameState = gp.connexionErrState;
                	        }
                	    }

                	    connection.close();
                	    System.out.println("Connexion fermée.");
                	} catch (SQLException e1) {
                	    gp.gameState = gp.connexionErrState;
                	    gp.ui.commandNum = 4;
                	    System.err.println("Erreur lors de la connexion : " + e1.getMessage());
                	}

            	}

            		
            	if(gp.ui.commandNum==2) {
                	gp.gameState=gp.titleState;
            	}
            }
    		
    	}
    	
    	// TITLE STATE
    	else if(gp.gameState == gp.titleState) {
    		
    		
            if(code == KeyEvent.VK_Z){
            	System.out.print("nsm");
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                	gp.ui.commandNum = 2;
                }
            }
            
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                	gp.ui.commandNum = 0;
                }
            }
            
            if(code == KeyEvent.VK_ENTER) {
            	
            	if(gp.ui.commandNum == 0) { // new game
            		gp.gameState = gp.setGameState;
            		            	}
            	if(gp.ui.commandNum == 1) { // load game
            		gp.gameState = gp.loadState;
            		System.out.print("load game");
   		
            	}
            	if(gp.ui.commandNum == 2) { // quit
            		System.exit(0);
            	}
            } 
    	}
    	
    	
    	// SETTING GAME STATE
    	else if(gp.gameState == gp.setGameState) {
            if(nameChecked==true) {
                if(nameText.length()<=10) {
    	            for (char c = 'a'; c <= 'z'; c++) {
    	            	  if (code == KeyEvent.getExtendedKeyCodeForChar(c)) {
    	            	    nameText += c;
    	            	  }
    	            	}
                }
    	            if(code == KeyEvent.VK_BACK_SPACE && nameText.length()>0){
    	            	nameText = nameText.substring(0, nameText.length() - 1);
                }
    	            if(code == KeyEvent.VK_ENTER) {
    	            	System.out.println("nnnnnnn");
    	            	nameChecked=false;
    	            }
            }else if(seedChecked==true){
                if(seedText.length()<=10) {
                	for (int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) {
                	    if (code == i) {
                	        seedText += (char) (i - KeyEvent.VK_0 + '0');
                	    }
                	}

                	for (int i = KeyEvent.VK_NUMPAD0; i <= KeyEvent.VK_NUMPAD9; i++) {
                	    if (code == i) {
                	    	seedText += (char) (i - KeyEvent.VK_NUMPAD0 + '0');
                	    }
                	}



                }
    	            if(code == KeyEvent.VK_BACK_SPACE && seedText.length()>0){
    	            	seedText = seedText.substring(0, seedText.length() - 1);
                }
    	            if(code == KeyEvent.VK_ENTER) {
    	            	System.out.println("nnnnnnn");
    	            	seedChecked=false;
    	            }
            }
            
            else {
            	if(code == KeyEvent.VK_Z || code==KeyEvent.VK_Q){
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0) {
                    	gp.ui.commandNum = 6;
                    }
                }
                if(code == KeyEvent.VK_S || code==KeyEvent.VK_D){
                	gp.ui.commandNum++;
                    if(gp.ui.commandNum > 6) {
                    	gp.ui.commandNum = 0;
                    }
                }
                if(code == KeyEvent.VK_ENTER) {
                	enterPressed = true;
                	if(gp.ui.commandNum == 0) { // Name
           
                		nameChecked=true;
                	}
                	if(gp.ui.commandNum == 1) { // Name
                        
                		seedChecked=true;
                	}
                	if(gp.ui.commandNum == 2) { // Facile
                		//gp.gameState = gp.loadState;
                   		if(level != 1) {
                			level = 1;
                		}else {
                			level = 0;
                		}


                	}
                	if(gp.ui.commandNum == 3) { // Normal
                		if(level != 2) {
                			level = 2;
                		}else {
                			level = 0;
                		}

                	}
                	if(gp.ui.commandNum == 4) { // Difficile
                   		if(level != 3) {
                			level = 3;
                		}else {
                			level = 0;
                		}

                	}
                	if(gp.ui.commandNum == 5) { // ok
                		int numSeed=0;
                		if(seedText.length()>0) {
                    		numSeed=Integer.parseInt(seedText);
                		}
                		System.out.println(nameText+" "+level);
                		if(level == 0 || nameText == "") {
                			// nothing
                		}
                		else if(level == 1) { // 7
                    		try {
        						Maze m=new Maze(7,7,0,0,6,6,1,numSeed);
        						gp.m=m;
        	            		gp.m.maze_name=nameText;
        	                	gp.maxWorldCol=gp.m.columns;
        	                	gp.maxWorldRow=gp.m.rows;
        	                	gp.player.worldX=m.worldX;
        	                	gp.player.worldY=m.worldY;
        	                	gp.obj[0].worldX=m.exit_x*gp.tileSize*2+gp.tileSize;
        	                	gp.obj[0].worldY=m.exit_y*gp.tileSize*2+gp.tileSize*2; 
        	                	gp.tileM = new TileManager(gp);
        	            		gp.gameState=gp.playState;
        					} catch (Exception e1) {
        						e1.printStackTrace();
        					}
                		}
                		else if(level == 2) { // 15
                			
                			
                    		try {
        						Maze m=new Maze(15,15,0,0,14,14,2,numSeed);
        						gp.m=m;
        	            		m.maze_name=nameText;
        	                	gp.maxWorldCol=gp.m.columns;
        	                	gp.maxWorldRow=gp.m.rows;
        	                	gp.player.worldX=m.worldX;
        	                	gp.player.worldY=m.worldY;
        	                	gp.obj[0].worldX=m.exit_x*gp.tileSize*2+gp.tileSize;
        	                	gp.obj[0].worldY=m.exit_y*gp.tileSize*2+gp.tileSize*2; 
        	                	gp.tileM = new TileManager(gp);
        	            		gp.gameState=gp.playState;
        	            		//m.save_maze(nameText);
        					} catch (Exception e1) {
        						e1.printStackTrace();
        					}
                		}
                		else if(level == 3) { // 30
                    		try {
        						Maze m=new Maze(30,30,0,0,29,29,3,numSeed);
        						gp.m=m;
        	            		m.maze_name=nameText;
        	                	gp.maxWorldCol=gp.m.columns;
        	                	gp.maxWorldRow=gp.m.rows;
        	                	gp.player.worldX=m.worldX;
        	                	gp.player.worldY=m.worldY;
        	                	gp.obj[0].worldX=m.exit_x*gp.tileSize*2+gp.tileSize;
        	                	gp.obj[0].worldY=m.exit_y*gp.tileSize*2+gp.tileSize*2; 
        	                	gp.tileM = new TileManager(gp);
        	            		gp.gameState=gp.playState;
        	            		//m.save_maze(nameText);
        					} catch (Exception e1) {
        						e1.printStackTrace();
        					}
                		}
                		
                		
                	}
                	if(gp.ui.commandNum == 6) { // back
                		gp.gameState = gp.titleState;
                		nameText="";
                		
                		gp.ui.commandNum = 0;

                	}
                } 
            }

    	}
    	
    	
    	// OPTION STATE
    	else if(gp.gameState == gp.optionState) {
    		int maxCommandNum = 0;
    		switch(gp.ui.subState) {
    		case 0: maxCommandNum = 3;break;
    		case 1: maxCommandNum = 1;break;
    		}

    		if(code == KeyEvent.VK_Z){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                	gp.ui.commandNum = maxCommandNum;
                }
            }
            
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
                if(gp.ui.commandNum > maxCommandNum) {
                	gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum == 0) { // give up
            		if(gp.ui.subState==0) {
                		gp.gameState=gp.looseState;
                		gp.player.worldX=gp.maxWorldRow*gp.tileSize;
                		gp.player.worldY=gp.maxWorldCol*gp.tileSize;
                		System.out.println("giveup: "+gp.m.maze_name);
                		gp.m.winState=1;
                    	gp.m.save_maze(gp.m.maze_name);
                		gp.zoomOut();
                		gp.tileM.drawWay = true;
                		gp.gameState = gp.looseState;
            		}else if(gp.ui.subState == 1) { // ok (quit option) 
            			gp.gameState = gp.titleState;
            			gp.ui.commandNum = 0;
            			nameText="";
            			level=0;
            		}

            	} 
            	
            	if(gp.ui.commandNum == 1) { // save
            		if(gp.ui.subState == 0) { // save (main option)
                    	System.out.println("enter svae");
                		gp.m.worldX = (int) gp.player.worldX;
                    	gp.m.worldY = (int) gp.player.worldY;
                    	gp.m.save_maze(gp.m.maze_name);
            		}
            		else if(gp.ui.subState == 1) { // cancel (quit option)
            			gp.ui.subState = 0;
            			gp.ui.commandNum = 0;
            		}
            	}
            	
            	if(gp.ui.commandNum == 2) { // quit	
            		gp.ui.subState = 1;
                	gp.ui.commandNum = 0;
            	}
            	
            	if(gp.ui.commandNum == 3) { // resume game	
            		gp.gameState = gp.playState;
            	}
            }
    		
    		if(code == KeyEvent.VK_ESCAPE) {
    			gp.gameState = gp.playState;
    		}
    	}
        
        //LOAD STATE
    	else if(gp.gameState==gp.loadState) {
            if(code == KeyEvent.VK_Z){
            	gp.ui.commandNum--;
            	if(gp.ui.commandNum<0) {
                	gp.ui.commandNum=gp.ui.list_size-1;
            	}
            	System.out.println(gp.ui.commandNum);
            }
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
            	if(gp.ui.commandNum>gp.ui.list_size-1) {
                	gp.ui.commandNum=0;
            	}
            	System.out.println(gp.ui.commandNum);
            }
            
            if(code == KeyEvent.VK_Q){
            	gp.ui.commandNum=-1;
            }
            if(code == KeyEvent.VK_D){
            	if(gp.ui.commandNum==-1) {
                	gp.ui.commandNum=0;

            	}

            }
            
            if(code==KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum>=0) {
            		try {
						Maze m=new Maze(listOfFiles[gp.ui.commandNum].getName());
						if(m.winState==0) { //Check if the game is'nt win or loss before loading it
							gp.m=m;
		                	gp.maxWorldCol=gp.m.columns;
		                	gp.maxWorldRow=gp.m.rows;
		                	gp.player.worldX=m.worldX;
		                	gp.player.worldY=m.worldY;
		                	gp.obj[0].worldX=m.exit_x*gp.tileSize*2+gp.tileSize;
		                	gp.obj[0].worldY=m.exit_y*gp.tileSize*2+gp.tileSize*2; 
		                	gp.tileM=new TileManager(gp);
		            		gp.gameState=gp.playState;
						}

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            	if(gp.ui.commandNum == -1) {
            		gp.ui.commandNum=0;
            		gp.gameState = gp.titleState;
            	}
            }
        }
    	
    	//CONNEXION ERROR STATE
    	
    	else if(gp.gameState==gp.connexionErrState) {
    		if(code == KeyEvent.VK_Z){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < -2) {
                	gp.ui.commandNum = -1;
                }
            }
            
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
                if(gp.ui.commandNum > -1) {
                	gp.ui.commandNum = -2;
                }
            }
            
            if(code==KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum==-1) {
            		gp.gameState=gp.scoreState;
            	}
            	if(gp.ui.commandNum==-2) {
            		gp.gameState=gp.titleState;
            	}
            }
    	}
    	
    	//SUCCESSFULLY SENDED STATE
    	else if(gp.gameState==gp.SuccessfullScoreState) {
    		pseudoText="";
    		if(code == KeyEvent.VK_Z){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < -2) {
                	gp.ui.commandNum = -1;
                }
            }
            
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
                if(gp.ui.commandNum > -1) {
                	gp.ui.commandNum = -2;
                }
            }
            if(code==KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum==-1) {
                    String url = "http://serveurmazius.alwaysdata.net/";
                    
                    try {
                        // Création d'un objet URI à partir de l'URL
                        URI uri = new URI(url);
                        
                        // Obtention de l'objet Desktop
                        Desktop desktop = Desktop.getDesktop();
                        
                        // Vérification que l'action est prise en charge
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            // Ouverture de la page Internet
                            desktop.browse(uri);
                        } else {
                            System.out.println("Action non prise en charge");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }            	}
            	if(gp.ui.commandNum==-2) {
            		gp.gameState=gp.titleState;
            	}
            }
    	} else if(gp.gameState==gp.CguState) {
    		if(code == KeyEvent.VK_Z){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                	gp.ui.commandNum = 1;
                }
            }
            
            if(code == KeyEvent.VK_S){
            	gp.ui.commandNum++;
                if(gp.ui.commandNum > 1) {
                	gp.ui.commandNum = 0;
                }
            }
            if(code==KeyEvent.VK_ENTER) {
            	if(gp.ui.commandNum==1) {
            		System.exit(0);
            	}else if(gp.ui.commandNum==0) {
            		gp.gameState=gp.titleState;
            	}
            }
           }
    	
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_Z){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_Q){
            leftPressed = false;    
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_ENTER){
            //enterPressed = false;
        }
       
    }


}
