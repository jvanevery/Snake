import java.awt.*;
import java.util.*;
/**
 * @author jordan vanevery
 *
 * Coordinates is a class that extends Point which contains an (x,y) position
 * on a 2D grid. It also serves as the basic block by which we build
 * all other GameObjects. For example, to make the Snake Class Coordinate is
 * extended once to create a class that represents the snake head and another
 * time for pieces of the snake's body that aren't the head.
 *
 * Note: Extending point is an unnecessary redundancy, but harmless as of now.
 */

class Coordinate extends Point{
  
    /** Simple constructor initialized from another Coordinate */
    public Coordinate( Coordinate pos ){
        super( new Point(pos.x,pos.y) );
    }

    /** Constructor from two points */
    public Coordinate( int x, int y ){
        super(x,y);
    }

}
