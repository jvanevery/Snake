import java.util.*;

/**
 * @author jordan vanevery
 *
 * Wall is a type of game object that represents a rigid (each piece of the
 * wall maintains a constant distance to another piece of the same wall) 
 * formation of objects with coordinates. When the snake head collides with 
 * any part of a wall, a game over condition is met.
 */

class Wall implements GameObject{

    /** List of wall positions */ 
    private List<Coordinate> positions;


    /** Nested class that makes Coordinates that belong to Wall unique */
    class WallBlock extends Coordinate{
        
        public WallBlock(Coordinate pos){ super( pos ); }
        public WallBlock(int x, int y){ super(x,y); }

        @Override 
        public String toString(){ return "X"; };
    }

    /**
     * Constructs a Wall given a list of Coordinates
     * @param positions Coordinates of new Wall
     */
    public Wall( List<Coordinate> positions ){
        List<Coordinate> temp = new ArrayList<>();
        for( Coordinate obj : positions ){
            temp.add( new WallBlock( obj ) );
        }
        this.positions = temp;
    }

    /** Return Coordinates */ 
    public List<Coordinate> getCoordinates(){ 
        return this.positions; 
    }

}
