package main;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.google.gson.Gson;

import main.Maze.Cell;

/**
 * Class representing a maze. A maze consists of a 2D array of cells, with a start position,
 * an end position, and a difficulty level. The maze also has a creation date and a name.
 * The class provides methods for generating a maze, loading a maze from a file, and saving
 * a maze to a file.
 *
 * @author ANTONELLI Paul, HENRIQUES Valentin, BOUGRAIN Nathan
 */
public class Maze{
	/** The number of rows in the maze. */
    public int rows;
    /** The number of columns in the maze. */
    public int columns;
    /** The 2D array of cells representing the maze. */
    public Cell[][] Maze_tab;
    /** The x-coordinate of the player's current position in the console game. */
    public int PlayerX;
    /** The y-coordinate of the player's current position in the console game. */
    public int PlayerY;
    /** An array representing the 4 possible directions a player can move (left, right, up, down). */
    private int [][] side = {{-1,0},{0,1},{1,0},{0,-1}};
    /** The date the maze was created. */
    public String Creation_Date=String_Date();
    /** The difficulty level of the maze (Easy, Normal, or Hard). */
    public String Difficulty;
    /** The x-coordinate of the start position of the maze. */
    public int start_x;
    /** The y-coordinate of the start position of the maze. */
    public int start_y;
    /** The x-coordinate of the end position of the maze. */
    public int exit_x;
    /** The y-coordinate of the end position of the maze. */
    public int exit_y;
    /** The x-coordinate of the player in MaziusIII. */
    public int worldX;
    /** The y-coordinate of the player in MaziusIII. */
    public int worldY;
    /** The current win state of the maze (0 for not finished, 1 for won, 2 for lost). */
    public int winState;
    /** The name of the maze. */
    public String maze_name;
    
    public int step_nb;
    public int seed;
    
    public ArrayList<Cell> c;

    /**
     * Constructs a new maze with the given number of rows, columns, start and end positions, and difficulty level.
     *
     * @param _rows the number of rows in the maze
     * @param _columns the number of columns in the maze
     * @param in_x the x-coordinate of the start position
     * @param in_y the y-coordinate of the start position
     * @param out_x the x-coordinate of the end position
     * @param out_y the y-coordinate of the end position
     * @param _Difficulty the difficulty level of the maze (1 for Easy, 2 for Normal, 3 for Hard)
     */
    public Maze(int _rows, int _columns, int in_x, int in_y, int out_x, int out_y, int _Difficulty,int _seed){
        rows = _rows;
        columns = _columns;
        start_x = in_x;
        start_y = in_y;
        exit_x = out_x;
        exit_y = out_y;
        worldX=48+start_y*2*48+48;
        worldY=48;
        Init_Maze_tab();
        PlayerX = in_x;
        PlayerY = in_y;

        if(_Difficulty == 1){
            Difficulty = "Easy";
        }else if(_Difficulty == 2){
            Difficulty = "Normal";
        }else if(_Difficulty == 3){
            Difficulty = "Hard";
        }
        if(_seed==0) {
        	Random random = new Random();
        	this.seed = Math.abs(random.nextInt());
        }else {
        	seed=_seed;
        }
        
        Generate_Maze(start_x, start_y, exit_x, exit_y);
        c=this.smallest_way(this);
        

    }
    /**
     * Constructs a new maze by loading it from the given file.
     *
     * @param filename the name of the file to load the maze from
      * @throws Exception if there is an error reading from the file
    */
    public Maze(String filename) throws Exception {
    	load_maze(filename);
    }
    
