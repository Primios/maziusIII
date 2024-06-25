package object;

import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * The banana object the player have to pick to win
 * contain the image of the banana, and if the collision is enabled
 * @author BOUGRAIN Nathan, HENRIQUES Valentin
 */
public class OBJ_Banana extends SuperObject{
    public OBJ_Banana(){
        name = "banana";

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/banana.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        collision = true;     
    }
}