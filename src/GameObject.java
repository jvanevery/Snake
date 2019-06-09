import java.util.*;

/**
 * @author jordan vanevery
 *
 * GameObject is an interface that is implemented by any object that occupies
 * one or more coordinates in our game model. Currently all game objects have
 * at least one coordinate, but this definition leaves the possibility of an
 * empty Coordinate list.
 */
interface GameObject{
    List<Coordinate> getCoordinates();
}
