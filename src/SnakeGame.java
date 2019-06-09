import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.SwingUtilities;

/**
 * @author jordan vanevery
 * Starting point for the entire application. Deals with user arguments, 
 * creates instance of SnakeFrame to start up the GUI. 
 */

public class SnakeGame{

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                Path userPath = null;
                //Check for user arguments
                if(args.length > 0){
                    userPath = Paths.get(args[0]);
                }
                SnakeFrame frame = new SnakeFrame(userPath);
                frame.setVisible(true);
            }
        });
    }
}
