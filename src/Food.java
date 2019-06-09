import java.util.*;

/* ****************************************************************************
 * @author jordan vanevery
 *
 * Food is a GameObject that has a single coordinate position in the game 
 * state. When a snake head encounters food the food is removed from the game
 * state and causes the snake to grow in size (occupy more coordinates)
 * ***************************************************************************/

class Food implements GameObject{

    //Food coordinate
    private Coordinate position;

    //Food ID
    private final int foodID;

    //Food value (how much it grows the snake)
    private final int value;

    //Amount that all foods at least grow the snake
    private static final int BASE_FOOD_VALUE = 3;

    class FoodBlock extends Coordinate{
        public FoodBlock(Coordinate pos){ super( pos ); }
        public FoodBlock(int x, int y){ super(x,y); } 

        @Override
        public String toString(){ return "f"; }
    }

    //Construct a new food object at specified Coordinate
    public Food( Coordinate position ){
        this.position = new FoodBlock(position);
        this.foodID=(int)(Math.random()*SnakeDisplay.NUM_FOOD_TYPES);
        this.value = foodID + BASE_FOOD_VALUE;
    }

    public List<Coordinate> getCoordinates(){
        List<Coordinate> temp = new ArrayList<Coordinate>();
        temp.add( position );
        return temp; 
    }

    public int getID(){ return foodID; }

    public int getValue(){ return value; }

}