    /**
     * Loads a maze from the given file.
     *
     * @param FileName the name of the file to load the maze from
     * @throws Exception if there is an error reading from the file
     */
    public void load_maze(String FileName) throws Exception{
        File directory = new File("res/maps");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        FileName = "res/maps/"+FileName;
        File file = new File(FileName);
        
        // Load the encrypted JSON string from the file
        String encryptedJsonString = convertFileIntoString(FileName);
        
        try {
            // Decrypt the JSON string using Jasypt
            String jsonString = decrypt(encryptedJsonString, "password");
            
            // Convert the JSON string to a maze object
            Gson gson = new Gson();
            Maze m = gson.fromJson(jsonString, Maze.class);
            
            // Set the maze object properties
            this.rows = m.rows;
            this.columns = m.columns;
            this.Maze_tab = m.Maze_tab;
            this.PlayerX = m.PlayerX;
            this.PlayerY = m.PlayerY;
            this.Creation_Date = m.Creation_Date;
            this.Difficulty = m.Difficulty;
            this.start_x = m.start_x;
            this.start_y = m.start_y;
            this.maze_name = m.maze_name;
            this.winState = m.winState;
            this.exit_x = m.exit_x;
            this.exit_y = m.exit_y;
            this.worldX = m.worldX;
            this.worldY = m.worldY;
            this.step_nb = m.step_nb;
            this.c = m.c;
            this.seed = m.seed;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String decrypt(String encryptedJsonString, String password) throws Exception {
        // Create a Jasypt encryptor with the specified password
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        
        // Decrypt the JSON string using Jasypt
        String jsonString = encryptor.decrypt(encryptedJsonString);
        
        return jsonString;
    }
    
    public static String convertFileIntoString(String file)throws Exception  
    {  
        String result;  
        result = new String(Files.readAllBytes(Paths.get(file)));  
        return result;  
    }
    /**
     * Returns the current date as a formatted string.
     *
     * @return the current date as a formatted string
     */
    public String String_Date(){
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        String formattedString = localDate.format(formatter);
        return formattedString;
    }

    public class Cell{
        public boolean[] Walls; // les quatre mur
        public int x,y; // position
        public boolean isPlayer;

        public Cell(int _x, int _y, boolean[] _Walls, boolean _isPlayer){
            x = _x;
            y = _y;
            Walls = _Walls;
            isPlayer = _isPlayer;
        }

        public Cell(Cell a){
            this.x=a.x;
            this.y=a.y;
            this.Walls=a.Walls;
            this.isPlayer=a.isPlayer;
        }
    }
    /**
     * Initializes the 2D array of cells for the maze.
     */
    public void Init_Maze_tab(){ // crée un tableau de case (Maze_tab)
        Maze_tab = new Cell[rows][columns];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                boolean [] tab={true,true,true,true};
                boolean player = false;
                Maze_tab[x][y] = new Cell(x, y, tab, player); //changer en true par defaut
            }
        }
    }
    /**
     * Generates a new maze randomly with the given start and end positions.
     *
     * @param x the x-coordinate of the start position
     * @param y the y-coordinate of the start position
     * @param sortie_x the x-coordinate of the end position
     * @param sortie_y the y-coordinate of the end position
     */
    public void Generate_Maze(int x,int y,int sortie_x, int sortie_y){ // crée aleatoirement le labyrhinte ( remplie Maze_tab)
        int current_x = x, current_y = y;

        ArrayList<Cell> list = new ArrayList<Cell>();
        
        Random r=new Random(this.seed);
        
        outerloop:while(true){ 
            boolean[] b=can_move(this, current_x, current_y);
            if(b[0]||b[1]||b[2]||b[3]){
                list.add(this.Maze_tab[current_x][current_y]);
                
                int direction = r.nextInt(4);
                // direction = le dernier random ajoute a list ( la direction en cour )

                for(int i = direction; i < direction + 4; ++i){     //try to move in the chousen direction 
                                                                    //if ist imposible chouse the next direction
                    int current_direction = i % 4;                  //the direction are between 0 and 3
                    
                    if (b[current_direction]){
                        remove_wall(this, current_x,current_y, current_direction);
                        current_x += this.side[current_direction][0];
                        current_y += this.side[current_direction][1];
                        
                        continue outerloop;
                    }
                }
            }
            else if (!list.isEmpty()){
                current_x=list.get(list.size()-1).x;
                current_y=list.get(list.size()-1).y;
                list.remove(list.size()-1);
            }
            else{
                break outerloop;
            }
        }
        Maze_tab[x][y].Walls[0] = false; //once the generation is finish the labytinth open
        Maze_tab[sortie_x][sortie_y].Walls[2] = false; 
        
        int taille=rows<columns?rows:columns;
        for (int i=0;i<taille;i++){
            remove_wall(this,r.nextInt(rows-1),r.nextInt(columns-1),r.nextInt(3));
        }
    }

