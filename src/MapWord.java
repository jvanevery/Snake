import java.util.*;

/**
 * @author jordan vanevery
 *
 * MapWord is a class for storing map information. Maps files are one of the
 * ways that a game of snake can be initialized.
 */

public class MapWord{

    /** Width of map */
    public int mapWidth = 0;
    /** Height of map */
    public int mapHeight = 0;
    /** Coordinate objects representing walls */
    public List<Wall> walls;

    /**
     * Constructor for MapWord initializes the data fields required in a
     * MapWord data packet.
     * @param width Width of game grid
     * @param height Height of game grid
     * @param walls List of all walls in the game
     */
    public MapWord(int width, int height, List<Wall> walls){
        mapWidth = width;
        mapHeight = height;
        this.walls = new ArrayList<Wall>( walls );
    }
}
