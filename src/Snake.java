import java.util.*;

/**
 * @author jordan vanevery
 *
 * Snake consists of a Coordinate representing the snake head, and a set of
 * other coordinates representing the snake body. Snake is the game object whose
 * position can be modified and that can interact with walls, food, and itself.
 */

class Snake implements GameObject{
    
    /** Snake coordinates (including head, a type of Coordinate) */
    private List<Coordinate> positions;

    /** Snake length (including head) */
    private int snakeLength;

    /** excess length of snake, for growing snake from tail*/
    private int excessLength;

    /** Class for parts of Snake that aren't the head */
    class SnakeBlock extends Coordinate{

        public SnakeBlock(Coordinate pos){ super( pos ); }
        public SnakeBlock(int x, int y){ super(x,y); }

        @Override
        public String toString(){ return "s"; }
    }

    //Class for Snake head
    class SnakeHead extends Coordinate{

        public Directions dir; 

        public SnakeHead(Coordinate pos, Directions dir){ 
            super( pos ); 
            this.dir = dir;
        }
        public SnakeHead(int x, int y, Directions dir){ 
            super(x,y); 
            this.dir = dir;
        }

        @Override 
        public String toString(){ return "S"; }
    };

    /** Snake head for easy access */
    private SnakeHead head;



    /** Constructs initial snake
     *  @param snakeHead Coordinate for snake head
     *  @param startDir  Direction snake will head in first 
     *  @param length    Excess length of snake that will 'unroll' from start
     **/
    public Snake(Coordinate headPosition, Directions startDir, int length){

        this.excessLength = length;
        head = new SnakeHead( headPosition, startDir );
        this.positions = new LinkedList<Coordinate>();
        this.positions.add( head );
        this.snakeLength = 1;

    }


    //Moves snake according to the direction specified in snakeHead
    public void move(){
        //TODO Do collision detection in separate method
        //Create container for next coordinates of snake blocks        
        Coordinate targetCoor = new Coordinate((int)head.getX(), 
                                               (int)head.getY());

        //Reference for changing snake block locations
        Coordinate nextCoorToMove = head;

        //Move head
        head.translate(head.dir.getXStep(), head.dir.getYStep());

        //Have all snake blocks 'follow the leader'
        Coordinate temp = new Coordinate( targetCoor );      
        for(int i=1; i<snakeLength; ++i){
            nextCoorToMove = positions.get(i);

            temp.setLocation( nextCoorToMove );
            nextCoorToMove.setLocation( (int)targetCoor.getX(), 
                                        (int)targetCoor.getY() );

            targetCoor.setLocation(temp);
        }

        //Add snakeBlock at old tail position if excessLength not 0
        if(excessLength != 0){
            int tailX = (int) targetCoor.getX();
            int tailY = (int) targetCoor.getY();
            positions.add( new SnakeBlock( tailX, tailY ) );
            ++snakeLength;
            --excessLength;
        }
    }

    /** Set direction of snake head */
    public void setDirection(Directions newDir){
        //TODO Check for arguments that would attempt to reverse direction 
        head.dir = newDir; 
    }

    public Directions getDirection(){
        return head.dir;
    }

    /** Get Coordinate position of snake head */
    public Coordinate getSnakeHeadLocation(){ 
        return new Coordinate( (int)head.getX(), (int)head.getY() );
    }

    /** 
     * Add some length to the snake, particularly when food is encountered
     * @param length Length you wish to increase the snake by
     * */
    public void addSnakeLength(int length){ excessLength += length; }

    /**
     * Get current length of snake (including head)
     * return snakeLength
     */
    public int getLength(){ return snakeLength; }

    /** Return snake body and head Coordinate objects */
    public List<Coordinate> getCoordinates(){
        return this.positions;
    }
}