    public boolean existe(Maze m, int x, int y) {
        return ((0<=x&&x<m.rows)&&(0<=y&&y<m.columns));
    }

    boolean has_been_visited(Maze m,int x,int y){
        if (existe(m, x, y)){
            boolean []w=m.Maze_tab[x][y].Walls;
            boolean b=w[0]==true && w[1]==true;
            return (!(b && w[2]==true && w[3]==true));
        }
        else 
            return false;
    }

    boolean[] can_move(Maze m, int x, int y){
        boolean[] b={false,false,false,false};
        for(int i=0;i<4;++i){
            if(existe(m,x+m.side[i][0],y+m.side[i][1]))
                b[i]=!has_been_visited(m, x+m.side[i][0],y+m.side[i][1]);
        }
        return b;
    }

    void remove_wall(Maze m, int x, int y, int direction){
        if (existe(m, x, y)){
            if (existe(m, x + m.side[direction][0],y + m.side[direction][1])){
                m.Maze_tab[x][y].Walls[direction] = false;
                m.Maze_tab[x + m.side[direction][0]][y + m.side[direction][1]].Walls[(direction + 2)%4] = false;
            }
        }
    }

    ArrayList<Cell> smallest_way(Maze m, int x, int y, ArrayList<Cell> c){
        if (c.size() == 0)
            c.add(m.Maze_tab[x][y]);
        
        Cell t = m.Maze_tab[exit_x][exit_y];
        if (c.get(c.size()-1)==t){
            return c;
        }
        boolean[] b = new boolean[4];
        for (int i=0;i<4;++i)
            b[i]=m.Maze_tab[x][y].Walls[i];
        
        

        Cell a=m.Maze_tab[x][y];
        
        ArrayList<Cell> s_0=new ArrayList<Cell>();
        ArrayList<Cell> s_1=new ArrayList<Cell>();
        ArrayList<Cell> s_2=new ArrayList<Cell>();
        ArrayList<Cell> s_3=new ArrayList<Cell>();
        
        if (0<a.x&&!b[0]&&!c.contains(m.Maze_tab[a.x+m.side[0][0]][a.y+m.side[0][1]])){
            s_0.addAll(c);
            s_0.add(m.Maze_tab[a.x+m.side[0][0]][a.y+m.side[0][1]]);
            s_0=smallest_way(m,x+m.side[0][0],y+m.side[0][1],s_0);
        }
        if (y<m.columns-1&&!b[1]&&!c.contains(m.Maze_tab[a.x+m.side[1][0]][a.y+m.side[1][1]])){
            s_1.addAll(c);
            s_1.add(m.Maze_tab[a.x+m.side[1][0]][a.y+m.side[1][1]]);
            s_1=smallest_way(m,x+m.side[1][0],y+m.side[1][1],s_1);
        }
        if (x<m.rows-1&&!b[2]&&!c.contains(m.Maze_tab[a.x+m.side[2][0]][a.y+m.side[2][1]])){
            s_2.addAll(c);
            s_2.add(m.Maze_tab[a.x+m.side[2][0]][a.y+m.side[2][1]]);
            s_2=smallest_way(m,x+m.side[2][0],y+m.side[2][1],s_2);
        }
        if (0<y&&!b[3]&&!c.contains(m.Maze_tab[a.x+m.side[3][0]][a.y+m.side[3][1]])){
            s_3.addAll(c);
            s_3.add(m.Maze_tab[a.x+m.side[3][0]][a.y+m.side[3][1]]);
            s_3=smallest_way(m,x+m.side[3][0],y+m.side[3][1],s_3);
        }

        int min=2147483647;//max int
        
        if (s_0.size()!=0){
            min=s_0.size();
            
        }if (s_1.size()!=0){
            
            min=min<s_1.size()?min:s_1.size();
        }if (s_2.size()!=0){
            
            min=min<s_2.size()?min:s_2.size();
        }if (s_3.size()!=0){
            
            min=min<s_3.size()?min:s_3.size();
        }
        
        int[]len={s_0.size(),s_1.size(),s_2.size(),s_3.size()};

        if (min==len[0]){
            c.clear();
            c.addAll(s_0);
        }else if(min==len[1]){
            c.clear();
            c.addAll(s_1);
        }else if(min==len[2]){
            c.clear();
            c.addAll(s_2);
        }else if(min==len[3]){
            c.clear();
            c.addAll(s_3);
        }

        if (c.get(c.size()-1)!=t)
            c.clear();
        return c;
    }
    
