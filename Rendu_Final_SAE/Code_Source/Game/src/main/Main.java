package main;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main that make the game work.
 *
 * @author HENRIQUES Valentin, BOUGRAIN Nathan
 */
public class Main {
    public static void main(String[] args) throws Exception{
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Labgme");

        //Maze m = new Maze("maze1.json");
        //m.PrintMaze();
        //System.out.println(m.columns);
        Maze m=new Maze(3,3,0,0,2,2,1,0);
        GamePanel gamePanel = new GamePanel(m);

        
        //System.out.println("Main X:"+m.worldX);
        //System.out.println("Main Y:"+m.worldY);

        
        //Maze m=load_maze("maze3");
        //GamePanel gamePanel = new GamePanel(m);

        System.out.println("Main u:"+m.worldX);
        System.out.println("Main Y:"+m.worldY);

     
        
        window.add(gamePanel);
        
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread();
        
   
    }

    
    
    
}
