import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;

/**
 * @author jordan vanevery
 *
 * SnakeIO offers utility methods for performing the input and output 
 * operations required for the game.
 */

public class SnakeIO{

    /** 
     * Read a path file with the expectation that it is a map file, return a
     * MapWord object that contains the data extracted from the file.
     * @param mapPath Path to extract map data from
     * @return MapWord object containing the extracted map data
     * @throws IOException
     */
    public static MapWord readMap(Path mapPath) throws IOException{

        int width = 0;
        int height = 0;
        List<Wall> walls = new ArrayList<>();

        BufferedReader reader = Files.newBufferedReader( mapPath );
        String line = null; 
        if( (line = reader.readLine()) != null ){ 
            Scanner scanIn = new Scanner( line );
            width = scanIn.nextInt();
            height = scanIn.nextInt();
            scanIn.close(); 
        }
        while( (line = reader.readLine()) != null ){
            Scanner scanIn = new Scanner( line );
            int x1 = scanIn.nextInt();
            int y1 = scanIn.nextInt();
            int x2 = scanIn.nextInt();
            int y2 = scanIn.nextInt();
            Directions buildWallDir = null;
            int length = 0;
            if( x2-x1 == 0 ){
                buildWallDir = Directions.UP;
                length = y2 - y1 + 1;
            } 
            else if( y2-y1 == 0 ){
                buildWallDir = Directions.RIGHT;
                length = x2 - x1 + 1;
            }
            else{
                throw new IOException( "Bad input in file detected, see proper usage" );
            }

            walls.add( GameManager.makeStraightWall( 
                        new Coordinate(x1,y1), buildWallDir, length ));
            scanIn.close();
        }
        return new MapWord(width, height, walls);
    } 
} 