    /**
     *Look for the smallest solution way 
     *@param m the Maze we're looking the solution for
     *@return an array filled with all the solution cell
     */
    public ArrayList<Cell> smallest_way(Maze m){ //permet de metre des valeur par defaut et renvoie tab de solutions
        return smallest_way(m,PlayerX,PlayerY,new ArrayList<Cell>());
    }

    public void PrintMaze(){ // affiche le labyrinth dans le terminale
        for(int i = 0; i < columns; i++){
            System.out.print(" ");
            if(Maze_tab[0][i].Walls[0] == true){
                System.out.print("_");
            }
            else{
                System.out.print(" "); 
            }           
        }System.out.println("");

        for(int x = 0; x < rows; x++){
            for(int y = 0; y < columns; y++){
                if(Maze_tab[x][y].Walls[3] == true){
                    System.out.print("|");
                }
                else{
                    System.out.print(" ");
                }
                if(Maze_tab[x][y].Walls[2] == true){
                    System.out.print("_");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }
        System.out.println("");
    }

    public void PrintMaze5(){
        for(int i = 0; i < columns; i++){
            System.out.print(" ");
            if(Maze_tab[0][i].Walls[0] == true){
                System.out.print("___");
            }
            else{
                System.out.print("   "); 
            }           
        }System.out.println("");

        for(int x = 0; x < rows; x++){
            for(int k = 0; k < 3; k ++){
                for(int y = 0; y < columns; y++){
                    
                    if(Maze_tab[x][y].Walls[3] == true){
                        System.out.print("|");
                    }
                    else{
                        System.out.print(" ");
                    }

                    if(Maze_tab[x][y].isPlayer == true && k == 1 ){
                        System.out.print(" O ");
                    }

                    else if(Maze_tab[x][y].Walls[2] == true && k == 2){
                        System.out.print("___");
                    }
                    else{
                        System.out.print("   ");
                    }

                }System.out.println("|");
            }
        }
        System.out.println("");
    }
    /**
     * Saves the current maze to the given file.
     *
     * @param FileName the name of the file to save the maze to
     */
    public void save_maze(String FileName){
        File directory = new File("res/maps");
        if (!directory.exists()) {
            directory.mkdirs();
        };
        this.PrintMaze();
        File file = new File("res/maps/"+FileName+".json");
        System.out.println("res/maps/"+FileName+".json");
        
        try {
            // Convert the maze object to a JSON string
            String jsonString = new Gson().toJson(this);
            
            // Encrypt the JSON string using Jasypt
            String encryptedJsonString = encrypt(jsonString, "password");
            
            // Save the encrypted JSON string to the file
            Writer writer = new FileWriter(file);
            writer.write(encryptedJsonString);
            writer.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String encrypt(String jsonString, String password) throws Exception {
        // Create a Jasypt encryptor with the specified password
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        
        // Encrypt the JSON string using Jasypt
        String encryptedJsonString = encryptor.encrypt(jsonString);
        
        return encryptedJsonString;
    }

    
}
