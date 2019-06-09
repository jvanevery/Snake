/**
 * @author jordan vanevery
 * enum for cardinal directions
 */
public enum Directions{
    UP    (0,1), 
    DOWN  (0,-1), 
    LEFT  (-1,0), 
    RIGHT (1,0);

    //Displacement in x when step occurs in this direction
    private final int xStep;
    //Displacement in y when step occurs in this direction
    private final int yStep;

    Directions(int xStep, int yStep){
        this.xStep = xStep;
        this.yStep = yStep;
    }

    /** Return row step amount */
    public int getXStep() { return xStep; }
    /** Return column step amount */
    public int getYStep() { return yStep; }

    /** 
     * Return the direction clockwise to the given direction 
     * @param dir Reference direction to turn right relative to.
     * @return Direction to the right of reference direction
     * */
    static public Directions turnRight(Directions dir){
        Directions newDir = null;
        switch(dir){
            case UP:    
                newDir = RIGHT;
                break;
            case RIGHT: 
                newDir = DOWN;
                break;
            case DOWN:
                newDir = LEFT;
                break;
            case LEFT:
                newDir = UP;
                break;
        }
        return newDir;
    }

    /** 
     * Return the direction counterclockwise to the given direction 
     * @param dir Reference direction to turn left relative to.
     * @return Direction to the left of reference direction
     * */
    static public Directions turnLeft(Directions dir){
        Directions newDir = null;
        switch(dir){
            case UP: 
                newDir = LEFT;
                break;
            case LEFT:
                newDir = DOWN;
                break;
            case DOWN:
                newDir = RIGHT;
                break;
            case RIGHT:
                newDir = UP;
                break;
        }
        return newDir;
    }

    /** Return reverse direction of the direction supplied
     *  @param dir Direction to be reversed
     *  @return Reverse direction of dir
     *  Ex: reverseDirection(UP) = DOWN
     */
    static public Directions reverseDirection(Directions dir){
        Directions newDir = null;
        switch(dir){
            case UP:
                newDir = DOWN;
                break;
            case DOWN:
                newDir = UP;
                break;
            case RIGHT:
                newDir = LEFT;
                break;
            case LEFT:
                newDir = RIGHT;
                break;
        }
        return newDir;

    }

    /** 
     * Return true if the nextDir can be obtained by turning left or right 90
     * degrees, or by not turning at all, return false otherwise
     * @param currDir Original direction before proposed turn
     * @param nextDir Proposed next direction that is checked for compatibility
     * @return Boolean denoting whether or not the directions are compatible
     */
    static public boolean isValidTurn(Directions currDir, Directions nextDir){
        boolean isValidTurn = true;
        if( currDir == reverseDirection( nextDir ) ){ isValidTurn = false; }
        return isValidTurn;
    }
}
